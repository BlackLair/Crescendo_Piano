package com.example.crescendo_piano;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.SoundPool;
import android.os.Build;
import android.os.Bundle;
import android.view.View;

public class KeyboardActivity extends AppCompatActivity {
    private View decorView;
    private int uiOption;
    private int inst; // 0 : 피아노 1 : 바이올린 2 : 하프
    private SoundResourceManager soundmanager;
    private int soundKeys[];
    private SoundPool keyboardSoundPool;
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

        Intent intent = new Intent();
        intent.getIntExtra("inst", 0);
        keyboardSoundPool=soundmanager.load(soundKeys, inst,this );


    }
}