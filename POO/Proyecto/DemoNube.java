import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.InputMismatchException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.UUID;

/**
 * Códigos de color ANSI agrupados en un solo lugar para no repetir
 * los escapes "\u001B[...m" por todo el código.
 */
class Colores {
    static final String RESET = "\u001B[0m";
    static final String VERDE = "\u001B[32m";
    static final String ROJO = "\u001B[31m";
    static final String AMARILLO = "\u001B[33m";
    static final String CIAN = "\u001B[36m";
    static final String AZUL = "\u001B[34m";
}

/**
 * Representa al alumno que solicita una terminal virtual.
 * Ahora los permisos se pueden modificar en caliente (bloquear /
 * desbloquear) porque el usuario vive en el RegistroUsuarios y no
 * se vuelve a crear cada vez que alguien inicia sesión.
 */
class Usuario {
    private String nombre;
    private String permisos; // "Alumno" o "Bloqueado"

    public Usuario(String nombre, String permisos) {
        this.nombre = nombre;
        this.permisos = permisos;
    }

    public String getNombre() { return nombre; }
    public String getPermisos() { return permisos; }
    public void setPermisos(String permisos) { this.permisos = permisos; }
    public boolean tieneAcceso() { return !permisos.equalsIgnoreCase("Bloqueado"); }
}

/**
 * Registro central de usuarios (alumnos). Antes cada solicitud creaba
 * un Usuario nuevo y se perdía apenas terminaba la operación; ahora
 * queda guardado en memoria Y en un archivo de texto plano
 * (usuarios_registro.txt), así que:
 *   - un alumno puede "volver a ingresar" sin registrarse de nuevo.
 *   - se puede bloquear/desbloquear a alguien y esa decisión persiste
 *     aunque se cierre y se vuelva a abrir el programa.
 */
class RegistroUsuarios {
    private static final String ARCHIVO = "usuarios_registro.txt";
    private final Map<String, Usuario> usuarios = new LinkedHashMap<>();

    public RegistroUsuarios() {
        cargarDesdeArchivo();
    }

    public boolean existe(String nombre) {
        return usuarios.containsKey(nombre.toLowerCase());
    }

    public Usuario obtener(String nombre) {
        return usuarios.get(nombre.toLowerCase());
    }

    /** Registra un alumno nuevo. Si ya existe, simplemente lo devuelve. */
    public Usuario registrar(String nombre) {
        if (existe(nombre)) {
            return obtener(nombre);
        }
        Usuario nuevo = new Usuario(nombre, "Alumno");
        usuarios.put(nombre.toLowerCase(), nuevo);
        guardarEnArchivo();
        return nuevo;
    }

    public boolean bloquear(String nombre) {
        Usuario u = obtener(nombre);
        if (u == null) return false;
        u.setPermisos("Bloqueado");
        guardarEnArchivo();
        return true;
    }

    public boolean desbloquear(String nombre) {
        Usuario u = obtener(nombre);
        if (u == null) return false;
        u.setPermisos("Alumno");
        guardarEnArchivo();
        return true;
    }

    public Collection<Usuario> listar() {
        return usuarios.values();
    }

    private void cargarDesdeArchivo() {
        File archivo = new File(ARCHIVO);
        if (!archivo.exists()) return;

        try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                if (linea.trim().isEmpty()) continue;
                String[] partes = linea.split(",", 2);
                if (partes.length == 2) {
                    usuarios.put(partes[0].toLowerCase(), new Usuario(partes[0], partes[1]));
                }
            }
        } catch (IOException e) {
            System.out.println(Colores.ROJO + "No se pudo cargar el registro de usuarios: " + e.getMessage() + Colores.RESET);
        }
    }

    private void guardarEnArchivo() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(ARCHIVO))) {
            for (Usuario u : usuarios.values()) {
                bw.write(u.getNombre() + "," + u.getPermisos());
                bw.newLine();
            }
        } catch (IOException e) {
            System.out.println(Colores.ROJO + "No se pudo guardar el registro de usuarios: " + e.getMessage() + Colores.RESET);
        }
    }
}

/**
 * Objeto Sesión: guarda el Token de autenticación, la IP del cliente
 * simulada y el instante en que se inició la conexión, para poder
 * calcular cuánto tiempo lleva conectada una instancia.
 */
class Sesion {
    private String token;
    private String ipCliente;
    private long inicioMillis;

    public Sesion() {
        this.token = generarToken();
        this.ipCliente = generarIpSimulada();
        this.inicioMillis = System.currentTimeMillis();
    }

