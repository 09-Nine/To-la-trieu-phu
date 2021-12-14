package com.example.tolatrieuphu.model;

public class Player {
    private String name;
    private int moneyInt;

    public Player(String name, int moneyInt) {
        this.name = name;
        this.moneyInt = moneyInt;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getMoneyInt() {
        return moneyInt;
    }

    public void setMoneyInt(int moneyInt) {
        this.moneyInt = moneyInt;
    }
}
