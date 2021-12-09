package com.example.tolatrieuphu.model;

public class Question {
    private String question, caseA, caseB, caseC, caseD;
    private int level, trueCase;

    public Question(String question, String caseA, String caseB, String caseC, String caseD, int level, int trueCase) {
        this.question = question;
        this.caseA = caseA;
        this.caseB = caseB;
        this.caseC = caseC;
        this.caseD = caseD;
        this.level = level;
        this.trueCase = trueCase;
    }

    public String getQuestion() {
        return question;
    }

    public String getCaseA() {
        return caseA;
    }

    public String getCaseB() {
        return caseB;
    }

    public String getCaseC() {
        return caseC;
    }

    public String getCaseD() {
        return caseD;
    }

    public int getLevel() {
        return level;
    }

    public int getTrueCase() {
        return trueCase;
    }
}
