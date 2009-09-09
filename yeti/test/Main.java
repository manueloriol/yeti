package yeti.test;

public class Main {
	
	private Inter inter;
	
	public Main(Inter inter) {
		this.inter = inter;
	}
	
	public void call() {
		inter.callme();
	}
}
