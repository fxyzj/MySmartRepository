package smartcarData;

/**
 * Created by fuxiaoyong on 16/11/15.
 *
 * 目的地集合
 */

public class Destinations {

    private  int MAX_DEST_COORD_NUM = 5;
    private Coordinate coordinates[] ;

    public Destinations(Coordinate[] coordinates) {
        this.coordinates = coordinates;
        this.MAX_DEST_COORD_NUM = coordinates.length;
    }


    public int getMAX_DEST_COORD_NUM() {
        return MAX_DEST_COORD_NUM;
    }

    public void setMAX_DEST_COORD_NUM(int MAX_DEST_COORD_NUM) {
        this.MAX_DEST_COORD_NUM = MAX_DEST_COORD_NUM;
    }

    public Coordinate[] getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(Coordinate[] coordinates) {
        this.coordinates = coordinates;
    }
}
