class Persona {
    String nombre;

    // Constructor del padre
    Persona(String nombre) {
        this.nombre = nombre;
        System.out.println("Constructor Persona");
    }
}

class Estudiante extends Persona {
    int edad;

    // Constructor del hijo
    Estudiante(String nombre, int edad) {
        super(nombre); // llama al constructor del padre
        this.edad = edad;
        System.out.println("Constructor Estudiante");
    }
}

public class ejem1 {
    public static void main(String[] args) {
        Estudiante e = new Estudiante("Juan", 20);
    }
}
