package com.example.crescendo_piano;

// 소리 재생 관련 메소드들 포함한 클래스

import android.media.SoundPool;

public class PlayNote {
    public static int[] streamID= new int[89];   // 각 건반의 stream ID 저장(소리 멈출 때 필요)
    public static int[] drumStreamID=new int[8];
    public static void noteOn(SoundPool spool, int key, int pitch, float velocity){ // 소리 재생
        streamID[pitch]=spool.play(key, velocity, velocity, 0, 0, 1);
    }
    public static void noteOff(SoundPool spool, int pitch){ // 소리 멈춤
        spool.stop(streamID[pitch]);
    }
    public static void drumNoteOn(SoundPool spool, int key, int index, float velocity){//8키드럼소리재생
        drumStreamID[index]=spool.play(key, velocity, velocity, 0, 0, 1);
    }
    public static void drumNoteOff(SoundPool spool, int index){//8키드럼소리멈춤
        spool.stop(drumStreamID[index]);
    }
    public static void metronomeOn(SoundPool spool,int key){ // 메트로놈 재생 메소드
        spool.play(key, 0.25f, 0.25f, 0, 0, 1);
    }
}
