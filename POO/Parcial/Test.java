import java.util.Random;
class Test{
	public static void main(String [] args){
		Random ran = new Random();
		System.out.println("Test de derivadas e integrales de Polinomios");
		PolinomioM pol = new PolinomioM(2+ran.nextInt(6));
//Tenemos el polinomio generado aleatoriamente
		System.out.println("p(x) = " + pol);
//evalua en 2
		System.out.println("p(2) = " + pol.toValue(2));
//llamamos a la funcion derivar e integrar y evaluamos en 2
		System.out.println("dp(x) = " + pol.derivar());
		System.out.println("dp(2) = " + pol.derivar().toValue(2));
		System.out.println("ip(x) = " + pol.integrar());
		System.out.println("ip(2) = " + pol.integrar().toValue(2));
//ahora integramos la derivada del polinomio y evaluamos en 2
		System.out.println("idp(x) = " + pol.derivar().integrar());
		System.out.println("idp(2) = " + pol.derivar().integrar().toValue(2));
//ahora derivamos la integral del polinomio y evaluamos en 2
		System.out.println("dip(x) = " + pol.integrar().derivar());
		System.out.println("dip(2) = " + pol.integrar().derivar().toValue(2));
	}
}
class Polinomio{
	Random ran = new Random();
	float[] coeficientes; 
	int[] exponentes; 	
	int dim;		// dimension
	Polinomio(int dim){
		this.dim = dim;
		coeficientes = new float[dim]; 
		exponentes   = new int[dim]; 
		int coef, exp = ran.nextInt(4); ;
		for(int i=0;i<dim;){
			coef = -3+ran.nextInt(8);
			if(coef!=0){
				coeficientes[i] = coef;
				exponentes[i] = exp;
				exp += 1+ran.nextInt(2); 
				i++;
			}
		}
	}

	public String toString(){
		if(dim<=0) return "No ha definido el polinomio."; 
// primer elemento
		String st = "";
		if(exponentes[0] == 0) st += coeficientes[0];
		else{
			if(coeficientes[0] < 0) st += "-";
			if(coeficientes[0] > 1) st += Math.abs(coeficientes[0]);
			st += (exponentes[0]==1)? "x": "x**" + exponentes[0] ;
		}
//  siguientes elementos
		for(int i=1;i<dim; i++){
			st += (coeficientes[i]>0)? " + ":" - ";
			if(!(coeficientes[i]==1 || coeficientes[i]==-1)) st += Math.abs(coeficientes[i]);
			st += (exponentes[i]==1)? "x": "x**" + exponentes[i] ;
		}
		return st;
	}

	int toValue(int x){
		int val=0;
		for(int i=0; i<dim; i++) val += coeficientes[i]*Math.pow(x,exponentes[i]);
		return val;
	}

	Polinomio suma(Polinomio pol){
		Polinomio pols = new Polinomio(dim+pol.dim);
		int i=0, j=0, k=0;
		float suma=0;
		while(i<dim && j<pol.dim){
			if(this.exponentes[i] == pol.exponentes[j]){
				suma = this.coeficientes[i] + pol.coeficientes[j];
				if(suma!=0){
					pols.coeficientes[k]   = suma;
					pols.exponentes  [k++] = this.exponentes[i];
				}
				i++; j++;
			} else if (this.exponentes[i] < pol.exponentes[j]){
			  	      pols.coeficientes[k  ] = this.coeficientes[i];
			 	      pols.exponentes  [k++] = this.exponentes  [i++];		
					 } else {pols.coeficientes[k]   = pol .coeficientes[j];
				    			pols.exponentes  [k++] = pol .exponentes  [j++];
				  	 }
		}
		for(;i<dim;){
			 pols.coeficientes[k]   = this.coeficientes[i];
			 pols.exponentes  [k++] = this.exponentes  [i++];
		}
		for(;j<pol.dim;){
			 pols.coeficientes[k]   = pol.coeficientes[j];
			 pols.exponentes  [k++] = pol.exponentes  [j++];
		}
//ajuste de tamaño de polinomio
		Polinomio polss = new Polinomio(k);
		for(i=0;i<k;i++){
			 polss.coeficientes[i] = pols.coeficientes[i];
			 polss.exponentes  [i] = pols.exponentes  [i];		
		}
		return polss;
	}	

Polinomio resta(Polinomio pol){
		Polinomio polr = new Polinomio(pol.dim);
		for(int i=0; i<pol.dim; i++){
			polr.coeficientes[i] = -pol.coeficientes[i]; 
			polr.exponentes  [i] =  pol.exponentes  [i]; 
		}
		return suma(polr);
	}

	Polinomio mul(Polinomio pol){	
		Polinomio polp  = new Polinomio(pol.dim);
		Polinomio polpi = new Polinomio(pol.dim);
		int i=0, j=0;
		for(; j<pol.dim;){
			polp.coeficientes[j] = coeficientes[0] * pol.coeficientes[j];
			polp.exponentes  [j] = exponentes  [0] + pol.exponentes  [j]; j++;
		}
		for(i=1; i<dim; i++){		
			for(j=0; j<pol.dim;){
				polpi.coeficientes[j] = coeficientes[i] * pol.coeficientes[j];
				polpi.exponentes  [j] = exponentes  [i] + pol.exponentes  [j];  j++;
			}
			polp = polp.suma(polpi);
		}
		return polp;		
	}
}

class PolinomioM extends Polinomio implements Matematica{
	PolinomioM(int dim){ //el constructor 
		super(dim);
	}

	public PolinomioM derivar(){
		PolinomioM polder = new PolinomioM(dim);
		for(int i=0; i<dim; i++){
//hacemos lo normal que es multiplicar el coeficiente por el exponente y restar uno al exponente
			polder.coeficientes[i] = coeficientes[i]*exponentes[i];
			polder.exponentes  [i] = exponentes  [i]-1;
		}
		return polder;
	}

	public PolinomioM integrar(){
		PolinomioM polint = new PolinomioM(dim);
		for(int i=0; i<dim; i++){
//hacemos lo normal al integrar dividimos el coeficiente entre el exponente mas uno y sumamos uno al exponente
			polint.coeficientes[i] = coeficientes[i]/(exponentes[i]+1);
			polint.exponentes  [i] = exponentes  [i]+1;
		}
		return polint;
	}
}

//declaramos la interface
interface Matematica{
	PolinomioM derivar();
	PolinomioM integrar();
}
