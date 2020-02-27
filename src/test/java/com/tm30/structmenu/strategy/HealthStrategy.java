package com.tm30.structmenu.strategy;

import com.tm30.structmenu.input.NameInput;
import com.tm30.structmenu.repository.InputRepository;

// TODO: Implement directy from Test Cases
public class HealthStrategy extends Strategy{

    public HealthStrategy() {
        this.setInputs(new NameInput());
        this.setUniqueId(2);
        this.setSlug("Health");
    }

    @Override
    public String implement() {
        return "This is Health Result for " + InputRepository.getUserInput(request.getRecipient(), "name");
    }
}
