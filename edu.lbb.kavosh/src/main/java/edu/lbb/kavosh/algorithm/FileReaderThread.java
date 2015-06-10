/**
@project Lbb_Kavosh
@author Mitra Ansari
@date Jul 9, 2011
 **/
package edu.lbb.kavosh.algorithm;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import edu.lbb.kavosh.algorithm.data.Parameters;
import edu.lbb.kavosh.data.common.Motif;
import edu.lbb.kavosh.data.common.SubGraph;

public class FileReaderThread implements Runnable {
	private String filePath;
	private JKavosh jKavosh;

	public FileReaderThread(String filePath, JKavosh jKavosh) {
		this.filePath = filePath;
		this.jKavosh = jKavosh;
	}
	@Override
	public void run() {
		InputStream inputStream;
		try {
			inputStream = new FileInputStream(new File(filePath));
			BufferedReader bufferedReader = new BufferedReader(
					new InputStreamReader(inputStream));
			String line;
			long sg = 1;
			while ((line = bufferedReader.readLine()) != null) {
				int[] nodes = new int[Parameters.getInstance().getMotifSize()];
				String[] s = line.split(" ");
				for (int j = 0; j < s.length; j++) {
					nodes[j] = Integer.valueOf(s[j]).intValue();
				}
				SubGraph subGraph = new SubGraph(sg++, nodes);
				line = bufferedReader.readLine();
				line = line.substring(0, line.length() - 1);
				synchronized (jKavosh) {
					 Motif motif = jKavosh.getMotifHash().get(jKavosh.getIdHash().get(line));
					 motif.getSubGraphs().add(subGraph);
				}
			}
			inputStream.close();
			bufferedReader.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		synchronized (jKavosh) {
			jKavosh.threadNum++;
		}
	}

}
