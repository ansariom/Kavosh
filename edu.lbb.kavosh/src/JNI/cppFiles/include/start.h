#ifndef START_H
#define START_H

#include <stdio.h>
#include <stdlib.h>
#include <map>
#include <math.h>
#include <graph.h>
#include <getopt.h>
#include <randomGenerator.h>

#define EPOC 10

using namespace std;

class Start {
public:
	Start(int motifSize);
//	Start(int argc, const char *inFileName, const char *outFileName);
	void print_usage (FILE * stream, int exit_code);
	bool ReadData(const char *path);
	void initChildSet(int root, int level);
	void Explore(vertex root, int level, int reminder);
	void swap( int i, int j, int root, int level, int reminder, int m); 
	void GEN( int n, int k, int root, int level, int reminder, int m);
	void NEG( int n, int k, int root, int level, int reminder, int m);
	void Enumerate();
private:
	vertex **subgraph;
	int subgraphSize;
	unsigned long num_random_graphs;
	bool *Visited;
	unsigned int **childSet;
	unsigned int **Index;  
	unsigned long long subgraphCounter; 
	unsigned long random_counter;
	Graph *g;
	long num;
};
#endif
