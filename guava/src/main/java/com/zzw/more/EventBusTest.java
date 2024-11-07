package com.zzw.more;

import com.google.common.eventbus.EventBus;
import com.zzw.more.handle.*;
import org.junit.jupiter.api.Test;

/**
 * 观察者模式
 */
public class EventBusTest
{

    @Test
    public void test()
    {
        EventBus bus = new EventBus();

        bus.register(new HandleA());
        bus.register(new HandleB());
        bus.register(new HandleC());

        bus.post(new EventA()); // HandleA
        bus.post(new EventB()); // HandleA + HandleB
        bus.post(new EventC()); // HandleC
    }
}
