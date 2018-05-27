package restcrudhibernate.orm;


import java.util.List;

import javassist.NotFoundException;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.stereotype.Service;
import restcrudhibernate.entities.Game;

import javax.persistence.EntityExistsException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

@Service
public class GameManipulator {

    public Long addGame(Long appid, String name, boolean gamepadSupported, Double price ) throws EntityExistsException {
        Session session = SessionService.getSession();
        Transaction tx = null;

        try {
            if(session.get(Game.class,appid)!=null)
                throw new EntityExistsException("Game with appid:"+appid+" already exist in database");
            tx = session.beginTransaction();
            Game Game = new Game(appid,name, price,gamepadSupported);
            session.save(Game);
            tx.commit();
        } catch (HibernateException e) {
            if (tx!=null) tx.rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }
        return appid;
    }

    public List<Game> listGames(){
        Session session = SessionService.getSession();
        Transaction tx = null;
        List Games = new ArrayList();
        try {
            tx = session.beginTransaction();
            Games = session.createQuery("FROM Game").getResultList();
            for (Iterator iterator = Games.iterator(); iterator.hasNext();){
                Game Game = (Game) iterator.next();
            }
            tx.commit();
        } catch (HibernateException e) {
            if (tx!=null) tx.rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }
        return Games;
    }




    public Long updateGame(Long appid, String nick, boolean gamepadSupport, Double price ) throws NotFoundException {
        Session session = SessionService.getSession();
        Transaction tx = null;

        try {
            tx = session.beginTransaction();
            Game Game = session.get(Game.class, appid);
            if(Game == null){
                throw new NotFoundException("Game with id: " + appid + " not found. Can't update");
            }
            Game.setName(nick);
            Game.setGamepadSupport(gamepadSupport);
            Game.setPrice(price);
            session.update(Game);
            tx.commit();
        } catch (HibernateException e) {
            if (tx!=null) tx.rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }
        return appid;
    }

    public Long deleteGame(Long appid) throws  NotFoundException{
        Session session = SessionService.getSession();
        Transaction tx = null;

        try {
            tx = session.beginTransaction();
            Game Game = session.get(Game.class, appid);
            if(Game == null){
                throw new NotFoundException("Game with id: " + appid + " not found. Can't delete");
            }
            session.delete(Game);
            tx.commit();
        } catch (HibernateException e) {
            if (tx!=null) tx.rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }
        return appid;
    }

}
