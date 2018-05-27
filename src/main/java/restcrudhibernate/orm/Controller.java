package restcrudhibernate.orm;


import java.util.*;

import javassist.NotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import restcrudhibernate.entities.Game;
import restcrudhibernate.entities.Player;

import javax.persistence.EntityExistsException;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;


@RestController
public class Controller {
    private static PlayerManipulator playerManipulator;
    private static GameManipulator gameManipulator;

    @Autowired
    Controller(PlayerManipulator playerManipulator, GameManipulator gameManipulator) {
        this.playerManipulator = playerManipulator;
        this.gameManipulator = gameManipulator;
    }


    @RequestMapping(value = "/player/add", method = RequestMethod.POST)
    public ResponseEntity<Object> addPlayer(@RequestParam(value = "steamid") long name,
                                            @RequestParam(value = "nickname") String nickname,
                                            @RequestParam(value = "vacban") boolean vacBanned,
                                            @RequestParam(value = "accountvalue") double accountValue) {

        try {
            playerManipulator.addPlayer(name, nickname, vacBanned, accountValue);
        } catch (EntityExistsException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
        } catch (ConstraintViolationException e) {
            Set<ConstraintViolation<?>> set = e.getConstraintViolations();
            return new ResponseEntity<>(set.iterator().next().getMessage(), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>("Player was added successfully.", HttpStatus.OK);
    }

    @RequestMapping(value = "/players/get", method = RequestMethod.GET)
    public List<Player> getPlayers() {
        List<Player> players = playerManipulator.listPlayers();
        return players;
    }

    @RequestMapping(value = "/player/get", method = RequestMethod.GET)
    public List<Player> getPlayer(@RequestParam(value = "steamid") long steamId) {
        List<Player> players = playerManipulator.listPlayers(steamId);
        return players;
    }

    @RequestMapping(value = "/player/update", method = RequestMethod.PUT)
    public ResponseEntity<Object> updatePlayer(@RequestParam(value = "steamid") long steamid,
                                               @RequestParam(value = "nickname") String nickname,
                                               @RequestParam(value = "vacbanned") boolean vacBanned,
                                               @RequestParam(value = "accountvalue") double accountValue) {
        try {
            playerManipulator.updatePlayer(steamid, nickname, vacBanned, accountValue);
        } catch (NotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (ConstraintViolationException e) {
            Set<ConstraintViolation<?>> set = e.getConstraintViolations();
            return new ResponseEntity<>(set.iterator().next().getMessage(), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>("Player was updated successfully.", HttpStatus.OK);
    }

    @RequestMapping(value = "/player/delete", method = RequestMethod.DELETE)
    public String deletePlayer(@RequestParam(value = "steamid") Long steamId) {
        try {
            playerManipulator.deletePlayer(steamId);
        } catch (NotFoundException e) {
            return e.getMessage();
        }
        return "Player:" + steamId + " deleted";
    }


    @RequestMapping(value = "/game/add", method = RequestMethod.POST)
    public ResponseEntity<Object> addGame(@RequestParam(value = "appid") long appid,
                                          @RequestParam(value = "name") String name,
                                          @RequestParam(value = "gamepadsupport") boolean gamepadSupported,
                                          @RequestParam(value = "price") double price) {

        try {
            gameManipulator.addGame(appid, name, gamepadSupported, price);
        } catch (EntityExistsException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
        } catch (ConstraintViolationException e) {
            Set<ConstraintViolation<?>> set = e.getConstraintViolations();
            return new ResponseEntity<>(set.iterator().next().getMessage(), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>("Game added successfully", HttpStatus.OK);
    }

    @RequestMapping(value = "/games/get", method = RequestMethod.GET)
    public List<Game> getGames() {
        List<Game> Games = gameManipulator.listGames();
        return Games;
    }

    @RequestMapping(value = "/game/get", method = RequestMethod.GET)
    public List<Game> getGame(@RequestParam(value = "appid") long appid) {
        List<Game> Games = gameManipulator.listGames();
        return Games;
    }

    @RequestMapping(value = "/game/update", method = RequestMethod.PUT)
    public ResponseEntity<Object> updateGame(@RequestParam(value = "appid") long appid,
                                             @RequestParam(value = "name") String name,
                                             @RequestParam(value = "gamepadsupport") boolean gamepadSupported,
                                             @RequestParam(value = "price") double price) {
        try {
            gameManipulator.updateGame(appid, name, gamepadSupported, price);
        } catch (NotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
        } catch (ConstraintViolationException e) {
            Set<ConstraintViolation<?>> set = e.getConstraintViolations();
            return new ResponseEntity<>(set.iterator().next().getMessage(), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>("Game updated successfully", HttpStatus.OK);
    }

    @RequestMapping(value = "/game/delete", method = RequestMethod.DELETE)
    public String deleteGame(@RequestParam(value = "appid") Long appid) {
        try {
            gameManipulator.deleteGame(appid);
        } catch (NotFoundException e) {
            return e.getMessage();
        }
        return "Game:" + appid + " deleted";
    }

    @RequestMapping(value = "/buy/{steamid}/{appid}", method = RequestMethod.POST)
    public ResponseEntity<Object> buyGame(@PathVariable("appid") Long appid, @PathVariable("steamid") Long steamid) {
        try {
            playerManipulator.buyGame(steamid, appid);
        } catch (NotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_ACCEPTABLE);
        }
        return new ResponseEntity<>("Game bought succesfully", HttpStatus.OK);
    }

    @RequestMapping(value = "/player/find/{nickname}", method = RequestMethod.GET)
    public ResponseEntity<Object> findByPartialSurname(@PathVariable("nickname") String nickname) {
        List<Player> players = new LinkedList<>();
        try {
            players = playerManipulator.findByNickname(nickname, false, false);
        } catch (NotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(players, HttpStatus.OK);
    }

    @RequestMapping(value = "/player/sort/{direction}", method = RequestMethod.GET)
    public ResponseEntity<Object> findBySurname(@PathVariable("direction") boolean direction) {
        List<Player> players = new LinkedList<>();
        try {
            players = playerManipulator.sortByAccountValues(direction);
        } catch (NotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(players, HttpStatus.OK);
    }

    @RequestMapping(value = "/player/value/{val1}/{val2}", method = RequestMethod.GET)
    public ResponseEntity<Object> findByValueBetween(@PathVariable("val1") double val1, @PathVariable("val2") double val2) {
        List<Player> players = new LinkedList<>();
        try {
            SortedSet<Double> numbers = new TreeSet<>();
            numbers.add(val1);
            numbers.add(val2);
            players = playerManipulator.getPlayersWhereValueBeetween(numbers.first(), numbers.last());
        } catch (NotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(players, HttpStatus.OK);
    }

}