    private String generarToken() {
        return "TKN-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }

    private String generarIpSimulada() {
        int ultimoOcteto = 2 + (int) (Math.random() * 250);
        return "192.168.1." + ultimoOcteto;
    }

    public String getToken() { return token; }
    public String getIpCliente() { return ipCliente; }

    public long getTiempoConectadoSegundos() {
        return (System.currentTimeMillis() - inicioMillis) / 1000;
    }

    public String resumen() {
        return "Token: " + token + " | IP: " + ipCliente;
    }
}

/**
 * Representa una terminal virtual ya asignada a un usuario.
 * Ahora guarda además el nombre del dueño (propietario) para que el
 * panel de control diga de quién es cada instancia, y no solo su ID.
 */
class InstanciaTerminal {
    private int id;
    private String propietario;
    private int ramAsignada;
    private int cpuAsignada; // en vCPU (núcleos virtuales)
    private boolean conectada;
    private Sesion sesion;

    public InstanciaTerminal(int id, String propietario, int ramAsignada, int cpuAsignada) {
        this.id = id;
        this.propietario = propietario;
        this.ramAsignada = ramAsignada;
        this.cpuAsignada = cpuAsignada;
        this.conectada = false;
        this.sesion = new Sesion();
    }

    public int getId() { return id; }
    public String getPropietario() { return propietario; }
    public int getRamAsignada() { return ramAsignada; }
    public int getCpuAsignada() { return cpuAsignada; }
    public boolean estaConectada() { return conectada; }

    /** Abre el "túnel SSH" simulado y arranca la sesión. */
    public void conectar() {
        this.conectada = true;
        System.out.println("    [Terminal #" + id + "] Túnel SSH abierto. " + sesion.resumen());
    }

    /** Simula la ejecución de un comando dentro de esta terminal. */
    public void ejecutar(String comando) {
        if (!conectada) {
            System.out.println(Colores.ROJO + "No se puede ejecutar: la instancia #" + id + " no está conectada." + Colores.RESET);
            return;
        }
        System.out.println("    terminal" + id + "$ " + comando);
        try {
            Thread.sleep(300);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        System.out.println(Colores.CIAN + "    (salida simulada — aquí el servidor real correría el comando con bash)" + Colores.RESET);
    }

    /** Cierra la sesión SSH simulada. El recurso se libera desde GestorNube. */
    public void desconectar() {
        this.conectada = false;
    }

    public void mostrarEstado() {
        String estado = conectada
                ? Colores.VERDE + "ACTIVO" + Colores.RESET
                : Colores.ROJO + "CERRADO" + Colores.RESET;
        System.out.println("  -> [Terminal ID " + id + "] Dueño: " + Colores.AZUL + propietario + Colores.RESET
                + " | Estado: " + estado
                + " | RAM: " + ramAsignada + " MB | CPU: " + cpuAsignada + " vCPU"
                + " | " + sesion.resumen() + " | Conectada hace " + sesion.getTiempoConectadoSegundos() + "s");
    }
}

/**
 * Objeto Recurso: encapsula las métricas de hardware de la "nube"
 * (RAM y CPU totales y disponibles) y las reglas de reserva/liberación.
 */
class Recurso {
    private int ramTotal;
    private int ramDisponible;
    private int cpuTotal;
    private int cpuDisponible;

    public Recurso(int ramTotal, int cpuTotal) {
        this.ramTotal = ramTotal;
        this.ramDisponible = ramTotal;
        this.cpuTotal = cpuTotal;
        this.cpuDisponible = cpuTotal;
    }

    public boolean hayDisponibilidad(int ramNecesaria, int cpuNecesaria) {
        return ramDisponible >= ramNecesaria && cpuDisponible >= cpuNecesaria;
    }

    public void reservar(int ram, int cpu) {
        ramDisponible -= ram;
        cpuDisponible -= cpu;
    }

    public void liberar(int ram, int cpu) {
        ramDisponible += ram;
        cpuDisponible += cpu;
    }

    public int getRamTotal() { return ramTotal; }
    public int getRamDisponible() { return ramDisponible; }
    public int getCpuTotal() { return cpuTotal; }
    public int getCpuDisponible() { return cpuDisponible; }
}

/**
 * Administra los recursos de la "nube" (vía Recurso) y el ciclo de vida
 * completo de las instancias: crearlas, ejecutar comandos en ellas y
 * liberarlas. Ya no crea Usuarios sueltos: recibe uno que viene del
 * RegistroUsuarios, así el historial de quién pidió qué queda coherente.
 */
class GestorNube {
    private Recurso recurso;
    private ArrayList<InstanciaTerminal> instanciasActivas;
    private int contadorIds;

    public GestorNube(int ramTotal, int cpuTotal) {
        this.recurso = new Recurso(ramTotal, cpuTotal);
        this.instanciasActivas = new ArrayList<>();
        this.contadorIds = 1;
    }

