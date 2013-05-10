package com.jeffrey.jxml;

import test.Plane;
import test.SystemResult;
import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;

import com.jeffrey.jxml.net.RequestXml;
import com.jeffrey.jxml.net.RequestXml.RequestEntity;

public class MainActivity extends Activity {
	public final static String CITY="City";
	private RequestEntity requestEntity;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		try {
			JeffreyXml jXml=new JeffreyXml(getAssets().open("city3.xml"));
			jXml.IgnoreNullKey(); 
//			jXml.aliasAttribute(CITY, CityAttribute.class);
//			String str=jXml.getString("FunctionName");
			System.out.println(jXml.parserObject(SystemResult.class).getData().get(0).getCityAttr());
//			Plane systemResult=jXml.parserObject(getAssets().open("1.xml"), Plane.class);
//			System.out.println(systemResult);
			RequestXml requestXml=new RequestXml() {
				
				@Override
				public void success(Object obj) {
					
				}
				
				@Override
				public void failure(String errCode) {
					
				}

				@Override
				public void error(Exception e) {
					if(e instanceof NullPointerException){
						
					}else{
						e.getMessage();
					}
				}
			};
			
			RequestEntity requestEntity=new RequestEntity(jXml, Plane.class);
//			requestXml.setRequestEntity(requestEntity);
//			requestEntity.setURL(url);
//			requestEntity.setParam(param);
//			requestEntity.setProtocol(protocol);
//			requestEntity.setHttpEntity(httpEntity);
//			requestEntity.Post();
		}catch (Exception e) {
			e.printStackTrace();
		}
	} 


	public <T> T parserObject(Class<T> clz){
		try {
		return  clz.newInstance();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}
}
