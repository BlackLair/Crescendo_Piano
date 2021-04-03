package com.example.crescendo_piano;

import android.app.Activity;
import android.content.Intent;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;

// 로딩화면 구현 클래스
public class SplashActivity extends Activity {

    int key_initsound;
    SoundPool initsound;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState){
        initsound=new SoundPool(1,AudioManager.STREAM_MUSIC,0);
        super.onCreate(savedInstanceState);
        key_initsound=initsound.load(this, R.raw.initapp,1);



        try{
            Thread.sleep(2400);
        }catch(InterruptedException e){
            e.printStackTrace();
        }
        startActivity(new Intent(this, MainActivity.class));
        initsound.play(key_initsound,1,1,0,0,1);
        finish();
    }


}
