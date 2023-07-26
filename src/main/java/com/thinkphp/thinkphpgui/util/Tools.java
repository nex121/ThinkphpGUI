package com.thinkphp.thinkphpgui.util;

import com.thinkphp.thinkphpgui.common.BasePayload;
import com.thinkphp.thinkphpgui.common.ProxyAuthenticator;
import com.thinkphp.thinkphpgui.exploit.*;

import java.io.BufferedReader;
import java.io.FileReader;
import java.net.*;
import java.util.*;

public class Tools {
    private static final Map<String, BasePayload> payloadMap = new HashMap<>();

    static {
        payloadMap.put("ThinkPHP 2 RCE", new tp2_rce());
        payloadMap.put("ThinkPHP 5.0 RCE", new tp50());
        payloadMap.put("ThinkPHP 5.0.10 RCE", new tp5010());
        payloadMap.put("ThinkPHP 5.0.22/5.1.29 RCE", new tp5022_5129());
        payloadMap.put("ThinkPHP 5.0.23 RCE", new tp5023());
        payloadMap.put("ThinkPHP 5.0.24-5.1.30 RCE", new tp5024_5130());
        payloadMap.put("ThinkPHP 5 文件包含漏洞", new tp5_file_inclusion());
        payloadMap.put("ThinkPHP 5 show-id RCE", new tp5_showid_rce());
        payloadMap.put("ThinkPHP 5 method filter RCE", new tp5_method_filter_rce());
        payloadMap.put("ThinkPHP 5 session 文件包含漏洞", new tp5_session_include());
        payloadMap.put("ThinkPHP 5 SQL注入漏洞 && 敏感信息泄露", new tp5_sql());
        payloadMap.put("ThinkPHP 5.x 数据库信息泄露", new tp5_db());
        payloadMap.put("ThinkPHP 5.x 日志泄露", new tp5_log());
        payloadMap.put("ThinkPHP 3.x RCE", new tp3());
        payloadMap.put("ThinkPHP 3.x 日志泄露", new tp3_log());
        payloadMap.put("ThinkPHP 3.x Log RCE", new tp3_log_rce());
        payloadMap.put("ThinkPHP 6.x 日志泄露", new tp6_log());
        payloadMap.put("ThinkPHP 6 文件包含漏洞", new tp6_lang());
        payloadMap.put("ThinkPHP 6 session文件写入", new tp6_session_file_write());
        payloadMap.put("ThinkPHP catch 命令执行漏洞", new tp_catch());
        payloadMap.put("ThinkPHP check-code sql注入漏洞", new tp_checkcode_time_sqli());
        payloadMap.put("ThinkPHP multi sql注入 && 信息泄露漏洞", new tp_multi_sql_leak());
        payloadMap.put("ThinkPHP orderid sql注入", new tp_pay_orderid_sqli());
        payloadMap.put("ThinkPHP update sql注入", new tp_update_sql());
        payloadMap.put("ThinkPHP recent_xff sql注入", new tp_view_recent_xff_sqli());
    }

    public static BasePayload getPayload(String select) {
        return payloadMap.get(select);
    }

    public static boolean checkTheURL(String weburl) {
        return weburl.startsWith("http");
    }

    public static String addTheURL(String weburl) {
        if (!weburl.startsWith("http")) {
            weburl = "http" + "://" + weburl;
        }
        return weburl;
    }

    public static List<String> read_file(String file) {
        List<String> list = new ArrayList<>();
        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String url;
            while ((url = br.readLine()) != null) {
                url = addTheURL(url);
                list.add(url);
            }
            br.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public static String getRandomString(int length) {
        String str = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        Random random = new Random();
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < length; ++i) {
            int number = random.nextInt(62);
            sb.append(str.charAt(number));
        }

        return sb.toString();
    }

    public static void setGlobalProxy(String proxyHost, String proxyPort) {
        // 设置 HTTP 代理
        System.setProperty("proxySet", "true");
        System.setProperty("http.proxyHost", proxyHost);
        System.setProperty("http.proxyPort", proxyPort);
    }

    public static void setGlobalProxy(String proxyHost, String proxyPort, String proxyUser, String proxyPass) {
        // 设置 HTTP 代理
        System.setProperty("proxySet", "true");
        System.setProperty("http.proxyHost", proxyHost);
        System.setProperty("http.proxyPort", proxyPort);
        System.setProperty("http.proxyUserName", proxyUser);
        System.setProperty("http.proxyPassword", proxyPass);
        Authenticator.setDefault(new ProxyAuthenticator(proxyUser, proxyPass));
    }

    public static void removeGlobalProxy() {
        System.setProperty("proxySet", "false");
        System.clearProperty("http.proxyHost");
        System.clearProperty("http.proxyPort");

        Authenticator.setDefault(null);
        ProxySelector.setDefault(ProxySelector.getDefault());
    }

}
