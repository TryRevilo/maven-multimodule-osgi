package com.bw.osgi.consumer;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;

import com.bw.osgi.provider.able.HelloWorldService;

public class HelloWorldActivator implements BundleActivator {
    private HelloWorldConsumer consumer;

    @Override
    public void start(BundleContext bundleContext) throws Exception {
        ServiceReference reference = bundleContext.getServiceReference(HelloWorldService.class.getName());

        consumer = new HelloWorldConsumer((HelloWorldService) bundleContext.getService(reference), "Oliver");
        consumer.startTimer();
    }

    @Override
    public void stop(BundleContext bundleContext) throws Exception {
        consumer.stopTimer();
    }
}