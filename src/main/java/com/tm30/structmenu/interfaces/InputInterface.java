package com.tm30.structmenu.interfaces;

import com.tm30.structmenu.message.Request;

public interface InputInterface {

    void setParamValue(String paramName);

    String getParamValue();

    String getParamKey();

    String getPrompt();

    String getType();

    void setRequest(Request request);

    void updateValue();

}
