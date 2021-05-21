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
import android.widget.TextView;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

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
        String backColors[]={"#2D017770", "#2D007700", "#2D777700", "#2D780059", "#2D000077", "#2D770000", "#2D764C00"};
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

        ///////////////////////////텍스트파일 읽어오기//////////////////////////////////
        TextView code_info=findViewById(R.id.code_info);

        String infoStr=null;
        int[] txtID={R.raw.info_cresc_one, R.raw.info_cresc_two, R.raw.info_cresc_three
                , R.raw.info_london, R.raw.info_paris, R.raw.info_tokyo, R.raw.info_la};
        InputStream inputStream = getResources().openRawResource(txtID[selectedCode]); // 텍스트파일 스트림 열기
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        int i;
        try{
            i=inputStream.read();
            while(i != -1){ // txt파일을 끝까지 읽어들임
                byteArrayOutputStream.write(i);
                i=inputStream.read();
            }
            infoStr=new String(byteArrayOutputStream.toByteArray()); // 읽어들인 텍스트를 문자열로 저장
            inputStream.close(); //스트림 종료
        }catch (IOException e){
            e.printStackTrace();
        }
        code_info.setText(infoStr);

        //////////////////////////////////////////////////////////////////////////////

    }
}