import java.io.DataOutputStream;

import interfaces.IAgent;

public class CAgentFactory {

	public static IAgent CreateSensorAgent()
	{
		return new CSunspotIsolatedAgent("CSensorAgent");
	}
	
	public static IAgent CreateClusterHeadAgent()
	{
		return new CSunspotIsolatedAgent("CClusterHeadAgent");
	}
	
	public static IAgent CreateUnknownIsolatedAgent(DataOutputStream dos)
	{
		CSunspotIsolatedAgent agent = new CSunspotIsolatedAgent("Unknown");
		return agent.Deserialize(dos);
	}
}
