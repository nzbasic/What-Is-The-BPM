package org.WhatIsTheBpm.Bpm;

public class Bpm {
    private int bpm;
    private int time;
    public Bpm(int bpm, int time) {
        this.bpm = bpm;
        this.time = time;
    }

    public int getTime() {
        return time;
    }

    public int getBpm() {
        return bpm;
    }
}
