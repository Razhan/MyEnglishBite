package com.ef.bite.ui.popup;

import android.app.Activity;

import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.widget.*;
import com.ef.bite.AppConst;
import com.ef.bite.R;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.ef.bite.utils.AppLanguageHelper;
import com.ef.bite.utils.AssetResourceHelper;
import com.ef.bite.utils.FontHelper;
import com.ef.bite.utils.JsonSerializeHelper;

/**
 * 服务条款Pop up
 * 
 * @author Allen.Zhu
 * 
 */
public class TermsServicePopupWindow extends BasePopupWindow {

	public final static String TERMS_SERVICE_PATH_EN = "html/terms_service.html";
	public final static String TERMS_SERVICE_PATH_ZH = "html/terms_service_zh.html";
	public final static String TERMS_SERVICE_PATH = "policy/android/";

	private RelativeLayout windows;
	private ImageButton mCancel;
	private LinearLayout mButtonBar;
	private Button mAgree;
	private Button mNotAgree;
	private TextView mPopuptermstitle;
	private WebView mTermsWeb;
	private View.OnClickListener mAgreeClick;
	private View.OnClickListener mNotAgreeClick;
	private boolean isButtonShow = false;

	/**
	 * 初始化服务条款，不带button bar
	 * 
	 * @param activity
	 */
	public TermsServicePopupWindow(Activity activity) {
		super(activity, R.layout.popup_terms_services);
	}

	/**
	 * 初始化服务条款，带button bar
	 * 
	 * @param activity
	 * @param agreeClick
	 * @param notAgreeClick
	 */
	public TermsServicePopupWindow(Activity activity,
			View.OnClickListener agreeClick, View.OnClickListener notAgreeClick) {
		super(activity, R.layout.popup_terms_services);
		isButtonShow = true;
		mAgreeClick = agreeClick;
		mNotAgreeClick = notAgreeClick;
	}

	@Override
	protected void initViews(View layout) {

		windows = (RelativeLayout)layout.findViewById(R.id.termswindows);
		mTermsWeb = (WebView) layout.findViewById(R.id.popup_terms_webview);
		mCancel = (ImageButton) layout.findViewById(R.id.popup_terms_cancel);
		mButtonBar = (LinearLayout) layout
				.findViewById(R.id.popup_terms_button_bar);
		mAgree = (Button) layout.findViewById(R.id.popup_terms_btn_agree);
		mNotAgree = (Button) layout.findViewById(R.id.popup_terms_btn_notagree);
		mPopuptermstitle = (TextView) layout
				.findViewById(R.id.popup_terms_title);
		mAgree.setText(JsonSerializeHelper.JsonLanguageDeserialize(mActivity,
				"popup_term_service_btn_agree"));
		mNotAgree.setText(JsonSerializeHelper.JsonLanguageDeserialize(
				mActivity, "popup_term_Service_btn_not_agree"));
		mPopuptermstitle.setText(JsonSerializeHelper.JsonLanguageDeserialize(
                mActivity, "popup_term_service_title"));


        windows.setFocusable(true);
        windows.setFocusableInTouchMode(true);
        windows.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				close();
			}
		});


		// load terms & service html
		mTermsWeb.getSettings().setLoadWithOverviewMode(true);
		mTermsWeb.getSettings().setTextSize(WebSettings.TextSize.SMALLER);
		mTermsWeb.setBackgroundColor(0); // 设置背景色
		String html = AppConst.EFAPIs.BaseAddress + TERMS_SERVICE_PATH
				+ AppLanguageHelper.getSystemLaunguage(mActivity);

		// old
		// if (AppLanguageHelper.getSystemLaunguage(mActivity).equals(
		// AppLanguageHelper.ZH_CN))
		// html = AssetResourceHelper.getJsonFromAssets(mActivity,
		// TERMS_SERVICE_PATH_ZH);
		// else
		// html = AssetResourceHelper.getJsonFromAssets(mActivity,
		// TERMS_SERVICE_PATH_EN);
		// mTermsWeb.loadDataWithBaseURL(null, html, "text/html", "UTF-8",
		// null);

		// new service
		// mTermsWeb.loadUrl("file:///android_asset/html/terms_service.html");
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
		FontHelper.applyFont(mActivity, layout, FontHelper.FONT_Museo300);
		if (isButtonShow) {
			mButtonBar.setVisibility(View.VISIBLE);
			mAgree.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					if (mAgreeClick != null)
						mAgreeClick.onClick(v);
					close();
				}
			});
			mNotAgree.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					if (mNotAgreeClick != null)
						mNotAgreeClick.onClick(v);
					close();
				}
			});
		} else
			mButtonBar.setVisibility(View.GONE);
		mCancel.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				close();
			}
		});
	}

}
