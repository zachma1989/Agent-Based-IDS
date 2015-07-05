package interfaces;

import java.util.Hashtable;

public interface IApplication {
	boolean Run(String[] args);
	boolean Run(Hashtable<String, String> parsedArgs);
}
