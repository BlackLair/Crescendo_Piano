package com.example.crescendo_piano;

import android.content.Context;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;

public class SoundResourceManager {
    private int soundIDs[];
    public SoundPool load(int[] keys, int inst, Context context){
        SoundPool tempSoundPool;
        soundIDs=new int[89];
        if(inst==0) {                // 선택한 악기에 맞는 리소스 파일 id를 가져옴
            for (int i = 0; i < 88; i++)
                soundIDs[i] = context.getResources().getIdentifier("@raw/p" + Integer.toString(i), "raw", context.getPackageName());
        }else if(inst==1){
            for (int i = 0; i < 88; i++)
                soundIDs[i] = context.getResources().getIdentifier("@raw/v" + Integer.toString(i), "raw", context.getPackageName());
        }else if(inst==2){
            for (int i = 0; i < 88; i++)
                soundIDs[i] = context.getResources().getIdentifier("@raw/h" + Integer.toString(i), "raw", context.getPackageName());
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {               // API 21 이후
            AudioAttributes audioAttributes = new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_GAME)
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .build();
            tempSoundPool = new SoundPool.Builder().setAudioAttributes(audioAttributes).setMaxStreams(128).build();
        } else {                // API 21 미만
            tempSoundPool = new SoundPool(128, AudioManager.STREAM_MUSIC, 0);
        }
        for (int i = 0; i < 88; i++)
            keys[i] = tempSoundPool.load(context, soundIDs[i], 1);  // 사운드 리소스를 메모리에 로드하고 재생을 위한 key값 저장
        return tempSoundPool;       // SoundPool 리턴
    }
    public SoundPool loadDrumSound(int[][] keys, Context context){ // 드럼(8개짜리) 사운드 로딩
        SoundPool tempSoundPool;
        int drumSoundIDs[][]={{R.raw.d_crash, R.raw.d_tom1, R.raw.d_tom2, R.raw.d_ride, R.raw.d_closedhat, R.raw.d_snare, R.raw.d_kick, R.raw.d_tom3}
                            , {R.raw.dd_crash, R.raw.dd_sfx, R.raw.dd_littlesnare, R.raw.dd_ride, R.raw.dd_hihat, R.raw.dd_snare, R.raw.dd_kick, R.raw.dd_snaph}};
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {               // API 21 이후
            AudioAttributes audioAttributes = new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_GAME)
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .build();
            tempSoundPool = new SoundPool.Builder().setAudioAttributes(audioAttributes).setMaxStreams(64).build();
        } else {                // API 21 미만
            tempSoundPool = new SoundPool(64, AudioManager.STREAM_MUSIC, 0);
        }
        for (int i = 0; i < 8; i++) {
            keys[0][i] = tempSoundPool.load(context, drumSoundIDs[0][i], 1);  // 사운드 리소스를 메모리에 로드하고 재생을 위한 key값 저장
            keys[1][i] = tempSoundPool.load(context, drumSoundIDs[1][i], 1);
        }
        return tempSoundPool;       // SoundPool 리턴
    }

    public SoundPool loadMetronomeSound(int[] keys, Context context){
        SoundPool tempSoundPool;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {               // API 21 이후
            AudioAttributes audioAttributes = new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_GAME)
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .build();

            tempSoundPool = new SoundPool.Builder().setAudioAttributes(audioAttributes).setMaxStreams(8).build();
        } else {                // API 21 미만
            tempSoundPool = new SoundPool(8, AudioManager.STREAM_MUSIC, 0);
        }
        keys[0] = tempSoundPool.load(context, R.raw.metronome0, 1);  // 사운드 리소스를 메모리에 로드하고 재생을 위한 key값 저장
        keys[1] = tempSoundPool.load(context, R.raw.metronome1, 1);
        return tempSoundPool;       // SoundPool 리턴


    }
    public void unLoad(SoundPool spools){ // soundpool 리소스 해제
        spools.release();
        spools=null;
    }
}
