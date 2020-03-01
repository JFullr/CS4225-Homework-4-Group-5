package matrix;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;

import utils.ErrorHandler;
import utils.FileUtils;

/**
 * The Class Matrix represents a serializable matrix which can be multiplied to
 * another.
 * 
 * @author Joseph Fuller, James Irwin, Timothy Brooks
 * @version Spring 2020
 */
public class Matrix implements Serializable {

	private static final long serialVersionUID = 1955136018662799383L;
	private static final IllegalArgumentException ERROR_INVALID_MATRIX = new IllegalArgumentException(
			"Matrix Specified Had Missing Data Or Was Imbalanced");

	/**
	 * Reads matrixes from the given file.
	 *
	 * @param file the file
	 * @return the matrix[]
	 * @throws Exception the exception
	 */
	public static Matrix[] readMatrixes(File file) throws Exception {
		String raw = new String(FileUtils.readFile(file.getPath()));
		return Matrix.readMatrixes(raw);
	}

	/**
	 * Reads matrixes from raw data.
	 *
	 * @param rawData the raw data
	 * @return the matrix[]
	 * @throws Exception the exception
	 */
	public static Matrix[] readMatrixes(String rawData) throws Exception {

		String rawMatrix = FileUtils.condenseNewLines(rawData);
		rawMatrix = rawMatrix.replaceAll("\n", ",");
		String[] values = rawMatrix.split(",");

		ArrayList<Matrix> found = new ArrayList<Matrix>();

		Matrix cur = new Matrix(values);
		int offset = 0;
		do {

			cur = new Matrix(values);
			found.add(cur);
			offset = cur.getHeight() * cur.getWidth() + 1;
			values = Arrays.copyOfRange(values, offset, values.length);

		} while (values.length > 1);

		return found.toArray(new Matrix[found.size()]);

	}

	private double[][] matrix;

	/**
	 * Instantiates a new matrix.
	 */
	public Matrix() {
		this.matrix = null;
	}

	/**
	 * Instantiates a new matrix.
	 *
	 * @param file the file
	 */
	public Matrix(File file) {
		this.readFromFile(file.getPath());
	}

	/**
	 * Instantiates a new matrix.
	 *
	 * @param rawMatrix the raw matrix
	 */
	public Matrix(String rawMatrix) {
		this.readFromString(rawMatrix);
	}

	/**
	 * Instantiates a new matrix based on the given csv values.
	 *
	 * @param matrixCsvValues the matrix csv values
	 */
	public Matrix(String[] matrixCsvValues) {
		this.readFromCsv(matrixCsvValues);
	}

	/**
	 * Instantiates a new matrix.
	 *
	 * @param height the height
	 * @param width  the width
	 */
	public Matrix(int height, int width) {
		this.matrix = new double[height][width];
	}

	/**
	 * Multiply.
	 *
	 * @param matrixB the b matrix
	 * @return the matrix
	 */
	public Matrix multiply(Matrix matrixB) {

		if (matrixB.getHeight() != this.getWidth()) {
			return null;
		}

		Matrix eval = new Matrix(this.getHeight(), matrixB.getWidth());

		for (int row = 0; row < this.getHeight(); row++) {

			for (int col = 0; col < matrixB.getWidth(); col++) {

				double value = 0;

				for (int i = 0; i < matrixB.getHeight(); i++) {

					value += this.getValue(row, i) * matrixB.getValue(i, col);

				}

				eval.setValue(row, col, value);

			}

		}

		return eval;
	}

	/**
	 * Gets the width.
	 *
	 * @return the width
	 */
	public int getWidth() {
		return this.matrix == null ? 0 : this.matrix[0].length;
	}

	/**
	 * Gets the height.
	 *
	 * @return the height
	 */
	public int getHeight() {
		return this.matrix == null ? 0 : this.matrix.length;
	}

	/**
	 * Gets the value.
	 *
	 * @param y the y
	 * @param x the x
	 * @return the value
	 */
	public double getValue(int y, int x) {
		return this.matrix[y][x];
	}

	/**
	 * Sets a specific value on the matrix specified by x and y coordinates.
	 *
	 * @param y     the y
	 * @param x     the x
	 * @param value the value
	 */
	public void setValue(int y, int x, double value) {
		this.matrix[y][x] = value;
	}

	/**
	 * Gets the storage representation.
	 *
	 * @return the storage representation
	 */
	public String getStorageRepresentation() {

		String matrixStr = this.stringify();
		matrixStr = matrixStr.substring(1, matrixStr.length() - 1);

		StringBuilder build = new StringBuilder();
		build.append("Matrix: ");
		build.append(this.getHeight());
		build.append(" x ");
		build.append(this.getWidth());
		build.append("\n");
		build.append(matrixStr);

		return build.toString();
	}

	/**
	 * Converts and returns a string representation of the data.
	 *
	 * @return the string
	 */
	public String stringify() {

		if (this.matrix == null) {
			return "[]";
		}

		StringBuilder build = new StringBuilder();
		build.append("[");
		for (int y = 0; y < this.getHeight(); y++) {
			for (int x = 0; x < this.getWidth(); x++) {

				build.append(this.matrix[y][x]);

				if (x != this.matrix[y].length - 1) {
					build.append(", ");
				}
			}

			if (y != this.matrix.length - 1) {
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
			ErrorHandler.addError("Could not read the file specified: " + filePath);
		}
	}

	private void readFromString(String rawMatrix) {

		rawMatrix = FileUtils.condenseNewLines(rawMatrix);
		rawMatrix = rawMatrix.replaceAll("\n", ",");
		String[] values = rawMatrix.split(",");

		this.readFromCsv(values);

	}

	private void readFromCsv(String[] values) {

		this.generateMatrix(values);

	}

	private void generateMatrix(String[] csvValues) throws IllegalArgumentException {

		String[] pieces = csvValues[0].substring(csvValues[0].indexOf(":") + 1).toLowerCase().split("x");

		int height = Integer.parseInt(pieces[0].trim());
		int width = Integer.parseInt(pieces[1].trim());

		this.matrix = new double[height][width];

		try {
			for (int y = 0; y < height; y++) {
				for (int x = 0; x < width; x++) {
					this.matrix[y][x] = Double.parseDouble(csvValues[x + y * width + 1].trim());
				}
			}
		} catch (Exception e) {
			ErrorHandler.addError(ERROR_INVALID_MATRIX);
			this.matrix = null;
		}
	}

}
