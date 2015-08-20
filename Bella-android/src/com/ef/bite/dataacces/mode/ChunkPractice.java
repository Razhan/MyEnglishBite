package com.ef.bite.dataacces.mode;

import java.util.List;

public class ChunkPractice {

	private String audioFile;
	private Integer score;// 用户得分
	private List<MulityChoiceAnswers> mulityChoiceAnswers;
	private String MulityChoiceQuestions;
	private Integer id;

	public Integer getId() {
		return id;
	}

	public void setId(Integer idInteger) {
		this.id = idInteger;
	}

	public String getAudioFile() {
		return audioFile;
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

	public void setMulityChoiceAnswers(
			List<MulityChoiceAnswers> mulityChoiceAnswers) {
		this.mulityChoiceAnswers = mulityChoiceAnswers;
	}

	public String getMulityChoiceQuestions() {
		return MulityChoiceQuestions;
	}

	public void setMulityChoiceQuestions(String mulityChoiceQuestions) {
		MulityChoiceQuestions = mulityChoiceQuestions;
	}

}
