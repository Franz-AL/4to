import java.util.Random;

class Abuelo {
    String nombre;
    int ahorro;

    Abuelo(String nombre) {
        this.nombre = nombre;
        this.ahorro = 0;
    }

    void ahorrar(int monto) {
        ahorro += monto;
    }
}

class Papa {
    String nombre;
    int ahorro;
    Abuelo ab;

    Papa(String nombre, Abuelo ab) {
        this.nombre = nombre;
        this.ab = ab;
        this.ahorro = 0;
    }

    void ahorrar(int monto) {
        ahorro += monto;
    }

    void ahorrarAbuelo(int monto) {
        ab.ahorrar(monto);
    }
}

class Hijo {
    String nombre;
    int ahorro;
    Papa pa;

    Hijo(String nombre, Papa pa) {
        this.nombre = nombre;
        this.pa = pa;
        this.ahorro = 0;
    }

    void ahorrar(int monto) {
        ahorro += monto;
    }

    void ahorrarPapa(int monto) {
        pa.ahorrar(monto);
    }

    void ahorrarAbuelo(int monto) {
        pa.ahorrarAbuelo(monto);
    }
}

public class p1242{
    public static void main(String[] args) {
        Random rand = new Random();

        Abuelo ab = new Abuelo("Abuelo");
        Papa pa = new Papa("Papá", ab);
        Hijo hijo1 = new Hijo("Hijo1", pa);
        Hijo hijo2 = new Hijo("Hijo2", pa);

        int totalAb = 0, totalPa = 0, totalH1 = 0, totalH2 = 0;

        System.out.println("Cuota\tAbuelo\tPapá\tHijo1\tHijo2\tTotal");

        for (int i = 1; i <= 4; i++) {
            int a = rand.nextInt(10) + 1;
            int p = rand.nextInt(11) + 10;
            int h1 = rand.nextInt(11) + 30;
            int h2 = rand.nextInt(11) + 20;

            ab.ahorrar(a);
            pa.ahorrar(p);
            hijo1.ahorrar(h1);
            hijo2.ahorrar(h2);

            // relaciones
            hijo1.ahorrarPapa(2);
            hijo2.ahorrarAbuelo(1);

            int total = a + p + h1 + h2;

            totalAb += a;
            totalPa += p;
            totalH1 += h1;
            totalH2 += h2;

            System.out.println(i + "\t" + a + "\t" + p + "\t" + h1 + "\t" + h2 + "\t" + total);
        }

        int totalFinal = totalAb + totalPa + totalH1 + totalH2;

        System.out.println("Total\t" + totalAb + "\t" + totalPa + "\t" + totalH1 + "\t" + totalH2 + "\t" + totalFinal);
        System.out.println("Saldo remanente en hijo1: " + (totalH1 - 300));
        System.out.println("Felicidades por la nueva casa.");
    }
}
