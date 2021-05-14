package com.example.crescendo_piano;

import android.content.Context;
import android.content.Intent;
import android.inputmethodservice.Keyboard;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

public class InstViewHolderPage extends RecyclerView.ViewHolder {
    private ImageButton InstButton;
    InstDataPage data;

    InstViewHolderPage(View itemView){
        super(itemView);
        InstButton=itemView.findViewById(R.id.inst_button);
    }
    public void onBind(InstDataPage data){
        this.data=data;
        int selectedInst=data.getSelectedInst();   // 선택한 악기
        int selectedFunc=data.getSelectedFunc();    // 화면상 연주/MIDI장치 연결 구분
        Context context=data.getContext();
        switch(selectedInst){ // 악기 버튼 이미지 설정
            case 0:
                InstButton.setBackgroundResource(R.drawable.app_sel_piano);
                break;
            case 1:
                InstButton.setBackgroundResource(R.drawable.app_sel_violin);
                break;
            case 2:
                InstButton.setBackgroundResource(R.drawable.app_sel_harp);
                break;
            case 3:
                InstButton.setBackgroundResource(R.drawable.app_sel_drum);
        }
        InstButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {    // 코드 레시피 버튼 클릭 시
                if(selectedFunc==0){ // 화면 상 연주 기능일 경우
                    if(selectedInst!=3) {
                        Intent intent = new Intent(context, KeyboardActivity.class);
                        intent.putExtra("inst", selectedInst);
                        context.startActivity(intent);
                    }
                    else{
                        Intent intent = new Intent(context, DrumPadActivity.class);
                        intent.putExtra("inst", selectedInst);
                        context.startActivity(intent);
                    }

                }
                else{   // MIDI 장치 연주일 경우
                    Intent intent=new Intent(context, MidiActivity.class);
                    intent.putExtra("inst", selectedInst);
                    context.startActivity(intent);
                }
            }
        });
        InstButton.setOnTouchListener(new View.OnTouchListener() { // 악기 버튼 누를 때, 뗄 때 이미지 바뀌는 효과 넣기
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if(motionEvent.getAction()==MotionEvent.ACTION_DOWN){
                    switch (selectedInst){
                        case 0:
                            InstButton.setBackgroundResource(R.drawable.app_sel_piano_p);
                            break;
                        case 1:
                            InstButton.setBackgroundResource(R.drawable.app_sel_violin_p);
                            break;
                        case 2:
                            InstButton.setBackgroundResource(R.drawable.app_sel_harp_p);
                            break;
                        case 3:
                            InstButton.setBackgroundResource(R.drawable.app_sel_drum_p);
                            break;
                    }
                }  else if(motionEvent.getAction()==MotionEvent.ACTION_UP || motionEvent.getAction()==MotionEvent.ACTION_CANCEL){
                    switch (selectedInst){                      // ACTION_UP은 손가락 뗐을 때, ACTION_CANCEL은 스크롤 했을 때
                        case 0:
                            InstButton.setBackgroundResource(R.drawable.app_sel_piano);
                            break;
                        case 1:
                            InstButton.setBackgroundResource(R.drawable.app_sel_violin);
                            break;
                        case 2:
                            InstButton.setBackgroundResource(R.drawable.app_sel_harp);
                            break;
                        case 3:
                            InstButton.setBackgroundResource(R.drawable.app_sel_drum);
                            break;
                    }
                }

                return false;
            }
        });
    }

}
