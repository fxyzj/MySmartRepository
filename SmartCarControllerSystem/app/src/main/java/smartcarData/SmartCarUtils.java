package smartcarData;

/**
 * Created by fuxiaoyong on 16/11/17.
 */

public  class SmartCarUtils {


    /**
     * 通过传入编码器脉冲数
     * 计算出当前车轮行走的总距离
     * 单位为厘米cm
     * 其实也可以根据车轮半径来算 r = 42.5mm
     * @param roles 轮子圈数
     * @return
     */
    public static double distanceFromWheel(int roles){
        return  roles/14.6;
    }


    /**
     *
     * 这个有待商量
     *
     *
     * 通过传入起点与终点
     * 如果计算出来的值为负数
     * 说明终点在起点的左边
     * 计算出转向角的度数为负数
     * @param start
     * @param end
     * @return
     */
    public static double turn_degreee(Coordinate start, Coordinate end){

        if(start.equals(end)) return 0;

        double y = end.getY()-start.getY();
        double x = end.getX() - start.getX();

        double degree = Math.atan2(y,x);

        double tmp = degree*360/3.1416;

        /**
         *  通过向量先确定坐标所在的象限 如果（x，y）
         *  if(x>0&&y>0) 说明在第一象限
         *
         *
         */

        /**
         * 记录所在象限
         */
        int quadrant = 0;

        if(x>0&&y>0) {
            quadrant = 1;
        }
       else if(x<0&&y>0)
            quadrant = 2;
        else if(x>0&&y<0)
            quadrant = 4;
        else if(x<0&&y<0)
            quadrant = 3;

        switch (quadrant){
            case 1:
                tmp =tmp;
                break;
            case 2:
                tmp +=180;
                break;
            case 3:
                tmp += 180;
                break;
            case 4:
                tmp += 360;
                break;
            default:
                break;
        }

        return  tmp;
    }


    /**
     * 返回相对于起点的弧度
     *
     * 正值说明向左拐了，负值说明往右
     * 通过小车转的圈数
     *得到与初始方向的角度
     *
     * a = (Dr - Dl)/r 得到弧度
     * theta = a * 2pi/360;
     * @param turns 转的圈数
     * @param r
     */

    public static double currentAngleRelativeToStartpoint(TurnsOfWheel turns, double r){

        //zuo边车轮行驶的距离
        double distance_l = turns.getLeftTurnsDis();
        //右边车轮行驶的距离
        double distance_r = turns.getRightTurnsDis();

        double theta = ((distance_r - distance_l)/r);

        return theta;

    }

    public static double currentAngleRelativeToLastpoint(TurnsOfWheel current_turns,TurnsOfWheel last_turns, double r){

        //zuo边车轮行驶的距离
        double distance_l = current_turns.getLeftTurnsDis()-last_turns.getLeftTurnsDis();
        //右边车轮行驶的距离
        double distance_r = current_turns.getRightTurnsDis()-last_turns.getRightTurnsDis();

        double theta = ((distance_r - distance_l)/r);

        return theta;

    }

    /**
     * 计算两点之间的距离
     * @param start
     * @param end
     * @return
     */
    public static double twoPointDistance(Coordinate start, Coordinate end){

        double distance = Math.sqrt((start.getY() - end.getY())*(start.getY() - end.getY())
                            -       (start.getX() - end.getX())*(start.getX() - end.getX())

        );

        return distance;
    }



    /**
     * 根据起点坐标startState 和 小车左右轮移动的距离算出当前小车的坐标
     * @param startState  起点坐标
     * @param turnsOfWheel 当前小车转的圈数（这个圈数是从起点开始计算的）
     * @return 返回当前小车位姿
     *
     * 初步想法是这样的
     *
     *
     * 根据当前小车的行驶的转动的圈数，我们可以算出小车偏移的角度
     * 通过
     *
     *
     */
    public static SmartCarState currentState(SmartCarState startState , TurnsOfWheel turnsOfWheel){


        SmartCarState currentState = new SmartCarState();

        Coordinate currentCoord = new Coordinate();

        TurnsOfWheel turnsOfWheel1 = new TurnsOfWheel();

        //两个车轮间的距离 24.1还有一个是28.6 单位厘米
        double r = 25.1 ;
        //算出当前小车转的角度
        double degree = currentAngleRelativeToLastpoint(turnsOfWheel,startState.getTurnsOfWheel(),r);

        //左轮所走的距离
        double distanceLeft = distanceFromWheel(turnsOfWheel.getLeftTurns()-startState.getTurnsOfWheel().getLeftTurns());
        //右轮走的距离
        double distanceRight = distanceFromWheel(turnsOfWheel.getRightTurns()-startState.getTurnsOfWheel().getRightTurns());

        double averageDis = (distanceLeft + distanceRight)/2;

        //
        double y = averageDis* Math.sin((degree+startState.getCar_current_angle()* Math.PI/180));

        double x = averageDis* Math.cos((degree+startState.getCar_current_angle()* Math.PI/180));

        currentCoord.setX(startState.getCarCoord().getX() + x);
        currentCoord.setY(startState.getCarCoord().getY() + y);

        currentState.setCarCoord(currentCoord);
        currentState.setCar_current_angle(degree*180/ Math.PI + startState.getCar_current_angle());


        turnsOfWheel1.setLeftTurns(turnsOfWheel.getLeftTurns());
        turnsOfWheel1.setRightTurns(turnsOfWheel.getRightTurns());

        currentState.setTurnsOfWheel(turnsOfWheel1);

        return currentState;

    }


    /**
     * 通过两边车轮转的圈数来控制小车旋转的角度
     *
     *
     *
     */

    /**
     *
     * @param angle 输入旋转的角度
     * @param r   旋转半径
     * @return 返回小车轮子编码器数 要转的数目
     */

    public static double rotate(double angle,double r){
        double T = (14.6*180/ Math.PI)*angle *r;
        return T;

    }




}
