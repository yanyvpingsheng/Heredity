package com.yanyv.workstation.vo;

import java.util.ArrayList;
import java.util.List;

// 工件类
public class Workpiece {
    // 工件名称
    private String name;
    // 工序列表
    private List<Process> processList = new ArrayList<>();
    // 上道工序完成时间
    private int lastTime;

    public Workpiece(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Process> getProcessList() {
        return processList;
    }

    public void setProcessList(List<Process> processList) {
        this.processList = processList;
    }

    public int getLastTime() {
        return lastTime;
    }

    public void setLastTime(int lastTime) {
        this.lastTime = lastTime;
    }
}
