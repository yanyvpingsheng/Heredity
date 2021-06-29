package com.yanyv.workstation.heredity;

import com.yanyv.workstation.MainController;
import com.yanyv.workstation.util.Gantt;
import com.yanyv.workstation.util.WorkStation;
import com.yanyv.workstation.vo.Machine;
import com.yanyv.workstation.vo.Workpiece;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

// 遗传算法
public class Heredity {

    // 初始dna列表
    private static List<DNA> dnaList = new ArrayList<>();
    // dna种群列表
    private static List<DNA> dnaGroup = new ArrayList<>();
    // 用于显示
    private static ObservableList<DNA> group = FXCollections.observableArrayList();

    // 传入主页面 车间模型
    public static DNA start(MainController mainController, WorkStation workStation) {

        // 随机生成8个初始DNA
        dnaList.clear();
        for (int i = 0; i < 8; i++) {
            dnaList.add(DNA.create(workStation));
            System.out.println(dnaList.get(i));
        }

        // 记录上次迭代结果中最优品种，以同一dna连续多次最优为循环结束条件
        List<DNA> lastBestDNA = new ArrayList<>();
        boolean allSame = false;
        int times = 0;
        while (!allSame) {
            times++;
            dnaGroup.clear();
            group.clear();
            mainController.getDnaListView().setItems(group);
            // 两两杂交，获得大小为64的种群
            for (int i = 0; i < dnaList.size(); i++) {
                for (int j = 0; j < dnaList.size(); j++) {
                    // 杂交
                    DNA newDna = dnaList.get(i).gox(dnaList.get(j));
                    // 变异
                    newDna.variation();
                    // 添加到种群
                    dnaGroup.add(newDna);
                    group.add(newDna);
                }
            }
            // 将父本加入种群
            for (int i = 0; i < dnaList.size(); i++) {
                dnaGroup.add(dnaList.get(i));
            }
            // 解码 获取所需时长
            for (DNA dna : dnaGroup) {
                // 翻译dna，将翻译后数据写入工序
                RRNA.translate(workStation, dna);
            }
            // 排序
            Collections.sort(dnaGroup);
            Collections.sort(group);
            // 选择最优的8个dna作为新一轮父本
            for (int i = 0; i < dnaList.size(); i++) {
                dnaList.set(i, dnaGroup.get(i));
            }

            // 当连续n次最优解相同时停止
            int stopValue = 20;

            if (lastBestDNA.size() >= stopValue) {
                // 如果超过n个，移除先进入的
                lastBestDNA.remove(0);
            }
            // 将本次最优dna加入列表
            lastBestDNA.add(dnaGroup.get(0));

            allSame = true;
            for (int i = 0; i < lastBestDNA.size() - 1; i++) {
                if (!lastBestDNA.get(i).equals(lastBestDNA.get(i + 1))) {
                    allSame = false;
                }
                break;
            }
            // 最少迭代n次
            if (lastBestDNA.size() < stopValue) {
                allSame = false;
            }
        }
        System.out.println("迭代了" + times + "次");


        return dnaGroup.get(0);
    }

}
