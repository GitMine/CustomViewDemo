package com.example.customviewdemo;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.example.customviewdemo.downloadutil.HttpDownloadUtil;

//创建一个HttpURLConnection对象
//HttpURLConnection urlConn=(HttpURLConnection)url.openConnection();
//获取一个InputStream对象
//InputStream is=urlConn.getInputStream();
//访问网络权限
//android.permission.INTERNET
//得到当前设备SD卡的目录
//Environment.getExternalStorageDirectory()
//访问SD卡的权限
//android.permission.WEITE_EXTERNAL_STORAGE
public class SecondActivity extends Activity {
	/** Called when the activity is first created. */
	HttpDownloadUtil httpDownloadUtil;
	private Button downloadTxt;
	private Button downloadMp3;

	public SecondActivity() {
		httpDownloadUtil = new HttpDownloadUtil();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.second_layout);
		downloadTxt = (Button) this.findViewById(R.id.downloadTxt);
		downloadTxt.setOnClickListener(new DownloadTxt());
		downloadMp3 = (Button) this.findViewById(R.id.downloadMp3);
		downloadMp3.setOnClickListener(new DownloadMp3());
	}

	class DownloadTxt implements OnClickListener {

		@Override
		public void onClick(View arg0) {
			String url = "http://www.baidu.com";
			System.out.println(httpDownloadUtil.downFile(url));
		}

	}

	class DownloadMp3 implements OnClickListener {

		@Override
		public void onClick(View arg0) {

			new Thread() {
				@Override
				public void run() {
					System.out.println("开始下载");
					String url = "http://media.ringring.vn/ringtone/realtone/0/0/161/165346.mp3";
					// int index=url.lastIndexOf("/");
					// String fileName=url.substring(index+1);
					int result = httpDownloadUtil.downFile(url, "music",
							"aa.mp3");
					System.out.println(result);
					System.out.println("下载完成");
				}
			}.start();

		}

	}
}