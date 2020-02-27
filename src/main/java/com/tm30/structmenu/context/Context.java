package com.tm30.structmenu.context;

import com.tm30.structmenu.exceptions.SetMenuStrategyException;
import com.tm30.structmenu.interfaces.ContextInterface;
import com.tm30.structmenu.message.Request;
import com.tm30.structmenu.message.Response;
import com.tm30.structmenu.repository.StateRepository;
import com.tm30.structmenu.repository.StrategyRepository;
import com.tm30.structmenu.strategy.Strategy;
import org.reflections.Reflections;

import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

public abstract class Context implements ContextInterface, Serializable {

    protected Strategy strategy;
    protected Strategy menuStrategy;

    protected Request request;
    protected Response response;
    protected State state;

    public Context() {
        request = new Request();
        response = new Response();
    }

    /**
     * Default or Base Strategy for Context
     *
     * @param strategy
     */
    @Override
    public void setMenuStrategy(Strategy strategy) {
        this.menuStrategy = strategy;
    }

    public Strategy getMenuStrategy() {
        return menuStrategy;
    }

    public void setStrategy(Strategy strategy) {
        this.strategy = strategy;
    }

    /**
     * Bootstrap with Default strategy Class Package Path
     */
    public void bootstrap() throws SetMenuStrategyException {
        bootstrap("com.tm30.structmenu.strategy");
    }


    /**
     * Bootstrap Context with custom strategy Class Package Path
     *
     * @param strategyClassPath
     */
    public void bootstrap(String strategyClassPath) throws SetMenuStrategyException {

        if (!Optional.ofNullable(this.getMenuStrategy()).isPresent()) {
            throw new SetMenuStrategyException("Menu Strategy Not Set");
        }

        Reflections reflections = new Reflections(strategyClassPath);

        Set<Class<? extends Strategy>> classes = reflections.getSubTypesOf(Strategy.class);

        for (Class<? extends Strategy> aClass : classes) {
            try {
                Constructor<?> constructor = Class.forName(aClass.getName()).getConstructor();
                Strategy strategy = (Strategy) constructor.newInstance();

                // TODO: Throw Exception based on

                StrategyRepository.save(strategy);
                StrategyRepository.saveById(strategy);

            } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }


    @Override
    public void executeStrategy() {

        state = StateRepository.getMostRecentState(request.getRecipient());
        boolean isStrategySet = false;

        // Check if User has a State
        if (Optional.ofNullable(state).isPresent()) {

            // There is a Previous State
            // Set session ID for State
            this.request.setSessionId(this.state.getSessionId());

            // Check State
            if (state.getAction().equals(State.Action.INPUT)) {
                // Strategy still has pending Inputs
                this.strategy = this.state.getStrategy();
                isStrategySet = true;
            }
        }

        // New Request
        if (!isStrategySet) {
            // create new Session ID
            this.request.setSessionId(generateSessionId());

            // Match User Request to most appropriate Strategy
            this.strategy = this.matchRequestToStrategy();
        }

        this.strategy.setRequest(request);
        // Set Response
        this.response = this.strategy.execute();
    }

    public Strategy matchRequestToStrategy() {

        // TODO: Implement Loose Word Handler Default Strategy
        // Menu is Default
        Strategy matchedStrategy = this.getMenuStrategy();

        String slugCheck = this.request.getMessage().replace(" ", "_").toLowerCase();

        // TODO: Optimize
        if (StrategyRepository.exists(slugCheck)) {
            Strategy _strategy = StrategyRepository.getStrategy(slugCheck);

            if (!Optional.ofNullable(_strategy).isPresent()) {
                _strategy = isNumeric(this.request.getMessage()) ? StrategyRepository.getStrategyById(this.request.getMessage()) : null;
            }

            if (Optional.ofNullable(_strategy).isPresent()) {
                matchedStrategy = _strategy;
            }
        }

        return matchedStrategy;
    }

    // TODO: Create a String/Character Utility
    public static boolean isNumeric(String strNum) {
        try {
            double d = Double.parseDouble(strNum);
        } catch (NumberFormatException | NullPointerException nfe) {
            return false;
        }
        return true;
    }


    public String generateSessionId() {
        return String.valueOf(Math.random());
    }

}
