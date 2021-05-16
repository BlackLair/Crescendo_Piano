package com.example.crescendo_piano;

import android.content.Context;
import android.content.Intent;
import android.media.SoundPool;
import android.media.midi.MidiDevice;
import android.media.midi.MidiDeviceInfo;
import android.media.midi.MidiManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.github.mmin18.widget.RealtimeBlurView;

import java.util.ArrayList;

public class MidiActivity extends AppCompatActivity {
    private View decorView; // 전체화면 출력을 위한 멤버 변수
    private int uiOption;

    ImageButton btn_goBack;
    private Context context=this;
    private SoundResourceManager soundManager;
    public int[] soundKeys, drumsoundKeys; // SoundPool 할당된 오디오 파일 구분 키값
    public MidiManager midiManager;// MIDI 장치 연결 관리 매니저
    public SoundPool midiSoundPool,midiDrumSoundPool; // 건반 사운드 리소스 로딩 SoundPool
    public int keyboardChannel,drumChannel; // 설정할 midi channel
    public MidiDeviceCallback myMidiCallback;
    public int inst;    // 0 : 피아노 1 : 바이올린 3 : 하프
    public TextView deviceName;
    public Boolean isConnected=false;

    public RealtimeBlurView midi_blur; // 미연결 시 붉은 블러
    public TextView midi_unplugged_tv;
    public ImageView midi_unplugged; // 미연결 시 이미지
    public Animation showblur, hideblur; // 블러 애니메이션


    public Spinner channel_key_spinner, channel_drum_spinner;
    AdapterChannelSpinner adapterDrumSpinner, adapterKeySpinner;

    public MidiMessageAnalyzer mAnalyzer;

    public native void initNative();
    public native void startReadingMidi(MidiDevice receiveDevice, int portNumber);
    public native void stopReadingMidi();

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
        soundManager=new SoundResourceManager();    // 사운드매니저 객체생성
        soundKeys=new int[88];
        drumsoundKeys=new int[8];
        inst=getInst.getIntExtra("inst", 0);
        midiSoundPool=soundManager.load(soundKeys, inst, getApplicationContext()); // 악기 음원 로딩
        midiDrumSoundPool=soundManager.loadDrumSound(drumsoundKeys, getApplicationContext());
        midiManager=(MidiManager)getApplicationContext().getSystemService(Context.MIDI_SERVICE);
        MidiDeviceInfo[] deviceList=midiManager.getDevices();   // 연결되어 있는 장치 목록 가져옴



        ///////////////////////////MIDI 수신 채널 설정 스피너//////////////////////////////////////
        channel_drum_spinner=findViewById(R.id.channel_drum_spinner);
        channel_key_spinner=findViewById(R.id.channel_key_spinner);
        ArrayList<String> channelString=new ArrayList<>();
        for(int i=1; i<17; i++){
            channelString.add("channel "+i);
        }
        adapterDrumSpinner = new AdapterChannelSpinner(this, channelString);
        adapterKeySpinner = new AdapterChannelSpinner(this, channelString);
        channel_drum_spinner.setAdapter(adapterDrumSpinner);
        channel_key_spinner.setAdapter(adapterKeySpinner);

        channel_drum_spinner.setEnabled(false);
        channel_key_spinner.setEnabled(false);
        channel_key_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                keyboardChannel=i+1;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        channel_drum_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                drumChannel=i+1;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        channel_key_spinner.setSelection(0);
        channel_drum_spinner.setSelection(9);
        /////////////////////////////////////////////////////////////////////////////////////////



        // 장치 연결 해제 시 애니메이션
        showblur=new AlphaAnimation(0f,1.0f);
        hideblur=new AlphaAnimation(1.0f,0.001f);
        showblur.setDuration(500);
        hideblur.setDuration(500);

        midi_unplugged_tv=(TextView)findViewById(R.id.midi_unplugged_tv);
        midi_blur=(RealtimeBlurView)findViewById(R.id.midi_blur); // 장치 미연결 시 화면 블러 처리
        midi_unplugged=(ImageView)findViewById(R.id.midi_unplugged);
        midi_unplugged_tv.setVisibility(View.VISIBLE);
        midi_unplugged.setVisibility(View.VISIBLE);
        midi_blur.setVisibility(View.VISIBLE);
        deviceName=(TextView)findViewById(R.id.deviceName); // 장치 이름 표시 텍스트뷰


        myMidiCallback=new MidiDeviceCallback(this, deviceName );
        midiManager.registerDeviceCallback(myMidiCallback, Handler.createAsync(Looper.getMainLooper())); //디바이스 콜백 등록

        /////////////////////////////////////////액티비티 진입 시 이미 연결되어 있을 경우//////////////////////////////////
        if(deviceList.length>0){
            initNative();
            mAnalyzer=new MidiMessageAnalyzer(context);
            Bundle properties = deviceList[0].getProperties(); // 장치 정보 가져옴
            String manufacturer=properties.getString(MidiDeviceInfo.PROPERTY_NAME); // 장치 이름 가져옴
            midi_blur.setVisibility(View.GONE);
            midi_blur.startAnimation(hideblur);
            midi_unplugged.setVisibility(View.GONE);
            midi_unplugged.startAnimation(hideblur);
            midi_unplugged_tv.setVisibility(View.GONE);
            midi_unplugged_tv.startAnimation(hideblur);
            channel_key_spinner.setEnabled(true);
            channel_drum_spinner.setEnabled(true);
            deviceName.setText("MIDI 장치 : "+manufacturer);
            midiManager.openDevice(deviceList[0], new MidiManager.OnDeviceOpenedListener() {
                @Override
                public void onDeviceOpened(MidiDevice midiDevice) {
                    if(midiDevice==null){
                    }else {
                        isConnected = true;
                        startReadingMidi(midiDevice, 0);
                    }
                }
            }, new Handler(Looper.getMainLooper()));
        }
        ////////////////////////////////////////////////////////////////////////////////////////////////////

        btn_goBack=(ImageButton)findViewById(R.id.midi_goBack);
        btn_goBack.setOnTouchListener(new View.OnTouchListener() { // 뒤로가기 버튼 이미지 변환 효과
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if(motionEvent.getAction()==MotionEvent.ACTION_DOWN)
                    btn_goBack.setBackgroundResource(R.drawable.backgray);
                else if(motionEvent.getAction()==MotionEvent.ACTION_UP)
                    btn_goBack.setBackgroundResource(R.drawable.back);
                return false;
            }
        });
        btn_goBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myMidiCallback.disConnect();    // 연결 해제
                midiManager.unregisterDeviceCallback(myMidiCallback);   //콜백 해제

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