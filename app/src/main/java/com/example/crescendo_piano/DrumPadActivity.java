package com.example.crescendo_piano;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

public class DrumPadActivity extends AppCompatActivity {
    private View decorView;
    private int uiOption;
    ImageButton btn_goBack;

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

        btn_goBack=(ImageButton)findViewById(R.id.drumpad_goBack);
        btn_goBack.setOnClickListener(new View.OnClickListener() {
            @Override       // 뒤로가기 버튼
            public void onClick(View view) {
                Intent intent = new Intent(DrumPadActivity.this, InstSelectActivity.class);
                intent.putExtra("selectInst", 0);
                startActivity(intent);
                finish();
            }
        });
    }
}