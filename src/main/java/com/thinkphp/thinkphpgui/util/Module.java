package com.thinkphp.thinkphpgui.util;

import com.github.kevinsawicki.http.HttpRequest;

import java.util.ArrayList;

public class Module {
    public static String getModule(String url) {
        ArrayList<String> list = new ArrayList<String>() {{
            add("manage");
            add("admin");
            add("api");
        }};
        String mod = "index";
        for (String s : list) {
            try {
                int code = HttpRequest.get(url + "/?s=/" + s).code();
                if (code == 200) {
                    mod = s;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return mod;
    }
}
