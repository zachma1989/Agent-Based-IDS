import interfaces.IAgentTransportAbstractFactory;
import interfaces.IContext;
import interfaces.IDeviceAbstractFactory;
import interfaces.ILocalStorage;

public class CSunspotDeviceFactory implements IDeviceAbstractFactory{

	public IContext TryGetDeviceContext() {
		return new CSunspotDeviceContext();
	}

	public ILocalStorage TryGetDeviceLocalStorage() {
		return new CSunspotLocalStorage();
	}
	
	public IAgentTransportAbstractFactory TryGetTransportAbstractFactory() {
		return new CSunspotAgentRadiostreamTransportAbstractFactory(); 
	}
}
