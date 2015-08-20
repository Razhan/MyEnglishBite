package com.ef.bite.dataacces.mode.httpMode;

import com.ef.bite.AppConst;

public class HttpRehearsalListRequest extends HttpBaseMessage {

    public String bella_id = AppConst.CurrUserInfo.UserId;

    public String system = AppConst.GlobalConfig.OS;

    public String course_source_culture_code = AppConst.GlobalConfig.Language;

    public String course_target_culture_code = "en";

}
