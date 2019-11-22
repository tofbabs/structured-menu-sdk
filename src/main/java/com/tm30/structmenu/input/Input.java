package com.tm30.structmenu.input;

import com.tm30.structmenu.interfaces.InputInterface;
import com.tm30.structmenu.interfaces.StrategyInterface;
import com.tm30.structmenu.message.Request;
import com.tm30.structmenu.message.Response;
import com.tm30.structmenu.repository.InputRepository;

import java.io.Serializable;

public abstract class Input implements InputInterface, Serializable{


    protected Request request;
    protected Response response;

    protected String paramKey;
    protected String paramValue;
    protected String prompt;
    protected String type;

    public Request getRequest() {
        return request;
    }

    public Response getResponse() {
        return response;
    }

    @Override
    public void setParamValue(String paramValue) {
        this.paramValue = paramValue;
    }

    @Override
    public String getParamValue() {
        return paramValue;
    }

    public void setPrompt(String prompt) {
        this.prompt = prompt;
    }

    public String getPrompt() {
        return prompt;
    }

    public void setParamKey(String paramKey) {
        this.paramKey = paramKey;
    }

    @Override
    public String getParamKey(){
        return this.paramKey;
    }

    /**
     * Input Type required for Validation
     * Supports String
     * TODO: Extend to more inputs
     *
     * @param type
     */
    public void setType(String type){
        this.type = type;
    }

    @Override
    public String getType() {
        return type;
    }


    @Override
    public void setRequest(Request request) {
        this.request = request;
    }

    @Override
    public void updateValue() {
        // Set User Response as Input Param
        this.setParamValue(this.request.getMessage());
        InputRepository.save(this);
    }
}
