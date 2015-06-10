 #include <jni.h>
 #include <stdio.h>
 #include "HelloWorld.h"
 
 JNIEXPORT void JNICALL 
 Java_hello_HelloWorld_print(JNIEnv *env, jobject obj)
 {
     printf("Hello World!\n");
     return;
 }

