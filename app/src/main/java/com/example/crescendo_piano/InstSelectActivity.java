package com.example.crescendo_piano;

// 악기를 선택하기 위한 화면 액티비티.

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;

public class InstSelectActivity extends AppCompatActivity {
    private View decorView; // 전체화면 출력을 위한 멤버 변수
    private int uiOption;
    private ImageButton instsel_goBack; // 뒤로가기 버튼
    private Intent intent;  // 메인메뉴에서 연주하기/MIDI연결 중 어떤 것을 선택했는지 받아오기 위한 인텐트
    private Integer selectedFunc; // 0 : 연주하기 1 : MIDI장치 연결
    ImageButton selPiano, selViolin, selHarp, selDrum;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inst_select);

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
        intent=getIntent();
        selectedFunc=intent.getIntExtra("selectInst", 0); // 메인메뉴에서 어떤 메뉴 선택했는지 읽음
        instsel_goBack=(ImageButton)findViewById(R.id.instsel_goBack);
        selPiano=(ImageButton)findViewById(R.id.inst_Piano);
        selViolin=(ImageButton)findViewById(R.id.inst_Violin);
        selHarp=(ImageButton)findViewById(R.id.inst_Harp);
        selDrum=(ImageButton)findViewById(R.id.inst_Drum);

        instsel_goBack.setOnTouchListener(new View.OnTouchListener() {  // 뒤로가기 버튼의 이미지 변환 효과
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if(motionEvent.getAction()==MotionEvent.ACTION_DOWN)
                    instsel_goBack.setBackgroundResource(R.drawable.backgray);
                else if(motionEvent.getAction()==MotionEvent.ACTION_UP)
                    instsel_goBack.setBackgroundResource(R.drawable.back);
                return false;
            }
        });
        instsel_goBack.setOnClickListener(new View.OnClickListener() {  // 뒤로가기 버튼
            @Override
            public void onClick(View view) {
                Intent backToMain = new Intent(InstSelectActivity.this, MainActivity.class);
                startActivity(backToMain);
                finish();
            }
        });

        selPiano.setOnClickListener(new View.OnClickListener() {    // 피아노 연주 선택
            @Override
            public void onClick(View view) {
                if(selectedFunc==0) {   // 메인메뉴에서 연주하기로 접근했을 경우
                    Intent intent = new Intent(InstSelectActivity.this, KeyboardActivity.class);
                    intent.putExtra("inst", 0); // 선택한 악기 종류
                    startActivity(intent);
                    finish();
                }
                else{
                    Intent intent=new Intent(InstSelectActivity.this, MidiActivity.class);
                    intent.putExtra("inst", 0);
                    startActivity(intent);
                    finish();
                }
            }
        });
        selViolin.setOnClickListener(new View.OnClickListener() {    // 피아노 연주 선택
            @Override
            public void onClick(View view) {
                if(selectedFunc==0) {   // 메인메뉴에서 연주하기로 접근했을 경우
                    Intent intent = new Intent(InstSelectActivity.this, KeyboardActivity.class);
                    intent.putExtra("inst", 1); // 선택한 악기 종류
                    startActivity(intent);
                    finish();
                }
                else{
                    Intent intent=new Intent(InstSelectActivity.this, MidiActivity.class);
                    intent.putExtra("inst", 1);
                    startActivity(intent);
                    finish();
                }
            }
        });
        selHarp.setOnClickListener(new View.OnClickListener() {    // 피아노 연주 선택
            @Override
            public void onClick(View view) {
                if(selectedFunc==0) {   // 메인메뉴에서 연주하기로 접근했을 경우
                    Intent intent = new Intent(InstSelectActivity.this, KeyboardActivity.class);
                    intent.putExtra("inst", 2); // 선택한 악기 종류
                    startActivity(intent);
                    finish();
                }
                else{
                    Intent intent=new Intent(InstSelectActivity.this, MidiActivity.class);
                    intent.putExtra("inst", 2);
                    startActivity(intent);
                    finish();
                }
            }
        });
        selDrum.setOnClickListener(new View.OnClickListener() {    // 피아노 연주 선택
            @Override
            public void onClick(View view) {
                if(selectedFunc==0) {
                    Intent intent = new Intent(InstSelectActivity.this, DrumPadActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        });

    }
}