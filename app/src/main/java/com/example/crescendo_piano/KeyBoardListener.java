package com.example.crescendo_piano;

import android.media.SoundPool;
import android.view.MotionEvent;
import android.view.View;

public class KeyBoardListener implements View.OnTouchListener {
    private SoundPool spool;
    private int soundkeys[];
    private int pitch;
    private boolean isWhite;
    public void setInitSound(SoundPool spool, int soundkeys[]){
        this.soundkeys=soundkeys;
        this.spool=spool;
    }
    public void setValue(int pitch, boolean isWhite) {
        this.pitch=pitch;
        this.isWhite=isWhite;

    }
        @Override
        public boolean onTouch (View view, MotionEvent motionEvent){
            pitch=pitch+KeyboardActivity.octave*12;
            if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                float y = motionEvent.getY();
                if(isWhite)
                    y/=745;
                else
                    y/=450;
                y *= y;
                PlayNote.noteOff(spool, soundkeys[pitch]);
                PlayNote.noteOn(spool, soundkeys[pitch], y);
            } else if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                if (KeyboardActivity.sustain == false)
                    PlayNote.noteOff(spool, soundkeys[pitch]);
            }
            return false;
        }

}