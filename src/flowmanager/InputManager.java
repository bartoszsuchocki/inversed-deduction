package flowmanager;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;
import java.util.stream.Stream;

public class InputManager {
	Scanner standardInputScanner = new Scanner(System.in);
	BufferedReader reader;
	
	public void printStandardCommunicate(String communicate) {
		System.out.println(communicate);
	}
	
	public String getAnswerForStdinQuestion(String question) {
		System.out.println(question);
		return standardInputScanner.nextLine();
	}
	
	public void openFileForReading(String path) throws FileNotFoundException {
		reader = new BufferedReader(new FileReader(path));
	}
	
	public Stream<String> readLines() {
		return reader.lines();
	}
	
	public void finishReading() throws IOException {
		reader.close();
	}
}