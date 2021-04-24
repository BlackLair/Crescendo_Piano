package com.example.crescendo_piano;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.media.SoundPool;
import android.media.midi.MidiDeviceInfo;
import android.media.midi.MidiManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;

public class MidiActivity extends AppCompatActivity {
    private View decorView; // 전체화면 출력을 위한 멤버 변수
    private int uiOption;

    ImageButton btn_goBack;

    private SoundResourceManager soundManager;
    public int[] soundKeys; // SoundPool 할당된 오디오 파일 구분 키값
    public MidiManager midiManager;// MIDI 장치 연결 관리 매니저
    public SoundPool midiSoundPool; // 건반 사운드 리소스 로딩 SoundPool
    public int keyboardChannel,drumChannel; // 설정할 midi channel
    public MyDeviceCallback myMidiCallback;
    @RequiresApi(api = Build.VERSION_CODES.P)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_midi);
        /////////////////////////////앱 하단바 제거///////////////////////////////
        decorView=getWindow().getDecorView();
        uiOption=getWindow().getDecorView().getSystemUiVisibility();
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.ICE_CREAM_SANDWICH)
            uiOption|= View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.JELLY_BEAN)
            uiOption|=View.SYSTEM_UI_FLAG_FULLSCREEN;
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.KITKAT)
            uiOption|=View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
        decorView.setSystemUiVisibility(uiOption);
        /////////////////////////////앱 하단바 제거///////////////////////////////
        Intent getInst=getIntent();     // 선택한 악기 정보를 가져오기 위한 인텐트
        keyboardChannel=1; drumChannel=10;  // 채널 기본값 설정
        soundManager=new SoundResourceManager();    // 사운드매니저 객체생성
        soundKeys=new int[88];
        midiSoundPool=soundManager.load(soundKeys, getInst.getIntExtra("inst", 0), getApplicationContext()); // 악기 음원 로딩
        midiManager=(MidiManager)getApplicationContext().getSystemService(Context.MIDI_SERVICE);
        MidiDeviceInfo[] deviceList=midiManager.getDevices();   // 연결되어 있는 장치 목록 가져옴

        myMidiCallback=new MyDeviceCallback(this );
        midiManager.registerDeviceCallback(myMidiCallback, Handler.createAsync(Looper.getMainLooper())); //디바이스 콜백 등록


        btn_goBack=(ImageButton)findViewById(R.id.midi_goBack);
        btn_goBack.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return false;
            }
        });
        btn_goBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MidiActivity.this, InstSelectActivity.class);
                intent.putExtra("selectInst", 1); // midi연주모드
                myMidiCallback.disConnect();    // Port Receiver 연결 해제
                midiManager.unregisterDeviceCallback(myMidiCallback);   //콜백 해제
                startActivity(intent);
                finish();
            }
        });

    }


    @Override
    protected void onDestroy() {    // 액티비티 종료시

        soundManager.unLoad(midiSoundPool); // 사운드 리소스 메모리 해제

        super.onDestroy();
    }
}