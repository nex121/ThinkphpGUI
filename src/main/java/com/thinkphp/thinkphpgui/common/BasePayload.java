package com.thinkphp.thinkphpgui.common;

import com.thinkphp.thinkphpgui.entity.Result;

public interface BasePayload {
    Result checkVUL(String url) throws Exception;

    Result exeVUL(String url, String cmd) throws Exception;

    Result getShell(String url) throws Exception;
}
