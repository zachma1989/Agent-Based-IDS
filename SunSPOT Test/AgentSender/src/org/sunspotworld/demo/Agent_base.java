package org.sunspotworld.demo;

import java.io.*;

/**
 * Mobile.Agent is the base class of all user-define mobile agents. It carries
 * an agent identifier, the next host IP and port, the name of the function to
 * invoke at the next host, arguments passed to this function, its class name,
 * and its byte code. It runs as an independent thread that invokes a given
 * function upon migrating the next host.
 *
 * @author  Zach Ma
 */
public class Agent_base{
    
    public static void main( String[] args ) {
        
        try {
            
            Agent_base myAgent = new Agent_base();
            
            int testInt = 0;
                
            while ( true ) {
                
                testInt++;
                
                int sum = 0, mul = 1;
                for (int i = 1; i < 1000; i++) {
                    sum += i;
                    mul *= i;
                }
                
                System.out.println( "Current value of testInt is: " + testInt );
            }
            
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        
    }


}
