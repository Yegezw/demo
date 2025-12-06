package com.zzw.more.handle;

import com.google.common.eventbus.Subscribe;

public class HandleA
{

    @Subscribe
    public void handle(EventA eventA)
    {
        System.out.println("HandleA -> " + eventA);
    }
}
