package com.example.crescendo_piano;
// 화면 상에서 드럼 연주하는 액티비티

import android.media.SoundPool;
import android.os.Build;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class DrumPadActivity extends AppCompatActivity {
    private View decorView;
    private int uiOption;
    ImageButton btn_goBack;
    private ImageButton btnDrum[]=new ImageButton[8];
    private SoundResourceManager soundManager;
    private int soundKeys[][];
    private SoundPool drumPadSoundPool;
    private int drumId[] = {R.id.drum_crush,R.id.drum_htom,R.id.drum_mtom,R.id.drum_ride,
            R.id.drum_hihat,R.id.drum_snare,R.id.drum_kick,R.id.drum_ltom};
    private int drumPreset=0;
    private ImageButton drum_presetbutton;
    private ImageView drum_presetview;
    private RelativeLayout drumpad_topbanner;

    private TextView BPMText;
    private SeekBar metronome_seekbar;
    private ImageButton bpmUp, bpmDown, btn_metronome_button;
    private ImageView btn_metronome_mode, btn_metronome_icon;
    private SoundPool metronomeSoundPool;
    private int metronomesoundKeys[];
    private int BPM;  // 메트로놈을 위한 BPM값
    private int metronome_count=0;      // 메트로놈 박자 세는 기준
    private int metronome_maxcount=3;   // 0이면 메트로놈 꺼짐 3이면 3/4박자 4면 4/4박자
    private ScheduledExecutorService metronomeService;
    public boolean isIconLeft=true;

    private final float drumVolumnOffset[][]={{0.6f, 0.7f, 0.7f, 0.9f, 0.4f, 0.55f, 0.9f, 0.7f},
                                            {0.55f, 0.7f, 1f, 0.6f, 0.85f, 1f, 0.85f, 0.7f}}; // 드럼소리크기 조절


    @Override
    protected void onDestroy() { // 메모리 리소스 해제
        soundManager.unLoad(drumPadSoundPool);
        soundManager.unLoad(metronomeSoundPool);
        super.onDestroy();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
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
        /////////////////////////////앱 하단바 제거//////////////////////////////
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drum_pad);

        soundManager=new SoundResourceManager();
        soundKeys=new int[2][8];   // 드럼소리의 종류는 8가지
        drumPadSoundPool=soundManager.loadDrumSound(soundKeys,getApplicationContext()); // 드럼 사운드 로딩
        for(int i=0;i<drumId.length;i++){
            int index=i;
            btnDrum[i]=findViewById(drumId[i]);
            Animation animation=new AlphaAnimation(0,1); // 처음 시작 시 드럼패드 천천히 나타나는 애니메이션
            animation.setDuration(1800);
            btnDrum[i].setAnimation(animation);
            btnDrum[i].setVisibility(View.VISIBLE);
            btnDrum[i].setOnTouchListener(new View.OnTouchListener() { // 드럼패드 버튼 리스너
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    if(motionEvent.getAction()==MotionEvent.ACTION_DOWN){
                        if(index==0 || index==3) // 크래시, 라이드 이전 소리 끊고 다시재생
                            PlayNote.drumNoteOff(drumPadSoundPool,index);
                        PlayNote.drumNoteOn(drumPadSoundPool,soundKeys[drumPreset][index],index,drumVolumnOffset[drumPreset][index]);
                        if(drumPreset==0)
                            view.setBackgroundResource(R.drawable.drumpad_p); // 터치 시 누른 이미지로 교체
                        else
                            view.setBackgroundResource(R.drawable.drumpadb_p);
                    }
                    else if(motionEvent.getAction()==MotionEvent.ACTION_UP){ // 손 떼면 이미지 원상복귀
                        if(drumPreset==0)
                            view.setBackgroundResource(R.drawable.drumpad);
                        else
                            view.setBackgroundResource(R.drawable.drumpadb);
                    }
                    return true;
                }
            });
        }

        ///////////////////////////////프리셋 변경기능////////////////////////////////////
        drum_presetbutton=findViewById(R.id.drum_presetbutton);
        drum_presetview=findViewById(R.id.drum_presetview);
        drumpad_topbanner=findViewById(R.id.drumpad_topbanner);
        drum_presetbutton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if(motionEvent.getAction()==MotionEvent.ACTION_DOWN){
                    view.setBackgroundResource(R.drawable.drum_presetbutton_p);
                }else if(motionEvent.getAction()==MotionEvent.ACTION_UP){
                    view.setBackgroundResource(R.drawable.drum_presetbutton);
                }
                return false;
            }
        });
        drum_presetbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(drumPreset==0){
                    drumPreset=1;
                    drum_presetview.setBackgroundResource(R.drawable.drum_presetview_b);
                    for(int i=0; i<8; i++)
                        btnDrum[i].setBackgroundResource(R.drawable.drumpadb);
                    drumpad_topbanner.setBackgroundResource(R.drawable.keyboard_backgrounddrum2);
                }else{
                    drumPreset=0;
                    drum_presetview.setBackgroundResource(R.drawable.drum_presetview_a);
                    for(int i=0; i<8; i++)
                        btnDrum[i].setBackgroundResource(R.drawable.drumpad);
                    drumpad_topbanner.setBackgroundResource(R.drawable.keyboard_backgrounddrum);
                }
            }
        });
        ////////////////////////////////////////////////////////////////////////////////
        metronomesoundKeys=new int[2];
        btn_goBack=(ImageButton)findViewById(R.id.drumpad_goBack);
        btn_goBack.setOnClickListener(new View.OnClickListener() {
            @Override       // 뒤로가기 버튼
            public void onClick(View view) {
                finish();
                overridePendingTransition(R.anim.zoomout_enter, R.anim.zoomout_exit);
            }
        });
        btn_goBack.setOnTouchListener(new View.OnTouchListener() { // 뒤로가기 버튼 이미지 변환 효과
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if(motionEvent.getAction()==MotionEvent.ACTION_DOWN)
                    btn_goBack.setBackgroundResource(R.drawable.back_p);
                else if(motionEvent.getAction()==MotionEvent.ACTION_UP)
                    btn_goBack.setBackgroundResource(R.drawable.back);
                return false;
            }
        });

        ////////////////////////////////////////메트로놈 기능////////////////////////////////////////
        BPMText=findViewById(R.id.dmetronome_bpm);
        metronome_seekbar=findViewById(R.id.dmetronome_seekbar);
        bpmUp=findViewById(R.id.dmetronome_bpmup);
        bpmDown=findViewById(R.id.dmetronome_bpmdown);
        btn_metronome_button=findViewById(R.id.dmetronome_button);
        btn_metronome_icon=findViewById(R.id.dmetronome_icon);
        btn_metronome_mode=findViewById(R.id.dmetronome_mode);
        BPM=120;
        btn_metronome_icon.setBackgroundResource(R.drawable.metronome_icon_l);
        isIconLeft=true;
        metronomeSoundPool=soundManager.loadMetronomeSound(metronomesoundKeys,this);
        Runnable metronomeRunnable=new Runnable(){ // 메트로놈 소리 1회 재생하는 runnable. bpm에 맞는 주기로 실행된다.
            @Override
            public void run() {// 메트로놈 소리를 정박에는 킥, 나머지는 하이햇 소리 냄
                if(metronome_count==0) // 첫 박자 킥 소리
                    PlayNote.metronomeOn(metronomeSoundPool, metronomesoundKeys[0]);
                else                    // 나머지 박자 하이햇 소리
                    PlayNote.metronomeOn(metronomeSoundPool, metronomesoundKeys[1]);
                metronome_count++;
                if(metronome_count>=metronome_maxcount) metronome_count=0; //설정된 메트로놈 박자에 따라 카운트조절
                if(isIconLeft){ // 메트로놈 울릴 때마다 아이콘 가리키는 좌우 방향 변경
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    btn_metronome_icon.setBackgroundResource(R.drawable.metronome_icon_r);
                                }
                            });
                        }
                    }).start();
                    isIconLeft=false;
                }else{
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    btn_metronome_icon.setBackgroundResource(R.drawable.metronome_icon_l);
                                }
                            });
                        }
                    }).start();
                    isIconLeft=true;
                }

            }
        };
        metronome_maxcount=0;
        btn_metronome_button.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if(motionEvent.getAction()==MotionEvent.ACTION_DOWN){
                    btn_metronome_button.setBackgroundResource(R.drawable.metronome_button_p);
                }else if(motionEvent.getAction()==MotionEvent.ACTION_UP){
                    btn_metronome_button.setBackgroundResource(R.drawable.metronome_button);
                }
                return false;
            }
        });
        btn_metronome_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(metronome_maxcount==3){ //메트로놈 꺼짐
                    metronome_maxcount=0;
                    metronome_count=0;
                    metronomeService.shutdownNow(); //메트로놈서비스 종료
                    btn_metronome_mode.setBackgroundResource(R.drawable.metronome_stop);
                }
                else if(metronome_maxcount==0){ //메트로놈 4/4로 설정
                    metronome_maxcount=4;
                    metronomeService= Executors.newSingleThreadScheduledExecutor();
                    metronomeService.scheduleAtFixedRate(metronomeRunnable,0,(60000000/BPM), TimeUnit.MICROSECONDS);
                    btn_metronome_mode.setBackgroundResource(R.drawable.metronome_quad);
                }
                else if(metronome_maxcount==4){ //메트로놈 3/4로 설정
                    metronome_maxcount=3;
                    metronomeService.shutdownNow();
                    metronomeService= Executors.newSingleThreadScheduledExecutor();
                    metronomeService.scheduleAtFixedRate(metronomeRunnable,0,(60000000/BPM), TimeUnit.MICROSECONDS);
                    btn_metronome_mode.setBackgroundResource(R.drawable.metronome_triple);
                }
            }
        });
        metronome_seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) { //Seekbar가 이동할경우
                BPM=i;
                BPMText.setText(Integer.toString(i));

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                BPMText.setText(Integer.toString(seekBar.getProgress()));
                BPM=seekBar.getProgress();
                if(metronome_maxcount!=0) { // 메트로놈 재시작
                    metronome_count=0;
                    metronomeService.shutdownNow();
                    metronomeService = Executors.newSingleThreadScheduledExecutor();
                    metronomeService.scheduleAtFixedRate(metronomeRunnable, 0, (60000000 / BPM), TimeUnit.MICROSECONDS);
                }
            }
        });

        bpmUp.setOnClickListener(new View.OnClickListener() { // BPM을 1씩 올려주는 버튼
            @Override
            public void onClick(View view) {
                Integer currentBPM=BPM;
                if(currentBPM<300){
                    currentBPM+=1;
                    BPM=currentBPM;
                    metronome_seekbar.setProgress(currentBPM);
                    if(metronome_maxcount!=0) { // 메트로놈 재시작
                        metronome_count=0;
                        metronomeService.shutdownNow();
                        metronomeService = Executors.newSingleThreadScheduledExecutor();
                        metronomeService.scheduleAtFixedRate(metronomeRunnable, 0, (60000000 / BPM), TimeUnit.MICROSECONDS);
                    }
                }
            }
        });
        bpmUp.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if(motionEvent.getAction()==MotionEvent.ACTION_DOWN){
                    bpmUp.setBackgroundResource(R.drawable.keyboard_met_plus_p);
                }else if(motionEvent.getAction()==MotionEvent.ACTION_UP){
                    bpmUp.setBackgroundResource(R.drawable.keyboard_met_plus);
                }
                return false;
            }
        });
        bpmDown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Integer currentBPM=BPM;
                if(currentBPM>1){
                    currentBPM-=1;
                    BPM=currentBPM;
                    metronome_seekbar.setProgress(currentBPM);
                    if(metronome_maxcount!=0) { // 메트로놈 재시작
                        metronome_count=0;
                        metronomeService.shutdownNow();
                        metronomeService = Executors.newSingleThreadScheduledExecutor();
                        metronomeService.scheduleAtFixedRate(metronomeRunnable, 0, (60000000 / BPM), TimeUnit.MICROSECONDS);
                    }
                }
            }
        });
        bpmDown.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if(motionEvent.getAction()==MotionEvent.ACTION_DOWN){
                    bpmDown.setBackgroundResource(R.drawable.keyboard_met_minus_p);
                }else if(motionEvent.getAction()==MotionEvent.ACTION_UP){
                    bpmDown.setBackgroundResource(R.drawable.keyboard_met_minus);
                }
                return false;
            }
        });

        /////////////////////////////////////////////
    }
}