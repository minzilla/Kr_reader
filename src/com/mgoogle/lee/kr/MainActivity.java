package com.mgoogle.lee.kr;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;


import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.xml.sax.*;
import org.xml.sax.helpers.DefaultHandler;

import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.mgoogle.lee.kr.com.ArticleIO;
import com.mgoogle.lee.kr.com.KrAdapter;
import com.mgoogle.lee.kr.com.KrItem;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MainActivity extends Activity {
	private static final String TAG = "Lab-36kr";

	final String url="http://www.36kr.com/" ;
	
	private PullToRefreshListView lv;
	private KrAdapter adapter;
	private Handler mHandler;
	private TextView tv;
	private MainActivity mContext=this;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);

		lv = (PullToRefreshListView) findViewById(R.id.mPull);
//		if(null!=savedInstanceState){
//			getContentResolver();
//		}
		
		mHandler=new Handler(getMainLooper()){

			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub
				super.handleMessage(msg);
				Log.i(TAG,"handleMessage");
				adapter.notifyDataSetChanged();
			}
			
		};
		

		adapter = new KrAdapter(this,R.layout.kr_item, null);
		
		
		lv.setOnItemClickListener(new OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				startActivity(PageActivity.makeIntent(mContext, adapter.getItem(arg2).getUrl()));
			}
			
		});
///		getUpdate();
		
		
		lv.setAdapter(adapter);
		new Thread(new Runnable(){

			@Override
			public void run() {
				// TODO Auto-generated method stub
				ArticleIO.getUpdate(mContext, url, adapter,mHandler);
				Log.i(TAG,"adapter number:"+adapter.getCount()+"");
				adapter.notifyDataSetChanged();
			}
			
		}).start();
//		try {
//			Thread.sleep(4000);
//			Thread.sleep(4000);
//		} catch (InterruptedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
	}

	private void getUpdateFromJsoup(String url) {
		mHandler=new Handler(Looper.getMainLooper());
		try{
			boolean flag=false;
			URL mURL =new URL(url);
			Document doc=Jsoup.parse(mURL, 20000);
			Elements els=doc.select("article.posts");
			Log.i(TAG,"length:"+els.lastIndexOf(els.last()));
			for(final Element e:els){
				flag= true;
//				Log.i(TAG,e.ownText()+"ownText");
//				if(els.indexOf(e)==3)Log.i(TAG,e.outerHtml());
//				mHandler.post(new Runnable(){
//
//					@Override
//					public void run() {
//						// TODO Auto-generated method stub
//						tv.setText(tv.getText()+e.outerHtml());
//					}
//					
//				});
				try{
//					Log.i(TAG,"--------------------index:"+els.indexOf(e)+"--------------------------");
					Log.i(TAG,e.select("div.right-col h1 a").attr("title"));
					String img_url=e.select("img").attr("src");
					Log.i(TAG,img_url);
					String article_url=url.substring(0, url.length()-1)+e.select("div.image a").get(0).attr("href");
					Log.i(TAG,article_url);
					Log.i(TAG,e.getElementsByTag("p").get(0).html());
//					Log.i(TAG,"--------------------------------------------------------");
				}catch(Exception es){
					Log.i(TAG,"index:"+els.indexOf(e));
					Log.i(TAG,e.outerHtml());
				}
			}
			
			Log.i(TAG,flag?"Excute Jsoup":"Not Excute Jsoup");
		}catch(Exception e){
			Log.i(TAG,e.toString());
		}
	}

	private void getUpdate() {
		// TODO Auto-generated method stub
		BufferedReader br =null;
		InputStream is =null;
		try {
			HttpURLConnection http=(HttpURLConnection)(new URL(url)).openConnection();
			http.setInstanceFollowRedirects(true);
			is =http.getInputStream();
			
			SAXParser parser=SAXParserFactory.newInstance().newSAXParser();
			XMLReader reader =parser.getXMLReader();
			
			parser.parse(is, new DefaultHandler(){
				private ArrayList<String[]> al =null;
				@Override
				public void startDocument() throws SAXException{
					al= new ArrayList<String[]>();
				}
				@Override
				public void startElement(String uri, String localName, String qName, Attributes attributes){
					
				}
				@Override
				public void characters(char[] arg0, int arg1, int arg2)
						throws SAXException {
					// TODO Auto-generated method stub
					
				}
				@Override
				public void endDocument() throws SAXException {
					// TODO Auto-generated method stub
					
				}
				@Override
				public void endElement(String arg0, String arg1, String arg2)
						throws SAXException {
					// TODO Auto-generated method stub
					
				}
				@Override
				public void endPrefixMapping(String arg0) throws SAXException {
					// TODO Auto-generated method stub
					
				}
				@Override
				public void ignorableWhitespace(char[] arg0, int arg1, int arg2)
						throws SAXException {
					// TODO Auto-generated method stub
					
				}
				@Override
				public void processingInstruction(String arg0, String arg1)
						throws SAXException {
					// TODO Auto-generated method stub
					
				}
				@Override
				public void setDocumentLocator(Locator arg0) {
					// TODO Auto-generated method stub
					
				}
				@Override
				public void skippedEntity(String arg0) throws SAXException {
					// TODO Auto-generated method stub
					
				}
				@Override
				public void startPrefixMapping(String arg0, String arg1)
						throws SAXException {
					// TODO Auto-generated method stub
					
				}
			});
			
			
			
			
			br = new BufferedReader(
					new InputStreamReader(is));
			for(String s=br.readLine();s!=null&&!(s.isEmpty());s=br.readLine()){
				
			}
			
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally{
			if(null!=br)
				try {
					br.close();
					
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
