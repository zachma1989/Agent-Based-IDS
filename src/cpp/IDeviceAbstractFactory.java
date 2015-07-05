package interfaces;

public interface IDeviceAbstractFactory {
	IContext TryGetDeviceContext();
	ILocalStorage TryGetDeviceLocalStorage();
	IAgentTransportAbstractFactory TryGetTransportAbstractFactory();
}
