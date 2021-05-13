#include <unistd.h>
#include <atomic>
#include <inttypes.h>
#include <stdio.h>
#include <string>

#include <jni.h>

#include <pthread.h>

#define LOG_TAG "AppMidiManager -JNI"

#include <amidi/AMidi.h>
#include "MidiSpec.h"

static AMidiDevice* sNativeReceiveDevice = NULL;
static AMidiOutputPort* sMidiOutputPort = NULL;

static pthread_t sReadThread;
static std::atomic<bool> sReading(false);

extern JavaVM* theJvm;
extern jobject dataCallbackObj;
extern jmethodID midDataCallback;


// C++단에서 Java단으로 JNI 이용해서 수신된 MIDI데이터 전송
static void SendTheReceivedData(uint8_t* data, int numBytes) {
    JNIEnv *env;
    theJvm->AttachCurrentThread(&env, NULL);
    if (env == NULL){
                
    }
    jbyteArray ret = env->NewByteArray(numBytes);
    env->SetByteArrayRegion (ret, 0, numBytes, (jbyte*)data);
    
    // 자바 콜백으로 데이터 전송
    env->CallVoidMethod(dataCallbackObj, midDataCallback, ret);
}

// MIDI 데이터수신 위한 루프 폴링
static void* readThreadRoutine(void * context){
    (void)context;
    
    sReading = true;
    
    AMidiOutputPort* outputPort = sMidiOutputPort;
    
    const size_t MAX_BYTES_TO_RECEIVE = 128;
    uint8_t incomingMessage[MAX_BYTES_TO_RECEIVE];
    
    while(sReading){
        usleep(2000);
        
        int32_t opcode;
        size_t numBytesReceived;
        int64_t timestamp;
        ssize_t numMessagesReceived = AMidiOutputPort_receive(outputPort, &opcode, incomingMessage, MAX_BYTES_TO_RECEIVE,
                &numBytesReceived, &timestamp);
        
        if(numMessagesReceived<0){
            sReading=false;
        }
        if(numMessagesReceived>0 && numBytesReceived>=0){
            if(opcode == AMIDI_OPCODE_DATA && (incomingMessage[0]&kMIDISysCmdChan) != kMIDISysCmdChan){
                SendTheReceivedData(incomingMessage, numBytesReceived);
            }else if (opcode == AMIDI_OPCODE_FLUSH){
                // 무시
            }
        }
    }
    return NULL;
}

// JNI함수

extern "C" {
// MIDI 신호 읽기 시작
void Java_com_example_crescendo_1piano_MidiDeviceCallback_startReadingMidi(
        JNIEnv *env, jobject, jobject midiDeviceObj, jint portNumber) {
    media_status_t status;
    status = AMidiDevice_fromJava(env, midiDeviceObj, &sNativeReceiveDevice);
    AMidiOutputPort *outputPort;
    status = AMidiOutputPort_open(sNativeReceiveDevice, portNumber, &outputPort);
    sMidiOutputPort = outputPort;
    pthread_create(&sReadThread, NULL, readThreadRoutine, NULL);
}
void Java_com_example_crescendo_1piano_MidiActivity_startReadingMidi(
        JNIEnv *env, jobject, jobject midiDeviceObj, jint portNumber) {
    media_status_t status;
    status = AMidiDevice_fromJava(env, midiDeviceObj, &sNativeReceiveDevice);
    AMidiOutputPort *outputPort;
    status = AMidiOutputPort_open(sNativeReceiveDevice, portNumber, &outputPort);
    sMidiOutputPort = outputPort;
    pthread_create(&sReadThread, NULL, readThreadRoutine, NULL);
}
// MIDI 읽기 종료
void Java_com_example_crescendo_1piano_MidiDeviceCallback_stopReadingMidi(JNIEnv *, jobject) {
    sReading = false;
    pthread_join(sReadThread, NULL);
    AMidiDevice_release(sNativeReceiveDevice);
    sNativeReceiveDevice = NULL;
}
    void Java_com_example_crescendo_1piano_MidiActivity_stopReadingMidi(JNIEnv *, jobject) {
        sReading = false;
        pthread_join(sReadThread, NULL);
        AMidiDevice_release(sNativeReceiveDevice);
        sNativeReceiveDevice = NULL;
    }
}
