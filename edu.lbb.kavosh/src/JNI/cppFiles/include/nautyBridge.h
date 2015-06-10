#ifndef NAUTYBRIDGE_H
#define NAUTYBRIDGE_H

#include <stdio.h>
#include <stdlib.h>
#include <nauty.h>

class NautyBridge {
    public:
	NautyBridge(int subgraphSize1);
	~NautyBridge();
	int* getResult(int** subgraph); 
};
#endif


