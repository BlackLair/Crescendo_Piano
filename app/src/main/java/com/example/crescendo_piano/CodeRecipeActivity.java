package com.example.crescendo_piano;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;

import kr.co.prnd.YouTubePlayerView;

public class CodeRecipeActivity extends AppCompatActivity {
    private View decorView;
    private int uiOption;
    private int selectedCode;
    private ImageButton goBack;
    private YouTubePlayerView youTubePlayerView;
    ConstraintLayout coderecipe_layout;
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
        setContentView(R.layout.activity_code_recipe);
        coderecipe_layout=(ConstraintLayout)findViewById(R.id.coderecipe_layout);
        Intent getCodeIntent=getIntent();
        selectedCode=getCodeIntent.getIntExtra("selectedCode", 0);
        String backColors[]={"#017770", "#007700", "#777700", "#780059", "#000077", "#770000", "#764C00"};
        String youTubeCode[]={"hFqt3KUZfKc","SxtUCmcqPi4","Gz5d61L6uBA","NkUCPB1EpBo","MMDppuUuImY","voujm_iqDIw","nmqMGHbHQQU"};
        coderecipe_layout.setBackgroundColor(Color.parseColor(backColors[selectedCode]));

        youTubePlayerView=findViewById(R.id.youtubeView);
        youTubePlayerView.play(youTubeCode[selectedCode], null);


        goBack=findViewById(R.id.coderecipe_goBack);
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
            }
        });

    }
}