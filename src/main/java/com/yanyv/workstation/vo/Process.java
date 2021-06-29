package com.yanyv.workstation.vo;

import com.yanyv.workstation.MainController;
import com.yanyv.workstation.util.Gantt;

// 工序类 实现甘特图格式接口，实现排序接口
public class Process implements Gantt.GanttFormat, Comparable<Process> {
    // 工序名称
    private String name;
    // 工序开始时间
    private int start;
    // 工序加工时间
    private int time;
    // 工序加工机器
    private Machine machine;
    // 所属工件
    private Workpiece workpiece;

    public Process(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public Machine getMachine() {
        return machine;
    }

    public void setMachine(Machine machine) {
        this.machine = machine;
    }

    public Workpiece getWorkpiece() {
        return workpiece;
    }

    public void setWorkpiece(Workpiece workpiece) {
        this.workpiece = workpiece;
    }

    // 获取甘特图-行-项id
    @Override
    public String getGanttId() {
        if (MainController.mode.equals("work")) {
            // 如果是作业模式
            // 返回机器名称
            return machine.getName();
        } else if (MainController.mode.equals("machine")) {
            // 如果是机器模式
            // 返回工件名称
            return workpiece.getName();
        }
        // 其他情况返回空字符串
        return "";
    }

    // 获取甘特图-行名称
    @Override
    public String getLineName() {
        if (MainController.mode.equals("work")) {
            // 如果是作业模式
            // 返回工件名称
            return workpiece.getName();
        } else if (MainController.mode.equals("machine")) {
            // 如果是机器模式
            // 返回机器名称
            return machine.getName();
        }
        // 其他情况返回空字符串
        return "";
    }

    // 获取甘特图-行-项开始位置
    @Override
    public int getGanttStart() {
        return start;
    }

    // 获取甘特图-行-项长度
    @Override
    public int getGanttLength() {
        return time;
    }

    // 获取甘特图-行-项名称 返回 工件名称-工序名称
    @Override
    public String getGanttName() {
        return workpiece.getName() + "-" + name;
    }

    // 按工序开始时间排序
    @Override
    public int compareTo(Process process) {
        if (start > process.start) {
            return 1;
        } else {
            return -1;
        }
    }
}
