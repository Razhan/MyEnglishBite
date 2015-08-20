package com.ef.bite.business;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;

import com.ef.bite.AppConst;
import com.ef.bite.dataacces.ChunksHolder;
import com.ef.bite.dataacces.dao.UserProgressStatusDao;
import com.ef.bite.dataacces.mode.Chunk;
import com.ef.bite.dataacces.mode.MulityChoiceQuestions;
import com.ef.bite.dataacces.mode.UserProgressStatus;
import com.ef.bite.model.ConfigModel;
import com.ef.bite.utils.AppLanguageHelper;
import com.ef.bite.utils.FileStorage;

public class ChunkBLL {
	public final static String ASSET_LATERINSTALL_CHUNKS = "chunks/later_installed";
	public final static String ASSET_PREINSTALL_CHUNKS = "chunks/pre_installed";

	Context mContext;
	ChunkBiz mChunkBiz;

	public ChunkBLL(Context context) {
		mContext = context;
		mChunkBiz = new ChunkBiz(mContext);
	}

	/**
	 * 初始化资源
	 */
	public boolean loadLaterinstallChunks() {
		try {
			ConfigModel config = new GlobalConfigBLL(mContext).getConfigModel();
			if (config == null
					|| !config.IsLaterinstallChunksLoaded
					|| AppConst.GlobalConfig.CHUNK_MIGRATION_VERSION > new SharedPreferenceBLL(
							mContext).getChunkMigrationVersion()) {
				// mChunkBiz.loadChunkFromResource(mContext,
				// ASSET_LATERINSTALL_CHUNKS);



//				FileStorage courseStorage = new FileStorage(mContext,
//						AppConst.CacheKeys.Storage_Course);
//				mChunkBiz.loadChunkFromStorage(mContext,
//						courseStorage.getStorageFolder());
				if (config == null) {
					config = new ConfigModel();
					config.IsLaterinstallChunksLoaded = true;
				} else
					config.IsLaterinstallChunksLoaded = true;
				new GlobalConfigBLL(mContext).setConfigModel(config);
				new SharedPreferenceBLL(mContext)
						.setChunkMigrationVersion(AppConst.GlobalConfig.CHUNK_MIGRATION_VERSION);
				return true;
			}
			return false;
		} catch (Exception ex) {
			ex.printStackTrace();
			return false;
		}
	}

	/***
	 * 加载预安装的
	 */
	public boolean loadPreinstallChunks() {
		try {
			ConfigModel config = new GlobalConfigBLL(mContext).getConfigModel();
			if (config == null
					|| !config.IsPreinstallChunksLoaded
					|| AppConst.GlobalConfig.CHUNK_MIGRATION_VERSION > new SharedPreferenceBLL(
							mContext).getChunkMigrationVersion()) {
				// mChunkBiz.loadChunkFromResource(mContext,
				// ASSET_PREINSTALL_CHUNKS);



//				FileStorage courseStorage = new FileStorage(mContext,
//						AppConst.CacheKeys.Storage_Course);
//				mChunkBiz.loadChunkFromStorage(mContext,
//						courseStorage.getStorageFolder());
				if (config == null) {
					config = new ConfigModel();
					config.IsPreinstallChunksLoaded = true;
				} else
					config.IsPreinstallChunksLoaded = true;
				new GlobalConfigBLL(mContext).setConfigModel(config);
				return true;
			}
			return false;
		} catch (Exception ex) {
			ex.printStackTrace();
			return false;
		}
	}

	// /**
	// * 获得所有的chunk
	// *
	// * @return
	// */
	// public List<Chunk> getAllChunk() {
	// try {
	// // loadPreinstallChunks();
	// // loadLaterinstallChunks();
	// return mChunkBiz.getAll(AppLanguageHelper.getLaunguage(mContext));
	// } catch (Exception ex) {
	// ex.printStackTrace();
	// return null;
	// }
	// }

