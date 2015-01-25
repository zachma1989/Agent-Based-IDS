public class CSensorAgent {	
	public static void main(String[] args)
	{
		// TODO implement sensor agent		
		
		// Same version with the previous Agent_base class
		
	    protected long agentId        = -1;    // this agent's identifier

	    /**
	     * setId( ) sets this agent identifier: agentId.
	     *
	     * @param id an identifier to set to this agent.
	     */
	    public void setId( long id ) {
		this.agentId = id;
	    }

	    /**
	     * getId( ) returns this agent identifier: agentId.
	     *
	     * @param this agent's identifier.
	     */
	    public long getId( ) {
	    	return this.agentId;
	    }
	    
	    public static void main( String[] args ) {
	        
	        try {
	            
	            Agent_base myAgent = new Agent_base();
	            
	            int testInt = 0;
	                
	            while ( true ) {
	                
	                testInt++;
	                myAgent.setId( testInt );
	                
	                Thread.sleep( 2000 );
	                
	                System.out.println( "Current value of testInt is: " + testInt );
	            }
	            
	        } catch (Exception ex) {
	            ex.printStackTrace();
	        }
	        
	    }
	}

}
