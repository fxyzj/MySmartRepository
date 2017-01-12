package smartcarData;

/**
 * Created by fuxiaoyong on 16/11/17.
 *
 * 路径规划类
 *
 * 输入两个点，算出
 *
 */

public class Road_Plan {


    /**
     * 起点
     */
    private Coordinate startCoord;
    /**
     * 终点
     */
    private Coordinate endCoord;
    /**
     * 起点与终点连线与x轴正向的夹角
     */
    private double startToendAngle;
    /**
     * 小车车头方向
     */
    private double carCurrentAngle;
    /**
     * 计算起点与终点间的距离
     */
    private double startToEndDistance;
    /**
     * 转向角度
     */
    private double turnAngle;


    public Road_Plan(Coordinate startCoord, Coordinate endCoord) {
        this.startCoord = startCoord;
        this.endCoord = endCoord;

        //获取起点和终点间的距离
        startToEndDistance = SmartCarUtils.twoPointDistance(startCoord,endCoord);

        //获得起点和终点的相对于x轴正向的角度
        startToendAngle = SmartCarUtils.turn_degreee(startCoord,endCoord);


    }
}
