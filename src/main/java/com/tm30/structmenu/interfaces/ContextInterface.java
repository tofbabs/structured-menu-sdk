package com.tm30.structmenu.interfaces;

import com.tm30.structmenu.strategy.Strategy;

public interface ContextInterface {

    void morphToRequest(Object data);

    Object dispatch();

    void setMenuStrategy(Strategy strategy);

    void executeStrategy();

}
