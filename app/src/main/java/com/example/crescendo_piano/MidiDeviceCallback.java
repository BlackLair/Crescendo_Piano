package com.example.crescendo_piano;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.midi.MidiDevice;
import android.media.midi.MidiDeviceInfo;
import android.media.midi.MidiManager;
import android.media.midi.MidiOutputPort;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.TextView;

public class MidiDeviceCallback extends MidiManager.DeviceCallback{
    private Context context;
    public MidiOutputPort outputPort; // 미디 통신을 위한 외부 포트 설정(MIDI장치 기준 OUTPUT)
    public MyReceiver receiver;
    TextView deviceName;

    MidiMessageAnalyzer mAnalyzer;
    static{
        System.loadLibrary("Crescendo_Piano");
    }
    private void onNativeMessageReceive(final byte[] message) { //MIDI데이터 파싱 (C++측에서 호출)
        Integer numMessage=Byte.toUnsignedInt(message[0]);
        for(int i=0; i<numMessage; i++) {
            int state = (Byte.toUnsignedInt(message[1+i * 3]) & 0xf0) >> 4;
            int channel = (Byte.toUnsignedInt(message[1+i * 3]) & 0x0f) + 1;
            int pitch = Byte.toUnsignedInt(message[2 + i * 3]) - 21;
            float velocity = (float) Byte.toUnsignedInt(message[3 + i * 3]) / 127;
            mAnalyzer.AnalyzeNote(state, channel, pitch, velocity); // 분석 가능한 midi 데이터로 분석 및 소리 재생
        }
    }
    public native void startReadingMidi(MidiDevice receiveDevice, int portNumber);
    public native void stopReadingMidi();
    private native void initNative();
    public MidiDeviceCallback(Context context, TextView deviceName){ // 콜백 생성자
        super();
        this.context=context;
        this.deviceName=deviceName;
        mAnalyzer=new MidiMessageAnalyzer(context);
    }
    public void onDeviceAdded(MidiDeviceInfo info){ // 장치 연결시
        initNative();
        ((MidiActivity)context).midiManager.openDevice(info, new MidiManager.OnDeviceOpenedListener(){
            @Override
            public void onDeviceOpened(MidiDevice midiDevice) {
                if(midiDevice==null){
                }else{
                    ((MidiActivity)context).isConnected=true;
                    startReadingMidi(midiDevice, 0);
                }
            }
        }, new Handler(Looper.getMainLooper()));
        Bundle properties = info.getProperties(); // 장치 정보 가져옴
        String manufacturer=properties.getString(MidiDeviceInfo.PROPERTY_NAME); // 장치 이름 가져옴
        deviceName.setText("MIDI 장치 : "+manufacturer);
        deviceName.setTextColor(Color.parseColor("#00FF00"));
        ((MidiActivity)context).colorAnimation.setObjectValues(((ColorDrawable)((MidiActivity)context).midi_layout.getBackground()).getColor(), Color.parseColor("#008886"));
        ((MidiActivity)context).colorAnimation.setDuration(500);
        ((MidiActivity)context).colorAnimation.start(); // 장치 연결되면 배경색 변경

    }

    @Override
    public void onDeviceRemoved(MidiDeviceInfo device) {    // 장치 연결이 해제되었을 경우
        stopReadingMidi();
        ((MidiActivity)context).colorAnimation.setObjectValues(((ColorDrawable)((MidiActivity)context).midi_layout.getBackground()).getColor(), Color.parseColor("#490019"));
        ((MidiActivity)context).colorAnimation.setDuration(500);
        ((MidiActivity)context).colorAnimation.start();
        ((MidiActivity)context).isConnected=false;

        deviceName.setText("MIDI 장치 : 연결된 장치 없음");
        deviceName.setTextColor(Color.parseColor("#FF0000"));
    }
    public void disConnect(){   // 장치 연결 끊기
        if(((MidiActivity)context).isConnected==true) {
            stopReadingMidi();
            ((MidiActivity)context).isConnected=false;
        }
    }
}
