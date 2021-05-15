package com.example.crescendo_piano;

import android.content.Context;

public class CodeDataPage {
    int CodeImageBackground;
    int codeIndex;
    Context context;
    public CodeDataPage(int CodeImageBackground, int codeIndex, Context context){
        this.CodeImageBackground=CodeImageBackground;
        this.codeIndex=codeIndex;
        this.context=context;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public int getCodeImageBackground() {
        return CodeImageBackground;
    }

    public void setCodeImageBackground(int codeImageBackground) {
        CodeImageBackground = codeImageBackground;
    }
    public int getCodeIndex() {
        return codeIndex;
    }

    public void setCodeIndex(int codeIndex) {
        this.codeIndex = codeIndex;
    }
}
