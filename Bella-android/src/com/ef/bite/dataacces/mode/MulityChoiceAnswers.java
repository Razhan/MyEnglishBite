package com.ef.bite.dataacces.mode;


import java.io.Serializable;

public class MulityChoiceAnswers implements Serializable {

	private Integer id;

	private Integer order;
	
	private String answer;

	private String hitString;

	private Integer IsCorrect;//1为正确 2为错误

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getAnswer() {
		return answer;
	}

	public void setAnswer(String answer) {
		this.answer = answer;
	}
	
	public Integer getOrder(){
		return order;
	}
	
	public void setOrder(Integer order){
		this.order = order;
	}

	public String getHitString() {
		return hitString;
	}

	public void setHitString(String hitString) {
		this.hitString = hitString;
	}

	public Integer getIsCorrect() {
		return IsCorrect;
	}

	public void setIsCorrect(Integer isCorrect) {
		IsCorrect = isCorrect;
	}

	public MulityChoiceAnswers() {
		super();
	}

}
