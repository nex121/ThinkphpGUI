package com.thinkphp.thinkphpgui.service;

import com.thinkphp.thinkphpgui.common.BasePayload;
import com.thinkphp.thinkphpgui.util.ExpList;
import com.thinkphp.thinkphpgui.entity.Result;
import com.thinkphp.thinkphpgui.util.Tools;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.util.ArrayList;

public class CheckTask extends Task<Void> {
    Alert alert = new Alert(Alert.AlertType.INFORMATION);
    private final ComboBox<String> comboBox;
    private final TextField url_txt;
    private final TextArea infores_txt;

    public CheckTask(ComboBox<String> comboBox, TextField url_txt, TextArea infores_txt) {
        this.comboBox = comboBox;
        this.url_txt = url_txt;
        this.infores_txt = infores_txt;
    }

    @Override
    protected Void call() throws Exception {
        check_vul();
        return null;
    }

    public void loginfo(String info) {
        Platform.runLater(() -> this.infores_txt.appendText(info + "\r\n"));
    }

    public void check_vul() throws Exception {
        String url = this.url_txt.getText();
        String version = this.comboBox.getSelectionModel().getSelectedItem();

        if (version.startsWith("ALL")) {
            loginfo("检测所有漏洞中......");
            loginfo("=====================================================================");
            ArrayList<String> versions = (ArrayList<String>) ExpList.get_exp();

            for (String v : versions) {
                BasePayload bp = Tools.getPayload(v);
                Result vul = bp.checkVUL(url);
                if (vul.isRes()) {
                    loginfo("[+] 存在" + vul.getVuln());
                    loginfo("Payload: " + vul.getPayload());
                } else {
                    loginfo("[-] 不存在" + vul.getVuln());
                }
            }
        } else {
            loginfo("检测漏洞 " + version + "中......");
            loginfo("=====================================================================");
            BasePayload bp = Tools.getPayload(version);
            Result vul = bp.checkVUL(url);
            if (vul.isRes()) {
                loginfo("[+] 存在" + vul.getVuln());
                loginfo("Payload: " + vul.getPayload());
            } else {
                loginfo("[-] 不存在" + vul.getVuln());
            }
        }
    }

    @Override
    protected void succeeded() {
        Platform.runLater(() -> {
            // 在任务完成时执行的代码，比如弹窗提示用户任务已经完成。
            alert.setTitle("提示:");
            alert.setHeaderText("任务提示");
            alert.setContentText("检测任务执行完成");
            alert.showAndWait();
        });
    }
}
