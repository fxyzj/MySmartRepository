package baowei.cross;


import android.content.Context;

import baowei.word2voice.Word2Voice;


/**
 * 
 * @author zhangbaowei
 * 
 *         <pre>
 * 用于交叉路口发出声音
 * 1、您好，请告诉我是向左走，还是向右走？
 *     public void leftOrRight()
 * 2、到达交叉路口，请问应该怎么走？
 *     public void arriveCross()
 * 3、到达终点位置，导航结束
 * </pre>
 */
public class Word2VoiceCrossFactory {

	// 指的就是Activity
	private Context context;

	public Word2VoiceCrossFactory(Context context) {
		this.context = context;
	}

	public void leftOrRight() {
		Word2Voice word2Voice = new Word2Voice("58059c69", context);
		word2Voice.startSpeaking("您好，请告诉我是向左转，还是向右转，谢谢?");
	}

	public void arriveCross() {
		Word2Voice word2Voice = new Word2Voice("58059c69", context);
		word2Voice.startSpeaking("到达交叉路口，请问应该怎么走?");
	}

	public void arriveDistination() {
		Word2Voice word2Voice = new Word2Voice("58059c69", context);
		word2Voice.startSpeaking("到达终点位置，导航结束?");
	}
}
