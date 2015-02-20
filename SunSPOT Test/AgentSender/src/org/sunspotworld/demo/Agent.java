/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.sunspotworld.demo;

import java.util.*;


public class Agent
{
    private static final int NUM_CLUSTERS = 2;    // Total clusters.
    private static final int TOTAL_DATA = 7;      // Total data points.
    
    private static final double SAMPLES[][] = new double[][] {{1.0, 1.0}, 
                                                                {1.5, 2.0}, 
                                                                {3.0, 4.0}, 
                                                                {5.0, 7.0}, 
                                                                {3.5, 5.0}, 
                                                                {4.5, 5.0}, 
                                                                {3.5, 4.5}};
    
    private static Vector dataSet = new Vector();
    private static Vector centroids = new Vector();
    
    private static void initialize()
    {
        System.out.println("Centroids initialized at:");
        centroids.addElement(new Centroid(1.0, 1.0)); // lowest set.
        centroids.addElement(new Centroid(5.0, 7.0)); // highest set.
        System.out.println("     (" + ((Centroid)centroids.elementAt(0)).X() + ", " + ((Centroid)centroids.elementAt(0)).Y() + ")");
        System.out.println("     (" + ((Centroid)centroids.elementAt(1)).X() + ", " + ((Centroid)centroids.elementAt(1)).Y() + ")");
        System.out.print("\n");
        return;
    }
    private static int power(int n, int p)
    {
        for(int i = 0; i < p; i++)
        {
            n *= n;
        }
        return n;
    }
    private static void kMeanCluster()
    {
        final double bigNumber = power(10,10);    // some big number that's sure to be larger than our data range.
        double minimum = bigNumber;                   // The minimum value to beat. 
        double distance = 0.0;                        // The current minimum value.
        int sampleNumber = 0;
        int cluster = 0;
        boolean isStillMoving = true;
        Data newData = null;
        
        // Add in new data, one at a time, recalculating centroids with each new one. 
        while(dataSet.size() < TOTAL_DATA)
        {
            newData = new Data(SAMPLES[sampleNumber][0], SAMPLES[sampleNumber][1]);
            dataSet.addElement(newData);
            minimum = bigNumber;
            for(int i = 0; i < NUM_CLUSTERS; i++)
            {
                distance = dist(newData, (Centroid)centroids.elementAt(i));
                if(distance < minimum){
                    minimum = distance;
                    cluster = i;
                }
            }
            newData.cluster(cluster);
            
            // calculate new centroids.
            for(int i = 0; i < NUM_CLUSTERS; i++)
            {
                int totalX = 0;
                int totalY = 0;
                int totalInCluster = 0;
                for(int j = 0; j < dataSet.size(); j++)
                {
                    Data temp = (Data)dataSet.elementAt(j);
                    
                    if(temp.cluster() == i){
                        totalX += temp.X();
                        totalY += temp.Y();
                        totalInCluster++;
                    }
                }
                if(totalInCluster > 0){
                    
                    Centroid tmp = (Centroid)centroids.elementAt(i);
                    
                    tmp.X(totalX / totalInCluster);
                    tmp.Y(totalY / totalInCluster);
                }
            }
            sampleNumber++;
        }
        
        // Now, keep shifting centroids until equilibrium occurs.
        while(isStillMoving)
        {
            // calculate new centroids.
            for(int i = 0; i < NUM_CLUSTERS; i++)
            {
                int totalX = 0;
                int totalY = 0;
                int totalInCluster = 0;
                for(int j = 0; j < dataSet.size(); j++)
                {
                    Data temp = (Data)dataSet.elementAt(j);
                    
                    if(temp.cluster() == i){
                        totalX += temp.X();
                        totalY += temp.Y();
                        totalInCluster++;
                    }
                }
                if(totalInCluster > 0){
                    
                    Centroid tmp = (Centroid)centroids.elementAt(i);
                    
                    tmp.X(totalX / totalInCluster);
                    tmp.Y(totalY / totalInCluster);
                }
            }
            
            // Assign all data to the new centroids
            isStillMoving = false;
            
            for(int i = 0; i < dataSet.size(); i++)
            {
                Data tempData = (Data)dataSet.elementAt(i);
                minimum = bigNumber;
                for(int j = 0; j < NUM_CLUSTERS; j++)
                {
                    distance = dist(tempData, (Centroid)centroids.elementAt(j));
                    if(distance < minimum){
                        minimum = distance;
                        cluster = j;
                    }
                }
                tempData.cluster(cluster);
                if(tempData.cluster() != cluster){
                    tempData.cluster(cluster);
                    isStillMoving = true;
                }
            }
        }
        return;
    }
    
    /**
     * // Calculate Euclidean distance.
     * @param d - Data object.
     * @param c - Centroid object.
     * @return - double value.
     */
    private static double dist(Data d, Centroid c)
    {
        return Math.sqrt(power((int) (c.Y() - d.Y()), 2) + power((int) (c.X() - d.X()), 2));
              
    }
    
    private static class Data
    {
        private double mX = 0;
        private double mY = 0;
        private int mCluster = 0;
        
        public Data()
        {
            return;
        }
        
        public Data(double x, double y)
        {
            this.X(x);
            this.Y(y);
            return;
        }
        
        public void X(double x)
        {
            this.mX = x;
            return;
        }
        
        public double X()
        {
            return this.mX;
        }
        
        public void Y(double y)
        {
            this.mY = y;
            return;
        }
        
        public double Y()
        {
            return this.mY;
        }
        
        public void cluster(int clusterNumber)
        {
            this.mCluster = clusterNumber;
            return;
        }
        
        public int cluster()
        {
            return this.mCluster;
        }
    }
    
    private static class Centroid
    {
        private double mX = 0.0;
        private double mY = 0.0;
        
        public Centroid()
        {
            return;
        }
        
        public Centroid(double newX, double newY)
        {
            this.mX = newX;
            this.mY = newY;
            return;
        }
        
        public void X(double newX)
        {
            this.mX = newX;
            return;
        }
        
        public double X()
        {
            return this.mX;
        }
        
        public void Y(double newY)
        {
            this.mY = newY;
            return;
        }
        
        public double Y()
        {
            return this.mY;
        }
    }
    
    public static void main(String[] args)
    {   
//        initialize();
//        kMeanCluster();
//        
//        // Print out clustering results.
//        for(int i = 0; i < NUM_CLUSTERS; i++)
//        {
//            System.out.println("Cluster " + i + " includes:");
//            for(int j = 0; j < TOTAL_DATA; j++)
//            {
//                Data temp = (Data)dataSet.elementAt(j);
//                
//                if(temp.cluster() == i){
//                    System.out.println("     (" + temp.X() + ", " + temp.Y() + ")");
//                }
//            } // j
//            System.out.println();
//        } // i
//        
//        // Print out centroid results.
//        System.out.println("Centroids finalized at:");
//        for(int i = 0; i < NUM_CLUSTERS; i++)
//        {
//            Centroid tmp = (Centroid)centroids.elementAt(i);
//            
//            System.out.println("     (" + tmp.X() + ", " + tmp.Y());
//        }
//        System.out.print("\n");
//        return;
        
        try {
            while( true )
                Thread.sleep( 2000 );
            
        } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
              
    return;
    }
}


