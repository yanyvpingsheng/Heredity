module org.yanyv.workstation {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.json;

    opens com.yanyv.workstation to javafx.fxml;
    opens com.yanyv.workstation.dm to javafx.base;

    exports com.yanyv.workstation;
}