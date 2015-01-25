
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

import interfaces.IAgent;
import interfaces.IAgentSender;

/**
 * Implementation class for sending agents
 */
public class CSunspotAgentRadiostreamSender implements IAgentSender {
	
    private static final int HOST_PORT = 67;
    private static final int TIMEOUT = 2000;
    private static final int SAMPLE_PERIOD = 5000;  // in milliseconds
    
    private static final String IEEEADDRESS = "0014.4F01.0000.7D69";	// Hard coded address

	public boolean TrySendAgent(IAgent agent) {

		// TODO: the sleep simulates waiting for an
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			return false;
		}
		
		RadiostreamConnection conn = null;
        OutputStream dos = null;
		
		dos = agent.Serialize();
		try {
            // Open up a unicast connection to the target sensor
           conn = (RadiostreamConnection) Connector.open("radiostream://" + IEEEADDRESS + ":" + HOST_PORT);  //Doesn't work between sensor and basestation, need to figure out why
//           conn.setTimeout( TIMEOUT );

           dos = conn.openOutputStream();
           
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
           
           if ( !agent.isHibernated() ) {
               agent.hibernate();
//               agent.join();
           }
           
           if ( agent.isHibernated() ) {
               agent.save( new DataOutputStream( dos ), "Migration" );
           }
           
           // Output to radiostream
           dos.flush();

           // Close connnection
           dos.close();
           conn.close();
           
           // Go to sleep to conserve battery
//           Utils.sleep( SAMPLE_PERIOD );
           
       } catch (Exception e) {
           System.err.println("Caught " + e + " while performing sensor action.");
       }
		
		return true;
	}

}
