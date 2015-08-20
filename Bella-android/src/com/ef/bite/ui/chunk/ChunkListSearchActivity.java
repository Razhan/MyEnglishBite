package com.ef.bite.ui.chunk;

import java.util.ArrayList;

import android.content.Intent;
import android.widget.*;
import com.ef.bite.R;
import java.util.List;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView.OnItemClickListener;

import com.ef.bite.AppConst;
import com.ef.bite.Tracking.MobclickTracking;
import com.ef.bite.adapters.ChunkRehearseListAdapter;
import com.ef.bite.business.ChunkBLL;
import com.ef.bite.dataacces.ChunkLoader;
import com.ef.bite.dataacces.mode.Chunk;
import com.ef.bite.dataacces.mode.httpMode.HttpRehearsalListResponse;
import com.ef.bite.dataacces.mode.httpMode.HttpRehearsalListResponse.courseInfo;
import com.ef.bite.ui.BaseActivity;
import com.ef.bite.utils.JsonSerializeHelper;
import com.ef.bite.utils.NetworkChecker;

public class ChunkListSearchActivity extends BaseActivity {
	private ChunkLoader chunkLoader;
	// Action Bar
	private EditText mSearchText;
	private ImageView mSearchCancel;
	private Button mSearchButton;

	private TextView mSearchRehearseText;
	private TextView mSearchMasteredText;
	private ListView mSearchRehearseListView;
	private ListView mSearchMasteredListView;
	private List<courseInfo> mSearchRehearseList;
	private List<courseInfo> mSearchMasteredList;
	private ChunkRehearseListAdapter mSearchRehearseAdapter;
	private ChunkRehearseListAdapter mSearchMasteredAdapter;

	List<courseInfo> mAvaiableRehearseChunkList;
	List<courseInfo> mMasteredChunkList;

