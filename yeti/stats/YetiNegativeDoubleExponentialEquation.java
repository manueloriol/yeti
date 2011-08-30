package yeti.stats;

public class YetiNegativeDoubleExponentialEquation extends YetiMichaelisMentenEquation {

	
	public YetiNegativeDoubleExponentialEquation(double Max, double K) {
		super(Max, K);
		// TODO Auto-generated constructor stub
	}

	@Override
	double valueOf(double x) {
		return this.getMax()*Math.exp(.000001d*x)/(1.0d-((1.0d-this.getK())*Math.exp(.000001d*x)));
	}

	 public String toString() {
		 return "f(x)="+this.getMax()+"e("+this.getK()+"*x)/(1-((1-"+this.getMax()+")*e("+this.getK()+"x)))";

	 }

}
