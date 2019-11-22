package com.tm30.structmenu.context;

import com.tm30.structmenu.input.Input;
import com.tm30.structmenu.interfaces.StrategyInterface;
import com.tm30.structmenu.message.Request;
import com.tm30.structmenu.message.Response;
import com.tm30.structmenu.strategy.Strategy;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class StrategyTest {

    private Strategy strategy;
    private Input input;

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
    }

    @After
    public void tearDown() throws Exception {
        this.strategy = null;
    }

    /**
     * Test to see that the message "Got it!" is sent in the response.
     */
    @Test
    public void testUnhandledRequest() {

        this.strategy.setRequest(new Request());
        this.strategy.setResponse(new Response());

        assertEquals("Unhandled Request", this.strategy.execute().getMessage());
    }



}