	boolean inSearchModel = false;
	ChunkBLL mChunkBLL = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_chunk_list_search);
		chunkLoader=new ChunkLoader(this);
		getExtra();
		mSearchText = (EditText) findViewById(R.id.chunk_search_actionbar_text);
		mSearchCancel = (ImageView) findViewById(R.id.chunk_search_actionbar_cancel);
		mSearchButton = (Button) findViewById(R.id.chunk_search_actionbar_ok);
		mSearchRehearseText = (TextView) findViewById(R.id.chunk_search_rehearse_text);
		mSearchRehearseListView = (ListView) findViewById(R.id.chunk_search_rehearse_listview);
		mSearchMasteredText = (TextView) findViewById(R.id.chunk_search_mastered_text);
		mSearchMasteredListView = (ListView) findViewById(R.id.chunk_search_mastered_listview);
		initialize();
	}

	private void getExtra(){
		mAvaiableRehearseChunkList = (List<courseInfo>) getSerializableExtra(AppConst.BundleKeys.LIST_REHEARSAL);
		mMasteredChunkList = (List<courseInfo>) getSerializableExtra(AppConst.BundleKeys.LIST_MASTERED);
	}

	private void initialize() {
		inSearchModel = false;
		mSearchCancel.setVisibility(View.GONE);
		mSearchText.setText("");
		mSearchButton.setText(getResources().getString(
				R.string.chunk_search_button_cancel));
		mSearchRehearseText.setVisibility(View.GONE);
		mSearchRehearseListView.setVisibility(View.GONE);
		mSearchMasteredText.setVisibility(View.GONE);
		mSearchMasteredListView.setVisibility(View.GONE);
		mChunkBLL = new ChunkBLL(mContext);
		mSearchRehearseList = new ArrayList<courseInfo>();
		mSearchMasteredList = new ArrayList<courseInfo>();
		// todo
		 mSearchRehearseAdapter = new ChunkRehearseListAdapter(
		 ChunkListSearchActivity.this, mSearchRehearseList,false);
		 mSearchMasteredAdapter = new ChunkRehearseListAdapter(
		 ChunkListSearchActivity.this, mSearchMasteredList,true);
		mSearchRehearseListView.setAdapter(mSearchRehearseAdapter);
		mSearchMasteredListView.setAdapter(mSearchMasteredAdapter);
		mSearchRehearseListView
				.setOnItemClickListener(new OnItemClickListener() {
					@Override
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {
						openChunkDetailAct(mSearchRehearseList.get(position));
						// start chunk definition
//						Chunk chunk = mSearchRehearseList.get(position);
//						Intent intent = new Intent(mContext,
//								ChunkLearnDetailActivity.class);
//						intent.putExtra(AppConst.BundleKeys.Is_Chunk_Learning,
//								false);
//						intent.putExtra(AppConst.BundleKeys.Chunk, chunk);
//						startActivity(intent);
					}
				});
		mSearchMasteredListView
				.setOnItemClickListener(new OnItemClickListener() {
					@Override
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {
						openChunkDetailAct(mSearchRehearseList.get(position));
//						Chunk chunk = mSearchMasteredList.get(position);
//						Intent intent = new Intent(mContext,
//								ChunkLearnDetailActivity.class);
//						intent.putExtra(AppConst.BundleKeys.Is_Chunk_Learning,
//								false);
//						intent.putExtra(AppConst.BundleKeys.Chunk, chunk);
//						startActivity(intent);
					}
				});
		mSearchText.addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
			}

			@Override
			public void afterTextChanged(Editable s) {
				mSearchCancel.setVisibility(View.VISIBLE);
				String search = s.toString();
				doSearch(search);
				MobclickTracking.OmnitureTrack.ActionSearch(0);
			}
		});
		mSearchCancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				initialize();
			}
		});
		mSearchButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
                //track cancelled search
                MobclickTracking.OmnitureTrack.ActionSearch(1);
                finish();
			}
		});
	}

	private void doSearch(String search) {
		inSearchModel = true;
		mSearchRehearseList.clear();
		List<courseInfo> rehearseTmpList = match(search,mAvaiableRehearseChunkList);
		if (rehearseTmpList != null && !rehearseTmpList.isEmpty())
			mSearchRehearseList.addAll(rehearseTmpList);
		mSearchMasteredList.clear();
		List<courseInfo> masterTmpList = match(search,mMasteredChunkList);
		if (masterTmpList != null && !masterTmpList.isEmpty())
			mSearchMasteredList.addAll(masterTmpList);
		if (mSearchRehearseList.size() > 0) {
//			mSearchRehearseText.setVisibility(View.VISIBLE);
			mSearchRehearseText.setText(String.format(JsonSerializeHelper
					.JsonLanguageDeserialize(mContext,
							"chunk_search_to_rehearse"), mSearchRehearseList
					.size()));
			mSearchRehearseAdapter.notifyDataSetChanged();
			mSearchRehearseListView.setVisibility(View.VISIBLE);
		} else {
			mSearchRehearseText.setVisibility(View.GONE);
			mSearchRehearseListView.setVisibility(View.GONE);
		}

		if (mSearchMasteredList.size() > 0) {
//			mSearchMasteredText.setVisibility(View.VISIBLE);
			mSearchMasteredText.setText(String.format(
					JsonSerializeHelper.JsonLanguageDeserialize(mContext,
							"chunk_search_mastered"), mSearchMasteredList
							.size()));
			mSearchMasteredAdapter.notifyDataSetChanged();
			mSearchMasteredListView.setVisibility(View.VISIBLE);
		} else {
			mSearchMasteredText.setVisibility(View.GONE);
			mSearchMasteredListView.setVisibility(View.GONE);
		}

	}

	private List<courseInfo> match(String text,List<courseInfo> list){
		List<courseInfo> temp = new ArrayList<courseInfo>();
		if(list!=null){
		for (courseInfo info :list) {
			if(info.course_name.toLowerCase().contains(text.toLowerCase())){
				temp.add(info);
			}

		}
		}

		return temp;
	}

	@Override
	protected void BI_Tracking(int i) {
		// TODO Auto-generated method stub
		switch (i) {
		case 1:

			break;

		default:
			break;
		}
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
}
