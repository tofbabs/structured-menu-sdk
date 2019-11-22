package com.tm30.structmenu.context;

import com.tm30.structmenu.message.Request;
import com.tm30.structmenu.repository.StateRepository;
import com.tm30.structmenu.strategy.Strategy;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.Serializable;

import static org.junit.Assert.assertEquals;

public class ContextTest implements Serializable{

    private Context context;
    private Strategy menuStrategy;

    @Before
    public void setUp() throws Exception {

        class TestContext extends Context {

            @Override
            public void morphToRequest(Object data) {

                String stringData = (String) data;
                String[] dataArray = stringData.split(";");

                this.request = new Request();
                this.request.setMessage(dataArray[0]);
                this.request.setRecipient(dataArray[1]);
            }

            @Override
            public Object dispatch() {
                String data;

                data = this.response.getMessage() + ";" + this.response.getRecipient();
                return data;
            }
        }

        context = new TestContext();

        String path = this.getClass().getCanonicalName();
        context.bootstrap(path);

        context.setMenuStrategy(menuStrategy);

    }

    @After
    public void tearDown() throws Exception {
        StateRepository.delete(this.context.state);
    }

    /**
     * Test to see that the message "Got it!" is sent in the response.
     */
    @Test
    public void testContext() {

        String data = "12;08034206970";
        context.morphToRequest(data);
        context.executeStrategy();

        assertEquals("Welcome to OneMoney;08034206970", context.dispatch());
    }

    @Test
    public void testFindStrategy(){

        String data = "1;08034206970";
        context.morphToRequest(data);
        context.findStrategy();
        context.executeStrategy();

        assertEquals("I found Test Strategy;08034206970", context.dispatch());

    }
}
