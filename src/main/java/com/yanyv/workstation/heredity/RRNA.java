package com.yanyv.workstation.heredity;

import com.yanyv.workstation.util.WorkStation;
import com.yanyv.workstation.vo.Machine;
import com.yanyv.workstation.vo.Process;
import com.yanyv.workstation.vo.Workpiece;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

// RRNA类 对应生物学核糖体 功能是翻译DNA
public class RRNA {

    // 传入车间模型和dna 将计算结果放入dna和工序中
    public static void translate(WorkStation workStation, DNA dna) {
        // 标志位列表 用于记录每个工件进行到哪道工序
        List<Integer> index = new ArrayList<>();
        // 记录每台机器完成当前工作后所在时间
        List<Integer> results = new ArrayList<>();
        // 初始化结果集
        for (Machine m : workStation.getMachineList()) {
            results.add(0);
        }
        // 初始化标志集 并 初始化工件内置时间
        for (Workpiece w : workStation.getWorkpieceList()) {
            index.add(0);
            w.setLastTime(0);
        }

        // 逐个读取dna
        for (Integer i : dna.getDna()) {
            // 找到对应工件
            Workpiece workpiece = workStation.getWorkpieceList().get(i);
            // 通过标志位找到对应工序
            Process process = workpiece.getProcessList().get(index.get(i));
            // 找到需要的机器
            Machine machine = process.getMachine();
            // 获取加工时长
            int time = process.getTime();
            int indexOfMachine = workStation.getMachineList().indexOf(machine);
            // 计算该工序加工完成后机器所在时间，（考虑前置工序未完成的情况，在工件内设值）
            int machineTime = results.get(indexOfMachine);
            int workpieceTime = workpiece.getLastTime();
            // 比较前置工序完成时间及机器完成当前工作时间，取最晚值
            if (workpieceTime > machineTime) {
                machineTime = workpieceTime;
            }
            // 将结果存入
            results.set(indexOfMachine, machineTime + time);
            workpiece.setLastTime(machineTime + time);
            process.setStart(machineTime);
            // 标志位自增
            index.set(i, index.get(i) + 1);
        }
        // 按从小到大排序
        Collections.sort(results);
        // 获取最大时间
        int time = results.get(results.size() - 1);
        // 将结果给dna
        dna.setTime(time);
    }
}
