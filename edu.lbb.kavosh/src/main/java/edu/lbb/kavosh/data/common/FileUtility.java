package edu.lbb.kavosh.data.common;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;

public class FileUtility {
	public static void createDirectory(String dir) {
		boolean success = (new File(dir)).mkdirs();
		if (!success) {
			System.err.println("Could not create Directory" + dir);
		}
	}
	
	public static void deleteDirectory(String dir) throws IOException {
		FileUtils.deleteDirectory(new File(dir));
	}
	
	public static String getCurrentDir() {
		try {
			return new File(".").getCanonicalPath();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

}
