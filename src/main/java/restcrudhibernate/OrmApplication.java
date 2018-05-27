package restcrudhibernate;

import org.hibernate.Session;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import restcrudhibernate.entities.Game;
import restcrudhibernate.entities.Player;
import restcrudhibernate.orm.PlayerManipulator;
import restcrudhibernate.orm.SessionService;

import java.util.HashSet;
import java.util.Set;

@SpringBootApplication
public class OrmApplication {

    public static void main(String[] args) {
        SpringApplication.run(OrmApplication.class, args);


}
}
