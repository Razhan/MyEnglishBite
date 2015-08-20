package com.ef.bite.dataacces.mode;

import java.io.Serializable;
import java.util.List;

public class ChunkPresentation implements Serializable {

	private Integer id;

	private String audioFile;// 音频路径

	private Integer score;

	private Integer presentationScore;// 前三题的用户得分

	private List<PresentationConversation> presentationConversations;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getAudioFile() {
		return audioFile;
	}

	public void setAudioFile(String audioFile) {
		this.audioFile = audioFile;
	}

	public List<PresentationConversation> getPresentationConversations() {
		return presentationConversations;
	}

	public void setPresentationConversations(
			List<PresentationConversation> presentationConversations) {
		this.presentationConversations = presentationConversations;
	}

	public Integer getScore() {
		return score;
	}

	public void setScore(Integer score) {
		this.score = score;
	}

	public ChunkPresentation() {
		super();
	}

	public Integer getPresentationScore() {
		return presentationScore;
	}

	public void setPresentationScore(Integer presentationScore) {
		this.presentationScore = presentationScore;
	}

}
