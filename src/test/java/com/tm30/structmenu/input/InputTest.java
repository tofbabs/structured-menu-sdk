package com.tm30.structmenu.input;

import com.tm30.structmenu.interfaces.StrategyInterface;
import com.tm30.structmenu.message.Request;
import com.tm30.structmenu.message.Response;
import com.tm30.structmenu.repository.InputRepository;
import com.tm30.structmenu.repository.StateRepository;
import com.tm30.structmenu.strategy.Strategy;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.Serializable;

import static org.junit.Assert.assertEquals;

public class InputTest implements Serializable {

     private Strategy strategy;
     private Input input;
     private Request request;

    @Before
    public void setUp() throws Exception {

        class TestStrategy extends Strategy {

        }
        this.strategy = new TestStrategy();


        class TestInput extends Input {

            public TestInput() {
                this.paramKey = "name";
                this.prompt = "Kindly Enter your name";
            }
        }
        this.input = new TestInput();

        request = new Request();
        request.setMessage("Tofunmi");
        request.setRecipient("08034206970");
        request.setServiceCode("123456");
    }

    @After
    public void tearDown() throws Exception {
        StateRepository.delete(StateRepository.getMostRecentState(this.request.getRecipient()));
        InputRepository.delete(this.input);
    }

    /**
     * Test to see that the message "Got it!" is sent in the response.
     */
    @Test
    public void testInputRequest() {
        this.strategy.setInputs(this.input);
        this.input.setRequest(this.request);
        this.input.updateValue();

        assertEquals("Tofunmi", this.input.getParamValue());
    }

    /**
     * Test to see that the message "Got it!" is sent in the response.
     */
    @Test
    public void testInputRetrieveFromRepo() {
        this.strategy.setInputs(this.input);
        this.input.setRequest(this.request);
        this.input.updateValue();

        assertEquals("Tofunmi", InputRepository.getUserInput(this.request.getRecipient(), this.input.getParamKey()));
    }


    @Test
    public void testInputPrompt(){

        this.strategy.setInputs(this.input);
        this.strategy.setRequest(this.request);

        Response response = this.strategy.execute();
        assertEquals("Kindly Enter your name", response.getMessage());
    }
}
