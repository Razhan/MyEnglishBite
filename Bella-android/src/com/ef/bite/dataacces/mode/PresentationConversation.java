package com.ef.bite.dataacces.mode;

import java.io.Serializable;

public class PresentationConversation implements Serializable {

	private Integer id;
	private String Sentence;
	private String Content_src;
	private String CharacterAvater;
	private Integer startTime;
	private Integer endTime;

	public PresentationConversation() {
		super();
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getSentence() {
		return Sentence;
	}

	public void setSentence(String sentence) {
		Sentence = sentence;
	}

	public String getCharacterAvater() {
		return CharacterAvater;
	}

	public void setCharacterAvater(String characterAvater) {
		CharacterAvater = characterAvater;
	}

	public void setStartTime(int start) {
		startTime = start;
	}

	public Integer getStartTime() {
		return startTime;
	}

	public void setEndTime(Integer end) {
		endTime = end;
	}

	public Integer getEndTime() {
		return endTime;
	}

	public void setContent_src(String content_src) {
		this.Content_src = content_src;
	}

	public String getContent_src() {
		return Content_src;
	}

}
