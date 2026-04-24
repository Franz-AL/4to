import java.util.Random;

class Polinomio {

    Random ran = new Random();
    int sumandos;
    int[] coeficientes;
    int[] exponentes;

    Polinomio(int sumandos){
        this.sumandos = sumandos;

        coeficientes = new int[sumandos];
        exponentes = new int[sumandos];

        for(int i = 0; i < sumandos; i++){
            int c;
            do {
                c = ran.nextInt(8) - 3; 
            } while(c == 0);
            coeficientes[i] = c;
        }

        exponentes[0] = ran.nextInt(3); 

        for(int i = 1; i < sumandos; i++){
            exponentes[i] = exponentes[i-1] + 1 + ran.nextInt(2);
        }
    }

    public String toString(){
        String s = "";

        for(int i = 0; i < sumandos; i++){
            int c = coeficientes[i];
            int e = exponentes[i];

            if(i > 0 && c > 0){
                s += "+";
            }
            if(e == 0){
                s += c;
            } else {
                if(c == -1) s += "-";
                else if(c != 1) s += c;

                // variable
                s += "x";

                // potencia
                if(e > 1){
                    s += "**" + e;
                }
            }
        }

        return s;
    }

    // 🔹 evaluar polinomio
    int toValue(int x){
        int suma = 0;

        for(int i = 0; i < sumandos; i++){
            suma += coeficientes[i] * Math.pow(x, exponentes[i]);
        }

        return suma;
    }
}

public class Test {

    public static void main(String[] args){

        Random ran = new Random();

        System.out.println("Test de Polinomios");

        int s1 = 2 + ran.nextInt(10);
        Polinomio pol1 = new Polinomio(s1);

        System.out.println("pol1(x) = " + pol1.toString());
        System.out.println("pol1(2) = " + pol1.toValue(2));

        int s2 = 2 + ran.nextInt(6);
        Polinomio pol2 = new Polinomio(s2);

        System.out.println("pol2(x) = " + pol2.toString());
        System.out.println("pol2(2) = " + pol2.toValue(2));
    }
}
