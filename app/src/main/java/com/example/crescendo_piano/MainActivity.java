package com.example.crescendo_piano;

// 메인 메뉴 화면을 출력하는 액티비티

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {
    private View decorView;
    private int uiOption;
    ImageButton main_onapp, main_onmidi, main_code;
    Intent intent;
    private Integer selectedMode; //0 : app 연주  1 : MIDI 장치 연주


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        /////////////////////////////앱 하단바 제거///////////////////////////////
        decorView=getWindow().getDecorView();
        uiOption=getWindow().getDecorView().getSystemUiVisibility();
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.ICE_CREAM_SANDWICH)
            uiOption|=View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.JELLY_BEAN)
            uiOption|=View.SYSTEM_UI_FLAG_FULLSCREEN;
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.KITKAT)
            uiOption|=View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
        decorView.setSystemUiVisibility(uiOption);
        /////////////////////////////앱 하단바 제거/////////////////////////////// // 앱 하단바 제거
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        main_onapp=(ImageButton)findViewById(R.id.main_onapp);
        main_onmidi=(ImageButton)findViewById(R.id.main_onmidi);
        main_code=(ImageButton)findViewById(R.id.main_code);

        main_onapp.setOnClickListener(new View.OnClickListener() {  // 연주하기 버튼
            @Override
            public void onClick(View view) {
                selectedMode=0;
                intent=new Intent(MainActivity.this, InstSelectActivity.class);
                intent.putExtra("selectInst", selectedMode);
                startActivity(intent);
                finish();
            }
        });
        main_onapp.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if(motionEvent.getAction() == MotionEvent.ACTION_DOWN){
                    main_onapp.setBackgroundResource(R.drawable.main_onapp_p);
                }
                else if(motionEvent.getAction()==MotionEvent.ACTION_UP){
                    main_onapp.setBackgroundResource(R.drawable.main_onapp);
                }
                return false;
            }
        });
        main_onmidi.setOnClickListener(new View.OnClickListener() { // MIDI장치 연결 버튼
            @Override
            public void onClick(View view) {
                selectedMode=1;
                intent=new Intent(MainActivity.this, InstSelectActivity.class);
                intent.putExtra("selectInst", selectedMode);
                startActivity(intent);
                finish();
            }
        });
        main_onmidi.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if(motionEvent.getAction() == MotionEvent.ACTION_DOWN){
                    main_onmidi.setBackgroundResource(R.drawable.main_onmidi_p);
                }
                else if(motionEvent.getAction()==MotionEvent.ACTION_UP){
                    main_onmidi.setBackgroundResource(R.drawable.main_onmidi);
                }
                return false;
            }
        });
        main_code.setOnClickListener(new View.OnClickListener() { // 코드 레시피 버튼
            @Override
            public void onClick(View view) {
                intent=new Intent(MainActivity.this, CodeSelectActivity.class);
                startActivity(intent);
                finish();
            }
        });
        main_code.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if(motionEvent.getAction() == MotionEvent.ACTION_DOWN){
                    main_code.setBackgroundResource(R.drawable.main_code_p);
                }
                else if(motionEvent.getAction()==MotionEvent.ACTION_UP){
                    main_code.setBackgroundResource(R.drawable.main_code);
                }
                return false;
            }
        });

    }


}