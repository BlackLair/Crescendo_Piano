package com.example.crescendo_piano;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.media.SoundPool;
import android.media.midi.MidiDevice;
import android.media.midi.MidiDeviceInfo;
import android.media.midi.MidiManager;
import android.media.midi.MidiOutputPort;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import org.w3c.dom.Text;

@RequiresApi(api = Build.VERSION_CODES.M)
public class MidiDeviceCallback extends MidiManager.DeviceCallback{
    private Context context;
    private MidiOutputPort outputPort; // 미디 통신을 위한 외부 포트 설정(MIDI장치 기준 OUTPUT)
    Boolean isConnected=false; // 연결된 상태 확인
    MyReceiver receiver;
    TextView deviceName;

    public MidiDeviceCallback(Context context, TextView deviceName){ // 콜백 생성자
        super();
        this.context=context;
        this.deviceName=deviceName;
    }
    public void onDeviceAdded(MidiDeviceInfo info){ // 장치 연결시
        Bundle properties = info.getProperties(); // 장치 정보 가져옴
        String manufacturer=properties.getString(MidiDeviceInfo.PROPERTY_NAME); // 장치 이름 가져옴
        deviceName.setText("MIDI 장치 : "+manufacturer);
        deviceName.setTextColor(Color.parseColor("#00FF00"));
        ((MidiActivity)context).midiManager.openDevice(info, new MidiManager.OnDeviceOpenedListener(){
            @Override
            public void onDeviceOpened(MidiDevice midiDevice) {
                if(midiDevice==null){

                }else{

                    isConnected=true;
                    receiver=new MyReceiver(context);
                    outputPort=midiDevice.openOutputPort(0);    // 포트 열기
                    outputPort.connect(receiver);   // 통신 시작

                }
            }

        }, new Handler(Looper.getMainLooper()));
    }

    @Override
    public void onDeviceRemoved(MidiDeviceInfo device) {    // 장치 연결이 해제되었을 경우
        isConnected=false;
        deviceName.setText("MIDI 장치 : 연결된 장치 없음");
        deviceName.setTextColor(Color.parseColor("#FF0000"));
    }
    public void disConnect(){   // 장치 연결 끊기
        if(isConnected==true) {
            outputPort.onDisconnect(receiver);
            isConnected=false;
        }
    }
}
