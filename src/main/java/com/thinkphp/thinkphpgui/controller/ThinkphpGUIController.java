package com.thinkphp.thinkphpgui.controller;

import com.thinkphp.thinkphpgui.common.BasePayload;
import com.thinkphp.thinkphpgui.entity.Result;
import com.thinkphp.thinkphpgui.service.BatchCheckTask;
import com.thinkphp.thinkphpgui.service.CheckTask;
import com.thinkphp.thinkphpgui.util.*;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ThinkphpGUIController {
    //主UI元素定义
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
    public static Map<String, String> currentProxy = new HashMap<>();

    public void initialize() {
        comboBox.setValue("ALL");
        comboBox.getItems().add("ALL");
        comboBox.getItems().addAll(ExpList.get_exp());
    }

    @FXML
    private void proxy_set() {
        final Alert inputDialog = new Alert(Alert.AlertType.NONE);
        inputDialog.setResizable(true);
        final Window window = inputDialog.getDialogPane().getScene().getWindow();
        window.setOnCloseRequest(e -> window.hide());

        ToggleGroup statusGroup = new ToggleGroup();
        RadioButton enableRadio = new RadioButton("启用");
        RadioButton disableRadio = new RadioButton("禁用");
        enableRadio.setToggleGroup(statusGroup);
        disableRadio.setToggleGroup(statusGroup);
        HBox statusHbox = new HBox(10.0D, enableRadio, disableRadio);
        GridPane proxyGridPane = new GridPane();
        proxyGridPane.setVgap(15.0D);
        proxyGridPane.setPadding(new Insets(20.0D, 20.0D, 0.0D, 10.0D));
        Label typeLabel = new Label("类型：");
        Label type = new Label("HTTP");
        Label IPLabel = new Label("IP地址：");
        TextField IPText = new TextField();
        Label PortLabel = new Label("端口：");
        TextField PortText = new TextField();
        Label userNameLabel = new Label("用户名：");
        TextField userNameText = new TextField();
        Label passwordLabel = new Label("密码：");
        TextField passwordText = new TextField();
        Button cancelBtn = new Button("取消");
        Button saveBtn = new Button("保存");
        saveBtn.setDefaultButton(true);

        // Set values if currentProxy is not null
        IPText.setText( currentProxy.getOrDefault("ipAddress", ""));
        PortText.setText(currentProxy.getOrDefault("port", ""));
        userNameText.setText(currentProxy.getOrDefault("username", ""));
        passwordText.setText(currentProxy.getOrDefault("password", ""));
        enableRadio.setSelected(currentProxy.get("proxy") != null && currentProxy.get("proxy").equals("Y"));

        saveBtn.setOnAction(e -> {
            if (disableRadio.isSelected()) {
                currentProxy.put("proxy", "N");
                Tools.removeGlobalProxy();
            } else {
                String ipAddress = IPText.getText().trim();
                String port = PortText.getText().trim();
                String username = userNameText.getText().trim();
                String password = passwordText.getText().trim();
                if (!username.isEmpty()) {
                    Tools.setGlobalProxy(ipAddress, port, username, password);
                } else {
                    Tools.setGlobalProxy(ipAddress, port);
                }
                currentProxy.put("ipAddress", ipAddress);
                currentProxy.put("port", port);
                currentProxy.put("username", username);
                currentProxy.put("password", password);
                currentProxy.put("proxy", "Y");
            }
            inputDialog.getDialogPane().getScene().getWindow().hide();
        });

        cancelBtn.setOnAction(e -> inputDialog.getDialogPane().getScene().getWindow().hide());

        proxyGridPane.add(statusHbox, 1, 0);
        proxyGridPane.add(typeLabel, 0, 1);
        proxyGridPane.add(type, 1, 1);
        proxyGridPane.add(IPLabel, 0, 2);
        proxyGridPane.add(IPText, 1, 2);
        proxyGridPane.add(PortLabel, 0, 3);
        proxyGridPane.add(PortText, 1, 3);
        proxyGridPane.add(userNameLabel, 0, 4);
        proxyGridPane.add(userNameText, 1, 4);
        proxyGridPane.add(passwordLabel, 0, 5);
        proxyGridPane.add(passwordText, 1, 5);
        HBox buttonBox = new HBox(20.0D, cancelBtn, saveBtn);
        buttonBox.setAlignment(Pos.CENTER);
        GridPane.setColumnSpan(buttonBox, 2);
        proxyGridPane.add(buttonBox, 0, 6);
        inputDialog.getDialogPane().setContent(proxyGridPane);
        inputDialog.showAndWait();
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
            alert.setTitle("提示:");
            alert.setHeaderText("信息");
            alert.setContentText("请选择漏洞对应版本!");
            alert.showAndWait();
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