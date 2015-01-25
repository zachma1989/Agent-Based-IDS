import java.util.List;
import java.util.Random;

import interfaces.IAgent;
import interfaces.IAgentDestination;

public abstract class CAgentBase implements IAgent{
	List<IAgentDestination> m_destinations = null;
	
	public IAgentDestination GetNextDestination() {
		Random random = new Random();
		int randomDest = random.nextInt(m_destinations.size());
		return m_destinations.get(randomDest);
	}

	public boolean TrySetDestinationList(List<IAgentDestination> destinations) {
		m_destinations = destinations;
		return false;
	}

}