    public void crearInstancia(Usuario u, int ramNecesaria, int cpuNecesaria) {
        System.out.println("\n[GestorNube] Procesando solicitud para el usuario: " + u.getNombre() + "...");
        simularRetrasoRed();

        if (!u.tieneAcceso()) {
            System.out.println(">>> " + Colores.ROJO
                    + "ACCESO DENEGADO: usuario sin permisos (simulación Capa 2 - Seguridad)." + Colores.RESET);
            return;
        }

        if (ramNecesaria <= 0 || cpuNecesaria <= 0) {
            System.out.println(">>> " + Colores.ROJO + "ERROR: la RAM y la CPU solicitadas deben ser mayores a 0." + Colores.RESET);
            return;
        }

        if (recurso.hayDisponibilidad(ramNecesaria, cpuNecesaria)) {
            recurso.reservar(ramNecesaria, cpuNecesaria);
            InstanciaTerminal nuevaTerminal = new InstanciaTerminal(contadorIds++, u.getNombre(), ramNecesaria, cpuNecesaria);
            nuevaTerminal.conectar();
            instanciasActivas.add(nuevaTerminal);
            System.out.println(">>> SUCCESS: " + Colores.VERDE
                    + "¡Instancia virtual #" + nuevaTerminal.getId() + " asignada con éxito!" + Colores.RESET);
        } else {
            String motivo = recurso.getRamDisponible() < ramNecesaria ? "RAM" : "CPU";
            System.out.println(">>> ERROR: " + Colores.ROJO
                    + "Recursos insuficientes (" + motivo + "). Conexión rechazada." + Colores.RESET);
        }
    }

    public void ejecutarComando(int id, String comando) {
        InstanciaTerminal instancia = buscarInstancia(id);
        if (instancia == null) {
            System.out.println(">>> " + Colores.ROJO + "ERROR: no existe ninguna instancia activa con ID " + id + Colores.RESET);
            return;
        }
        if (comando.isEmpty()) {
            System.out.println(">>> " + Colores.ROJO + "ERROR: el comando no puede estar vacío." + Colores.RESET);
            return;
        }
        instancia.ejecutar(comando);
    }

    public void liberarInstancia(int id) {
        InstanciaTerminal encontrada = buscarInstancia(id);
        if (encontrada == null) {
            System.out.println(">>> " + Colores.ROJO + "ERROR: no existe ninguna instancia activa con ID " + id + Colores.RESET);
            return;
        }

        encontrada.desconectar();
        recurso.liberar(encontrada.getRamAsignada(), encontrada.getCpuAsignada());
        instanciasActivas.remove(encontrada);
        System.out.println(">>> " + Colores.AMARILLO + "Instancia #" + id + " liberada. Recursos devueltos a la nube." + Colores.RESET);
    }

    private InstanciaTerminal buscarInstancia(int id) {
        for (InstanciaTerminal t : instanciasActivas) {
            if (t.getId() == id) return t;
        }
        return null;
    }

    private void simularRetrasoRed() {
        System.out.print("    Conectando con el servidor");
        for (int i = 0; i < 3; i++) {
            try {
                Thread.sleep(350);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            System.out.print(".");
            System.out.flush();
        }
        System.out.println();
    }

    public void mostrarPanel() {
        System.out.println("\n================ PANEL DE CONTROL (BACKEND SERVIDOR) ================");
        System.out.println(" RAM: " + recurso.getRamDisponible() + "/" + recurso.getRamTotal() + " MB disponible"
                + "  |  CPU: " + recurso.getCpuDisponible() + "/" + recurso.getCpuTotal() + " vCPU disponible");
        System.out.println(" Instancias activas: " + instanciasActivas.size());
        for (InstanciaTerminal t : instanciasActivas) {
            t.mostrarEstado();
        }
        System.out.println("=====================================================================");
    }
}

public class DemoNube {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        GestorNube servidorUNI = new GestorNube(4096, 4); // 4096 MB de RAM, 4 vCPU
        RegistroUsuarios registro = new RegistroUsuarios();

        System.out.println(Colores.CIAN + "=== SISTEMA DE TERMINALES EN LA NUBE INICIADO ===" + Colores.RESET);
        System.out.println(Colores.CIAN + "(Se cargaron " + registro.listar().size() + " usuario(s) desde usuarios_registro.txt)" + Colores.RESET);

