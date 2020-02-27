package matrix;

import java.io.IOException;

import utils.FileUtils;

public class Matrix {
	
	private double[][] matrix;
	
	public Matrix() {
		this.matrix = null;
	}
	
	public void readFromFile(String filePath) {
		try {
			
			String[] values = FileUtils.readCsv(filePath);
			
			this.generateMatrix(values);
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void readFromString(String rawMatrix) {
		
		rawMatrix = FileUtils.condenseNewLines(rawMatrix);
		rawMatrix = rawMatrix.replaceAll("\n", ",");
		String[] values = rawMatrix.split(",");
		
		this.generateMatrix(values);
		
	}
	
	private void generateMatrix(String[] csvValues) {
		
		String[] pieces = csvValues[0].substring(csvValues[0].indexOf(":")+1).toLowerCase().split("x");
		
		int height = Integer.parseInt(pieces[0].trim());
		int width = Integer.parseInt(pieces[1].trim());
		
		this.matrix = new double[width][height];
		
		if((pieces.length-1)%width != 0) {
			//error sparse data
			return;
		}
		
		for(int y = 0; y < this.matrix.length; y++) {
			for(int x = 0; x < this.matrix[y].length; x++) {
				this.matrix[x][y] = Double.parseDouble(csvValues[x + y*width +1]);
			}
		}
		
	}
	
	public String getString() {
		
		StringBuilder build = new StringBuilder();
		build.append("[");
		for(int y = 0; y < this.matrix.length; y++) {
			for(int x = 0; x < this.matrix[y].length; x++) {
				build.append(this.matrix[y][x]);
				if(x!=this.matrix[y].length-1) {
					build.append(", ");
				}
			}
			if(y!=this.matrix.length-1) {
				build.append("\n");
			}
		}
		build.append("]");
		return build.toString();
	}
	
}
