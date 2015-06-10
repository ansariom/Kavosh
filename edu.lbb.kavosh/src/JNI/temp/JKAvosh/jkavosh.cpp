#include <jni.h>
#include <stdio.h>
#include <stdlib.h>
#include <start.h>
#include "jkavosh.h"

JNIEXPORT void JNICALL Java_edu_lbb_kavosh_algorithm_JKavosh_getCanonicalLabeling ( JNIEnv* env, jobject obj, jint argc) {

	printf("Starting ... \n");
	Start *s = new Start(argc);
	printf("Ending ... \n");
} 
