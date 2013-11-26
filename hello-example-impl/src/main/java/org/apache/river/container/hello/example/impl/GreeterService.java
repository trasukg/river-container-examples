/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.apache.river.container.hello.example.impl;

import com.sun.jini.config.Config;
import com.sun.jini.start.LifeCycle;
import java.io.IOException;
import java.rmi.server.ExportException;
import net.jini.config.Configuration;
import net.jini.config.ConfigurationException;
import net.jini.config.ConfigurationProvider;
import net.jini.core.entry.Entry;
import net.jini.core.lookup.ServiceID;
import net.jini.discovery.DiscoveryManagement;
import net.jini.export.Exporter;
import net.jini.lookup.JoinManager;
import net.jini.lookup.ServiceIDListener;
import net.jini.security.ProxyPreparer;
import org.apache.river.container.hello.example.api.Greeter;

/**
 *
 * @author trasukg
 */
public class GreeterService implements Greeter, ServiceIDListener {
    private static final String GREETER="org.apache.river.container.examples.greeter";
    
    Configuration config=null;
    Greeter myProxy=null;
    Exporter exporter=null;
    ProxyPreparer registrarPreparer=null;
    JoinManager joinManager=null;
    DiscoveryManagement discoveryManager=null;
    Entry[] attributes=null;
    
    public GreeterService(String[] args, final LifeCycle lc) throws ConfigurationException, ExportException, IOException {
        config=ConfigurationProvider.getInstance(args);
        // Get the exporter and create our proxy.
        exporter=(Exporter) Config.getNonNullEntry(config, GREETER, "exporter", Exporter.class);
        myProxy=(Greeter) exporter.export(this);
        
        discoveryManager=(DiscoveryManagement) config.getEntry(GREETER, "discoveryManager", DiscoveryManagement.class);
        attributes=(Entry[]) config.getEntry(GREETER, "attributes", Entry[].class);
        
        // Publish it using the join manager.
        // We don't have to do anything with it - just creating it starts the join process.
        joinManager = new JoinManager(myProxy, attributes, this, discoveryManager, null, config);
        
    }
    
    public String sayHello(String name) throws IOException {
        return "Hi there " + name;
        
    }

    ServiceID sid=null;
    
    public void serviceIDNotify(ServiceID sid) {
        this.sid=sid;
    }
    
}
