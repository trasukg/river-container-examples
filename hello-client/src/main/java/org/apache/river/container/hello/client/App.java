package org.apache.river.container.hello.client;

import java.util.Scanner;
import net.jini.config.Configuration;
import net.jini.config.ConfigurationProvider;
import net.jini.core.entry.Entry;
import net.jini.core.lookup.ServiceItem;
import net.jini.core.lookup.ServiceTemplate;
import net.jini.lookup.ServiceDiscoveryManager;
import net.jini.security.ProxyPreparer;
import org.apache.river.container.hello.example.api.Greeter;

/**
 * Hello world!
 *
 */
public class App {

    public static void main(String[] args) {
        try {
            // Get the config
            Configuration config = ConfigurationProvider.getInstance(args);
            // From the config, get the ServiceDiscoveryManager
            ServiceDiscoveryManager sdm=
                    (ServiceDiscoveryManager) 
                    config.getEntry("hello", "sdm", ServiceDiscoveryManager.class);
            // We'll also need a proxy preparer.
            ProxyPreparer preparer=(ProxyPreparer) config.getEntry("hello", 
                    "greeterPreparer", ProxyPreparer.class);
            // While the sdm is finding registrars, let's ask the user for their
            // name.
            Scanner in = new Scanner(System.in);
            System.out.println("Please enter your name:");
            String name = in.nextLine();
            // Query the sdm for Greeter services.
            ServiceTemplate template=new ServiceTemplate(
                    null,
                    new Class[] { Greeter.class },
                    new Entry[0]
            );
            ServiceItem[] serviceItems=sdm.lookup(template, 5, null);
            if (serviceItems.length==0) {
                System.out.println("We didn't find any greeter services.");
                System.exit(0);
            }
            // Pick a service item
            ServiceItem chosen=serviceItems[0];
            // Prepare the proxy.
            Greeter greeter=(Greeter) preparer.prepareProxy(chosen.service);
            // Make the call
            String message=greeter.sayHello(name);
            // Print the result
            System.out.println("Greeter replied '" + message + "'.");
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            System.exit(0);
        }
    }
}
