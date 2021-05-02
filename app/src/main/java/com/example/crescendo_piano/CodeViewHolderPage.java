package com.example.crescendo_piano;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;

import androidx.recyclerview.widget.RecyclerView;

public class CodeViewHolderPage extends RecyclerView.ViewHolder {
    private ImageButton codeButton;
    private RelativeLayout code_layout;
    CodeDataPage data;

    CodeViewHolderPage(View itemView){
        super(itemView);
        codeButton=itemView.findViewById(R.id.code_button);
        code_layout=itemView.findViewById(R.id.item_layout);
    }
    public void onBind(CodeDataPage data){
        this.data=data;
        codeButton.setBackgroundResource(data.getCodeImageBackground());
        codeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {    // 코드 레시피 버튼 클릭 시
                int selectedCode=data.getCodeIndex();   // 선택한 버튼
                //Intent intent=new Intent();
                //intent.putExtra("selectedCode", selectedCode);
            }
        });
    }
}
