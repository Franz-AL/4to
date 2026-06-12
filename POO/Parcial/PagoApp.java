import java.util.Random;
import java.util.ArrayList;

public class PagoApp{
	public static void main(String[] args){
		Random ran = new Random();
		int numItems = 5 + ran.nextInt(5);
		float smc = 0;
		float smnc = 0;
		System.out.println("Pagos de condominio");
		System.out.println("Factura");
		System.out.printf("%-10s %-10s %-12s %-10s%n", "Codigo", "Nombre", "Consumible", "Monto"); 
		for(int a = 0;a<numItems;a++){
			String c = "C"+a;
			String d = "N"+a;
			Item ii = new Item(c,d);
			System.out.printf("%-10s %-10s %-12b %-10.2f%n",ii.codigo,ii.nombre,ii.consumible,ii.monto);
			if(ii.consumible){
				smc += ii.monto;
			}else{
				smnc += ii.monto;
			}
		}
		float total = smc + smnc;
		System.out.printf("%-34s %-10.2f%n","Total:", total);
		int numPagos = 4 + ran.nextInt(6);
		ArrayList<Pago> pagos = new ArrayList<>();
		int numPagosD = 0;
		Pago pp;
		for(int a = 0;a<numPagos;a++){
			boolean v;
			int y = ran.nextInt(2);
			String c = "D"+a;
			String d = "N"+a;
			if(y == 1){
				v = true;
			}else{
				v = false;
			}
			pp = new Pago(c,d,v);
			if(pp.viveD){
				numPagosD++;
			}
			pagos.add(pp);
		}
//ahora calculamos los montos
		for(int a = 0; a<numPagos;a++){
			if((pagos.get(a)).viveD){
				(pagos.get(a)).monto = smnc/numPagos + smc/numPagosD;
			}else{
				(pagos.get(a)).monto = smnc/numPagos;
			}
		}
		System.out.println("Pago");
		System.out.printf("%-15s %-10s %-10s %-10s%n", "Departamento", "Nombre", "ViveD", "Monto");
//mostramos en la salida
		float total1 = 0;// para calcular el total de pago
		for(int a = 0; a<numPagos;a++){
			Pago p = pagos.get(a);
         System.out.printf("%-15s %-10s %-10b %-10.2f%n", p.departamento, p.nombre, p.viveD, p.monto);
         total1 += p.monto;
		}
		System.out.printf("%-37s %-10.2f%n","Total:", total1);
	}
}

class Item{
	Random ran = new Random();
	String codigo;
	String nombre;
	boolean consumible;
	float monto;	
//Constructor
	Item(String codigo,String nombre){
		this.codigo = codigo;
		this.nombre = nombre;
		monto = 100.0f + (ran.nextFloat()*(20.0f));
		int a = ran.nextInt(2);
		if(a == 1){
			consumible = true;
		}else{
			consumible = false;
		}

	}

	
}

class Pago{
	Random ran = new Random();
	String departamento;
	String nombre;
	boolean viveD;
	float monto;
//Constructor
	Pago(String departamento,String nombre,boolean viveD){
		this.departamento = departamento;
		this.nombre = nombre;
		this.viveD = viveD;
	}

}
