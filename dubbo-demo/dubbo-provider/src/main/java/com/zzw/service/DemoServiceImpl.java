package com.zzw.service;

import com.zzw.DemoService;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboService;
import org.apache.dubbo.rpc.RpcContext;

@Slf4j
@DubboService
public class DemoServiceImpl implements DemoService {

    @Override
    public String sayHello(String name) {
        log.info("Hello {}, request from consumer: {}", name, RpcContext.getServiceContext().getRemoteAddress());
        return "Hello " + name;
    }
}
