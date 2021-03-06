package com.example.crescendo_piano;

import android.content.Intent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;

import androidx.recyclerview.widget.RecyclerView;

public class CodeViewHolderPage extends RecyclerView.ViewHolder {
    private ImageButton codeButton;
    private RelativeLayout code_layout;
    private Float posY=0f;
    CodeDataPage data;

    CodeViewHolderPage(View itemView){
        super(itemView);
        codeButton=itemView.findViewById(R.id.code_button);
        code_layout=itemView.findViewById(R.id.item_layout);
    }
    public void onBind(CodeDataPage data){
        this.data=data;
        codeButton.setBackgroundResource(data.getCodeImageBackground());
        int selectedCode=data.getCodeIndex();   // 선택한 버튼
        codeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {    // 코드 레시피 버튼 클릭 시
                if(posY<600) {
                    Intent intent = new Intent(data.getContext(), CodeRecipeActivity.class);
                    intent.putExtra("selectedCode", selectedCode);
                    data.getContext().startActivity(intent);
                    ((CodeSelectActivity)data.getContext()).overridePendingTransition(R.anim.zoom_enter, R.anim.zoom_exit);
                }
            }
        });
        codeButton.setOnTouchListener(new View.OnTouchListener() { // 코드 레시피 버튼 누를 때, 뗄 때 이미지 바뀌는 효과 넣기
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                if(motionEvent.getAction()==MotionEvent.ACTION_DOWN){
                    posY=motionEvent.getY();
                    if(posY<600) {
                        switch (selectedCode) {
                            case 0:
                                codeButton.setBackgroundResource(R.drawable.code_cresc_one_p);
                                break;
                            case 1:
                                codeButton.setBackgroundResource(R.drawable.code_cresc_two_p);
                                break;
                            case 2:
                                codeButton.setBackgroundResource(R.drawable.code_cresc_three_p);
                                break;
                            case 3:
                                codeButton.setBackgroundResource(R.drawable.code_london_p);
                                break;
                            case 4:
                                codeButton.setBackgroundResource(R.drawable.code_paris_p);
                                break;
                            case 5:
                                codeButton.setBackgroundResource(R.drawable.code_tokyo_p);
                                break;
                            case 6:
                                codeButton.setBackgroundResource(R.drawable.code_la_p);
                                break;
                        }
                    }
                }  else if(motionEvent.getAction()==MotionEvent.ACTION_UP || motionEvent.getAction()==MotionEvent.ACTION_CANCEL){
                    posY=motionEvent.getY();
                    switch (selectedCode){                      // ACTION_UP은 손가락 뗐을 때, ACTION_CANCEL은 스크롤 했을 때
                        case 0:
                            codeButton.setBackgroundResource(R.drawable.code_cresc_one);
                            break;
                        case 1:
                            codeButton.setBackgroundResource(R.drawable.code_cresc_two);
                            break;
                        case 2:
                            codeButton.setBackgroundResource(R.drawable.code_cresc_three);
                            break;
                        case 3:
                            codeButton.setBackgroundResource(R.drawable.code_london);
                            break;
                        case 4:
                            codeButton.setBackgroundResource(R.drawable.code_paris);
                            break;
                        case 5:
                            codeButton.setBackgroundResource(R.drawable.code_tokyo);
                            break;
                        case 6:
                            codeButton.setBackgroundResource(R.drawable.code_la);
                            break;
                    }
                }

                return false;
            }
        });
    }

}
