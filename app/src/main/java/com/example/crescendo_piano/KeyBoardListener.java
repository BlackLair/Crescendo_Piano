package com.example.crescendo_piano;

import android.media.SoundPool;
import android.view.MotionEvent;
import android.view.View;

public class KeyBoardListener implements View.OnTouchListener {
    private SoundPool spool;
    private int soundkeys[];
    private int pitch;
    private boolean isWhite;
    public void setInitSound(SoundPool spool, int soundkeys[]){ // 리스너에서 재생할 SoundPool 가져옴
        this.soundkeys=soundkeys;
        this.spool=spool;
    }
    public void setValue(int pitch, boolean isWhite) {  // 각 건반의 리스너에 기본 pitch값과 건반색깔 정보 가져옴
        this.pitch=pitch;
        this.isWhite=isWhite;

    }
        @Override       // 건반 이미지뷰 터치리스너
        public boolean onTouch (View view, MotionEvent motionEvent){
            pitch=pitch+KeyboardActivity.octave*12;     // 옥타브 값 적용
            if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                float y = motionEvent.getY();   // 터치된 y좌표 특정해서 건반 색에 따라 절대적 벨로시티 계산
                if(isWhite)
                    y/=745;
                else
                    y/=450;
                y *= y; // 벨로시티 조절
                y+=0.07;
                PlayNote.noteOff(spool, soundkeys[pitch]);
                PlayNote.noteOn(spool, soundkeys[pitch], y);
            } else if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                if (KeyboardActivity.sustain == false)  // 서스테인 기능이 꺼져있을 때만 소리 정지
                    PlayNote.noteOff(spool, soundkeys[pitch]);
            }
            return false;
        }

}