#include <jni.h>
#include <stdio.h>
#include <stdlib.h>
#include "Canonical.h"

JNIEXPORT jintArray JNICALL
Java_edu_lbb_kavosh_jni_Canonical_getCanonicalLabeling ( JNIEnv* env, jobject obj, jintArray pList, jcharArray matrix, jint subGraphSize ) {
     jint i, j, sum = 0;
     jintArray lab_Ret;
     jint *lab_retBody;
     jsize pl_len = (*env)->GetArrayLength(env, pList);
     jsize mt_len = (*env)->GetArrayLength(env, matrix);
     jint *pl_body = (*env)->GetIntArrayElements(env, pList, 0);
     jchar *mt_body = (*env)->GetCharArrayElements(env, matrix, 0);

     int clab[subGraphSize];

		int a = 0, b = 0, k = 0;
		int count = 0;
		char max[subGraphSize * subGraphSize];
		for (i = 0; i < subGraphSize * subGraphSize; i++) {
			max[i] = 0;
		}

		for (count = 0; count < pl_len / subGraphSize; count++) {
			char compare = 1;
			int turn = 0;
			int cc = count * subGraphSize;
			for (i = 0; i < subGraphSize; i++) {
				a = pl_body[cc + i];
				int nn = a * subGraphSize;
				for (j = 0; j < subGraphSize; j++) {
					b = pl_body[cc + j];
					k = nn + b;
					if (i == j)
						continue;
					char c = mt_body[k];
					int index = (i * subGraphSize) + j;
					if (compare == 1) {
						if (max[index] < c) {
							turn = 1;
							compare = 0;
							max[index] = c;
						} else if (max[index] > c)
							compare = 0;
					} else if (turn == 1) {
						max[index] = c;
					}
				}
			}
			if (turn == 1) {
				for (i = 0; i < subGraphSize; i++) {
					clab[i] = pl_body[cc + i];
				}
			}
		}




   lab_Ret = (*env)->NewIntArray(env, subGraphSize);
   lab_retBody = (*env)->GetIntArrayElements(env, lab_Ret, 0);

   for (i=0; i<subGraphSize; i++)
   {
      lab_retBody[i] = clab[i];
   }

   (*env)->ReleaseIntArrayElements(env, pList,pl_body, 0);
   (*env)->ReleaseCharArrayElements(env, matrix,mt_body, 0);
   (*env)->ReleaseIntArrayElements(env, lab_Ret, lab_retBody, 0);
      return lab_Ret;

} 
