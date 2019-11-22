package com.tm30.structmenu.facade;

import com.tm30.structmenu.exceptions.InputNotFoundException;
import com.tm30.structmenu.input.Input;
import com.tm30.structmenu.strategy.Strategy;

import java.util.*;

public class InputFacade {

    Strategy strategy;
    HashMap<String, String> inputMap;

    public InputFacade(Strategy strategy) {
        this.strategy = strategy;

        inputMap = new HashMap<>();

        List<Input> inputList = this.strategy.getInputList();

        for (Input input : inputList) {
            inputMap.put(input.getParamKey(), input.getParamValue());
        }

        System.out.println(inputMap.toString());

    }


    public String getParam(String paramKey) throws InputNotFoundException {

        String param;

        try {
            param = inputMap.get(paramKey);
        } catch (Exception err) {
            throw new InputNotFoundException(paramKey + " not present as an Input for Strategy", err);
        }

        return param;
    }

}
