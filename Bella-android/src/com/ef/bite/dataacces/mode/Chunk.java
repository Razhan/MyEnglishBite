package com.ef.bite.dataacces.mode;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.ef.bite.utils.ScoreLevelHelper;
import com.ef.bite.utils.StringUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class Chunk implements Serializable {

	private String pronounce;// 发音

	private Integer id;

	private String chunkCode;

	private String coursename;

	private String targetlanguage;

	private Integer version;

	private String chunkText;

	private String explanation;

	private String language;

	private String AudioFileName;// 发音音频路径;

	private List<String> errorBalloonWords;

	private List<HintDefinition> hintDefinitions;

	private Boolean isSimulative;

	/**
	 * 0 – 未可用 1—可用未学习 2—学习未练习 3—练习完毕未实战
	 */
	private int chunkStatus;

	/**
	 * 0 – 未开始rehearsal, 1—阶段2—阶段2 3—阶段3，4—阶段4 (master)
	 */
	private int rehearsalStatus;

	private ChunkPresentation chunkPresentation;

	private List<MulityChoiceQuestions> mulityChoiceQuestions;

	private boolean isPreinstall;

	public String getCoursename() {
		return coursename;
	}

	public void setCoursename(String coursename) {
		this.coursename = coursename;
	}

	public String getTargetlanguage() {
		return targetlanguage;
	}

	public void setTargetlanguage(String targetlanguage) {
		this.targetlanguage = targetlanguage;
	}

	public String getPronounce() {
		return pronounce;
	}

	public void setPronounce(String pronounce) {
		this.pronounce = pronounce;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}

	public Integer getVersion() {
		return version;
	}

	public List<HintDefinition> getHintDefinitions() {
		return hintDefinitions;
	}

	public void setHintDefinitions(List<HintDefinition> hintDefinitions) {
		this.hintDefinitions = hintDefinitions;
	}

	public Integer getChunkStatus() {
		return chunkStatus;
	}

	public void setChunkStatus(Integer chunkStatus) {
		this.chunkStatus = chunkStatus;
	}

	public Integer getRehearsalStatus() {
		return rehearsalStatus;
	}

	public void setRehearsalStatus(Integer rehearsalStatus) {
		this.rehearsalStatus = rehearsalStatus;
	}

	public ChunkPresentation getChunkPresentation() {
		return chunkPresentation;
	}

	public void setChunkPresentation(ChunkPresentation chunkPresentation) {
		this.chunkPresentation = chunkPresentation;
	}

	public List<MulityChoiceQuestions> getMulityChoiceQuestions() {
		return mulityChoiceQuestions;
	}

	public void setMulityChoiceQuestions(
			List<MulityChoiceQuestions> mulityChoiceQuestions) {
		this.mulityChoiceQuestions = mulityChoiceQuestions;
	}

	public String getChunkCode() {
		return chunkCode;
	}

	public void setChunkCode(String chunkCode) {
		this.chunkCode = chunkCode;
	}

	public String getChunkText() {
		return chunkText;
	}

	public void setChunkText(String chunkText) {
		this.chunkText = chunkText;
	}

	public String getExplanation() {
		return explanation;
	}

	public void setExplanation(String explanation) {
		this.explanation = explanation;
	}

	public String getAudioFileName() {
		return AudioFileName;
	}

	public void setAudioFileName(String audioFileName) {
		AudioFileName = audioFileName;
	}

	public List<String> getErrorBalloonWords() {
		return errorBalloonWords;
	}

	public void setErrorBalloonWords(List<String> errorWords) {
		this.errorBalloonWords = errorWords;
	}

	public Boolean getIsPreinstall() {
		return this.isPreinstall;
	}

	public void setIsPreinstall(boolean is) {
		this.isPreinstall = is;
	}

	public Boolean getIsSimulative() {
		return isSimulative;
	}

	public void setIsSimulative(Boolean isSimulative) {
		this.isSimulative = isSimulative;
	}

	public Chunk() {
		super();
	}

	public Chunk(Integer id, String chunkCode, String chunkText,
			String explanation, String audioFileName, String language) {
		super();
		this.id = id;
		this.chunkCode = chunkCode;
		this.chunkText = chunkText;
		this.explanation = explanation;
		this.language = language;
	}

	// @Override
	// public void parse(String json) {
	// 这里是之前开发人员用来解析从数据库读出来的对象的Json的，现在重构不再使用
	// try {
	// JSONObject jsonObject = new JSONObject(json);
	// if (jsonObject.has("id"))
	// setId(Integer.valueOf(jsonObject.getString("id")));
	// if (jsonObject.has("pronounce"))
	// setPronounce(jsonObject.getString("pronounce"));
	// if (jsonObject.has("chunkCode"))
	// setChunkCode(jsonObject.getString("chunkCode"));
	// if (jsonObject.has("coursename"))
	// setCoursename(jsonObject.getString("coursename"));
	// if (jsonObject.has("version"))
	// setVersion(jsonObject.getInt("version"));
	// if (jsonObject.has("audio"))
	// setAudioFileName(jsonObject.getString("audio"));
	// if (jsonObject.has("status"))
	// setChunkStatus(Integer.parseInt(jsonObject.getString("status")));
	// if (jsonObject.has("chunkText"))
	// setChunkText(jsonObject.getString("chunkText"));
	// if (jsonObject.has("explanation"))
	// setExplanation(jsonObject.getString("explanation"));
	// if (jsonObject.has("language"))
	// setLanguage(jsonObject.getString("language"));
	// if (jsonObject.has("rehearsalStatus"))
	// setRehearsalStatus(jsonObject.getInt("rehearsalStatus"));
	// if (jsonObject.has("isPreinstall"))
	// setIsPreinstall(jsonObject.getBoolean("isPreinstall"));
	// if (jsonObject.has("isSimulative"))
	// setIsSimulative(jsonObject.getBoolean("isSimulative"));
	// List<String> errorWords = new ArrayList<String>();
	// JSONArray errorWordsJsonArray = jsonObject
	// .getJSONArray("balloon-error-words");
	// List<String> errorBalloonWords = new ArrayList<String>();
	// if (errorWords != null) {
	// for (int i = 0; i < errorWordsJsonArray.length(); i++)
	// errorBalloonWords.add(errorWordsJsonArray.getString(i));
	// }
	// setErrorBalloonWords(errorBalloonWords);
	// List<HintDefinition> hintDefinitions = new ArrayList<HintDefinition>();
	// JSONArray hintArray = jsonObject.getJSONArray("hint");
	// for (int i = 0; i < hintArray.length(); i++) {
	// JSONObject hintJsonObject = (JSONObject) hintArray.get(i);
	// HintDefinition hintDefinition = new HintDefinition();
	// hintDefinition.setContent(hintJsonObject.getString("content"));
	// hintDefinition.setExample(hintJsonObject.getString("example"));
	// hintDefinition.setId(hintJsonObject.optInt("id"));
	// hintDefinitions.add(hintDefinition);
	// }
	// setHintDefinitions(hintDefinitions);
	// JSONObject chunkPresentation = jsonObject
	// .getJSONObject("presentation");
	// ChunkPresentation presentation = new ChunkPresentation();
	// presentation.setAudioFile(chunkPresentation.getString("audio"));
	// presentation.setId(chunkPresentation.optInt("id"));
	// presentation.setScore(chunkPresentation.getInt("score"));
	// if (chunkPresentation.has("presentationScore")) {
	// presentation.setPresentationScore(chunkPresentation
	// .getInt("presentationScore"));
	// }
	//
	// List<PresentationConversation> presentationConversations = new
	// ArrayList<PresentationConversation>();
	//
	// JSONArray conversations = chunkPresentation
	// .getJSONArray("dialogue");
	// for (int i = 0; i < conversations.length(); i++) {
	// PresentationConversation presentationConversation = new
	// PresentationConversation();
	// JSONObject convJsonObject = conversations.getJSONObject(i);
	// presentationConversation.setCharacterAvater(convJsonObject
	// .getString("characterAvater"));
	// if (convJsonObject.has("id"))
	// presentationConversation.setId(convJsonObject.optInt("id"));
	// if (convJsonObject.has("content"))
	// presentationConversation.setSentence(convJsonObject
	// .getString("content"));
	// if (convJsonObject.has("startTime"))
	// presentationConversation.setStartTime(convJsonObject
	// .getInt("startTime"));
	// if (convJsonObject.has("endTime"))
	// presentationConversation.setEndTime(convJsonObject
	// .getInt("endTime"));
	// presentationConversations.add(presentationConversation);
	// }
	// presentation
	// .setPresentationConversations(presentationConversations);
	// setChunkPresentation(presentation);
	// JSONArray choicesArray = jsonObject.getJSONArray("choice");
	// List<MulityChoiceQuestions> choices = new
	// ArrayList<MulityChoiceQuestions>();
	// for (int i = 0; i < choicesArray.length(); i++) {
	// JSONObject choiceJsonObject = (JSONObject) choicesArray.get(i);
	// MulityChoiceQuestions mulityChoiceQuestion = new MulityChoiceQuestions();
	// mulityChoiceQuestion.setAudioFile(choiceJsonObject
	// .getString("audio"));
	// mulityChoiceQuestion.setContent(choiceJsonObject
	// .getString("content"));
	// mulityChoiceQuestion.setOrder(choiceJsonObject.getInt("order"));
	// mulityChoiceQuestion.setRandom(choiceJsonObject
	// .getInt("random"));
	// mulityChoiceQuestion.setType(choiceJsonObject.getInt("type"));
	// mulityChoiceQuestion.setLimitTime(choiceJsonObject
	// .getInt("limitTime"));
	// mulityChoiceQuestion.setScore(choiceJsonObject.getInt("score"));
	// mulityChoiceQuestion.setId(choiceJsonObject.optInt("id"));
	// if (choiceJsonObject.has("header"))
	// mulityChoiceQuestion.setHeader(choiceJsonObject
	// .getString("header"));
	// if (choiceJsonObject.has("multi-choice-typ"))
	// mulityChoiceQuestion.setMulti_choicetype(choiceJsonObject
	// .getInt("multi-choice-type"));
	// List<MulityChoiceAnswers> answers = new ArrayList<MulityChoiceAnswers>();
	// JSONArray answersArray = choiceJsonObject.getJSONArray("item");
	// for (int j = 0; j < answersArray.length(); j++) {
	// MulityChoiceAnswers mulityChoiceAnswers = new MulityChoiceAnswers();
	// JSONObject answerJsonObject = (JSONObject) answersArray
	// .get(j);
	// mulityChoiceAnswers.setOrder(answerJsonObject
	// .getInt("order"));
	// mulityChoiceAnswers.setAnswer(answerJsonObject
	// .getString("answer"));
	// if (answerJsonObject.has("hit"))
	// mulityChoiceAnswers.setHitString(answerJsonObject
	// .getString("hit"));
	// mulityChoiceAnswers.setId(answerJsonObject.optInt("id"));
	// mulityChoiceAnswers.setIsCorrect(answerJsonObject
	// .getInt("isCorrect"));
	// answers.add(mulityChoiceAnswers);
	// }
	// mulityChoiceQuestion.setAnswers(answers);
	// choices.add(mulityChoiceQuestion);
	// }
	// setMulityChoiceQuestions(choices);
	//
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	// Log.v("test","---chunk parse()");
	// }

	// @Override
	// public String toJson() {
	// try {
	// JSONObject jsonObject = new JSONObject();
	// if (getId() != null)
	// jsonObject.put("id", getId());
	// if (getCoursename() != null)
	// jsonObject.put("coursename", getCoursename());
	// if (getPronounce() != null)
	// jsonObject.put("pronounce", getPronounce());
	// if (getChunkCode() != null)
	// jsonObject.put("chunkCode", getChunkCode());
	// if (getVersion() != null)
	// jsonObject.put("version", getVersion());
	// if (getAudioFileName() != null)
	// jsonObject.put("audio", getAudioFileName());
	// if (getChunkStatus() != null)
	// jsonObject.put("status", getChunkStatus());
	// if (getChunkText() != null)
	// jsonObject.put("chunkText", getChunkText());
	// if (getExplanation() != null)
	// jsonObject.put("explanation", getExplanation());
	// if (getLanguage() != null)
	// jsonObject.put("language", getLanguage());
	// if (getRehearsalStatus() != null)
	// jsonObject.put("rehearsalStatus", getRehearsalStatus());
	// if (getIsPreinstall() != null)
	// jsonObject.put("isPreinstall", getIsPreinstall());
	// if (getIsSimulative() != null)
	// jsonObject.put("isSimulative", getIsSimulative());
	//
	// JSONArray errorWordsJsonArray = new JSONArray();
	// if (getErrorBalloonWords() != null
	// && getErrorBalloonWords().size() > 0) {
	// for (String word : getErrorBalloonWords())
	// errorWordsJsonArray.put(word);
	// }
	// jsonObject.put("balloon-error-words", errorWordsJsonArray);
	//
	// JSONArray hintArray = new JSONArray();
	// if (getHintDefinitions().size() > 0) {
	// for (HintDefinition hintDefinition : getHintDefinitions()) {
	// JSONObject hint = new JSONObject();
	// hint.put("content", hintDefinition.getContent());
	// hint.put("example", hintDefinition.getExample());
	// hint.put("id", hintDefinition.getId());
	// hintArray.put(hint);
	// }
	// }
	// jsonObject.put("hint", hintArray);
	//
	// JSONObject chunkPresentation = new JSONObject();
	// if (getChunkPresentation() != null) {
	// chunkPresentation
	// .put("id", this.getChunkPresentation().getId());
	// chunkPresentation.put("audio", this.getChunkPresentation()
	// .getAudioFile());
	// chunkPresentation.put("score", this.getChunkPresentation()
	// .getScore());
	// chunkPresentation.put("presentationScore", this
	// .getChunkPresentation().getPresentationScore());
	//
	// JSONArray Conversations = new JSONArray();
	// for (PresentationConversation conversation : this
	// .getChunkPresentation().getPresentationConversations()) {
	// JSONObject conJsonObject = new JSONObject();
	// conJsonObject.put("content", conversation.getSentence());
	// conJsonObject.put("content_src", conversation.getContent_src());
	// conJsonObject.put("characterAvater",
	// conversation.getCharacterAvater());
	// conJsonObject.put("id", conversation.getId());
	// conJsonObject.put("startTime", conversation.getStartTime());
	// conJsonObject.put("endTime", conversation.getEndTime());
	// Conversations.put(conJsonObject);
	// }
	// chunkPresentation.put("dialogue", Conversations);
	// jsonObject.put("presentation", chunkPresentation);
	//
	// JSONArray choicesArray = new JSONArray();
	//
	// if (getMulityChoiceQuestions() != null
	// && getMulityChoiceQuestions().size() > 0) {
	// for (MulityChoiceQuestions choiceQuestions : getMulityChoiceQuestions())
	// {
	// JSONObject choice = new JSONObject();
	// choice.put("limitTime", choiceQuestions.getLimitTime());
	// choice.put("audio", choiceQuestions.getAudioFile());
	// choice.put("content", choiceQuestions.getContent());
	// choice.put("order", choiceQuestions.getOrder());
	// choice.put("random", choiceQuestions.getRandom());
	// choice.put("type", choiceQuestions.getType());
	// choice.put("id", choiceQuestions.getId());
	// choice.put("score", choiceQuestions.getScore());
	// choice.put("header", choiceQuestions.getHeader());
	// choice.put("multi-choice-type",
	// choiceQuestions.getMulti_choicetype());
	// JSONArray answer = new JSONArray();
	//
	// for (MulityChoiceAnswers answerObject : choiceQuestions
	// .getAnswers()) {
	// JSONObject answerJsonObject = new JSONObject();
	// answerJsonObject.put("order",
	// answerObject.getOrder());
	// answerJsonObject.put("answer",
	// answerObject.getAnswer());
	// answerJsonObject.put("hit",
	// answerObject.getHitString());
	// answerJsonObject.put("id", answerObject.getId());
	// answerJsonObject.put("isCorrect",
	// answerObject.getIsCorrect());
	// answer.put(answerJsonObject);
	// }
	// choice.put("item", answer);
	// choicesArray.put(choice);
	// }
	// jsonObject.put("choice", choicesArray);
	//
	// String jsonString = jsonObject.toString();
	// return jsonString;
	// }
	// }
	// } catch (JSONException e) {
	// e.printStackTrace();
	// }
	// Log.v("test","---chunk toJson()");
	// return null;
	// }

	// 通过json 得到chunk并写入数据库
	public static Chunk parseChunk(JSONObject chunkDefinition)
			throws JSONException {
		if (chunkDefinition == null) {
			return null;
		}
		List<HintDefinition> hintDefinitions = new ArrayList<HintDefinition>();
		List<PresentationConversation> presentationConversations = new ArrayList<PresentationConversation>();
		List<MulityChoiceQuestions> mulityChoiceList = new ArrayList<MulityChoiceQuestions>();

		Chunk chunk = new Chunk();
		String filePath = chunkDefinition.getString("filePath");
		// 解析基本信息
		chunk.setChunkCode(chunkDefinition.optString("id").trim());// 课程号
		chunk.setCoursename(chunkDefinition.optString("coursename"));// 课程名
		chunk.setLanguage(chunkDefinition.optString("language").trim()
				.toLowerCase());// 母语
		chunk.setTargetlanguage(chunkDefinition.optString("targetlanguage")
				.trim().toLowerCase());// 第二语言
		chunk.setVersion(chunkDefinition.optInt("version"));// 课程版本
		chunk.setChunkStatus(0);// 课程状态 0 – 未可用 1—可用未学习 2—学习未练习 3—练习完毕未实战

		// 解析chunk
		JSONObject chunkObject = chunkDefinition.optJSONObject("chunk");
		if (chunkObject == null) {
			return null;
		}
		chunk.setChunkText(chunkObject.optString("chunk").trim());
		chunk.setExplanation(chunkObject.optString("definition").trim());

		// 解析details
		JSONArray hint = chunkObject.optJSONArray("details");
		if (hint != null) {
			for (int i = 0; i < hint.length(); i++) {
				HintDefinition hintDefinition = new HintDefinition();
				JSONObject hintJsonObject = (JSONObject) hint.get(i);
				hintDefinition.setContent(hintJsonObject.optString("title")
						.trim());
				hintDefinition.setExample(hintJsonObject.optString("content")
						.trim());
				hintDefinitions.add(hintDefinition);
			}
			chunk.setHintDefinitions(hintDefinitions);
		}

		// 解析details结束

		chunk.setAudioFileName(filePath + chunkObject.optString("audio").trim());// 音频文件名称
		chunk.setPronounce(chunkObject.optString("pronounce").trim());// 发音

		// 解析errorBalloonWords
		if (!chunkObject.isNull("balloon-error-words")) {
			JSONArray errorWords = chunkObject
					.optJSONArray("balloon-error-words");
			List<String> errorBalloonWords = new ArrayList<String>();
			if (errorWords != null) {
				for (int i = 0; i < errorWords.length(); i++) {
					errorBalloonWords.add(errorWords.optString(i));
				}
			}
			chunk.setErrorBalloonWords(errorBalloonWords);
		}
		// 解析errorBalloonWords结束

		// 解析presentation
		JSONObject presentation = chunkDefinition.optJSONObject("presentation");

		// 解析dialogue
		JSONArray dialogue = presentation.optJSONArray("dialogue");
		if (dialogue != null) {
			for (int i = 0; i < dialogue.length(); i++) {
				PresentationConversation presentationConversation = new PresentationConversation();
				JSONObject dialogueObject = (JSONObject) dialogue.get(i);
				presentationConversation.setSentence(dialogueObject.optString(
						"content").trim());//
				presentationConversation.setContent_src(dialogueObject
						.optString("content_src").trim());//
				presentationConversation.setCharacterAvater(filePath + dialogueObject
						.optString("speakericon").trim());
				presentationConversation.setStartTime(dialogueObject
						.optInt("start_time"));
				presentationConversation.setEndTime(dialogueObject
						.optInt("end_time"));
				presentationConversations.add(presentationConversation);
			}
		}

		// 解析dialogue结束

		ChunkPresentation chunkPresentation = new ChunkPresentation();
		chunkPresentation.setAudioFile(filePath
				+ presentation.optString("audio").trim());// 对话音频文件名
		chunkPresentation
				.setPresentationConversations(presentationConversations);
		chunkPresentation.setScore(presentation.optInt("score",
				ScoreLevelHelper.MAXIMUN_SCORE_CHUNK_INFO));
		chunk.setChunkPresentation(chunkPresentation);
		// 解析presentation结束

		// 解析multi-choice
		JSONArray multi_choice = chunkDefinition.optJSONArray("multi-choice");
		if (multi_choice != null) {
			for (int i = 0; i < multi_choice.length(); i++) {
				JSONObject optionsObject = (JSONObject) multi_choice.get(i);
				MulityChoiceQuestions mulityChoiceQuestions = new MulityChoiceQuestions();
				mulityChoiceQuestions.setAudioFile(StringUtils
						.isBlank(optionsObject.optString("audio")) ? ""
						: filePath + optionsObject.optString("audio").trim());
				mulityChoiceQuestions.setLimitTime(optionsObject
						.optLong("limit-time"));
				mulityChoiceQuestions.setOrder(i);
				mulityChoiceQuestions.setHeader(optionsObject.optString(
						"header").trim());// 多选题标题

				if ("form".equalsIgnoreCase(optionsObject.optString(
						"multi-choice-type").trim())) {// 多选题类型
					mulityChoiceQuestions.setMulti_choicetype(1);
				} else if ("meaning".equalsIgnoreCase(optionsObject.optString(
						"multi-choice-type").trim())) {
					mulityChoiceQuestions.setMulti_choicetype(2);
				} else if ("use".equalsIgnoreCase(optionsObject.optString(
						"multi-choice-type").trim())) {
					mulityChoiceQuestions.setMulti_choicetype(3);
				}
				mulityChoiceQuestions.setContent(optionsObject.optString(
						"question").trim());// 题目
				mulityChoiceQuestions.setOrder(i + 1);
				if (i >= 0 && i <= 2) {
					mulityChoiceQuestions.setType(1);
				} else {
					mulityChoiceQuestions.setType(2);
				}
				mulityChoiceQuestions.setRandom("Y"
						.equalsIgnoreCase(optionsObject.optString("random")
								.trim()) ? 1 : 0);// 表示随机
				mulityChoiceQuestions.setScore(optionsObject.optInt("score"));// 此题分数

				// 解析items
				JSONArray itemsArray = optionsObject.optJSONArray("items");
				List<MulityChoiceAnswers> mulityChoiceAnswers = new ArrayList<MulityChoiceAnswers>();
				if (itemsArray != null) {
					for (int j = 0; j < itemsArray.length(); j++) {
						MulityChoiceAnswers mulityChoiceAnswer = new MulityChoiceAnswers();
						JSONObject item = (JSONObject) itemsArray.get(j);
						mulityChoiceAnswer.setOrder(j);
						mulityChoiceAnswer.setAnswer(item.optString("item")
								.trim());
						mulityChoiceAnswer.setIsCorrect(("Y")
								.equalsIgnoreCase(item.getString("iscorrect")
										.trim()) ? 1 : 0);
						mulityChoiceAnswer.setHitString(item.optString(
								"errorhint").trim());
						mulityChoiceAnswers.add(mulityChoiceAnswer);
					}
					mulityChoiceQuestions.setAnswers(mulityChoiceAnswers);
				}

				// 解析items结束
				mulityChoiceList.add(mulityChoiceQuestions);
			}
		}
		chunk.setMulityChoiceQuestions(mulityChoiceList);
		// 解析multi-choice结束
		// 解析chunk结束
		return chunk;
	}

	// // 通过json 得到chunk并写入数据库
	// public static Chunk parseChunk(JSONObject chunkDefinition)
	// throws JSONException {
	// if (chunkDefinition == null) {
	// return null;
	// }
	// List<HintDefinition> hintDefinitions = new ArrayList<HintDefinition>();
	// List<PresentationConversation> presentationConversations = new
	// ArrayList<PresentationConversation>();
	// List<MulityChoiceQuestions> mulityChoiceList = new
	// ArrayList<MulityChoiceQuestions>();
	//
	// Chunk chunk = new Chunk();
	// String fileName = chunkDefinition.getString("fileName");
	// if (chunkDefinition.has("id"))
	// chunk.setChunkCode(chunkDefinition.getString("id").trim());
	// if (chunkDefinition.has("language"))
	// chunk.setLanguage(chunkDefinition.getString("language").trim()
	// .toLowerCase());
	// if (chunkDefinition.has("version"))
	// chunk.setVersion(chunkDefinition.getInt("version"));
	// chunk.setChunkStatus(0);
	//
	// JSONObject chunkObject = chunkDefinition.getJSONObject("chunk");
	// chunk.setChunkText(chunkObject.getString("chunk").trim());
	// chunk.setExplanation(chunkObject.getString("definition").trim());
	// JSONArray hint = chunkObject.getJSONArray("details");
	//
	// for (int i = 0; i < hint.length(); i++) {
	// HintDefinition hintDefinition = new HintDefinition();
	// JSONObject hintJsonObject = (JSONObject) hint.get(i);
	// hintDefinition.setContent(hintJsonObject.getString("title").trim());
	// hintDefinition.setExample(hintJsonObject.getString("content")
	// .trim());
	// hintDefinitions.add(hintDefinition);
	// }
	// chunk.setHintDefinitions(hintDefinitions);
	// if (chunkObject.getString("audio") != null
	// && !chunkObject.getString("audio").trim().isEmpty())
	// chunk.setAudioFileName(fileName
	// + chunkObject.getString("audio").trim());
	// else
	// chunk.setAudioFileName("");
	// chunk.setPronounce(chunkObject.getString("pronounce").trim());
	// if (!chunkObject.isNull("balloon-error-words")) {
	// JSONArray errorWords = chunkObject
	// .getJSONArray("balloon-error-words");
	// List<String> errorBalloonWords = new ArrayList<String>();
	// if (errorWords != null) {
	// for (int i = 0; i < errorWords.length(); i++) {
	// errorBalloonWords.add(errorWords.getString(i));
	// }
	// }
	// chunk.setErrorBalloonWords(errorBalloonWords);
	// }
	// JSONObject presentation = chunkDefinition.getJSONObject("presentation");
	// JSONArray dialogue = presentation.getJSONArray("dialogue");
	// for (int i = 0; i < dialogue.length(); i++) {
	// PresentationConversation presentationConversation = new
	// PresentationConversation();
	// JSONObject dialogueoJsonObject = (JSONObject) dialogue.get(i);
	// if (dialogueoJsonObject.has("content"))
	// presentationConversation.setSentence(dialogueoJsonObject
	// .getString("content"));
	// if (dialogueoJsonObject.has("speakericon"))
	// presentationConversation.setCharacterAvater(dialogueoJsonObject
	// .getString("speakericon").trim());
	// if (dialogueoJsonObject.has("startTime"))
	// presentationConversation.setStartTime(dialogueoJsonObject
	// .getInt("startTime"));
	// if (dialogueoJsonObject.has("endTime"))
	// presentationConversation.setEndTime(dialogueoJsonObject
	// .getInt("endTime"));
	// presentationConversations.add(presentationConversation);
	// }
	//
	// ChunkPresentation chunkPresentation = new ChunkPresentation();
	// if (presentation.getString("audio") != null
	// && !presentation.getString("audio").trim().isEmpty())
	// chunkPresentation.setAudioFile(fileName
	// + presentation.getString("audio").trim());
	// else
	// chunkPresentation.setAudioFile("");
	// chunkPresentation
	// .setPresentationConversations(presentationConversations);
	// // 设置Presentation Score
	// chunkPresentation.setScore(ScoreLevelHelper.MAXIMUN_SCORE_CHUNK_INFO);
	// chunk.setChunkPresentation(chunkPresentation);
	// JSONArray multi_choice = chunkDefinition.getJSONArray("multi-choice");
	//
	// for (int i = 0; i < multi_choice.length(); i++) {
	// JSONObject iJsonObject = (JSONObject) multi_choice.get(i);
	// MulityChoiceQuestions mulityChoiceQuestions = new
	// MulityChoiceQuestions();
	// if (iJsonObject.getString("audio") != null
	// && !iJsonObject.getString("audio").trim().isEmpty())
	// mulityChoiceQuestions.setAudioFile(fileName
	// + iJsonObject.getString("audio").trim());
	// else
	// mulityChoiceQuestions.setAudioFile("");
	// mulityChoiceQuestions.setOrder(i);
	// if (iJsonObject.has("limit-time"))
	// mulityChoiceQuestions.setLimitTime(Long.parseLong(iJsonObject
	// .getString("limit-time").trim()));
	// mulityChoiceQuestions.setOrder(i);
	// if (iJsonObject.has("header")) {
	// mulityChoiceQuestions.setHeader(iJsonObject.getString("header")
	// .trim());
	// }
	// if (iJsonObject.has("multi-choice-type")) {
	// if ("form".equalsIgnoreCase(iJsonObject.getString(
	// "multi-choice-type").trim())) {
	// mulityChoiceQuestions.setMulti_choicetype(1);
	// } else if ("meaning".equalsIgnoreCase(iJsonObject.getString(
	// "multi-choice-type").trim())) {
	// mulityChoiceQuestions.setMulti_choicetype(2);
	// } else if ("use".equalsIgnoreCase(iJsonObject.getString(
	// "multi-choice-type").trim())) {
	// mulityChoiceQuestions.setMulti_choicetype(3);
	// }
	// }
	// mulityChoiceQuestions.setContent(iJsonObject.getString("question")
	// .trim());
	// mulityChoiceQuestions.setOrder(i + 1);
	// if (i >= 0 && i <= 2) {
	// mulityChoiceQuestions.setType(1);
	// } else {
	// mulityChoiceQuestions.setType(2);
	// }
	// if ("Y".equalsIgnoreCase(iJsonObject.getString("random").trim())) {
	// mulityChoiceQuestions.setRandom(1);// 表示随机
	// } else {
	// mulityChoiceQuestions.setRandom(0);
	// }
	// // 设置Multi-Choice的Max Score
	// switch (i) {
	// case 0:
	// mulityChoiceQuestions
	// .setScore(ScoreLevelHelper.MAXIMUN_SCORE_PRACTICE_FORM);
	// break;
	// case 1:
	// mulityChoiceQuestions
	// .setScore(ScoreLevelHelper.MAXIMUN_SCORE_PRACTICE_MEAN);
	// break;
	// case 2:
	// mulityChoiceQuestions
	// .setScore(ScoreLevelHelper.MAXIMUN_SCORE_PRACTICE_USE);
	// break;
	// case 3:
	// mulityChoiceQuestions
	// .setScore(ScoreLevelHelper.MAXIMUN_SCORE_REHEARSE_FORM);
	// break;
	// case 4:
	// mulityChoiceQuestions
	// .setScore(ScoreLevelHelper.MAXIMUN_SCORE_REHEARSE_MEAN);
	// break;
	// case 5:
	// mulityChoiceQuestions
	// .setScore(ScoreLevelHelper.MAXIMUN_SCORE_REHEARSE_USE);
	// break;
	// default:
	// mulityChoiceQuestions
	// .setScore(ScoreLevelHelper.MAXIMUN_SCORE_PRACTICE_FORM);
	// break;
	// }
	//
	// JSONArray itemsArray = iJsonObject.getJSONArray("items");
	// List<MulityChoiceAnswers> mulityChoiceAnswers = new
	// ArrayList<MulityChoiceAnswers>();
	//
	// for (int j = 0; j < itemsArray.length(); j++) {
	// MulityChoiceAnswers mulityChoiceAnswer = new MulityChoiceAnswers();
	// JSONObject item = (JSONObject) itemsArray.get(j);
	// mulityChoiceAnswer.setOrder(j);
	// mulityChoiceAnswer.setAnswer(item.getString("item").trim());
	// if (item.has("iscorrect")) {
	// if (("Y").equalsIgnoreCase(item.getString("iscorrect")
	// .trim())) {
	// mulityChoiceAnswer.setIsCorrect(1);
	// } else {
	// mulityChoiceAnswer.setIsCorrect(0);
	// }
	// }
	//
	// if (item.has("errorhint")) {
	// mulityChoiceAnswer.setHitString(item.getString("errorhint")
	// .trim());
	// }
	// mulityChoiceAnswers.add(mulityChoiceAnswer);
	// }
	// mulityChoiceQuestions.setAnswers(mulityChoiceAnswers);
	// mulityChoiceList.add(mulityChoiceQuestions);
	// }
	// chunk.setMulityChoiceQuestions(mulityChoiceList);
	// return chunk;
	// }
}
