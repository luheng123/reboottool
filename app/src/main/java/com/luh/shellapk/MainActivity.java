package com.luh.shellapk;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.http.util.EncodingUtils;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;

import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {

	private int recLen;
	private int rctime;
	private TextView txtView;
	private TextView rctimev;
	private EditText relytime;
	private EditText et1;
	private Button bt1;
	private Button bt2;
	private Button bt3;
	Timer timer = new Timer();
	TimerTask task = new TimerTask() {
		@Override
		public void run() {

			runOnUiThread(new Runnable() { // UI thread
				@Override
				public void run() {
					recLen--;
					txtView.setText("" + recLen);
					if (recLen < 0) {
						timer.cancel();
						txtView.setText("reboot!");
						reboot();

					}
				}
			});
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		bt1 = (Button) findViewById(R.id.reboot1);
		bt2 = (Button) findViewById(R.id.reboot2);
		bt3 = (Button) findViewById(R.id.reboot3);
		txtView = (TextView) findViewById(R.id.textView1);
		rctimev = (TextView) findViewById(R.id.rctimev);
		relytime = (EditText) findViewById(R.id.relytime);
		et1 = (EditText) findViewById(R.id.editText1);

		bt1.setText("写入");
		bt2.setText("停止重启");
		bt3.setText("开始重启");
		bt3.setEnabled(false);
		try {
			rctime = Integer.parseInt(readFile("123.txt"));

			rctimev.setText("" + rctime);

			if (rctime != 0) {
				
				bt3.setKeepScreenOn(true);
				rctime--;
				writeFile("123.txt", "" + rctime);
				timer.schedule(task, 1000, 1000);

			} else {
				writeFile("321.txt", "0");
				bt3.setKeepScreenOn(false);

			}

			recLen = Integer.parseInt(readFile("321.txt"));
			txtView.setText("" + recLen);

		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void Btn1_Click(View view) throws IOException {
		/*
		 * Toast.makeText(getApplicationContext(), reboot, Toast.LENGTH_SHORT)
		 * .show();
		 */
		// reboot();
		if (relytime.getText().toString().length() != 0
				&& et1.getText().toString().length() != 0) {

			writeFile("321.txt", relytime.getText().toString());
			writeFile("123.txt", et1.getText().toString());
			rctimev.setText(et1.getText().toString());
			txtView.setText(relytime.getText().toString());
			recLen = Integer.parseInt(relytime.getText().toString());
			bt3.setEnabled(true);
		} else {
			Toast.makeText(getApplicationContext(), "数据不完整，写入失败，沿用之前配置",
					Toast.LENGTH_SHORT).show();
		}

	}

	public void Btn2_Click(View view) throws IOException {

		// reboot();
		writeFile("123.txt", "0");
		writeFile("321.txt", "0");
		// rctimev.setText("0");
		timer.cancel();
		Toast.makeText(getApplicationContext(), "有緣再見^.^", Toast.LENGTH_SHORT)
				.show();
		System.exit(0);

	}

	public void Btn3_Click(View view) throws IOException {

		rctime = Integer.parseInt(readFile("123.txt"));
		if (rctime > 0) {

			timer.schedule(task, 1000, 1000); // timeTask
			rctime--;
			writeFile("123.txt", "" + rctime);
			bt1.setEnabled(false);
			bt3.setEnabled(false);
			bt3.setKeepScreenOn(true);
		} else {

			Toast.makeText(getApplicationContext(), "别开玩笑了！0次？",
					Toast.LENGTH_SHORT).show();

		}

	}

	private void reboot() {
		Intent intent = new Intent();
		intent.setAction(Intent.ACTION_REBOOT);
		intent.putExtra("nowait", 1);
		intent.putExtra("interval", 1);
		intent.putExtra("window", 0);
		sendBroadcast(intent);
	}

	// 写数据
	public void writeFile(String fileName, String writestr) throws IOException {
		try {

			FileOutputStream fout = openFileOutput(fileName, MODE_PRIVATE);

			byte[] bytes = writestr.getBytes();

			fout.write(bytes);

			fout.close();
		}

		catch (Exception e) {
			e.printStackTrace();
		}
	}

	// 读数据
	public String readFile(String fileName) throws IOException {
		String res = "";
		try {
			FileInputStream fin = openFileInput(fileName);
			int length = fin.available();
			byte[] buffer = new byte[length];
			fin.read(buffer);
			res = EncodingUtils.getString(buffer, "UTF-8");
			fin.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return res;

	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_BACK) {

			try {
				writeFile("321.txt", "0");
				writeFile("123.txt", "0");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			// rctimev.setText("0");
			timer.cancel();
			Toast.makeText(getApplicationContext(), "有緣再見^.^",
					Toast.LENGTH_SHORT).show();
			System.exit(0);
		}
		return false;
	}

	
}
