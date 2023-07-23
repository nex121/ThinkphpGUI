package com.thinkphp.thinkphpgui.service;

import com.github.kevinsawicki.http.HttpRequest;
import com.thinkphp.thinkphpgui.common.BasePayload;
import com.thinkphp.thinkphpgui.util.ExpList;
import com.thinkphp.thinkphpgui.entity.Result;
import com.thinkphp.thinkphpgui.util.Tools;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;

import java.util.ArrayList;
import java.util.List;

public class BatchCheckTask extends Task<Void> {
    Alert alert = new Alert(Alert.AlertType.INFORMATION);
    private final ComboBox<String> comboBox;
    private final TextArea infores_txt;
    private final List<String> list_url;

    public BatchCheckTask(List<String> list_url, ComboBox<String> comboBox, TextArea infores_txt) {
        this.list_url = list_url;
        this.comboBox = comboBox;
        this.infores_txt = infores_txt;
    }

    @Override
    protected Void call() throws Exception {
        batch_check_url();
        return null;
    }

    public void loginfo(String info) {
        this.infores_txt.appendText(info + "\r\n");
    }

    public void batch_check_url() throws Exception {
        String version = this.comboBox.getSelectionModel().getSelectedItem();
        if (list_url.size() > 0) {
            if (version.startsWith("ALL")) {

                ArrayList<String> versions = (ArrayList<String>) ExpList.get_exp();
                for (String s : list_url) {
                    loginfo("检测URL " + s + "中......");
                    loginfo("=====================================================================");
                    //批量检测查该url是否有效
                    try {
                        HttpRequest.get(s).connectTimeout(5000).code();
                    } catch (Exception e) {
                        loginfo(s + " 未存活,跳过!");
                        continue;
                    }
                    for (String v : versions) {
                        BasePayload bp = Tools.getPayload(v);
                        Result vul = bp.checkVUL(s);
                        if (vul.isRes()) {
                            loginfo("[+] 存在" + vul.getVuln());
                            loginfo("Payload: " + vul.getPayload());
                        } else {
                            loginfo("[-] 不存在" + vul.getVuln());
                        }
                    }
                }
            } else {
                BasePayload bp = Tools.getPayload(version);
                for (int j = 0; j < list_url.size(); j++) {
                    if (j != 0) {
                        loginfo("");
                    }
                    loginfo("检测URL " + list_url.get(j) + "中......");
                    loginfo("=====================================================================");
                    Result isvul = bp.checkVUL(list_url.get(j));
                    if (isvul.isRes()) {
                        loginfo("[+] 存在" + isvul.getVuln());
                        loginfo("Payload: " + isvul.getPayload());
                    } else {
                        loginfo("[-] 不存在" + isvul.getVuln());
                    }
                }
            }

        } else {
            alert.setTitle("提示:");
            alert.setHeaderText("URL批量检查");
            alert.setContentText("请先导入URL！");
            alert.showAndWait();
        }
    }

    @Override
    protected void succeeded() {
        Platform.runLater(() -> {
            // 在任务完成时执行的代码，比如弹窗提示用户任务已经完成。
            alert.setTitle("提示:");
            alert.setHeaderText("任务提示");
            alert.setContentText("批量检测任务执行完成");
            alert.showAndWait();
        });
    }

}
