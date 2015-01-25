package interfaces;

public interface IAgentTransportAbstractFactory {
	IAgentReceiver TryGetAgentReceiever();
	IAgentSender TryGetAgentSender();
}
