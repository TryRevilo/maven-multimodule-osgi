package com.bw.osgi.provider.impl;

import com.bw.osgi.provider.able.HelloWorldService;

public class HelloWorldServiceImpl implements HelloWorldService {
	
	int i = 0;
    @Override
    public void hello(String name){
    	i++;
        System.out.println("Hello World " + name + " : " + i);
    }
}