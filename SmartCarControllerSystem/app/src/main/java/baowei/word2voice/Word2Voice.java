package baowei.word2voice;

import android.content.Context;
import android.text.TextUtils;
import android.widget.Toast;

import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechSynthesizer;
import com.iflytek.cloud.SpeechUtility;
import com.iflytek.cloud.SynthesizerListener;

public class Word2Voice {

	/**
	 * 用于标识在讯飞平台上申请的AppID,可以为空字符串
	 */
	private String appId = "58059c69";
	/**
	 * 当前的MainActivity
	 */
	private Context context;

	/**
	 * 进行发出语音的人物的选择 默认是 xiaoyan
	 * 人物的选择,可以参照网站http://www.xfyun.cn/services/online_tts?tab_index=1
	 */
	private String voiceName = "aisjiuxu";

	/**
	 * 进行发出语音的人物的选择 默认是 xiaoyan
	 * 人物的选择,可以参照网站http://www.xfyun.cn/services/online_tts?tab_index=1
	 * 
	 * @param voiceName
	 *            人物的名称
	 */
	public void setVoiceName(String voiceName) {
		this.voiceName = voiceName;
	}

	/**
	 * 
	 * @param appId
	 *            讯飞平台上申请的app的key,如果沒有key,用另外一个构造函数
	 * @param context
	 *            一般指的是当前的MainActivity
	 */
	public Word2Voice(String appId, Context context) {
		this.appId = appId;
		this.context = context;
	}

	/**
	 * 
	 * @param context
	 *            一般指的是当前的MainActivity
	 */
	public Word2Voice(Context context) {
		this.context = context;
	}

	/**
	 * Word2Voice的实现 默认的SynthesizerListener的实现为SynthesizerListenerAdapter
	 * 
	 * @param dataVoice
	 *            要转换为语音的文字
	 * @return 成功返回0
	 */
	public int startSpeaking(String dataVoice) {
		// 进行与讯飞app的关联
		if (!TextUtils.isEmpty(appId)) {
			SpeechUtility.createUtility(context, SpeechConstant.APPID + "="
					+ appId);
		}

		// 1.创建SpeechSynthesizer对象,
		// 第一个参数为context
		// 第二个参数：本地合成时传InitListener
		SpeechSynthesizer mTts = SpeechSynthesizer.createSynthesizer(context,
				null);

		// 2.合成参数设置，详见《科大讯飞MSC API手册(Android)》SpeechSynthesizer 类
		// 清空参数
		mTts.setParameter(SpeechConstant.PARAMS, null);
		// 設置阐述
		mTts.setParameter(SpeechConstant.ENGINE_TYPE, SpeechConstant.TYPE_CLOUD); // 设置云端
		mTts.setParameter(SpeechConstant.VOICE_NAME, voiceName);// 设置发音人
		mTts.setParameter(SpeechConstant.SPEED, "50");// 设置语速
		// 设置合成音调
		mTts.setParameter(SpeechConstant.PITCH, "50");
		mTts.setParameter(SpeechConstant.VOLUME, "80");// 设置音量，范围0~100
		mTts.setParameter(SpeechConstant.STREAM_TYPE, "3");

		// 3.开始合成
		int code = mTts.startSpeaking(dataVoice,
				new SynthesizerListenerAdapter());

		if (code != ErrorCode.SUCCESS) {
			if (code == ErrorCode.ERROR_COMPONENT_NOT_INSTALLED) {
				// 上面的语音配置对象为初始化时：
				Toast.makeText(context, "语音组件未安装", Toast.LENGTH_LONG).show();
			} else {
				Toast.makeText(context, "语音合成失败,错误码: " + code,
						Toast.LENGTH_LONG).show();
			}
		}

		return code;

	}

	/**
	 * Word2Voice的实现 默认的SynthesizerListener的实现为SynthesizerListenerAdapter
	 * 
	 * @param dataVoice
	 * @param synthesizerListener
	 *            可以选择实现SynthesizerListener或者利用SynthesizerListenerAdapter来重写方法
	 * @return
	 */
	public int startSpeaking(String dataVoice,
			SynthesizerListener synthesizerListener) {
		// 进行与讯飞app的关联
		if (!TextUtils.isEmpty(appId)) {
			SpeechUtility.createUtility(context, SpeechConstant.APPID
					+ "=5805c477");
		}

		// 1.创建SpeechSynthesizer对象,
		// 第一个参数为context
		// 第二个参数：本地合成时传InitListener
		SpeechSynthesizer mTts = SpeechSynthesizer.createSynthesizer(context,
				null);

		// 2.合成参数设置，详见《科大讯飞MSC API手册(Android)》SpeechSynthesizer 类
		// 清空参数
		mTts.setParameter(SpeechConstant.PARAMS, null);
		// 設置阐述
		mTts.setParameter(SpeechConstant.ENGINE_TYPE, SpeechConstant.TYPE_CLOUD); // 设置云端
		mTts.setParameter(SpeechConstant.VOICE_NAME, voiceName);// 设置发音人
		mTts.setParameter(SpeechConstant.SPEED, "50");// 设置语速
		// 设置合成音调
		mTts.setParameter(SpeechConstant.PITCH, "50");
		mTts.setParameter(SpeechConstant.VOLUME, "80");// 设置音量，范围0~100
		mTts.setParameter(SpeechConstant.STREAM_TYPE, "3");

		// 3.开始合成
		int code = mTts.startSpeaking(dataVoice, synthesizerListener);
		if (code != ErrorCode.SUCCESS) {
			if (code == ErrorCode.ERROR_COMPONENT_NOT_INSTALLED) {
				// 上面的语音配置对象为初始化时：
				Toast.makeText(context, "语音组件未安装", Toast.LENGTH_LONG).show();
			} else {
				Toast.makeText(context, "语音合成失败,错误码: " + code,
						Toast.LENGTH_LONG).show();
			}
		}
		return code;
	}

}
