package com.tm30.structmenu.interfaces;

import com.tm30.structmenu.context.State;
import com.tm30.structmenu.input.Input;
import com.tm30.structmenu.message.Request;
import com.tm30.structmenu.message.Response;

import java.util.Iterator;

public interface StrategyInterface {

    void setUniqueId(Integer uid);

    Integer getUniqueId();

    void setSlug(String slug);

    String getSlug();

    void setChildStrategies(StrategyInterface... strategies);

    void setInputs(Input... inputs);

    Iterator<Input> getInputs();

    Boolean hasInput();

    Boolean hasPendingInput();

    void setKeywords(String ...keywords);

    void setRequest(Request request);

    void setResponse(Response response);

    void setState(State state);

    String implement();

    void obtainInput();

    Response execute();

}
