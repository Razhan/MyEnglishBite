package com.ef.bite.ui.chunk;

import java.io.Serializable;
import java.util.ArrayList;

import android.app.ProgressDialog;
import com.ef.bite.R;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.ef.bite.AppConst;
import com.ef.bite.Tracking.ContextDataMode;
import com.ef.bite.Tracking.MobclickTracking;
import com.ef.bite.adapters.ChunkRehearseListAdapter;
import com.ef.bite.business.ChunkBLL;
import com.ef.bite.business.TutorialConfigBiz;
import com.ef.bite.business.action.RehearseChunkOpenAction;
import com.ef.bite.business.task.GetRehearsalListTask;
import com.ef.bite.business.task.PostExecuting;
import com.ef.bite.dataacces.ChunkLoader;
import com.ef.bite.dataacces.TutorialConfigSharedStorage;
import com.ef.bite.dataacces.mode.Chunk;
import com.ef.bite.dataacces.mode.httpMode.HttpRehearsalListRequest;
import com.ef.bite.dataacces.mode.httpMode.HttpRehearsalListResponse;
import com.ef.bite.ui.BaseActivity;
import com.ef.bite.utils.*;
import com.ef.bite.widget.ActionbarLayout;
import com.ef.bite.widget.SelectSwitcherLayout;

public class ChunkListActivity extends BaseActivity {
	private ChunkLoader chunkLoader;
	private ActionbarLayout mActionbar;
	private LinearLayout mListLayout;
	private SelectSwitcherLayout mSelectSwitcher; // 选择切换器
	// Rehearse Layout
	private LinearLayout mRehearseLayout;
	private TextView mRehearseAvailableText;
	private ImageButton mRehearseAvailableGo;
	private ListView mRehearseAvailableListView;
	private TextView mRehearseFutureText;
	private ListView mRehearseFutureListView;
	// Mastered Layout
	private LinearLayout mMarsteredLayout;
	private ListView mMasteredListView;
	private TextView mMasteredNoPhrase; // 没有任何master的消息提示

	private ProgressDialog progressDialog;
	private int rehearsalCount;
	private int masteredCount;

	ChunkBLL mChunkBLL;
	List<HttpRehearsalListResponse.courseInfo> rehearsalChunkList = new ArrayList<HttpRehearsalListResponse.courseInfo>();
	List<HttpRehearsalListResponse.courseInfo> mAvaiableRehearseChunkList;
	ChunkRehearseListAdapter mAvailableRehearseAdapter;
	List<HttpRehearsalListResponse.courseInfo> mFutureRehearseChunkList;
	ChunkRehearseListAdapter mFutureRehearseAdapter;
	List<HttpRehearsalListResponse.courseInfo> mMasteredChunkList;
	ChunkRehearseListAdapter mMasteredChunkAdapter;
	int type = CHUNK_LIST_TYPE_REHEARSE; // 0-Rehearse List; 1-Mastered List
	public final static int CHUNK_LIST_TYPE_REHEARSE = 0;
	public final static int CHUNK_LIST_TYPE_MASTERED = 1;

