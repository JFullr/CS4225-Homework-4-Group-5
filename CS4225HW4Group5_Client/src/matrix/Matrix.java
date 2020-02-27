package matrix;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;

import utils.FileUtils;

public class Matrix implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1955136018662799383L;
	
	private double[][] matrix;
	
	public Matrix() {
		this.matrix = null;
	}
	
	public Matrix(File file) {
		this.readFromFile(file.getPath());
	}
	
	public Matrix(String rawMatrix) {
		this.readFromString(rawMatrix);
	}
	
	
	public Matrix(int height, int width) {
		this.matrix = new double[height][width];
	}
	
	public Matrix multiply(Matrix b) {
		
		if(b.getHeight() == -1 || b.getWidth() != this.getHeight()) {
			return null;
		}
		
		Matrix eval = new Matrix(this.getHeight(),b.getWidth());
		
		
		
		for(int row = 0; row < this.getHeight(); row++) {
			
			for(int col = 0; col < b.getWidth(); col++) {
			
				double value = 0;
					
				for(int i = 0; i < b.getHeight(); i++) {
					
					value+= this.getValue(row, i)*b.getValue(i,col);
					
				}
			
				eval.setValue(row,col, value);
			
			}
			
		}
		
		
		
		return eval;
	}
	
	public int getWidth() {
		return this.matrix == null? -1 : this.matrix[0].length;
	}
	
	public int getHeight() {
		return this.matrix == null? -1 : this.matrix.length;
	}
	
	public double getValue(int y, int x) {
		return this.matrix[y][x];
	}
	
	public void setValue(int y, int x, double value) {
		this.matrix[y][x] = value;
	}
	
	public String stringify() {
		
		if(this.matrix == null) {
			return "[]";
		}
		
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
	
	
	
	
	private void readFromFile(String filePath) {
		try {
			
			String[] values = FileUtils.readCsv(filePath);
			
			this.generateMatrix(values);
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void readFromString(String rawMatrix) {
		
		rawMatrix = FileUtils.condenseNewLines(rawMatrix);
		rawMatrix = rawMatrix.replaceAll("\n", ",");
		String[] values = rawMatrix.split(",");
		
		this.generateMatrix(values);
		
	}
	
	private void generateMatrix(String[] csvValues) {
		
		String[] pieces = csvValues[0].substring(csvValues[0].indexOf(":")+1).toLowerCase().split("x");
		
		int height = Integer.parseInt(pieces[0].trim());
		int width = Integer.parseInt(pieces[1].trim());
		
		this.matrix = new double[height][width];
		
		if((csvValues.length-1)%width != 0) {
			///TODO error sparse data maybe handle case
			return;
		}
		
		for(int y = 0; y < height; y++) {
			for(int x = 0; x < width; x++) {
				this.matrix[y][x] = Double.parseDouble(csvValues[x + y*width +1].trim());
			}
		}
		
	}

}
