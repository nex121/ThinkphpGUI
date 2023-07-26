package com.thinkphp.thinkphpgui.util;

import java.util.ArrayList;
import java.util.List;

public class ExpList {
    public static List<String> get_exp() {
        List<String> list = new ArrayList<>();
        list.add("ThinkPHP 2 RCE");
        list.add("ThinkPHP 5.0 RCE");
        list.add("ThinkPHP 5.0.10 RCE");
        list.add("ThinkPHP 5.0.22/5.1.29 RCE");
        list.add("ThinkPHP 5.0.23 RCE");
        list.add("ThinkPHP 5.0.24-5.1.30 RCE");
        list.add("ThinkPHP 5 文件包含漏洞");
        list.add("ThinkPHP 5 show-id RCE");
        list.add("ThinkPHP 5 method filter RCE");
        list.add("ThinkPHP 5 session 文件包含漏洞");
        list.add("ThinkPHP 5 SQL注入漏洞 && 敏感信息泄露");
        list.add("ThinkPHP 5.x 数据库信息泄露");
        list.add("ThinkPHP 5.x 日志泄露");
        list.add("ThinkPHP 3.x RCE");
        list.add("ThinkPHP 3.x 日志泄露");
        list.add("ThinkPHP 3.x Log RCE");
        list.add("ThinkPHP 6.x 日志泄露");
        list.add("ThinkPHP 6 文件包含漏洞");
        list.add("ThinkPHP 6 session文件写入");
        list.add("ThinkPHP catch 命令执行漏洞");
        list.add("ThinkPHP check-code sql注入漏洞");
        list.add("ThinkPHP multi sql注入 && 信息泄露漏洞");
        list.add("ThinkPHP orderid sql注入");
        list.add("ThinkPHP update sql注入");
        list.add("ThinkPHP recent_xff sql注入");
        return list;
    }
}
