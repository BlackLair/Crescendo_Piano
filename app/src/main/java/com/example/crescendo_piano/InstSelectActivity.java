package com.example.crescendo_piano;

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
    private ImageButton instsel_goBack;
    private Intent intent;
    private Integer selectInst;
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
        selectInst=intent.getIntExtra("selectInst", 0);
        instsel_goBack=(ImageButton)findViewById(R.id.instsel_goBack);

        instsel_goBack.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if(motionEvent.getAction()==MotionEvent.ACTION_DOWN)
                    instsel_goBack.setBackgroundResource(R.drawable.backgray);
                else if(motionEvent.getAction()==MotionEvent.ACTION_UP)
                    instsel_goBack.setBackgroundResource(R.drawable.back);
                return false;
            }
        });
        instsel_goBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent backToMain = new Intent(InstSelectActivity.this, MainActivity.class);
                startActivity(backToMain);
                finish();
            }
        });

    }
}