	private final static int VocabularylistTorehearseValues = 1;
	private final static int VocabularylistMasteredValues = 2;
	private final static int VocabularySearchValues = 3;
    private final static int SearchLocation_mastered = 4;
    private final static int SearchLocation_rehearse = 5;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_chunk_list);
		chunkLoader=new ChunkLoader(this);
		progressDialog = new ProgressDialog(this);
		progressDialog.setMessage(JsonSerializeHelper.JsonLanguageDeserialize(
				this, "loading_data"));
		mListLayout = (LinearLayout) findViewById(R.id.chunk_list_layout);
		mActionbar = (ActionbarLayout) findViewById(R.id.chunk_list_actionbar_layout);
		mSelectSwitcher = (SelectSwitcherLayout) findViewById(R.id.chunk_list_switcher);
		mRehearseLayout = (LinearLayout) findViewById(R.id.chunk_list_rehearse_layout);
		mRehearseAvailableText = (TextView) findViewById(R.id.chunk_list_rehearse_available_text);
		mRehearseAvailableGo = (ImageButton) findViewById(R.id.chunk_list_rehearse_available_go);
		mRehearseAvailableListView = (ListView) findViewById(R.id.chunk_list_rehearse_available_listview);
		mRehearseFutureText = (TextView) findViewById(R.id.chunk_list_rehearse_future_text);
		mRehearseFutureListView = (ListView) findViewById(R.id.chunk_list_rehearse_future_listview);
		mMarsteredLayout = (LinearLayout) findViewById(R.id.chunk_list_marstered_layout);
		mMasteredListView = (ListView) findViewById(R.id.chunk_list_marstered_listview);
		mMasteredNoPhrase = (TextView) findViewById(R.id.chunk_list_marstered_no_phrase);
		mRehearseAvailableGo.setOnClickListener(new ClickListener());

		// 字体设置
		FontHelper.applyFont(mContext, mRehearseAvailableText,
				FontHelper.FONT_Museo300);
		FontHelper.applyFont(mContext, mRehearseFutureText,
				FontHelper.FONT_Museo300);
		FontHelper.applyFont(mContext, mMasteredNoPhrase,
				FontHelper.FONT_Museo300);
		// 初始化
		if (mChunkBLL == null)
			mChunkBLL = new ChunkBLL(mContext);
		// mMasteredChunkList = mChunkBLL.getMasteredChunks();

		String chunk_list_actionbar_title = null;
		if (JsonSerializeHelper.JsonLanguageDeserialize(mContext,
				"chunk_list_actionbar_title") != null) {
			chunk_list_actionbar_title = JsonSerializeHelper
					.JsonLanguageDeserialize(mContext,
							"chunk_list_actionbar_title");
		} else {
			chunk_list_actionbar_title = getResources().getString(
					R.string.chunk_list_actionbar_title);
		}
		// 初始化Actionbar
		mActionbar.initiWithTitle(chunk_list_actionbar_title,
		 R.drawable.arrow_goback_black, R.drawable.search_black,
//				R.drawable.arrow_goback_black, -1,
				new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						finish();
					}

				}, new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						// 搜索

                        if (type == CHUNK_LIST_TYPE_REHEARSE) {
                            BI_Tracking(SearchLocation_rehearse);
                        } else {
                            BI_Tracking(SearchLocation_mastered);
                        }

						startActivity(new Intent(ChunkListActivity.this, ChunkListSearchActivity.class)
								.putExtra(AppConst.BundleKeys.LIST_REHEARSAL, (Serializable) mAvaiableRehearseChunkList));
