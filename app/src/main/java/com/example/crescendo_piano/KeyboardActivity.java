package com.example.crescendo_piano;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.SoundPool;
import android.os.Build;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;

public class KeyboardActivity extends AppCompatActivity {
    private View decorView;
    private int uiOption;
    private int inst; // 0 : 피아노 1 : 바이올린 2 : 하프
    private SoundResourceManager soundmanager;
    private int soundKeys[];
    private SoundPool keyboardSoundPool;
    private ImageButton goBack;
    private ImageButton wKeys[]=new ImageButton[15];
    private ImageButton bKeys[]=new ImageButton[10];
    private int wButtonID[]={R.id.btn_wkey0, R.id.btn_wkey1, R.id.btn_wkey2, R.id.btn_wkey3, R.id.btn_wkey4, R.id.btn_wkey5,
                            R.id.btn_wkey6, R.id.btn_wkey7, R.id.btn_wkey8, R.id.btn_wkey9, R.id.btn_wkey10, R.id.btn_wkey11,
                            R.id.btn_wkey12, R.id.btn_wkey13, R.id.btn_wkey14};
    private int bButtonID[]={R.id.btn_bkey0, R.id.btn_bkey1, R.id.btn_bkey2, R.id.btn_bkey3, R.id.btn_bkey4, R.id.btn_bkey5,
            R.id.btn_bkey6, R.id.btn_bkey7, R.id.btn_bkey8, R.id.btn_bkey9};
    private int octave=0;
    private boolean sustain=false;

    @Override
    protected void onDestroy() {
        soundmanager.unLoad(keyboardSoundPool); // 액티비티 종료 시 사운드 리소스 언로드
        super.onDestroy();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        soundmanager=new SoundResourceManager();
        soundKeys=new int[88];
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
        for(int i=0; i<15;i++) wKeys[i]=findViewById(wButtonID[i]);
        for(int i=0; i<10;i++) bKeys[i]=findViewById(bButtonID[i]);


        Intent intent = new Intent();
        intent.getIntExtra("inst", 0);
        keyboardSoundPool=soundmanager.load(soundKeys, inst,this );

        goBack.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if(motionEvent.getAction()==MotionEvent.ACTION_DOWN)
                    goBack.setBackgroundResource(R.drawable.backgray);
                else if(motionEvent.getAction()==MotionEvent.ACTION_UP)
                    goBack.setBackgroundResource(R.drawable.back);
                return false;
            }
        });
        goBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentBack=new Intent(KeyboardActivity.this, InstSelectActivity.class);
                startActivity(intentBack);
                finish();
            }
        });



        KeyBoardListener keyBoardListeners[] = new KeyBoardListener[25];
        int wkeyid[]={27, 29, 31, 32, 34, 36, 38, 39, 41, 43, 44, 46, 48, 50, 51};
        int bkeyid[]={28,30,33,35,37,40,42,45,47,49};
        for(int i=0; i<15; i++){
            keyBoardListeners[i]=new KeyBoardListener();
            keyBoardListeners[i].setInitSound(keyboardSoundPool, soundKeys);
            keyBoardListeners[i].setValue(wkeyid[i],sustain,octave,true);
            wKeys[i].setOnTouchListener(keyBoardListeners[i]);
        }
        for(int i=0; i<10; i++){
            keyBoardListeners[i+15]=new KeyBoardListener();
            keyBoardListeners[i+15].setInitSound(keyboardSoundPool, soundKeys);
            keyBoardListeners[i+15].setValue(bkeyid[i],sustain,octave,false);
            bKeys[i].setOnTouchListener(keyBoardListeners[i+15]);
        }







    }
}