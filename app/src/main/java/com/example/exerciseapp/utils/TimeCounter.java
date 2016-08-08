package com.example.exerciseapp.utils;

import android.widget.Button;

import java.util.Timer;
import java.util.TimerTask;

public class TimeCounter {

	int count = 30;
	private TimerTask timerTask;
	private Timer timer;
	private Button button;

	public TimeCounter(Button button) {
		this.button = button;
	}

	public void startCount() {
		timer = new Timer();
		timerTask = new TimerTask() {
			@Override
			public void run() {
				if (count > 0)
					button.setText("" + count);
				else
					button.setText("重新获取");
				count--;

			}
		};
		timer.schedule(timerTask, 0, 1000);
	}
}
