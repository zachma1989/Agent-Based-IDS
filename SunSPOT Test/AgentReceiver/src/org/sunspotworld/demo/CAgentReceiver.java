
package org.sunspotworld.demo;

import com.sun.spot.io.j2me.radiostream.*;
import com.sun.spot.io.j2me.radiogram.*;
import com.sun.spot.peripheral.radio.RadioFactory;
import com.sun.spot.peripheral.radio.IRadioPolicyManager;
import com.sun.spot.util.IEEEAddress;


import com.sun.spot.resources.Resources;
import com.sun.spot.resources.transducers.ITriColorLED;
import com.sun.spot.resources.transducers.ILightSensor;
import com.sun.spot.util.Utils;
import com.sun.squawk.Isolate;

import javax.microedition.io.*;
import javax.microedition.midlet.MIDlet;
import javax.microedition.midlet.MIDletStateChangeException;

import java.io.*;
import java.util.Date;

/**
 * Implementation class for receiving agents
 * 
 * @author: Zach Ma
 */
public class CAgentReceiver extends MIDlet {
    
    private static final int HOST_PORT = 67;
    private static final int TIMEOUT = 5000;
    private static final int SAMPLE_PERIOD = 2000;  // in milliseconds
    
//    private static final String IEEEADDRESS = "0014.4F01.0000.78ED";
//    private static final String IEEEADDRESS = "0014.4F01.0000.77A0";
    private static final String IEEEADDRESS = "0014.4F01.0000.7B87";
    
    protected void startApp() throws MIDletStateChangeException {
        
        String ourAddress = System.getProperty("IEEE_ADDRESS");
        
//        ILightSensor lightSensor = (ILightSensor)Resources.lookup(ILightSensor.class);
//        ITriColorLED led = (ITriColorLED)Resources.lookup(ITriColorLED.class, "LED7");
        
        System.out.println("Starting Agent sender application on " + ourAddress + " ...");

	// Listen for downloads/commands over USB connection
	new com.sun.spot.service.BootloaderListenerService().getInstance().start();

        try {
            
            long ourAddr = RadioFactory.getRadioPolicyManager().getIEEEAddress();
            System.out.println("Our radio address = " + IEEEAddress.toDottedHex(ourAddr));
            
        } catch (Exception e) {
            System.err.println("Caught " + e + " in connection initialization.");
            notifyDestroyed();
        }
        
        System.out.println("Established connection to " + IEEEADDRESS + "!");
        
        RadiostreamConnection conn = null;
        InputStream dis = null;

        try {

            conn = (RadiostreamConnection) Connector.open("radiostream://" + IEEEADDRESS + ":" + HOST_PORT);  //Doesn't work
//            conn.setTimeout( TIMEOUT );

            dis = conn.openInputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        while (true) {
            
            Isolate migratedAgent;
            
            try {
                
                // For mearuring time
                System.out.println( new Date() );
                
                try {
                    
                    System.out.println( "Available bytes: " + dis.available() );
                    
                    migratedAgent = Isolate.load( new DataInputStream( dis ), "Migration");
                
                    System.out.println( "Received Agent on " + new Date() + "!" );

                    migratedAgent.unhibernate();

                    System.out.println( migratedAgent.getMainClassArguments()[0] );
                
                } catch (Exception ce) {
//                    System.err.println( ce.getMessage() );
                    ce.printStackTrace();
                    ce.getMessage();
                }
                
                // Close connection
//                dis.close();
//                conn.close();
            
            } catch (Exception e) {
                System.err.println("Caught " + e + " while performing sensor action.");
            }
        }
    }
    
    protected void pauseApp() {
        // This will never be called by the Squawk VM
    }
    
    protected void destroyApp(boolean arg0) throws MIDletStateChangeException {
        // Only called if startApp throws any exception other than MIDletStateChangeException
    }
}
