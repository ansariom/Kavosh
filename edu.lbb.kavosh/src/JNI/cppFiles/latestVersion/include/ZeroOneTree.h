#ifndef	ZEROONETREE_H
#define ZEROONETREE_H

#include <string>
#include <stdio.h>
#include <stdlib.h>
#include <limits.h>

using namespace std;


class Node {
	public:
		bool visited;
		Node * left;
		Node * right;
		Node() {	left = NULL; right = NULL; visited = false;}
        ~Node() { printf("Destroying ... \n"); fflush(stdout); }// if(right != NULL) delete right; if(left != NULL) delete left; }
};

class Leaf:public Node {
	public:
		unsigned long long count;
		Leaf() {	count = 1;}
};

struct value {
	unsigned long long * C;
	char ** adj_str;
};

class tree {
	protected:
		Node * root;
		Node * node;
		Node * cur_node;
		Leaf * leaf;
		int node_ptr;
		int leaf_ptr;
		unsigned long long leaf_num;
		unsigned long long * C;
		int head;
		value v;
		int subgraphSize;
	public:
		tree(int);
		Node * add_node();
		Node * return_root();
		Leaf * add_leaf();
		Leaf * update_leaf(Leaf * l, int val);
		void allocate_node();
		void allocate_leaf();
		unsigned long long  get_leafnum();
		void init_cur_node();
		void insert_one_main();
		void insert_zero_main();
		void insert_one_rand();
		void insert_zero_rand();
		void update_one_main(int val);
		void update_zero_main(int val);
		void update_one_rand(int val);
		void update_zero_rand(int val);
		unsigned long long * extract();
		void DFS(Node * cur);
		value destroy();
		void DFS_value(Node * cur, char * str, int lev);
        ~tree();
};
	
#endif //ZEROONETREE_H
