import interfaces.IAgentReceiver;
import interfaces.IAgentSender;
import interfaces.IAgentTransportAbstractFactory;

import java.lang.OutOfMemoryError;

public class CSunspotAgentRadiostreamTransportAbstractFactory implements IAgentTransportAbstractFactory {

	public IAgentReceiver TryGetAgentReceiever() {
		try
		{
			return new CSunspotAgentRadiostreamReceiver();
		} catch (OutOfMemoryError err)
		{
			return null;
		}
	}

	public IAgentSender TryGetAgentSender() {
		try
		{
			return new CSunspotAgentRadiostreamSender();
		} catch (OutOfMemoryError err)
		{
			return null;
		}
	}

}
