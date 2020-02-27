package com.tm30.structmenu.strategy;

import com.tm30.structmenu.input.NameInput;
import com.tm30.structmenu.repository.InputRepository;

// TODO: Implement directy from Test Cases
public class TravelStrategy extends Strategy{

    public TravelStrategy() {
        this.setUniqueId(1);
        this.setSlug("Travel");
    }

    @Override
    public String implement() {
        return "This is Travel Strategy for " + this.request.getRecipient();
    }
}
