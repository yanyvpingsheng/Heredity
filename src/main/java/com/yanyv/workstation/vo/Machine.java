package com.yanyv.workstation.vo;

import java.util.ArrayList;
import java.util.List;

// 机器类
public class Machine {
    // 机器名称
    private String name;
    // 能力列表 能加工的工序
    private List<Process> ability = new ArrayList<>();

    public Machine(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Process> getAbility() {
        return ability;
    }

    public void setAbility(List<Process> ability) {
        this.ability = ability;
    }
}
