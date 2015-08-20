package com.ef.bite.business;

import java.util.Date;

import android.content.Context;

import com.ef.bite.dataacces.dao.TraceModeDao;
import com.ef.bite.dataacces.mode.TraceMode;

public class TraceBiz {

	TraceModeDao traceModeDao;

	public TraceBiz(Context context) {
		traceModeDao = new TraceModeDao(context);
	}

	public boolean insertTrace(String uid, String groupId, String even,
			String value) {
		TraceMode traceMode = new TraceMode();
		traceMode.setCreateTime(new Date().getTime());
		traceMode.setEvent(even);
		traceMode.setIsSyncWithServer(1);
		traceMode.setSynctime(0);
		traceMode.setUid(uid);
		traceMode.setGroupId(groupId);
		traceMode.setValue(value);

		traceModeDao.insertTrace(traceMode);
		return true;
	}

	public boolean updateTrace(Integer id) {
		traceModeDao.updateTrace(id);
		return true;
	}
}
