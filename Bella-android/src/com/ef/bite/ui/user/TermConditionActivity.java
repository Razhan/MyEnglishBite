package com.ef.bite.ui.user;

import android.os.Bundle;
import com.ef.bite.R;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ef.bite.AppConst;
import com.ef.bite.ui.BaseActivity;
import com.ef.bite.utils.AppLanguageHelper;
import com.ef.bite.utils.AssetResourceHelper;
import com.ef.bite.utils.FontHelper;
import com.ef.bite.utils.JsonSerializeHelper;

public class TermConditionActivity extends BaseActivity {

	public final static String TERMS_SERVICE_PATH_EN = "html/terms_service.html";
	public final static String TERMS_SERVICE_PATH_ZH = "html/terms_service_zh.html";
	public final static String TERMS_SERVICE_PATH = "policy/android/";

	private View layout;
	private ImageButton mCancel;
	private LinearLayout mButtonBar;
	private Button mAgree;
	private Button mNotAgree;
	private TextView popup_terms_title;
	private WebView mTermsWeb;
	private View.OnClickListener mAgreeClick;
	private View.OnClickListener mNotAgreeClick;
	private boolean isButtonShow = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_term_condition);
		Bundle bundle = getIntent().getExtras();
		if (bundle != null
				&& bundle.containsKey(AppConst.BundleKeys.Terms_Btnbar_Show))
			isButtonShow = bundle
					.getBoolean(AppConst.BundleKeys.Terms_Btnbar_Show);
		initViews();
	}

	protected void initViews() {
		layout = findViewById(R.id.terms_condition_layout);
		mTermsWeb = (WebView) findViewById(R.id.popup_terms_webview);
		mCancel = (ImageButton) findViewById(R.id.popup_terms_cancel);
		mButtonBar = (LinearLayout) findViewById(R.id.popup_terms_button_bar);
		mAgree = (Button) findViewById(R.id.popup_terms_btn_agree);
		mNotAgree = (Button) findViewById(R.id.popup_terms_btn_notagree);
		popup_terms_title = (TextView) findViewById(R.id.popup_terms_title);
		mAgree.setText(JsonSerializeHelper.JsonLanguageDeserialize(mContext,
				"popup_term_service_btn_agree"));
		mNotAgree.setText(JsonSerializeHelper.JsonLanguageDeserialize(mContext,
				"popup_term_Service_btn_not_agree"));
		popup_terms_title.setText(JsonSerializeHelper.JsonLanguageDeserialize(
				mContext, "popup_term_service_title"));

		// load terms & service html
		mTermsWeb.getSettings().setLoadWithOverviewMode(true);
		// mTermsWeb.getSettings().setUseWideViewPort(true);
		mTermsWeb.getSettings().setTextSize(WebSettings.TextSize.SMALLER);
		mTermsWeb.setBackgroundColor(0); // 设置背景色
		// mTermsWeb.getBackground().setAlpha(2); // 设置填充透明度 范围：0-255
		String html = AppConst.EFAPIs.BaseAddress + TERMS_SERVICE_PATH
				+ AppLanguageHelper.getSystemLaunguage(mContext);

		// if(AppLanguageHelper.getSystemLaunguage(mContext).equals(AppLanguageHelper.ZH_CN))
		// html = AssetResourceHelper.getJsonFromAssets(mContext,
		// TERMS_SERVICE_PATH_ZH);
		// else
		// html = AssetResourceHelper.getJsonFromAssets(mContext,
		// TERMS_SERVICE_PATH_EN);
		// mTermsWeb.loadDataWithBaseURL(null, html, "text/html", "UTF-8",
		// null);
		// .loadUrl("file:///android_asset/html/terms_service.html");
		mTermsWeb.loadUrl(html);
		mTermsWeb.setWebViewClient(new WebViewClient() {
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				// TODO Auto-generated method stub
				// return super.shouldOverrideUrlLoading(view, url);
				view.loadUrl(url);
				return true;
			}
		});
		FontHelper.applyFont(mContext, layout, FontHelper.FONT_Museo300);
		if (isButtonShow) {
			mButtonBar.setVisibility(View.VISIBLE);
			mAgree.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					if (mAgreeClick != null)
						mAgreeClick.onClick(v);
					close(true);
				}
			});
			mNotAgree.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					if (mNotAgreeClick != null)
						mNotAgreeClick.onClick(v);
					close(false);
				}
			});
		} else
			mButtonBar.setVisibility(View.GONE);
		mCancel.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				close(null);
			}
		});
	}

	/**
	 * 关闭服务条款页面
	 * 
	 * @param result
	 */
	private void close(Boolean result) {
		if (result != null && result == true) {
			setResult(AppConst.ResultCode.TERMS_ACCEPT);
		} else if (result != null && result == false)
			setResult(AppConst.ResultCode.TERMS_NOT_ACCEPT);
		finish();
	}
}
