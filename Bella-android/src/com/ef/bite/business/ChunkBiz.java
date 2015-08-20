package com.ef.bite.business;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import com.ef.bite.dataacces.ChunksHolder;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import com.ef.bite.dataacces.dao.ChunkDao;
import com.ef.bite.dataacces.dao.UserProgressStatusDao;
import com.ef.bite.dataacces.mode.Chunk;
import com.ef.bite.dataacces.mode.ChunkPresentation;
import com.ef.bite.dataacces.mode.HintDefinition;
import com.ef.bite.dataacces.mode.MulityChoiceAnswers;
import com.ef.bite.dataacces.mode.MulityChoiceQuestions;
import com.ef.bite.dataacces.mode.MyDBHelper;
import com.ef.bite.dataacces.mode.PresentationConversation;
import com.ef.bite.utils.JsonSerializeHelper;
import com.ef.bite.utils.ScoreLevelHelper;

@SuppressLint("DefaultLocale")
public class ChunkBiz {

    public final static String Language_EN = "en";
    public final static String Language_CN = "zh-cn";

    /**
     * chunk状态：不可用 *
     */
    public final static int CHUNK_STATUS_NOT_AVAILABLE = 0;
    /**
     * chunk状态：可用未学习 *
     */
    public final static int CHUNK_STATUS_AVAILABLE_NOT_LEARNING = 1;
    /**
     * chunk状态：学习未练习 *
     */
    public final static int CHUNK_STATUS_LEARNED_NOT_PRACTICED = 2;
    /**
     * chunk状态：练习了未实战 *
     */
    public final static int CHUNK_STATUS_PRACTICED_NOT_REHARSE = 3;

    /**
     * 实战阶段一 *
     */
    public final static int REHEARSE_R1 = 0;
    /**
     * 实战阶段二 *
     */
    public final static int REHEARSE_R2 = 1;
    /**
     * 实战阶段三 *
     */
    public final static int REHARSE_R3 = 2;
    /**
     * 实战并掌握 *
     */
    public final static int REHEARSE_MARSTERED = 3;

    /**
     * chunk的解锁时间
     */
    public final static long CHUNK_UNLOCK_TIME = (long) (24 * 60 * 60 * 1000);
    /**
     * chunk Rehearsal 1的解锁时间 *
     */
    public final static long REHEARSAL_R1_UNLOCK_TIME = 1 * CHUNK_UNLOCK_TIME;
    /**
     * chunk Rehearsal 2的解锁时间 *
     */
    public final static long REHARSAL_R2_UNLOCK_TIME = 5 * CHUNK_UNLOCK_TIME;
    /**
     * chunk Rehearsal 3的解锁时间 *
     */
    public final static long REHARSAL_R3_UNLOCK_TIME = 10 * CHUNK_UNLOCK_TIME;
    /**
     * chunk解锁的最大限制 *
     */
    public final static int UNLOCK_LIMITS_NUMBER = 3;

    Context mContext;
    ChunksHolder chunksHolder = ChunksHolder.getInstance();
    ChunkDao chunkDao;
    UserProgressStatusDao userProgressStatusDao;
    private SQLiteDatabase database;
    private MyDBHelper dbHelper;

    public ChunkBiz(Context context) {
        userProgressStatusDao = new UserProgressStatusDao(context);
        chunkDao = new ChunkDao(context);
        mContext = context;
        dbHelper = MyDBHelper.Instance(context);
        database = dbHelper.getWritableDatabase();
    }

    /**
     * 是否所有的当前的单词都学完了
     *
     * @return
     */
    public int getAllChunkCount() {
        try {
            return chunkDao.getAllChunkCount();
        } catch (Exception ex) {
            ex.printStackTrace();
            return 0;
        }
    }

