package yeti.stats;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Class that represents a data transformer.
 * 
 * @author Manuel Oriol (manuel@cs.york.ac.uk)
 * @date May 7, 2011
 *
 */
public class YetiDataTransformer {

	public String []categories;

	public ArrayList<String[]>data = new ArrayList<String[]>();

	public YetiDataTransformer(String categories) {
		this.categories = categories.split(",");
	}

	public int addLine(String line) {
		data.add(line.split(","));
		return data.size();
	}

	public void print(int x,int y,int z) {
		ArrayList<String> xLabels = new ArrayList<String>();
		ArrayList<String> yLabels = new ArrayList<String>();
		
		for (String []line: data) {
			if (!xLabels.contains(line[x]))
				xLabels.add(line[x]);
			if (!yLabels.contains(line[y]))
				yLabels.add(line[y]);
		}

		sort(xLabels);
		sort(yLabels);

		int xSize=xLabels.size();
		int ySize=yLabels.size();
		
		String [][]data = new String[xSize][ySize];
		
		for (String []line: this.data) {
			int x0 = xLabels.indexOf(line[x]);
			int y0 = yLabels.indexOf(line[y]);
			data[x0][y0]=line[z];
		}

		
		for (String xLabel: xLabels) {
			System.out.print(","+xLabel);
		}
		for(int i=0;i<ySize;i++) {
			System.out.print("\n"+yLabels.get(i));
			
			for(int j=0;j<xSize;j++) {
				System.out.print(",");
				System.out.print(data[j][i]);
			}
		}
		
		
	}
	
	public ArrayList<String> sort(ArrayList<String> l){
		// change if sorting is needed
		return l;
	}

	/**
	 * @param args
	 * @throws IOException, FileNotFoundException 
	 */
	public static void main(String[] args) throws IOException, FileNotFoundException {
		if (args.length!=4) {
			printHelp();
			return;
		}
		YetiDataTransformer ydt = null;
		BufferedReader br = new BufferedReader(new FileReader(args[0]));
		String s = null;
		if ((s=br.readLine())!=null) {
			ydt = new YetiDataTransformer(s);
			while((s=br.readLine())!=null) {
				ydt.addLine(s);
			}
		}
		int xcolumn = Integer.parseInt(args[1]);
		int ycolumn = Integer.parseInt(args[2]);
		int zcolumn = Integer.parseInt(args[3]);
		
		ydt.print(xcolumn,ycolumn,zcolumn);
		
	}

	public static void printHelp() {
		System.out.println("Usage: java yeti.stats.YetiDataTransformer fileName columnNumberForX columnNumberForY columnNumberForZ");
	}
}
