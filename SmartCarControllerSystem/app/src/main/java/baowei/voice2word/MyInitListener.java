package baowei.voice2word;

import android.content.Context;
import android.widget.Toast;

import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.InitListener;

public class MyInitListener implements InitListener {

	/**
	 * 一般指的是MainActity
	 */
	private Context context;

	public MyInitListener(Context context) {
		this.context = context;
	}

	@Override
	public void onInit(int code) {
		if (code != ErrorCode.SUCCESS) {
			Toast.makeText(context, "初始化失败，错误码：" + code, Toast.LENGTH_SHORT)
					.show();
		}
	}

}
