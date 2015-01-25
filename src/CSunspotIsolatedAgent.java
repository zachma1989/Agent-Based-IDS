import interfaces.IAgent;
import interfaces.IContext;
import interfaces.ILocalStorage;

import java.io.*;
import java.util.List;

public class CSunspotIsolatedAgent extends CAgentBase {
    
	String m_classPath = null;
	
	public CSunspotIsolatedAgent(String classPath)
	{
		m_classPath = classPath;
		
		// TODO create the isolated object here
	}
	
    // live data to carry with the agent upon a migration
    protected long agentId = -1;    // this agent's identifier

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
    	return agentId;
    }

    // Implementing IAgent's methods
    public boolean Run(IContext context, ILocalStorage storage) {    	
    	// TODO
    	
    	// Call Start, Join on isolate object.
    	return false;
    }

	public DataOutputStream Serialize() {
		// TODO Auto-generated method stub
		return null;
	}

	public IAgent Deserialize(DataOutputStream dos) {
		// TODO Auto-generated method stub
		return null;
	}
    

}
