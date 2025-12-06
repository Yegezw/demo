package com.zzw.consumer.controller;

import com.zzw.DemoService;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController("demo")
public class DemoController {

    @DubboReference
    private DemoService demoService;

    @GetMapping("test")
    public void test() {
        log.info(demoService.sayHello("dubbo"));
    }
}
