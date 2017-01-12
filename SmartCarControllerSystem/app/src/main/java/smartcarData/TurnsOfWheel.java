package smartcarData;

/**
 * Created by fuxiaoyong on 16/11/11.
 * 轮子旋转的圈数
 *
 */

public class TurnsOfWheel {


    private int leftTurns;
    private int rightTurns;

    public TurnsOfWheel() {
    }



    public TurnsOfWheel(int leftTurns, int rightTurns) {
        this.leftTurns = leftTurns;
        this.rightTurns = rightTurns;
    }

    public int getLeftTurns() {
        return leftTurns;
    }

    public void setLeftTurns(int leftTurns) {
        this.leftTurns = leftTurns;
    }

    public int getRightTurns() {
        return rightTurns;
    }

    public void setRightTurns(int rightTurns) {
        this.rightTurns = rightTurns;
    }


    /**
     * 获取左轮移动的距离
     * @return
     */
    public double getLeftTurnsDis(){
        //单位为厘米
        return  leftTurns/14.6;
    }

    /**
     *
     * 获取右轮移动的距离
     * @return
     */
    public double getRightTurnsDis(){

        //单位为厘米
        return  rightTurns/14.6;
    }


    /**
     * 计算差值
     */
    public TurnsOfWheel dis(TurnsOfWheel wheel){

        TurnsOfWheel turnsOfWheel = new TurnsOfWheel();
        turnsOfWheel.setLeftTurns(this.getLeftTurns()-wheel.getLeftTurns());
        turnsOfWheel.setRightTurns(this.getRightTurns()-wheel.getRightTurns());
        return turnsOfWheel;
    }



}
