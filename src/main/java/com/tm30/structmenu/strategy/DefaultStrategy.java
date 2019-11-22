package com.tm30.structmenu.strategy;

public class DefaultStrategy extends Strategy{

    public DefaultStrategy(){
        this.setSlug("default");
        this.setUniqueId(999999999);
    }

    @Override
    public String implement() {
        return "Not Strategy to Serve Context";
    }
}
