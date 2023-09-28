package com.cox.oss.pet.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.InetAddress;
import java.net.UnknownHostException;

@RestController
@Slf4j
public class HelloWorldController {

    @GetMapping("/helloworld")
    public String helloWorld() throws UnknownHostException {return "Hello from " + InetAddress.getLocalHost().getHostName();}

}
