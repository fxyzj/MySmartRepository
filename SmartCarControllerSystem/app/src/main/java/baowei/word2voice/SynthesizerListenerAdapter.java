package baowei.word2voice;

import android.os.Bundle;

import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SynthesizerListener;

/**
 * 
 * @author zhangbaowei
 * 用于适配SynthesizerListener,防止过多的重写接口的实现
 */
public class SynthesizerListenerAdapter implements SynthesizerListener {
    
	@Override
	public void onBufferProgress(int arg0, int arg1, int arg2, String arg3) {
		
	}

	@Override
	public void onCompleted(SpeechError arg0) {
		
	}

	@Override
	public void onEvent(int arg0, int arg1, int arg2, Bundle arg3) {
		
	}

	@Override
	public void onSpeakBegin() {
		
	}

	@Override
	public void onSpeakPaused() {
		
	}

	@Override
	public void onSpeakProgress(int arg0, int arg1, int arg2) {
		
	}

	@Override
	public void onSpeakResumed() {
		
	}

}
