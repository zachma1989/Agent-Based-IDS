package org.sunspotworld.demo;

import com.sun.spot.io.j2me.radiostream.RadiostreamConnection;
import com.sun.spot.peripheral.IBattery;
import com.sun.spot.peripheral.IPowerController;
import com.sun.spot.peripheral.ISleepManager;
import com.sun.spot.peripheral.ISpot;
import com.sun.spot.peripheral.IUSBPowerDaemon;
import com.sun.spot.peripheral.Spot;
import com.sun.spot.peripheral.radio.RadioFactory;
import com.sun.spot.resources.Resources;
import com.sun.spot.resources.transducers.ITriColorLED;
import com.sun.spot.resources.transducers.ITriColorLEDArray;
import com.sun.spot.resources.transducers.LEDColor;
import com.sun.spot.service.BootloaderListenerService;
import com.sun.spot.util.IEEEAddress;
import com.sun.spot.util.Utils;
import com.sun.squawk.Isolate;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import javax.microedition.io.Connector;
import javax.microedition.midlet.MIDlet;
import javax.microedition.midlet.MIDletStateChangeException;

public class PowerTest
  extends MIDlet
{
  private static final LEDColor WHITE = new LEDColor(255, 255, 255);
  public static final double BATTERY_CAPACITY = 3.7D;
  private static final String IEEEADDRESS = "0014.4F01.0000.7B87";
  private static final int HOST_PORT = 67;
     
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
  private static final int TIMEOUT = 2000;
  private static final int SAMPLE_PERIOD = 1000;
  private static final int AGENT_COUNT = 200;
  
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
          if (!PowerTest.flag)
          {
            System.out.println("time flag");
            PowerTest.this.start = this.index;
            PowerTest.flag = true;
          }
          //if(index > 20) {
          //this.index = 0;
          PowerTest.this.array[this.index] = ((short)PowerTest.this.pwr.getIdischarge());
          
          System.out.println( this.index + ": " + PowerTest.this.array[this.index]);
          
          this.index += 1;
          }
        }
      //}
    }, 0, 10);
    
    System.out.println("After Timer");
     try {
          flag = true;
          receivingAgent();
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
  
   protected void receivingAgent() throws MIDletStateChangeException {
        
        String ourAddress = System.getProperty("IEEE_ADDRESS");
        
//        ILightSensor lightSensor = (ILightSensor)Resources.lookup(ILightSensor.class);
//        ITriColorLED led = (ITriColorLED)Resources.lookup(ITriColorLED.class, "LED7");
        
        System.out.println("Starting Agent receiver application on " + ourAddress + " ...");

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
                    
                    while ( dis.available() == 0 );
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
       
  private void AgentVoltageTest()
  {
    int v = this.pwr.getVbatt();
    
    createAgent();
    
    System.out.println("Battery Before Agent Creation " + v);
    System.out.println("Battery After Agent Creation " + this.pwr.getVbatt());
    System.out.println();
  }
  
  private void AgentCurrentTest()
  {
    double currentBefore = this.pwr.getIdischarge();
    System.out.println("DEBUG");
    createAgent();
    
    double currentAfter = this.pwr.getIdischarge();
    System.out.println("Current Before Agent Creation Method " + currentBefore);
    System.out.println("Current After Agent Creation Method " + currentAfter);
    System.out.println();
  }
  
  private void createAgent()
  {
    //wait for timer task to init. 
    Utils.sleep(1000);
    flag = false;
    for (int i = 0 ; i < AGENT_COUNT; i++)
    {
      Isolate agent = null;
      try
      {
        String[] args = { "Success" };
        agent = new Isolate("org.sunspotworld.demo.Agent", args, null, Isolate.currentIsolate().getParentSuiteSourceURI());
        agent.start();
        
//        while (agent.isAlive()) {}
        
        agent.join();
        
//        if ( i == AGENT_COUNT - 1 )
//            agent.join();
        
      }
      catch (Exception e)
      {
        System.err.println("Caught " + e + " while performing creating a new agent.");
      }
      if (i == 99) {
        System.out.println("Last Iteration Current " + this.pwr.getIdischarge());
      }
    }
  }

  
  private void TimeTest()
  {
    long timeBefore = this.slp.getUpTime();
    
    createAgent();
    
    long timeAfter = this.slp.getUpTime();
    long duration = timeAfter - timeBefore;
    
    System.out.println("Time Elapsed " + duration);
  }
  
  private void IdleCurrentTest()
  {
    this.slp.disableDeepSleep();
    Utils.sleep(180000L);
    System.out.println("Idle Current " + this.pwr.getIdischarge());
  }
  
  private void clearAgents()
  {
    Isolate[] agents = Isolate.getIsolates();
    
    System.out.println("Running agent #:" + agents.length);
    for (int i = 0; i < agents.length; i++) {
      if (agents[i].isAlive()) {
        agents[i].exit(0);
      }
    }
    System.out.println("Running agents #:" + Isolate.getIsolates().length);
  }
  
  protected void pauseApp() {}
  
  protected void destroyApp(boolean unconditional)
    throws MIDletStateChangeException
  {}
  
  private void measureReading()
  {
    Utils.sleep(1000L);
    int time = 0;
    while (time < 100)
    {
      Utils.sleep(1000L);
      if (time % 10 == 0)
      {
        System.out.println(this.bat.rawBatteryData());
        Utils.sleep(1000L);
      }
      System.out.println(1);
      Utils.sleep(1000L);
      this.currentDischarge += this.pwr.getIdischarge();
      


      System.out.println();
      System.out.println(this.runningTime + " milliseconds");
      System.out.println("currentDischarge() " + this.currentDischarge + " mA");
      

      System.out.println("getBatteryLevel() " + this.bat.getBatteryLevel());
      System.out.println();
      time++;
    }
    this.batteryLevelAfter = this.bat.getBatteryLevel();
    

    double batteryCapacityDifference = this.bat.getAvailableCapacity() - this.capacityBefore;
    
    long timeAfterLoop = this.slp.getUpTime();
    
    this.runningTime = (timeAfterLoop - this.pre);
    
    Utils.sleep(5000L);
    System.out.println("getAvailableCapacity() BEFORE Loop " + this.capacityBefore);
    System.out.println("getAvailableCapacity() AFTER loop " + this.bat.getAvailableCapacity());
    System.out.println("Difference of Capacities Before and After " + batteryCapacityDifference);
    System.out.println("Battery Level Before Running " + this.batteryLevelBefore);
    System.out.println("Battery Level After Running " + this.batteryLevelAfter);
    System.out.println("Time Before Loop " + this.pre);
    System.out.println("Time After Loop " + timeAfterLoop);
    System.out.println("Total Time Running " + this.runningTime * 0.001D + "seconds");
    System.out.println(this.bat.rawBatteryData());
  }
  
  private void LEDTest()
  {
    this.leds = ((ITriColorLEDArray)Resources.lookup(ITriColorLEDArray.class)).toArray();
    for (int i = 0; i < 8; i++)
    {
      this.leds[i].setOff();
      this.leds[i].setColor(WHITE);
      this.leds[i].setOn();
      Utils.sleep(100L);
    }
    if (this.bat.getState() == 3)
    {
      for (int i = 0; i < 8; i++) {
        this.leds[i].setOff();
      }
      this.sleep_discharge = this.pwr.getIdischarge();
      this.sleep_battery = this.pwr.getVbatt();
      for (int j = 0; j < 150; j++) {
        for (int i = 0; i < 8; i++)
        {
          this.leds[i].setOn();
          Utils.sleep(1000L);
          this.leds[i].setOff();
        }
      }
      this.run_discharge = this.pwr.getIdischarge();
      this.run_battery = this.pwr.getVbatt();
      
      System.out.println("Sleep");
      System.out.println("Discharge rate " + this.sleep_discharge);
      System.out.println("Battery before " + this.sleep_battery);
      
      System.out.println("After Running");
      System.out.println("Discharge rate " + this.run_discharge);
      System.out.println("Battery After " + this.run_battery);
      
      double dv = this.sleep_battery - this.run_battery;
      double di = this.run_discharge - this.sleep_discharge;
      




      double ir = (dv / di - 0.1D) * 0.001D + 0.5D;
      System.out.println("Battery Resistance " + ir);
    }
  }
}
