package yeti.stats;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class YetiDataSet {
	
	public ArrayList<Double> xVector = null;
	public ArrayList<Double> yVector = null;
	
	
	
	public YetiDataSet(ArrayList<Double> xs, ArrayList<Double> ys) {
		xVector =xs;
		yVector =ys;
		
	}
	
	double squaredResiduals (YetiEquation e) {
		double sum = 0.0;
		int max = xVector.size();
		for (int i = 0; i<max; i++) {
			double residual =  (yVector.get(i)-e.valueOf(xVector.get(i)));
			sum += residual*residual;
		}		
		return sum;
	}
	
	double coeffOfDetermination (YetiEquation e) {
		double SSerr = squaredResiduals(e);
		double total = 0;
		int max = xVector.size();
		for (int i = 0; i<max; i++) {
			total +=  yVector.get(i);
		}		
		double mean = total/max;
		
		double SStot = 0.0;
		for (int i = 0; i<max; i++) {
			double sumOfSquare =  (yVector.get(i)-mean);
			SStot += sumOfSquare*sumOfSquare;
		}		
		return 1.0-(SSerr/SStot);
	}
	
	public YetiMichaelisMentenEquation firstFitMichaelisMenten() {
		double Max = 0.0;
		double K = 0.0;
		int i=0;
		
		double fPrimeZero = yVector.get(1)/xVector.get(1);
		
		int size = xVector.size();
		for (i = 2; i<size; i++) {
			if ((yVector.get(i)/xVector.get(i))<fPrimeZero/2){
				Max = yVector.get(i)*2;
				K = xVector.get(i);
				break;
			}
		}
		
		return new YetiMichaelisMentenEquation(Max, K);
		
	}
	
	
	public YetiMichaelisMentenEquation fitMichaelisMenten() {

		YetiMichaelisMentenEquation e=firstFitMichaelisMenten();

		boolean finished = false;
		while (!finished) {
			double res = squaredResiduals(e);
			//System.out.println("Trying with: "+e);
			YetiMichaelisMentenEquation eM0 = new YetiMichaelisMentenEquation(e.getMax()+1,e.getK());
			YetiMichaelisMentenEquation em0 = new YetiMichaelisMentenEquation(e.getMax()-1,e.getK());
			YetiMichaelisMentenEquation e0M = new YetiMichaelisMentenEquation(e.getMax(),e.getK()+1);
			YetiMichaelisMentenEquation e0m = new YetiMichaelisMentenEquation(e.getMax(),e.getK()-1);
			
			if (squaredResiduals(eM0)<res) {
				e = eM0;
				continue;
			}
			if (squaredResiduals(em0)<res) {
				e = em0;
				continue;
			}
			if (squaredResiduals(e0M)<res) {
				e = e0M;
				continue;
			}
			if (squaredResiduals(e0m)<res) {
				e = e0m;
				continue;
			}
			finished = true;
		}
		
		return e;
		
	}
	
	public static void main(String []args) {
		try {
			BufferedReader br = new BufferedReader(new FileReader(args[0]));
			String s = null;
			ArrayList<Double> xVector = new ArrayList<Double>();
			ArrayList<Double> yVector = new ArrayList<Double>();
			while((s=br.readLine())!=null) {
				String []values = s.split(",");
				xVector.add(Double.parseDouble(values[0]));
				yVector.add(Double.parseDouble(values[1]));
			}
			YetiDataSet ds=new YetiDataSet(xVector,yVector);
			System.out.println("Data set read");
			YetiMichaelisMentenEquation e = ds.fitMichaelisMenten();
			System.out.println(e);
			System.out.println("R^2 = "+ds.coeffOfDetermination(e));
		} catch (FileNotFoundException e) {
			
			System.out.println("File not found: "+args[1]);
			// TODO Auto-generated catch block
			//e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}

}