//								.putExtra(AppConst.BundleKeys.LIST_MASTERED, (Serializable) mMasteredChunkList));
//						mListLayout.setVisibility(View.GONE);
//						mSearchLayout.setVisibility(View.VISIBLE);
//						initializeSearch();
						BI_Tracking(VocabularySearchValues);
					}
				});
		if (mMasteredChunkList != null && mMasteredChunkList.size() > 0) {
			type = CHUNK_LIST_TYPE_MASTERED;
		} else
			type = CHUNK_LIST_TYPE_REHEARSE;

		String chunk_list_to_rehearse = null;
		if (JsonSerializeHelper.JsonLanguageDeserialize(mContext,
				"profile_phrasses").replace("%1$d", "") != null) {
			chunk_list_to_rehearse = JsonSerializeHelper
					.JsonLanguageDeserialize(mContext, "chunk_list_learned")
					.replace("%1$d", "");
		} else {
			chunk_list_to_rehearse = JsonSerializeHelper
					.JsonLanguageDeserialize(mContext, "chunk_list_learned")
					.replace("%1$d", "");
		}

		String chunk_list_mastered = null;
		if (JsonSerializeHelper.JsonLanguageDeserialize(mContext,
				"chunk_list_mastered").replace("%1$d", "") != null) {
			chunk_list_mastered = JsonSerializeHelper.JsonLanguageDeserialize(
					mContext, "chunk_list_mastered").replace("%1$d", "");
		} else {
			chunk_list_mastered = JsonSerializeHelper.JsonLanguageDeserialize(
					mContext, "chunk_list_mastered").replace("%1$d", "");
		}

		// 初始化选择切换器
		mSelectSwitcher.initializeWithText(chunk_list_to_rehearse, "0",
				chunk_list_mastered, "0",
				type == CHUNK_LIST_TYPE_REHEARSE ? true : false,
				new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						if (type == CHUNK_LIST_TYPE_MASTERED) {
							type = CHUNK_LIST_TYPE_REHEARSE;
							initView();
							BI_Tracking(VocabularylistTorehearseValues);
						}
					}
				}, new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						if (type == CHUNK_LIST_TYPE_REHEARSE) {
							type = CHUNK_LIST_TYPE_MASTERED;
							initView();
							BI_Tracking(VocabularylistMasteredValues);
						}
						openGuide();
					}
				});


	}

	protected void onResume() {
		super.onResume();
		getRehearsalList();
		if (type == CHUNK_LIST_TYPE_REHEARSE
				&& AppConst.GlobalConfig.TutorialConfig) {
			BI_Tracking(VocabularylistTorehearseValues);
		}
		if (type == CHUNK_LIST_TYPE_MASTERED
				&& AppConst.GlobalConfig.TutorialConfig) {
			BI_Tracking(VocabularylistMasteredValues);
		}
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
//		MobclickTracking.UmengTrack
//				.setPageEnd(
//						ContextDataMode.VocabularylistTorehearseValues.pageNameValue,
//						ContextDataMode.VocabularylistTorehearseValues.pageSiteSubSectionValue,
//						ContextDataMode.VocabularylistTorehearseValues.pageSiteSectionValue,
//						mContext);
//		MobclickTracking.UmengTrack
//				.setPageEnd(
//						ContextDataMode.VocabularylistMasteredValues.pageNameValue,
//						ContextDataMode.VocabularylistMasteredValues.pageSiteSubSectionValue,
//						ContextDataMode.VocabularylistMasteredValues.pageSiteSectionValue,
//						mContext);
	}

	class ClickListener implements View.OnClickListener {
		@Override
		public void onClick(View v) {
			if (v.getId() == mRehearseAvailableGo.getId()) {
				if (rehearsalChunkList != null
						&& rehearsalChunkList.size() > 0) {
							openChunkRehearsalAct(rehearsalChunkList);
				} else {
					Toast.makeText(
							mContext,
							JsonSerializeHelper.JsonLanguageDeserialize(
									mContext, "home_screen_no_rehearse_chunks"),
							Toast.LENGTH_SHORT).show();
				}
			}
		}
	}

	private void initView() {
		// mAvaiableRehearseChunkList = mChunkBLL.getRehearseChunkList();
		// mFutureRehearseChunkList = mChunkBLL.getFutrueRehearseChunks();

		mAvailableRehearseAdapter = new ChunkRehearseListAdapter(this,
				mAvaiableRehearseChunkList, false);
		mFutureRehearseAdapter = new ChunkRehearseListAdapter(this,
				mFutureRehearseChunkList, false);
		// mMasteredChunkList = mChunkBLL.getMasteredChunks();
		mMasteredChunkAdapter = new ChunkRehearseListAdapter(this,
				mMasteredChunkList, true);
//		mSelectSwitcher.updateText(Integer
//				.toString(mAvaiableRehearseChunkList == null ? 0
//						: mAvaiableRehearseChunkList.size()), Integer
//				.toString(mMasteredChunkList == null ? 0 : mMasteredChunkList
//						.size()));
		mSelectSwitcher.updateText(Integer
				.toString(rehearsalCount), Integer
				.toString(masteredCount));
		if (mAvaiableRehearseChunkList == null
				|| mAvaiableRehearseChunkList.size() == 0) {
			mRehearseAvailableGo
					.setBackgroundResource(R.drawable.button_go_ahead_diable);
			mRehearseAvailableGo.setEnabled(false);
		} else {
			mRehearseAvailableGo
					.setBackgroundResource(R.drawable.button_go_ahead_enable);
			mRehearseAvailableGo.setEnabled(true);
		}

		if (type == CHUNK_LIST_TYPE_REHEARSE) { // Rehearse List
			if (mAvaiableRehearseChunkList == null) {
				if (JsonSerializeHelper.JsonLanguageDeserialize(mContext,
						"chunk_list_rehearse_available_text") != null) {
					mRehearseAvailableText.setText(String.format(
							JsonSerializeHelper.JsonLanguageDeserialize(
									mContext,
									"chunk_list_rehearse_available_text"), 0));
				} else {
					mRehearseAvailableText
							.setText(String
									.format(getResources()
											.getString(
													R.string.chunk_list_rehearse_available_text),
											0));
				}

			} else {
				if (JsonSerializeHelper.JsonLanguageDeserialize(mContext,
						"chunk_list_rehearse_available_text") != null) {
					mRehearseAvailableText.setText(String.format(
							JsonSerializeHelper.JsonLanguageDeserialize(
									mContext,
									"chunk_list_rehearse_available_text"),
							rehearsalChunkList.size()));
				} else {
					mRehearseAvailableText
							.setText(String
									.format(getResources()
													.getString(
															R.string.chunk_list_rehearse_available_text),
											rehearsalChunkList.size()));
				}

			}
			if (mFutureRehearseChunkList == null) {
				if (JsonSerializeHelper.JsonLanguageDeserialize(mContext,
						"chunk_list_rehearse_future_text") != null) {
					mRehearseFutureText.setText(String.format(
							JsonSerializeHelper
									.JsonLanguageDeserialize(mContext,
											"chunk_list_rehearse_future_text"),
							0));
				} else {
					mRehearseFutureText.setText(String.format(
							getResources().getString(
									R.string.chunk_list_rehearse_future_text),
							0));
				}

			} else {
				if (JsonSerializeHelper.JsonLanguageDeserialize(mContext,
						"chunk_list_rehearse_future_text") != null) {
					mRehearseFutureText.setText(String.format(
							JsonSerializeHelper
									.JsonLanguageDeserialize(mContext,
											"chunk_list_rehearse_future_text"),
							mFutureRehearseChunkList.size()));
				} else {
					mRehearseFutureText.setText(String.format(
							getResources().getString(
									R.string.chunk_list_rehearse_future_text),
							mFutureRehearseChunkList.size()));
				}

			}
			
			mRehearseLayout.setVisibility(View.VISIBLE);
			mMarsteredLayout.setVisibility(View.GONE);
			mRehearseAvailableListView.setAdapter(mAvailableRehearseAdapter);
			mRehearseFutureListView.setAdapter(mFutureRehearseAdapter);
			mSelectSwitcher.setBackgroundColor(getResources().getColor(
					R.color.bella_chunk_background));
			mRehearseAvailableListView
					.setOnItemClickListener(new OnItemClickListener() {
						@Override
						public void onItemClick(AdapterView<?> parent,
								View view, int position, long id) {
							openChunkDetailAct(mAvaiableRehearseChunkList
									.get(position));
						}
					});
			
			mRehearseFutureListView
					.setOnItemClickListener(new OnItemClickListener() {
						@Override
						public void onItemClick(AdapterView<?> parent,
								View view, int position, long id) {
							openChunkDetailAct(mFutureRehearseChunkList
									.get(position));

						}
					});
		} else { // Mastered List
			mRehearseLayout.setVisibility(View.GONE);
			mMarsteredLayout.setVisibility(View.VISIBLE);
			mMasteredListView.setAdapter(mMasteredChunkAdapter);
			if (mMasteredChunkList == null || mMasteredChunkList.size() == 0) {
				mMasteredNoPhrase.setVisibility(View.VISIBLE);
				mMasteredNoPhrase.setText(JsonSerializeHelper
						.JsonLanguageDeserialize(mContext,
								"chunk_list_master_no_phrase"));
				mSelectSwitcher.setBackgroundColor(getResources().getColor(
						R.color.bella_chunk_background));
			} else {
				mMasteredNoPhrase.setVisibility(View.GONE);
				mSelectSwitcher.setBackgroundColor(getResources().getColor(
						R.color.white));
			}
			mMasteredListView.setOnItemClickListener(new OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					openChunkDetailAct(mMasteredChunkList
							.get(position));

				}
			});
		}
	}

	private void getRehearsalList() {

		progressDialog.show();
		HttpRehearsalListRequest request = new HttpRehearsalListRequest();
		request.course_source_culture_code = "en";
		GetRehearsalListTask task = new GetRehearsalListTask(this,
				new PostExecuting<HttpRehearsalListResponse>() {
					@Override
					public void executing(HttpRehearsalListResponse response) {
						progressDialog.dismiss();
						if (response != null
								&& StringUtils.isEquals(response.status, "0")) {
							masteredCount = response.data.mastered_count;
							rehearsalCount = response.data.in_progress_count + response.data.mastered_count;
							rehearsalChunkList.clear();
							rehearsalChunkList.addAll(response.data.available_now_courses);
							mRehearseAvailableText.setText(String.format(
									JsonSerializeHelper.JsonLanguageDeserialize(
											mContext,
											"chunk_list_rehearse_available_text"),
									rehearsalChunkList.size()));

							mAvaiableRehearseChunkList = response.data.available_now_courses;
							mAvaiableRehearseChunkList.addAll(response.data.available_future_courses);
							mAvaiableRehearseChunkList.addAll(response.data.mastered_courses);
//							mFutureRehearseChunkList = response.data.available_future_courses;
							mMasteredChunkList = response.data.mastered_courses;
							initView();
						}
					}
				});
		task.execute(request);
	}

	/**
	 * convert RehearsalListResponse to RehearsalList
	 * 
	 * @param list
	 */
	private void openChunkRehearsalAct(
			List<HttpRehearsalListResponse.courseInfo> list) {
		if (list == null) {
			return;
		}

		final List<ChunkLoader.Request> requests =new ArrayList<ChunkLoader.Request>();
		for (HttpRehearsalListResponse.courseInfo info : list) {
			ChunkLoader.Request request =new ChunkLoader.Request();
			request.setChunkID(info.course_id);
			request.setUrl(info.course_package_url);
			request.setVersion(info.course_version);
			requests.add(request);
		}

		chunkLoader.load(requests,
				new ChunkLoader.OnFinishListener() {
					@Override
					public void doOnFinish(boolean isDone) {
						List<Chunk> chunks = chunkLoader.getChunkList(requests);
						if (chunks != null) {
							new RehearseChunkOpenAction().open(ChunkListActivity.this, chunks);
						} else {
							if (NetworkChecker.isConnected(mContext)) {
								Toast.makeText(
										mContext,
										JsonSerializeHelper.JsonLanguageDeserialize(
												mContext, "record_msg_no_course"),
										Toast.LENGTH_SHORT).show();
							} else {
								Toast.makeText(
										mContext,
										JsonSerializeHelper.JsonLanguageDeserialize(
												mContext, "error_check_network_available"),
										Toast.LENGTH_SHORT).show();
							}

						}
					}
				});
	}


	private void openChunkDetailAct(final HttpRehearsalListResponse.courseInfo courseInfo){
		chunkLoader.load(new ChunkLoader.Request(
						courseInfo.course_package_url,
						courseInfo.course_id,
						courseInfo.course_version),
				new ChunkLoader.OnFinishListener() {
					@Override
					public void doOnFinish(boolean isDone) {
						Chunk chunk = chunkLoader.getChunk(courseInfo.course_id);
						if (chunk != null) {
							Intent intent = new Intent(mContext,
									ChunkLearnDetailActivity.class);
							intent.putExtra(
									AppConst.BundleKeys.Is_Chunk_Learning,
									false);
							intent.putExtra(AppConst.BundleKeys.Chunk, chunk);
							startActivity(intent);

						} else {
							if (NetworkChecker.isConnected(mContext)) {
								Toast.makeText(
										mContext,
										JsonSerializeHelper.JsonLanguageDeserialize(
												mContext, "record_msg_no_course"),
										Toast.LENGTH_SHORT).show();
							} else {
								Toast.makeText(
										mContext,
										JsonSerializeHelper.JsonLanguageDeserialize(
												mContext, "error_check_network_available"),
										Toast.LENGTH_SHORT).show();
							}

						}
					}
				});
	}



	@Override
	protected void BI_Tracking(int i) {
		// TODO Auto-generated method stub
		super.BI_Tracking(i);

		switch (i) {
		case 1:
			MobclickTracking.OmnitureTrack
					.AnalyticsTrackState(
							ContextDataMode.VocabularylistTorehearseValues.pageNameValue,
							ContextDataMode.VocabularylistTorehearseValues.pageSiteSubSectionValue,
							ContextDataMode.VocabularylistTorehearseValues.pageSiteSectionValue,
							mContext);
//			MobclickTracking.UmengTrack
//					.setPageStart(
//							ContextDataMode.VocabularylistTorehearseValues.pageNameValue,
//							ContextDataMode.VocabularylistTorehearseValues.pageSiteSubSectionValue,
//							ContextDataMode.VocabularylistTorehearseValues.pageSiteSectionValue,
//							mContext);
			break;

		case 2:
			MobclickTracking.OmnitureTrack
					.AnalyticsTrackState(
							ContextDataMode.VocabularylistMasteredValues.pageNameValue,
							ContextDataMode.VocabularylistMasteredValues.pageSiteSubSectionValue,
							ContextDataMode.VocabularylistMasteredValues.pageSiteSectionValue,
							mContext);
//			MobclickTracking.UmengTrack
//					.setPageStart(
//							ContextDataMode.VocabularylistMasteredValues.pageNameValue,
//							ContextDataMode.VocabularylistMasteredValues.pageSiteSubSectionValue,
//							ContextDataMode.VocabularylistMasteredValues.pageSiteSectionValue,
//							mContext);
			break;
		case 3:
			MobclickTracking.OmnitureTrack
					.AnalyticsTrackState(
							ContextDataMode.VocabularySearchValues.pageNameValue,
							ContextDataMode.VocabularySearchValues.pageSiteSubSectionValue,
							ContextDataMode.VocabularySearchValues.pageSiteSectionValue,
							mContext);
			break;
        case 4:
            MobclickTracking.OmnitureTrack.ActionSearchLocation(1);
            break;
        case 5:
            MobclickTracking.OmnitureTrack.ActionSearchLocation(0);
            break;
		}
	}

	private void openGuide(){
		TutorialConfigBiz tutorialBiz = new TutorialConfigBiz(mContext);
		tutorialBiz.interrupt(TutorialConfigBiz.TUTORIAL_TYPE_MASTER_CHUNK);

		try {
			TutorialConfigSharedStorage tutorialConfigSharedStorage = new TutorialConfigSharedStorage(
					mContext, AppConst.CurrUserInfo.UserId);
			if (tutorialConfigSharedStorage.get().Tutorial_Master_Chunk) {
				AppConst.GlobalConfig.TutorialConfig = true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
