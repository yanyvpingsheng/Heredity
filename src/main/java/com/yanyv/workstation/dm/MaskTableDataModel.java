package com.yanyv.workstation.dm;

import com.yanyv.workstation.util.WorkStation;
import com.yanyv.workstation.vo.Workpiece;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class MaskTableDataModel {
    // 工序id
    private String processId;
    // 工件id
    private String workpieceId;
    // 工序名称
    private String processName;
    // 加工时间
    private Integer workTime;
    // 加工机器
    private String useMachine;

    // 传入车间模型，返回静态调度任务表列表
    public static ObservableList<MaskTableDataModel> getModel(WorkStation workStation) {
        // 声明被返回的结果列表
        ObservableList<MaskTableDataModel> list = FXCollections.observableArrayList();

        // 遍历车间模型中的工件列表
        for (Workpiece workpiece : workStation.getWorkpieceList()) {
            // 遍历工件中的工序列表
            for (int i = 0; i < workpiece.getProcessList().size(); i++) {
                // 声明一行
                MaskTableDataModel model = new MaskTableDataModel();
                // 工序id 为 工件名称+第几道工序
                model.processId = workpiece.getName() + "-" + (i+1);
                // 工件名称
                model.workpieceId = workpiece.getName();
                // 工序名称
                model.processName = workpiece.getProcessList().get(i).getName();
                // 加工时间
                model.workTime = workpiece.getProcessList().get(i).getTime();
                // 加工机器
                model.useMachine = workpiece.getProcessList().get(i).getMachine().getName();
                // 将行加入结果列表
                list.add(model);
            }
        }
        // 返回结果列表
        return list;
    }

    public String getProcessId() {
        return processId;
    }

    public void setProcessId(String processId) {
        this.processId = processId;
    }

    public String getWorkpieceId() {
        return workpieceId;
    }

    public void setWorkpieceId(String workpieceId) {
        this.workpieceId = workpieceId;
    }

    public String getProcessName() {
        return processName;
    }

    public void setProcessName(String processName) {
        this.processName = processName;
    }

    public Integer getWorkTime() {
        return workTime;
    }

    public void setWorkTime(Integer workTime) {
        this.workTime = workTime;
    }

    public String getUseMachine() {
        return useMachine;
    }

    public void setUseMachine(String useMachine) {
        this.useMachine = useMachine;
    }
}