	/**
	 * 在线版 获得所有的chunk
	 * 
	 * @return
	 */
	public List<Chunk> getAllChunk() {
		try {
			// loadPreinstallChunks();
			// loadLaterinstallChunks();
			return mChunkBiz.getAll(AppLanguageHelper
					.getSystemLaunguage(mContext));
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}

	/**
	 * 获得本地的所有chunk，用来判断是否需要更新版本
	 * 
	 * @return
	 */
	public List<Chunk> getAllChunksForUpdate() {
		return getAllChunk();
	}

	/**
	 * 通过ID获得chunk
	 * 
	 * @param id
	 * @return
	 */
	public Chunk getChunk(String id) {
		try {
			return mChunkBiz.getChunk(id, AppConst.CurrUserInfo.UserId);
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}

	/**
	 * 获得一个可以学习的chunk，如果不存在则根据相应的规则解锁一个chunk
	 * 
	 * @return
	 */
	public Chunk getChunkForLearn() {
		try {
			List<Chunk> newChunks = getNewChunk();
			if (newChunks != null && newChunks.size() > 0)
				return newChunks.get(0);
			else
				return unlockOneChunk();
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}

	// /**
	// * 获得当前chunk的翻译， 如果当前语言是中文：则翻译的为英文 如果当前语言是英文：则翻译的为中文
	// *
	// * @param chunkCode
	// * @return
	// */
	// public Chunk getTranslatedChunk(String chunkCode) {
	// try {
	// if (AppLanguageHelper.getLaunguage(mContext).equals(
	// AppLanguageHelper.EN)) // 当前是英文
	// return mChunkBiz.getChunkByCode(chunkCode,
	// AppConst.CurrUserInfo.UserId, AppLanguageHelper.ZH_CN);
	// else
	// return mChunkBiz.getChunkByCode(chunkCode,
	// AppConst.CurrUserInfo.UserId, AppLanguageHelper.EN);
	// } catch (Exception ex) {
	// ex.printStackTrace();
	// return null;
	// }
	// }

	/**
	 * 在线版（不只是两种语言） 获得当前chunk的翻译， 如果当前语言是中文：则翻译的为英文 如果当前语言是英文：则翻译的为中文
	 * 
	 * @param chunkCode
	 * @return
	 */
	public Chunk getTranslatedChunk(String chunkCode) {
		try {
			// if (AppLanguageHelper.getSystemLaunguage(mContext).equals(
			// AppLanguageHelper.EN)) // 当前是英文
			return mChunkBiz.getChunkByCode(chunkCode,
					AppConst.CurrUserInfo.UserId,
                    AppLanguageHelper.EN);
			// else
			// return mChunkBiz.getChunkByCode(chunkCode,
			// AppConst.CurrUserInfo.UserId, AppLanguageHelper.EN);
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}

	/***
	 * 是否所有的当前的单词都学完了
	 * 
	 * @return
	 */
	public int getAllChunkCount() {
		try {
			return mChunkBiz.getAllChunkCount();
		} catch (Exception ex) {
			ex.printStackTrace();
			return 0;
		}
	}

	// /**
	// * 获得可以学习的Chunk
	// *
	// * @return
	// */
	// public List<Chunk> getNewChunk() {
	// try {
	// return mChunkBiz.getNewChunk(AppConst.CurrUserInfo.UserId,
	// AppLanguageHelper.getLaunguage(mContext));
	// } catch (Exception ex) {
	// ex.printStackTrace();
	// return null;
	// }
	// }

	/**
	 * 在线版 获得可以学习的Chunk
	 * 
	 * @return
	 */
	public List<Chunk> getNewChunk() {
		try {
			return mChunkBiz.getNewChunk(AppConst.CurrUserInfo.UserId,
					AppLanguageHelper.getSystemLaunguage(mContext));
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}

	// /**
	// * 解锁一个chunk并返回
	// *
	// * @return
	// */
	// public Chunk unlockOneChunk() {
	// try {
	// return mChunkBiz.unlockOneChunk(AppConst.CurrUserInfo.UserId,
	// AppLanguageHelper.getLaunguage(mContext),
	// ChunkBiz.CHUNK_UNLOCK_TIME);
	// } catch (Exception ex) {
	// ex.printStackTrace();
	// return null;
	// }
	// }

	/**
	 * 解锁一个chunk并返回
	 * 
	 * @return
	 */
	public Chunk unlockOneChunk() {
		try {
			return mChunkBiz.unlockOneChunk(AppConst.CurrUserInfo.UserId,
					AppLanguageHelper.getSystemLaunguage(mContext),
					ChunkBiz.CHUNK_UNLOCK_TIME);
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}

	/**
	 * 获得可以Rehearse的Chunk数
	 * 
	 * @return
	 */
	public int getToRehearseChunkCount() {
		try {
			List<Chunk> chunkList = getRehearseChunkList();
			return chunkList == null ? 0 : chunkList.size();
		} catch (Exception ex) {
			ex.printStackTrace();
			return 0;
		}
	}

	/**
	 * 获得Marstered的Chunk数
	 * 
	 * @return
	 */
	public int getMarsterdChunkCount() {
		try {
			List<Chunk> chunkList = getMasteredChunks();
			return chunkList == null ? 0 : chunkList.size();
		} catch (Exception ex) {
			ex.printStackTrace();
			return 0;
		}
	}

	// /**
	// * 获得可以rehearse的所有chunk
	// *
	// * @return
	// */
	// public List<Chunk> getRehearseChunkList() {
	// try {
	// return mChunkBiz.GetRehearseChunksByPageByUser(
	// AppConst.CurrUserInfo.UserId,
	// AppLanguageHelper.getLaunguage(mContext),
	// ChunkBiz.REHEARSAL_R1_UNLOCK_TIME,
	// ChunkBiz.REHARSAL_R2_UNLOCK_TIME,
	// ChunkBiz.REHARSAL_R3_UNLOCK_TIME);
	// } catch (Exception ex) {
	// ex.printStackTrace();
	// return null;
	// }
	// }

	/**
	 * 在线版 获得可以rehearse的所有chunk
	 * 
	 * @return
	 */
	public List<Chunk> getRehearseChunkList() {
		try {
			return mChunkBiz.GetRehearseChunksByPageByUser(
					AppConst.CurrUserInfo.UserId,
					AppLanguageHelper.getSystemLaunguage(mContext),
					ChunkBiz.REHEARSAL_R1_UNLOCK_TIME,
					ChunkBiz.REHARSAL_R2_UNLOCK_TIME,
					ChunkBiz.REHARSAL_R3_UNLOCK_TIME);
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}

	// /**
	// * 获得将来可以rehearse的chunk
	// *
	// * @return
	// */
	// public List<Chunk> getFutrueRehearseChunks() {
	// try {
	// return mChunkBiz.getFutrueRehearseChunks(
	// AppConst.CurrUserInfo.UserId,
	// AppLanguageHelper.getLaunguage(mContext),
	// ChunkBiz.REHEARSAL_R1_UNLOCK_TIME,
	// ChunkBiz.REHARSAL_R2_UNLOCK_TIME,
	// ChunkBiz.REHARSAL_R3_UNLOCK_TIME);
	// } catch (Exception ex) {
	// ex.printStackTrace();
	// return null;
	// }
	// }

	/**
	 * 在线版 获得将来可以rehearse的chunk
	 * 
	 * @return
	 */
	public List<Chunk> getFutrueRehearseChunks() {
		try {
			return mChunkBiz.getFutrueRehearseChunks(
					AppConst.CurrUserInfo.UserId,
					AppLanguageHelper.getSystemLaunguage(mContext),
					ChunkBiz.REHEARSAL_R1_UNLOCK_TIME,
					ChunkBiz.REHARSAL_R2_UNLOCK_TIME,
					ChunkBiz.REHARSAL_R3_UNLOCK_TIME);
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}

	/***
	 * 获得Marstered的Chunk
	 * 
	 * @return
	 */
	public List<Chunk> getMasteredChunks() {
		// return AppConst.ChunkList;
		try {
			// return
			// mChunkBiz.getMasteredChunkList(AppConst.CurrUserInfo.UserId,
			// AppLanguageHelper.getLaunguage(mContext));
			return mChunkBiz.getMasteredChunkList(AppConst.CurrUserInfo.UserId,
					AppLanguageHelper.getSystemLaunguage(mContext));
		} catch (Exception ex) {
			return null;
		}
	}

	// /**
	// * 在rehearse的chunk中搜索
	// *
	// * @param search
	// * @return
	// */
	// public List<Chunk> searchMasteredChunks(String search) {
	// try {
	// return mChunkBiz.searchMasterChunkList(search,
	// AppLanguageHelper.getLaunguage(mContext),
	// AppConst.CurrUserInfo.UserId);
	// } catch (Exception ex) {
	// return null;
	// }
	// }

	/**
	 * 在线版 在rehearse的chunk中搜索
	 * 
	 * @param search
	 * @return
	 */
	public List<Chunk> searchMasteredChunks(String search) {
		try {
			return mChunkBiz.searchMasterChunkList(search,
					AppLanguageHelper.getSystemLaunguage(mContext),
					AppConst.CurrUserInfo.UserId);
		} catch (Exception ex) {
			return null;
		}
	}

	// /**
	// * 在mastered的chunk中搜索
	// *
	// * @param search
	// * @return
	// */
	// public List<Chunk> searchRehearseChunks(String search) {
	// try {
	// return mChunkBiz.searchRehearseChunkList(search,
	// AppLanguageHelper.getLaunguage(mContext),
	// AppConst.CurrUserInfo.UserId,
	// ChunkBiz.REHEARSAL_R1_UNLOCK_TIME,
	// ChunkBiz.REHARSAL_R2_UNLOCK_TIME,
	// ChunkBiz.REHARSAL_R3_UNLOCK_TIME);
	// } catch (Exception ex) {
	// return null;
	// }
	// }

	/**
	 * 在线版 在mastered的chunk中搜索
	 * 
	 * @param search
	 * @return
	 */
	public List<Chunk> searchRehearseChunks(String search) {
		try {
			return mChunkBiz.searchRehearseChunkList(search,
					AppLanguageHelper.getSystemLaunguage(mContext),
					AppConst.CurrUserInfo.UserId,
					ChunkBiz.REHEARSAL_R1_UNLOCK_TIME,
					ChunkBiz.REHARSAL_R2_UNLOCK_TIME,
					ChunkBiz.REHARSAL_R3_UNLOCK_TIME);
		} catch (Exception ex) {
			return null;
		}
	}

	/**
	 * 更新Chunk状态
	 * 
	 * @param id
	 * @param status
	 * @return
	 */
	public boolean updateChunkStatus(String chunkId, int chunkstatus,
			int rehearseStatus, int score) {
		try {
			return mChunkBiz.SetChunkStatus(chunkId,
					AppConst.CurrUserInfo.UserId, chunkstatus, rehearseStatus,
					score, System.currentTimeMillis());
		} catch (Exception ex) {
			return false;
		}
	}

	/**
	 * Reheasal失败！
	 * 
	 * @param chunkId
	 * @return
	 */
	public boolean setRehearseFailed(String chunkId) {
		try {
			return mChunkBiz.setRehearseFailed(AppConst.CurrUserInfo.UserId,
					chunkId);
		} catch (Exception ex) {
			return false;
		}
	}

	/**
	 * 获得多选题
	 * 
	 * @param chunk
	 * @param isRehearsal
	 *            是Practice还是Rehearsal的chunk
	 * @return
	 */
	public List<MulityChoiceQuestions> getPraticeMultiChoiceList(Chunk chunk) {
		try {
			List<MulityChoiceQuestions> choiceList = new ArrayList<MulityChoiceQuestions>();
			if (chunk == null || chunk.getMulityChoiceQuestions() == null
					|| chunk.getMulityChoiceQuestions().size() == 0)
				return null;
			int index = 0;
			for (; index < (chunk.getMulityChoiceQuestions().size() > 3 ? 3
					: chunk.getMulityChoiceQuestions().size()); index++) {
				choiceList.add(chunk.getMulityChoiceQuestions().get(index));
			}
			return choiceList;
		} catch (Exception ex) {
			return null;
		}
	}

	/**
	 * 获得当前chunk可以rehearsal的多选题
	 * 
	 * @param chunk
	 * @return
	 */
	public MulityChoiceQuestions getRehearseMultiChoice(Chunk chunk) {
		try {
			if (chunk == null
					|| chunk.getMulityChoiceQuestions() == null
					|| chunk.getMulityChoiceQuestions().size() == 0){
				return null;
			}
			int multiChoiceSize = chunk.getMulityChoiceQuestions().size();
			switch (chunk.getRehearsalStatus()) {
			case ChunkBiz.REHEARSE_R1:
				return chunk.getMulityChoiceQuestions().get(
						multiChoiceSize > 3 ? 3 : multiChoiceSize - 1);
			case ChunkBiz.REHEARSE_R2:
				return chunk.getMulityChoiceQuestions().get(
						multiChoiceSize > 4 ? 4 : multiChoiceSize - 1);
			case ChunkBiz.REHARSE_R3:
				return chunk.getMulityChoiceQuestions().get(
						multiChoiceSize > 5 ? 5 : multiChoiceSize - 1);
			default:
				return null;
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}

	/**
	 * 获得Progress状态
	 * 
	 * @param uid
	 * @param chunkCode
	 * @return
	 */
	public UserProgressStatus getUserProgress(String chunkCode) {
		UserProgressStatusDao dao = new UserProgressStatusDao(mContext);
		return dao.getUserProgress(AppConst.CurrUserInfo.UserId, chunkCode);
	}

	/**
	 * 计算下一个chunk学习时间； 如果当前没有可以新学的chunk，需要计算下一个chunk解锁时间； NULL表示没有新chunk可以解锁了,
	 * 0表示当前就有可以learn的
	 * 
	 * @return
	 */
	public long getLearnAvailableTime() {
		return mChunkBiz.getLearnAvailableTime(AppConst.CurrUserInfo.UserId);
	}

	/**
	 * 计算下一个chunk rehearsal时间 Null表示没有可以rehearsal的了 0表示当前就有直接可以rehearsal的
	 * 
	 * @param uid
	 * @return
	 */
	public Long getRehearsalAvailableTime() {
		return mChunkBiz
				.getRehearsalAvailableTime(AppConst.CurrUserInfo.UserId);
	}
}
