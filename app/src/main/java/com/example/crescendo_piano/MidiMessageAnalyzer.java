package com.example.crescendo_piano;
// 들어온 MIDI 신호를 분석하여 알맞는 소리 출력
import android.content.Context;
import android.media.SoundPool;
import android.widget.Toast;

import java.util.Stack;

public class MidiMessageAnalyzer {
    private int pedalFlag;      // 0 : 페달 뗌  1 : 페달 밟음
    private int[] isKeyOn;      // 0 : 건반 똄  0 : 건반 누름
    private Stack<Integer> relNote=new Stack<>(); // 페달 밟는도중 떼어진 건반 정보
    private Context context;
    public MidiMessageAnalyzer(Context context){
        this.context=context;
        isKeyOn=new int[88];
        for(int i=0; i<88; i++)
            isKeyOn[i]=0;
    }

    public void AnalyzeNote(String receivedDataString){
        int pitch;
        float velocity;
        int mChannel;

        pitch=getPitch(receivedDataString);
        velocity=getVelocity(receivedDataString);
        mChannel=getChannel(receivedDataString);

        if(mChannel==((MidiActivity)context).keyboardChannel && (pitch>=0 && pitch<88)){ // 입력 신호 채널이 설정한 건반채널일 때
            if(receivedDataString.substring(2,3).equals("9")){ //건반 누른 신호일 때
                PlayNote.noteOff(((MidiActivity)context).midiSoundPool, pitch);
                PlayNote.noteOn(((MidiActivity)context).midiSoundPool, ((MidiActivity)context).soundKeys[pitch], pitch, velocity);
                isKeyOn[pitch]=1;
            }else if(receivedDataString.substring(2,3).equals("8")){ // 건반 떼는 신호일 때
                if(pedalFlag==0){   //페달이 밟혀있지 않으면 소리 정지
                    PlayNote.noteOff(((MidiActivity)context).midiSoundPool, pitch);
                    isKeyOn[pitch]=0;
                }else{              // 페달이 밟혀있으면 해당 건반 정보 스택에 저장
                    isKeyOn[pitch]=2;
                    relNote.push(pitch);
                }
            }else if(receivedDataString.substring(4,6).equals("40")){ // 페달 신호일 경우
                if(receivedDataString.substring(6,8).equals("7f")){ // 페달 밟을 경우
                    pedalFlag=1;
                }else{  // 페달에서 발을 뗐을 경우
                    while(!relNote.empty()){
                        int pit=relNote.pop();
                        if(isKeyOn[pit]!=1){ // 해당 건반이 눌려있지 않은 경우 소리 정지
                            isKeyOn[pit]=0;
                            PlayNote.noteOff(((MidiActivity)context).midiSoundPool,pit);
                        }
                    }
                    pedalFlag=0;
                }
            }
        }
        else if(mChannel==((MidiActivity)context).drumChannel){
            // 드럼 연주 기능
        }



    }
    public int getPitch(String receivedDataString){ // 누른 건반 pitch 가져옴
        return Integer.parseInt(receivedDataString.substring(4,6), 16)-21;
    }
    public float getVelocity(String receivedDataString){ // 건반 누른 세기 가져옴
        return (float)Integer.parseInt(receivedDataString.substring(6,8), 16)/128;
    }
    public int getChannel(String receivedDataString){ // MIDI 신호의 채널 가져옴 (1~16)
        return Integer.parseInt(receivedDataString.substring(3,4), 16)+1;
    }
}
