package com.yanyv.workstation.util;

import com.yanyv.workstation.vo.Machine;
import com.yanyv.workstation.vo.Process;
import com.yanyv.workstation.vo.Workpiece;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

// 车间模型类
public class WorkStation {
    private List<Workpiece> workpieceList = new ArrayList<>();
    private List<Machine> machineList = new ArrayList<>();

    // 通过静态工厂方法创建本类对象 传入 json格式数据
    public static WorkStation load(JSONObject json) {
        // 声明车间模型对象
        WorkStation workStation = new WorkStation();

        // 获取并遍历工件集合 记录了所有工件的名称
        JSONArray workpieceArray = json.getJSONArray("工件集合");
        for (Object name : workpieceArray) {
            // 通过名称创建所有工件并添加到车间模型的工件列表
            workStation.workpieceList.add(new Workpiece((String) name));
        }

        // 获取并遍历工件阵 记录了所有工件中的工序
        JSONArray processArray = json.getJSONArray("工件阵");
        for (int i = 0; i < processArray.length(); i++) {
            // 获取并遍历一行数据
            JSONArray workpiece = processArray.getJSONArray(i);
            for (Object name : workpiece) {
                // 按名称生成工序
                Process process = new Process((String) name);
                // 工件和工序双向绑定
                workStation.workpieceList.get(i).getProcessList().add(process);
                process.setWorkpiece(workStation.workpieceList.get(i));
            }
        }

        // 获取并遍历加工时间阵 记录了所有工序所需的加工时间
        JSONArray processTimeArray = json.getJSONArray("工件加工时间阵");
        for (int i = 0; i < processTimeArray.length(); i++) {
            JSONArray workpieceTime = processTimeArray.getJSONArray(i);
            for (int j = 0; j < workpieceTime.length(); j++) {
                // 为每道工序设置加工时间
                workStation.workpieceList.get(i).getProcessList().get(j).setTime(workpieceTime.getInt(j));
            }
        }

        // 获取并遍历机器集合 记录了所有机器的名称
        JSONArray machineArray = json.getJSONArray("机器集合");
        for (Object name : machineArray) {
            // 通过名称创建所有机器并添加到车间模型的机器列表
            workStation.machineList.add(new Machine((String) name));
        }

        // 获取并遍历机器能力集合 记录了所有机器能加工的工序
        JSONArray machineAbilityArray = json.getJSONArray("机器能力集合");
        for (int i = 0; i < machineAbilityArray.length(); i++) {
            JSONArray machineAbilityItemArray = machineAbilityArray.getJSONArray(i);
            for (Object ability : machineAbilityItemArray) {
                Workpiece workpiece = null;
                // 遍历车间模型中的工件，找到与json中名称相同的工件
                for (Workpiece w : workStation.workpieceList) {
                    if (w.getName().equals(((JSONObject) ability).getString("工件"))) {
                        workpiece = w;
                        break;
                    }
                }
                // 从工件中找到json中记录的工序，添加到机器能力列表 由于json中数据从1开始，故此处需-1
                workStation.machineList.get(i).getAbility().add(workpiece.getProcessList().get(((JSONObject) ability).getInt("工序") - 1));
            }
        }

        // 获取并遍历工件机器需求阵 记录了每道工序所需的机器
        JSONArray processMachineNeedArray = json.getJSONArray("工件机器需求阵");
        for (int i = 0; i < processMachineNeedArray.length(); i++) {
            JSONArray processMachineNeedItemArray = processMachineNeedArray.getJSONArray(i);
            for (int j = 0; j < processMachineNeedItemArray.length(); j++) {
                Machine machine = null;
                // 遍历车间模型中的机器，找到与json中名称相同的机器
                for (Machine m : workStation.machineList) {
                    if (m.getName().equals(processMachineNeedItemArray.getString(j))) {
                        machine = m;
                        break;
                    }
                }
                // 获取工件和工序
                Workpiece workpiece = workStation.workpieceList.get(i);
                Process process = workpiece.getProcessList().get(j);
                if (machine.getAbility().contains(process)) {
                    // 如果机器能加工该工序 为工序设置所需机器
                    process.setMachine(machine);
                } else {
                    // 否则报错
                    System.out.println("数据文件异常，机器" + machine.getName() + "不能加工工件" + workpiece.getName() + "的工序" + process.getName());
                }
            }
        }
        // 输出加载完成
        System.out.println("加载完成");
        // 返回车间模型
        return workStation;
    }

    public List<Workpiece> getWorkpieceList() {
        return workpieceList;
    }

    public void setWorkpieceList(List<Workpiece> workpieceList) {
        this.workpieceList = workpieceList;
    }

    public List<Machine> getMachineList() {
        return machineList;
    }

    public void setMachineList(List<Machine> machineList) {
        this.machineList = machineList;
    }
}
