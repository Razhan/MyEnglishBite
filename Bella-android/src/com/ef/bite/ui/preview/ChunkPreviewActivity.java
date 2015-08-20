package com.ef.bite.ui.preview;

import java.util.ArrayList;
import com.ef.bite.R;
import java.util.List;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageButton;
import android.widget.ListView;

import com.ef.bite.AppConst;
import com.ef.bite.AppSession;
import com.ef.bite.adapters.ChunkMasteredListAdapter;
import com.ef.bite.business.ChunkBLL;
import com.ef.bite.business.task.BaseAsyncTask;
import com.ef.bite.business.task.PostExecuting;
import com.ef.bite.dataacces.ConfigSharedStorage;
import com.ef.bite.dataacces.mode.Chunk;
import com.ef.bite.model.ConfigModel;
import com.ef.bite.ui.BaseActivity;
import com.ef.bite.ui.chunk.ChunkLearnActivity;
import com.ef.bite.utils.AppLanguageHelper;

public class ChunkPreviewActivity extends BaseActivity {
	ImageButton mGoback;
	List<Chunk> mChunkList = new ArrayList<Chunk>();
	ListView mListView;
	ChunkMasteredListAdapter mPreviewAdapter;
	ProgressDialog progress;
	public static boolean IsLoaded = false;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_chunk_main_preview);
		mGoback = (ImageButton)findViewById(R.id.chunk_preview_actionbar_goback);
		mListView = (ListView)findViewById(R.id.chunk_preview_listview);
		initAppConfig();
		mGoback.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View v) {
				AppSession.getInstance().exit();
			}
		});
		initPreviewChunkList();
	}
	
	
	 @Override
	 public void onBackPressed() {
		 AppSession.getInstance().exit();
	 }
	
	private void initAppConfig(){
		// 加载APP语言设置
    	ConfigModel config = new ConfigSharedStorage(mContext).get();
    	if(config!=null && config.MultiLanguageType!=AppConst.MultiLanguageType.Default)
    		AppLanguageHelper.loadLanguageFirstTime(mContext, config.MultiLanguageType);
    	AppConst.GlobalConfig.Language = AppLanguageHelper.getSystemLaunguage(mContext);
		
	}
	
	/**
	 * 初始化chunk预览
	 */
	private void initPreviewChunkList(){
		if(IsLoaded == true){
			ChunkBLL bll = new ChunkBLL(mContext);
			mChunkList = bll.getAllChunk();
			mPreviewAdapter = new ChunkMasteredListAdapter(ChunkPreviewActivity.this, mChunkList);
			mListView.setAdapter(mPreviewAdapter);
			mListView.setOnItemClickListener(new OnItemClickListener(){
				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					Chunk chunk = mChunkList.get(position);
					previewChunk(chunk);
				}
			});
			return;
		}
		progress = new ProgressDialog(this);
		progress.setMessage("Initializing phrases for preview...");
		progress.show();
		InitPreviewChunkTask task = new InitPreviewChunkTask(mContext,new PostExecuting<List<Chunk>>(){
			@Override
			public void executing(List<Chunk> result) {
				progress.dismiss();
				mChunkList = result;
				mPreviewAdapter = new ChunkMasteredListAdapter(ChunkPreviewActivity.this, mChunkList);
				mListView.setAdapter(mPreviewAdapter);
				mListView.setOnItemClickListener(new OnItemClickListener(){
					@Override
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {
						Chunk chunk = mChunkList.get(position);
						previewChunk(chunk);
					}
				});
				IsLoaded = true;
			}
		});
		task.execute();
	}
	
	
	class InitPreviewChunkTask extends BaseAsyncTask<Void,Void,List<Chunk>>{

		public InitPreviewChunkTask(Context context,
				PostExecuting<List<Chunk>> executing) {
			super(context, executing);
			// TODO Auto-generated constructor stub
		}

		@Override
		protected List<Chunk> doInBackground(Void... params) {
			ChunkBLL bll = new ChunkBLL(mContext);
			return bll.getAllChunk();
		}
		
	}
	
	
	/** 开始预览chunk **/
	private void previewChunk(Chunk chunk){
		Intent intent = new Intent(mContext, ChunkLearnActivity.class);
		intent.putExtra(AppConst.BundleKeys.Is_Chunk_For_Preview, true);
		intent.putExtra(AppConst.BundleKeys.Chunk, chunk);
		startActivityForResult(intent, AppConst.RequestCode.CHUNK_PREVIEW);
	}
}
