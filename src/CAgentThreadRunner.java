import interfaces.IAgent;
import interfaces.IAgentSender;
import interfaces.IContext;
import interfaces.ILocalStorage;

public class CAgentThreadRunner implements Runnable{
	IAgent m_agent;
	ILocalStorage m_storage;
	IContext m_context;
	IAgentSender m_sender;
	
	public CAgentThreadRunner(IAgentSender sender, IAgent agent, ILocalStorage storage, IContext context)
	{
		m_sender = sender;
		m_agent = agent;
		m_storage = storage;
		m_context = context;
	}
	
	public void run() {
		// TODO Auto-generated method stub
		m_agent.Run(m_context, m_storage);			
		if (!m_sender.TrySendAgent(m_agent))
		{
			// Agent came but was unable to be sent....
			// This should never happen...
			throw new java.lang.IllegalStateException("The Mobile Agent Manager accepted an agent, but cannot send it to its next destination.");		

		}
	}

}
