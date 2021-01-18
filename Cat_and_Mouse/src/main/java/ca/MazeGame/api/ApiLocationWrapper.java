package ca.MazeGame.api;
import ca.MazeGame.model.CellLocation;
import java.util.ArrayList;
import java.util.List;

/**
 * Wrapper for the CellLocation class
 */

public class ApiLocationWrapper {
    public int x;
    public int y;

    // MAY NEED TO CHANGE PARAMETERS HERE TO SUITE YOUR PROJECT
    public static ApiLocationWrapper makeFromCellLocation(CellLocation cell) {
        ApiLocationWrapper location = new ApiLocationWrapper();
        location.x = cell.getX();
        location.y = cell.getY();
        return location;
    }
    // MAY NEED TO CHANGE PARAMETERS HERE TO SUITE YOUR PROJECT
    public static List<ApiLocationWrapper> makeFromCellLocations(Iterable<CellLocation> cells) {
        List<ApiLocationWrapper> locations = new ArrayList<>();
        for (ApiLocationWrapper location : locations){
            for(CellLocation cell : cells){
                location.x = cell.getX();
                location.y = cell.getY();
            }
        }
        return locations;
    }

}
