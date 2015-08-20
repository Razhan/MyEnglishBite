package com.ef.bite.dataacces.mode;

import java.util.List;

public class ChunkRehearsal {
	private Integer id;
	private String audioFile;
	private Integer score;
	private List<MulityChoiceAnswers> mulityChoiceAnswers;
	private String  mulityChoiceQuestions;
	public String getAudioFile() {
		return audioFile;
	}
	
	
	public Integer getId() {
		return id;
	}


	public void setId(Integer id) {
		this.id = id;
	}


	public void setAudioFile(String audioFile) {
		this.audioFile = audioFile;
	}
	public Integer getScore() {
		return score;
	}
	public void setScore(Integer score) {
		this.score = score;
	}
	public List<MulityChoiceAnswers> getMulityChoiceAnswers() {
		return mulityChoiceAnswers;
	}
	public void setMulityChoiceAnswers(List<MulityChoiceAnswers> mulityChoiceAnswers) {
		this.mulityChoiceAnswers = mulityChoiceAnswers;
	}
	public String getMulityChoiceQuestions() {
		return mulityChoiceQuestions;
	}
	public void setMulityChoiceQuestions(String mulityChoiceQuestions) {
		this.mulityChoiceQuestions = mulityChoiceQuestions;
	}
	
	
	
}
