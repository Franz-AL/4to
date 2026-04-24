abstract class A{
	String a;
//Constructor
	A(String a){
		this.a = a;
	}
	abstract void print();
}
class C{
	String c;
	C(String c){
//Constructor
		this.c = c;
	}
}

class B extends A{
	String b;
	C c;
	B(String a,String b,C c){
		super(a);
		this.b = b;
		this.c = c;
	}
	void print(){
		System.out.println("a = '" + a + "' , b = '" + b + "' , c = '" + c + "'");
	}
}
public class TestABC{
	public static void main(String[] args){
		String a = "a";
		String d = "b";
		String e = "c";
		C c = new C(e);
		B b = new B(a,d,c);
		b.print();
	}
}
