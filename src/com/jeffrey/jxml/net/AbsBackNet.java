package com.jeffrey.jxml.net;

/**
 * @author Jeffrey Shi
 * QQ 362116120
 * MAIL to shijunfan@163.com
 * 
 * ����Ϊ���ݷ��ؽӿ�
 */
public abstract class AbsBackNet {
	public abstract void success(Object obj);
	public abstract void failure(String errCode);
	public abstract void error(Exception e);
}
