package interfaces;

public interface ILocalStorage {
	boolean TryWriteKeyValue(String key, Object value);
	Object ReadKeyValue(String key);
}
