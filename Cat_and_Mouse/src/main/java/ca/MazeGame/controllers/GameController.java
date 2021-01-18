package ca.MazeGame.controllers;
import ca.MazeGame.api.ApiBoardWrapper;
import ca.MazeGame.api.ApiGameWrapper;
import ca.MazeGame.model.MazeGame;
import ca.MazeGame.model.MoveDirection;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

/**
 * GameController class containing all the methods as listed in the REST API documentation with
 * an additional getMoveDirection method to help with the /api/games/{id}/moves endpoint, as well as 2
 * Exception Handlers handling illegal arguments and bad inputs/requests.
 *
 */

@RestController
public class GameController {
    private Map<Integer , ApiGameWrapper > gameWrappers = new HashMap<>();
    private AtomicLong nextId = new AtomicLong();

    @GetMapping("/api/about")
    public String getAbout (){
        return "Denzel Tjokroardi";
    }

    @GetMapping("/api/games")
    public Map<Integer, ApiGameWrapper> getAllGames(){
        return gameWrappers;
    }

    @ResponseStatus(value = HttpStatus.CREATED)
    @PostMapping("/api/games")
    public ApiGameWrapper createNewGame(){
        MazeGame newGame = new MazeGame();
        int id = (int)nextId.incrementAndGet();
        ApiGameWrapper newWrapper = ApiGameWrapper.makeFromGame(newGame, id);
        gameWrappers.put(id, newWrapper);
        return newWrapper;
    }

    @GetMapping("/api/games/{id}")
    public ApiGameWrapper getOneGame(@PathVariable("id") long gameId){
        for (int id : gameWrappers.keySet()){
            if(id == gameId){
                return gameWrappers.get(id);
            }
        }
        throw new IllegalArgumentException();
    }


    @GetMapping("/api/games/{id}/board")
    public ApiBoardWrapper getCurrentBoard(@PathVariable("id") long gameId){
        ApiGameWrapper gameWrapper;
        for (int id: gameWrappers.keySet()) {
            if (id == gameId) {
                gameWrapper = gameWrappers.get(id);
                return gameWrapper.boardWrapper;
            }
        }
        throw new IllegalArgumentException();
    }

    @PostMapping("/api/games/{id}/moves")
    @ResponseStatus(value = HttpStatus.ACCEPTED)
    public void makeAMove(@PathVariable("id") long gameId, @RequestBody String move) throws IOException {
        for (int id : gameWrappers.keySet()) {
            if (id == gameId) {
                ApiGameWrapper gameWrapper = gameWrappers.get(id);
                if(move.equals("MOVE_CATS")){
                    gameWrapper.moveCats();
                    return;
                }
                else if(move.equals("MOVE_UP") ||
                        move.equals("MOVE_DOWN") ||
                        move.equals("MOVE_LEFT") ||
                        move.equals("MOVE_RIGHT")){
                    MoveDirection moveDirection = getMoveDirection(move);
                    if(gameWrapper.makeAMove(moveDirection)){
                        return;
                    }
                    throw new IOException();
                }
                else{
                    throw new IOException();
                }
            }
        }
        throw new IllegalArgumentException();
    }


    private MoveDirection getMoveDirection (String move){
        switch(move){
            case "MOVE_UP":
                return MoveDirection.MOVE_UP;
            case "MOVE_DOWN":
                return MoveDirection.MOVE_DOWN;
            case "MOVE_LEFT":
                return MoveDirection.MOVE_LEFT.MOVE_LEFT;
            case "MOVE_RIGHT":
                return MoveDirection.MOVE_RIGHT;
        }
        return MoveDirection.MOVE_NONE;
    }

    @PostMapping("/api/games/{id}/cheatstate")
    @ResponseStatus(value = HttpStatus.ACCEPTED)
    public void postCheatState(@PathVariable("id") long gameId, @RequestBody String cheatRequest) throws IOException{
        for(int id : gameWrappers.keySet()){
            if(gameId == id){
                ApiGameWrapper gameWrapper = gameWrappers.get(id);
                if(cheatRequest.equals("1_CHEESE")){
                    gameWrapper.setNumOfCheese(1);
                    return;
                }
                else if (cheatRequest.equals("SHOW_ALL")){
                    gameWrapper.setAllVisible();
                    return;
                }
                else{
                    throw new IOException();
                }
            }
        }
        throw new IllegalArgumentException();
    }

    //Exception Handlers
    @ResponseStatus(value= HttpStatus.BAD_REQUEST, reason = "Bad Move")
    @ExceptionHandler(IOException.class)
    public void badRequestExceptionHandler(){
        // Nothing to do
    }

    @ResponseStatus(value= HttpStatus.NOT_FOUND, reason = "request ID not found")
    @ExceptionHandler(IllegalArgumentException.class)
    public void badIdExceptionHandler(){
        // Nothing to do
    }

}
