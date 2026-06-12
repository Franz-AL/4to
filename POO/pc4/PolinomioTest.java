class PolinomioTest{
	public static void main(String[] args){
		System.out.println("\nTest de toString() para el primer término de Polinomios");
		Polinomio p = new Polinomio(1,0);
	   System.out.println("p(x) = " + p);
	   p = new Polinomio(-1,0);
	   System.out.println("p(x) = " + p);
	   p = new Polinomio(4,0);
	   System.out.println("p(x) = " + p);
	   p = new Polinomio(-4,0);
	   System.out.println("p(x) = " + p + "\n");
        
     p = new Polinomio(1,1);	
	  System.out.println("p(x) = " + p);
	  p = new Polinomio(-1,1);
     System.out.println("p(x) = " + p);
	  p = new Polinomio(4,1);
     System.out.println("p(x) = " + p);
	  p = new Polinomio(-4,1);
     System.out.println("p(x) = " + p + "\n");
  
     p = new Polinomio(1,2);
     System.out.println("p(x) = " + p);
     p = new Polinomio(-1,2);
     System.out.println("p(x) = " + p);
     p = new Polinomio(4,2);
     System.out.println("p(x) = " + p);
     p = new Polinomio(-4,2);
     System.out.println("p(x) = " + p + "\n");  
	}
}
class Polinomio{
	float[] coeficientes; 
	int[] exponentes; 	
	int dim;		// dimension
	Polinomio(int c, int e){
		dim = 1;
		coeficientes = new float[dim]; 
		exponentes   = new int[dim];
		coeficientes[0] = c;
		exponentes[0] = e;
	}
	public String toString(){
		if(dim<=0) return "No ha definido el polinomio."; 
// guardando el primer termino
		String st;
		Polinomio p;
		switch(exponentes[0]){
			case 0: 
			st = coeficientes[0]+"";
			break;
			case 1:
			st = coeficientes[0] + "x";
			break;
			default:
			st = coeficientes[0] + "x**" + exponentes[0];
		}
		return st;
	}
}
