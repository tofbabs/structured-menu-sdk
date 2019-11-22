package com.tm30.structmenu.strategy;

import com.tm30.structmenu.context.State;
import com.tm30.structmenu.facade.InputFacade;
import com.tm30.structmenu.input.Input;
import com.tm30.structmenu.interfaces.StrategyInterface;
import com.tm30.structmenu.message.Request;
import com.tm30.structmenu.message.Response;
import com.tm30.structmenu.repository.StateRepository;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

public abstract class Strategy implements StrategyInterface, Serializable {

    static int UNIQUE_ID = 0;
    protected int uid = ++UNIQUE_ID;

    transient protected Iterator<Strategy> strategies;
    protected List<Strategy> strategyList;

    transient protected Iterator<Input> inputs;
    protected List<Input> inputList;

    protected Input nextInput;
    protected Integer nextInputIndex = 0;

    protected Iterator<String> keywords;
    protected State state;
    protected String slug;

    public InputFacade inputFacade;

    protected Request request;
    protected Response response;

    public Strategy() {
    }

    /**
     * Enable Supply Client supply uid that represent the Strategy on the Structured Menu
     *
     * @param uid
     */
    @Override
    public void setUniqueId(Integer uid) {
        this.uid = uid;
    }

    @Override
    public Integer getUniqueId() {
        return this.uid;
    }

    @Override
    public void setSlug(String slug) {
        this.slug = slug;
    }


    public String getSlug() {
        return slug;
    }

    @Override
    public void setInputs(Input... inputs) {
        this.inputs = Arrays.asList(inputs).iterator();
        this.inputList = Arrays.asList(inputs);
    }

    public Iterator<Input> getInputs() {
        return inputs;
    }

    public List<Input> getInputList() {
        return inputList;
    }

    public Input getCurrentInput() {
        return nextInput;
    }

    @Override
    public void setChildStrategies(Strategy... strategies) {
        this.strategies = Arrays.asList(strategies).iterator();
        this.strategyList = Arrays.asList(strategies);
    }

    @Override
    public void setKeywords(String... keywords) {
        this.keywords = Arrays.asList(keywords).iterator();
    }

    @Override
    public Boolean hasInput() {

        if (Optional.ofNullable(this.inputs).isPresent())
            return true;

        return false;
    }

    // TODO: To Remove if redundant
    @Override
    public Boolean hasPendingInput() {
        // TODO: Implement
        return false;
    }

    @Override
    public void setRequest(Request request) {
        this.request = request;
    }

    @Override
    public void setResponse(Response response) {
        this.response = response;
    }

    public void setState(State state) {
        this.state = state;
    }

    @Override
    public String implement() {
        return "Unhandled Request";
    }



    public void obtainInput() {

        int index = 0;
        nextInput = null;

        // Update Value for current input if exist
        if (Optional.ofNullable(state).isPresent() ) {

            if (state.getAction().equals(State.Action.INPUT) && Optional.ofNullable(state.getInput()).isPresent()){
                Input currentInput = state.getInput();
                currentInput.setRequest(this.request);
                currentInput.updateValue();

                index = inputList.indexOf(state.getInput());
                index++;
            }

        }

        // Use List inputList instead of Iterator inputs
        // inputs is transient

        boolean hasNext = Optional.ofNullable(inputList).isPresent() ? inputList.size()  > index : false;
        if (hasNext) {
            nextInput = inputList.get(index);
            this.response.setMessage(nextInput.getPrompt());
        }
    }

    public String generateMenu() {

        String string = "";

        if (Optional.ofNullable(strategyList).isPresent()) {
            for (Strategy stat : strategyList) {

                String label = stat.getSlug().substring(0, 1).toUpperCase() + stat.getSlug().substring(1).toLowerCase();
                string += stat.getUniqueId() + ". " + label + "\n";
            }
        }

        return string;

    }

    public void persistState() {

        state = new State();

        state.setStrategy(this);

        // Persist State
        if (Optional.ofNullable(nextInput).isPresent()) {
            state.setAction(State.Action.INPUT);
            state.setInput(nextInput);
        } else {
            state.setAction(State.Action.STRATEGY);
        }

        state.setUserId(request.getRecipient());
        state.setSessionId(request.getSessionId());
        StateRepository.save(state);
    }

    /**
     * Do Strategy Operation Here
     * Default: Returns "Unhandled Request"
     *
     * @return
     */
    @Override
    public Response execute() {

        response = new Response();

        // Obtain Input paramValue from User Request
        this.obtainInput();

        // Persist State
        this.persistState();

        response.setRecipient(request.getRecipient());
        response.setAccessCode(request.getAccessCode());
        response.setServiceCode(request.getServiceCode());
        response.setSessionId(request.getSessionId());

        // Implement Strategy when all Inputs has been supplied
        if (!Optional.ofNullable(nextInput).isPresent()) {
            response.setMessage(this.implement());
        }

        return response;
    }


    private void readObject(ObjectInputStream inputStream)
            throws IOException, ClassNotFoundException {
        inputStream.defaultReadObject();

    }
}
