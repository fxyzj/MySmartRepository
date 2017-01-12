package baowei.voice2word;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.RecognizerResult;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechUtility;
import com.iflytek.cloud.ui.RecognizerDialog;
import com.iflytek.cloud.ui.RecognizerDialogListener;

import java.util.List;

public class Voice2Word {

	/**
	 * 用于处理声音获取的结果
	 */
	public DataResultDealWith dataResultDealWith;
	/**
	 * 一般指的是MainActity
	 */
	private Context context;
	/**
	 * 初始化的监听对象，默认的实现是MyInitListener
	 */
	private InitListener initListener;

	public Voice2Word(DataResultDealWith dataResultDealWith, Context context,
			InitListener initListener) {
		this.dataResultDealWith = dataResultDealWith;
		this.context = context;
		this.initListener = initListener;
	}

	// 返回收听到的语言文字
	public String voice2Word() {
		// ①语音配置对象初始化
		SpeechUtility
				.createUtility(context, SpeechConstant.APPID + "=58059c69");// 将这里的578f1af7替换成自己申请得到的8位appid

		// ②初始化有交互动画的语音识别器
		// 有动画效果
		RecognizerDialog iatDialog = new RecognizerDialog(context, initListener);
		// ③设置监听，实现听写结果的回调
		iatDialog.setListener(new RecognizerDialogListener() {

			String resultJson = "[";// 放置在外边做类的变量则报错，会造成json格式不对（？）

			@Override
			public void onResult(RecognizerResult recognizerResult,
					boolean isLast) {
				if (!isLast) {
					resultJson += recognizerResult.getResultString() + ",";
				} else {
					resultJson += recognizerResult.getResultString() + "]";
				}

				if (isLast) {
					// 解析语音识别后返回的json格式的结果
					Gson gson = new Gson();
					List<DictationResult> resultList = gson.fromJson(
							resultJson, new TypeToken<List<DictationResult>>() {
							}.getType());
					String result = "";
					for (int i = 0; i < resultList.size() - 1; i++) {
						result += resultList.get(i).toString();
					}

					// 将对文字的处理以接口的形式抽象出去
					dataResultDealWith.dataResultDealWith(result);
					// Toast.makeText(context, result,
					// Toast.LENGTH_SHORT).show();
					// etText.setText(result);
					// 获取焦点
					// etText.requestFocus();
					// 将光标定位到文字最后，以便修改
					// etText.setSelection(result.length());
				}
			}

			@Override
			public void onError(SpeechError speechError) {
				// 自动生成的方法存根
				speechError.getPlainDescription(true);
			}
		});

		// 开始听写，需将sdk中的assets文件下的文件夹拷入项目的assets文件夹下（没有的话自己新建）
		iatDialog.show();
		return null;
	}

}
