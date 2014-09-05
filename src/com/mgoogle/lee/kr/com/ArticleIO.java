package com.mgoogle.lee.kr.com;

import java.net.URL;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.mgoogle.lee.kr.MainActivity;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

public class ArticleIO {
	private static final String TAG = "Lab-36kr";
	
	public static void getUpdate(MainActivity context,String url,final KrAdapter kr_a,final Handler handlerUI) {
		try
		{
			URL mURL =new URL(url);
			Document doc=Jsoup.parse(mURL, 20000);
			Elements els=doc.select("article.posts");
//			Log.i(TAG,"img list:");
//			for( Element e:els.select("img")){
//				String src=e.attr("data-src");
//				Log.i(TAG,src.substring(src.lastIndexOf("/")+1,src.indexOf("!")));
//			}
			Log.i(TAG,"length:"+els.lastIndexOf(els.last()));
			for( Element e:els){
				String atcl_url=null;
				String title=null;
				String brief=null;
				String img_url=null;
				try{
					title=e.select("div.right-col h1 a").attr("title");
					if(kr_a.getCount()>0){
						if(title.equals(kr_a.getItem(kr_a.getCount()-1).getTitle()))break;		//没有更新,第0个一定要是最后被添加的
					}
	
					if(title==null||title.trim().length()<4)continue;
				}catch(Exception e1){
					Log.i(TAG,"parsing html error:"+e1.toString());
					e1.printStackTrace();
				}
				try{
					img_url=e.select("img").attr("data-src").split("!")[0].trim();
				}
				catch(Exception er){
					Log.i(TAG,"parsing html error:"+er.toString());
					er.printStackTrace();
				}
				try{
					atcl_url=url.substring(0, url.length()-1)+e.select("div.image a").get(0).attr("href");
				}finally{}
				try{
					brief=e.getElementsByTag("p").get(0).html();
				}catch(Exception er3){
					Log.i(TAG,"parsing html error:"+er3.toString());
					er3.printStackTrace();
				}

				

//				SQLiteOpenHelper db = new SQLiteOpenHelper();
				Log.i(TAG,"title:"+title);
				Log.i(TAG,"brief:"+brief);
				Log.i(TAG,"img_url:"+img_url);
				Log.i(TAG,"atcl_url:"+atcl_url);
				
				final String s1=title;
				final String s2=brief;
				final String s3=img_url;
				final String s4=atcl_url;
				
				Log.i(TAG,"start runOnUiThread.");
				context.runOnUiThread(new Runnable(){

					@Override
					public void run() {
						// TODO Auto-generated method stub
						Log.i(TAG,"start to update img to adapter.");
						kr_a.add(new KrItem(s1, s2, s4, s3));
						Message msg = Message.obtain();
						msg.what=1;
						msg.setTarget(handlerUI);
						msg.sendToTarget();
					}
					
				});
				
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}
	}
}
