#include <string.h>
#include <stdlib.h>

// Conversion 80% completed

class IAgent {

public:
	boolean Run(IContext context, ILocalStorage storage);
	boolean TrySetDestinationList(vector<IAgentDestination> destinations);
	IAgentDestination GetNextDestination();
	

	/*
	 * Questions: Are we gonna use socket to send/receive data?
	*/
	virtual DataOutputStream Serialize() = 0;

	virtual IAgent Deserialize(DataOutputStream dos) = 0;

}
