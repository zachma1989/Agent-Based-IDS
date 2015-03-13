
package org.sunspotworld.demo;

import com.sun.spot.io.j2me.radiostream.*;
import com.sun.spot.io.j2me.radiogram.*;
import com.sun.spot.peripheral.IBattery;
import com.sun.spot.peripheral.IPowerController;
import com.sun.spot.peripheral.ISleepManager;
import com.sun.spot.peripheral.IUSBPowerDaemon;
import com.sun.spot.peripheral.Spot;
import com.sun.spot.peripheral.radio.RadioFactory;
import com.sun.spot.peripheral.radio.IRadioPolicyManager;
import com.sun.spot.util.IEEEAddress;


import com.sun.spot.resources.Resources;
import com.sun.spot.resources.transducers.ITriColorLED;
import com.sun.spot.resources.transducers.ILightSensor;
import com.sun.spot.resources.transducers.LEDColor;
import com.sun.spot.service.BootloaderListenerService;
import com.sun.spot.util.Utils;

import javax.microedition.io.*;
import javax.microedition.midlet.MIDlet;
import javax.microedition.midlet.MIDletStateChangeException;

import com.sun.squawk.Isolate;
import java.io.*;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;


/**
 * Implementation class for sending agents
 * 
 * @author: Zach Ma
 */
public class CAgentSender extends MIDlet {
    
    private static final int HOST_PORT = 67;
    private static final int TIMEOUT = 2000;
    private static final int SAMPLE_PERIOD = 5000;  // in milliseconds
    
//    private static final String IEEEADDRESS = "0014.4F01.0000.7D69";
    private static final String IEEEADDRESS = "0014.4F01.0000.78ED";
  
    
    private static final LEDColor WHITE = new LEDColor(255, 255, 255);
  public static final double BATTERY_CAPACITY = 3.7D;
  private IUSBPowerDaemon usb;
  private IPowerController pwr;
  private ISleepManager slp;
  private IBattery bat;
  private Timer timer;
  private int currentDischarge;
  private long runningTime;
  private int batteryLevelBefore;
  private int batteryLevelAfter;
  private double capacityBefore;
  private long pre;
  private ITriColorLED[] leds;
  private double sleep_discharge;
  private int sleep_battery;
  private double run_discharge;
  private int run_battery;
  short[] array;
  int start;
  public static boolean flag;

  
  protected void startApp()
    throws MIDletStateChangeException
  {
    try
    {
      this.usb = Spot.getInstance().getUsbPowerDaemon();
      this.pwr = Spot.getInstance().getPowerController();
      this.slp = Spot.getInstance().getSleepManager();
      this.timer = new Timer();
      this.bat = this.pwr.getBattery();
      this.pre = this.slp.getUpTime();
      this.capacityBefore = this.bat.getAvailableCapacity();
      this.batteryLevelBefore = this.bat.getBatteryLevel();
      
      this.array = new short[200];
      
      run();
    }
    catch (IOException ex)
    {
      ex.printStackTrace();
    }
  }
  
  private void run()
    throws IOException
  {
    BootloaderListenerService.getInstance().start();
    
    Utils.sleep(7000);
    System.out.println("Before Timer");
    flag = true;
    this.timer.schedule(new TimerTask()
    {
      int index = 0;
      
      public void run()
      {
        if (this.index < 200)
        {
          if (!CAgentSender.flag)
          {
            System.out.println("time flag");
            CAgentSender.this.start = this.index;
            PowerTest.flag = true;
          }
          if(index > 20)
          //this.index = 0;
          CAgentSender.this.array[this.index] = ((short)CAgentSender.this.pwr.getIdischarge());
          
          System.out.println( this.index + ": " + CAgentSender.this.array[this.index]);
          
          this.index += 1;
        }
      }
    }, 0, 50);
    
    System.out.println("After Timer");
    
        try {
            migrateAgent();
          //  receivingAgent();
        } catch (MIDletStateChangeException ex) {
            ex.printStackTrace();
        }
    

    float averageCurrent = 0.0F;
    
    int i = this.start;
    while ((this.array[i] > 0) && ( i < this.array.length ))
    {
        System.out.println( i + ": " + this.array[i] );
        
      averageCurrent += this.array[i];
      i++;
    }
    System.out.println("i = " + i);
    averageCurrent /= (i - this.start);
    
    System.out.println("Average current " + averageCurrent);
    

    notifyDestroyed();
  }
    
    protected void migrateAgent() throws MIDletStateChangeException {
        
        String ourAddress = System.getProperty("IEEE_ADDRESS");
        ILightSensor lightSensor = (ILightSensor)Resources.lookup(ILightSensor.class);
        ITriColorLED led = (ITriColorLED)Resources.lookup(ITriColorLED.class, "LED7");
        
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
        
        RadiostreamConnection conn = null;
        OutputStream dos = null;

        

        try {
             // Open up a unicast connection to the target sensor
            conn = (RadiostreamConnection) Connector.open("radiostream://" + IEEEADDRESS + ":" + HOST_PORT);  //Doesn't work between sensor and basestation, need to figure out why
//            conn.setTimeout( TIMEOUT );

            dos = conn.openOutputStream();

        } catch (IOException e) {
            e.printStackTrace();
        }
        
        System.out.println("Established connection to " + IEEEADDRESS + "!");

        while (true) {
            
            Isolate agent = null;
            
            try {
	
                // Serialize agent into byte array
                String[] args = { "Success" };
                agent = new Isolate("org.sunspotworld.demo.Agent", args, null, Isolate.currentIsolate().getParentSuiteSourceURI() );
                agent.start();
                
            } catch (Exception e) {
                System.err.println("Caught " + e + " while performing creating a new agent.");
            }

            try {
                
                System.out.println( "Agent class name is " + agent.getMainClassName() );
                System.out.println( "Its curent AgentId is " + agent.getMainClassArguments()[0] );
                
                while ( agent.isNew() );
                
                // For Debuggin
                if ( agent.isAlive() )
                    System.out.println( "Agent is alives" );
                
                if ( agent.isHibernated() )
                    System.out.println( "Agent is hibernated" );
                
                
                if ( !agent.isHibernated() ) {
                    
                    // For Debugging
                    System.out.println( "Agent is alive here" );
                    
                    agent.hibernate();
                    agent.join();
                }
                
                // For measuring time
                System.out.println( new Date() );
                
                if ( agent.isHibernated() ) {
//                    agent.save( new DataOutputStream( dos ), "Migration" );
                    
                    // For debugging
                    dos.write( 100 );
                    dos.flush();
                    
                }
                
                System.out.println("Serialization completed!");
                
                // For measuring time
                System.out.println( new Date() );
                
                
                if ( agent.isExited() )
                    System.out.println( "Agent has exited!" );
                
                
                // Go to sleep to conserve battery
                Utils.sleep( SAMPLE_PERIOD );
                
                //Close connection
//                dos.close();
//                conn.close();
                
                
                
            } catch (Exception e) {
                System.err.println("Caught " + e + " while performing sensor action.");
                
                break;
                
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
