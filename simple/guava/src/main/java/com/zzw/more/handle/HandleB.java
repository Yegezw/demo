package com.zzw.more.handle;

import com.google.common.eventbus.Subscribe;

public class HandleB
{

    @Subscribe
    public void handle(EventB eventB)
    {
        System.out.println("HandleB -> " + eventB);
    }
}
