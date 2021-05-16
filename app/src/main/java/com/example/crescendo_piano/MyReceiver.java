package com.example.crescendo_piano;

// MIDI장치에서 전송받은 원시적 MIDI 메시지를 분석할 수 있는 형태로 변환

import android.content.Context;
import android.media.midi.MidiReceiver;

import java.io.IOException;

public class MyReceiver extends MidiReceiver {
    private MidiMessageAnalyzer mAnalyzer;
    public MyReceiver(Context context){
        super();
        mAnalyzer=new MidiMessageAnalyzer(context);
    }
    @Override   // MIDI 원시데이터 수신
    public void onSend(byte[] data, int offset, int count, long timestamp) throws IOException {
        int state=(Byte.toUnsignedInt(data[1])&0xf0)>>4;
        int channel=(Byte.toUnsignedInt(data[1])&0x0f) +1;
        int pitch=Byte.toUnsignedInt(data[2])-21;
        float velocity=(float)Byte.toUnsignedInt(data[3])/127;
        mAnalyzer.AnalyzeNote(state, channel, pitch, velocity); // 분석 가능한 midi 데이터로 분석 및 소리 재생
    }
}
