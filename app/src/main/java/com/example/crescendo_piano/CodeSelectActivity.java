package com.example.crescendo_piano;
// 코드 레시피를 선택하는 액티비티

import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import java.util.ArrayList;

public class CodeSelectActivity extends AppCompatActivity {
    private View decorView;
    private int uiOption;
    ViewPager2 codeViewPager;
    private ImageButton goBack;
    private LinearLayout code_background;

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
        setContentView(R.layout.activity_code_select);
        codeViewPager=findViewById(R.id.viewPager);
        ArrayList<CodeDataPage> list=new ArrayList<>();
        // 리스트에 버튼 정보 추가
        list.add(new CodeDataPage(R.drawable.code_cresc_one,0, this));    // crescendo style 1
        list.add(new CodeDataPage(R.drawable.code_cresc_two,1, this));    // crescendo style 2
        list.add(new CodeDataPage(R.drawable.code_cresc_three,2, this));  // crescendo style 3
        list.add(new CodeDataPage(R.drawable.code_london,3, this));       // london style
        list.add(new CodeDataPage(R.drawable.code_paris,4, this));        // paris style
        list.add(new CodeDataPage(R.drawable.code_tokyo,5, this));        // tokyo style
        list.add(new CodeDataPage(R.drawable.code_la,6, this));           // los angeles style

        codeViewPager.setAdapter(new CodeViewPagerAdapter(list));

        // 페이지 넘어갈 때 배경색 바꾸기
        code_background=findViewById(R.id.code_background);
        ValueAnimator colorAnimation;
        int colorfrom, colorto;
        colorfrom=((ColorDrawable)code_background.getBackground()).getColor();
        colorto= Color.parseColor("#5033FF33");
        colorAnimation=ValueAnimator.ofObject(new ArgbEvaluator(), colorfrom, colorto);
        colorAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                code_background.setBackgroundColor((int)valueAnimator.getAnimatedValue());
            }
        });
        codeViewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override   // 뷰페이저가 넘어갈 때마다 배경 색 애니메이션 넣어서 부드럽게 바뀜
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                switch(position){
                    case 0:
                        colorAnimation.setObjectValues(((ColorDrawable)code_background.getBackground()).getColor(), Color.parseColor("#5001FFF8"));
                        colorAnimation.setDuration(500);
                        colorAnimation.start();
                        break;
                    case 1:
                        colorAnimation.setObjectValues(((ColorDrawable)code_background.getBackground()).getColor(), Color.parseColor("#5033FF33"));
                        colorAnimation.setDuration(500);
                        colorAnimation.start();
                        break;
                    case 2:
                        colorAnimation.setObjectValues(((ColorDrawable)code_background.getBackground()).getColor(), Color.parseColor("#50FFFF00"));
                        colorAnimation.setDuration(500);
                        colorAnimation.start();
                        break;
                    case 3:
                        colorAnimation.setObjectValues(((ColorDrawable)code_background.getBackground()).getColor(), Color.parseColor("#50FF21FF"));
                        colorAnimation.setDuration(500);
                        colorAnimation.start();
                        break;
                    case 4:
                        colorAnimation.setObjectValues(((ColorDrawable)code_background.getBackground()).getColor(), Color.parseColor("#504444FF"));
                        colorAnimation.setDuration(500);
                        colorAnimation.start();
                        break;
                    case 5:
                        colorAnimation.setObjectValues(((ColorDrawable)code_background.getBackground()).getColor(), Color.parseColor("#50FF4444"));
                        colorAnimation.setDuration(500);
                        colorAnimation.start();
                        break;
                    case 6:
                        colorAnimation.setObjectValues(((ColorDrawable)code_background.getBackground()).getColor(), Color.parseColor("#50FFAA33"));
                        colorAnimation.setDuration(500);
                        colorAnimation.start();
                        break;
                }

            }
        });


        goBack=findViewById(R.id.code_goBack);
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
                Intent intentBack=new Intent(CodeSelectActivity.this, MainActivity.class);
                startActivity(intentBack);
                finish();
            }
        });

    }
}