package com.example.crescendo_piano;

import android.app.Activity;
import android.media.midi.MidiReceiver;
import android.os.Build;

import androidx.annotation.RequiresApi;

import java.io.IOException;

@RequiresApi(api = Build.VERSION_CODES.M)
public class MyReceiver extends MidiReceiver {
    private Activity activity;
    public MyReceiver(Activity activity){
        super();
        this.activity=activity;
    }
    @Override   // MIDI 원시데이터 수신
    public void onSend(byte[] data, int offset, int count, long timestamp) throws IOException {
        StringBuilder hexString=new StringBuilder();
        for(byte b : data){ // 들어온 원시 데이터를 연속적 16진수 문자열로 변환환
           hexString.append(String.format("%02x", b&0xff));
            if(hexString.toString().length()>=20)
                break;
        }
        String receivedDataString=hexString.toString();


    }
}
