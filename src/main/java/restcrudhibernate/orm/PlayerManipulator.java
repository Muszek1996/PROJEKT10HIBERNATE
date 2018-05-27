package restcrudhibernate.orm;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import javassist.NotFoundException;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;

import org.hibernate.query.Query;
import org.springframework.stereotype.Service;
import restcrudhibernate.entities.Game;
import restcrudhibernate.entities.Player;

import javax.persistence.EntityExistsException;

@Service
public class PlayerManipulator {


    public Long addPlayer(Long steamid, String nickname, boolean vacBanned, Double accountValue ) throws EntityExistsException{
        Session session = SessionService.getSession();
        Transaction tx = null;

        try {
            if(session.get(Player.class,steamid)!=null)
                throw new EntityExistsException("Player with steamId:"+steamid+" already exist in database");
            tx = session.beginTransaction();
            Player player = new Player(steamid,nickname,vacBanned, accountValue);
            session.save(player);
            tx.commit();
        } catch (HibernateException e) {
            if (tx!=null) tx.rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }
        return steamid;
    }

    public List<Player> listPlayers(){
        Session session = SessionService.getSession();
        Transaction tx = null;
        List players = new ArrayList();
        try {
            tx = session.beginTransaction();
            players = session.createQuery("FROM Player").getResultList();
            for (Iterator iterator = players.iterator(); iterator.hasNext();){
                Player player = (Player) iterator.next();
            }
            tx.commit();
        } catch (HibernateException e) {
            if (tx!=null) tx.rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }
        return players;
    }

    public List<Player> listPlayers(Long steamid){
        Session session = SessionService.getSession();
        Transaction tx = null;
        List players = new ArrayList();
        try {
            tx = session.beginTransaction();
            Query query = session.createQuery("FROM Player WHERE steamid = ?1");
            query.setParameter(1,steamid);
            players = query.getResultList();
            for (Iterator iterator = players.iterator(); iterator.hasNext();){
                Player player = (Player) iterator.next();
            }
            tx.commit();
        } catch (HibernateException e) {
            if (tx!=null) tx.rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }
        return players;
    }




    public Long updatePlayer(Long steamId, String nickname, boolean vacBanned, Double accountValue ) throws NotFoundException {
        Session session = SessionService.getSession();
        Transaction tx = null;

        try {
            tx = session.beginTransaction();
            Player player = session.get(Player.class, steamId);
            if(player == null){
                throw new NotFoundException("Player with id: " + steamId + " not found. Can't update");
            }
            player.setNickname(nickname);
            player.setVacBanned(vacBanned);
            player.setAccountValue(accountValue);
            session.update(player);
            tx.commit();
        } catch (HibernateException e) {
            if (tx!=null) tx.rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }
        return steamId;
    }

    public Long deletePlayer(Long steamId) throws  NotFoundException{
        Session session = SessionService.getSession();
        Transaction tx = null;

        try {
            tx = session.beginTransaction();
            Player player = session.get(Player.class, steamId);
            if(player == null){
                throw new NotFoundException("Player with id: " + steamId + " not found. Can't delete");
            }
            session.delete(player);
            tx.commit();
        } catch (HibernateException e) {
            if (tx!=null) tx.rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }
        return steamId;
    }

    public Long buyGame(Long steamid,Long appid) throws  NotFoundException{
        Session session = SessionService.getSession();
        Transaction tx = null;

        try {
            Player player = session.get(Player.class, steamid);
            Game game = session.get(Game.class,appid);
            if(player == null){
                throw new NotFoundException("Player with id: " + steamid + " not found.");
            }else if(game == null){
                throw new NotFoundException("Game with id: " + appid + " not found.");
            }else if(game.getPlayersWhoOwnsThisGame().contains(player)){
                throw new NotFoundException("Already have this game");
            }
            tx = session.beginTransaction();
            player.setAccountValue(player.getAccountValue()+game.getPrice());

            player.getPlayerGames().add(game);
            session.update(player);
            tx.commit();
        } catch (HibernateException e) {
            if (tx!=null) tx.rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }
        return appid;
    }

    public List<Player> findByNickname(String nickname,boolean caseSensitive,boolean exact) throws NotFoundException{
        Session session = SessionService.getSession();
        Transaction tx = null;
        List<Player> players = new LinkedList<>();
        try {
            Query query = session.createQuery("FROM Player WHERE nickname LIKE ?1");
            if(exact){
                query.setParameter(1, nickname);
            }else{
                query.setParameter(1, "%"+nickname+"%");
            }

            for(final Object o:query.getResultList()){
                players.add((Player)o);
                ((Player)o).getPlayerGames();
            }
            if(caseSensitive)
                players.forEach(p->{
                    if(!p.getNickname().equals(nickname))
                        players.remove(p);
                });
            if(players.size()<=0){
                throw new NotFoundException("Player with nickname: " + nickname + " not found.");
            }


        } catch (HibernateException e) {
            if (tx!=null) tx.rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }


        return players;
    }

    public List<Player> sortByAccountValues(boolean direction) throws NotFoundException{
        Session session = SessionService.getSession();
        Transaction tx = null;
        List<Player> players = new LinkedList<>();
        try {
            Query query;
            if(direction){
                query = session.createQuery("FROM Player ORDER BY accountValue ASC");
            }else{
                query = session.createQuery("FROM Player ORDER BY accountValue DESC");
            }

            for(final Object o:query.getResultList()){
                players.add((Player)o);
                ((Player)o).getPlayerGames();
            }
            if(players.size()<=0){
                throw new NotFoundException("Table empty");
            }
        } catch (HibernateException e) {
            if (tx!=null) tx.rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }


        return players;
    }


    public List<Player> getPlayersWhereValueBeetween(double smallerNumber,double largerNumber) throws NotFoundException{
        Session session = SessionService.getSession();
        Transaction tx = null;
        List<Player> players = new LinkedList<>();
        try {
            Query query;
                query = session.createQuery("FROM Player WHERE accountValue BETWEEN ?1 AND ?2");
                query.setParameter(1,smallerNumber);
                query.setParameter(2,largerNumber);
            for(final Object o:query.getResultList()){
                players.add((Player)o);
                ((Player)o).getPlayerGames();
            }
            if(players.size()<=0){
                throw new NotFoundException("Table empty");
            }
        } catch (HibernateException e) {
            if (tx!=null) tx.rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }


        return players;
    }

}









