import interfaces.IAgent;
import interfaces.IAgentReceiver;
import interfaces.IAgentSender;
import interfaces.IAgentTransportAbstractFactory;
import interfaces.IContext;
import interfaces.IDeviceAbstractFactory;
import interfaces.ILocalStorage;

import java.util.Hashtable;

public class MobileAgentReceiver extends CApplication {
	// Get transport handles
	protected IAgentSender m_sender = null;
	protected IAgentReceiver m_receiver = null;
	
	// Create device specific variables
	protected IContext m_context = null;
	protected ILocalStorage m_storage = null;
	
	public MobileAgentReceiver()
	{
		if (!Init())
		{
			 new java.lang.IllegalStateException("Could not initialize the Mobile Agent Receiver.");	
		}
	}
	
	public boolean Init()
	{
		System.out.println("MobileAgentReceiver Init()");

		// TODO: register the application's name with the OTA Command server & start OTA running
        // OTACommandServer.start( "MAR" );

		// Get device handles
		IDeviceAbstractFactory deviceFactory = CDeviceFactory.TryGetSunspotDeviceFactory();
		if (deviceFactory == null) return false;

		// Get transport handles
		IAgentTransportAbstractFactory transportFactory = deviceFactory.TryGetTransportAbstractFactory();
		if (transportFactory == null) return false;
		m_sender = transportFactory.TryGetAgentSender();
		m_receiver = transportFactory.TryGetAgentReceiever();
		
		// Create device specific variables
		m_context = deviceFactory.TryGetDeviceContext();
		m_storage = deviceFactory.TryGetDeviceLocalStorage();
		
		if (m_sender == null ||
			m_receiver == null ||
			m_context == null ||
			m_storage == null)
		{
			return false;
		}
		
		return true;
	}
	
	public boolean RunAgentLoop(Hashtable<String, String> parsedArgs) {
		System.out.println("MobileAgentReceiver RunAgentLoop()");

		IAgent agent = null;
		do 
		{
			System.out.println("MobileAgentReceiver loop()");
			// TryReceiveAgent should block the thread
			agent = m_receiver.TryReceiveAgent();
			
			// Unhibernate the received isolate
			// agent.unhibernate();
			
			if (agent != null)
			{
				CAgentThreadRunner agentRunner = new CAgentThreadRunner(m_sender, agent, m_storage, m_context);
				Thread thread = new Thread(agentRunner);
				thread.start();
			}
		} while (agent != null);
		
		return true;
	}
	
	public boolean Run(Hashtable<String, String> parsedArgs) {	
		System.out.println("MobileAgentReceiver Run()");
		return RunAgentLoop(parsedArgs);
	}
	
	public static void main(String[] args)
	{
		System.out.println("MobileAgentReceiver main()");
		MobileAgentReceiver mar = new MobileAgentReceiver();
		if (!mar.Run(args))
			throw new java.lang.IllegalStateException("The Mobile Agent Receiver terminated execution with a failure.");		
	}
}
