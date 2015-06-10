import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * @project Lbb_Kavosh
 * @author Mitra Ansari
 * @date May 18, 2011
 **/

public class TestCal {
	public static void main(String[] args) throws IOException, InterruptedException {
		// extractSo();
		Runtime runTime = Runtime.getRuntime();
		String[] s = {"chmod", "777", "/home/Mitra/Cytoscape_v2.8.1/plugins/Kavosh-SRC/Kavosh"}; // -777 " + System.getProperty("user.name");
		Process process = runTime.exec(s);
		InputStream inputStream = process.getErrorStream();
		BufferedReader bufferedReader = new BufferedReader(
				new InputStreamReader(inputStream));
		System.out.println("Error");
		String line;
		while ((line = bufferedReader.readLine()) != null) {
			System.out.println(line);
		}
		process.waitFor();
	}
	
	private static void runKavosh() {
		Runtime runTime = Runtime.getRuntime();
		try {
			String[] strings = {
					"/home/Mitra/Documents/University/LBB/Kavosh/Kavosh-SourceCode/Kavosh",
					"-i",
					"/home/Mitra/Documents/University/LBB/Kavosh/Kavosh-SourceCode/networks/ecoli",
					"-s", "5", "-r", "100" };
			Process process = runTime.exec(strings);
			InputStream inputStream = process.getInputStream();
			BufferedReader bufferedReader = new BufferedReader(
					new InputStreamReader(inputStream));
			System.out.println("Error");
			String line;
			while ((line = bufferedReader.readLine()) != null) {
				System.out.println(line);
			}
			process.waitFor();
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(e.getMessage());
		}

	}

	private static void extractSo() {
		String currentDir = "/home/Mitra/Cytoscape_v2.8.1/plugins/";
		try {
			JarFile jarFile = new JarFile(currentDir + "kavosh-1.0.jar");
			JarEntry file = (JarEntry) jarFile.getEntry("libjkavosh.so");
			java.io.File f = null;
			if (file.getName().equalsIgnoreCase("libjkavosh.so")) {
				f = new java.io.File(currentDir + java.io.File.separator
						+ file.getName());
				// if (file.isDirectory()) { // if its a directory, create
				// it
				// f.mkdir();
				// continue;
				java.io.InputStream is = jarFile.getInputStream(file);
				java.io.FileOutputStream fos = new java.io.FileOutputStream(f);
				while (is.available() > 0) { // write contents of 'is' to 'fos'
					fos.write(is.read());
				}
				fos.close();
				is.close();
			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
