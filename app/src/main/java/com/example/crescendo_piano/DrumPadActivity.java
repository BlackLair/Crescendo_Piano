package com.example.crescendo_piano;
// 화면 상에서 드럼 연주하는 액티비티
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.SoundPool;
import android.os.Build;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageButton;

public class DrumPadActivity extends AppCompatActivity {
    private View decorView;
    private int uiOption;
    ImageButton btn_goBack;
    private ImageButton btnDrum[]=new ImageButton[8];
    private SoundResourceManager soundManager;
    private int soundKeys[];
    private SoundPool drumPadSoundPool;
    private int drumId[] = {R.id.drum_crush,R.id.drum_htom,R.id.drum_mtom,R.id.drum_ride,
            R.id.drum_hihat,R.id.drum_snare,R.id.drum_kick,R.id.drum_ltom};
    private int drumImageId[]={R.drawable.drum_crash_up, R.drawable.drum_crash_down,
            R.drawable.drum_htom_up,R.drawable.drum_htom_down, R.drawable.drum_mtom_up,
            R.drawable.drum_mtom_down, R.drawable.drum_ride_up, R.drawable.drum_ride_down,
            R.drawable.drum_hihat_up, R.drawable.drum_hihat_down, R.drawable.drum_snare_up,
            R.drawable.drum_snare_down, R.drawable.drum_kick_up, R.drawable.drum_kick_down,
            R.drawable.drum_ltom_up, R.drawable.drum_ltom_down};
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

        soundManager=new SoundResourceManager();
        soundKeys=new int[8];   // 드럼소리의 종류는 8가지
        drumPadSoundPool=soundManager.loadDrumSound(soundKeys,getApplicationContext()); // 드럼 사운드 로딩

        for(int i=0;i<drumId.length;i++){
            int index=i;
            float drumVolumnOffset;
            if(i==6) drumVolumnOffset=0.15f; //킥 소리 증폭
            else if(i==4 || i==0) drumVolumnOffset=-0.15f; // 하이햇,크래시 소리 감소
            else drumVolumnOffset=0;
            btnDrum[i]=findViewById(drumId[i]);
            Animation animation=new AlphaAnimation(0,1); // 처음 시작 시 드럼패드 천천히 나타나는 애니메이션
            animation.setDuration(1800);
            btnDrum[i].setAnimation(animation);
            btnDrum[i].setVisibility(View.VISIBLE);
            btnDrum[i].setOnTouchListener(new View.OnTouchListener() { // 드럼패드 버튼 리스너
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    if(motionEvent.getAction()==MotionEvent.ACTION_DOWN){
                        if(index==0 || index==3) // 크래시, 라이드 이전 소리 끊고 다시재생
                            PlayNote.drumNoteOff(drumPadSoundPool,index);
                        PlayNote.drumNoteOn(drumPadSoundPool,soundKeys[index],index,0.8f+drumVolumnOffset);
                        view.setBackgroundResource(drumImageId[index*2+1]); // 터치 시 누른 이미지로 교체
                    }
                    else if(motionEvent.getAction()==MotionEvent.ACTION_UP){ // 손 떼면 이미지 원상복귀
                        view.setBackgroundResource(drumImageId[index*2]);
                    }
                    return true;
                }
            });
        }

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