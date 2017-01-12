package FileOperation;

import android.content.Context;
import android.os.Environment;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

/**
 *
 *
 * @category 主要用于将内容，写到文件里面
 */
public class AndroidFileOperate {

	private Context context = null;

	public Context getContext() {
		return context;
	}

	public void setContext(Context context) {
		this.context = context;
	}

	public AndroidFileOperate(Context context) {
		super();
		this.context = context;
	}

	/**
	 *
	 * @param content
	 *            需要存储的内容
	 */
	@SuppressWarnings("deprecation")
	/**
	 * 主要用于文件的操作,将内容写到，sd卡上面
	 * @param filename  文件的名称
	 * @param content   要保存的文件的内容
	 */
	public void saveFile(String filename, String content) {
		FileOutputStream out = null;

		try {
			out = context.openFileOutput(filename, Context.MODE_APPEND);
			content += "\n";
			out.write(content.getBytes("UTF-8"));

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				out.close();
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}

	}

	public void saveToSDCard(String filename, String filecontent) {
		FileOutputStream outputStream = null;
		try {
			File file = new File(Environment.getExternalStorageDirectory().getPath(),filename);
			outputStream = new FileOutputStream(file,true);
			filecontent = filecontent+"\n";
			outputStream.write(filecontent.getBytes("utf-8"));
			outputStream.flush();

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			try {
				outputStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}

}
