import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

/**
@project Lbb_Kavosh
@author Mitra Ansari
@date Jun 19, 2011
 **/

public class Compare {
	public static void main(String[] args) throws IOException {
		BufferedReader reader2 = new BufferedReader(new FileReader("/home/Mitra/Public/workspace/Personal-workspace/edu.lbb.kavosh/adjMatrix.txt"));
		BufferedReader reader = new BufferedReader(new FileReader("/home/Mitra/Cytoscape_v2.8.1/adjMatrix.txt"));
		String s1 = reader.readLine();
		int i = 1;
		while (s1 != null) {
			String s2 = reader2.readLine();
			if (!s1.equalsIgnoreCase(s2)) {
				System.out.println("Conflict ... " + i + " " + s1 + "\t " + s2);
			}
			s1 = reader.readLine();
			i++;
		}
	}
}	
