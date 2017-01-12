package baowei.cross;

/**
 * 
 * @author zhangbaowei
 * 
 *         <pre>
 * 1、dataResult用于保存语音，识别的结果
 *       每次语音，识别后。下车都要对应的做一系列的操作。比如左转，右转
 * 2、flag表示的是，这个操作有没有做过
 *       默认为false 表示，还没有执行
 *            true  表示，已经执行过了
 * 3、重要的方法
 * public byte[] word2Command() {
 * 		return Word2Command.word2Command(this.getDataResult());
 * 	}
 * 主要作用，是将语音识别的汉字结果，
 *         转换为控制小车行驶的命令
 * </pre>
 * 
 */
public class Voice2WordResultTask {
	private String dataResult;
	private boolean flag = false;

	public Voice2WordResultTask(String dataResult) {
		// 默认情况下，flag为false，
		// 表示还没有去做这个操作
		this(dataResult, false);
	}

	private Voice2WordResultTask(String dataResult, boolean flag) {
		this.dataResult = dataResult;
		this.flag = flag;
	}

	/**
	 * 通过dataResult，获取对应的指令
	 * 
	 * @return 控制小车行驶的指令信息
	 */
	public byte[] word2Command() {
		return Word2Command.word2Command(this.getDataResult());
	}

	public String getDataResult() {
		return dataResult;
	}

	public void setDataResult(String dataResult) {
		this.dataResult = dataResult;
	}

	public boolean isFlag() {
		return flag;
	}

	public void setFlag(boolean flag) {
		this.flag = flag;
	}

}
