package com.ef.bite.utils;

/**
 * 积分和等级的计算helper类
 *
 */
public class ScoreLevelHelper {
	
	/** 升级的因子 */
	public final static int LEVEL_FACTOR = 100;
	/**  **/
	public final static double LEVEL_FACTOR_X = 1.3;
	
	public final static double LEVEL_FACTOR_Y = 2.51;
	
	public final static double LEVEL_FACTOR_Z = 3.5;
	
	public final static int LEVEL_MAX = 500;
	
	public final static float LEVEL_FACTOR_FAST = 1;
	
	public final static float LEVEL_FACTOR_NORMAL = 0.8f;
	
	public final static float LEVEL_FACTOR_SLOW = 0.7f;
	
	public final static float LEVEL_FACTOR_SECOND_TRY = 0.3f;
	
	public final static float LEVEL_fACTOR_THIRD_TRY = 0.05f;
	
	/**
	 *  获得chunk的infomation
	 *  解锁一个chunk？ 
	 **/
	public final static int MAXIMUN_SCORE_CHUNK_INFO = 10;
	
	/** practice 初级  **/
	public final static int MAXIMUN_SCORE_PRACTICE_FORM = 50;
	/** practice 中级**/
	public final static int MAXIMUN_SCORE_PRACTICE_MEAN = 50;
	/** practice 高级 **/
	public final static int MAXIMUN_SCORE_PRACTICE_USE = 50;
	/** Rehearse 初级**/
	public final static int MAXIMUN_SCORE_REHEARSE_FORM = 50;
	/** Rehearse 中级**/
	public final static int MAXIMUN_SCORE_REHEARSE_MEAN = 50;
	/** Rehearse 高级**/
	public final static int MAXIMUN_SCORE_REHEARSE_USE = 50;
	/** Master一个chunk**/
	public final static int MAXIMUN_SCORE_REHEARSE_MASTERED = 50;
	/** Rehearse Old Question**/
	public final static int MAXIMUN_SCORE_REHEARSE_OLD_QUESTIONS = 50;

	/**
	 * 得到当前等级
	 * @param score
	 * @return
	 */
	private static int getLevel(int score){
		if(score == 0)
			return 0;
		for (int i = 0; i < LEVEL_MAX; i++) {
			if(getScoreByLevel(i)>score){
				return i-1;
			}else if(((i*(i+1))/2) * LEVEL_FACTOR==score){
				return i;
			}
		}
		return 0;
	}

	/**
	 * 获得外在显示的level
	 * @param score
	 * @return
	 */
	public static int getDisplayLevel(int score){
		return getLevel(score)+1;
	}
	
	/**
	 * 得到离升级还差多少分
	 * @param score
	 * @return
	 */
	public static int getLevelUpScore(int score){
			int nextlevel=getLevel(score)+1;
				return  getScoreByLevel(nextlevel)-score;
	}
	
	/**
	 * 获得达到某个级别需要的分数
	 * @param level
	 * @return
	 */
	private static int getScoreByLevel(int level){
		if(level == 0 )
			return 0;
		return (int) Math.floor(Math.pow(level+LEVEL_FACTOR_Y, LEVEL_FACTOR_Z) * LEVEL_FACTOR_X);
	}
	
	/**
	 * 得到当前级别的分数
	 * @param score
	 * @return
	 */
	public static int getCurrentLevelScore(int score){
		int level=getLevel(score);
		return  getScoreByLevel(level+1) - getScoreByLevel(level);
	}
	
	/**
	 * 获得当前级别超出的分数
	 * @param score
	 * @return
	 */
	public static int getCurrentLevelExistedScore(int score){
		if(score == 0)
			return 0;
		int level=getLevel(score);
		return score - getScoreByLevel(level);
	}
	
	/**
	 * 计算多选题获得的分数
	 * @param maxScore 一题满分得分
	 * @param totalTime  总共限制的时间， 单位秒
	 * @param costTime 所花费的时间， 单位秒
	 * @param tryTimes  第几次尝试
	 * @return
	 */
	public static int getMultiChoiceScore(int maxScore, long totalTime, long costTime, int tryTimes )
	{
		if(tryTimes <= 1){  // 第一次
			float costTimeRate = (float)costTime/(float)totalTime;
			if(costTimeRate <= 0.333){
				// fast
				return (int)(maxScore * LEVEL_FACTOR_FAST);
			}else if(costTimeRate>0.333 && costTimeRate < 0.666){
				// normal
				return (int)(maxScore * LEVEL_FACTOR_NORMAL);
			}else{
				// slow
				return (int)(maxScore * LEVEL_FACTOR_SLOW);
			}
		}else if(tryTimes == 2){ // 第二次
			return (int)(maxScore * LEVEL_FACTOR_SECOND_TRY);
		}else{ // 多于或等于3次
			return (int)(maxScore * LEVEL_fACTOR_THIRD_TRY);
		}
	}
}
