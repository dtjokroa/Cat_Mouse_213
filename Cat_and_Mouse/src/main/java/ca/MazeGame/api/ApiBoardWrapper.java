package ca.MazeGame.api;
import ca.MazeGame.model.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Wrapper class for the Maze, contains static factory method function to convert a maze created in
 * the model to one that fits the api. Also has one private method to create the visibility and walls
 * of the maze
 */



public class ApiBoardWrapper {
    public int boardWidth;
    public int boardHeight;
    public ApiLocationWrapper mouseLocation;
    public ApiLocationWrapper cheeseLocation;
    public List<ApiLocationWrapper> catLocations;
    public boolean[][] hasWalls;
    public boolean[][] isVisible;

    public static ApiBoardWrapper makeFromGame(MazeGame game, boolean visibility) {
        ApiBoardWrapper wrapper = new ApiBoardWrapper();
        wrapper.mouseLocation = ApiLocationWrapper.makeFromCellLocation(game.getPlayerLocation());
        wrapper.cheeseLocation = ApiLocationWrapper.makeFromCellLocation((game.getCheeseLocation()));

        wrapper.catLocations = new ArrayList<>();
        List<Cat> cats = game.getCats();
        for (Cat cat: cats){
            wrapper.catLocations.add(ApiLocationWrapper.makeFromCellLocation(cat.getLocation()));
        }
        wrapper.setWallsAndVisibility(game, visibility);
        return wrapper;
    }

    private void setWallsAndVisibility(MazeGame game, boolean visibility){
        Maze maze = game.getMaze();
        this.boardHeight = maze.getHeight();
        this.boardWidth = maze.getWidth();

        CellState[][] cellStates = maze.getBoard();
        this.hasWalls = new boolean[this.boardHeight][this.boardWidth];
        this.isVisible = new boolean[this.boardHeight][this.boardWidth];

        for(int y = 0; y < this.boardHeight; y++) {
            for(int x = 0; x < this.boardWidth; x++){
                CellState tmp = cellStates[y][x];
                if(tmp.isWall()){
                    this.hasWalls[y][x]= true;
                }
                if(tmp.isVisible()){
                    this.isVisible[y][x] = true;
                }
                if(tmp.isHidden() && !visibility){
                    this.isVisible[y][x] = false;
                }
                if(tmp.isHidden() && visibility){
                    this.isVisible[y][x] = true;
                }
            }
        }

    }






}
