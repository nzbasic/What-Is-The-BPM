package org.WhatIsTheBpm.Controllers;

import javafx.animation.FadeTransition;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class SceneController {

    @FXML
    public Label bpm;

    @FXML
    public AnchorPane keyZ;

    @FXML
    public AnchorPane keyX;

    @FXML
    public Label key1Label;

    @FXML
    public Label key2Label;

    @FXML
    public Label mapBpm;

    private String key1 = "Z";
    private String key2 = "X";

    private List<Long> list;
    private List<String> key;

    public SceneController() {
        list = new ArrayList<Long>();
        key = new ArrayList<String>();
    }

    enum Status {
        DOWN, UP
    }

    enum Record {
        ON, OFF
    }

    private Status key1Status;
    private Status key2Status;

    public void down(String string) {
        if (string.equals(key1)) {
            if (key1Status == Status.DOWN) {
                return;
            } else {
                downAnimation(keyZ);
                eventKey(key1);
            }
            key1Status = Status.DOWN;
        } else if (string.equals(key2)) {
            if (key2Status == Status.DOWN) {
                return;
            } else {
                downAnimation(keyX);
                eventKey(key2);
            }
            key2Status = Status.DOWN;
        }
    }

    public void downAnimation(AnchorPane key) {
        FadeTransition ft = new FadeTransition(Duration.millis(50), key);
        ft.setFromValue(0.2);
        ft.setToValue(1.0);
        ft.setCycleCount(1);
        ft.setAutoReverse(false);
        ft.play();
    }

    public void upAnimation(AnchorPane key) {
        FadeTransition ft = new FadeTransition(Duration.millis(50), key);
        ft.setFromValue(1.0);
        ft.setToValue(0.2);
        ft.setCycleCount(1);
        ft.setAutoReverse(false);
        ft.play();
    }

    public void up(String string) {
        if (string.equals(key1)) {
            key1Status=Status.UP;
            upAnimation(keyZ);
        } else if (string.equals(key2)) {
            key2Status=Status.UP;
            upAnimation(keyX);
        }
    }

    @FXML
    public void initialize() {

    }

    @FXML
    public void eventKey(String string) {
        Platform.runLater( () -> {

            double BPM = 0;

            if (list.size() < 4) {
                list.add(new Date().getTime());
                key.add(string);
                if (list.size() == 1) {
                    return;
                }
            } else {
                list.remove(0);
                key.remove(0);
                list.add(new Date().getTime());
                key.add(string);
            }

            int length = list.size();
            long msDifference = list.get(length-1) - list.get(length-2);

            if (msDifference > 1000) {
                list = new ArrayList<Long>();
                key = new ArrayList<String>();
                return;
            }

            // case singletapping
            if (key.get(length-1).equals(key.get(length-2))) {
                BPM = 60000/(2*msDifference);
                bpm.setText((int)BPM + "");
                return;
            }
            // case alternating
            if (length >= 3) {
                String key1 = key.get(length-3);
                String key3 = key.get(length-1);

                if (key1.equals(key3)) {
                    long timeA = list.get(length-3);
                    long timeB = list.get(length-2);
                    long timeC = list.get(length-1);
                    long msDiffAB = timeB - timeA;
                    long msDiffBC = timeC - timeB;

                    if (msDiffBC > 1.5*msDiffAB) {
                        return;
                    }
                    long average = (msDiffAB + msDiffBC) / 2;
                    BPM = 60000/(4*average);
                    bpm.setText((int)BPM + "");
                }
            }
        });
    }

    @FXML
    public void changeKeyX() {

    }

    @FXML
    public void changeKeyZ() {

    }
}
