package org.WhatIsTheBpm.Bpm;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class TimingPointReader {
    private String path;
    private String osuPath = "D:\\osu\\Songs\\";
    private List<Bpm> bpmList;
    public void setPath(String path) {
        this.path = osuPath + path;
    }
    public List<Bpm> readTimingPoints() throws FileNotFoundException {
        bpmList = new ArrayList<Bpm>();
        File file = new File(path);
        Scanner sc = new Scanner(file);
        boolean flag = false;
        while (sc.hasNextLine()) {
            String line = sc.nextLine();
            if (line.equals("[TimingPoints]")) {
                flag = true;
                continue;
            }
            if (flag && line.equals("")) {
                break;
            }
            if (flag) {
                String[] data = line.split(",");
                int bpm = (int)(60000 / Double.parseDouble(data[1]));
                if (bpm > 0) {
                    int time = Integer.parseInt(data[0]);
                    Bpm bpmObj = new Bpm(bpm, time);
                    bpmList.add(bpmObj);
                }
            }
        }

        return bpmList;
    }
}
