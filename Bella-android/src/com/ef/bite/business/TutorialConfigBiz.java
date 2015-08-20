package com.ef.bite.business;

import android.content.Context;
import android.content.Intent;

import com.ef.bite.AppConst;
import com.ef.bite.dataacces.TutorialConfigSharedStorage;
import com.ef.bite.model.TutorialConfig;
import com.ef.bite.ui.guide.WalkthroughActivity;

public class TutorialConfigBiz {
	public final static int TUTORIAL_TYPE_WALKTHROUGH = 0;
	public final static int TUTORIAL_TYPE_LERN_CHUNK = 1;
	public final static int TUTORIAL_TYPE_HOME_SCREEN = 2;
	public final static int TUTORIAL_TYPE_REHEARSE_CHUNK = 3;
	public final static int TUTORIAL_TYPE_MASTER_CHUNK = 4;

	Context mContext;
	TutorialConfigSharedStorage mStorage;

	public TutorialConfigBiz(Context context) {
		mContext = context;
		mStorage = new TutorialConfigSharedStorage(context,
				AppConst.CurrUserInfo.UserId);
	}

	/**
	 * 拦截显示Tutorial
	 * 
	 * @param tutorialType
	 */
	public boolean interrupt(int tutorialType) {
		boolean is_show = false;
		TutorialConfig tutorialConfig = mStorage.get();
		if (tutorialConfig == null)
			is_show = true;
		else {
			switch (tutorialType) {
			case TUTORIAL_TYPE_LERN_CHUNK:
				if (!tutorialConfig.Tutorial_Learn_Chunk)
					is_show = true;
				break;
				
			case TUTORIAL_TYPE_HOME_SCREEN:
				if (!tutorialConfig.Tutorial_Home_Screen)
					is_show = true;
				break;
				
			case TUTORIAL_TYPE_REHEARSE_CHUNK:
				if (!tutorialConfig.Tutorial_Rehearse_Chunk)
					is_show = true;
				break;
				
			case TUTORIAL_TYPE_MASTER_CHUNK:
				if (!tutorialConfig.Tutorial_Master_Chunk)
					is_show = true;
				break;
			}
		}

		if (is_show) {
			Intent intent = new Intent(mContext, WalkthroughActivity.class);
			intent.putExtra(AppConst.BundleKeys.Is_In_Tutorial_Model, true);
			intent.putExtra(AppConst.BundleKeys.Tutorial_TYPE, tutorialType);
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			mContext.startActivity(intent);
		}
		return is_show;
	}

}
