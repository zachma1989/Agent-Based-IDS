import interfaces.IAgent;

import java.util.Hashtable;

public class MobileAgentManager extends MobileAgentReceiver {	
	public boolean Run(Hashtable<String, String> parsedArgs) {
		System.out.println("MobileAgentManager Run()");
        // TODO
		// Create the dispatcher
		// Create agents
		// Send out agents
		
		// Temporarily i am creating a single dispatched agent
		IAgent dispatchedAgent = CAgentFactory.CreateSensorAgent();
		if (!m_sender.TrySendAgent(dispatchedAgent))
		{
			return false;
		}
		
		return RunAgentLoop(parsedArgs);
	}
	
	public static void main(String[] args)
	{
		System.out.println("MobileAgentReceiver main()");
		MobileAgentManager mam = new MobileAgentManager();
		if (!mam.Run(args))
			throw new java.lang.IllegalStateException("The Mobile Agent Manager terminated execution with a failure.");		
	}
}
