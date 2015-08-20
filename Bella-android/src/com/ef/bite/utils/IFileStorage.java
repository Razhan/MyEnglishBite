package com.ef.bite.utils;

import java.io.File;

public interface IFileStorage {

	String findFilePath(String key);
	
	File get(String key);
	
	void put(String key, File val);
	
	void delete(String key);
	
	void clear();
}
