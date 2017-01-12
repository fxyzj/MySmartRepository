package smartcarData;

/**
 * Created by fuxiaoyong on 16/11/11.
 */

public class Coordinate {


    /**
     * Created by fuxiaoyong on 16/11/11.
     *
     * 坐标信息
     */


        private double x;
        private double y;


         public Coordinate(){

         }


        public Coordinate(double x,double y){
            this.x = x;
            this.y = y;
        }

        public double getX() {
            return x;
        }

        public void setX(double x) {
            this.x = x;
        }

        public double getY() {
            return y;
        }

        public void setY(double y) {
            this.y = y;
        }

    @Override
    public boolean equals(Object obj) {

        if( !(obj instanceof Coordinate)){
            return  false;}

        Coordinate tmp = (Coordinate) obj;

        if(tmp.getX() == x && tmp.getY() == y){
            return true;
        }else{
            return  false;
        }
    }
}
