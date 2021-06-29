package com.yanyv.workstation.heredity;

import com.yanyv.workstation.util.WorkStation;
import com.yanyv.workstation.vo.Workpiece;
import com.yanyv.workstation.vo.Process;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

// dna类
public class DNA implements Comparable<DNA> {
    // declare
    // 存入工件编号，工序的顺序自动确定
    private List<Integer> dna = new ArrayList<>();
    // 完成所需时间
    private int time;

    // func
    // 生成初始DNA序列
    public static DNA create(WorkStation workStation) {
        DNA dna = new DNA();
        List<Workpiece> list = workStation.getWorkpieceList();
        for (int i = 0; i < workStation.getWorkpieceList().size(); i++) {
            for(Process process : list.get(i).getProcessList()) {
                dna.dna.add(i);
            }
        }
        // 随机排序
        Collections.shuffle(dna.dna);

        return dna;
    }

    // 交叉
    public DNA gox(DNA anotherDNA) {
        DNA newDna = new DNA();

        // 逻辑 :从bDNA取出一段，将aDNA中多余的去掉，将bDNA片段插入aDNA
        // 获取交叉起点和长度
        int start = (int) (Math.random() * (dna.size() - 1));
        int length = (int) (Math.random() * (dna.size() - start - 1) + 1);
        // 获取插入位置
        int index = (int) (Math.random() * (dna.size() - length));
        // 取出片段
        List<Integer> part = anotherDNA.dna.subList(start, start + length);
        // 复制到新dna
        newDna.dna = dna.stream().collect(Collectors.toList());
        newDna.insert(part, index);

        return newDna;
    }

    // 变异
    public void variation() {
        int indexA = (int) (Math.random() * dna.size());
        int indexB = (int) (Math.random() * dna.size());
        int dnaA = dna.get(indexA);
        dna.set(indexA, dna.get(indexB));
        dna.set(indexB, dnaA);
    }


    // 插入dna片段
    private void insert(List<Integer> part, int index) {
        // 移除所有多余基因
        for (Integer o : part) {
            for (int i = 0; i < dna.size(); i++) {
                if(o == dna.get(i)) {
                    dna.remove(i);
                    break;
                }
            }
        }
        // 插入
        for (int i = part.size(); i > 0; i--) {
            dna.add(index, part.get(i - 1));
        }
    }

    // override
    @Override
    public String toString() {
        String toString = "";
        for (Integer o : dna) {
            toString += o;
        }
        toString += " " + time;
        return toString;
    }

    // 按完成时间排序
    @Override
    public int compareTo(DNA dna) {
        if (time > dna.time) {
            return 1;
        } else if(time == dna.time) {
            return 0;
        }else {
            return -1;
        }
    }

    // dna序列相同就相同
    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (dna.equals(((DNA) obj).dna)) {
            return true;
        } else {
            return false;
        }
    }

    // getter and setter
    public List<Integer> getDna() {
        return dna;
    }

    public void setDna(List<Integer> dna) {
        this.dna = dna;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }
}
