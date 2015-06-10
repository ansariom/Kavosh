#include <jni.h>
#include <stdio.h>
#include "Helloworld.h"

JNIEXPORT jintArray JNICALL
Java_org_pos_Helloworld_print ( JNIEnv* env, jobject obj, jintArray aiArr ) {

   int i, sum = 0;
   jintArray aiRet;
   jint *retBody;

   jsize len = (*env)->GetArrayLength(env, aiArr);
   jint *body = (*env)->GetIntArrayElements(env, aiArr, 0);
   /* to copy a region of the array only, use Get/Set<type>ArrayRegion functions */

   for (i=0; i<len; i++)
   {
      sum += body[i];
      body[i]++;
   }

   aiRet = (*env)->NewIntArray(env, len);
   retBody = (*env)->GetIntArrayElements(env, aiRet, 0);

   for (i=0; i<len; i++)
   {
      retBody[i] = body[i];
   }

   (*env)->ReleaseIntArrayElements(env, aiArr, body, 0);
   (*env)->ReleaseIntArrayElements(env, aiRet, retBody, 0);

   return aiRet;   
} 
