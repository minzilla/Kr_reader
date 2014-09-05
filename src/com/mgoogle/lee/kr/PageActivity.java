package com.mgoogle.lee.kr;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.Window;
import android.webkit.WebView;

public class PageActivity extends Activity {
	WebView wv=null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.page);
		wv=(WebView) findViewById(R.id.page);
		wv.loadUrl(getIntent().getStringExtra("url"));
		
	}

	public static Intent makeIntent(Context context,String url) {
		// TODO Auto-generated method stub
		Intent intent=new Intent(context,PageActivity.class);
		intent.putExtra("url", url);
		return intent;
	}

}
