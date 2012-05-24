package com.chall32.whatthe;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.os.StrictMode;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class AndroidRssReader extends ListActivity {

	private RSSFeed myRssFeed = null;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		StrictMode.setThreadPolicy(policy);
		super.onCreate(savedInstanceState);
		setTitle("What The .....? Blog Reader");
		setContentView(R.layout.main);

		
		try {
			URL rssUrl = new URL("http://feeds.feedburner.com/chall32.rss");
			SAXParserFactory mySAXParserFactory = SAXParserFactory
					.newInstance();
			SAXParser mySAXParser = mySAXParserFactory.newSAXParser();
			XMLReader myXMLReader = mySAXParser.getXMLReader();
			RSSHandler myRSSHandler = new RSSHandler();
			myXMLReader.setContentHandler(myRSSHandler);
			InputSource myInputSource = new InputSource(rssUrl.openStream());
			myXMLReader.parse(myInputSource);

			myRssFeed = myRSSHandler.getFeed();

		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// setProgressBarIndeterminateVisibility(false);
		// setProgressBarVisibility(false);
		if (myRssFeed != null) {
			ArrayAdapter<RSSItem> adapter = new ArrayAdapter<RSSItem>(this,
					R.layout.rsslist, R.id.label, myRssFeed.getList());
			setListAdapter(adapter);
		}
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		// TODO Auto-generated method stub
		Intent intent = new Intent(this, ShowDetails.class);
		Bundle bundle = new Bundle();
		bundle.putString("keyTitle", myRssFeed.getItem(position).getTitle());
		bundle.putString("keyDescription", myRssFeed.getItem(position)
				.getDescription());
		bundle.putString("keyLink", myRssFeed.getItem(position).getLink());
		bundle
				.putString("keyPubdate", myRssFeed.getItem(position)
						.getPubdate());
		intent.putExtras(bundle);
		startActivity(intent);
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
			AlertDialog d = new AlertDialog.Builder(this)
			 .setPositiveButton(android.R.string.ok, null)
			 .setIcon(R.drawable.wticon)
			 .setTitle("What The.....? Blog Reader")
			 .setMessage(Html.fromHtml("<p>Click a post heading to be taken to the live post on the What the....? Blog website. </p><p>Alternatively, you can view the blog at:</p><p></p><a href=\"http://chall32.blogspot.com/?m=1\">chall32.blogspot.com</a>"))
			 .create();
			d.show();
			    ((TextView)d.findViewById(android.R.id.message)).setMovementMethod(LinkMovementMethod.getInstance());
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
					.setMessage("Version: " + packageInfo.versionName + "\n \n \nHope you like my first Android application.\n \nBeta 3:\n \n - Comform to Google style guide when launced under Ice Cream Sandwich\n \n - Updated help & about dialogues \n \n - Chris (chall32@gmail.com)")
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