
import com.sun.spot.io.j2me.radiostream.*;
import com.sun.spot.io.j2me.radiogram.*;
import com.sun.spot.peripheral.radio.RadioFactory;
import com.sun.spot.peripheral.radio.IRadioPolicyManager;
import com.sun.spot.util.IEEEAddress;


import com.sun.spot.resources.Resources;
import com.sun.spot.resources.transducers.ITriColorLED;
import com.sun.spot.resources.transducers.ILightSensor;
import com.sun.spot.util.Utils;

import javax.microedition.io.*;
import javax.microedition.midlet.MIDlet;
import javax.microedition.midlet.MIDletStateChangeException;

import com.sun.squawk.Isolate;

import java.io.*;
import java.util.Date;
import java.io.DataOutputStream;

import interfaces.IAgent;
import interfaces.IAgentReceiver;

/*
 * class CAgentReceiver 
 * Implementation class for receiving agents
 */
public class CSunspotAgentRadiostreamReceiver implements IAgentReceiver {
	
    private static final int HOST_PORT = 67;
    private static final int TIMEOUT = 2000;
    private static final int SAMPLE_PERIOD = 5000;  // in milliseconds
    
    private static final String IEEEADDRESS = "0014.4F01.0000.7D69";	// Hard coded address

	public IAgent TryReceiveAgent() {
		
		try {
			// TODO: the sleep simulates waiting for an
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			return null;
		}
		
		RadiostreamConnection conn = null;
        InputStream dis = null;
		
		dos = agent.Serialize();
		try {
            // Open up a unicast connection to the target sensor
           conn = (RadiostreamConnection) Connector.open("radiostream://" + IEEEADDRESS + ":" + HOST_PORT);  //Doesn't work between sensor and basestation, need to figure out why
//           conn.setTimeout( TIMEOUT );

           dis = conn.openInputStream();
           
       } catch (IOException e) {
           e.printStackTrace();
       }
       
//       int reading = 0;
       
//       try {
//           // Get the current time and sensor reading
////           long now = System.currentTimeMillis();
//           reading = lightSensor.getValue();
//           
//           // Flash an LED to indicate a sampling event
//           led.setRGB(255, 255, 255);
//           led.setOn();
//           Utils.sleep(50);
//           led.setOff();
//           
//       } catch (Exception e) {
//           System.err.println("Caught " + e + " while performing creating a new agent.");
//       }

       try {
           
    	   migratedAgent = Isolate.load( new DataInputStream( dis ) , "Migration");
           
    	   return CAgentFactory.CreateUnknownIsolatedAgent( migratedAgent );
           
           System.out.println( migratedAgent.getMainClassArguments()[0] );

           // Close connnection
           dos.close();
           conn.close();
           
           // Go to sleep to conserve battery
//           Utils.sleep( SAMPLE_PERIOD );
           
       } catch (Exception e) {
           System.err.println("Caught " + e + " while performing sensor action.");
       }
					
			


}
