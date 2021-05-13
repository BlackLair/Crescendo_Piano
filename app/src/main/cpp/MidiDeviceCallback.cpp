#include <jni.h>

extern "C"{
    JavaVM* theJvm;
    jobject dataCallbackObj;
    jmethodID midDataCallback;

    JNICALL void Java_com_example_crescendo_1piano_MidiDeviceCallback_initNative(JNIEnv * env, jobject instance){
        env->GetJavaVM(&theJvm);

        jclass clsMidiActivity = env->FindClass("com/example/crescendo_piano/MidiDeviceCallback");
        dataCallbackObj= env->NewGlobalRef(instance);
        midDataCallback= env->GetMethodID(clsMidiActivity, "onNativeMessageReceive", "([B)V");
    }
    JNICALL void Java_com_example_crescendo_1piano_MidiActivity_initNative(JNIEnv * env, jobject instance){
        env->GetJavaVM(&theJvm);

        jclass clsMidiActivity = env->FindClass("com/example/crescendo_piano/MidiActivity");
        dataCallbackObj= env->NewGlobalRef(instance);
        midDataCallback= env->GetMethodID(clsMidiActivity, "onNativeMessageReceive", "([B)V");
    }
}