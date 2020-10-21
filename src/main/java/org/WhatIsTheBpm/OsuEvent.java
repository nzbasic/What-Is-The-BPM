package org.WhatIsTheBpm;

public class OsuEvent {
    private String status;
    private int mapTime;
    private String isoTime;
    private double bpmMultiplier;
    private String relativeOsuFilePath;
    public OsuEvent(String status, int mapTime, String isoTime, double bpmMultiplier, String relativeOsuFilePath) {
        this.status = status;
        this.mapTime = mapTime;
        this.isoTime = isoTime;
        this.bpmMultiplier = bpmMultiplier;
        this.relativeOsuFilePath = relativeOsuFilePath;
    }
    public String getStatus() {
        return status;
    }

    public int getMapTime() {
        return mapTime;
    }

    public String getIsoTime() {
        return isoTime;
    }

    public double getBpmMultiplier() {
        return bpmMultiplier;
    }

    public String getRelativeOsuFilePath() {
        return relativeOsuFilePath;
    }
}
