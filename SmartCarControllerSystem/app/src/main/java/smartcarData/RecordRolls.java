package smartcarData;

/**
 * Created by fuxiaoyong on 16/11/23.
 *
 *使用方法
 * s
 *
 *
 */
/**
 * 1、创建旋转类
 * 2、开启一个新的旋转
 * 3、设置起始车轮旋转数
 * 4、获得当前车轮旋转数
 * 5、判断是否完成旋转
 *
 * 返回 true 说明完成当前旋转
 * 否则说明当前旋转未完成
 *
 *
 * RecordRolls recordRolls = new RecordRolls();
 recordRolls.startNewRoate();
 recordRolls.setStartTurnsOfWheel(new TurnsOfWheel(0,0));
 recordRolls.setCurrentTurnsOfWheel(new TurnsOfWheel(3,4));
 boolean flag = recordRolls.finishRoll(45);

 *
 *
 * 1.设置一定的角度
 * 2.获得要转的圈数
 * 3.获得初始圈数值
 * 4.  循环监听当前圈数值与当前圈数值之差
 * 5.   判断圈数差与要转的圈数之差，
 * 6.   如果大于等于0时，说明达到旋转目标
 * 7.   停止旋转
 *  if(recordRolls.isFlag()) { //如果上一次转角结束，那么才可以开启新的转动
        recordRolls.startNewRoate();
        recordRolls.setStartTurnsOfWheel(turnsOfWheel);
        dataSend(SmartMessage.CMD_LEFT);
    }
        recordRolls.setCurrentTurnsOfWheel(turnsOfWheel);
        if(recordRolls.finishRoll(90)){
        dataSend(SmartMessage.CMD_STOP);
    }
 *
 *
 *
 *
 */


public class RecordRolls {






    /**
     * 记录初始轮子圈数
     */
    private TurnsOfWheel startTurnsOfWheel;

    /**
     * 记录当前圈数
     */
    private TurnsOfWheel currentTurnsOfWheel;


    /**
     * 记录是否被用到
     */
    private boolean flag ;

    /**
     * 设置单例模式饿汉式
     */

//    public static RecordRolls instance = new RecordRolls();
//
//    private RecordRolls(){
//        setFlag(true);
//
//    }
//
//    public static RecordRolls getInstance(){
//        return  new RecordRolls();
//    }


    public RecordRolls() {
        setFlag(true);
    }


    public TurnsOfWheel getStartTurnsOfWheel() {
        return startTurnsOfWheel;
    }

    public void setStartTurnsOfWheel(TurnsOfWheel startTurnsOfWheel) {
        this.startTurnsOfWheel = startTurnsOfWheel;
    }

    public TurnsOfWheel getCurrentTurnsOfWheel() {
        return currentTurnsOfWheel;
    }

    public void setCurrentTurnsOfWheel(TurnsOfWheel currentTurnsOfWheel) {
        this.currentTurnsOfWheel = currentTurnsOfWheel;
    }

    public boolean isFlag() {
        return flag;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }

    /**
     *
     * @param angle 输入旋转的角度
     * @param r   旋转半径
     * @return 返回小车轮子编码器数 要转的数目
     */

    public static double rotate(double angle,double r){
        double T = (14.6* Math.PI/180)*angle *r;
        return T;

    }

    public static double rotate(double angle){

        double T = (14.6* Math.PI/180)*angle *22.1;
        return T;
    }


    /**
     * 开启新的旋转后，要将startTurnsOfWheel固定，不能多次赋值
     * 故将flag置为fasle
     */
    public void startNewRoate(){
        flag = false;
    }


    /**
     * 输入需要旋转的角度
     * 原理：
     *
     *
     * @param angle
     * @return
     */
    public boolean finishRoll(double angle){
        int left = currentTurnsOfWheel.getLeftTurns() - startTurnsOfWheel.getLeftTurns();
        int right = currentTurnsOfWheel.getRightTurns() - startTurnsOfWheel.getRightTurns();

        if(Math.abs( right+left)>=rotate(angle,25)){
            flag = true;
            return  true;
        }else {
            return  false;
        }
    }


}
