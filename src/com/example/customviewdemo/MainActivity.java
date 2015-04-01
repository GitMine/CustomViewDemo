package com.example.customviewdemo;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.content.Intent;
import android.net.TrafficStats;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.customviewdemo.myview.MyView_1;
import com.example.customviewdemo.myview.MyView_1.ViewData;

public class MainActivity extends Activity {
	public static final String TAG = "TAG";
	private String path = Environment.getExternalStorageDirectory()
			.getAbsolutePath() + "/wifiin/";

	private MyView_1 myView_1 = null;
	private Button btn = null;
	private Button btnCancle = null;
	private TextView txt = null;
	private Thread thread = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		this.myView_1 = (MyView_1) findViewById(R.id.myView_1);

		this.btn = (Button) findViewById(R.id.btn);
		this.btnCancle = (Button) findViewById(R.id.btn_cancle);
		this.txt = (TextView) findViewById(R.id.txt);

		this.btn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// startTestSpeed();
				// startTestNetSpeed();

				testAsyncTask();

			}
		});
		this.btnCancle.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (asyncTask != null && !asyncTask.isCancelled()) {
					Toast.makeText(MainActivity.this, "取消测速",
							Toast.LENGTH_SHORT).show();
					asyncTask.cancel(true);
				}
			}
		});

	}

	private void startTestNetSpeed() {
		System.out.println("开始测速");
		lastTotalRxBytes = getTotalRxBytes();
		lastTimeStamp = System.currentTimeMillis();
		new Timer().schedule(task, 1000, 300); // 1s后启动任务，每(2000)2s执行一次
	}

	private void startTestSpeed() {
		if (this.thread != null && this.thread.isAlive()) {
			Toast.makeText(this, "正在测速,请稍后", Toast.LENGTH_SHORT).show();
		} else {
			this.thread = new Thread() {
				public void run() {
					for (int i = 0; i <= 270; i++) {
						Message msg = handler.obtainMessage();
						try {
							Thread.sleep(500);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						msg.what = i;

						handler.sendMessage(msg);
					}
				};
			};
			this.thread.start();
		}

	}

	private List<Long> speeds = new ArrayList<Long>();

	private void showNetSpeed() {

		long nowTotalRxBytes = getTotalRxBytes();
		long nowTimeStamp = System.currentTimeMillis();
		long speed = ((nowTotalRxBytes - lastTotalRxBytes) * 1000 / (nowTimeStamp - lastTimeStamp));// 毫秒转换

		lastTimeStamp = nowTimeStamp;
		lastTotalRxBytes = nowTotalRxBytes;

		speeds.add(speed);
		long totalSpeed = 0;
		for (long s : speeds) {
			totalSpeed += s;
		}
		long finalSpeed = totalSpeed / speeds.size();
		Message msg = mHandler.obtainMessage();
		msg.what = 100;
		msg.obj = String.valueOf(speed) + " kb/s";
		msg.arg1 = (int) finalSpeed;
		Log.i(TAG, "showSpeed:" + speed);
		// mHandler.sendMessage(msg);// 更新界面
	}

	private long lastTotalRxBytes = 0;
	private long lastTimeStamp = 0;

	private long getTotalRxBytes() {
		return TrafficStats.getUidRxBytes(getApplicationInfo().uid) == TrafficStats.UNSUPPORTED ? 0
				: (TrafficStats.getTotalRxBytes() / 1024);// 转为KB
	}

	TimerTask task = new TimerTask() {
		@Override
		public void run() {
			showNetSpeed();
		}
	};

	private Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			ViewData data = myView_1.new ViewData();
			if (msg.what < 270) {
				data.setTestSpeedStatus("正在测速...");
			} else {
				data.setTestSpeedStatus("");
			}

			String wifiSpeed = String.valueOf(msg.arg1);
			data.setWifiSpeed(wifiSpeed);
			data.setSpeedUnit("kb/s");
			data.setWifiLinkStatus("已连接");
			data.setWifiName("ChinaUnicom");

			data.setSweepAngle(msg.arg1 / 7.0f);
			myView_1.setData(data);
		};
	};

	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			ViewData data = myView_1.new ViewData();
			if (msg.what < 270) {
				data.setTestSpeedStatus("正在测速...");
			} else {
				data.setTestSpeedStatus("");
			}

			String wifiSpeed = (int) msg.what * 7 + "";
			data.setWifiSpeed(wifiSpeed);
			data.setSpeedUnit("kb/s");
			data.setWifiLinkStatus("已连接");
			data.setWifiName("ChinaUnicom");
			data.setSweepAngle(msg.what);
			myView_1.setData(data);
		};
	};

	DownloadAsyncTask asyncTask = null;

	private void testAsyncTask() {
		Toast.makeText(this, "开始ping", Toast.LENGTH_SHORT).show();
		List<String> pingAddrs = new ArrayList<String>();
		pingAddrs.add("127.0.0.1");
		pingAddrs.add("0.0.0.0");
		pingAddrs.add("125.39.240.113");
		pingAddrs.add("61.135.169.121");
		pingAddrs.add("124.95.150.98");
		List<String> urls = new ArrayList<String>();
		// urls.add("http://gdown.baidu.com/data/wisegame/4f9b25fb0e093ac6/QQ_220.apk");
		// urls.add("http://www.wifiin.com/download/wifiin_android.apk");
		urls.add("https://www.baidu.com/img/bdlogo.png");
		urls.add("http://2.im.guokr.com/gkimage/7o/my/ey/7omyey.png");
		urls.add("http://2.im.guokr.com/gkimage/7o/my/ey/7omyey.png");
		urls.add("http://2.im.guokr.com/gkimage/7o/my/ey/7omyey.png");
		urls.add("http://2.im.guokr.com/gkimage/7o/my/ey/7omyey.png");
		urls.add("http://2.im.guokr.com/gkimage/7o/my/ey/7omyey.png");
		urls.add("http://2.im.guokr.com/gkimage/7o/my/ey/7omyey.png");
		urls.add("http://2.im.guokr.com/gkimage/7o/my/ey/7omyey.png");
		urls.add("http://2.im.guokr.com/gkimage/7o/my/ey/7omyey.png");
		urls.add("http://2.im.guokr.com/gkimage/7o/my/ey/7omyey.png");
		urls.add("http://2.im.guokr.com/gkimage/7o/my/ey/7omyey.png");
		urls.add("http://2.im.guokr.com/gkimage/7o/my/ey/7omyey.png");
		urls.add("http://2.im.guokr.com/gkimage/7o/my/ey/7omyey.png");
		urls.add("http://2.im.guokr.com/gkimage/7o/my/ey/7omyey.png");
		urls.add("http://2.im.guokr.com/gkimage/7o/my/ey/7omyey.png");
		urls.add("http://2.im.guokr.com/gkimage/7o/my/ey/7omyey.png");
		urls.add("http://2.im.guokr.com/gkimage/7o/my/ey/7omyey.png");
		urls.add("http://2.im.guokr.com/gkimage/7o/my/ey/7omyey.png");
		urls.add("http://2.im.guokr.com/gkimage/7o/my/ey/7omyey.png");
		urls.add("http://2.im.guokr.com/gkimage/7o/my/ey/7omyey.png");
		urls.add("http://2.im.guokr.com/gkimage/7o/my/ey/7omyey.png");
		urls.add("http://2.im.guokr.com/gkimage/7o/my/ey/7omyey.png");
		urls.add("http://2.im.guokr.com/gkimage/7o/my/ey/7omyey.png");
		urls.add("http://2.im.guokr.com/gkimage/7o/my/ey/7omyey.png");
		urls.add("http://2.im.guokr.com/gkimage/7o/my/ey/7omyey.png");
		urls.add("http://2.im.guokr.com/gkimage/7o/my/ey/7omyey.png");
		urls.add("http://2.im.guokr.com/gkimage/7o/my/ey/7omyey.png");
		asyncTask = new DownloadAsyncTask(urls, pingAddrs);
		asyncTask.execute();
		this.btn.setEnabled(false);// 按钮不可点击
	}

	private class DownloadAsyncTask extends AsyncTask<String, Long, String> {
		// 开始下载时间
		private long startTime = 0;
		// 已下载大小
		private long totalSize = 0;

		ViewData data = myView_1.new ViewData();

		private int pingDelay = 0;

		private List<String> pingAddrs = null;
		private List<String> urls = null;

		public DownloadAsyncTask(List<String> urlList, List<String> pingAddrList) {
			this.urls = urlList;
			this.pingAddrs = pingAddrList;
		}

		@Override
		protected void onPreExecute() {
			Toast.makeText(MainActivity.this, "开始测速", Toast.LENGTH_SHORT)
					.show();
			btn.setText("正在测速");
			btn.setEnabled(false);
		}

		@Override
		protected String doInBackground(String... params) {
			Log.i(TAG, "doInBackground(Params... params) called");
			publishProgress((long) 0, (long) 0);
			for (int i = 0; i < pingAddrs.size(); i++) {
				this.pingDelay += pingServer(pingAddrs.get(i));
			}

			startTime = System.currentTimeMillis();
			for (int i = 0; i < urls.size(); i++) {
				String urlStr = urls.get(i);
				String fileName = urlStr.substring(urlStr.lastIndexOf("/") + 1);
				File file = new File(path, fileName);
				if (file.exists()) {
					file.delete();
				}
				try {
					file.createNewFile();
				} catch (IOException e) {
					e.printStackTrace();
				}
				try {
					URL url = new URL(urlStr);
					HttpURLConnection urlConnection = (HttpURLConnection) url
							.openConnection();
					InputStream inputStream = urlConnection.getInputStream();
					System.out.println(fileName + "Length:"
							+ urlConnection.getContentLength() / 1024.0f + "Kb"
							+ "," + urlConnection.getContentLength() / 1024
							/ 1024.0f + "Mb");
					FileOutputStream fileOutputStream = new FileOutputStream(
							file);

					byte[] buffer = new byte[4 * 1024];
					int byteRead = 0;
					while ((byteRead = inputStream.read(buffer)) != -1) {
						totalSize += byteRead;
						fileOutputStream.write(buffer, 0, byteRead);
						publishProgress(totalSize, (long) 1);
					}

					fileOutputStream.close();

				} catch (MalformedURLException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
				Log.d(TAG, fileName + "下载完成");
				publishProgress(totalSize, (long) 2);
			}

			return null;
		}

		@Override
		protected void onProgressUpdate(Long... values) {

			if (values[1] == 0) {
				data.setSpeedUnit("kb/s");
				data.setSweepAngle(0);
				data.setTestSpeedStatus("正在ping...");
				data.setWifiLinkStatus("已连接");
				data.setWifiName("ChinaUnicom");
				data.setWifiSpeed(0 + "");
				myView_1.setData(data);
				return;
			}
			txt.setText("延时:" + (int) (this.pingDelay / this.pingAddrs.size())
					+ "ms");
			long usedTime = System.currentTimeMillis() - startTime;
			Log.v(TAG, "totalSize:" + values[0]);
			int speed = (int) ((values[0] / 1024 / (usedTime / 1000.0d)));
			Log.v(TAG, "speed:" + speed);
			data.setTestSpeedStatus("正在测速...");

			String wifiSpeed = String.valueOf(speed);
			data.setWifiSpeed(wifiSpeed);
			data.setSpeedUnit("kb/s");
			data.setWifiLinkStatus("已连接");
			data.setWifiName("ChinaUnicom");

			float angle = 0;
			if (speed > 0 && speed <= 100) {// 0~90度，最多100kb，每度100/90kb
				angle = speed / (100 / 90);
			} else if (speed > 100 && speed <= 2000) {// 90~180度，最多2000kb,每度(2000-100)/90kb
				angle = 90 + (speed - 100) / ((2000 - 100) / 90);
			} else if (speed > 2000 && speed <= 10000) {// 180~270度,最多10000kb,每度(10000-2000)/90kb
				angle = 180 + (speed - 2000) / ((10000 - 2000) / 90);
			}
			if (angle < 0) {
				angle = 0;
			} else if (angle > 270) {
				angle = 270;
			}
			data.setSweepAngle(angle);
			myView_1.setData(data);
		}

		@Override
		protected void onPostExecute(String result) {
			Toast.makeText(MainActivity.this, "测速完成", Toast.LENGTH_SHORT)
					.show();
			data.setTestSpeedStatus("");
			myView_1.setData(data);
			btn.setText("重新测速");
			btn.setEnabled(true);
		}

	}

	public int pingServer(String pingServerAddr) {

		String lost = new String();
		String delay = new String();
		StringBuffer strBuffer = new StringBuffer();
		int result = 0;

		Process p;
		try {
			p = Runtime.getRuntime().exec("ping -c 2 " + pingServerAddr);

			BufferedReader buf = new BufferedReader(new InputStreamReader(
					p.getInputStream()));
			String str = new String();

			while ((str = buf.readLine()) != null) {
				System.out.println("str:" + str);
				if (str.contains("packet loss")) {
					int i = str.indexOf("received");
					int j = str.indexOf("%");
					lost = str.substring(i + 10, j + 1);
					System.out.println("丢包率:" + lost);
					strBuffer.append("丢包率:" + lost);
				}
				if (str.contains("avg")) {
					int i = str.indexOf("/", 20);
					int j = str.indexOf(".", i);
					delay = str.substring(i + 1, j) + "ms";
					result = Integer.valueOf(str.subSequence(i + 1, j)
							.toString().trim());
					System.out.println("延时:" + delay);
					strBuffer.append(",延时:" + delay);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
			return result;
		}
		// this.txt.setText(strBuffer.toString());
		return result;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		} else if (id == R.id.action_second_activity) {
			Intent intent = new Intent(this, SecondActivity.class);
			startActivity(intent);
		}
		return super.onOptionsItemSelected(item);
	}
}
