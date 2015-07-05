#include <string.h>

// Conversion 80% completed

class IAgent {

public:
	boolean Run(IContext context, ILocalStorage storage);
	boolean TrySetDestinationList(List<IAgentDestination> destinations);
	IAgentDestination GetNextDestination();
	

	virtual DataOutputStream Serialize() = 0;

	virtual IAgent Deserialize(DataOutputStream dos) = 0;
}
