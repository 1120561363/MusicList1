package com.cn.lyric;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class LrcProcess {
	private List<LrcContent> LrcList;
	private LrcContent mLrcContent;
	public LrcProcess() {mLrcContent = new LrcContent();
		LrcList = new ArrayList<LrcContent>();
	}

	public String readLRC(String song_path) {
		
		StringBuilder stringBuilder = new StringBuilder();
		String name=song_path.replace(".mp3",".lrc");
		File f = new File(name);
		if(f.exists()){
		try {
			FileInputStream fis = new FileInputStream(f);
			InputStreamReader isr = new InputStreamReader(fis, "UTF-8");
			BufferedReader br = new BufferedReader(isr);
			String s = "";
			while ((s = br.readLine()) != null) {
				s = s.replace("[", "");
				s = s.replace("]", "@");
				// 分离"@"字符
				String splitLrc_data[] = s.split("@");
				if (splitLrc_data.length > 1) {
					mLrcContent.setLrc(splitLrc_data[1]);
					// 处理歌词取得歌曲时间
					int LrcTime = TimeStr(splitLrc_data[0]);
					mLrcContent.setLrc_time(LrcTime);
					// 添加进列表数组
					LrcList.add(mLrcContent);
					// 创建对象
					mLrcContent = new LrcContent();
				}
			}
			br.close();
			isr.close();
			fis.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			stringBuilder.append("");
		} catch (IOException e) {
			e.printStackTrace();
			stringBuilder.append("");
		}
		}
		else if(!f.exists()){
			return null;
		}
		return stringBuilder.toString();
	}
	/*
	 * 解析歌曲时间处理类
	 */
	public int TimeStr(String timeStr) {
		timeStr = timeStr.replace(":", ".");
		timeStr = timeStr.replace(".", "@");
		String timeData[] = timeStr.split("@");
		// 分离出分、秒并转换为整型
		int currentTime = 0;
		try{
		int minute = Integer.parseInt(timeData[0]);
		int second = Integer.parseInt(timeData[1]);
		int millisecond = Integer.parseInt(timeData[2]);
		//计算上一行与下一行的时间转换为毫秒数
		currentTime = (minute * 60 + second) * 1000 + millisecond * 10;
		}catch(Exception e){
			e.printStackTrace();
		}
		return currentTime;
	}
	public List<LrcContent> getLrcContent() {
		return LrcList;
	}
	/**
	 * 获得歌词和时间并返回的类
	 */
	public class LrcContent {
		private String Lrc;
		private int Lrc_time;
		
		public String getLrc() {
			return Lrc;
		}
		public void setLrc(String lrc) {
			Lrc = lrc;
		}
		public int getLrc_time() {
			return Lrc_time;
		}
		public void setLrc_time(int lrc_time) {
			Lrc_time = lrc_time;
		}
	}
}
