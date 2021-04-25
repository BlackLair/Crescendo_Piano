package com.example.crescendo_piano;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.SoundPool;
import android.os.Build;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class KeyboardActivity extends AppCompatActivity {
    private View decorView;
    private int uiOption;
    private int inst; // 0 : 피아노 1 : 바이올린 2 : 하프
    private SoundResourceManager soundmanager;
    private int soundKeys[];
    private SoundPool keyboardSoundPool;
    private ImageButton goBack, btnSustain;
    private ImageView btnOctave;
    private ImageButton wKeys[]=new ImageButton[15];
    private ImageButton bKeys[]=new ImageButton[10];
    private TextView octaveValue;
    private RelativeLayout keyboardTopBanner;
    // 각 건반 이미지버튼들의 id속성 저장
    private int wButtonID[]={R.id.btn_wkey0, R.id.btn_wkey1, R.id.btn_wkey2, R.id.btn_wkey3, R.id.btn_wkey4, R.id.btn_wkey5,
                            R.id.btn_wkey6, R.id.btn_wkey7, R.id.btn_wkey8, R.id.btn_wkey9, R.id.btn_wkey10, R.id.btn_wkey11,
                            R.id.btn_wkey12, R.id.btn_wkey13, R.id.btn_wkey14};
    private int bButtonID[]={R.id.btn_bkey0, R.id.btn_bkey1, R.id.btn_bkey2, R.id.btn_bkey3, R.id.btn_bkey4, R.id.btn_bkey5,
            R.id.btn_bkey6, R.id.btn_bkey7, R.id.btn_bkey8, R.id.btn_bkey9};
    public static AtomicInteger octave=new AtomicInteger();   // 옥타브 설정값  ( -2 ~ +3 )
    public static AtomicBoolean sustain=new AtomicBoolean(); // 서스테인 설정값

    @Override
    protected void onDestroy() {
        soundmanager.unLoad(keyboardSoundPool); // 액티비티 종료 시 사운드 리소스 언로드
        super.onDestroy();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        soundmanager=new SoundResourceManager();    // 음원 파일 관리 객체
        soundKeys=new int[88];      // 건반 소리 재생을 위한 key값 저장 배열
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
        // 건반 백 15개, 흑 10개
        for(int i=0; i<15;i++) wKeys[i]=findViewById(wButtonID[i]);
        for(int i=0; i<10;i++) bKeys[i]=findViewById(bButtonID[i]);
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
                    goBack.setBackgroundResource(R.drawable.backgray);
                else if(motionEvent.getAction()==MotionEvent.ACTION_UP)
                    goBack.setBackgroundResource(R.drawable.back);
                return false;
            }
        });
        goBack.setOnClickListener(new View.OnClickListener() {  // 뒤로가기 버튼
            @Override
            public void onClick(View view) {
                Intent intentBack=new Intent(KeyboardActivity.this, InstSelectActivity.class);
                startActivity(intentBack);
                finish();
            }
        });
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
                    for(int i=0; i<88; i++)
                        PlayNote.noteOff(keyboardSoundPool, i);
                }
            }
        });
        btnOctave.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if(motionEvent.getAction()==MotionEvent.ACTION_DOWN){
                    float touchedX=motionEvent.getX(); // 터치된 X좌표 받아서 작업 결정
                    float value=touchedX/view.getWidth();   // 0.5 이상이면 + 이하면 -
                    int currentOctave=octave.get();
                    if(value>0.5 && currentOctave<3){
                        octave.set(currentOctave+1);
                        if(currentOctave+1>=0)
                            octaveValue.setText("+"+Integer.toString(currentOctave+1));
                        else
                            octaveValue.setText(Integer.toString(currentOctave+1));
                    }
                    else if(value<0.5 && currentOctave>-2){
                        octave.set(currentOctave-1);
                        if(currentOctave-1>=0)
                            octaveValue.setText("+"+Integer.toString(currentOctave-1));
                        else
                            octaveValue.setText(Integer.toString(currentOctave-1));
                    }

                }
                return false;
            }
        });
        // 각 건반을 담당하는 버튼들에 리스너 추가
        KeyBoardListener keyBoardListeners[] = new KeyBoardListener[25];
        int wkeypitch[]={27, 29, 31, 32, 34, 36, 38, 39, 41, 43, 44, 46, 48, 50, 51};
        int bkeypitch[]={28,30,33,35,37,40,42,45,47,49};
        for(int i=0; i<15; i++){    // 흰 건반
            keyBoardListeners[i]=new KeyBoardListener();
            keyBoardListeners[i].setInitSound(keyboardSoundPool, soundKeys, wkeypitch[i], true);
            wKeys[i].setOnTouchListener(keyBoardListeners[i]);
        }
        for(int i=15; i<25; i++){   // 검은 건반
            keyBoardListeners[i]=new KeyBoardListener();
            keyBoardListeners[i].setInitSound(keyboardSoundPool, soundKeys, bkeypitch[i-15], false);
            bKeys[i-15].setOnTouchListener(keyBoardListeners[i]);
        }
    }

}