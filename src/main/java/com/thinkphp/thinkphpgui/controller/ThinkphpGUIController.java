package com.thinkphp.thinkphpgui.controller;

import com.thinkphp.thinkphpgui.common.BasePayload;
import com.thinkphp.thinkphpgui.entity.Result;
import com.thinkphp.thinkphpgui.service.BatchCheckTask;
import com.thinkphp.thinkphpgui.service.CheckTask;
import com.thinkphp.thinkphpgui.util.*;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import javax.swing.*;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ThinkphpGUIController {
    @FXML
    private TextField url_txt, file_txt, cmd_txt;
    @FXML
    private TextArea infores_txt, cmdres_txt;
    @FXML
    private Button import_btn;
    @FXML
    private ComboBox<String> comboBox;
    List<String> list_url = new ArrayList<>();
    Alert alert = new Alert(Alert.AlertType.INFORMATION);

    public void initialize() {
        comboBox.setValue("ALL");
        comboBox.getItems().add("ALL");
        comboBox.getItems().addAll(ExpList.get_exp());
    }

    @FXML
    private void about() {
        alert.setTitle("提示:");
        alert.setHeaderText("by nex121");
        alert.setContentText("本人使用javafx更新了下UI,增加了几个poc,略微优化下代码!");
        alert.showAndWait();
    }

    @FXML
    private void clear() {
        infores_txt.setText("");
        cmdres_txt.setText("");
    }

    public void loginfo(String info) {
        Platform.runLater(() -> this.infores_txt.appendText(info + "\r\n"));
    }

    public void logcmd(String info) {
        Platform.runLater(() -> this.cmdres_txt.appendText(info));
    }


    @FXML
    public void getShell() throws Exception {
        String url = this.url_txt.getText();
        String version = this.comboBox.getSelectionModel().getSelectedItem();
        String res;

        if (version.startsWith("ALL")) {
            JOptionPane.showMessageDialog(null, "请选择漏洞对应版本!", "信息", JOptionPane.WARNING_MESSAGE);
        } else {
            BasePayload bp = Tools.getPayload(version);
            Result vul = bp.getShell(url);
            if (vul.isRes()) {
                res = vul.getPayload();
                loginfo("[+] " + res);
            } else {
                alert.setTitle("提示:");
                alert.setHeaderText("信息");
                alert.setContentText("Getshell失败");
                alert.showAndWait();
            }
        }
    }

    @FXML
    public void batch_import_url() {
        Stage stage = (Stage) import_btn.getScene().getWindow();
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select URL File");
        File file = fileChooser.showOpenDialog(stage);

        if (file != null) {
            this.file_txt.setText(file.getAbsolutePath());
            list_url = Tools.read_file(file.getAbsolutePath());
            alert.setTitle("提示:");
            alert.setHeaderText("导入提示");
            alert.setContentText("成功导入" + list_url.size() + "个URL！");
            alert.showAndWait();
        }
    }

    @FXML
    public void batch_check_url() {
        BatchCheckTask bct = new BatchCheckTask(list_url, comboBox, infores_txt);
        new Thread(bct).start();
    }

    @FXML
    private void check_url() {
        boolean standard = Tools.checkTheURL(this.url_txt.getText());

        if (standard) {
            CheckTask ct = new CheckTask(comboBox, url_txt, infores_txt);
            new Thread(ct).start();
        } else {
            alert.setTitle("提示:");
            alert.setHeaderText("URL检查");
            alert.setContentText("URL格式不符合要求，示例：http://127.0.0.1:7001");
            alert.showAndWait();
        }
    }

    @FXML
    public void exe_vul() throws Exception {
        String url = this.url_txt.getText();
        String version = this.comboBox.getSelectionModel().getSelectedItem();
        String cmd = this.cmd_txt.getText();
        String res;

        if (version.startsWith("ALL")) {
            alert.setTitle("提示:");
            alert.setHeaderText("信息");
            alert.setContentText("请选择漏洞对应版本!");
            alert.showAndWait();
        } else {
            BasePayload bp = Tools.getPayload(version);
            Result vul = bp.exeVUL(url, cmd);
            if (vul.isRes()) {
                res = vul.getPayload();
                logcmd("[+] " + res);
            } else {
                alert.setTitle("提示:");
                alert.setHeaderText("信息");
                alert.setContentText("命令执行失败!");
                alert.showAndWait();
            }
        }
    }
}