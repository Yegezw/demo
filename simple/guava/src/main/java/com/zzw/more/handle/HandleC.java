package com.zzw.more.handle;

import com.google.common.eventbus.Subscribe;

public class HandleC
{

    @Subscribe
    public void handle(EventC eventC)
    {
        System.out.println("HandleC -> " + eventC);
    }
}
