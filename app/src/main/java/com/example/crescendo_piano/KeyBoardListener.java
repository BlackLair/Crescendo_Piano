package com.example.crescendo_piano;

// 각 건반 이미지버튼에 부여할 리스너 클래스
// OnTouchListener 인터페이스를 구현

import android.graphics.Color;
import android.media.SoundPool;
import android.view.MotionEvent;
import android.view.View;

public class KeyBoardListener implements View.OnTouchListener {
    private SoundPool spool;
    private int soundkeys[];
    private int pitch;
    private boolean isWhite;
    private boolean isViolin;
    private int currentpitch;
    public void setInitSound(SoundPool spool, int soundkeys[], int pitch, boolean isWhite, boolean isViolin){ // 리스너에서 재생할 SoundPool 가져옴
        this.soundkeys=soundkeys;
        this.spool=spool;
        this.pitch=pitch;
        this.isWhite=isWhite;
        this.isViolin=isViolin;
    }
        @Override       // 건반 이미지뷰 터치리스너
        public boolean onTouch (View view, MotionEvent motionEvent){
            if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                float y = motionEvent.getY();   // 터치한 Y좌표와 건반 최대 Y좌표 이용해 벨로시티 값 계산
                float ymax=view.getHeight();
                y=y/ymax;
                currentpitch=pitch+KeyboardActivity.octave.get()*12;     // 옥타브 값 적용
                if(isViolin) y=y*0.65f;
                PlayNote.noteOff(spool, currentpitch);
                PlayNote.noteOn(spool, soundkeys[currentpitch], currentpitch, y);
                if(isWhite)
                    view.setBackgroundColor(Color.parseColor("#B0B0B0"));
                else
                    view.setBackgroundResource(R.drawable.key_black_push);
                KeyboardActivity.isPushed[currentpitch]=true;
            }
            if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                if (KeyboardActivity.sustain.get() == false ) {  // 서스테인 기능이 꺼져있을 때만 소리 정지
                        PlayNote.noteOff(spool, currentpitch);
                }
                if(isWhite)
                    view.setBackgroundColor(Color.WHITE);
                else
                    view.setBackgroundResource(R.drawable.key_black);
                KeyboardActivity.isPushed[currentpitch] = false;
            }
            return false;
        }

}