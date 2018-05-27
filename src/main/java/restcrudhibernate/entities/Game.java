package restcrudhibernate.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;


import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;


public class Game implements Serializable {
    @NotNull(message="Appid cannot be null")
    private long appid;
    @NotNull(message="Game title cannot be null")
    @Size(min=1, max=64)
    private String name;
    private double price;
    private boolean gamepadSupport;


     private Set<Player> playersWhoOwnsThisGame;



    public Game(){
    }

    public Game(long appid, String name, double price, boolean gamepadSupport, Set<Player> playersWhoOwnsThisGame) {
        this.appid = appid;
        this.name = name;
        this.price = price;
        this.gamepadSupport = gamepadSupport;
        this.playersWhoOwnsThisGame = playersWhoOwnsThisGame;
    }

    public Game(long appid, String name, double price, boolean gamepadSupport) {
        this.appid = appid;
        this.name = name;
        this.price = price;
        this.gamepadSupport = gamepadSupport;
    }

    public long getAppid() {
        return appid;
    }

    public void setAppid(long appid) {
        this.appid = appid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public boolean isGamepadSupport() {
        return gamepadSupport;
    }

    public void setGamepadSupport(boolean gamepadSupport) {
        this.gamepadSupport = gamepadSupport;
    }

    public Set<Player> getPlayersWhoOwnsThisGame() {
        if(playersWhoOwnsThisGame!=null){
            playersWhoOwnsThisGame.forEach(p->p.setPlayerGames(null));
        }
        return playersWhoOwnsThisGame;
    }

    public void setPlayersWhoOwnsThisGame(Set<Player> playersWhoOwnsThisGame) {
        this.playersWhoOwnsThisGame = playersWhoOwnsThisGame;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Game game = (Game) o;
        return appid == game.appid &&
                Double.compare(game.price, price) == 0 &&
                gamepadSupport == game.gamepadSupport &&
                Objects.equals(name, game.name);
    }

    @Override
    public int hashCode() {

        return Objects.hash(appid, name, price, gamepadSupport);
    }

}
