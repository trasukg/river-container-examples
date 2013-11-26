/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.apache.river.container.hello.example.api;

import java.io.IOException;
import java.rmi.Remote;

/**
 *
 * @author trasukg
 */
public interface Greeter extends Remote {
    public String sayHello(String name) throws IOException;
}
