package com.jorisaerts.omda.service;

import com.jorisaerts.omda.util.ProxyObject;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class ProxyObjectTest {


    interface TestInterface {

        String getName();

        void setName(String name);

    }

    @Test
    public void testGettersAndSetters() {

        
        TestInterface ti = ProxyObject.create(TestInterface.class);
        // not set yet
        assertNull(ti.getName());

        // after being set
        ti.setName("Joris");
        assertEquals("Joris", ti.getName());


        // after being set the second time
        ti.setName("Aerts");
        assertEquals("Aerts", ti.getName());

        // set to null
        ti.setName(null);
        assertNull(ti.getName());
    }

}
