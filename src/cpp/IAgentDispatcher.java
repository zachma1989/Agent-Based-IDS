package interfaces;

public interface IAgentDispatcher {
	IAgent CreateSensorAgent();
	IAgent CreateClusterHeadAgent();
	boolean Dispatch(IAgent agent);
}
