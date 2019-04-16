import com.sun.net.httpserver.HttpServer;
import model.Kauf;
import model.Kunde;
import model.Position;
import org.glassfish.jersey.jdkhttp.JdkHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.net.URI;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

public class WebServiceStarter {

    public static void main(String[] args) throws Exception {

     EntityManagerFactory emf = Persistence.createEntityManagerFactory("mariadb-localhost");
        EntityManager   em2 = emf.createEntityManager();
        /*   Kauf kauf;*/
      Kauf kauf = new Kauf();
        kauf.setKaufID(1);
        kauf.setBaumarktID(1);
        kauf.setZeit("ggg");


        Position position = new Position();
        position.setId(2);
        position.setArtikelID(1);
        position.setAnzahl(3);
        position.setPreis(20);
        position.setKauf(kauf);

       // Set<Position> set = Set.of(position);
     //   set.add(position);

        kauf.getPositions().add(position);
        em2.getTransaction().begin();
        em2.persist(kauf);
        em2.getTransaction().commit();

        ResourceConfig rc = new ResourceConfig().packages("resources", "filter");
        HttpServer server = JdkHttpServerFactory.createHttpServer(URI.create("http://localhost:8080/"), rc);
        System.out.println("Hit enter to stop HTTP server.");
        System.in.read();
        server.stop(0);
        //pra
        //test
    }

  /*  public static EntityManager getEntityManager() {
        return emf.createEntityManager();
    }*/
}
