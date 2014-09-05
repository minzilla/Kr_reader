package com.mgoogle.lee.kr.com;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.mgoogle.lee.kr.MainActivity;
import com.mgoogle.lee.kr.R;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class KrAdapter extends ArrayAdapter<KrItem> {
	private static final String TAG = "Lab-36kr";
	private int resourseId;
	private Context context;
	private List<KrItem> mList;
	private Handler handlerUI=null;
	private Handler mHandler=null;
	private HandlerThread mThread=null;
	private static int debug=0;
	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return super.getItemId(position);
	}

	@Override
	public void insert(KrItem object, int index) {
		// TODO Auto-generated method stub
		super.insert(object, index);
	}

	

	public KrAdapter(MainActivity context, int resource, KrItem[] objects) {
		super(context, resource);
		// TODO Auto-generated constructor stub
		this.resourseId=resource;
		this.context=context;
		this.mList=Collections.synchronizedList(new ArrayList<KrItem>());
		if(null!=objects&&objects.length>0){
			for(KrItem item:objects){
				mList.add(item);
			}
		}
		handlerUI=new Handler(context.getMainLooper());
		mThread=new HandlerThread("DownloadImg");
		mThread.start();
		mHandler=new Handler(mThread.getLooper());
		
	}
	
	@Override
	public void add(KrItem object) {
		// TODO Auto-generated method stub
		super.add(object);
		mList.add(object);
	}



	@Override
	public void clear() {
		// TODO Auto-generated method stub
		super.clear();
		mList.clear();
	}

	@Override
	public Context getContext() {
		// TODO Auto-generated method stub
		return super.getContext();
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
//		return super.getCount();
		return mList.size();
	}

	@Override
	public KrItem getItem(int position) {
		// TODO Auto-generated method stub
//		return super.getItem(position);
		return mList.get(position);
	}

	@Override
	public int getPosition(KrItem item) {
		// TODO Auto-generated method stub
//		return super.getPosition(item);
		return mList.indexOf(item);
	}

	@Override
	public void remove(KrItem object) {
		// TODO Auto-generated method stub
		super.remove(object);
		mList.remove(object);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent){
		Log.i(TAG,"start to getView");
		LinearLayout item=null;
		KrItem krItem = getItem(position);
		if(convertView==null){
			LayoutInflater li = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			item = new LinearLayout(getContext());
			li.inflate(resourseId, item,true);
		}else{
			item=(LinearLayout) convertView;
		}
			TextView title = (TextView)item.findViewById(R.id.title);

//			TextView brief = (TextView)item.findViewById(R.id.brief);
			ImageView iv=(ImageView) item.findViewById(R.id.iv);
			LinearLayout item_text=(LinearLayout) item.findViewById(R.id.item_text);
			title.setText(krItem.getTitle()+"  Pos: "+position);
			iv.setImageBitmap(null);
//			brief.setText(krItem.getBrief());
			item_text.setWeightSum(0.7f);
			
			
			
			if(!krItem.isImg_loaded()){
				if(null!=krItem.getFile_out()&&!krItem.getFile_out().isEmpty()){
					loadImg(position);
					
				}else{
					downLoadImgForItem(position);
				
				}
			}
			iv.setImageBitmap(krItem.getBitMap());
		
//		item.setWeightSum(1);
		return item;
	}

	private void loadImg(int position) {
		BitmapFactory.Options option=new BitmapFactory.Options();
//				option.inSampleSize=option.outWidth/60;
		KrItem krItem=getItem(position);
		Log.i(TAG+"-BitMap","position:"+position+"  krItem.getFile_out():"+krItem.getFile_out()+"  title:"+krItem.getTitle()+"  listsize:"+mList.size());
		krItem.setBitMap(BitmapFactory.decodeFile(krItem.getFile_out(),option));
		krItem.setImg_loaded(true);
		if(position>7){
			mList.get(position-7).setBitMap(null);
			if(position+7<mList.size())
				mList.get(position+7).setBitMap(null);
		}
		Message msg=Message.obtain();
		msg.setTarget(handlerUI);
		msg.what=1;
		msg.sendToTarget();
	}

	private void downLoadImg( final int position) {
		final KrItem item =mList.get(position);
		
		final String img_url=item.getBm();
		String file_out=null;
		file_out=context.getFilesDir().getAbsolutePath();
		Log.i(TAG,"img file will store to "+file_out);
		String file_name=img_url.substring(img_url.lastIndexOf('/')+1,img_url.length());
		if(!file_out.endsWith("/"))file_out=file_out+"/"+file_name;
		else file_out=file_out+file_name;
		
		final String file=file_out;
		if(img_url!=null&&!img_url.isEmpty()
				&&(item.getFile_out()==null||item.getFile_out().isEmpty())
				){

					// TODO Auto-generated method stub
			if(!new File(file).exists()){
					InputStream is=null;
					OutputStream os=null;
					Log.i(TAG,"downloading img_url:"+img_url);
					try{
						is =(InputStream) new URL(img_url).getContent();

						os = new FileOutputStream(file);
						byte[] buf=new byte[4*1024];
						for(int read=0;(read=is.read(buf))!=-1;){
							os.write(buf, 0, read);
						}
						item.setFile_out(file);
						loadImg(position);
//						Message msg=Message.obtain(handlerUI);
//						Bundle data=new Bundle();
//						data.putInt("pos", position);
//						data.putParcelable("item", item);
//						msg.setData(data);
//						msg.sendToTarget();
					}catch(Exception e){
						e.printStackTrace();
					}finally{
						if(null!=is)
							try {
								is.close();
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						if(null!=os)
							try {
								os.close();
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
					}

			}else{
				Log.i(TAG+"-debug","before POST item.getFile_out(): "+item.getFile_out()+" pos:"+position+"  debugID:"+(++debug));
				final int debug3=debug;
				item.setFile_out(file);
				mList.set(position, item);
				loadImg(position);
				
			}
		}
	}
	
	boolean isToDeleted(MotionEvent arg1){
		if(arg1.getRawX()>2){
			return true;
		}else{
			return false;
		}
	}
	
	private void downLoadImgForItem( final int position){
		
		mHandler.post(new Runnable(){
			@Override
			public void run() {
				// TODO Auto-generated method stub
				downLoadImg(position);
			}
			
		});
	}
}
