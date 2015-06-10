package edu.lbb.kavosh.algorithm;

import java.io.IOException;

public class Main {

	public static void main(String[] args) {
		System.out.println("java -jar kavosh-1.1.jar [motifSize] [inputNetwork] [outPutDir] [numberOfRandomNetworks]");
		try {
			new StartJKavosh()
					.Start(Integer.valueOf(args[0]), args[1], args[2], Integer.valueOf(args[3]));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
