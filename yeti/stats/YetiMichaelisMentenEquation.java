package yeti.stats;

public class YetiMichaelisMentenEquation extends YetiEquation {

	double Max;
	double K;
	
	public YetiMichaelisMentenEquation(double Max, double K) {
		this.Max=Max;
		this.K=K;
		
	}
	@Override
	double valueOf(double x) {
		return (Max*x)/(K+x);
	}
	public double getMax() {
		return Max;
	}
	public void setMax(double max) {
		Max = max;
	}
	public double getK() {
		return K;
	}
	public void setK(double k) {
		K = k;
	}

	public String toString() {
		return "f(x)=("+Max+"*x)/("+K+"+x)";
		
	}
}
