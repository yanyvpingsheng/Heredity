package com.yanyv.workstation;

import com.yanyv.workstation.dm.MaskTableDataModel;
import com.yanyv.workstation.heredity.DNA;
import com.yanyv.workstation.heredity.Heredity;
import com.yanyv.workstation.heredity.RRNA;
import com.yanyv.workstation.util.Gantt;
import com.yanyv.workstation.util.InputFile;
import com.yanyv.workstation.util.WorkStation;
import com.yanyv.workstation.vo.Machine;
import com.yanyv.workstation.vo.Workpiece;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainController {

    // 标签容器
    @FXML
    private TabPane mainTabPane;
    // 静态调度任务表标签
    @FXML
    private Tab staticMaskTableTab;
    // 求解标签
    @FXML
    private Tab seekSolutionTableTab;
    // 任务页面
    @FXML
    private TableView maskTable;
    // 各个列
    @FXML
    private TableColumn processIdColumn;
    @FXML
    private TableColumn workpieceIdColumn;
    @FXML
    private TableColumn processNameColumn;
    @FXML
    private TableColumn workTimeColumn;
    @FXML
    private TableColumn useMachineColumn;

    // dna列表
    @FXML
    private ListView dnaListView;
    // 作业计划甘特图
    @FXML
    private Pane workPlanGanttPane;
    // 机器计划甘特图
    @FXML
    private Pane machinePlanGanttPane;

    // 模式
    public static String mode = "";
    // 车间模型
    private WorkStation workStation = null;

    // 由加载按钮点击触发
    public void load(ActionEvent actionEvent) {
        // 打开文件弹窗
        FileChooser fileChooser = new FileChooser();
        // 设置文件选择器的标题
        fileChooser.setTitle("选择输入文件");
        // 下面这句用来记忆过去的默认路径
        fileChooser.setInitialDirectory(new File("D:\\IDEAProject\\作业车间调度\\src\\main\\resources\\com\\yanyv\\workstation\\input"));
        // 传入多个文件选择器，支持json和ywsin格式
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("JSON文件", "*.json"), new FileChooser.ExtensionFilter("烟雨作业调度输入文件", "*.ywsin"), new FileChooser.ExtensionFilter("全部文件", "*.*"));
        // 获取选择的文件
        File file = fileChooser.showOpenDialog(App.getStage());
        // 如果选择了文件
        if (file != null) {
            // 输出文件的绝对路径
            System.out.println(file.getAbsolutePath());
            // 获取车间模型
            workStation = InputFile.load(file);
            // 绑定数据
            ObservableList<MaskTableDataModel> data = MaskTableDataModel.getModel(workStation);
            processIdColumn.setCellValueFactory(
                    new PropertyValueFactory<MaskTableDataModel, String>("processId")
            );
            workpieceIdColumn.setCellValueFactory(
                    new PropertyValueFactory<MaskTableDataModel, String>("workpieceId")
            );
            processNameColumn.setCellValueFactory(
                    new PropertyValueFactory<MaskTableDataModel, String>("processName")
            );
            workTimeColumn.setCellValueFactory(
                    new PropertyValueFactory<MaskTableDataModel, Integer>("workTime")
            );
            useMachineColumn.setCellValueFactory(
                    new PropertyValueFactory<MaskTableDataModel, String>("useMachine")
            );
            // 传入数据
            maskTable.setItems(data);
            // 切换标签
            mainTabPane.getSelectionModel().select(staticMaskTableTab);
        }
    }

    // 由求解按钮点击触发
    public void seekSolution(ActionEvent actionEvent) {
        // 绑定item事件
        addOnDnaListItemClickListener();
        if (workStation != null) {
            // 切换标签
            mainTabPane.getSelectionModel().select(seekSolutionTableTab);

            // 遗传算法解决作业车间调度问题
            Heredity.start(MainController.this, workStation);
            // 画最终结果的甘特图
            drawGantt((DNA) dnaListView.getItems().get(0));



        } else {
            // 车间模型为空时，显示警告
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("警告");
            alert.setContentText("车间模型尚未加载");
            alert.showAndWait();
        }
    }

    // 绑定dna列表中子选项的点击事件
    public void addOnDnaListItemClickListener() {
        // 绑定dna列表中子选项的点击事件
        dnaListView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Object>() {

            // 由被选中项改变触发
            @Override
            public void changed(ObservableValue<? extends Object> observable, Object oldValue, Object newValue) {
                // 画甘特图
                drawGantt((DNA) newValue);
            }

        });
    }

    // 传入dna，画甘特图
    public void drawGantt(DNA dna) {
        // 翻译dna，将翻译后数据写入工序
        RRNA.translate(workStation, dna);
        // 作业计划
        // 设置模式为作业计划
        mode = "work";
        // 声明甘特图需要的数据列
        List<Gantt.GanttValue> vList = new ArrayList<>();
        // 遍历车间模型中的工件列表
        for (Workpiece wp : workStation.getWorkpieceList()) {
            // 声明甘特图行数据
            Gantt.GanttValue value = new Gantt.GanttValue();
            // 传入工序列表 格式化
            value.formatToGanttValue(wp.getProcessList());
            // 将数据行加入到数据列中
            vList.add(value);
        }
        // 画甘特图，传入容器和数据
        Gantt.drawGantt(workPlanGanttPane, vList);
        // 机器计划
        // 设置模式为机器计划
        mode = "machine";
        // 声明甘特图需要的数据列
        vList = new ArrayList<>();
        // 遍历车间模型中的机器列表
        for (Machine machine : workStation.getMachineList()) {
            // 声明甘特图行数据
            Gantt.GanttValue value = new Gantt.GanttValue();
            // 获取机器能力列表
            List list = machine.getAbility();
            // 按时间排序
            Collections.sort(list);
            // 传入工序列表 格式化
            value.formatToGanttValue(list);
            // 将数据行加入到数据列中
            vList.add(value);
        }
        // 画甘特图，传入容器和数据
        Gantt.drawGantt(machinePlanGanttPane, vList);
    }

    public ListView getDnaListView() {
        return dnaListView;
    }

    public void setDnaListView(ListView dnaListView) {
        this.dnaListView = dnaListView;
    }

    public Pane getWorkPlanGanttPane() {
        return workPlanGanttPane;
    }

    public void setWorkPlanGanttPane(Pane workPlanGanttPane) {
        this.workPlanGanttPane = workPlanGanttPane;
    }

    public Pane getMachinePlanGanttPane() {
        return machinePlanGanttPane;
    }

    public void setMachinePlanGanttPane(Pane machinePlanGanttPane) {
        this.machinePlanGanttPane = machinePlanGanttPane;
    }

    public WorkStation getWorkStation() {
        return workStation;
    }

    public void setWorkStation(WorkStation workStation) {
        this.workStation = workStation;
    }
}
