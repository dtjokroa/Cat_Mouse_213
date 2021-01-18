package ca.MazeGame.api;
import ca.MazeGame.model.MazeGame;
import ca.MazeGame.model.MoveDirection;

/**
 * Wrapper for the MazeGame Class, stores a model of the game in the modelGame variable,
 * used to update player location through user input and update cat locations. Also contains
 * 2 cheat methods which functions as listed in the REST API documentation.
 */

public class ApiGameWrapper {
    public int gameNumber;
    public boolean isGameWon;
    public boolean isGameLost;
    public int numCheeseFound;
    public int numCheeseGoal;
    public ApiBoardWrapper boardWrapper;
    public MazeGame modelGame;
    private boolean allVisible = false;

    public static ApiGameWrapper makeFromGame(MazeGame game, int id) {
        ApiGameWrapper wrapper = new ApiGameWrapper();
        wrapper.gameNumber = id;
        wrapper.numCheeseFound = game.getNumberCheeseCollected();
        wrapper.numCheeseGoal = game.getNumberCheeseToCollect();
        wrapper.isGameLost = game.hasUserLost();
        wrapper.isGameWon = game.hasUserWon();
        wrapper.boardWrapper = ApiBoardWrapper.makeFromGame(game, wrapper.allVisible);
        wrapper.modelGame = game;
        return wrapper;
    }

    private void updateVariables(){
        this.boardWrapper = ApiBoardWrapper.makeFromGame(modelGame, allVisible);
        this.numCheeseGoal = modelGame.getNumberCheeseToCollect();
        this.numCheeseFound = modelGame.getNumberCheeseCollected();
        this.isGameWon = modelGame.hasUserWon();
        this.isGameLost = modelGame.hasUserLost();
    }

    public boolean makeAMove(MoveDirection move){
        if (modelGame.recordPlayerMove(move)){
            updateVariables();
            return true;
        }
        return false;
    }

    public void moveCats(){
        modelGame.doCatMoves();
        updateVariables();
    }

    public void setNumOfCheese(int cheeseNum){
        modelGame.setNumberCheeseToCollect(cheeseNum);
        updateVariables();
    }

    public void setAllVisible() {
        this.boardWrapper = ApiBoardWrapper.makeFromGame(modelGame, true);
        this.allVisible = true;
    }







}
