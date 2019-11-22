package com.tm30.structmenu.facade;

import com.tm30.structmenu.exceptions.InputNotFoundException;
import com.tm30.structmenu.input.Input;
import com.tm30.structmenu.interfaces.StrategyInterface;
import com.tm30.structmenu.message.Request;
import com.tm30.structmenu.strategy.Strategy;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class InputFacadeTest {

    private Strategy strategy;
    private Input input;
    private Request request;

    @Before
    public void setUp() {

        class TestStrategy extends Strategy {
        }
        this.strategy = new TestStrategy();

        class TestInput extends Input {

            public TestInput() {
                this.paramKey = "name";
                this.prompt = "Kindly Enter your name";
            }
        }

        this.strategy.setInputs(this.input);

        request = new Request();
        request.setMessage("Tofunmi");
        request.setRecipient("08034206970");
        request.setServiceCode("123456");
    }

    @After
    public void tearDown() {
        this.strategy = null;
    }

    /**
     * Test to see that the message "Got it!" is sent in the response.
     */
    @Test
    public void testInputRequest() {

        this.strategy.setRequest(request);
        this.strategy.execute();

        try {
//            assertEquals("Tofunmi", this.input.getParamValue());
            assertEquals("Tofunmi", this.strategy.inputFacade.getParam("name"));
        } catch (InputNotFoundException e) {
            e.printStackTrace();
        }
    }
}
