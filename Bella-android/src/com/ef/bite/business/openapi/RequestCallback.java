package com.ef.bite.business.openapi;

public abstract class RequestCallback<Result> {
	public abstract void callback(Result result);
}
