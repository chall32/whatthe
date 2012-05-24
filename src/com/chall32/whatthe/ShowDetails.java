package com.chall32.whatthe;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.Window;

public class ShowDetails extends Activity {
	WebView mWebView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setTitle("What The .....? Blog");
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		requestWindowFeature(Window.FEATURE_PROGRESS);

		setContentView(R.layout.details);
		setProgressBarIndeterminateVisibility(true);
		setProgressBarVisibility(true);

		// TextView detailsTitle = (TextView)findViewById(R.id.detailstitle);
		// TextView detailsDescription =
		// (TextView)findViewById(R.id.detailsdescription);
		// TextView detailsPubdate =
		// (TextView)findViewById(R.id.detailspubdate);
		// TextView detailsLink = (TextView)findViewById(R.id.detailslink);

		Bundle bundle = this.getIntent().getExtras();

		// detailsTitle.setText(bundle.getString("keyTitle"));
		// detailsDescription.setText(bundle.getString("keyDescription"));
		// detailsPubdate.setText(bundle.getString("keyPubdate"));
		// detailsLink.setText(bundle.getString("keyLink"));

		try {
			mWebView = (WebView) findViewById(R.id.showpage);
			mWebView.getSettings().setJavaScriptEnabled(true);
			mWebView.setWebViewClient(new WebViewClient());

			mWebView.setWebChromeClient(new WebChromeClient() {
				public void onProgressChanged(WebView view, int progress) {
					setProgress(progress * 100);
					if (progress == 100) {
						setProgressBarIndeterminateVisibility(false);
						setProgressBarVisibility(false);
					}

				}
			});
			mWebView.loadUrl((bundle.getString("keyLink"))+"?m=1");

		} catch (Exception e) {
			// blah
		}
	}
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.layout.menu, menu);
		return true;
	}

	public boolean onOptionsItemSelected(MenuItem item) {
		PackageInfo packageInfo = null;
		switch (item.getItemId()) {
		case R.id.refresh:
			Intent intent = getIntent();
			finish();
			startActivity(intent);
			break;
		case R.id.help:
			Toast.makeText(this, "Work it out. It can't be that hard!",
					Toast.LENGTH_LONG).show();
			break;
		case R.id.info:
			try {
				packageInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
			} catch (NameNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			new AlertDialog.Builder(this)
								.setIcon(R.drawable.wticon)
					.setTitle("What The.....? Blog Reader")
					.setMessage("Version: " + packageInfo.versionName + "\n \n \nHope you like my first Android application.\n \nFeel free to drop me some feedback! \n \n - Chris")
					.setNeutralButton("OK",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int which) {
								}
							}).show();
			break;
		}
		return true;
	}
}
