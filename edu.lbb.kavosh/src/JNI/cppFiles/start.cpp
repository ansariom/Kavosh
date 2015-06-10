#include <start.h>

bool isRand;
/****************************************************************
****************************************************************/

void Start::print_usage (FILE * stream, int exit_code)
{
	fprintf (stream, "Usage: Kavosh options[inputfile...] \n ");
    fprintf (stream,
		 "\t-h	--help\t\t\tDisplay this usage information. \n"
		 "\t-i	--input filename\tInput filename.\n"
		 "\t-o	--output path\t\tOutput directory.\n"
		 "\t-r 	--random number \tNumber of random graphs (default = 0).\n"
		 "\t-s 	--size motifsize \tMotif size.\n" );
	     
    exit (exit_code);
}

/****************************************************************
****************************************************************/

bool Start::ReadData(const char *path) {
	register int i, j;
	int graphSize;
	FILE * inFile = fopen(path, "r");
	
	if (inFile == NULL) {
		printf("Error opening %s file.\n", path);
		return false;
	}
	
	if (!feof(inFile))
		fscanf(inFile, "%d\n", &graphSize);	
	
	Visited = new bool[graphSize+1];
	for(i = 1; i <= graphSize; i++)
		Visited[i] = false;
	
	
	g = new Graph(graphSize, subgraphSize);
	while (!feof(inFile)) {
		fscanf(inFile, "%d %d\n", &i, &j);
		if(i == j) continue;
		g->addEdge(i, j);
	}
	
	g->Finalize();
	
	//g->Print();
	fclose(inFile);
	childSet = new unsigned int*[subgraphSize];
	Index    = new unsigned int*[subgraphSize];
	
	for(i = 0; i < subgraphSize; i++) {
		childSet[i] = new unsigned int[g->MaxDegree() * subgraphSize + 1];
		Index[i] = new unsigned int[g->MaxDegree() * subgraphSize + 1];
	}

	return true;
}

/***************************************************************
 * This function finds the valid children in each given level. *
***************************************************************/

void Start::initChildSet(int root, int level) {
	register int *N;
	register int i, j;
	const int *parents = subgraph[level-1];
		
	childSet[level][0] = 0;
	for(i = 1; i <= parents[0]; i++) {
		N = g->getNeighbours(parents[i]);
		for(j = 1; j <= N[0] && root <= N[j]; j++) {
			if(!Visited[N[j]]) {
				Visited[N[j]] = true;
				childSet[level][0]++;
				childSet[level][childSet[level][0]] = N[j];				
			}
		}		
	}
}

/****************************************************************************************
 * This function Explores the root vertex and generates subgraphs containing that node. *
****************************************************************************************/

void Start::Explore(vertex root, int level, int reminder) {
	register int i, j, k; // k is the number of selected nodes in the current level.

#ifdef Debug
	printf("************************************\n");
	printf("*****   Exploring level %3d  *******\n", level);
	printf("************************************\n");
#endif

	if (reminder == 0) { //reminder == 0 assures level <= subgraphSize
		subgraphCounter++;
		
#ifdef Debug
		printf("--> Subgraph Number %llu: \n", subgraphCounter);
		for(i = 0; i < level; i++) {
			printf("Level %d: ", i);
			for(k = 1; k <= subgraph[i][0]; k++) {
				printf("%d ", subgraph[i][k]);
			}
			printf("\n");
		}
		printf("\n");
		printf("------------------------------------\n");
#endif

		g->Classify(subgraph, level);
		
		return;
	}
	
	
	initChildSet(root, level); 
	
#ifdef Debug
	printf("Valid Children in level %d:\nN = { ", level);
	for(k = 1; k <= childSet[level][0]; k++) {
		printf("%d ", childSet[level][k]);
	}		
	printf("}\n");
#endif

	for(k = 1; k <= reminder; k++) {
		if( childSet[level][0] < k ) { //There is not enough child to choose m from.
			for(i = 1; i <= childSet[level][0]; i++) {
				Visited[childSet[level][i]] = false;
			}
			return;
		}

#ifdef Debug
		printf("Selecting %d node(s) from level %d\n", k, level);
		printf("Initial Selection = { ");
		for(i = 1; i <= k; i++) {
			printf("%d ", childSet[level][i]);
		}
		printf("}\n");
#endif
		subgraph[level][0] = k;
		for(i = 1; i <= k; i++) {
			subgraph[level][i] = childSet[level][i];
			Index[level][i] = i;
		}	
		
		Explore(root, level + 1, reminder - k);
	    GEN( childSet[level][0], k, root, level, reminder, k);

#ifdef Debug
		printf("************************************\n");
		printf("*****    Back to level %3d   *******\n", level);
		printf("************************************\n");
#endif
	}
	
	for(i = 1; i <= childSet[level][0]; i++) {
		Visited[childSet[level][i]] = false;
	}
	subgraph[level][0] = 0;
	return;
}

