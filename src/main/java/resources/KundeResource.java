package resources;

import model.Kunde;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import javax.validation.constraints.NotNull;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.PATCH;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.net.URI;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Path("/kunden")
public class KundeResource {

    final static Map<Integer, Kunde> kundenMap = new ConcurrentHashMap<>();

    @GET
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Collection<Kunde> getAccounts() {
//        for (int i = 1; i <= kundenMap.size(); i++){
//            System.out.println(kundenMap.get(i).nachname + " " + kundenMap.get(i).kundenID);
  //      }
      //  System.out.println(kundenMap.get(1).nachname);

        return kundenMap.values();  // return code is 200
    }

    @GET
    @Path("{id}")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response getKunde(@PathParam("id") int id) {

        Kunde kunde = kundenMap.get(id);
        if (kunde == null) {
            return Response.status(Response.Status.NOT_FOUND).build(); // return code is 404
        }

        return Response.ok(kunde).build(); // return code is 200
    }

    @POST
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response postKunde(@NotNull Kunde kunde, @Context UriInfo uriInfo) {

        EntityManagerFactory emf = Persistence.createEntityManagerFactory("mariadb-localhost");
        EntityManager em = emf.createEntityManager();

        //Gibt es Datensätze in der Datenbank?
        String sql = "SELECT k.kundenID FROM Kunde k";
        Query q = em.createQuery(sql);
        List<Object[]> res = q.getResultList();
        System.out.println(res.isEmpty());
        //res.forEach(record -> System.out.println(Arrays.toString(record)));

      //  boolean validId = kunde.kundenID > 0 && kundenMap.get(kunde.kundenID) == null;
        boolean validId = kunde.kundenID > 0 && res.isEmpty();
        if (!validId) {
           // kunde.kundenID = Kunde.nextId.getAndIncrement();
            kunde.kundenID = res.size()+1;
        }
        //Kunde in Datenbank
        em.getTransaction().begin();
        em.persist(kunde);
        em.getTransaction().commit();

       // kundenMap.put(kunde.kundenID, kunde);
        URI uri = uriInfo.getAbsolutePathBuilder().path(Integer.toString(kunde.kundenID)).build(); // append new id to URI

       /* System.out.println("POSTET: " +  kunde.kundenId + "" );
        System.out.println(kunde.kundenId);
        kunde.getEntries().forEach(x -> System.out.println("value: " + x.value + ", date: " + x.date));*/




        return Response.created(uri).entity(kunde).build(); // return code is 201
    }

    @PUT
    @Path("{id}")
    public Response putKunde(@PathParam("id") int id, @NotNull Kunde kunde, @Context UriInfo uriInfo) {
        boolean exists = kundenMap.get(id) != null;
        kunde.kundenID = id;
        if (!exists) {
            return postKunde(kunde, uriInfo);
        } else {
            kundenMap.put(id, kunde);
            return Response.ok(kunde).build(); // return code is 200
        }
    }

    @PATCH
    @Path("{id}")
    public Response patchKunde(@PathParam("id") int id, @NotNull Kunde patchedKunde) {
        Kunde kunde = kundenMap.get(id);
        boolean exists = kunde != null;
        if (!exists) {
            return Response.status(404).build(); // return code is 404
        } else {
            if (patchedKunde.vorname != null) {
                kunde.vorname = patchedKunde.vorname;
            } else
            if (patchedKunde.nachname != null) {
                System.out.println("TEST");
                kunde.nachname = patchedKunde.nachname;
            } else
            if (patchedKunde.anschrift.strasse != null) {
                kunde.anschrift.strasse = patchedKunde.anschrift.strasse;
            } else
            if (patchedKunde.anschrift.plz != 0) {
                kunde.anschrift.plz = patchedKunde.anschrift.plz;
            } else
            if (patchedKunde.anschrift.ort != null) {
                kunde.anschrift.ort = patchedKunde.anschrift.ort;
            } else
            if (patchedKunde.geschaeftskunde != null) {
                kunde.geschaeftskunde = patchedKunde.geschaeftskunde;
            }
            if (patchedKunde.kundenkarte != null) {
                kunde.kundenkarte = patchedKunde.kundenkarte;
            }

            return Response.ok(kunde).build(); // return code is 200
        }
    }

    @DELETE
    public Response deleteKunden() {

        kundenMap.clear();
        System.out.println("DELETED ALL");
        return Response.noContent().build(); // return code is 204
    }

    @DELETE
    @Path("{id}")
    public Response deleteKunde(@PathParam("id") int id) {
        kundenMap.remove(id);
        return Response.noContent().build(); // return code is 204
    }
}
