package com.ef.bite.model;

public abstract class BaseJsonModel {
	
	abstract public void parse(String json);
	
	abstract public String toJson();
}
