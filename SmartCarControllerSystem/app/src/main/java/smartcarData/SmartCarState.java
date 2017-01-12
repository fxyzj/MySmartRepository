package smartcarData;

/**
 *
 * 小车当前位姿
 * 主要包括
 * 当前的坐标  coordinate
 * 当前的方向（相对于起点）angle
 * Created by fuxiaoyong on 16/11/17.
 */

public class SmartCarState {


    /**
     * 当前坐标点
     */
    private Coordinate carCoord;
    /**
     * 当前角度，这个是相对的就看你自己相对谁
     */
    private double car_current_angle;

    public TurnsOfWheel getTurnsOfWheel() {
        return turnsOfWheel;
    }

    public void setTurnsOfWheel(TurnsOfWheel turnsOfWheel) {
        this.turnsOfWheel = turnsOfWheel;
    }

    /**
     * 当前小车左右轮的圈数（编码器的数值）
     */
    private TurnsOfWheel turnsOfWheel;



    public SmartCarState(){}

    public SmartCarState(Coordinate carCoord, double car_current_angle) {
        this.car_current_angle = car_current_angle;
        this.carCoord = carCoord;
    }

    public SmartCarState(Coordinate carCoord, double car_current_angle, TurnsOfWheel turnsOfWheel) {
        this.carCoord = carCoord;
        this.car_current_angle = car_current_angle;
        this.turnsOfWheel = turnsOfWheel;
    }

    public Coordinate getCarCoord() {
        return carCoord;
    }

    public void setCarCoord(Coordinate carCoord) {
        this.carCoord = carCoord;
    }

    public double getCar_current_angle() {
        return car_current_angle;
    }

    public void setCar_current_angle(double car_current_angle) {
        this.car_current_angle = car_current_angle;
    }

    @Override
    public String toString() {
        String string = "(";
        string += String.valueOf(carCoord.getX())+",";
        string += String.valueOf(carCoord.getY())+",";
        string += String.valueOf(car_current_angle)+")";
        return  string;
    }
}