        boolean continuar = true;
        while (continuar) {
            servidorUNI.mostrarPanel();
            System.out.println("\n[MENÚ DE LA DEMO EN VIVO]");
            System.out.println("1. Registrar nuevo alumno");
            System.out.println("2. Iniciar sesión y solicitar Instancia Virtual");
            System.out.println("3. Ejecutar un comando en una Instancia activa");
            System.out.println("4. Liberar una Instancia (Alumno se desconecta)");
            System.out.println("5. Bloquear a un alumno");
            System.out.println("6. Desbloquear a un alumno");
            System.out.println("7. Ver alumnos registrados");
            System.out.println("8. Salir de la simulación");

            int opcion = leerEntero(scanner, "Seleccione una opción: ");

            switch (opcion) {
                case 1: {
                    String nombre = leerTexto(scanner, "Ingrese nombre del nuevo alumno: ");
                    if (registro.existe(nombre)) {
                        System.out.println(Colores.AMARILLO + "Ese alumno ya estaba registrado; usa la opción 2 para iniciar sesión." + Colores.RESET);
                    } else {
                        registro.registrar(nombre);
                        System.out.println(Colores.VERDE + "Alumno '" + nombre + "' registrado con éxito." + Colores.RESET);
                    }
                    break;
                }
                case 2: {
                    String nombre = leerTexto(scanner, "Ingrese su nombre de alumno: ");
                    Usuario u = registro.obtener(nombre);
                    if (u == null) {
                        System.out.println(Colores.AMARILLO + "No estabas registrado, te registro automáticamente..." + Colores.RESET);
                        u = registro.registrar(nombre);
                    } else {
                        System.out.println(Colores.CIAN + "Bienvenido de nuevo, " + u.getNombre() + " (permisos: " + u.getPermisos() + ")." + Colores.RESET);
                    }
                    int ram = leerEntero(scanner, "Ingrese RAM requerida (MB, ej: 2048): ");
                    int cpu = leerEntero(scanner, "Ingrese vCPU requeridas (ej: 1): ");
                    servidorUNI.crearInstancia(u, ram, cpu);
                    break;
                }
                case 3: {
                    int idEjecutar = leerEntero(scanner, "Ingrese el ID de la instancia donde ejecutar el comando: ");
                    String comando = leerTexto(scanner, "Ingrese el comando a simular (ej: ls -la): ");
                    servidorUNI.ejecutarComando(idEjecutar, comando);
                    break;
                }
                case 4: {
                    int idLiberar = leerEntero(scanner, "Ingrese el ID de la instancia a liberar: ");
                    servidorUNI.liberarInstancia(idLiberar);
                    break;
                }
                case 5: {
                    String nombre = leerTexto(scanner, "Ingrese nombre del alumno a bloquear: ");
                    if (registro.bloquear(nombre)) {
                        System.out.println(Colores.AMARILLO + "Alumno '" + nombre + "' bloqueado." + Colores.RESET);
                    } else {
                        System.out.println(Colores.ROJO + "No existe un alumno registrado con ese nombre." + Colores.RESET);
                    }
                    break;
                }
                case 6: {
                    String nombre = leerTexto(scanner, "Ingrese nombre del alumno a desbloquear: ");
                    if (registro.desbloquear(nombre)) {
                        System.out.println(Colores.VERDE + "Alumno '" + nombre + "' desbloqueado." + Colores.RESET);
                    } else {
                        System.out.println(Colores.ROJO + "No existe un alumno registrado con ese nombre." + Colores.RESET);
                    }
                    break;
                }
                case 7: {
                    System.out.println("\n--- Alumnos registrados ---");
                    if (registro.listar().isEmpty()) {
                        System.out.println("(todavía no hay nadie registrado)");
                    }
                    for (Usuario u : registro.listar()) {
                        String estado = u.tieneAcceso()
                                ? Colores.VERDE + u.getPermisos() + Colores.RESET
                                : Colores.ROJO + u.getPermisos() + Colores.RESET;
                        System.out.println(" - " + u.getNombre() + " (" + estado + ")");
                    }
                    break;
                }
                case 8:
                    continuar = false;
                    System.out.println("Cerrando entorno de simulación...");
                    break;
                default:
                    System.out.println(Colores.ROJO + "Opción no válida." + Colores.RESET);
            }
        }
        scanner.close();
    }

    /**
     * Lee un entero validando la entrada para que un typo en vivo
     * (muy probable si están escribiendo desde el celular) no tumbe la demo.
     */
    private static int leerEntero(Scanner scanner, String mensaje) {
        while (true) {
            System.out.print(mensaje);
            System.out.flush();
            try {
                int valor = scanner.nextInt();
                scanner.nextLine(); // limpiar el resto de la línea
                return valor;
            } catch (InputMismatchException e) {
                System.out.println(Colores.ROJO + "Por favor ingresa solo un número." + Colores.RESET);
                scanner.nextLine(); // descartar la entrada inválida
            }
        }
    }

    /** Lee una línea de texto y no acepta que quede vacía. */
    private static String leerTexto(Scanner scanner, String mensaje) {
        String valor;
        do {
            System.out.print(mensaje);
            System.out.flush();
            valor = scanner.nextLine().trim();
            if (valor.isEmpty()) {
                System.out.println(Colores.ROJO + "Este campo no puede quedar vacío." + Colores.RESET);
            }
        } while (valor.isEmpty());
        return valor;
    }
}
