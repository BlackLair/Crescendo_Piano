package com.example.crescendo_piano;

import android.content.Intent;
import android.media.SoundPool;
import android.os.Build;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class KeyboardActivity extends AppCompatActivity {
    private View decorView;
    private int uiOption;

    private ImageButton touch_improve;

    private int inst; // 0 : 피아노 1 : 바이올린 2 : 하프
    private SoundResourceManager soundmanager;
    private int soundKeys[], metronomesoundKeys[];
    private SoundPool keyboardSoundPool, metronomeSoundPool;
    private ImageButton goBack, btnSustain, bpmUp, bpmDown, btn_metronome_button;
    private ImageView btn_metronome_mode, btn_metronome_icon;
    private ImageView btnOctave;
    private ImageButton wKeys[]=new ImageButton[15];
    private ImageButton bKeys[]=new ImageButton[10];
    private ImageView shadow[]=new ImageView[10];
    private TextView octaveValue, BPMText;
    private SeekBar metronome_seekbar;
    private RelativeLayout keyboardTopBanner;
    // 각 건반 이미지버튼들의 id속성 저장
    private int wButtonID[]={R.id.btn_wkey0, R.id.btn_wkey1, R.id.btn_wkey2, R.id.btn_wkey3, R.id.btn_wkey4, R.id.btn_wkey5,
                            R.id.btn_wkey6, R.id.btn_wkey7, R.id.btn_wkey8, R.id.btn_wkey9, R.id.btn_wkey10, R.id.btn_wkey11,
                            R.id.btn_wkey12, R.id.btn_wkey13, R.id.btn_wkey14};
    private int bButtonID[]={R.id.btn_bkey0, R.id.btn_bkey1, R.id.btn_bkey2, R.id.btn_bkey3, R.id.btn_bkey4, R.id.btn_bkey5,
            R.id.btn_bkey6, R.id.btn_bkey7, R.id.btn_bkey8, R.id.btn_bkey9};
    private int shadowID[]={R.id.shadow0, R.id.shadow1, R.id.shadow2, R.id.shadow3, R.id.shadow4, R.id.shadow5,
            R.id.shadow6, R.id.shadow7, R.id.shadow8, R.id.shadow9};
    public static AtomicInteger octave=new AtomicInteger();   // 옥타브 설정값  ( -2 ~ +3 )
    public static AtomicBoolean sustain=new AtomicBoolean(); // 서스테인 설정값
    private int BPM;  // 메트로놈을 위한 BPM값
    private int metronome_count=0;      // 메트로놈 박자 세는 기준
    private int metronome_maxcount=3;   // 0이면 메트로놈 꺼짐 3이면 3/4박자 4면 4/4박자
    public boolean isIconLeft=true; // 메트로놈 아이콘 좌우 변경 플래그
    private ScheduledExecutorService metronomeService;

    public static boolean isPushed[]; // 건반이 눌린 상태인지 확인

    @Override
    protected void onDestroy() {
        soundmanager.unLoad(keyboardSoundPool); // 액티비티 종료 시 사운드 리소스 언로드
        if(metronome_maxcount!=0)
            metronomeService.shutdownNow();
        soundmanager.unLoad(metronomeSoundPool);
        super.onDestroy();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        soundmanager=new SoundResourceManager();    // 음원 파일 관리 객체
        soundKeys=new int[88];      // 건반 소리 재생을 위한 key값 저장 배열
        metronomesoundKeys=new int[2];      // 메트로놈 소리 재생을 위한 key값 저장 배열
        isPushed=new boolean[88]; // 건반이 눌려있는지 확인
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
        /////////////////////////////앱 하단바 제거/////////////////////////////// // 앱 하단바 제거
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_keyboard);
        goBack=findViewById(R.id.keyboard_goBack);
        btnSustain=findViewById(R.id.btnSustain);
        octaveValue=findViewById(R.id.octaveValue);
        btnOctave=findViewById(R.id.btnOctave);
        keyboardTopBanner=findViewById(R.id.keyboard_Toplayout);
        BPMText=findViewById(R.id.metronome_bpm);
        metronome_seekbar=findViewById(R.id.metronome_seekbar);
        bpmUp=findViewById(R.id.metronome_bpmup);
        bpmDown=findViewById(R.id.metronome_bpmdown);
        btn_metronome_button=findViewById(R.id.metronome_button);
        btn_metronome_icon=findViewById(R.id.metronome_icon);
        btn_metronome_mode=findViewById(R.id.metronome_mode);
        touch_improve=findViewById(R.id.touch_improve);
        BPM=120;
        Animation animation[]= new Animation[25];
        for(int i=0; i<25; i++) {
            animation[i] = new AlphaAnimation(0, 1);
            animation[i].setDuration(100 + i * 90);
        }

        ///////////////////// 뒷 배경 터치 시 건반 동작 안하는 현상 개선//////////////////////
        keyboardTopBanner.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return true;
            }
        });
        touch_improve.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return true;
            }
        });
        //////////////////////////////////////////////////////////////////////////////////
        // 건반 백 15개, 흑 10개
        for(int i=0; i<15;i++) {
            wKeys[i] = findViewById(wButtonID[i]);
            wKeys[i].setVisibility(View.VISIBLE);
            wKeys[i].setAnimation(animation[i]);
        }
        for(int i=0; i<10;i++){
            bKeys[i]=findViewById(bButtonID[i]);
            shadow[i]=findViewById(shadowID[i]);
            bKeys[i].setVisibility(View.VISIBLE);
            shadow[i].setVisibility(View.VISIBLE);
            bKeys[i].setAnimation(animation[i+15]);
            shadow[i].setAnimation(animation[i+15]);
        }
        sustain.set(false);
        octave.set(0);
        Intent intent = getIntent();
        inst=intent.getIntExtra("inst", 0);  // 선택한 악기 가져옴
        keyboardSoundPool=soundmanager.load(soundKeys, inst,this ); // 선택한 악기의 음원 파일 로딩
        if(inst==0) keyboardTopBanner.setBackground(getResources().getDrawable(R.drawable.keyboard_backgroundpiano));
        else if(inst==1) keyboardTopBanner.setBackground(getResources().getDrawable(R.drawable.keyboard_backgroundviolin));
        else if(inst==2) keyboardTopBanner.setBackground(getResources().getDrawable(R.drawable.keyboard_backgroundharp));
        goBack.setOnTouchListener(new View.OnTouchListener() { // 뒤로가기 버튼 이미지 변환 효과
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if(motionEvent.getAction()==MotionEvent.ACTION_DOWN)
                    goBack.setBackgroundResource(R.drawable.back_p);
                else if(motionEvent.getAction()==MotionEvent.ACTION_UP)
                    goBack.setBackgroundResource(R.drawable.back);
                return false;
            }
        });
        goBack.setOnClickListener(new View.OnClickListener() {  // 뒤로가기 버튼
            @Override
            public void onClick(View view) {
                finish();
                overridePendingTransition(R.anim.zoomout_enter, R.anim.zoomout_exit);
            }
        });

        //////////////////////////////////서스테인 기능///////////////////////////////////////////
        btnSustain.setOnClickListener(new View.OnClickListener() { //서스테인 버튼
            @Override
            public void onClick(View view) {
                if(!sustain.get()) {
                    btnSustain.setBackgroundResource(R.drawable.keyboard_sustain_on);
                    sustain.set(true);
                }
                else {
                    btnSustain.setBackgroundResource(R.drawable.keyboard_sustain_off);
                    sustain.set(false);
                    for(int i=0; i<88; i++) { // 서스테인 기능 껐을 때 눌려있지 않은 건반의 소리만 끔
                        if(isPushed[i]==false)
                            PlayNote.noteOff(keyboardSoundPool, i);
                    }
                }
            }
        });
        btnSustain.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if(motionEvent.getAction()==MotionEvent.ACTION_DOWN){
                    if(!sustain.get())  btnSustain.setBackgroundResource(R.drawable.keyboard_sustain_off_p);
                    else btnSustain.setBackgroundResource(R.drawable.keyboard_sustain_on_p);
                }
                else if(motionEvent.getAction()==MotionEvent.ACTION_UP){
                    if(!sustain.get())  btnSustain.setBackgroundResource(R.drawable.keyboard_sustain_off);
                    else btnSustain.setBackgroundResource(R.drawable.keyboard_sustain_on);
                }
                return false;
            }
        });

        /////////////////////////////////////////////////////////////////////////////////////////
        ///////////////////////////////////옥타브 조절 기능///////////////////////////////////////
        btnOctave.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if(motionEvent.getAction()==MotionEvent.ACTION_DOWN){
                    float touchedX=motionEvent.getX(); // 터치된 X좌표 받아서 작업 결정
                    float value=touchedX/view.getWidth();   // 0.5 이상이면 + 이하면 -
                    int currentOctave=octave.get();
                    if(value>0.5 && currentOctave<3){
                        btnOctave.setBackgroundResource(R.drawable.keyboard_octave_plus_p);
                        octave.set(currentOctave+1);
                        if(currentOctave+1>=0)
                            octaveValue.setText("+"+Integer.toString(currentOctave+1));
                        else
                            octaveValue.setText(Integer.toString(currentOctave+1));
                    }
                    else if(value<0.5 && currentOctave>-2){
                        btnOctave.setBackgroundResource(R.drawable.keyboard_octave_minus_p);
                        octave.set(currentOctave-1);
                        if(currentOctave-1>=0)
                            octaveValue.setText("+"+Integer.toString(currentOctave-1));
                        else
                            octaveValue.setText(Integer.toString(currentOctave-1));
                    }

                }
                else if(motionEvent.getAction()==MotionEvent.ACTION_UP){
                    btnOctave.setBackgroundResource(R.drawable.keyboard_octave);
                }
                return true; // false 리턴하면 옥타브 이미지버튼 누른 상태로 건반버튼 눌러도 반응을 안함
            }
        });
        //////////////////////////////////////////////////////////////////////////////////////////

        ////////////////////////////////////////메트로놈 기능////////////////////////////////////////

        btn_metronome_icon.setBackgroundResource(R.drawable.metronome_icon_l);
        isIconLeft=true;
        metronomeSoundPool=soundmanager.loadMetronomeSound(metronomesoundKeys,this);
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
                    metronome_count=0;
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

        //////////////////////////////////////////////////////////////////////////////////////////

        // 각 건반을 담당하는 버튼들에 리스너 추가
        KeyBoardListener keyBoardListeners[] = new KeyBoardListener[25];
        int wkeypitch[]={27, 29, 31, 32, 34, 36, 38, 39, 41, 43, 44, 46, 48, 50, 51};
        int bkeypitch[]={28,30,33,35,37,40,42,45,47,49};
        boolean isViolin=false;
        if(inst==1)
            isViolin=true;
        for(int i=0; i<15; i++){    // 흰 건반
            keyBoardListeners[i]=new KeyBoardListener();
            keyBoardListeners[i].setInitSound(keyboardSoundPool, soundKeys, wkeypitch[i], true, isViolin);
            wKeys[i].setOnTouchListener(keyBoardListeners[i]);
        }
        for(int i=15; i<25; i++){   // 검은 건반
            keyBoardListeners[i]=new KeyBoardListener();
            keyBoardListeners[i].setInitSound(keyboardSoundPool, soundKeys, bkeypitch[i-15], false, isViolin);
            bKeys[i-15].setOnTouchListener(keyBoardListeners[i]);
        }


    }

}