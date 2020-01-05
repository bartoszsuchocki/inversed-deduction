package logger;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;

public class Logger {
	
	public static final String STDOUT_STRING = "stdout";
	private static BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(System.out));
	private static String outputPath = STDOUT_STRING;
	
	public static void initLogger(String outputPath) throws IOException {
		Logger.outputPath = outputPath;
		if(!outputPath.equals(STDOUT_STRING)) {
			writer = new BufferedWriter(new FileWriter(outputPath));
		}
	}
	
	public static void log(String logLine) {
		if(outputPath.equals(STDOUT_STRING)) {
			System.out.println(logLine);
		} else {
			try {
				writer.write(logLine);
				writer.newLine();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public static void close() throws IOException {
		writer.close();
	}
}
