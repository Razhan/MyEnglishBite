package com.ef.bite.ui.preview;

import java.util.ArrayList;
import java.util.List;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.ef.bite.AppConst;
import com.ef.bite.R;
import com.ef.bite.adapters.CoursePreviewListAdapter;
import com.ef.bite.business.task.DownloadCoursesTask;
import com.ef.bite.business.task.GetCoursePreviewTask;
import com.ef.bite.business.task.PostExecuting;
import com.ef.bite.dataacces.mode.Chunk;
import com.ef.bite.dataacces.mode.httpMode.HttpCourseRequest;
import com.ef.bite.dataacces.mode.httpMode.HttpCourseResponse;
import com.ef.bite.ui.BaseActivity;
import com.ef.bite.ui.chunk.ChunkLearnActivity;
import com.ef.bite.utils.AppLanguageHelper;
import com.ef.bite.widget.ActionbarLayout;

/**
 * Created by yang on 15/2/5.
 */
public class CoursePreviewListActivity extends BaseActivity {
	private ActionbarLayout mActionbar;
	private ListView listView;
	private CoursePreviewListAdapter listAdapter;
	private ProgressDialog progress;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_course_preview_list);
		sertupViews();
	}

	private void sertupViews() {
		progress = new ProgressDialog(this);
		List<String> idList = getIntent().getStringArrayListExtra(
				AppConst.BundleKeys.Course_id_list);
		mActionbar = (ActionbarLayout) findViewById(R.id.preview_actionbar);
		mActionbar.initiWithTitle("Course List", R.drawable.arrow_goback_black,
				-1, new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						finish();
					}
				}, null);
		getCourses(buildRequest(idList));

	}

	/**
	 * Load course list
	 * 
	 * @param requestList
	 */
	private void getCourses(List<HttpCourseRequest> requestList) {
		progress.setMessage("Loading course list...");
		progress.show();
		GetCoursePreviewTask getCoursePreviewTask = new GetCoursePreviewTask(
				this, requestList, new PostExecuting<HttpCourseResponse>() {
					@Override
					public void executing(HttpCourseResponse result) {
						if (result != null && result.status != null
								&& result.status.equals("0")
								&& result.data != null) {
							setupListView(result);
						}
						progress.dismiss();
					}
				});
		getCoursePreviewTask.execute();
	}

	/**
	 * assemble course request
	 * 
	 * @param list
	 * @return
	 */
	private List<HttpCourseRequest> buildRequest(List<String> list) {
		List<HttpCourseRequest> requestList = new ArrayList<HttpCourseRequest>();
		for (String id : list) {
			HttpCourseRequest reques = new HttpCourseRequest();
			reques.app_version = AppConst.GlobalConfig.App_Version;
			reques.course_id = id;
			reques.system = AppConst.GlobalConfig.OS;
			reques.source_language = AppLanguageHelper
					.getSystemLaunguage(mContext);// 本地语言
			reques.target_language = AppLanguageHelper.EN;// 目标语言默认为英语
			requestList.add(reques);
		}
		return requestList;
	}

	/**
	 * setup ListView
	 * 
	 * @param courseResponses
	 */
	private void setupListView(HttpCourseResponse courseResponses) {
		listView = (ListView) findViewById(R.id.preview_listview);
		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				if (view.getTag() == null) {
					return;
				}
				getChunkResource((HttpCourseResponse.HttpCourseData) view
						.getTag());
			}
		});
		listAdapter = new CoursePreviewListAdapter(this, courseResponses.data);
		listView.setAdapter(listAdapter);
	}

	/**
	 * get chunk resource
	 * 
	 * @param courseData
	 */
	private void getChunkResource(HttpCourseResponse.HttpCourseData courseData) {
		progress.setMessage("Loading course resource...");
		progress.show();
		DownloadCoursesTask task = new DownloadCoursesTask(this, courseData,
				new PostExecuting<Chunk>() {
					@Override
					public void executing(Chunk chunk) {
						if (chunk != null) {
							// new
							// NewChunkOpenAction().open(CoursePreviewListActivity.this,
							// chunk);
							previewChunk(chunk);
						} else {
							Toast.makeText(
									CoursePreviewListActivity.this,
									"No resource found or resource parse occurs error",
									Toast.LENGTH_SHORT).show();
						}
						progress.dismiss();
					}
				});
		task.execute();
	}

	/**
	 * open ChunkLearnActivity
	 * 
	 * @param chunk
	 */
	private void previewChunk(Chunk chunk) {
		Intent intent = new Intent(mContext, ChunkLearnActivity.class);
		intent.putExtra(AppConst.BundleKeys.Is_Chunk_For_Preview, true);
		intent.putExtra(AppConst.BundleKeys.Chunk, chunk);
		startActivityForResult(intent, AppConst.RequestCode.CHUNK_PREVIEW);
	}

	// protected Serializable getSerializableExtra(String value) {
	// Bundle bundle = getIntent().getExtras();
	// if (bundle == null) {
	// return null;
	// }
	// return bundle.getSerializable(value);
	// }
}
