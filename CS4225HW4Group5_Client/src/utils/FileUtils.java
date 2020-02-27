package utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class FileUtils {

	public static final IOException FILE_EMPTY = new IOException("File Does Not Exist Or Is Empty");
	public static final IOException FILE_TOO_LARGE = new IOException("File Is Too Large");

	public static byte[] readFile(String filePath) throws IOException {

		File file = new File(filePath);
		long length = file.length();

		if (length == 0) {
			throw FileUtils.FILE_EMPTY;
		}

		if (length > Integer.MAX_VALUE) {
			throw FileUtils.FILE_TOO_LARGE;
		}

		byte[] data = new byte[(int) length];

		try (FileInputStream fis = new FileInputStream(file)) {

			fis.read(data);

		}

		return data;
	}

	public static String[] readLines(String filePath) throws IOException {

		String raw = new String(FileUtils.readFile(filePath));
		raw = FileUtils.condenseNewLines(raw);
		;

		return raw.split("\n");

	}

	public static String[] readCsv(String filePath) throws IOException {

		String raw = new String(FileUtils.readFile(filePath));
		raw = FileUtils.condenseNewLines(raw);
		raw = raw.replace("\n", ",");

		return raw.split(",");

	}

	public static String condenseNewLines(String value) {
		return value.replaceAll("\r", "\n").replaceAll("\n\n", "\n");
	}

}
