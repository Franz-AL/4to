abstract class Figuras {
    abstract double area();
    abstract double perimetro();
}

class Circulo extends Figuras {
    double radio;

    Circulo(double radio) {
        this.radio = radio;
    }

    double area() {
        return Math.PI * radio * radio;
    }

    double perimetro() {
        return 2 * Math.PI * radio;
    }
}

class Rectangulo extends Figuras {
    double largo, ancho;

    Rectangulo(double largo, double ancho) {
        this.largo = largo;
        this.ancho = ancho;
    }

    double area() {
        return largo * ancho;
    }

    double perimetro() {
        return 2 * (largo + ancho);
    }
}

public class FigurasApp {
    public static void main(String[] args) {
        Circulo c = new Circulo(2);
        Rectangulo r = new Rectangulo(4, 2);

        System.out.printf("Círculo: radio = %.3f; área = %.3f, perímetro = %.3f\n",
                c.radio, c.area(), c.perimetro());

        System.out.printf("Rectángulo: largo = %.3f, ancho = %.3f; área = %.3f, perímetro = %.3f\n",
                r.largo, r.ancho, r.area(), r.perimetro());
    }
}
