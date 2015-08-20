package com.ef.bite.dataacces.dao;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.ef.bite.dataacces.ChunksHolder;
import com.ef.bite.dataacces.mode.Chunk;
import com.ef.bite.dataacces.mode.MyDBHelper;
import com.ef.bite.dataacces.mode.UserProgressStatus;
import com.ef.bite.utils.StringUtils;

import java.util.*;

public class ChunkDao {
    private SQLiteDatabase database;
    private MyDBHelper dbHelper;
    private ChunksHolder chunksHolder = ChunksHolder.getInstance();

    public ChunkDao(Context context) {
        dbHelper = MyDBHelper.Instance(context);
        database = dbHelper.getWritableDatabase();
    }

//	// 通过chunkCode找到QuestionsId
//	public String getQuestionsId(String chunkCode, Integer orderNum) {
//		Cursor cursor = database
//				.rawQuery(
//						"select id from MulityChoiceQuestions where chunkid=(select id from ChunkDefinition where chunkCode=? limit 1) and orderNum=?",
//						new String[] { chunkCode, orderNum.toString() });
//		if (cursor != null) {
//			if (cursor.moveToFirst()) {
//				return cursor.getString(0);
//			}
//			cursor.close();
//		}
//		return null;
//	}

    /**
     * 查询所有的chunk数
     *
     * @return
     */
    public int getAllChunkCount() {

        Map<String, Chunk> chunkMap = new HashMap<String, Chunk>();
        List<Chunk> chunks = chunksHolder.getChunks();
        if (chunks == null || chunks.size() == 0) {
            return 0;
        }
        for (Iterator<Chunk> iterator = chunks.iterator(); iterator.hasNext(); ) {
            Chunk chunk = iterator.next();
            chunkMap.put(chunk.getChunkCode(), chunk);
        }
        return chunkMap.size();

//		Cursor cursor = null;
//		try {
//			cursor = database.rawQuery(
//					"select distinct chunkCode from ChunkDefinition", null);
//			if (cursor != null)
//				return cursor.getCount();
//			return 0;
//		} catch (Exception ex) {
//			ex.printStackTrace();
//			return 0;
//		} finally {
//			if (cursor != null)
//				cursor.close();
//		}
    }

/*	// 通过QuestionsId得到chunkCode
    public String getchunkCode(Integer QuestionsId) {
		Cursor cursor = database
				.rawQuery(
						"select chunkCode from ChunkDefinition where id=(select chunkid from MulityChoiceQuestions where id=?)",
						new String[] { QuestionsId.toString() });
		if (cursor != null) {
			if (cursor.moveToFirst()) {
				return cursor.getString(0);
			}
			cursor.close();
		}
		return null;
	}*/

/*	// 通过QuestionsId得到orderNum
    public Integer getorderNum(Integer QuestionsId) {
		Cursor cursor = database.rawQuery(
				"select orderNum from MulityChoiceQuestions where id=?",
				new String[] { QuestionsId.toString() });
		if (cursor != null) {
			if (cursor.moveToFirst()) {
				return cursor.getInt(0);
			}
			cursor.close();
		}
		return null;
	}*/

