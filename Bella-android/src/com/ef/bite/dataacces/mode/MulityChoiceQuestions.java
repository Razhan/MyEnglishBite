package com.ef.bite.dataacces.mode;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MulityChoiceQuestions implements Serializable {

	private Integer id;
	private String header;			
	private String audioFile;
	private Integer random;			// 1 随机 2不随机
	private Integer order;
	private Integer score;			//最大得分
	private String content;
	private long limitTime; 
	private Integer type;			// 1 -- practise 2-- rehearsal
	private Integer multi_choicetype;//1 form 2meaning 3use
	private List<MulityChoiceAnswers> answers;
	
	

	public String getHeader() {
		return header;
	}

	public void setHeader(String header) {
		this.header = header;
	}

	public Integer getMulti_choicetype() {
		return multi_choicetype;
	}

	public void setMulti_choicetype(Integer multi_choicetype) {
		this.multi_choicetype = multi_choicetype;
	}
	
	/**
	 * 答案随机产生
	 * @return
	 */
	public List<MulityChoiceAnswers> getRandomAnswers(){
		if(answers!=null &&answers.size() > 0){
			List<MulityChoiceAnswers> randomAnswers = new ArrayList<MulityChoiceAnswers>();
			List<MulityChoiceAnswers> copyAnswers = new ArrayList<MulityChoiceAnswers>();
			// copy
			for(MulityChoiceAnswers answer : answers)
				copyAnswers.add(answer);
			while(copyAnswers.size() > 0){
				if(copyAnswers.size() == 1){
					randomAnswers.add(copyAnswers.get(0));
					break;
				}else{
					Random r = new Random();
					int index_R = r.nextInt(9999) % (copyAnswers.size());
					MulityChoiceAnswers answer = copyAnswers.get(index_R);
					randomAnswers.add(answer);
					copyAnswers.remove(answer);
				}
			}
			return randomAnswers;
		}else
			return null;
	}
	

	public List<MulityChoiceAnswers> getAnswers() {
		return answers;
	}

	public void setAnswers(List<MulityChoiceAnswers> answers) {
		this.answers = answers;
	}

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

	public Integer getOrder() {
		return order;
	}

	public void setOrder(Integer order) {
		this.order = order;
	}

	public Integer getRandom() {
		return random;
	}

	public void setRandom(Integer random) {
		this.random = random;
	}

	

	public Integer getScore() {
		return score;
	}

	public void setScore(Integer scrore) {
		this.score = scrore;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public long getLimitTime() {
		return limitTime;
	}

	public void setLimitTime(long limitTime) {
		this.limitTime = limitTime;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public MulityChoiceQuestions() {
		super();
	}

}
