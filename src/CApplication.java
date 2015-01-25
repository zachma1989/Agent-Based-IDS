import java.util.Hashtable;

import interfaces.*;

public abstract class CApplication implements IApplication{

	public static boolean TryParseCommandLine(String[] args,
			Hashtable<String, String> parsedArgs) {
		
		for (int i = 0; i < args.length; i++)
		{
			if (args[i].startsWith("-") || args[i].startsWith("/"))
			{
				String key = args[i].substring(1);
				String value = null;
				if (i + 1 < args.length && 
					!args[i + 1].startsWith("-") &&
					!args[i + 1].startsWith("/"))
				{
					value = args[i + 1];
					i++;
				}
				if (!parsedArgs.containsKey(key))
					parsedArgs.put(key, value);
				else 
					parsedArgs.replace(key, value);
			}
		}
		
		return true;
	}

	public boolean Run(String[] args)
	{
		Hashtable<String, String> parsedArgs = null;
		if (TryParseCommandLine(args, parsedArgs))
		{
			return Run(parsedArgs);			
		}
		return false;
	}

}
