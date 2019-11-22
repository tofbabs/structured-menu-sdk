package com.tm30.structmenu.context;

import com.tm30.structmenu.interfaces.ContextInterface;
import com.tm30.structmenu.message.Request;
import com.tm30.structmenu.message.Response;
import com.tm30.structmenu.repository.StateRepository;
import com.tm30.structmenu.repository.StrategyRepository;
import com.tm30.structmenu.strategy.DefaultStrategy;
import com.tm30.structmenu.strategy.Strategy;
import org.reflections.Reflections;

import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

public abstract class Context implements ContextInterface, Serializable {

    Strategy strategy;
    Strategy menuStrategy;

    protected Request request;
    protected Response response;
     State state;

    public Context() {
        request = new Request();
        response = new Response();
    }

    public Object dispatch() {

        Object object = null;
        // Morph Response to data
        return object;

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

    public void setStrategy(Strategy strategy) {
        this.strategy = strategy;
    }

    /**
     * Bootstrap with Default strategy Class Package Path
     */
    public void bootstrap(){
        bootstrap("com.tm30.structmenu.strategy");
    }


    /**
     * Bootstrap Context with custom strategy Class Package Path
     * @param strategyClassPath
     */
    public void bootstrap(String strategyClassPath){

        Reflections reflections = new Reflections(strategyClassPath);

        Set<Class<? extends Strategy>> classes = reflections.getSubTypesOf(Strategy.class);

        for (Class<? extends Strategy> aClass : classes) {
            System.out.println(aClass.getName());
            try {
                Class<?> clazz = Class.forName(aClass.getName());

                Constructor<?> constructor = Class.forName(aClass.getName()).getConstructor();
                System.out.println(Arrays.toString(constructor.getParameterTypes())); // prints "[int]"
                Strategy strategy = (Strategy) constructor.newInstance();

                StrategyRepository.save(strategy);

            } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }


    @Override
    public void executeStrategy() {

        state = StateRepository.getMostRecentState(request.getRecipient());

        if (!Optional.ofNullable(state).isPresent()) {

            this.request.setSessionId(generateSessionId());

            // Set to use Menu Strategy when User inputs defined menu slug
            if (this.request.getMessage().equalsIgnoreCase(menuStrategy.getSlug()))
                this.strategy = this.menuStrategy;
            else{
                this.strategy = this.findStrategy();
            }

        } else {
            this.request.setSessionId(this.state.getSessionId());

            // Strategy still has pending Inputs
            if (this.state.getAction().equals(State.Action.INPUT)){
                this.strategy = this.state.getStrategy();
            }
            // If Strategy has been executed
            else {
                this.strategy = this.findStrategy();
            }
        }

        this.strategy.setState(state);
        this.strategy.setRequest(request);

        // Set Response
        this.response = this.strategy.execute();
    }

    public Strategy findStrategy() {
        Strategy matchedStrategy;
        matchedStrategy = StrategyRepository.exists(this.request.getMessage()) ? StrategyRepository.getStrategy(this.request.getMessage()) : new DefaultStrategy();
        return matchedStrategy;
    }


    public String generateSessionId() {
        return String.valueOf(Math.random());
    }

}
