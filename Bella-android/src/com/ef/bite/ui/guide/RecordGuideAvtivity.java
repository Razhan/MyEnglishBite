package com.ef.bite.ui.guide;

import android.widget.ImageView;
import android.widget.TextView;
import com.ef.bite.R;
import com.ef.bite.Tracking.ContextDataMode;
import com.ef.bite.Tracking.MobclickTracking;

/**
 * Created by yang on 15/3/27.
 */
public class RecordGuideAvtivity extends BaseGuideActivity {

    @Override
    void setScreenView(ImageView screen) {
        screen.setBackgroundResource(R.drawable.guide_record);
        tracking();
    }

    @Override
    void setDescView(TextView DescView) {

    }

    private void tracking(){
        MobclickTracking.OmnitureTrack.AnalyticsTrackState(
                ContextDataMode.RecordGuideValues.pageNameValue,
                ContextDataMode.RecordGuideValues.pageSiteSubSectionValue,
                ContextDataMode.RecordGuideValues.pageSiteSectionValue, this);
    }
}
