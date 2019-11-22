package com.tm30.structmenu.context;

import com.tm30.structmenu.message.Request;
import com.tm30.structmenu.repository.InputRepository;
import com.tm30.structmenu.repository.StateRepository;
import com.tm30.structmenu.strategy.MenuStrategy;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.Serializable;
import java.util.Optional;

import static org.junit.Assert.assertEquals;

public class ContextTest implements Serializable {

    private Context context;

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
                this.request.setServiceCode(dataArray[2]);
            }

            @Override
            public Object dispatch() {
                String data;

                data = this.response.getMessage() + ";" + this.response.getRecipient() + ";" + this.response.getServiceCode();
                return data;
            }
        }

        context = new TestContext();

//        String path = this.getClass().getCanonicalName();
        context.setMenuStrategy(new MenuStrategy());

        context.bootstrap();


    }

    @After
    public void tearDown() throws Exception {
        StateRepository.delete(this.context.state);
    }

    @Test
    public void testGetMenuStrategyMatchBySlug() {
        String data = "menu;08034206970;55010";

        context.morphToRequest(data);
        context.executeStrategy();
        assertEquals("Welcome to Test Menu Service\n1. Travel\n2. Health\n;08034206970;55010", context.dispatch());
    }

    @Test
    public void testGetStrategyBySlug() {
        String data = "travel;08034206970;55010";
        context.morphToRequest(data);
        context.executeStrategy();
        assertEquals("This is Travel Strategy for 08034206970;08034206970;55010", context.dispatch());
    }


    @Test
    public void testGetStrategyByUniqueId() {
        String data = "1;08034206970;55010";
        context.morphToRequest(data);
        context.executeStrategy();
        assertEquals("This is Travel Strategy for 08034206970;08034206970;55010", context.dispatch());
    }

    @Test
    public void testStrategyWithInput() {

        String data = "health;08034206970;55010";
        context.morphToRequest(data);
        context.executeStrategy();

        assertEquals("Enter your Name;08034206970;55010", context.dispatch());

    }

    @Test
    public void testStrategyInputResponse() {

        String data = "health;08034206970;55010";
        context.morphToRequest(data);
        context.executeStrategy();
        String result = (String) context.dispatch();

        data = "Tofunmi Babatunde;08034206970;55010";
        context.morphToRequest(data);
        context.executeStrategy();

        assertEquals("This is Health Result for Tofunmi Babatunde;08034206970;55010", context.dispatch());
    }

}
