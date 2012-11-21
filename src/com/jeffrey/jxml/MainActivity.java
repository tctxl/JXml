package com.jeffrey.jxml;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.widget.Toast;

import com.jeffrey.jxml.test.SystemResult;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		try {
			JeffreyXml jXml=new JeffreyXml();
			jXml.IgnoreNullKey();
			SystemResult systemResult=(SystemResult) jXml.parserObject(new String(read(getAssets().open("city.xml"))), SystemResult.class);
			System.out.println(systemResult);
//			Toast.makeText(this, systemResult.toString(),0).show();
		}catch (Exception e) {
//			Toast.makeText(this, "ERROR : "+e.getMessage(),0).show();
			e.printStackTrace();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}
	public static byte[] read(InputStream inputStream) throws Exception {
		ByteArrayOutputStream arrayBuffer = new ByteArrayOutputStream();
		byte[] b = new byte[1024];
		int len = -1;
		while ((len = inputStream.read(b)) != -1) {
			arrayBuffer.write(b, 0, len);
		}
		byte[] bytes=arrayBuffer.toByteArray();
		inputStream.close();
		arrayBuffer.close();
		return bytes;
	}
}
