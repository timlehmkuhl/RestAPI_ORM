import com.sun.net.httpserver.HttpServer;
import org.glassfish.jersey.jdkhttp.JdkHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.net.URI;

public class WebServiceStarter {

    public static void main(String[] args) throws Exception {

     //   EntityManagerFactory emf = Persistence.createEntityManagerFactory("mariadb-localhost");
   //     EntityManager   em = emf.createEntityManager();

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
