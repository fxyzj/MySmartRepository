package smartcarData;

/**
 * Created by fuxiaoyong on 16/11/11.
 * 超声波数据
 */

public class Ultrasonic {


    /**
     * 用于储存数据信息
     */
    private int[] ultrasonics ;

    public Ultrasonic(){}

    public Ultrasonic(int[] ultrasonics) {
        this.ultrasonics = ultrasonics;
    }

    public int[] getUltrasonics() {
        return ultrasonics;
    }

    public void setUltrasonics(int[] ultrasonics) {
        this.ultrasonics = ultrasonics;
    }
}
