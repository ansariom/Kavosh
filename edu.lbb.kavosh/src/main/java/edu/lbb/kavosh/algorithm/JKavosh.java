/**
@project Lbb_Kavosh
@author Mitra Ansari
@date Jun 24, 2011
 **/
package edu.lbb.kavosh.algorithm;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import cytoscape.logger.CyLogger;
import edu.lbb.kavosh.algorithm.data.Graph;
import edu.lbb.kavosh.algorithm.data.Parameters;
import edu.lbb.kavosh.data.common.FileUtility;
import edu.lbb.kavosh.data.common.Motif;
import edu.lbb.kavosh.data.common.Utility;

public class JKavosh {

	protected static CyLogger logger = CyLogger.getLogger(JKavosh.class);
	private static final String CURRENT_DIR = FileUtility.getCurrentDir()
			+ Parameters.PLUGIN_DIR;
	private static final String RESULT_PATH = CURRENT_DIR
			+ Parameters.RESULT_DIR;
	private Writer inputFile;
	private String inFileName;
	private int motifSize;
	private HashMap<Long, Motif> motifHash = new HashMap<Long, Motif>();
	private HashMap<String, Long> idHash = new HashMap<String, Long>();
	private static ThreadPoolExecutor poolExecutor;
	public static int threadNum = 0;

	static {
		 extractSo();
		poolExecutor = new ThreadPoolExecutor(10, 20, 10, TimeUnit.SECONDS,
				new ArrayBlockingQueue<Runnable>(10));
	}

	public List<Motif> getMotifList() {
		List<Motif> motifList = new ArrayList<Motif>();
		List<Motif> tempList = new ArrayList<Motif>();
		tempList.addAll(motifHash.values());
		for (Motif motif : tempList) {
			if (motif.getFrequency() >= Parameters.getInstance().getFreqLimit()
					&& motif.getzScore() >= Parameters.getInstance()
							.getzScoreLimit())
				motifList.add(motif);
		}
		sortMotifs(motifList);
		return motifList;
	}

	public void sortMotifs(List<Motif> motifs) {
		for (int i = 1; i < motifs.size(); i++) {
			int j = i;
			Motif pr = motifs.get(i);
			while ((j > 0) && (motifs.get(j - 1).getNumber() > pr.getNumber())) {
				motifs.set(j, motifs.get(j - 1));
				j--;
			}
			motifs.set(j, pr);
		}
	}

	public JKavosh(Graph graph) {
		this.motifSize = Parameters.getInstance().getMotifSize();
		try {
			inFileName = CURRENT_DIR + Parameters.LIN_DIR_SEP
					+ Parameters.IN_FILE_NAME;
			inputFile = new BufferedWriter(new FileWriter(new File(inFileName)));
			inputFile.write(String.valueOf(graph.getGraphSize()));
			for (int i = 1; i <= graph.getGraphSize(); i++) {
				for (int j = 1; j <= graph.getGraphSize(); j++) {
					if (i == j)
						continue;
					if (graph.isConnected(i, j))
						inputFile.write("\n" + i + " " + j);
				}
			}
			inputFile.close();
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
		}
	}

	public JKavosh() {
		// TODO Auto-generated constructor stub
	}

