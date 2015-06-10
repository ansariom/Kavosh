/**
@project Lbb_Kavosh
@author Mitra Ansari
@date May 10, 2011
 **/
package edu.lbb.kavosh.algorithm.data;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import edu.lbb.kavosh.data.common.Motif;

public class Graph {

	private BufferedWriter writer;
	private BufferedWriter zscore_writer;
	private int graphSize;
	private final int subGraphSize;
	private Tree tree;
	private int idxID;
	private int head = 1;
	private int nE = 0;
	private int nEd = 0;
	private int maxDegree = 0;
	private int[][] edges;
	private List<List<Integer>> e_temp;
	private char[][] adjMat;
	// private char[] adjM;
	// private HashMap<Integer, int[]> adj ;
	private int[] degree;
	private double[] mean;
	private double[] var;
	private double[] score;
	private double enumerated_class;
	private long[] ID;
	long[] c_main;
	long[] c_rand;
	private boolean isRand;
	private Writer out;
	private List<Motif> motifsList = new ArrayList<Motif>();

	public List<Motif> getMotifsList() {
		return motifsList;
	}

	public boolean isRand() {
		return isRand;
	}

	public void setRand(boolean isRand) {
		this.isRand = isRand;
	}

	public Graph(int graphSize, int subGraphSize, boolean isrand, String out_path) {
		System.out.println("g Size" + graphSize);
		this.graphSize = graphSize;
		this.subGraphSize = subGraphSize;
		this.isRand = isrand;
		init();
		try {
			writer = new BufferedWriter(new FileWriter(
					new File(out_path + Parameters.LIN_DIR_SEP + "adjMatrix.txt")));
			out = new OutputStreamWriter(new FileOutputStream("test.txt"));
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public Graph(int graphSize, int subGraphSize, boolean isrand) {
		System.out.println("g Size" + graphSize);
		this.graphSize = graphSize;
		this.subGraphSize = subGraphSize;
		this.isRand = isrand;
		init();
		try {
			writer = new BufferedWriter(new FileWriter(
					new File("adjMatrix.txt")));
//			out = new OutputStreamWriter(new FileOutputStream("test.txt"));
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void init() {
		tree = new Tree(subGraphSize);
		edges = new int[graphSize + 1][graphSize + 1];
		e_temp = new ArrayList<List<Integer>>();
		for (int i = 0; i <= graphSize; i++) {
			e_temp.add(new ArrayList<Integer>());
		}
		adjMat = new char[graphSize + 1][graphSize + 1];
		for (int i = 0; i <= graphSize; i++) {
			for (int j = 0; j <= graphSize; j++) {
				adjMat[i][j] = '0';
			}
		}
	}

	public void initData(char[][] matx) {
		for (int i = 0; i < matx.length; i++) {
			for (int j = 0; j < matx.length; j++) {
				if (i == j)
					continue;
				if (matx[i][j] == '1')
					addEdge(i, j);
			}
		}
	}

	public void addEdgeAdjMat(int u, int v) {
		adjMat[u][v] = '1';
	}

	public void deleteEdgeAdjMat(int u, int v) {
		adjMat[u][v] = '0';
	}

	public void swapEdge(int v, int ind, int u) {
		if (u < edges[v][ind]) {
			ind++;
			while (ind <= edges[v][0] && u < edges[v][ind]) {
				edges[v][ind - 1] = edges[v][ind];
				ind++;
			}
			if (ind <= edges[v][0] && u == edges[v][ind]) {
				while (ind <= edges[v][0]) {
					edges[v][ind - 1] = edges[v][ind];
					ind++;
				}
				edges[v][0]--;
			} else
				edges[v][ind - 1] = u;
		} else {
			ind--;
			while (ind > 0 && u > edges[v][ind]) {
				edges[v][ind + 1] = edges[v][ind];
				ind--;
			}
			if (ind > 0 && u == edges[v][ind]) {
				ind++;
				ind++;
				while (ind <= edges[v][0]) {
					edges[v][ind - 1] = edges[v][ind];
					ind++;
				}
				edges[v][0]--;
			} else
				edges[v][ind + 1] = u;
		}
	}

	public void addEdge(int u, int v) {
		nE++;
		if (!e_temp.get(u).contains(v))
			e_temp.get(u).add(v);
		if (!e_temp.get(v).contains(u))
			e_temp.get(v).add(u);
		adjMat[u][v] = '1';
	}

	public char[][] getAdjMat() {
		return adjMat;
	}

	public void print() {
		for (int i = 0; i < graphSize; i++) {
			System.out.print("Node " + i + " : ( " + edges[i][0] + " )  ");
			for (int j = 0; j < edges[i][0]; j++) {
				System.out.print(edges[i][j]);
			}
			System.out.println();
		}
		System.out
				.println("---------------------------------------------------------------");

	}

	public boolean isConnected(int u, int v) {
		boolean result = adjMat[u][v] == '1';
		return result;
	}

	public int[] getNeighbours(int v) {
		return edges[v];
	}

	public int get_vertex() {
		Random random = new Random();
		int ind = random.nextInt(nEd);
		return degree[ind];
	}

	public void finalizeG() throws IOException {
		int max = 0;

		degree = new int[nE];
		int degInd = 0;

		for (int i = 1; i <= graphSize; i++) {

			if (max < e_temp.get(i).size())
				max = e_temp.get(i).size();

			Collections.sort(e_temp.get(i));
			edges[i] = new int[e_temp.get(i).size() + 1];
			edges[i][0] = e_temp.get(i).size();
			for (int j = 0; j < e_temp.get(i).size(); j++) {
				edges[i][j + 1] = e_temp.get(i).get(j);
				if (isConnected(i, e_temp.get(i).get(j))) {
					degree[degInd] = i;
					degInd++;
					nEd++;
				}
			}
			e_temp.get(i).clear();
		}
		maxDegree = max;

		System.out.println("Number of Nodes: " + graphSize);
		System.out.println("Number of Edges: " + nE);
		System.out.println("Maximum Degree: " + maxDegree);
	}

	public void closeStreams() throws IOException {
		writer.close();
	}
	public int getMaxDegree() {
		return maxDegree;
	}

	public void allocateCounter() {
		int class_num = tree.getLeafNumber();
		c_main = new long[class_num + 1];
		c_rand = new long[class_num];
		c_main[0] = 0;

		mean = new double[class_num];
		var = new double[class_num];
		score = new double[class_num];

		for (int i = 0; i < class_num; i++) {
			mean[i] = 0;
			var[i] = 0;
		}

		idxID = 0;
		ID = new long[class_num];
	}
	
	public void resetIdxID() {
		this.idxID = 0;
	}

	public void printAdjMatrix(char[] str) throws IOException {

		int l = 0;
		int index = 0;
		int maxpow = subGraphSize * subGraphSize - 1;
		for (int i = 0; i < subGraphSize; i++) {
			for (int j = 0; j < subGraphSize; j++) {
				if (i == j) {
					l++;
					writer.write('0');
				} else {
					index = (i * (subGraphSize)) + (j - l);
					writer.write(str[index]);
					if (str[index] == '1')
						ID[idxID] += (long) (Math.pow(2, (maxpow - (i
								* subGraphSize + j))));
				}
			}
			writer.write("\n");
		}
		writer.write(" ID: " + ID[idxID]);
		writer.write("\n");
		// out.close();
		idxID++;
	}

	private char[] getMatrix(char[] str) {
		char[] charList = new char[subGraphSize * subGraphSize];
		int l = 0, k;
		int index = 0;
		for (int i = 0; i < subGraphSize; i++) {
			for (int j = 0; j < subGraphSize; j++) {
				k = (i * subGraphSize) + j;
				if (i == j) {
					charList[k] = '0';
					l++;
				} else {
					index = (i * (subGraphSize)) + (j - l);
					charList[k] = str[index];
				}
			}
		}
		return charList;
	}

	public void outputFreq(long RAND, long subgraphCounter, String path)
			throws IOException {
		BufferedWriter cm = new BufferedWriter(new FileWriter(new File(path
				+ Parameters.LIN_DIR_SEP + "Frequencies.txt")));

		System.out.println("Writing Frequencies to " + cm);
		cm.write("TOTAL NUMBER OF CLASSES:: " + enumerated_class);
		cm.newLine();
		cm.newLine();
		cm.write("SUBGRAPH NUMBER \t\t ID \t\t Total count \t\t Frequency \n");
		for (int i = 0; i < tree.getLeafNumber(); i++) {
			cm.write((i + 1) + " \t\t " + ID[i] + " \t\t " + c_main[i + 1]
					+ " \t\t "
					+ ((double) c_main[i + 1] / subgraphCounter * 100));
			cm.newLine();
		}
		cm.close();
	}

	public void extract() throws IOException {
		int class_num = tree.getLeafNumber();
		char[] adj_str = new char[subGraphSize * (subGraphSize - 1)];
		Node current = tree.returnRoot();

		head = 0;
		if (isRand)
			DFS(current);
		else {
			DFSmain(current, adj_str, 0);
			out.close();
			enumerated_class = c_main[0];
			System.out.println("Number of Non-isomorphic Classes: "
					+ enumerated_class);
		}

		if (isRand) {
			int j = 0;
			for (int i = 0; i < class_num; i++) {
				mean[j] += c_rand[i];
				var[j] += (c_rand[i] * c_rand[i]);
				j++;
			}
		}
	}

	public BufferedWriter getWriter() {
		return writer;
	}

	public void DFS(Node currentNode) {
		if (currentNode == null)
			return;
		if (currentNode.getChildCount() == 0) {
			Node leaf = currentNode;
			if (leaf.getCount() > 0) {
				c_rand[head] = leaf.getCount();
				head++;
				leaf.setCount(0);
			}
			return;
		}
		if (currentNode.getLeftChild() != null)
			DFS(currentNode.getLeftChild());
		DFS(currentNode.getRightChild());
	}

	public Tree getTree() {
		return tree;
	}

	public int getGraphSize() {
		return graphSize;
	}

	public void DFSmain(Node currentNode, char[] str, int lev)
			throws IOException {
		if (currentNode == null)
			return;
		if (currentNode.getChildCount() == 0) {
			printAdjMatrix(str);
			Node leaf = currentNode;
			head++;
			c_main[head] = leaf.getCount();
			Motif motif = new Motif(ID[head - 1], getMatrix(str), c_main[head]);
			motifsList.add(motif);
			leaf.setCount(0);
			c_main[0]++;
			return;
		}
		if (currentNode.getLeftChild() != null) {
			str[lev] = '0';
			DFSmain(currentNode.getLeftChild(), str, lev + 1);
		}
		str[lev] = '1';
		DFSmain(currentNode.getRightChild(), str, lev + 1);
	}

	public void calculateZSCORE(long RAND, long subgraphCounter, String path)
			throws IOException {
		zscore_writer = new BufferedWriter(new FileWriter(new File(path + Parameters.LIN_DIR_SEP + "ZScore.txt")));
		System.out.println("HI! Size = " + tree.getLeafNumber());

		for (int i = 0; i < tree.getLeafNumber(); i++) {
			mean[i] = mean[i] / RAND;
			System.out.println("mean = " + mean[i] + " Var = " + var[i]);
			var[i] = Math.sqrt((var[i] - (RAND * (mean[i] * mean[i]))) / RAND);

			if (var[i] != 0)
				score[i] = ((double) (c_main[i + 1] - mean[i])) / var[i];
			else {
				score[i] = -1;
			}
		}

		// outputFreq(RAND, subgraphCounter, path);

//		System.out.println("Writing ZScores to ...");

		zscore_writer.write("TOTAL NUMBER OF CLASSES:: " + enumerated_class);
		zscore_writer.newLine();
		zscore_writer.newLine();
		zscore_writer
				.write("SUBGRAPH NUMBER \t\t ID \t\t MEAN IN RANDOM \t\t PERCENTAGE IN "
						+ "RANDOM \t\t STD IN RANDOM \t\t ZSCORE (*Constant Delta, if std = 0)\n");
		for (int i = 0; i < tree.getLeafNumber(); i++) {
			Motif motif = motifsList.get(i);
			motif.setTotalMotifCount(c_main[0]);
			if (var[i] != 0) {
				zscore_writer.write(i + 1 + " \t\t\t " + ID[i] + " \t\t\t "
						+ mean[i] + " \t\t "
						+ (((double) mean[i] / subgraphCounter) * 100)
						+ " \t\t\t" + var[i] + " \t\t\t " + score[i]);
				motif.setzScore(score[i]);
			} else if (var[i] == 0) {
				double newScore = (c_main[i + 1] - mean[i]);
				zscore_writer.write(i + 1 + " \t\t\t " + ID[i] + " \t\t\t "
						+ mean[i] + " \t\t\t "
						+ (((double) mean[i] / subgraphCounter) * 100)
						+ " \t\t\t" + var[i] + " \t\t\t " + newScore);
				motif.setzScore(newScore);
			}
			zscore_writer.newLine();
		}
		zscore_writer.close();
	}

	public void classify(int[] tempSubgraph, int level, int[] permutationList)
			throws IOException {
		int[] lab = new int[subGraphSize];
		int i = 0;
		// writer.write("SubGraph: [ ");
		// for (int j = 0; j < tempSubgraph.length; j++) {
		// writer.write(tempSubgraph[j] + " ");
		// }
		// writer.write(" ]\n");

		char[] workingMatrix = new char[subGraphSize * subGraphSize];
		int count = 0;
		for (i = 0; i < tempSubgraph.length; i++) {
			for (int j = 0; j < tempSubgraph.length; j++) {
				workingMatrix[count++] = adjMat[tempSubgraph[i]][tempSubgraph[j]];
			}
		}
		getCanonicalLabeling(workingMatrix, lab, permutationList);
		tree.init_cur_node();
		if (!isRand) {
			for (i = 0; i < subGraphSize - 1; i++) {
				for (int j = 0; j < subGraphSize; j++) {
					if (i == j)
						continue;
					if (isConnected(tempSubgraph[lab[i]], tempSubgraph[lab[j]])) {
						tree.insert_one_main();
					} else {
						tree.insert_zero_main();
					}
				}
			}
			int j = 0;
			for (j = 0; j < subGraphSize - 2; j++) {
				if (isConnected(tempSubgraph[lab[i]], tempSubgraph[lab[j]])) {
					tree.insert_one_main();
				} else {
					tree.insert_zero_main();
				}
			}
			if (isConnected(tempSubgraph[lab[i]], tempSubgraph[lab[j]])) {
				tree.update_one_main(1);
			} else {
				tree.update_zero_main(1);
			}
		} else {
			for (i = 0; i < subGraphSize - 1; i++) {
				for (int j = 0; j < subGraphSize; j++) {
					if (i == j)
						continue;
					if (isConnected(tempSubgraph[lab[i]], tempSubgraph[lab[j]])) {
						tree.insert_one_rand();
					} else {
						tree.insert_zero_rand();
					}
				}
			}
			int j = 0;
			for (j = 0; j < subGraphSize - 2; j++) {
				if (isConnected(tempSubgraph[lab[i]], tempSubgraph[lab[j]])) {
					tree.insert_one_rand();
				} else {
					tree.insert_zero_rand();
				}
			}

			if (isConnected(tempSubgraph[lab[i]], tempSubgraph[lab[j]])) {
				tree.update_one_rand(1);
			} else {
				tree.update_zero_rand(1);
			}
		}
	}

	// private void getBliss(int[] tempSubgraph) {
	// fi.tkk.ics.jbliss.Graph<Integer> g = new
	// fi.tkk.ics.jbliss.Graph<Integer>();
	// for (int i = 0; i < tempSubgraph.length; i++) {
	// g.add_vertex(tempSubgraph[i]);
	// for (int j = 0; j < tempSubgraph.length; j++) {
	// if (isConnected(tempSubgraph[i], tempSubgraph[j]))
	// g.add_edge(tempSubgraph[i], tempSubgraph[j]);
	// }
	// }
	// Map<Integer, Integer> canlab = g.canonical_labeling();
	// System.out.print("A canonical labeling for the graph is: ");
	// Utils.print_labeling(System.out, canlab);
	// }

	public void getCanonicalLabeling(char[] matrix, int[] lab,
			int[] permutationList) throws IOException {
		int l = subGraphSize;
		int a = 0, b = 0, k = 0;
		char[] max = new char[l * l];
		for (int i = 0; i < l * l; i++) {
			max[i] = '0';
		}
		for (int count = 0; count < permutationList.length / l; count++) {
			boolean compare = true;
			int turn = 0;
			int nn = 0;
			int cc = count * l;
			for (int i = 0; i < l; i++) {
				a = permutationList[cc + i];
				nn = a * l;
				for (int j = 0; j < l; j++) {
					b = permutationList[cc + j];
					k = nn + b ;
					if (i == j)
						continue;
					char c = matrix[k];
					int index = (i * l) + j;
					if (compare) {
						if (max[index] < c) {
							turn = 1;
							compare = false;
							max[index] = c;
						} else if (max[index] > c)
							compare = false;
					} else if (turn == 1) {
						max[index] = c;
					}
				}
			}
			if (turn == 1) {
				for (int i = 0; i < l; i++) {
					lab[i] = permutationList[cc + i];
				}
			}
		}
//		 printM(max);
	}

	public void printM(char[][] tempMtx) throws IOException {
		writer.write("---------------------\n");
		for (int i = 0; i < tempMtx.length; i++) {
			writer.write("| ");
			for (int j = 0; j < tempMtx.length; j++) {
				writer.write(tempMtx[i][j] + " ");
			}
			writer.write("|\n");
		}

	}

	private void printM(char[] tempMtx) throws IOException {
		// writer.write("------------\n");
		// for (int i = 0; i < subGraphSize; i++) {
		// writer.write("| ");
		// for (int j = 0; j < subGraphSize; j++) {
		// writer.write(tempMtx[(i * subGraphSize) + j] + " ");
		// }
		// writer.write("|\n");
		// }
		// writer.write("------------\n");
		for (int i = 0; i < tempMtx.length; i++) {
			writer.write(tempMtx[i] + " ");
		}
		writer.write("\n");
//		writer.close();

	}

	@Override
	protected Object clone() throws CloneNotSupportedException {
		return super.clone();
	}

	public static void main(String[] args) {
		char[][] mtx = new char[4][4];
		mtx[0][0] = '0';
		mtx[0][1] = '0';
		mtx[0][2] = '0';
		mtx[0][3] = '0';
		mtx[1][0] = '0';
		mtx[1][1] = '0';
		mtx[1][2] = '0';
		mtx[1][3] = '0';
		mtx[2][0] = '0';
		mtx[2][1] = '1';
		mtx[2][2] = '0';
		mtx[2][3] = '0';
		mtx[3][0] = '1';
		mtx[3][1] = '0';
		mtx[3][2] = '1';
		mtx[3][3] = '0';
		// new Graph(4, 4, true).getCanonicalLabeling(mtx, new int[4],
		// new PermutationGenerator(4).getList());
		// char[] max = { '1', '0', '0', '1' };
		// char[] c = { '0', '1', '1', '1' };
		// int turn = 0;
		//
		// boolean compare = true;
		// for (int index = 0; index < 4; index++) {
		// if (compare) {
		// if (max[index] < c[index]) {
		// turn = 1;
		// compare = false;
		// max[index] = c[index];
		// } else if (max[index] > c[index])
		// compare = false;
		// } else if (turn == 1) {
		// max[index] = c[index];
		// }
		// }
		// for (int i = 0; i < c.length; i++) {
		// System.out.println(max[i]);
		// }
		int r = 0;
		int q = 0;
		int d = 9;
		int count = 9;
		q = (int) Math.floor((Math.log(d) / Math.log(2)));
		r = (int) (d - Math.pow(2, q));
		int r1 = (r == 0) ? 0 : (count << (r - 1));
		int cc = (count << q) + r1;
		System.out.println("q = " + q + " r = " + r);
		System.out.println(cc);

		// for (int x = 0; x < 5; x++) {
		// System.out.println(((x << 2) + x) << 1);

		// }

		byte[] b1 = { 0, 0, 0, 1 };
		byte[] b2 = { 1, 0, 1, 1 };

	}

}
