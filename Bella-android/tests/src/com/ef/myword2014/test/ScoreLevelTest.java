package com.ef.myword2014.test;

import org.junit.Assert;
import org.junit.Test;

import com.ef.myword2014.business.UserScoreBiz;
import com.ef.myword2014.utils.ScoreLevelHelper;

import android.test.AndroidTestCase;

public class ScoreLevelTest extends AndroidTestCase  {
	
	@Test
	public void test(){
//		int level0=0, level1 = 1, level2=2, level3=3, level4=4, level25=25;
//		int score0=0, score0_1 = 1, score1=105, score2=253, score3=510, score4=1509, score25=141958;
//		int t_level0 = ScoreLevelHelper.getLevel(score0);
//		int t_level0_1 = ScoreLevelHelper.getLevel(score0_1);
//		int t_level1 = ScoreLevelHelper.getLevel(score1);
//		int t_level2 = ScoreLevelHelper.getLevel(score2);
//		int t_level3= ScoreLevelHelper.getLevel(score3);
//		int t_level4 = ScoreLevelHelper.getLevel(score4);
//		int t_level25 = ScoreLevelHelper.getLevel(score25);
		
//		Assert.assertTrue(t_level0 == level0);
//		Assert.assertTrue(t_level0_1 == level0);
//		Assert.assertTrue(t_level1 == level1);
//		Assert.assertTrue(t_level2 == level2);
//		Assert.assertTrue(t_level3 == level3);
//		Assert.assertTrue(t_level4 == level4);
//		Assert.assertTrue(t_level25 == level25);
	}
	
	@Test
	public void testScoreUp(){
		UserScoreBiz biz = new UserScoreBiz(mContext);
		int score = biz.getUserScore("1");
		int added = 56;
		biz.increaseScore(added, "1");
		int newscore = biz.getUserScore("1");
		Assert.assertTrue(score+added == newscore );
	}
}
