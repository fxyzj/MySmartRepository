package smartcarData;

/**
 * Created by fuxiaoyong on 16/11/11.
 *
 * 记录当前小车信息
 *
 */

public class StateInfo {


    /**
     * 陀螺仪的数据
     */
    private Gyro mGyro;
    /**
     * 当前坐标
     */
    private Coordinate mCoordinate;
    /**
     * 轮子旋转的圈数
     */
    private TurnsOfWheel mTurnsOfWheel;
    /**
     * 当前超声波的值
     */
    private Ultrasonic mUltrasonic;

    /**
     * 小车当前车头方向
     */
    private double current_Angle;



    public double getCurrent_Angle() {
        return current_Angle;
    }

    public void setCurrent_Angle(double current_Angle) {
        this.current_Angle = current_Angle;
    }

    public Gyro getmGyro() {
        return mGyro;
    }

    public void setmGyro(Gyro mGyro) {
        this.mGyro = mGyro;
    }

    public Coordinate getmCoordinate() {
        return mCoordinate;
    }

    public void setmCoordinate(Coordinate mCoordinate) {
        this.mCoordinate = mCoordinate;
    }

    public TurnsOfWheel getmTurnsOfWheel() {
        return mTurnsOfWheel;
    }

    public void setmTurnsOfWheel(TurnsOfWheel mTurnsOfWheel) {
        this.mTurnsOfWheel = mTurnsOfWheel;
    }

    public Ultrasonic getmUltrasonic() {
        return mUltrasonic;
    }

    public void setmUltrasonic(Ultrasonic mUltrasonic) {
        this.mUltrasonic = mUltrasonic;
    }
}