/***************************************************************************************************
 * The following three functions generate all C(n, k) in Gray Code order, Adopted from Rusky code. *
***************************************************************************************************/

void Start::swap( int i, int j, int root, int level, int reminder, int m) {
#ifdef Debug
	printf("Switch %d with %d in level %d\n", childSet[level][j], childSet[level][i], level);
#endif

	Index[level][i] = Index[level][j];
	subgraph[level][Index[level][i]] = childSet[level][i];	
	Explore(root, level + 1, reminder - m);
}

/****************************************************************
****************************************************************/

void Start::GEN( int n, int k, int root, int level, int reminder, int m) {	
	if (k > 0 && k < n) {
    	GEN( n-1, k, root, level, reminder, m);
		if (k == 1) 
			swap( n, n-1, root, level, reminder, m);  
		else 
			swap( n, k-1, root, level, reminder, m);
    
		NEG( n-1, k-1, root, level, reminder, m);
    }
}

/****************************************************************
****************************************************************/

void Start::NEG( int n, int k, int root, int level, int reminder, int m) {	
	if (k > 0 && k < n) {
    	GEN( n-1, k-1, root, level, reminder, m);
    	
		if (k == 1) 
			swap( n-1, n, root, level, reminder, m);  
		else 
			swap( k-1, n, root, level, reminder, m);
    
		NEG( n-1, k, root, level, reminder, m);
	}
	
}

/***********************************************************************************
 * This function enumerates the subgraphs related to each vertex of inpur network. *
***********************************************************************************/
void Start::Enumerate() {
	register int v;

	for (v = 1; v <= g->Size(); v++)
	{
#ifdef Debug
		printf("+ Exploring Node %d ...\n", v);
#endif Debug
		subgraph[0][0] = 1;
		subgraph[0][1] = v;
		
		Visited[v] = true;
		Explore(v, 1, subgraphSize - 1);
		Visited[v] = false;
	}	
}

/****************************************************************
****************************************************************/

Start::Start(int motifSize) {
	register int i, j;
	long long subgraphCounterMain;
	generator gen;
  int next_option;
    char *program_name;
    const char *output_directory;
    const char *input_filename;
    int verbose = 0;
	subgraphSize = motifSize;
	input_filename = "/home/Mitra/Documents/University/LBB/Kavosh/Kavosh-SourceCode/networks/ecoli";
	output_directory = "";

	subgraph = new int*[subgraphSize];
	for (int i = 0; i < subgraphSize; i++)
		subgraph[i] = new int[subgraphSize+1];
	
	num = 0;	
	printf("Motif Size: %d\n", subgraphSize);
	printf("Input Graph: %s\n", input_filename);
	ReadData(input_filename);
	
    	g->setPath(output_directory);

	//for main graph
	isRand = false;
	subgraphCounter = 0;
	Enumerate();
	g->AllocateCounter();
	printf("Total Number of Subgraphs: %llu\n", subgraphCounter);	
	g->Extract();
	
	subgraphCounterMain = subgraphCounter;
	//for random graphs
	isRand = true;
    long long boz = 0;
	if (subgraphSize > 6) 
		num_random_graphs = 0;
	else 	
		num_random_graphs = 10;
	printf("Number of Random Graphs: %d\n", num_random_graphs);
	for (i = 1; i <= num_random_graphs; i++) {
		random_counter = i;
		//start_random_time = clock();
		gen.genRandGraph_Edge(g);
		subgraphCounter = 0;
		Enumerate();
		g->Extract();
	}
	if (0 < num_random_graphs)
		g->calculateZSCORE(num_random_graphs, subgraphCounterMain, output_directory);

	else {
		g->outputFreq(num_random_graphs, subgraphCounterMain, output_directory);
	}		
	for(i = 0; i < subgraphSize; i++) {
		delete [] Index[i];
		delete [] childSet[i];
	}
	
	for (int i = 0; i < subgraphSize; i++)
		delete [] subgraph[i];

    delete [] subgraph;
	delete [] Index;
	delete [] childSet;
    delete [] Visited;
	delete g;
	
}