    /**
     * 获取chunk
     *
     * @param chunkId
     * @param uid
     */
    public Chunk getChunk(String chunkId, String uid) {
        try {
            Chunk chunk = chunkDao.getChunkByCode(chunkId, uid, null);
            if (chunk != null) {
                return chunk;
            }
            return null;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    /**
     * 通过ChunkCode，user和language获得chunk
     *
     * @param code
     * @param uid
     * @param language
     * @return
     */
    public Chunk getChunkByCode(String code, String uid, String language) {
        try {
            return chunkDao.getChunkByCode(code, uid, language);
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public Chunk getChunkByCode(String chunkCode){
        return getChunkByCode(chunkCode,null,null);
    }

    public Chunk getChunkByCode(String chunkCode,String language){
        return getChunkByCode(chunkCode,null,language);
    }


//    /**
//     * 通过JSON 添加chunk
//     * @param chunkDefinition
//     * @param preInstall
//     * @return
//     */
//	public boolean addChunk(JSONObject chunkDefinition, boolean preInstall) {
//		try {
//			Chunk chunk = parseJsonChunk(chunkDefinition, preInstall);
//			if (chunk != null)
//				chunkDao.insertChunkIntoDB(chunk);
//		} catch (JSONException e) {
//			e.printStackTrace();
//			return false;
//		}
//		return true;
//	}

    // 通过json 得到chunk并写入数据库
    private Chunk parseJsonChunk(JSONObject chunkDefinition, boolean preInstall)
            throws JSONException {
        List<HintDefinition> hintDefinitions = new ArrayList<HintDefinition>();
        List<PresentationConversation> presentationConversations = new ArrayList<PresentationConversation>();
        List<MulityChoiceQuestions> mulityChoiceList = new ArrayList<MulityChoiceQuestions>();

        Chunk chunk = new Chunk();
        String fileName = chunkDefinition.getString("fileName");
        if (chunkDefinition.has("id"))
            chunk.setChunkCode(chunkDefinition.getString("id").trim());
        if (chunkDefinition.has("coursename"))
            chunk.setCoursename(chunkDefinition.getString("coursename").trim());
        if (chunkDefinition.has("language"))
            chunk.setLanguage(chunkDefinition.getString("language").trim()
                    .toLowerCase());
        if (chunkDefinition.has("version"))
            chunk.setVersion(chunkDefinition.getInt("version"));
        chunk.setChunkStatus(0);
        chunk.setIsPreinstall(preInstall);

        JSONObject chunkObject = chunkDefinition.getJSONObject("chunk");
        chunk.setChunkText(chunkObject.getString("chunk").trim());
        chunk.setExplanation(chunkObject.getString("definition").trim());
        JSONArray hint = chunkObject.getJSONArray("details");

        for (int i = 0; i < hint.length(); i++) {
            HintDefinition hintDefinition = new HintDefinition();
            JSONObject hintJsonObject = (JSONObject) hint.get(i);
            hintDefinition.setContent(hintJsonObject.getString("title").trim());
            hintDefinition.setExample(hintJsonObject.getString("content")
                    .trim());
            hintDefinitions.add(hintDefinition);
        }
        chunk.setHintDefinitions(hintDefinitions);
        if (chunkObject.getString("audio") != null
                && !chunkObject.getString("audio").trim().isEmpty())
            chunk.setAudioFileName(fileName
                    + chunkObject.getString("audio").trim());
        else
            chunk.setAudioFileName("");
        chunk.setPronounce(chunkObject.getString("pronounce").trim());
        if (!chunkObject.isNull("balloon-error-words")) {
            JSONArray errorWords = chunkObject
                    .getJSONArray("balloon-error-words");
            List<String> errorBalloonWords = new ArrayList<String>();
            if (errorWords != null) {
                for (int i = 0; i < errorWords.length(); i++) {
                    errorBalloonWords.add(errorWords.getString(i));
                }
            }
            chunk.setErrorBalloonWords(errorBalloonWords);
        }
        JSONObject presentation = chunkDefinition.getJSONObject("presentation");
        JSONArray dialogue = presentation.getJSONArray("dialogue");
        for (int i = 0; i < dialogue.length(); i++) {
            PresentationConversation presentationConversation = new PresentationConversation();
            JSONObject dialogueoJsonObject = (JSONObject) dialogue.get(i);
            if (dialogueoJsonObject.has("content"))
                presentationConversation.setSentence(dialogueoJsonObject
                        .getString("content"));
            if (dialogueoJsonObject.has("content_src"))
                presentationConversation.setContent_src(dialogueoJsonObject
                        .getString("content_src"));
            if (dialogueoJsonObject.has("speakericon"))
                presentationConversation.setCharacterAvater(dialogueoJsonObject
                        .getString("speakericon").trim());
            if (dialogueoJsonObject.has("audio_start_ts"))
                presentationConversation.setStartTime(dialogueoJsonObject
                        .getInt("audio_start_ts"));
            if (dialogueoJsonObject.has("audio_end_ts"))
                presentationConversation.setEndTime(dialogueoJsonObject
                        .getInt("audio_end_ts"));
            presentationConversations.add(presentationConversation);
        }

        ChunkPresentation chunkPresentation = new ChunkPresentation();
        if (presentation.getString("audio") != null
                && !presentation.getString("audio").trim().isEmpty())
            chunkPresentation.setAudioFile(fileName
                    + presentation.getString("audio").trim());
        else
            chunkPresentation.setAudioFile("");
        chunkPresentation
                .setPresentationConversations(presentationConversations);
        // 设置Presentation Score
        chunkPresentation.setScore(ScoreLevelHelper.MAXIMUN_SCORE_CHUNK_INFO);
        chunk.setChunkPresentation(chunkPresentation);
        JSONArray multi_choice = chunkDefinition.getJSONArray("multi-choice");

        for (int i = 0; i < multi_choice.length(); i++) {
            JSONObject iJsonObject = (JSONObject) multi_choice.get(i);
            MulityChoiceQuestions mulityChoiceQuestions = new MulityChoiceQuestions();
            if (iJsonObject.getString("audio") != null
                    && !iJsonObject.getString("audio").trim().isEmpty())
                mulityChoiceQuestions.setAudioFile(fileName
                        + iJsonObject.getString("audio").trim());
            else
                mulityChoiceQuestions.setAudioFile("");
            mulityChoiceQuestions.setOrder(i);
            if (iJsonObject.has("limit-time"))
                mulityChoiceQuestions.setLimitTime(Long.parseLong(iJsonObject
                        .getString("limit-time").trim()));
            mulityChoiceQuestions.setOrder(i);
            if (iJsonObject.has("header")) {
                mulityChoiceQuestions.setHeader(iJsonObject.getString("header")
                        .trim());
            }
            if (iJsonObject.has("multi-choice-type")) {
                if ("form".equalsIgnoreCase(iJsonObject.getString(
                        "multi-choice-type").trim())) {
                    mulityChoiceQuestions.setMulti_choicetype(1);
                } else if ("meaning".equalsIgnoreCase(iJsonObject.getString(
                        "multi-choice-type").trim())) {
                    mulityChoiceQuestions.setMulti_choicetype(2);
                } else if ("use".equalsIgnoreCase(iJsonObject.getString(
                        "multi-choice-type").trim())) {
                    mulityChoiceQuestions.setMulti_choicetype(3);
                }
            }
            mulityChoiceQuestions.setContent(iJsonObject.getString("question")
                    .trim());
            mulityChoiceQuestions.setOrder(i + 1);
            if (i >= 0 && i <= 2) {
                mulityChoiceQuestions.setType(1);
            } else {
                mulityChoiceQuestions.setType(2);
            }
            if ("y".equalsIgnoreCase(iJsonObject.getString("random").trim())) {
                mulityChoiceQuestions.setRandom(1);// 表示随机
            } else {
                mulityChoiceQuestions.setRandom(0);
            }
            // 设置Multi-Choice的Max Score
            switch (i) {
                case 0:
                    mulityChoiceQuestions
                            .setScore(ScoreLevelHelper.MAXIMUN_SCORE_PRACTICE_FORM);
                    break;
                case 1:
                    mulityChoiceQuestions
                            .setScore(ScoreLevelHelper.MAXIMUN_SCORE_PRACTICE_MEAN);
                    break;
                case 2:
                    mulityChoiceQuestions
                            .setScore(ScoreLevelHelper.MAXIMUN_SCORE_PRACTICE_USE);
                    break;
                case 3:
                    mulityChoiceQuestions
                            .setScore(ScoreLevelHelper.MAXIMUN_SCORE_REHEARSE_FORM);
                    break;
                case 4:
                    mulityChoiceQuestions
                            .setScore(ScoreLevelHelper.MAXIMUN_SCORE_REHEARSE_MEAN);
                    break;
                case 5:
                    mulityChoiceQuestions
                            .setScore(ScoreLevelHelper.MAXIMUN_SCORE_REHEARSE_USE);
                    break;
                default:
                    mulityChoiceQuestions
                            .setScore(ScoreLevelHelper.MAXIMUN_SCORE_PRACTICE_FORM);
                    break;
            }

            JSONArray itemsArray = iJsonObject.getJSONArray("items");
            List<MulityChoiceAnswers> mulityChoiceAnswers = new ArrayList<MulityChoiceAnswers>();

            for (int j = 0; j < itemsArray.length(); j++) {
                MulityChoiceAnswers mulityChoiceAnswer = new MulityChoiceAnswers();
                JSONObject item = (JSONObject) itemsArray.get(j);
                mulityChoiceAnswer.setOrder(j);
                mulityChoiceAnswer.setAnswer(item.getString("item").trim());
                if (item.has("iscorrect")) {
                    if (("y").equalsIgnoreCase(item.getString("iscorrect")
                            .trim())) {
                        mulityChoiceAnswer.setIsCorrect(1);
                    } else {
                        mulityChoiceAnswer.setIsCorrect(0);
                    }
                }

                if (item.has("errorhint")) {
                    mulityChoiceAnswer.setHitString(item.getString("errorhint")
                            .trim());
                }
                mulityChoiceAnswers.add(mulityChoiceAnswer);
            }
            mulityChoiceQuestions.setAnswers(mulityChoiceAnswers);
            mulityChoiceList.add(mulityChoiceQuestions);
        }
        chunk.setMulityChoiceQuestions(mulityChoiceList);
        return chunk;
    }

//	/**
//	 * 通过标题关键字查找chunk
//	 *
//	 * @param text
//	 * @param language
//	 * @return List<Chunk>
//	 */
//	public List<Chunk> SearchChunkByKeyword(String text, String language) {
//		try {
//			List<Chunk> chunks = chunkDao.SearchChunkByKeyword(text, language);
//			if (chunks != null && chunks.size() > 0) {
//				return chunks;
//			}
//			return null;
//		} catch (Exception ex) {
//			ex.printStackTrace();
//			return null;
//		}
//	}

    /**
     * 获取所有chunk
     *
     * @param language
     * @return
     */
    public List<Chunk> getAll(String language) {
        try {
            List<Chunk> chunks = chunkDao.getAll(language);
            if (chunks != null && chunks.size() > 0) {
                return chunks;
            }
            return null;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    /**
     * 获得可以实战的chunk集合
     *
     * @param uid
     * @param language
     * @param rehearse1Time
     * @param rehearse2Time
     * @param rehearse3Time
     * @return
     */
    public List<Chunk> GetRehearseChunksByPageByUser(String uid,
                                                     String language, long rehearse1Time, long rehearse2Time,
                                                     long rehearse3Time) {
        try {
            List<Chunk> chunks = chunkDao.getToRehearseChunks(uid, language,
                    rehearse1Time, rehearse2Time, rehearse3Time);
            if (chunks != null && chunks.size() > 0) {
                return chunks;
            }
            return null;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    /**
     * 获取即将解锁的chunk
     *
     * @param uid
     * @param language
     * @param rehearse1Time
     * @param rehearse2Time
     * @param rehearse3Time
     * @return
     */
    public List<Chunk> getFutrueRehearseChunks(String uid, String language,
                                               long rehearse1Time, long rehearse2Time, long rehearse3Time) {
        try {
            return chunkDao.getFutrueRehearseChunks(uid, language,
                    rehearse1Time, rehearse2Time, rehearse3Time);
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    /**
     * 解锁chunk，如果解锁成功则返回改chunk对象
     *
     * @param uid
     * @param language
     * @param unlockTime
     * @return
     */
    public Chunk unlockOneChunk(String uid, String language, long unlockTime) {
        try {
            return chunkDao.unlockOneChunk(uid, language, unlockTime,
                    UNLOCK_LIMITS_NUMBER);
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    /**
     * 获得新chunk的集合
     *
     * @param uid
     * @param language
     * @return
     */
    public List<Chunk> getNewChunk(String uid, String language) {
        try {
            List<Chunk> chunks = chunkDao.getNewChunk(uid, language);
            if (chunks != null && chunks.size() > 0) {
                return chunks;
            }
            return null;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    /**
     * 加载chunk并Add chunk
     *
     * @param context
     */
    // public void loadChunkFromResource(Context context, String assetPath) {
    // AssetManager asserter = context.getAssets();
    // String[] chunkDirs = null;
    // try {
    // chunkDirs = asserter.list(assetPath);
    // for (String dir : chunkDirs) {
    // Log.e("chunk", dir);
    // String[] chunks = asserter.list(assetPath + File.separator
    // + dir);
    // if (chunks != null && chunks.length > 0) {
    // try {
    // database.beginTransaction();
    // for (String chunk : chunks) {
    // Log.e("file", chunk);
    // if (chunk.contains(".json")) {
    // InputStream is = asserter.open(assetPath
    // + File.separator + dir + File.separator
    // + chunk);
    // loadChunkStream(chunk, assetPath
    // + File.separator + dir, is, true);
    // }
    // }
    // database.setTransactionSuccessful();
    // } catch (Exception e) {
    // // TODO: handle exception
    // } finally {
    // database.endTransaction();
    // }
    // }
    // }
    // } catch (Exception ex) {
    // ex.printStackTrace();
    // }
    // }

//	/**
//	 * 从本地存储加载chunk
//	 *
//	 * @param context
//	 * @param chunkPath
//	 */
//	public void loadChunkFromStorage(Context context, String chunkPath) {
//		String[] chunkDirs = null;
//		try {
//			chunkDirs = new File(chunkPath).list();
//			for (String dir : chunkDirs) {
//				Log.e("chunk", dir);
//				String[] chunks = new File(chunkPath, dir).list();
//				if (chunks != null && chunks.length > 0) {
//					for (String chunk : chunks) {
//						Log.e("file", chunk);
//						if (chunk.contains(".json")) {
//							InputStream is = new FileInputStream(new File(
//									chunkPath + File.separator + dir
//											+ File.separator + chunk));
//							loadChunkStream(chunk, chunkPath + File.separator
//									+ dir, is, false);
//						}
//					}
//				}
//			}
//		} catch (Exception ex) {
//			ex.printStackTrace();
//		}
//	}

//	/**
//	 * 通过chunkid从本地存储加载chunk
//	 *
//	 * @param context
//	 * @param chunkPath
//	 */
//	public Chunk loadChunkIDFromStorage(Context context, String chunkPath,
//			String chunkid) {
//		Chunk chunkname = new Chunk();
//		String[] chunkDirs = null;
//		try {
//			chunkDirs = new File(chunkPath).list();
//			for (String dir : chunkDirs) {
//				Log.e("chunk", dir);
//				String[] chunks = new File(chunkPath, dir).list();
//				if (chunks != null && chunks.length > 0) {
//					for (String chunk : chunks) {
//						Log.e("file", chunk);
//						if (chunk.contains(chunkid)) {
//							InputStream is = new FileInputStream(new File(
//									chunkPath + File.separator + dir
//											+ File.separator + chunk));
//							chunkname = parseJsonChunk(
//									loadChunkStream2(chunk, chunkPath
//											+ File.separator + dir, is, false),
//									false);
//						}
//					}
//				}
//			}
//		} catch (Exception ex) {
//			ex.printStackTrace();
//		}
//		return chunkname;
//	}

    /**
     * 从本地存储加载chunkVersion
     *
     * @param chunkPath
     * @param ChunkID
     * @return
     */
    public int loadChunkVersionFromStorage(String chunkPath, String ChunkID) {
        Chunk chunk = getChunk(ChunkID, null);
        if(chunk==null){
            return 0;
        }
        return chunk.getVersion();

    }

//	/**
//	 * 从本地获取音频路径
//	 *
//	 * @param context
//	 * @param chunkPath
//	 */
//	public String loadChunkAudioFromStorage(String chunkPath, String ChunkID) {
//		String AudioFile = "";
//		String[] chunkDirs = null;
//		try {
//			chunkDirs = new File(chunkPath).list();
//			for (String dir : chunkDirs) {
//				Log.e("chunk", dir);
//				String[] chunks = new File(chunkPath, dir).list();
//				if (chunks != null && chunks.length > 0) {
//					for (String chunk : chunks) {
//						Log.e("file", chunk);
//						if (chunk.contains(".json")) {
//							if (chunk.contains(ChunkID)) {
//								InputStream is = new FileInputStream(new File(
//										chunkPath + File.separator + dir
//												+ File.separator + chunk));
//								String jsonString = loadChunkJson(chunk,
//										chunkPath + File.separator + dir, is);
//								Chunk chunk2 = (Chunk) JsonSerializeHelper
//										.JsonDeserialize(jsonString,
//												Chunk.class);
//								AudioFile = chunk2.getChunkPresentation()
//										.getAudioFile();
//							}
//						}
//					}
//				}
//			}
//			return AudioFile;
//		} catch (Exception ex) {
//			ex.printStackTrace();
//			return AudioFile;
//		}
//
//	}

    /**
     * 从本地判断chunkID是否存在
     *
     * @param chunkPath
     * @param ChunkID
     * @return
     */
    public boolean isChunkIDFromStorage(String chunkPath, String ChunkID) {
            return chunkDao.isChunkExit(ChunkID);
    }

//	private boolean loadChunkStream(String chunkFileName, String baseDir,
//			InputStream is, boolean preinstall) {
//		try {
//			if (is == null)
//				return false;
//			InputStreamReader inputStreamReader = new InputStreamReader(is,
//					"UTF-8");
//			char[] buffer = new char[is.available()];
//			String jsonString;
//			StringBuffer stringBuffer = new StringBuffer();
//			while ((inputStreamReader.read(buffer)) != -1) {
//				stringBuffer.append(new String(buffer, 0, buffer.length));
//			}
//			jsonString = stringBuffer.toString();
//			JSONObject chunkjson = new JSONObject(jsonString);
//			chunkjson.put("fileName", baseDir + File.separator);
//			return addChunk(chunkjson, preinstall);
//		} catch (Exception e) {
//			e.printStackTrace();
//			return false;
//		}
//	}

//	private JSONObject loadChunkStream2(String chunkFileName, String baseDir,
//			InputStream is, boolean preinstall) {
//		try {
//			if (is == null)
//				return null;
//			InputStreamReader inputStreamReader = new InputStreamReader(is,
//					"UTF-8");
//			char[] buffer = new char[is.available()];
//			String jsonString;
//			StringBuffer stringBuffer = new StringBuffer();
//			while ((inputStreamReader.read(buffer)) != -1) {
//				stringBuffer.append(new String(buffer, 0, buffer.length));
//			}
//			jsonString = stringBuffer.toString();
//			JSONObject chunkjson = new JSONObject(jsonString);
//			chunkjson.put("fileName", baseDir + File.separator);
//			return chunkjson;
//		} catch (Exception e) {
//			e.printStackTrace();
//			return null;
//		}
//	}

    @SuppressWarnings("unused")
    private String loadChunkJson(String chunkFileName, String baseDir,
                                 InputStream is) {
        String jsonString = "";
        try {
            if (is == null)
                return jsonString;
            InputStreamReader inputStreamReader = new InputStreamReader(is,
                    "UTF-8");
            char[] buffer = new char[is.available()];

            StringBuffer stringBuffer = new StringBuffer();
            while ((inputStreamReader.read(buffer)) != -1) {
                stringBuffer.append(new String(buffer, 0, buffer.length));
            }
            jsonString = stringBuffer.toString();
            return jsonString;
        } catch (Exception e) {
            e.printStackTrace();
            return jsonString;
        }
    }

    /**
     * 获得实战完成的 chunk集合
     *
     * @param uid
     * @return list<chunk>
     */
    public List<Chunk> getMasteredChunkList(String uid, String language) {
        try {
            return chunkDao.getMasteredChunkList(uid, language);
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    /**
     * 通过关键字搜索可以Rehearse的chunk
     *
     * @param text
     * @param language
     * @param uid
     * @param rehearse1Time
     * @param rehearse2Time
     * @param rehearse3Time
     * @return
     */
    public List<Chunk> searchRehearseChunkList(String text, String language,
                                               String uid, long rehearse1Time, long rehearse2Time,
                                               long rehearse3Time) {
        try {
            return chunkDao.searchRehearseChunkList(text, language, uid,
                    rehearse1Time, rehearse2Time, rehearse3Time);
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    /**
     * 通过关键字搜索完成的chunk
     *
     * @param text
     * @param language
     * @param uid
     * @return
     */

    public List<Chunk> searchMasterChunkList(String text, String language,
                                             String uid) {
        try {
            return chunkDao.searchMasterChunkList(text, language, uid);
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public Boolean SetChunkStatus(String chunkID, String uid, Integer status,
                                  Integer rehearseStatus, Integer score, long costtime) {
        try {
            return userProgressStatusDao.SetChunkStatus(chunkID, uid, status,
                    rehearseStatus, score, costtime);
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }

    /**
     * 如果RehearseFailed 则重置
     *
     * @param uid
     * @param chunkId
     * @return
     */
    public boolean setRehearseFailed(String uid, String chunkId) {
        try {
            return userProgressStatusDao.setRehearseFailed(uid, chunkId);
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }

//	/**
//	 * 获得Progress状态
//	 *
//	 * @param uid
//	 * @param chunkCode
//	 * @return
//	 */
//	public UserProgressStatus getUserProgress(String uid, String chunkCode) {
//		UserProgressStatusDao dao = new UserProgressStatusDao(mContext);
//		return dao.getUserProgress(uid, chunkCode);
//	}

    /**
     * 计算下一个chunk学习时间； 如果当前没有可以新学的chunk，需要计算下一个chunk解锁时间； NULL表示没有新chunk可以解锁了,
     * 0表示当前就有可以learn的
     *
     * @return
     */
    public Long getLearnAvailableTime(String uid) {
        try {
            return chunkDao.getLearnAvailableTime(uid);
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    /**
     * 计算下一个chunk rehearsal时间 Null表示没有可以rehearsal的了 0表示当前就有直接可以rehearsal的
     *
     * @param uid
     * @return
     */
    public Long getRehearsalAvailableTime(String uid) {
        try {
            return chunkDao.getRehearsalAvailableTime(uid,
                    REHEARSAL_R1_UNLOCK_TIME, REHARSAL_R2_UNLOCK_TIME,
                    REHARSAL_R3_UNLOCK_TIME);
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }
}
