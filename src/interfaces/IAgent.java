package interfaces;

import java.io.DataOutputStream;
import java.util.List;

public interface IAgent {
	boolean Run(IContext context, ILocalStorage storage);
	boolean TrySetDestinationList(List<IAgentDestination> destinations);
	IAgentDestination GetNextDestination();
	

	public DataOutputStream Serialize();

	public IAgent Deserialize(DataOutputStream dos);
}
