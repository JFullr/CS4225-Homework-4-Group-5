package network;

import java.io.Serializable;

import matrix.Matrix;

public class MatrixEval implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 2840796658898258429L;
	
	private static final String ERROR_NONE = "No Error";
	private static final String ERROR_MISMATCH_DIMENSIONS = "Matrix Dimensions Did Not Match";
	private static final String ERROR_SPARSE_DATA = "Not Enough Matrix Data Was Given";

	private String error;
	private long processTime;
	private Matrix evaluated;
	
	private transient Matrix[] workingSet;
	
	public MatrixEval() {
		this.error = ERROR_NONE;
		this.processTime = 0;
		this.evaluated = null;
	}
	
	public MatrixEval(Matrix... matrixes) {
		this.workingSet = matrixes;
		this.process();
	}
	
	public String getError() {
		return this.error;
	}
	
	public long getTimeMilliseconds() {
		return this.processTime;
	}
	
	public Matrix getMatrix() {
		return this.evaluated;
	}

	private void process() {
		
		if(this.workingSet == null || this.workingSet.length < 2) {
			this.error = ERROR_SPARSE_DATA;
			this.processTime = 0;
			this.evaluated = null;
			
			return;
		}
		
		long start = System.currentTimeMillis();
		
		Matrix eval = this.workingSet[0];
		for(int i = 1; i < this.workingSet.length; i++) {
			eval = eval.multiply(this.workingSet[i]);
			if(eval == null) {
				this.error = ERROR_MISMATCH_DIMENSIONS;
			}
		}
		
		long end = System.currentTimeMillis();
		
		this.processTime = end-start;
		this.evaluated = eval;
		this.error = ERROR_NONE;
		
	}
	
}
