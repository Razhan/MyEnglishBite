package com.ef.bite.dataacces.mode;

import java.io.Serializable;

public class HintDefinition implements Serializable {
	private Integer id;
	private String content;
	private String example;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getExample() {
		return example;
	}
	public void setExample(String example) {
		this.example = example;
	}
	
	
}
