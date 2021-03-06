package com.example.musiclist;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.ActivityGroup;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.cn.sava.MusicNum;
import com.cn.ui.ScreenInfo;
import com.example.love.ToTime;

@SuppressWarnings("deprecation")
@SuppressLint("ShowToast")
@TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
public class Index extends ActivityGroup {

	public static final String REMEMBER_USERID_KEY9 = "remember9";
	private ImageButton play_main;
	private TextView musicname_main, main_singer;
	private RelativeLayout relative_main;
	ToTime toTime;
	private Close close;
	private List<Music> lists;
	private MyCompletionListner2 completionListner2;
	private MyProgressBroadCa receiver;
	SharedPreferences localSharedPreferences;
	private Animation myAnimation;
	static ImageView mainindexback, tcursor;
	ViewPager viewPager;
	ScreenInfo s;
	private ArrayList<View> pageViews;
	int aaa;
	Button textmusic, textsinger, textlist, textonline;
	int screenW;
	float predree = 0.0f;

	int current = 0;
	ScaleAnimation scaleAnimation;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		s = new ScreenInfo(Index.this);
		screenW = s.getWidth();
		viewPager = (ViewPager) findViewById(R.id.vPager);
		RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) viewPager
				.getLayoutParams();
		layoutParams.height = s.getHeight() * 2 / 5;
		viewPager.setLayoutParams(layoutParams);
		close = new Close();
		IntentFilter filter22 = new IntentFilter("com.sleep.close");
		this.registerReceiver(close, filter22);
		toTime = new ToTime();

		findviews();
		RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) tcursor
				.getLayoutParams();
		params.width = screenW / 4;
		tcursor.setLayoutParams(params);
		Button titles[] = { textmusic, textsinger, textlist, textonline };

		completionListner2 = new MyCompletionListner2();
		IntentFilter filter21 = new IntentFilter("com.cn.musicserviceplayer");
		this.registerReceiver(completionListner2, filter21);

		receiver = new MyProgressBroadCa();
		IntentFilter filter = new IntentFilter("cn.com.karl.progress");
		this.registerReceiver(receiver, filter);

		lists = MusicList.getMusicData(this);
		localSharedPreferences = getSharedPreferences("music", 0);

		for (int i = 0; i <= 3; i++) {
			final int s = i;
			titles[s].setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View arg0) {
					setcurrent(s);
					viewPager.setCurrentItem(s);
				}
			});
		}
		relative_main.setOnClickListener(new OnClickListener());

		play_main.setOnClickListener(new OnClickListener());
		run = true;
		handler.postDelayed(task, 10);
	}

	protected void setcurrent(int arg0) {
		Button titles[] = { textmusic, textsinger, textlist, textonline };
		for (int i = 0; i < 4; i++) {
			final int a = i;
			if (a == arg0) {
				titles[a].setTextColor(Color.parseColor("#ffffff"));
			} else {
				titles[a].setTextColor(Color.parseColor("#adadad"));
			}
		}
	}

	void InItView() {
		pageViews = new ArrayList<View>();
		View view01 = getLocalActivityManager().startActivity("activity01",
				new Intent(this, com.example.musiclist.IMainActivity.class))
				.getDecorView();
		View view02 = getLocalActivityManager().startActivity("activity02",
				new Intent(this, com.example.musiclist.ArtistsActivity.class))
				.getDecorView();
		View view03 = getLocalActivityManager().startActivity("activity03",
				new Intent(this, com.example.musiclist.List.class))
				.getDecorView();
		View view04 = getLocalActivityManager().startActivity("activity04",
				new Intent(this, com.example.musiclist.Online.class))
				.getDecorView();
		pageViews.add(view01);
		pageViews.add(view02);
		pageViews.add(view03);
		pageViews.add(view04);
	}

	private void setb() {
		Uri uri = Uri.parse(localSharedPreferences.getString("background", ""));
		if (String.valueOf(uri).length() > 4) {
			ContentResolver contentResolver = this.getContentResolver();
			Bitmap bitmap = null;
			try {
				bitmap = BitmapFactory.decodeStream(contentResolver
						.openInputStream(uri));
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (OutOfMemoryError e) {
				e.printStackTrace();
				mainindexback
						.setBackgroundResource(R.drawable.video_stub_small);
				Toast.makeText(getApplicationContext(), "抱歉，内存不足设置失败！", 1)
						.show();
			}
			mainindexback.setImageBitmap(bitmap);
			mainindexback.setVisibility(View.VISIBLE);
			mainindexback.startAnimation(myAnimation);
		}
	}

	private void findviews() {
		myAnimation = AnimationUtils.loadAnimation(this, 0x7f040001);
		play_main = (ImageButton) this.findViewById(R.id.play_main);
		mainindexback = (ImageView) this.findViewById(R.id.mainindexbac);
		tcursor = (ImageView) this.findViewById(R.id.tcursor);
		musicname_main = (TextView) this.findViewById(R.id.musicname_main);
		relative_main = (RelativeLayout) this
				.findViewById(R.id.relative_mainindex);
		main_singer = (TextView) this.findViewById(R.id.main_singer);
		textmusic = (Button) this.findViewById(R.id.textmusic);
		textsinger = (Button) this.findViewById(R.id.textsinger);
		textlist = (Button) this.findViewById(R.id.textlist);
		textonline = (Button) this.findViewById(R.id.textonline);
	}

	@Override
	protected void onResume() {
		if (MusicNum.getbtn(18)) {
			setb();
			MusicNum.putusbtn(18, false);
		}

		super.onResume();
	}

	private class MyCompletionListner2 extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			int id1 = intent.getIntExtra("_id", MusicService._id);
			if (id1 <= lists.size()) {
				Music m = lists.get(id1);
				musicname_main.setText(m.getTitle());
				if (m.getSinger().equals("未知艺术家")) {
					main_singer.setText("music");
				} else {
					main_singer.setText(m.getSinger());
				}
				play_main.setBackgroundResource(R.drawable.pause1);
			}
		}
	}

	@SuppressLint("ShowToast")
	@Override
	protected void onStart() {
		if (MusicService.nowplay) {
			play_main.setBackgroundResource(R.drawable.pause1);
			try {

				Music m = lists.get(MusicService._id);
				musicname_main.setText(lists.get(MusicService._id).getTitle());
				if (m.getSinger().equals("未知艺术家")) {
					main_singer.setText("music");
				} else {
					main_singer.setText(m.getSinger());
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			play_main.setBackgroundResource(R.drawable.play1);
			if (MusicService.player == null) {
				int a = Integer.valueOf(localSharedPreferences.getInt(
						"currentId", 0));
				try {
					Music mm = lists.get(a);
					musicname_main.setText(mm.getTitle());
					if (mm.getSinger().equals("未知艺术家")) {
						main_singer.setText("music");
					} else {
						main_singer.setText(mm.getSinger());
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else {
				try {
					Music mmm = lists.get(MusicService._id);
					musicname_main.setText(mmm.getTitle());
					if (mmm.getSinger().equals("未知艺术家")) {
						main_singer.setText("music");
					} else {
						main_singer.setText(mmm.getSinger());
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

		}

		super.onStart();
	}

	public class Close extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			finish();
			play_main.setBackgroundResource(R.drawable.play1);
		}
	}

	private long count = 0;
	private boolean run = false;

	private Handler handler = new Handler();

	private Runnable task = new Runnable() {

		public void run() {
			if (run) {
				handler.postDelayed(this, 10);
				count++;

			}
			if (count > 1) {
				setb();
				InItView();
				setcurrent(0);
				viewPager.setAdapter(new myPagerView());
				viewPager.setOnPageChangeListener(new OnPageChangeListener() {
					@Override
					public void onPageSelected(int arg0) {
						setcurrent(arg0);
					}

					@Override
					public void onPageScrolled(int arg0, float arg1, int arg2) {
						if (arg1 != 0) {
							TranslateAnimation animation = new TranslateAnimation(
									predree * screenW / 4 + current, arg1
											* screenW / 4 + arg0 * screenW / 4,
									0, 0);
							animation.setDuration(200);
							animation.setFillAfter(true);

							tcursor.startAnimation(animation);
							predree = arg1;
							current = arg0 * screenW / 4;
						}
					}

					@Override
					public void onPageScrollStateChanged(int arg0) {
					}
				});
				run = false;
			}
		}
	};

	@Override
	protected void onDestroy() {
		this.unregisterReceiver(close);
		this.unregisterReceiver(completionListner2);
		this.unregisterReceiver(receiver);
		super.onDestroy();
	}

	@SuppressLint("ShowToast")
	public class OnClickListener implements android.view.View.OnClickListener {

		@Override
		public void onClick(View v) {
			if (v == relative_main) {
				Intent intent3 = new Intent(Index.this, MusicActivity.class);
				startActivity(intent3);
			} else if (v == play_main) {
				if (lists.size() > 0) {
					if (MusicService.player != null
							&& MusicService.player.isPlaying()) {
						play_main.setBackgroundResource(R.drawable.play1);
					} else {
						play_main.setBackgroundResource(R.drawable.pause1);
					}
					// 播放
					Intent intents = new Intent(Index.this, MusicService.class);
					intents.putExtra("play", 3);
					MusicNum.putplay(3);
					MusicNum.putisok(true);
					startService(intents);
				} else {
					Toast.makeText(getApplicationContext(), "您的播放列表为空", 1)
							.show();
				}
			}
		}
	}

	public class MyProgressBroadCa extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			if (MusicService.player != null) {
				if (MusicService.player.isPlaying()) {
					play_main.setBackgroundResource(R.drawable.pause1);
				} else {
					play_main.setBackgroundResource(R.drawable.play1);
				}
			}
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// menu.add(0,1,0,"退出"); //添加选项
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@SuppressLint("ShowToast")
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == R.id.exit) {
			Intent intent = new Intent("com.sleep.close");
			sendBroadcast(intent);
			finish();
		}
		if (item.getItemId() == R.id.about) {
			Intent intent1 = new Intent(Index.this, About.class);
			startActivity(intent1);
		}
		if (item.getItemId() == R.id.deletecurrent) {
			if (MusicService.nowplay) {
				File f = new File(lists.get(MusicService._id).getUrl());
				if (f.exists()) {
					try {
						f.delete();
						if (MusicService.his > 0) {
							MusicService.his -= 1;
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
					MusicNum.putint(3);
					Intent intent = new Intent(Index.this, MusicService.class);
					MusicNum.putplay(5);
					MusicNum.putisok(true);
					startService(intent);
					Toast.makeText(getApplicationContext(), "删除成功！", 1).show();
				}
			} else {
				Toast.makeText(getApplicationContext(), "抱歉,当前没有歌曲在播放！", 1)
						.show();
			}
		}
		if (item.getItemId() == R.id.entersleep) {
			Intent intent1 = new Intent(Index.this, Sleep.class);
			startActivity(intent1);
		}
		if (item.getItemId() == R.id.menusearch) {
			Intent intent1 = new Intent(Index.this, Search.class);
			startActivity(intent1);
		}
		if (item.getItemId() == R.id.setting) {
			Intent intent1 = new Intent(Index.this, Setting.class);
			startActivity(intent1);
		}
		if (item.getItemId() == R.id.settingring) {
			if (MusicService.player == null) {
				Toast.makeText(getApplicationContext(), "当前音乐为空！", 1).show();
			} else {
				try {
					Music m = lists.get(MusicService._id);
					String path = m.getUrl();
					setMyRingtone(path);
					Toast.makeText(getApplicationContext(), "设置铃声成功！", 1)
							.show();
				} catch (Exception e) {
					e.printStackTrace();
					Toast.makeText(getApplicationContext(), "设置铃声失败！", 1)
							.show();
				}
			}
		}
		return super.onOptionsItemSelected(item);
	}

	public void setMyRingtone(String path) {
		File file = new File(path);
		ContentValues values = new ContentValues();
		values.put(MediaStore.MediaColumns.DATA, file.getAbsolutePath());
		values.put(MediaStore.MediaColumns.MIME_TYPE, "audio/mp3");
		values.put(MediaStore.Audio.Media.IS_RINGTONE, true);
		values.put(MediaStore.Audio.Media.IS_NOTIFICATION, false);
		values.put(MediaStore.Audio.Media.IS_ALARM, false);
		values.put(MediaStore.Audio.Media.IS_MUSIC, false);
		Uri uri = MediaStore.Audio.Media.getContentUriForPath(file
				.getAbsolutePath());
		Uri newUri = this.getContentResolver().insert(uri, values);
		RingtoneManager.setActualDefaultRingtoneUri(this,
				RingtoneManager.TYPE_RINGTONE, newUri);
	}

	class myPagerView extends PagerAdapter {
		// 显示数目
		@Override
		public int getCount() {
			return pageViews.size();
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}

		@Override
		public int getItemPosition(Object object) {
			return super.getItemPosition(object);
		}

		@Override
		public void destroyItem(View arg0, int arg1, Object arg2) {
			((ViewPager) arg0).removeView(pageViews.get(arg1));
		}

		@Override
		public Object instantiateItem(View arg0, int arg1) {
			((ViewPager) arg0).addView(pageViews.get(arg1));
			return pageViews.get(arg1);
		}

	}

	// int startY = 0;
	//
	// @Override
	// public boolean onTouch(View arg0, MotionEvent arg1) {
	// // if (arg1.getAction() == MotionEvent.ACTION_DOWN) {
	// // startY = (int) arg1.getRawY();
	// // }
	// // if (arg1.getAction() == MotionEvent.ACTION_MOVE) {
	// // int a = (int) arg1.getRawY() - startY;
	// // viewPager.layout(viewPager.getLeft(), viewPager.getTop() + a,
	// // viewPager.getRight(), viewPager.getBottom());
	// // startY = (int) arg1.getRawY();
	// // }
	// // return false;
	// }
}
