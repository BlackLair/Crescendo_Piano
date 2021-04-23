package com.example.crescendo_piano;

import android.app.Activity;
import android.content.Context;
import android.media.midi.MidiDevice;
import android.media.midi.MidiDeviceInfo;
import android.media.midi.MidiManager;
import android.media.midi.MidiOutputPort;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

@RequiresApi(api = Build.VERSION_CODES.M)
public class MyDeviceCallback extends MidiManager.DeviceCallback{
    private MidiManager midiManager;
    private MidiDevice myDevice;
    private Activity activity;
    private Context context;
    private MidiOutputPort outputPort; // 미디 통신을 위한 외부 포트 설정(MIDI장치 기준 OUTPUT)
    Boolean isConnected=false; // 연결된 상태 확인

    MyReceiver receiver;


    public MyDeviceCallback(Activity activity, MidiManager midiManager, Context context){ // 콜백 생성자
        super();
        this.midiManager=midiManager;
        this.activity=activity;
        this.context=context;
    }
    public void onDeviceAdded(MidiDeviceInfo info){ // 장치 연결시
        Bundle properties = info.getProperties(); // 장치 정보 가져옴
        String manufacturer=properties.getString(MidiDeviceInfo.PROPERTY_NAME);
        Toast.makeText(context, manufacturer+" 연결되었습니다.", Toast.LENGTH_SHORT).show();
        midiManager.openDevice(info, new MidiManager.OnDeviceOpenedListener(){
            @Override
            public void onDeviceOpened(MidiDevice midiDevice) {
                if(midiDevice==null){

                }else{
                    isConnected=true;
                    receiver=new MyReceiver(activity);
                    myDevice=midiDevice;
                    outputPort=midiDevice.openOutputPort(0);    // 포트 열기
                    outputPort.connect(receiver);   // 통신 시작

                }
            }

        }, new Handler(Looper.getMainLooper()));
    }

    @Override
    public void onDeviceRemoved(MidiDeviceInfo device) {    // 장치 연결이 해제되었을 경우
        isConnected=false;
        Toast.makeText(context, "연결이 해제되었습니다.", Toast.LENGTH_SHORT).show();
    }
    public void disConnect(){   // 장치 연결 끊기
        if(isConnected==true) {
            outputPort.onDisconnect(receiver);
            isConnected=false;
        }
    }
}
