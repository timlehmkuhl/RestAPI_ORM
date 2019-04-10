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
    EntityManagerFactory emf = Persistence.createEntityManagerFactory("mariadb-localhost");
    EntityManager em = emf.createEntityManager();
    final static Map<Integer, Kunde> kundenMap = new ConcurrentHashMap<>();

    @GET
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Collection<Kunde> getKunden() {
    Query q = em.createQuery("select k from Kunde k");
            List<Kunde> list = q.getResultList();
            em.close();
        return list;  // return code is 200
    }

    @GET
    @Path("{id}")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response getKunde(@PathParam("id") int id) {

        //Gesuchten Kunden aus DB holen
        Query q = em.createQuery("select k from Kunde k where k.kundenID = :sqlWhere");
        q.setParameter("sqlWhere", id);
        Kunde kunde = (Kunde) q.getSingleResult();

        if (kunde == null) {
            return Response.status(Response.Status.NOT_FOUND).build(); // return code is 404
        }

        return Response.ok(kunde).build(); // return code is 200
    }

    @POST
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response postKunde(@NotNull Kunde kunde, @Context UriInfo uriInfo) {

        //neue KundenID heruasfinden
       Query q = em.createQuery("Select max(k.kundenID) From Kunde k");
       Object maxKundenIDObject = q.getSingleResult();
       int maxKundenID = maxKundenIDObject == null ? 0: (int) maxKundenIDObject;

        boolean validId = kunde.kundenID > 0 && maxKundenID == 0;
        if (!validId) {
            kunde.kundenID = maxKundenID+1;
        }
        //Kunde in Datenbank
        em.getTransaction().begin();
        em.persist(kunde);
        em.getTransaction().commit();
        em.close();

        URI uri = uriInfo.getAbsolutePathBuilder().path(Integer.toString(kunde.kundenID)).build(); // append new id to URI

        return Response.created(uri).entity(kunde).build(); // return code is 201
    }

    @PUT
    @Path("{id}")
    public Response putKunde(@PathParam("id") int id, @NotNull Kunde kunde, @Context UriInfo uriInfo) {

        Query q = em.createQuery("select k from Kunde k where k.kundenID = :sqlWhere");
        q.setParameter("sqlWhere", id);
        Object kundenIDObject = q.getSingleResult();
       // em.close();
        boolean exists = kundenIDObject != null;
        kunde.kundenID = id;
        if (!exists) {
            return postKunde(kunde, uriInfo);
        } else {
            //Ausgewaelten Kunden loeschen und mit neuen Werten einfügen
            em.getTransaction().begin();
            Query qD = em.createQuery("delete from Kunde k where k.kundenID = :sqlWhere");
            qD.setParameter("sqlWhere", id);
            qD.executeUpdate();
            em.persist(kunde);
            em.getTransaction().commit();
            em.close();
            return Response.ok(kunde).build(); // return code is 200
        }
    }

    @PATCH
    @Path("{id}")
    public Response patchKunde(@PathParam("id") int id, @NotNull Kunde patchedKunde) {
       // Kunde kunde = kundenMap.get(id);

        Query q = em.createQuery("select k from Kunde k where k.kundenID = :sqlWhere");
        q.setParameter("sqlWhere", id);
        Kunde kunde = (Kunde) q.getSingleResult();

        boolean exists = kunde != null;
        if (!exists) {
            return Response.status(404).build(); // return code is 404
        } else {
            if (patchedKunde.vorname != null) {
                kunde.vorname = patchedKunde.vorname;
            }
            if (patchedKunde.nachname != null) {

                kunde.nachname = patchedKunde.nachname;
            }
            if (patchedKunde.anschrift.strasse != null) {
                System.out.println("TEST");
                kunde.anschrift.strasse = patchedKunde.anschrift.strasse;
            }
            if (patchedKunde.anschrift.plz != 0) {
                kunde.anschrift.plz = patchedKunde.anschrift.plz;
            }
            if (patchedKunde.anschrift.ort != null) {
                kunde.anschrift.ort = patchedKunde.anschrift.ort;
            }
            if (patchedKunde.geschaeftskunde != null) {
                kunde.geschaeftskunde = patchedKunde.geschaeftskunde;
            }
            if (patchedKunde.kundenkarte != null) {
                kunde.kundenkarte = patchedKunde.kundenkarte;
            }


         /*   em.getTransaction().begin();
            Query qD = em.createQuery("delete from Kunde k where k.kundenID = :sqlWhere2");
            qD.setParameter("sqlWhere2", id);
            qD.executeUpdate();
            em.getTransaction().commit();*/


            em.getTransaction().begin();
            em.persist(kunde);
            em.getTransaction().commit();
            em.close();


            return Response.ok(kunde).build(); // return code is 200
        }

    }

    @DELETE
    public Response deleteKunden() {

   //     kundenMap.clear();

        em.getTransaction().begin();
        Query q =  em.createQuery("Delete FROM Kunde");
        q.executeUpdate();
        em.getTransaction().commit();
        em.close();
        System.out.println("DELETED ALL");
        return Response.noContent().build(); // return code is 204
    }

    @DELETE
    @Path("{id}")
    public Response deleteKunde(@PathParam("id") int id) {
     //   kundenMap.remove(id);

        em.getTransaction().begin();
        Query q = em.createQuery("delete from Kunde k where k.kundenID = :sqlWhere");
        q.setParameter("sqlWhere", id);
        q.executeUpdate();
        em.getTransaction().commit();
        em.close();

        return Response.noContent().build(); // return code is 204
    }
}