    public boolean isChunkExit(String chunkCode) {
        List<Chunk> chunks = chunksHolder.getChunks();
        if (chunks == null || chunks.size() == 0) {
            return false;
        }
        for (Iterator<Chunk> iterator = chunks.iterator(); iterator.hasNext(); ) {
            Chunk chunk = iterator.next();
            if (StringUtils.isEquals(chunk.getChunkCode(), chunkCode)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 通过通过chunkcode获得chunk对象
     *
     * @param chunkCode
     * @param uid
     * @param language
     * @return
     */
    public Chunk getChunkByCode(String chunkCode, String uid, String language) {

        List<Chunk> chunks = chunksHolder.getChunks();
        if (chunks == null || chunks.size() == 0) {
            return null;
        }

//        if (language == null) {

            for (Iterator<Chunk> iterator = chunks.iterator(); iterator.hasNext(); ) {
                Chunk chunk = iterator.next();
                if (StringUtils.isEquals(chunk.getChunkCode(), chunkCode)) {
                    getChunkStatus(chunk, uid);
                    return chunk;
                }
            }
//        } else {
//
//            for (Iterator<Chunk> iterator = chunks.iterator(); iterator.hasNext(); ) {
//                Chunk chunk = iterator.next();
//                if (StringUtils.isEquals(chunk.getChunkCode(), chunkCode) && StringUtils.isEquals(chunk.getLanguage(), language)) {
//                    getChunkStatus(chunk, uid);
//                    return chunk;
//                }
//            }
//        }
        return null;
//		Chunk chunk = null;
//		Cursor cursor = database
//				.rawQuery(
//						"select * from chunkdefinition where chunkCode=? and language=?",
//						new String[] { chunkCode, language });
//		if (cursor != null && cursor.moveToFirst()) {
//			int chunkId = cursor.getInt(cursor.getColumnIndex("id"));
//			cursor.close();
//			chunk = getChunk(chunkId, uid);
//		}
//		return chunk;
    }

    /**
     * 为该chunk查询并添加用户进度
     *
     * @param chunk
     * @param uid
     */
    private void getChunkStatus(Chunk chunk, String uid) {
        if (uid != null) {
            // Query User Progres Status
            Cursor cursor = database
                    .rawQuery(
                            "select * from UserProgressStatus where chunkCode=? and uid=?",
                            new String[]{chunk.getChunkCode(), uid.toString()});
            if (cursor != null && cursor.moveToFirst()) {
                chunk.setChunkStatus(cursor.getInt(cursor
                        .getColumnIndex("chunkStatus")));
                chunk.setRehearsalStatus(cursor.getInt(cursor
                        .getColumnIndex("rehearseStatus")));
                chunk.getChunkPresentation().setPresentationScore(cursor.getInt(cursor
                        .getColumnIndex("presentationScore")));
            }
            // colose cursor
            if (cursor != null) {
                cursor.close();
                cursor = null;
            }
        }
    }

//	// 根据 chunkid 和 uid 得到 chunk(已经测试)
//	public Chunk getChunk(Integer chunkId, String uid) {
//		Chunk chunk = new Chunk();
//		// 提示信息
//		List<HintDefinition> hintDefinitions = new ArrayList<HintDefinition>();
//		// 对话
//		ChunkPresentation chunkPresentation = new ChunkPresentation();
//		// 会话介绍
//		List<PresentationConversation> presentationConversations = new ArrayList<PresentationConversation>();
//
//		// 问题
//		List<MulityChoiceQuestions> mulityChoiceQuestions = new ArrayList<MulityChoiceQuestions>();
//
//		// 通过chunkid 查询 chunkdefinition 表
//		Cursor cursor = database.rawQuery(
//				"select * from chunkdefinition where id=?",
//				new String[] { chunkId.toString() });
//		if (cursor == null || !cursor.moveToFirst())
//			return null;
//		chunk.setId(chunkId);
//		chunk.setVersion(cursor.getInt(cursor.getColumnIndex("version")));
//		chunk.setChunkText(cursor.getString(cursor.getColumnIndex("chunkText")));
//		chunk.setAudioFileName(cursor.getString(cursor
//				.getColumnIndex("audioFileName")));
//		chunk.setLanguage(cursor.getString(cursor.getColumnIndex("language")));
//		chunk.setExplanation(cursor.getString(cursor
//				.getColumnIndex("explanation")));
//		chunk.setChunkCode(cursor.getString(cursor.getColumnIndex("chunkCode")));
//		chunk.setPronounce(cursor.getString(cursor.getColumnIndex("pronounce")));
//		chunk.setIsPreinstall(cursor.getInt(cursor
//				.getColumnIndex("isPreinstall")) > 0);
//		String errorWords = cursor.getString(cursor
//				.getColumnIndex("errorWords"));
//		if (cursor != null) {
//			cursor.close();
//			cursor = null;
//		}
//		List<String> wordsList = new ArrayList<String>();
//		if (errorWords != null && !errorWords.isEmpty()) {
//			String[] wrodsArray = errorWords.split(";");
//			for (int index = 0; index < wrodsArray.length; index++) {
//				if (wrodsArray[index] != null && !wrodsArray[index].isEmpty())
//					wordsList.add(wrodsArray[index]);
//			}
//		}
//		chunk.setErrorBalloonWords(wordsList);
//
//		if (uid != null) {
//			// Query User Progres Status
//			cursor = database
//					.rawQuery(
//							"select * from UserProgressStatus where chunkCode=? and uid=?",
//							new String[] { chunk.getChunkCode(), uid.toString() });
//			if (cursor != null && cursor.moveToFirst()) {
//				chunk.setChunkStatus(cursor.getInt(cursor
//						.getColumnIndex("chunkStatus")));
//				chunk.setRehearsalStatus(cursor.getInt(cursor
//						.getColumnIndex("rehearseStatus")));
//				chunkPresentation.setPresentationScore(cursor.getInt(cursor
//						.getColumnIndex("presentationScore")));
//			}
//			// colose cursor
//			if (cursor != null) {
//				cursor.close();
//				cursor = null;
//			}
//		}
//		// Query HintDefinition
//		cursor = database.rawQuery(
//				"select * from HintDefinition where chunkid=?",
//				new String[] { chunkId.toString() });
//		if (cursor != null) {
//			while (cursor.moveToNext()) {
//				HintDefinition hintDefinition = new HintDefinition();
//				hintDefinition.setContent(cursor.getString(cursor
//						.getColumnIndex("content")));
//				hintDefinition.setExample(cursor.getString(cursor
//						.getColumnIndex("example")));
//				hintDefinition
//						.setId(cursor.getInt(cursor.getColumnIndex("id")));
//				hintDefinitions.add(hintDefinition);
//			}
//		}
//		// colose cursor
//		if (cursor != null) {
//			cursor.close();
//			cursor = null;
//		}
//		chunk.setHintDefinitions(hintDefinitions);
//		// Query ChunkPresentation
//		cursor = database.rawQuery(
//				"select * from ChunkPresentation where chunkid=?",
//				new String[] { chunkId.toString() });
//		if (cursor != null && cursor.moveToFirst()) {
//			chunkPresentation.setAudioFile(cursor.getString(cursor
//					.getColumnIndex("audioFile")));
//			chunkPresentation.setId(cursor.getInt(cursor.getColumnIndex("id")));
//			chunkPresentation.setScore(cursor.getInt(cursor
//					.getColumnIndex("score")));
//			// close cursor
//			if (cursor != null) {
//				cursor.close();
//				cursor = null;
//			}
//			Cursor cursor2 = database
//					.rawQuery(
//							"select * from PresentationConversation where presentationID=?",
//							new String[] { chunkPresentation.getId().toString() });
//			if (cursor2 != null) {
//				while (cursor2.moveToNext()) {
//					PresentationConversation presentationConversation = new PresentationConversation();
//					presentationConversation.setCharacterAvater(cursor2
//							.getString(cursor2
//									.getColumnIndex("characterAvater")));
//					presentationConversation.setSentence(cursor2
//							.getString(cursor2.getColumnIndex("sentence")));
//					presentationConversation.setId(cursor2.getInt(cursor2
//							.getColumnIndex("id")));
//					presentationConversation.setStartTime(cursor2
//							.getInt(cursor2.getColumnIndex("startTime")));
//					presentationConversation.setEndTime(cursor2.getInt(cursor2
//							.getColumnIndex("endTime")));
//					presentationConversations.add(presentationConversation);
//				}
//				// close cursor
//				if (cursor2 != null) {
//					cursor2.close();
//					cursor2 = null;
//				}
//			}
//			chunkPresentation
//					.setPresentationConversations(presentationConversations);
//			chunk.setChunkPresentation(chunkPresentation);
//		}
//		// Query MulityChoiceQuestions
//		cursor = database.rawQuery(
//				"select * from MulityChoiceQuestions where chunkId=?",
//				new String[] { chunkId.toString() });
//		if (cursor != null) {
//			while (cursor.moveToNext()) {
//				MulityChoiceQuestions mulityChoiceQuestion = new MulityChoiceQuestions();
//				mulityChoiceQuestion.setAudioFile(cursor.getString(cursor
//						.getColumnIndex("audioFile")));
//				mulityChoiceQuestion.setContent(cursor.getString(cursor
//						.getColumnIndex("content")));
//				mulityChoiceQuestion.setId(cursor.getInt(cursor
//						.getColumnIndex("id")));
//				mulityChoiceQuestion.setLimitTime(cursor.getLong(cursor
//						.getColumnIndex("limitTime")));
//				mulityChoiceQuestion.setScore(cursor.getInt(cursor
//						.getColumnIndex("score")));
//				mulityChoiceQuestion.setType(cursor.getInt(cursor
//						.getColumnIndex("type")));
//				mulityChoiceQuestion.setOrder(cursor.getInt(cursor
//						.getColumnIndex("orderNum")));
//				mulityChoiceQuestion.setRandom(cursor.getInt(cursor
//						.getColumnIndex("random")));
//				mulityChoiceQuestion.setHeader(cursor.getString(cursor
//						.getColumnIndex("header")));
//				mulityChoiceQuestion.setMulti_choicetype(cursor.getInt(cursor
//						.getColumnIndex("multiType")));
//				List<MulityChoiceAnswers> mulityChoiceAnswers = new ArrayList<MulityChoiceAnswers>();
//				Cursor cursor2 = database.rawQuery(
//						"select * from MulityChoiceAnswers where questionID="
//								+ mulityChoiceQuestion.getId(), null);
//				if (cursor2 != null) {
//					while (cursor2.moveToNext()) {
//						MulityChoiceAnswers mulityChoiceAnswer = new MulityChoiceAnswers();
//						mulityChoiceAnswer.setOrder(cursor2.getInt(cursor2
//								.getColumnIndex("orderNum")));
//						mulityChoiceAnswer.setAnswer(cursor2.getString(cursor2
//								.getColumnIndex("answer")));
//						mulityChoiceAnswer.setHitString(cursor2
//								.getString(cursor2.getColumnIndex("hit")));
//						mulityChoiceAnswer.setIsCorrect(cursor2.getInt(cursor2
//								.getColumnIndex("isCorrect")));
//						mulityChoiceAnswer.setId(cursor2.getInt(cursor2
//								.getColumnIndex("id")));
//						mulityChoiceAnswers.add(mulityChoiceAnswer);
//					}
//					// close cursor
//					if (cursor2 != null) {
//						cursor2.close();
//						cursor2 = null;
//					}
//				}
//				mulityChoiceQuestion.setAnswers(mulityChoiceAnswers);
//
//				mulityChoiceQuestions.add(mulityChoiceQuestion);
//			}
//		}
//		chunk.setMulityChoiceQuestions(mulityChoiceQuestions);
//		// close cursor
//		if (cursor != null) {
//			cursor.close();
//			cursor = null;
//		}
//		return chunk;
//	}

//	/**
//	 * 将chunk插入数据库内
//	 *
//	 * @param chunk
//	 */
//	public void insertChunkIntoDB(Chunk chunk) {
//		try {
//			String errorWords = "";
//			List<String> errorWordsList = chunk.getErrorBalloonWords();
//			if (errorWordsList != null && errorWordsList.size() > 0) {
//				for (int index = 0; index < errorWordsList.size(); index++)
//					errorWords += errorWordsList.get(index) + ";";
//			}
//			int id = insertChunkDefinitionReturnId(chunk.getChunkCode(),
//					chunk.getChunkText(), chunk.getExplanation(),
//					chunk.getAudioFileName(), chunk.getLanguage(),
//					chunk.getPronounce(), errorWords, chunk.getVersion(),
//					chunk.getIsPreinstall());
//			Cursor cursor = null;
//			if (id <= 0)
//				return;
//			// 如果插入chunkdefinition成功
//			database.execSQL("insert into ChunkPresentation values(?,?,?,?); ",
//					new String[] { null,
//							chunk.getChunkPresentation().getAudioFile(),
//							chunk.getChunkPresentation().getScore().toString(),
//							String.valueOf(id) });
//			// Chunk Presentation
//			cursor = database.rawQuery(
//					"select id from ChunkPresentation where chunkId=?",
//					new String[] { String.valueOf(id) });
//			Integer PresentationID = null;
//			if (cursor != null) {
//				if (cursor.moveToFirst()) {
//					PresentationID = cursor.getInt(cursor.getColumnIndex("id"));
//				}
//				cursor.close();
//			}
//			// Hit Definition
//			for (HintDefinition hintDefinition : chunk.getHintDefinitions()) {
//				database.execSQL(
//						"insert into HintDefinition values(?,?,?,?); ",
//						new String[] { null, String.valueOf(id),
//								hintDefinition.getContent(),
//								hintDefinition.getExample() });
//			}
//			// Conversations
//			for (PresentationConversation presentationConversation : chunk
//					.getChunkPresentation().getPresentationConversations()) {
//				database.execSQL(
//						"insert into PresentationConversation values(?,?,?,?,?,?); ",
//						new String[] {
//								null,
//								PresentationID.toString(),
//								presentationConversation.getSentence(),
//								presentationConversation.getCharacterAvater(),
//								String.valueOf(presentationConversation
//										.getStartTime()),
//								String.valueOf(presentationConversation
//										.getEndTime()), });
//			}
//			// Multi Choice
//			for (MulityChoiceQuestions choice : chunk
//					.getMulityChoiceQuestions()) {
//				database.execSQL(
//						"insert into MulityChoiceQuestions values(?,?,?,?,?,?,?,?,?,?,?); ",
//						new String[] { null, String.valueOf(id),
//								choice.getAudioFile(),
//								choice.getScore().toString(),
//								choice.getContent(),
//								String.valueOf(choice.getLimitTime()),
//								String.valueOf(choice.getType()),
//								String.valueOf(choice.getOrder()),
//								String.valueOf(choice.getRandom()),
//								choice.getHeader(),
//								String.valueOf(choice.getMulti_choicetype()) });
//
//			}
//
//			List<Integer> choiceIdsIntegers = new ArrayList<Integer>();
//			cursor = database.rawQuery(
//					"select id from MulityChoiceQuestions where chunkId=?",
//					new String[] { String.valueOf(id) });
//			if (cursor != null) {
//				while (cursor.moveToNext()) {
//					choiceIdsIntegers.add(cursor.getInt(cursor
//							.getColumnIndex("id")));
//				}
//				cursor.close();
//			}
//			for (int i = 0; i < chunk.getMulityChoiceQuestions().size(); i++) {
//				List<MulityChoiceAnswers> answers = chunk
//						.getMulityChoiceQuestions().get(i).getAnswers();
//				for (MulityChoiceAnswers answer : answers) {
//					database.execSQL(
//							"insert into MulityChoiceAnswers values(?,?,?,?,?,?); ",
//							new String[] { null,
//									choiceIdsIntegers.get(i).toString(),
//									answer.getOrder().toString(),
//									answer.getAnswer(), answer.getHitString(),
//									answer.getIsCorrect().toString() });
//				}
//			}
//		} catch (Exception ex) {
//			ex.printStackTrace();
//		}
//	}

//	/** 数据库插入Chunk Definition记录 **/
//	private int insertChunkDefinitionReturnId(String code, String chunkText,
//			String explanation, String audioFile, String language,
//			String pronounce, String errorWords, Integer version,
//			Boolean isPreinstall) {
//		int id = -1;
//		Cursor cursor = null;
//		try {
//			cursor = database
//					.rawQuery(
//							"select id,version from chunkdefinition where chunkCode=? and language=?",
//							new String[] { code, language });
//			if (cursor != null && cursor.moveToFirst()) { // 如果已存在，则返回错误！
//				int idExist = cursor.getInt(cursor.getColumnIndex("id"));
//				Integer versionExist = cursor.getInt(cursor
//						.getColumnIndex("version"));
//				cursor.close();
//				// 判断已存在的当前版本,无需更新
//				if (version == null || version == 0
//						|| (versionExist != null && versionExist >= version))
//					return -1;
//				// 需要升级先删除local db内数据
//				this.deleteChunk(idExist);
//			}
//			database.execSQL(
//					"insert into chunkdefinition values(?,?,?,?,?,?,?,?,?,?); ",
//					new String[] { null, code, chunkText, explanation,
//							audioFile, language, pronounce, errorWords,
//							String.valueOf(version),
//							isPreinstall == true ? "1" : "0" });
//			cursor = database
//					.rawQuery(
//							"select id from chunkdefinition where chunkCode=? order by id desc",
//							new String[] { code });
//			if (cursor != null && cursor.moveToFirst()) {
//				id = cursor.getInt(cursor.getColumnIndex("id"));
//				cursor.close();
//			}
//			return id;
//		} catch (Exception ex) {
//			ex.printStackTrace();
//			return id;
//		} finally {
//			if (cursor != null) {
//				cursor.close();
//				cursor = null;
//			}
//		}
//	}

//	// 通过标题关键字获取chunk
//	public List<Chunk> SearchChunkByKeyword(String text, String language) {
//		Cursor cursor = database.rawQuery(
//				"select id from chunkdefinition where language=? and chunkText like \"%"
//						+ text + "%\"", new String[] { language });
//		List<Chunk> chunkDefinitions = new ArrayList<Chunk>();
//		if (cursor != null) {
//			while (cursor.moveToNext()) {
//				Chunk chunk = getChunk(
//						cursor.getInt(cursor.getColumnIndex("id")), "null");
//				chunkDefinitions.add(chunk);
//			}
//			cursor.close();
//			cursor = null;
//		}
//		return chunkDefinitions;
//	}

    // 获取所有chunk
    public List<Chunk> getAll(String language) {


        List<Chunk> selectedChunks=new LinkedList<Chunk>() ;
        List<Chunk> chunks = chunksHolder.getChunks();
        if (chunks == null || chunks.size() == 0) {
            return selectedChunks;
        }
        for (Iterator<Chunk> iterator = chunks.iterator(); iterator.hasNext(); ) {
            Chunk chunk = iterator.next();
//            if(StringUtils.isEquals(chunk.getLanguage(), language)){
                selectedChunks.add(chunk);
//            }

        }
        return selectedChunks;
//        List<Chunk> chunkDefinitions = new ArrayList<Chunk>();
//        Cursor cursor = database
//                .rawQuery(
//                        "select id from ChunkDefinition where language=? order by chunkCode",
//                        new String[]{language});
//        if (cursor != null) {
//            while (cursor.moveToNext()) {
//                Chunk chunk = new Chunk();
//                int id = cursor.getInt(cursor.getColumnIndex("id"));
//                chunk = getChunk(id, "");
//                chunkDefinitions.add(chunk);
//            }
//            cursor.close();
//        }
//        return chunkDefinitions;
    }

    // 获得可以实战的chunks
    public List<Chunk> getToRehearseChunks(String uid, String language,
                                           long rehearse1Time, long rehearse2Time, long rehearse3Time) {
        long time = new Date().getTime();
        List<Chunk> chunkList = new ArrayList<Chunk>();
        List<String> codeList = new ArrayList<String>();
        Cursor cursor = database
                // (24 * 60 * 60 * 1000)测试不用
                .rawQuery(
                        "select chunkCode from UserProgressStatus where uid=? and chunkStatus=3 and ((rehearseStatus=0 and ?-? >= chunkLearnTime) or(rehearseStatus=1 and ?-?>=r1CostTime) or (rehearseStatus=2 and ?-?>=r2CostTime)) order by chunkCode",
                        new String[]{uid, String.valueOf(time),
                                String.valueOf(rehearse1Time),
                                String.valueOf(time),
                                String.valueOf(rehearse2Time),
                                String.valueOf(time),
                                String.valueOf(rehearse3Time)});
        if (cursor != null) {
            while (cursor.moveToNext()) {
                String code = cursor.getString(cursor
                        .getColumnIndex("chunkCode"));
                if (code != null && !code.isEmpty())
                    codeList.add(code);
            }
            cursor.close();
            cursor = null;
        }
        if (codeList != null && codeList.size() > 0) {
            for (int index = 0; index < codeList.size(); index++) {
                Chunk chunk;
                chunk = getChunkByCode(codeList.get(index), uid, language);
                if (chunk != null)
                    chunkList.add(chunk);
            }
        }
        return chunkList;
    }

    // 获得即将解锁的集合
    public List<Chunk> getFutrueRehearseChunks(String uid, String language,
                                               long rehearse1Time, long rehearse2Time, long rehearse3Time) {
        long time = new Date().getTime();
        List<Chunk> chunkDefinitions = new ArrayList<Chunk>();
        List<String> codeList = new ArrayList<String>();
        Cursor cursor = database
                .rawQuery(
                        "select chunkCode from UserProgressStatus where uid=? and chunkStatus=3 and ((rehearseStatus=0 and ?-? < chunkLearnTime) or (rehearseStatus=1 and ?-? < r1CostTime ) or (rehearseStatus=2 and ?-?< r2CostTime)) order by chunkCode",
                        new String[]{uid, String.valueOf(time),
                                String.valueOf(rehearse1Time),
                                String.valueOf(time),
                                String.valueOf(rehearse2Time),
                                String.valueOf(time),
                                String.valueOf(rehearse3Time)});
        if (cursor != null) {
            while (cursor.moveToNext()) {
                String code = cursor.getString(cursor
                        .getColumnIndex("chunkCode"));
                if (code != null && !code.isEmpty())
                    codeList.add(code);
            }
            cursor.close();
            cursor = null;
        }
        if (codeList != null && codeList.size() > 0) {
            for (int index = 0; index < codeList.size(); index++) {
                Chunk chunk = new Chunk();
                chunk = getChunkByCode(codeList.get(index), uid, language);
                if (chunk != null)
                    chunkDefinitions.add(chunk);
            }
        }
        return chunkDefinitions;
    }

    /**
     * 返回新的chunk列表
     *
     * @param uid
     * @param language
     * @return
     */
    public List<Chunk> getNewChunk(String uid, String language) {
        Cursor cursor = null;
        List<Chunk> chunkDefinitions = new ArrayList<Chunk>();
        List<String> codeList = new ArrayList<String>();
        cursor = database
                .rawQuery(
                        "select chunkCode from UserProgressStatus where chunkStatus=1 and uid=? order by id",
//                        "select chunkCode from UserProgressStatus where chunkStatus=1 and uid=? order by chunkCode",
                        new String[]{uid});
        if (cursor != null) {
            while (cursor.moveToNext()) {
                String code = cursor.getString(cursor
                        .getColumnIndex("chunkCode"));
                if (code != null && !code.isEmpty())
                    codeList.add(code);
            }
            cursor.close();
            cursor = null;
        }
        if (codeList != null && codeList.size() > 0) {
            for (int index = 0; index < codeList.size(); index++) {
                Chunk chunk = getChunkByCode(codeList.get(index), uid, language);
                if (chunk != null)
                    chunkDefinitions.add(chunk);
            }
        }
        return chunkDefinitions;
    }

    /**
     * 解锁一个chunk
     *
     * @param uid
     * @param language
     * @param unlockTime
     * @param unlockLimits
     * @return
     */
    public Chunk unlockOneChunk(String uid, String language, long unlockTime,
                                int unlockLimits) {
        Cursor cursor = null;
        try {
            // 我的所有已解锁、已学习、已练习和已掌握的chunk数量
            int myChunksCount = getMyChunkCount(uid);
            if (myChunksCount < unlockLimits) {
                // 如果是初始化状态,直接解锁unlockLimits个
                List<String> codeList = getUnlockChunkCode(uid, language,
                        unlockLimits - myChunksCount);
                if (codeList != null && codeList.size() > 0) {
                    for (int index = 0; index < codeList.size(); index++)
                        addNewChunkIntoProgress(uid, codeList.get(index));
                }
            } else {
                int alreadyUnlockNum = getNewChunkNumber(uid);
                long latestUnlockChunkTime = 0;
                if (alreadyUnlockNum < unlockLimits) { // chunk不足unlockLimits，需要解锁
                    cursor = database
                            .rawQuery(
                                    "select max(createTime),uid from UserProgressStatus where uid=?",
                                    new String[]{uid});
                    if (cursor != null && cursor.moveToFirst())
                        latestUnlockChunkTime = cursor.getLong(cursor
                                .getColumnIndex("max(createTime)"));
                    if (cursor != null)
                        cursor.close();
                    long currentTime = System.currentTimeMillis();
                    // 时间间隔不够，不能解锁
                    if (currentTime - latestUnlockChunkTime < unlockTime)
                        return null;
                    // 获得所有对用户未解锁的前days个chunk id列表
                    String chunkCode = getUnlockChunkCode(uid, language);
                    if (chunkCode == null || chunkCode.isEmpty())
                        return null;
                    if (!addNewChunkIntoProgress(uid, chunkCode))
                        return null;
                }
            }
            // 返回新解锁的第一个chunk
            cursor = database
                    .rawQuery(
                            "select chunkCode from UserProgressStatus where chunkStatus=1 and uid=? order by id",
//                            "select chunkCode from UserProgressStatus where chunkStatus=1 and uid=? order by chunkCode",
                            new String[]{uid});
            if (cursor != null && cursor.moveToFirst())
                return getChunkByCode(
                        cursor.getString(cursor.getColumnIndex("chunkCode")),
                        uid, language);
            return null;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        } finally {
            if (cursor != null) {
                cursor.close();
                cursor = null;
            }
        }
    }

    /**
     * 添加新的chunk到userprogress中 *
     */
    private boolean addNewChunkIntoProgress(String uid, String chunkCode) {
        Cursor cursor = null;
        try {
            UserProgressStatus userProgressStatus = new UserProgressStatus();
            userProgressStatus.setChunkCode(chunkCode);
            userProgressStatus.setUid(uid);
            userProgressStatus.setCreateTime(new Date().getTime());
            userProgressStatus.setChunkStatus(1);
            userProgressStatus.setRehearseStatus(0);
            // 判断chunkcode是否存在
//			cursor = database
//					.rawQuery(
//							"select * from ChunkDefinition where chunkCode=?",
//							new String[] { userProgressStatus.getChunkCode()
//									.toString() });
//			if (cursor == null || cursor.getCount() < 1) {
//				return false;
//			}
//			cursor.close();


            if (!isChunkExit(chunkCode)) {
                return false;
            }

            // 判断user progress是否存在
            cursor = database
                    .rawQuery(
                            "select * from UserProgressStatus where uid=? and chunkCode=?",
                            new String[]{uid, chunkCode});
            if (cursor != null && cursor.getCount() > 0)
                return false;
            database.execSQL(
                    "insert into UserProgressStatus values(?,?,?,?,?,1,0,?,0,0,0,0,0,0,0,0,0)",
                    new String[]{null, uid,
                            userProgressStatus.getChunkCode().toString(),
                            userProgressStatus.getChunkStatus().toString(),
                            userProgressStatus.getRehearseStatus().toString(),
                            String.valueOf(userProgressStatus.getCreateTime()),});
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        } finally {
            if (cursor != null)
                cursor.close();
        }
    }

    /**
     * 获得我所有解锁的chunk数量
     *
     * @param uid
     * @return
     */
    private int getMyChunkCount(String uid) {
        int num = 0;
        Cursor cursor = database.rawQuery(
                "select count(chunkCode) from UserProgressStatus where uid=?",
                new String[]{uid});
        if (cursor != null && cursor.moveToFirst()) {
            num = cursor.getInt(0);
            cursor.close();
        }
        return num < 0 ? 0 : num;
    }

    /*
     * 获得状态为新解锁的chunk数量
     */
    private int getNewChunkNumber(String uid) {
        int num = 0;
        Cursor cursor = database
                .rawQuery(
                        "select count(chunkCode) from UserProgressStatus where chunkStatus=1 and uid=?",
                        new String[]{uid});
        if (cursor != null && cursor.moveToFirst()) {
            num = cursor.getInt(0);
            cursor.close();
        }
        return num < 0 ? 0 : num;
    }

    /**
     * 获得前1个未解锁的Chunk,并根据chunk code升序排序
     *
     * @param uid
     * @param language
     * @return
     */
    private String getUnlockChunkCode(String uid, String language) {

        try {
            List<String> chunkIds=new ArrayList<String>();
            Cursor cursor = database
                    .rawQuery(
                            "select chunkCode from UserProgressStatus where uid=?",
                            new String[]{uid});
            if (cursor != null) {
                while (cursor.moveToNext()) {
                    chunkIds.add(cursor.getString(0));
                }
                cursor.close();
            }
            for (Chunk chunk :getAll(language)) {

               if(!chunkIds.contains(chunk.getChunkCode())) {
                   return chunk.getChunkCode();
               }
            }

            return null;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }


//        try {
//            Cursor cursor = database
//                    .rawQuery(
//                            "select chunkCode from ChunkDefinition where language=? and chunkCode not in(select chunkCode from UserProgressStatus where uid=?) order by chunkCode",
//                            new String[]{language, uid});
//            if (cursor != null && cursor.moveToFirst()) {
//                return cursor.getString(0);
//            } else
//                return null;
//        } catch (Exception ex) {
//            ex.printStackTrace();
//            return null;
//        }
    }

    /**
     * 获得topN的未解锁的chunk code *
     */
    private List<String> getUnlockChunkCode(String uid, String language,
                                            int topN) {
        try {
            List<String> chunkIds=new ArrayList<String>();
            List<String> selectedIds=new ArrayList<String>();
            Cursor cursor = database
                    .rawQuery(
                            "select chunkCode from UserProgressStatus where uid=?",
                            new String[]{uid});
            if (cursor != null) {
                while (cursor.moveToNext()) {
                    chunkIds.add(cursor.getString(0));
                }
                cursor.close();
            }
            for (Chunk chunk : getAll(language)) {
//                if(!chunkIds.contains(chunk.getChunkCode())) {
                if(!chunkIds.contains(chunk.getChunkCode())&&!StringUtils.isNumeric(chunk.getChunkCode())) {
                    selectedIds.add(chunk.getChunkCode());
                    if(selectedIds.size()>=topN){
                        return selectedIds;
                    }
                }
            }
            return selectedIds;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }

//        try {
//            Cursor cursor = database
//                    .rawQuery(
//                            "select chunkCode from ChunkDefinition where language=? and chunkCode not in(select chunkCode from UserProgressStatus where uid=?) order by chunkCode limit ?",
//                            new String[]{language, uid, String.valueOf(topN)});
//            if (cursor != null) {
//                List<String> codeList = new ArrayList<String>();
//                while (cursor.moveToNext()) {
//                    codeList.add(cursor.getString(0));
//                }
//                return codeList;
//            }
//            return null;
//        } catch (Exception ex) {
//            ex.printStackTrace();
//            return null;
//        }
    }

    // 获得结束的chunk集合
    public List<Chunk> getMasteredChunkList(String uid, String language) {
        List<Chunk> chunks = new ArrayList<Chunk>();
        Cursor cursor = database
                .rawQuery(
                        "select chunkCode from UserProgressStatus where rehearseStatus>2 and uid=?",
                        new String[]{uid});
        if (cursor != null) {
            while (cursor.moveToNext()) {
                chunks.add(getChunkByCode(
                        cursor.getString(cursor.getColumnIndex("chunkCode")),
                        uid, language));
            }
        }
        return chunks;
    }

    // 根据关键字查找可以rehearseChunks
    public List<Chunk> searchRehearseChunkList(String text, String language,
                                               String uid, long rehearse1Time, long rehearse2Time,
                                               long rehearse3Time) {
        long time = new Date().getTime();
        List<Chunk> chunkDefinitions = new ArrayList<Chunk>();
        Cursor cursor2 = database
                .rawQuery(
                        "select chunkCode from UserProgressStatus where uid=? and chunkStatus=3 and ((rehearseStatus=0 and ?-? >= chunkLearnTime) or(rehearseStatus=1 and ?-?>=r1CostTime) or (rehearseStatus=2 and ?-?>=r2CostTime))",
                        new String[]{uid, String.valueOf(time),
                                String.valueOf(rehearse1Time),
                                String.valueOf(time),
                                String.valueOf(rehearse2Time),
                                String.valueOf(time),
                                String.valueOf(rehearse3Time)});
        if (cursor2 != null) {
            while (cursor2.moveToNext()) {
                Chunk chunk = getChunkByCode(cursor2.getString(0), uid, language);
                if(chunk.getChunkText().contains(text)){
                    chunkDefinitions.add(chunk);
                }
            }
        }
        cursor2.close();
        return chunkDefinitions;
    }

    // 根据关键字查找MasterChunkList
    public List<Chunk> searchMasterChunkList(String text, String language,
                                             String uid) {
        List<Chunk> chunks = new ArrayList<Chunk>();
        Cursor cursor2 = database
                .rawQuery(
                        "select chunkCode from UserProgressStatus where rehearseStatus>2 and uid=?", new String[]{uid});
        if (cursor2 != null) {
            while (cursor2.moveToNext()) {
                Chunk chunk = getChunkByCode(cursor2.getString(0), uid, language);
                if(chunk.getChunkText().contains(text)){
                    chunks.add(chunk);
                }
            }
        }
        cursor2.close();

        return chunks;
    }

//	/** 根据ID来删除chunk **/
//	public boolean deleteChunk(int id) {
//		Chunk chunk = this.getChunk(id, null);
//		if (chunk == null) // chunk不存在
//			return true;
//		try {
//			// 删除多选题
//			if (chunk.getMulityChoiceQuestions() != null
//					&& chunk.getMulityChoiceQuestions().size() > 0) {
//				for (MulityChoiceQuestions question : chunk
//						.getMulityChoiceQuestions())
//					database.execSQL(
//							"delete from MulityChoiceAnswers where questionID = ?",
//							new Object[] { question.getId() });
//			}
//			database.execSQL(
//					"delete from MulityChoiceQuestions where chunkid = ?",
//					new Object[] { id });
//			// 删除hint list
//			database.execSQL("delete from HintDefinition where chunkid = ?",
//					new Object[] { id });
//			// 删除对话
//			if (chunk.getChunkPresentation() != null)
//				database.execSQL(
//						"delete from PresentationConversation where presentationID = ?",
//						new Object[] { chunk.getChunkPresentation().getId() });
//			database.execSQL("delete from ChunkPresentation where chunkid = ?",
//					new Object[] { id });
//			// 删除chunk本身
//			database.execSQL("delete from ChunkDefinition where id = ?",
//					new Object[] { id });
//			return true;
//		} catch (Exception ex) {
//			ex.printStackTrace();
//			return false;
//		}
//	}

    /**
     * 计算下一个chunk学习时间； 如果当前没有可以新学的chunk，需要计算下一个chunk解锁时间； NULL表示没有新chunk可以解锁了,
     * 0表示当前就有可以learn的
     *
     * @return
     */
    public Long getLearnAvailableTime(String uid) {
        Cursor cursor = null;
        try {
            // 判断当前是否有新的chunk可以学习
            cursor = database
                    .rawQuery(
                            "select * from UserProgressStatus where uid=? and chunkStatus=0",
                            new String[]{uid});
            if (cursor != null && cursor.getCount() > 0)
                return (long) 0;
            cursor.close();
            cursor = database
                    .rawQuery(
                            "select max(createTime) from UserProgressStatus where uid=? ",
                            new String[]{uid});
            if (cursor != null && cursor.moveToFirst()) {
                long latestUnlockTime = cursor.getLong(0);
                return System.currentTimeMillis() - latestUnlockTime;
            }
            return null;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        } finally {
            if (cursor != null)
                cursor.close();
        }
    }

    /**
     * 计算下一个chunk rehearsal时间 Null表示没有可以rehearsal的了 0表示当前就有直接可以rehearsal的
     *
     * @param uid
     * @return
     */
    public Long getRehearsalAvailableTime(String uid, long r1MaxTime,
                                          long r2MaxTime, long r3MaxTime) {
        Cursor cursor = null;
        try {
            // 判断当前是否有直接可以rehearsal的chunk
            long time = System.currentTimeMillis();
            cursor = database
                    .rawQuery(
                            "select * from UserProgressStatus where uid=? and chunkStatus=3 and ((rehearseStatus=0 and ?-? >= chunkLearnTime) or(rehearseStatus=1 and ?-?>=r1CostTime) or (rehearseStatus=2 and ?-?>=r2CostTime))",
                            new String[]{uid, String.valueOf(r1MaxTime),
                                    String.valueOf(time),
                                    String.valueOf(r2MaxTime),
                                    String.valueOf(time),
                                    String.valueOf(r3MaxTime)});
            if (cursor != null && cursor.getCount() > 0)
                return (long) 0;
            cursor.close();
            // 优先判断最近可以做rehearsal 1的时间
            cursor = database
                    .rawQuery(
                            "select max(chunkLearnTime) from UserProgressStatus where uid=? and chunkStatus=3 and rehearseStatus = 0 ",
                            new String[]{uid});
            if (cursor != null && cursor.moveToFirst()) {
                long latestLearnTime = cursor.getLong(0);
                cursor.close();
                return System.currentTimeMillis() - latestLearnTime;
            }
            // 第二判断rehearsal 2
            cursor = database
                    .rawQuery(
                            "select max(r1CostTime) from UserProgressStatus where uid=? and chunkStatus=3 and rehearseStatus = 1",
                            new String[]{uid});
            if (cursor != null && cursor.moveToFirst()) {
                long latestR1Time = cursor.getLong(0);
                cursor.close();
                return System.currentTimeMillis() - latestR1Time;
            }
            // 第三判断rehearsal 3
            cursor = database
                    .rawQuery(
                            "select max(r2CostTime) from UserProgressStatus where uid=? and chunkStatus=3 and rehearseStatus = 2",
                            new String[]{uid});
            if (cursor != null && cursor.moveToFirst()) {
                long latestR2Time = cursor.getLong(0);
                return System.currentTimeMillis() - latestR2Time;
            }
            return null;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        } finally {
            if (cursor != null)
                cursor.close();
        }
    }

}