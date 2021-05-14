package com.example.crescendo_piano;

public class CodeDataPage {
    int CodeImageBackground;
    int codeIndex;
    public CodeDataPage(int CodeImageBackground, int codeIndex){
        this.CodeImageBackground=CodeImageBackground;
        this.codeIndex=codeIndex;
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
