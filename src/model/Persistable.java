package model;

import java.util.HashMap;

public interface Persistable<T> {

	public T add(T obj);
	
	public T delete(int id);
	
	public T search(int id);
	
	public HashMap<Integer,T> getMap();
}


