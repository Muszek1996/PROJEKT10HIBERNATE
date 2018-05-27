package restcrudhibernate.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;


public class Player implements Serializable {
    @NotNull(message="Steamid cannot be null")
    private long steamid;
    @NotNull(message="Name cannot be null")
    @Size(min=6, max=30)
    private String nickname;
    private boolean vacBanned;
    private double accountValue;

    private Set<Game> playerGames = new HashSet<>(0);


    public Player() {
    }

    public Player(long steamid, String nickname, boolean vacBanned, double accountValue, Set<Game> playerGames) {
        this.steamid = steamid;
        this.nickname = nickname;
        this.vacBanned = vacBanned;
        this.accountValue = accountValue;
        this.playerGames = playerGames;
    }

    public Player(long steamid, String nickname, boolean vacBanned, double accountValue) {

        this.steamid = steamid;
        this.nickname = nickname;
        this.vacBanned = vacBanned;
        this.accountValue = accountValue;
    }


    public long getSteamid() {
        return steamid;
    }

    public void setSteamid(long steamid) {
        this.steamid = steamid;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public boolean isVacBanned() {
        return vacBanned;
    }

    public void setVacBanned(boolean vacBanned) {
        this.vacBanned = vacBanned;
    }

    public double getAccountValue() {
        return accountValue;
    }

    public void setAccountValue(double accountValue) {
        this.accountValue = accountValue;
    }


    public Set<Game> getPlayerGames() {
        if(playerGames!=null){
            playerGames.forEach(p->p.setPlayersWhoOwnsThisGame(null));
        }
        return playerGames;
    }

    public void setPlayerGames(Set<Game> playerGames) {
        this.playerGames = playerGames;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Player player = (Player) o;
        return steamid == player.steamid &&
                vacBanned == player.vacBanned &&
                Double.compare(player.accountValue, accountValue) == 0 &&
                Objects.equals(nickname, player.nickname) ;
    }

    @Override
    public int hashCode() {

        return Objects.hash(steamid, nickname, vacBanned, accountValue);
    }
}
