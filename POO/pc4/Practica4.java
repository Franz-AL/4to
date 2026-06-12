import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Date;
import java.text.DateFormat;
import java.util.Random;
import java.util.Arrays;
	
class Practica4 {
   public static void main(String[] args) {
		Random ran = new Random();
		DateFormat df = DateFormat.getDateInstance();
		GregorianCalendar [] gcs = {new GregorianCalendar(2001, 0, 21), new GregorianCalendar(2002, 4, 15),
											 new GregorianCalendar(2003, 8, 18), new GregorianCalendar(2004, 9, 19),
											 new GregorianCalendar(2005, 10, 27)};
// Lista de alumnos
		int numAlu = 3 + ran.nextInt(4); 
      Alumno[] alus = new Alumno[numAlu]; 
		System.out.println("Practica 4 de POO");
      System.out.println("Lista de alumnos");
		System.out.printf("%-6s  %-6s  %-10s\n", "Codigo", "Nombre","FechaNac.");
      for(int i = 0; i < numAlu; i++) {
//creamos al alumno
         alus[i] = new Alumno("C" + i, "N" + i, gcs[i].getTime());
         System.out.printf("%-6s  %-6s  %-10s\n", alus[i].codigo, alus[i].nombre, df.format(alus[i].fechaNac));
      }

//Ordanamiento segun los menores
		System.out.println("\nEntrada de alumnos (los menores primero)");
      Alumno.orden = 3;
      Arrays.sort(alus);
      System.out.printf("%-6s  %-6s  %-10s\n", "Codigo", "Nombre","FechaNac.");
      for(Alumno alu : alus) {
         System.out.printf("%-6s  %-6s  %-10s\n", alu.codigo, alu.nombre, df.format(alu.fechaNac));
      }
        
		PolinomioTest.main(args);
        
		for(Alumno alu : alus) {
            	alu.nota = 9 + ran.nextInt(12);  // calificación
      }
//Ordenados por nombre    
      System.out.println("\nNota de alumnos ordenados por nombre");
      Alumno.orden = 1;
      Arrays.sort(alus);
      System.out.printf("%-6s  %-6s  %-10s  %-4s\n", "Codigo", "Nombre","FechaNac.","Nota");
      for(Alumno alu : alus) {
         System.out.printf("%-6s  %-6s  %-10s  %2d\n", alu.codigo, alu.nombre, df.format(alu.fechaNac), alu.nota);
        }
        
//Llamamos funcion para ordenar los mayores salen primero
		System.out.println("\nSalida de alumnos (Los mayores salen primero)");
      Alumno.orden = 2;
      Arrays.sort(alus);
      System.out.printf("%-6s  %-6s  %-10s\n", "Codigo", "Nombre","FechaNac.");
      for(Alumno alu : alus) {
      	System.out.printf("%-6s  %-6s  %-10s\n", alu.codigo, alu.nombre, df.format(alu.fechaNac));
      }
    }
}

class Alumno implements Comparable<Alumno> {
   static int orden; // orden de sort
   String codigo;
   String nombre;
   Date fechaNac;
   int nota;

   Alumno(String codigo, String nombre, Date fechaNac) {
      this.codigo = codigo;
      this.nombre = nombre;
      this.fechaNac = fechaNac;
   }

   public int compareTo(Alumno alu) {
        switch (orden) {    
            case 1: 
                return this.nombre.compareTo(alu.nombre);
            case 2: 
                return alu.fechaNac.compareTo(this.fechaNac);
            case 3:
                return this.nombre.compareTo(alu.nombre);
            default: 
                return this.codigo.compareTo(alu.codigo);
        }
   }
}
