package com.jeffrey.jxml.net;

import java.lang.reflect.InvocationTargetException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import com.jeffrey.jxml.JeffreyXml;
import com.jeffrey.jxml.exception.NullRequestEntityException;
import com.jeffrey.jxml.util.InvokeUtil;

/**
 * @author Jeffrey Shi
 * QQ 362116120
 * MAIL to shijunfan@163.com
 */
public abstract class RequestXml extends AbsBackNet{
	
	private RequestEntity requestEntity;
	private boolean isCancelRequest=false;
	private  ExecutorService executorService = Executors
			.newSingleThreadExecutor();
	
	public void setRequestEntity(RequestEntity requestEntity){
		this.requestEntity=requestEntity;
	}
	
	public static class RequestEntity implements BackResult{
		private String url=null;
		private String param;
		private String protocol;
		private HttpEntity httpEntity;
		private JeffreyXml jXml;
		private Class<?> clz;
		
		public RequestEntity(JeffreyXml jXml,Class<?> backClass) {
			this.jXml=jXml;
			this.clz=backClass;
		}
		@Override
		public String url() {
			return url.concat("/").concat(protocol);
		}

		@Override
		public HttpEntity getEntity() {
			return httpEntity;
		}

		@Override
		public JeffreyXml jXml() {
			return jXml;
		}

		@Override
		public Class<?> backClass() {
			return clz;
		}

		@Override
		public String getParam() {
			return param;
		}
		
		/**请求路径*/
		public void setURL(String url){
			this.url=url;
		}
		
		/**请求协议 如 http://127.0.0.1/login?userName=test&passWord=test 中 login为请求协议 "?" 后的则是参数 */
		public void setProtocol(String protocol){
			this.protocol=protocol;
		}
		
		/**传入对象需要实现setter/getter方法*/
		public void setParam(Object param){
			try {
				this.param=InvokeUtil.invokeObjtoString(param);
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (SecurityException e) {
				e.printStackTrace();
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			} catch (NoSuchMethodException e) {
				e.printStackTrace();
			}
		}
		
		/**请求参数*/
		public void setParam(String param){
			this.param=param;
		}
		
		/**请求参数*/
		public void setParam(Map<String,String> param){
			StringBuilder sb = new StringBuilder();
			boolean frist=true;
			for(Iterator<String> it = param.keySet().iterator();it.hasNext();){
				String key =  it.next();
				String value = param.get(key);
				if(frist){
					frist=false;
				}else{
					sb.append("&");
				}
				sb.append(key).append("=").append(value);
			}
			
			this.param=sb.toString();
		}
		
		/**传入HttpEntity*/
		public void setHttpEntity(HttpEntity httpEntity){
			this.httpEntity=httpEntity;
		}
	}
	
	/**请求数据接口*/
	private interface BackResult {
		public String url();
		public HttpEntity getEntity();
		public JeffreyXml jXml();
		public Class<?> backClass();
		public String getParam();
	}

	/**POST
	 * @throws NullRequestEntityException */
	public void Post() throws NullRequestEntityException{
		if(requestEntity==null){
			throw new NullRequestEntityException();
		}else
		requestPost(requestEntity);
	}
	
	/**Get请求*/
	public void Get() throws NullRequestEntityException{
		if(requestEntity==null){
			throw new NullRequestEntityException();
		}else
		requestGet(requestEntity);
	}
	
	private void requestPost(final BackResult backResult) {
		executorService.submit(new Runnable() {
			@Override
			public void run() {
				Object obj = null;
				HttpPost request = new HttpPost(backResult.url());
				List<NameValuePair> p = new ArrayList<NameValuePair>();
				
				String[] params = backResult.getParam().split("&");
						
				for (String param:params) {
					String[] keyValue=param.split("=");
					String key = keyValue[0]!=null?keyValue[0]:null;
					if(key==null)
						continue;
					p.add(new BasicNameValuePair(key, keyValue[1]!=null?keyValue[1]:""));
				}
				
				HttpEntity entity = null;
				try {
					entity = new UrlEncodedFormEntity(p, "UTF-8");
					request.setEntity(entity);
					HttpParams httpParameters = new BasicHttpParams();
					int timeoutConnection = 40000;
					HttpConnectionParams.setConnectionTimeout(httpParameters,
							timeoutConnection);
					int timeoutSocket = 40000;
					HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);
					HttpClient client = new DefaultHttpClient(httpParameters);
					if(!isCancelRequest){
						HttpResponse response = client.execute(request);
						if(!isCancelRequest){
							if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
								obj=backResult.jXml().parserObject(response.getEntity().getContent(), backResult.backClass());
								RequestXml.this.success(obj);
							}else{
								RequestXml.this.failure(String.valueOf(response.getStatusLine().getStatusCode()));
							}
						}else{
							isCancelRequest=false;
						}
					}else{
						isCancelRequest=false;
					}
				} catch (Exception e) {
					RequestXml.this.error(e);
				}
			}
		});
	}
	
	private void requestGet(final BackResult backResult){
		executorService.submit(new Runnable() {
			@Override
			public void run() {
				Object obj=null;
				URL requestUrl;
				try {
					requestUrl = new URL(backResult.url().concat("?").concat(backResult.getParam()));
					if(!isCancelRequest){
						HttpURLConnection urlConnection = (HttpURLConnection) requestUrl
								.openConnection();
						urlConnection.setRequestMethod("GET");
						urlConnection.setConnectTimeout(5 * 1000);
						if(!isCancelRequest){
							if (urlConnection.getResponseCode() == 200) {
								obj=backResult.jXml().parserObject(urlConnection.getInputStream(), backResult.backClass());
								RequestXml.this.success(obj);
							}else{
								RequestXml.this.failure(String.valueOf(urlConnection.getResponseCode()));
							}
						}else{
							isCancelRequest=false;
						}
					}else{
						isCancelRequest=false;
					}
				} catch (Exception e) {
					RequestXml.this.error(e);
				} 
			}
		});
	}
}
