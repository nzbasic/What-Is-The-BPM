package org.WhatIsTheBpm;

import javafx.application.Platform;
import org.WhatIsTheBpm.Bpm.Bpm;
import org.WhatIsTheBpm.Bpm.TimingPointReader;
import org.WhatIsTheBpm.Controllers.SceneController;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

public class OsuEventHandler {
    private TimingPointReader reader = new TimingPointReader();
    private List<OsuEvent> events = new ArrayList<OsuEvent>();
    private SceneController controller;
    private List<Bpm> bpmList;

    public OsuEventHandler(SceneController controller) {
        this.controller = controller;
        System.out.println("Bpm handler created");
    }

    public void parse(OsuEvent event) {
        events.add(event);

        if (events.size() == 1) {
            reader.setPath(event.getRelativeOsuFilePath());
            try {
                bpmList = reader.readTimingPoints();
                System.out.println(bpmList);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        } else {
            int length = events.size();
            OsuEvent previous = events.get(length-2);
            if (!event.getRelativeOsuFilePath().equals(previous.getRelativeOsuFilePath())) {
                reader.setPath(event.getRelativeOsuFilePath());
                try {
                    bpmList = reader.readTimingPoints();
                    System.out.println(bpmList);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
        changeBpm(event.getMapTime());
    }

    public void changeBpm(int time) {
        if (bpmList != null && bpmList.size() > 0) {
            if (bpmList.size() == 1) {
                Platform.runLater(() -> controller.mapBpm.setText(Integer.toString(bpmList.get(0).getBpm())));
            } else {
                int length = bpmList.size();
                for ( int i = 0 ; i < length-1 ; i++ ) {
                    Bpm bpm1 = bpmList.get(i);
                    Bpm bpm2 = bpmList.get(i+1);
                    if (time > bpm1.getTime() && time < bpm2.getTime()) {
                        Platform.runLater(() -> controller.mapBpm.setText(Integer.toString(bpm1.getBpm())));
                        return;
                    }
                }
                Platform.runLater(() -> controller.mapBpm.setText(Integer.toString(bpmList.get(length-1).getBpm())));
            }
        }
    }


    public void generateTimingPoints() {

        //changer.destroy();
        reader.setPath(events.get(events.size()-1).getRelativeOsuFilePath());
        int startingPoint = (events.get(events.size()-1).getMapTime());
        List<Bpm> bpmList = null;
        try {
            bpmList = reader.readTimingPoints();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }



        //changer.setFields(bpmList, startingPoint);
        //changer.generate();

        //System.out.println("COCK COCK COCK COCK COCK COCK COCK COCK COCK COCK COCK COCK COCK COCK COCK COCK COCK COCK COCK COCK COCK COCK COCK COCK COCK COCK COCK COCK COCK COCK COCK COCK COCK COCK COCK COCK COCK COCK COCK COCK COCK COCK COCK COCK COCK COCK COCK COCK COCK COCK ");
    }
    public void deleteTimingPoints() {
        //changer.destroy();
        //System.out.println("YEP YEP YEP YEP YEP YEP YEP YEP YEP YEP YEP YEP YEP YEP YEP YEP YEP YEP YEP YEP YEP YEP YEP YEP YEP YEP YEP YEP YEP YEP YEP YEP YEP YEP YEP ");
    }
}
