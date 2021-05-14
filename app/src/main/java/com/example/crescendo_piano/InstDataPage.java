package com.example.crescendo_piano;

import android.content.Context;

public class InstDataPage { // 각 페이지에 저장될 기능정보, 악기정보
    int selectedFunc, selectedInst;
    Context context;
    public InstDataPage(int selectedFunc, int selectedInst, Context context){
        this.selectedFunc=selectedFunc;
        this.selectedInst=selectedInst;
        this.context=context;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public int getSelectedInst() {
        return selectedInst;
    }

    public void setSelectedInst(int selectedInst) {
        this.selectedInst = selectedInst;
    }


    public int getSelectedFunc() {
        return selectedFunc;
    }

    public void setSelectedFunc(int selectedFunc) {
        this.selectedFunc = selectedFunc;
    }
}