package baowei.cross;


import smartmessage.SmartMessage;

/**
 * 
 * @author zhangbaowei
 * 
 *         <pre>
 * 用于将识别的语音，里面包含的汉字，
 * 转化为与小车通信的字节数组
 * </pre>
 */
public class Word2Command {

	public static byte[] word2Command(String dataResult) {

		if (dataResult.contains("左")) {
			return SmartMessage.CMD_LEFT;
		} else if (dataResult.contains("右")) {
			return SmartMessage.CMD_RIGHT;
		} else if (dataResult.contains("前")) {
			return SmartMessage.CMD_FORWARD;
		} else if (dataResult.contains("后")) {
			return SmartMessage.CMD_BACKWARD;
		}
		// 没有符合条件的，就返回null
		return null;
	}
}
