import java.util.Random;
public class Test{
	public static void main(String [] args){
		Random ran = new Random();
		System.out.println("Test de Polinomios");
   	Polinomio pol1 = new Polinomio(2+ran.nextInt(10));
	System.out.println("pol1(x) = " + pol1.toString());
        System.out.println("pol1(2) = " + pol1.toValue(2));
// reportar pol1
   	Polinomio pol2 = new Polinomio(2+ran.nextInt(6));
	System.out.println("pol2(x) = " + pol2.toString());
        System.out.println("pol2(2) = " + pol2.toValue(2));
// reportar pol2
//Llamamos al metodo Suma y sumamos los polinomios
	Polinomio polS = pol1.suma(pol2);
//Imprimimos el polinomio suma
	System.out.println("polS(x) = " + polS.toString());
//Verificamos la suma
	int poSS = pol1.toValue(2) + pol2.toValue(2);
        System.out.println("polS(2) = " + polS.toValue(2) + " = pol1(2) + pol2(2) = " + poSS);
//Llamamos al metodo Resta y restamos los polinomios
	Polinomio polR = pol1.resta(pol2);
//Imprimimos el polinomio resta
	System.out.println("polR(x) = " + polR.toString());
//Verificamos la resta
	int poRR = pol1.toValue(2) - pol2.toValue(2);
        System.out.println("polR(2) = " + polR.toValue(2) + " = pol1(2) - pol2(2) = " + poRR);
//LLamamos al metedo mul y multiplicamos los polinomios
	Polinomio polm = pol1.mul(pol2);
//imprimimos el nuevo polinomio que viene de la multiplicacion de ambos
	System.out.println("polM(x) = " + polm.toString());
//Verificamos la multiplicacion
	int poMM = pol1.toValue(2) * pol2.toValue(2);
        System.out.println("polM(2) = " + polm.toValue(2) + " = pol1(2) * pol2(2) = " + poMM);
	}
}
class Polinomio{
	Random ran = new Random();
	int[] coeficientes; 
	int[] exponentes; 	
	int dim;		// dimension
	Polinomio(int dim){
		this.dim = dim;
		coeficientes = new int[dim]; 
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
		if(exponentes[0]==0) st += coeficientes[0] + " "; 
		else { switch (coeficientes[0]){
					case  1: st += "x"; break;
					case -1: st += "-x"; break;
					default: st += coeficientes[0] + "x";
			    }
			    if(exponentes[0]>1) st += "**" + exponentes[0] + " ";
			    else st += " ";
			  } 
//  siguientes elementos
		int i;
		for(i=1;i<dim; i++){
			if(coeficientes[i]>0){			 st += "+";
				if(coeficientes[i]!=1) 		 st += coeficientes[i];
			} else if(coeficientes[i]!=-1) st += coeficientes[i];
					 else 						 st += "-";

			if(exponentes[i]==1) st += "x ";
			else					   st += "x**" + exponentes[i]+" ";
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
		int i=0, j=0, k=0, suma=0;
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
		Polinomio polr = new Polinomio(dim+pol.dim);
		int i=0, j=0, k=0, resta=0;
		while(i<dim && j<pol.dim){
			if(this.exponentes[i] == pol.exponentes[j]){
				resta = this.coeficientes[i] - pol.coeficientes[j];
				if(resta!=0){
					polr.coeficientes[k]   = resta;
					polr.exponentes  [k++] = this.exponentes[i];
				}
				i++; j++;
			} else if (this.exponentes[i] < pol.exponentes[j]){
			  	      polr.coeficientes[k  ] = this.coeficientes[i];
			 	      polr.exponentes  [k++] = this.exponentes  [i++];		
					 } else {polr.coeficientes[k]   = 0 - pol .coeficientes[j];
				    			polr.exponentes  [k++] = pol .exponentes  [j++];
				  	 }
		}
		for(;i<dim;){
			 polr.coeficientes[k]   = this.coeficientes[i];
			 polr.exponentes  [k++] = this.exponentes  [i++];
		}
		for(;j<pol.dim;){
			 polr.coeficientes[k]   = pol.coeficientes[j];
			 polr.exponentes  [k++] = pol.exponentes  [j++];
		}
//ajuste de tamaño de polinomio
		Polinomio polrr = new Polinomio(k);
		for(i=0;i<k;i++){
			 polrr.coeficientes[i] = polr.coeficientes[i];
			 polrr.exponentes  [i] = polr.exponentes  [i];		
		}
	   return polrr; 
	}
	Polinomio mul(Polinomio pol){	
// tamaño máximo posible
    		Polinomio polm = new Polinomio(this.dim * pol.dim);
    		int k = 0;

// multiplicación término a término
    		for(int i = 0; i < this.dim; i++){
        		for(int j = 0; j < pol.dim; j++){
            			int coef = this.coeficientes[i] * pol.coeficientes[j];
            			int exp  = this.exponentes[i] + pol.exponentes[j];

// buscar si ya existe ese exponente
            			boolean encontrado = false;
            			for(int t = 0; t < k; t++){
                			if(polm.exponentes[t] == exp){
                    			polm.coeficientes[t] += coef;
                    			encontrado = true;
                    			break;
                			}
            			}
// si no existe, lo agregamos
            			if(!encontrado && coef != 0){
                			polm.coeficientes[k] = coef;
                			polm.exponentes[k] = exp;
                			k++;
            			}
        		}
    		}

//Creamos un nuevo tamaño para el polinomio final
//Ya que puede que se haya generado una variable con un nuevo exponente 
//o borramos los que salgan con coeficiente 0
    		int nuevoTam = 0;
    		for(int i = 0; i < k; i++){
            		if(polm.coeficientes[i] != 0){
				polm.coeficientes[nuevoTam] = polm.coeficientes[i];
            			polm.exponentes[nuevoTam] = polm.exponentes[i];
            			nuevoTam++;
			}
    		}

// ajustamos el tamaño final
    		Polinomio polmm = new Polinomio(nuevoTam);
    		for(int i = 0; i < nuevoTam; i++){
        		polmm.coeficientes[i] = polm.coeficientes[i];
        		polmm.exponentes[i] = polm.exponentes[i];
    		}

    		return polmm;
	}
}