	public void startKavosh() {
		try {
//			setPermission();
//			removeResults();
//			removeResults_win();
//			createResult();
//			createResult_win();
			FileUtility.deleteDirectory(CURRENT_DIR + Parameters.RESULT_DIR);
			FileUtility.createDirectory(CURRENT_DIR + Parameters.RESULT_DIR);
			executeKavosh();
			System.out.println("------1");
			fillResult();
			System.out.println("------2");
			fillSubGraphs();
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage());
		}
	}

	public class SubGraphFilter implements FilenameFilter {

		@Override
		public boolean accept(File dir, String name) {
			return name.startsWith("subGraph");
		}

	}

	private void fillResult() {
		System.out.println("Fill Results Start ...");
		try {
			InputStream inputStream = new FileInputStream(new File(RESULT_PATH
					+ File.separatorChar + "adjMatrix.txt"));
			BufferedReader bufferedReader = new BufferedReader(
					new InputStreamReader(inputStream));
			String line;
			StringBuilder matrix = new StringBuilder("");
			long i = 1;
			while ((line = bufferedReader.readLine()) != null) {
				if (!line.startsWith("ID"))
					matrix.append(line);
				else {
					Motif motif = new Motif(Long.valueOf(line.substring(4))
							.longValue(), matrix.toString().toCharArray(), 0);
					motif.setNumber(i++);
					motifHash.put(motif.getId(), motif);
					idHash.put(motif.toString(), motif.getId());
					matrix.replace(0, matrix.length(), "");
					bufferedReader.readLine();
				}
			}
			inputStream.close();
			bufferedReader.close();
			inputStream = new FileInputStream(new File(RESULT_PATH
					+ File.separatorChar+ "Frequencies.txt"));
			bufferedReader = new BufferedReader(new InputStreamReader(
					inputStream));
			matrix = new StringBuilder("");
			bufferedReader.readLine();
			bufferedReader.readLine();
			bufferedReader.readLine();
			while ((line = bufferedReader.readLine()) != null) {
				String[] items = line.split("\t\t");
				long id = Long.valueOf(items[1].trim()).longValue();
				long count = Long.valueOf(items[2].trim()).longValue();
				String fStr = items[3].trim();
				double freq = Double.valueOf(
						fStr.substring(0, fStr.length() - 1)).doubleValue();
				Motif motif = motifHash.get(id);
				motif.setCount(count);
				motif.setFrequency(freq);
			}
			inputStream.close();
			bufferedReader.close();
			inputStream = new FileInputStream(new File(RESULT_PATH
					+ File.separatorChar + "ZScore.txt"));
			bufferedReader = new BufferedReader(new InputStreamReader(
					inputStream));
			matrix = new StringBuilder("");
			bufferedReader.readLine();
			bufferedReader.readLine();
			bufferedReader.readLine();
			while ((line = bufferedReader.readLine()) != null) {
				String[] items = line.split("\t\t");
				long id = Utility.getNumericValue(items[0]);
				double zscore = 0;
				try {
					zscore = Double.valueOf(items[4].trim()).doubleValue();
				} catch (Exception e) {
					e.printStackTrace();
				}
				Motif motif = motifHash.get(id);
				motif.setzScore(zscore);
			}
			inputStream.close();
			bufferedReader.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("Fill Results ENd ...");
	}

	private void fillSubGraphs() {
		System.out.println("Fill subGraphs Start ...");
		File dir = new File(RESULT_PATH);
		String[] list = dir.list(new SubGraphFilter());
		for (int i = 0; i < list.length; i++) {
			poolExecutor.execute(new FileReaderThread(RESULT_PATH + Parameters.LIN_DIR_SEP
					+ list[i], this));
		}
		System.out.println("Fill subGraphs End ...");
	}

	private void executeKavosh() throws InterruptedException, IOException {
		System.out.println("Exe 1 .....");
		Runtime runTime = Runtime.getRuntime();
		String[] strings = { CURRENT_DIR + Parameters.KAVOSH_EXE_PATH, "-i",
				inFileName, "-s", String.valueOf(motifSize), "-r",
				String.valueOf(Parameters.getInstance().getRandSize()), "-o",
				RESULT_PATH };
		Process process = runTime.exec(strings);
		InputStream inputStream = process.getInputStream();
		BufferedReader bufferedReader = new BufferedReader(
				new InputStreamReader(inputStream));
		String line;
		while ((line = bufferedReader.readLine()) != null) {
			System.out.println(line);
		}
		process.waitFor();
		System.out.println("Exe WWWWW .....");

	}

	private void removeResults() throws IOException, InterruptedException {
		Runtime runTime = Runtime.getRuntime();
		String[] s = { "rm", "-f", "-r", CURRENT_DIR + Parameters.RESULT_DIR };
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
	
	private void createResult() throws IOException, InterruptedException {
		Runtime runTime = Runtime.getRuntime();
		String[] s = { "mkdir", CURRENT_DIR + Parameters.RESULT_DIR };
		Process process = runTime.exec(s);
		InputStream inputStream = process.getErrorStream();
		BufferedReader bufferedReader = new BufferedReader(
				new InputStreamReader(inputStream));
		String line;
		while ((line = bufferedReader.readLine()) != null) {
		}
		process.waitFor();
	}
	
	private void setPermission() throws IOException, InterruptedException {
		Runtime runTime = Runtime.getRuntime();
		String[] s = { "chmod", "777", CURRENT_DIR + Parameters.KAVOSH_EXE_PATH }; // -777
																					// " + System.getProperty("user.name");
		Process process = runTime.exec(s);
		InputStream inputStream = process.getErrorStream();
		BufferedReader bufferedReader = new BufferedReader(
				new InputStreamReader(inputStream));
		String line;
		while ((line = bufferedReader.readLine()) != null) {
			System.out.println(line);
		}
		process.waitFor();
	}

	private static void extractSo() {
		String currentDir = new File(".").getAbsolutePath()
				+ Parameters.PLUGIN_DIR + Parameters.LIN_DIR_SEP;
		try {
			JarFile jarFile = new JarFile(currentDir
					+ Parameters.KAVOSH_JAR_NAME);
			Enumeration<JarEntry> enums = jarFile.entries();
			while (enums.hasMoreElements()) {
				JarEntry file = (JarEntry) enums.nextElement();
				if (file.getName().startsWith("Kavosh-SRC")) {
					File f = new java.io.File(currentDir
							+ java.io.File.separator + file.getName());
					if (file.isDirectory()) {
						f.mkdir();
						continue;
					}
					java.io.InputStream is = jarFile.getInputStream(file);
					java.io.FileOutputStream fos = new java.io.FileOutputStream(
							f);
					while (is.available() > 0) {
						fos.write(is.read());
					}
					fos.close();
					is.close();
				}
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public HashMap<Long, Motif> getMotifHash() {
		return motifHash;
	}

	public HashMap<String, Long> getIdHash() {
		return idHash;
	}

	public static void main(String[] args) {
//		try {
//			new JKavosh().createResult();
//			new JKavosh().removeResults();
//		} catch (IOException e) {
//			e.printStackTrace();
//		} catch (InterruptedException e) {
//			e.printStackTrace();
//		}
//		String idStr = s.matches("*");
//		System.out.println(idStr);
	}

}
