package com.ef.bite.business;

import android.content.Context;
import com.ef.bite.AppConst;
import com.ef.bite.dataacces.ProfileCache;
import com.ef.bite.dataacces.dao.UserScoreDao;

public class UserScoreBiz {
    ProfileCache profileCache;
    UserScoreDao userScoreDao;

    public UserScoreBiz(Context context) {
        userScoreDao = new UserScoreDao(context);
        profileCache = new ProfileCache(context);
    }

    /**
     * 获得用户总积分
     *
     * @return
     */
    public int getUserScore() {
        profileCache.loadUserProfile();
        return AppConst.CurrUserInfo.Score;
//		try{
//			UserScore userScore= userScoreDao.getUserScore(uid);
//		    if(userScore==null){
//		    	userScore=new UserScore();
//		    	userScore.setCreateTime(new Date().getTime());
//		    	userScore.setIsSyncWithServer(1);
//		    	userScore.setScore(0);
//		    	userScore.setSyncTime(0);
//		    	userScore.setUid(uid);
//		    	userScoreDao.insertUserScore(userScore);
//		    	return 0;
//		    }else
//		    	return userScore.getScore();
//		}catch(Exception ex){
//			ex.printStackTrace();
//			return 0;
//		}
    }

    /**
     * 添加分数
     *
     * @param score
     * @return
     */
    public Boolean increaseScore(int score) {
        AppConst.CurrUserInfo.Score = AppConst.CurrUserInfo.Score + score;
        profileCache.save();
//	    UserScore userScore= userScoreDao.getUserScore(uid);
//	    if(userScore==null){
//	    	userScore=new UserScore();
//	    	userScore.setCreateTime(new Date().getTime());
//	    	userScore.setIsSyncWithServer(1);
//	    	userScore.setScore(score);
//	    	userScore.setSyncTime(0);
//	    	userScore.setUid(uid);
//	    	userScoreDao.insertUserScore(userScore);
//	    	return true;
//	    }
//	    userScore.setScore(userScore.getScore()+score);
//	    userScoreDao.upDateUserScore(userScore.getScore(), uid);
        return true;
    }


}
