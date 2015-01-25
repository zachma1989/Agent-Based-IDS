import interfaces.IDeviceAbstractFactory;

public class CDeviceFactory {
	static IDeviceAbstractFactory TryGetSunspotDeviceFactory() {
		try
		{
			return new CSunspotDeviceFactory();
		} catch (OutOfMemoryError err)
		{
			return null;
		}
	}
	
	static IDeviceAbstractFactory TryGetMockDeviceFactory() {
		// TODO:
		// Create a mock implementation here to test
		// functionality when not running on sunspots...
		return null;
	}
}
