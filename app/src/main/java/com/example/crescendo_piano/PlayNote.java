package com.example.crescendo_piano;

import android.media.SoundPool;

public class PlayNote {
   public static int[] streamID= new int[89];
    public static void noteOn(SoundPool spool, int key, float velocity){
        streamID[key]=spool.play(key, velocity, velocity, 0, 0, 1);
    }
    public static void noteOff(SoundPool spool, int key){

        spool.stop(streamID[key]);

    }
}
