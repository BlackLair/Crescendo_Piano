package com.example.crescendo_piano;
// 들어온 MIDI 신호를 분석하여 알맞는 소리 출력
import android.media.SoundPool;
import android.widget.Toast;

import java.util.Stack;

public class MidiMessageAnalyzer {
    private int pedalFlag;      // 0 : 페달 뗌  1 : 페달 밟음
    private int[] isKeyOn;      // 0 : 건반 똄  0 : 건반 누름
    private Stack<Integer> relNote; // 페달 밟는도중 떼어진 건반 정보
    SoundPool midiSoundPool;
    int soundKeys[];
    public MidiMessageAnalyzer(SoundPool midiSoundPool, int soundKeys[]){
        this.midiSoundPool=midiSoundPool;
        this.soundKeys=soundKeys;
    }

    public void AnalyzeNote(String receivedDataString){
        int pitch;
        float velocity;
        int channel;
        pitch=getPitch(receivedDataString);
        velocity=getVelocity(receivedDataString);
        channel=getChannel(receivedDataString);
        
    }
    public int getPitch(String receivedDataString){ // 누른 건반 pitch 가져옴
        return Integer.parseInt(receivedDataString.substring(4,6), 16)-21;
    }
    public float getVelocity(String receivedDataString){ // 건반 누른 세기 가져옴
        return (float)Integer.parseInt(receivedDataString.substring(6,8), 16)/128;
    }
    public int getChannel(String receivedDataString){ // MIDI 신호의 채널 가져옴 (1~16)
        return Integer.parseInt(receivedDataString.substring(3,4), 16)+1;
    }
}
