package com.yanyv.workstation.util;

import com.yanyv.workstation.MainController;
import com.yanyv.workstation.heredity.DNA;
import com.yanyv.workstation.heredity.RRNA;
import com.yanyv.workstation.vo.Machine;
import com.yanyv.workstation.vo.Workpiece;
import javafx.scene.control.Label;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

// 甘特图类
public class Gantt {
    // 左右两边空余
    private static int sideWidth = 50;
    // 顶部空余
    private static int sideTop = 10;
    // 底部空余
    private static int sideBottom = 20;

    // 静态方法绘制甘特图 传入容器 数据
    public static void drawGantt(Pane pane, List<GanttValue> vList) {
        // 清空容器子控件
        pane.getChildren().clear();
        // 表宽
        double width = pane.getWidth() - 2 * sideWidth;
        // 表高
        double height = pane.getHeight() - sideTop - sideBottom;
        // 行数
        double line = vList.size();
        // 行高
        double lineHeight = height / line;
        // 行间距
        double lineDis = 5;
        // 声明两个坐标轴
        Line xLine = new Line(sideWidth, pane.getHeight() - sideBottom, pane.getWidth() - sideWidth, pane.getHeight() - sideBottom);
        Line yLine = new Line(sideWidth, pane.getHeight() - sideBottom, sideWidth, sideTop);
        // 获取x轴最大值
        int xMax = 0;
        for (GanttValue value : vList) {
            // 取出每一行的最后一项
            GanttItemValue itemValue = value.lineData.get(value.lineData.size() - 1);
            // 获取该行长度
            int itemMax = itemValue.start + itemValue.length;
            // 如果比最大值大 将其设为最大值
            if (itemMax > xMax) xMax = itemMax;
        }
        // 求横坐标间距
        double disX = width / xMax;
        // 画横坐标
        for (int i = 0; i <= xMax; i+=10) {
            // 按间隔为 10 绘制文字
            Label label = new Label("" + i);
            label.setLayoutX(sideWidth + i * disX);
            label.setLayoutY(pane.getHeight() - sideBottom);
            // 刻度线
            Line lineSmall = new Line(sideWidth + i * disX, pane.getHeight() - sideBottom, sideWidth + i * disX, pane.getHeight() - sideBottom - 5);
            pane.getChildren().add(label);
            pane.getChildren().add(lineSmall);
        }
        // 画纵坐标
        for (int i = 0; i < vList.size(); i++) {
            // 取出一行数据中的第一个，获取该行名称
            String lineName = vList.get(i).lineData.get(0).lineName;
            // 绘制该行名称
            Label label = new Label(lineName);
            label.setLayoutX(sideWidth - 25);
            label.setLayoutY(sideTop + i * lineHeight);
            pane.getChildren().add(label);
        }

        // 记录每个数据的id，并为每个id分配一个随机颜色
        // 由于字体颜色使用黑色，所以将随机颜色的RGB范围限制在100-250（较浅、非纯白）
        List<String> ids = new ArrayList<>();
        List<Color> colors = new ArrayList<>();
        // 遍历行
        for (int i = 0; i < vList.size(); i++) {
            // 遍历每条数据
            for (int j = 0; j < vList.get(i).lineData.size(); j++) {
                // 取出一个数据
                GanttItemValue value = vList.get(i).lineData.get(j);
                // 获取该数据id
                String id = value.id;
                // 查找是否已分配随机颜色，并确定标志位
                int index = ids.indexOf(id);
                // 若未分配，则分配颜色，并确定标志位
                if (index == -1) {
                    index = ids.size();
                    ids.add(id);
                    colors.add(Color.rgb((int) (Math.random() * 150 + 100), (int) (Math.random() * 150 + 100), (int) (Math.random() * 150 + 100)));
                }
                // 将该数据以标签形式添加到容器中
                Label label = new Label(value.id);
                label.setLayoutX(sideWidth + value.start * disX);
                label.setLayoutY(sideTop + i * lineHeight);
                label.setPrefWidth(value.length * disX);
                label.setPrefHeight(lineHeight - lineDis);
                label.setBackground(new Background(new BackgroundFill(colors.get(index), null, null)));
                pane.getChildren().add(label);
            }
        }


        // 添加控件
        // 添加x轴
        pane.getChildren().add(xLine);
        // 添加y轴
        pane.getChildren().add(yLine);

    }

    // 该类表示一行数据
    public static class GanttValue {

        // 每条数据列表
        List<GanttItemValue> lineData = new ArrayList<>();

        // 传入实现了GanttFormat接口的类的实体列表
        public void formatToGanttValue(List<? extends GanttFormat> list) {
            // 将列表内容格式化为GanttItemValue对象，并添加到列表
            for (GanttFormat g : list) {
                GanttItemValue value = new GanttItemValue();
                value.id = g.getGanttId();
                value.lineName = g.getLineName();
                value.start = g.getGanttStart();
                value.length = g.getGanttLength();
                value.name = g.getGanttName();
                lineData.add(value);
            }

        }
    }

    // 该类表示一行中的一段
    public static class GanttItemValue {
        // 同色块需要相同id
        private String id;
        // 行名称
        private String lineName;
        private int start;
        private int length;
        // 悬停显示的名字
        private String name;
    }

    // 放入甘特图的数据需要实现此接口
    public interface GanttFormat {
        // 同色块需要相同id
        String getGanttId();
        // 行名称
        String getLineName();

        int getGanttStart();

        int getGanttLength();
        // 显示的名字
        String getGanttName();
    }
}
