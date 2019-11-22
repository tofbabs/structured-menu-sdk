package com.tm30.structmenu.context;


import com.tm30.structmenu.input.Input;
import com.tm30.structmenu.strategy.Strategy;

import java.io.Serializable;

public class State implements Serializable{

    public enum Action {
        STRATEGY,
        INPUT
    }

    Action action;
    Input input;
    Strategy strategy;
    String userId;
    String sessionId;


    public Strategy getStrategy() {
        return strategy;
    }

    public void setStrategy(Strategy strategy) {
        this.strategy = strategy;
    }

    public Action getAction() {
        return action;
    }

    public void setAction(Action action) {
        this.action = action;
    }

    public Input getInput() {
        return input;
    }

    public void setInput(Input input) {
        this.input = input;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }
}


