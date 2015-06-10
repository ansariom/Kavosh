package edu.lbb.kavosh.algorithm;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import edu.lbb.kavosh.algorithm.data.Graph;
import edu.lbb.kavosh.algorithm.data.RandGenerator;
import edu.lbb.kavosh.data.common.Utility;

public class StartJKavosh {
	boolean isRand;
	int[][] subgraph;
	int subgraphSize;
	long num_random_graphs;
	Boolean[] Visited;
	int[][] childSet;
	int[][] Index;
	long subgraphCounter;
	long random_counter;
	Graph g;
	long num;

	public void printUsage() {
		System.out.println("Usage: Kavosh options[inputfile...] \n ");
		System.out
				.println("\t-h	--help\t\t\tDisplay this usage information. \n"
						+ "\t-i	--input filename\tInput filename.\n"
						+ "\t-o	--output path\t\tOutput directory.\n"
						+ "\t-r 	--random number \tNumber of random graphs (default = 0).\n"
						+ "\t-s 	--size motifsize \tMotif size.\n");

	}

	public boolean readData(String path, String out_path) {
		int i, j;
		int graphSize;

		try {
			InputStream inputStream = new FileInputStream(new File(path));
			BufferedReader bufferedReader = new BufferedReader(
					new InputStreamReader(inputStream));
			String line = null;
			graphSize = Integer.valueOf(bufferedReader.readLine());
			Visited = new Boolean[graphSize + 1];
			for (i = 1; i <= graphSize; i++)
				Visited[i] = false;
			g = new Graph(graphSize, subgraphSize, isRand, out_path);
			while ((line = bufferedReader.readLine()) != null) {
				i = Integer.valueOf(line.split("\t")[0]).intValue();
				j = Integer.valueOf(line.split("\t")[1]).intValue();
				if (i == j)
					continue;
				g.addEdge(i, j);
			}
			g.finalizeG();
			inputStream.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		childSet = new int[subgraphSize][];
		Index = new int[subgraphSize][];

		for (i = 0; i < subgraphSize; i++) {
			childSet[i] = new int[g.getMaxDegree() * subgraphSize + 1];
			Index[i] = new int[g.getMaxDegree() * subgraphSize + 1];
		}

		return true;
	}

	public void initChildSet(int root, int level) {
		int N[];
		int i, j;
		int[] parents = subgraph[level - 1];

		childSet[level][0] = 0;
		for (i = 1; i <= parents[0]; i++) {
			N = g.getNeighbours(parents[i]);
			for (j = 1; j <= N[0]; j++) {
				if (root > N[j])
					continue;
				if (!Visited[N[j]]) {
					Visited[N[j]] = true;
					childSet[level][0]++;
					childSet[level][childSet[level][0]] = N[j];
				}
			}
		}
	}

	public void Explore(int root, int level, int reminder) throws IOException {
		int i = 0, j, k; // k is the number of selected nodes in the current
							// level.

		// System.out.println("************************************");
		// System.out.println("*****   Exploring level " + level + "  *******");
		// System.out.println("************************************");

		if (reminder == 0) { // reminder == 0 assures level <= subgraphSize
			subgraphCounter++;

			// System.out.println("--> Subgraph Number " + subgraphCounter);
			// for (i = 0; i < level; i++) {
			// System.out.println("Level " + i);
			// for (k = 1; k <= subgraph[i][0]; k++) {
			// System.out.print(subgraph[i][k] + " ");
			// }
			// System.out.println("\n");
			// }
			// System.out.println("------------------------------------\n");

			i = 0;
			int[] tempSubgraph = new int[subgraphSize];
			for (int l = 0; l < level; l++) {
				for (k = 1; k <= subgraph[l][0]; k++) {
					tempSubgraph[i++] = subgraph[l][k];
				}
			}
			g.classify(tempSubgraph, level,
					Utility.generatePermutationList(subgraphSize));

			return;
		}

		initChildSet(root, level);

		// System.out.print("Valid Children in level = " + level + " {");
		// for (k = 1; k <= childSet[level][0]; k++) {
		// System.out.print(childSet[level][k] + ", ");
		// }
		// System.out.println();

		for (k = 1; k <= reminder; k++) {
			if (childSet[level][0] < k) { // There is not enough child to choose
											// m from.
				for (i = 1; i <= childSet[level][0]; i++) {
					Visited[childSet[level][i]] = false;
				}
				return;
			}

			// System.out.println("Selecting " + k + " node(s) from level "
			// + level);
			// System.out.print("Initial Selection = { ");
			// for (i = 1; i <= k; i++) {
			// System.out.print(childSet[level][i] + " ,");
			// }
			// System.out.println("}");
			subgraph[level][0] = k;
			for (i = 1; i <= k; i++) {
				subgraph[level][i] = childSet[level][i];
				Index[level][i] = i;
			}

			Explore(root, level + 1, reminder - k);
			GEN(childSet[level][0], k, root, level, reminder, k);

			// System.out.println("************************************");
			// System.out.println("*****    Back to level " + level + " *******"
			// + level);
			// System.out.println("************************************\n");
		}

		for (i = 1; i <= childSet[level][0]; i++) {
			Visited[childSet[level][i]] = false;
		}
		subgraph[level][0] = 0;
		return;
	}

	public void swap(int i, int j, int root, int level, int reminder, int m)
			throws IOException {
		Index[level][i] = Index[level][j];
		subgraph[level][Index[level][i]] = childSet[level][i];
		Explore(root, level + 1, reminder - m);
	}

	public void GEN(int n, int k, int root, int level, int reminder, int m)
			throws IOException {
		if (k > 0 && k < n) {
			GEN(n - 1, k, root, level, reminder, m);
			if (k == 1)
				swap(n, n - 1, root, level, reminder, m);
			else
				swap(n, k - 1, root, level, reminder, m);

			NEG(n - 1, k - 1, root, level, reminder, m);
		}
	}

	public void NEG(int n, int k, int root, int level, int reminder, int m)
			throws IOException {
		if (k > 0 && k < n) {
			GEN(n - 1, k - 1, root, level, reminder, m);

			if (k == 1)
				swap(n - 1, n, root, level, reminder, m);
			else
				swap(k - 1, n, root, level, reminder, m);

			NEG(n - 1, k, root, level, reminder, m);
		}

	}

	public void Enumerate() throws IOException {
		int v;

		for (v = 1; v <= g.getGraphSize(); v++) {
			subgraph[0][0] = 1;
			subgraph[0][1] = v;

			Visited[v] = true;
			Explore(v, 1, subgraphSize - 1);
			Visited[v] = false;
		}
	}

	public void Start(int motifSize, String infilename, String outDir, int randNum)
			throws IOException {
		long subgraphCounterMain;
		RandGenerator gen = new RandGenerator();
		int next_option;
		String program_name;
		String output_directory = outDir;
		String input_filename = infilename;
		int verbose = 0;
		subgraphSize = motifSize;
		// input_filename =
		// "/home/mitra/Documents/MyDocs/UniversityRelated/LBB/Kavosh/networks/E-coli";
		// input_filename = "Networks/Test";
		// output_directory = "";

		subgraph = new int[subgraphSize][];
		for (int i = 0; i < subgraphSize; i++)
			subgraph[i] = new int[subgraphSize + 1];

		num = 0;
		System.out.println("Motif Size: " + subgraphSize);
		System.out.println("Input Graph: " + input_filename);
		readData(input_filename, output_directory);

		// for main graph
		isRand = false;
		subgraphCounter = 0;
		Enumerate();
		g.allocateCounter();
		System.out.println("Total Number of Subgraphs:" + subgraphCounter);
		g.extract();

		subgraphCounterMain = subgraphCounter;
		// for random graphs
		isRand = true;
		g.setRand(true);
		g.resetIdxID();
		long boz = 0;
		num_random_graphs = 100;
		num_random_graphs = randNum;
		System.out.println("Number of Random Graphs: " + num_random_graphs);
		for (int i = 1; i <= num_random_graphs; i++) {
			random_counter = i;
			// start_random_time = clock();
			gen.genRandGraph_Edge(g);
			subgraphCounter = 0;
			Enumerate();
			g.extract();
		}
		if (0 < num_random_graphs)
			g.calculateZSCORE(num_random_graphs, subgraphCounterMain,
					output_directory);

		g.outputFreq(num_random_graphs, subgraphCounterMain, output_directory);

		g.closeStreams();
	}

	
}
