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
        Kunde kunde =  em.find(Kunde.class, id);

        if (kunde == null) {
            return Response.status(Response.Status.NOT_FOUND).build(); // return code is 404
        }

        return Response.ok(kunde).build(); // return code is 200
    }

    @POST
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response postKunde(@NotNull Kunde kunde, @Context UriInfo uriInfo) {
        int maxKundenID;
        //neue KundenID heruasfinden
      /*  try {
            Query q = em.createQuery("Select max(k.kundenID) From Kunde k");
            Object maxKundenIDObject = q.getSingleResult();
            maxKundenID = maxKundenIDObject == null ? 0 : (int) maxKundenIDObject;
        } catch (Exception e) {
           maxKundenID = 0;
        }
        boolean validId = kunde.kundenID > 0 && maxKundenID == 0;
        if (!validId) {
            kunde.kundenID = maxKundenID+1;
        }*/
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

        Kunde findKunde =  em.find(Kunde.class, id);   //q.getSingleResult();

        boolean exists = findKunde != null;
        kunde.kundenID = id;
        if (!exists) {
            return postKunde(kunde, uriInfo);
        } else {
            //Ausgewaelten Kunden loeschen und mit neuen Werten einfügen
       /*     em.getTransaction().begin();
            Query qD = em.createQuery("delete from Kunde k where k.kundenID = :sqlWhere");
            qD.setParameter("sqlWhere", id);
            qD.executeUpdate();
         //   em.remove(id);

            em.persist(kunde);
            em.getTransaction().commit();*/
            em.getTransaction().begin();
            em.merge(kunde);
            em.getTransaction().commit();
            em.close();
            return Response.ok(kunde).build(); // return code is 200
        }
    }

    @PATCH
    @Path("{id}")
    public Response patchKunde(@PathParam("id") int id, @NotNull Kunde patchedKunde) {

        Kunde kunde =  em.find(Kunde.class, id);

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
            if (patchedKunde.anschrift.plz != null) {
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

            em.getTransaction().begin();
            em.persist(kunde);
            em.getTransaction().commit();
            em.close();


            return Response.ok(kunde).build(); // return code is 200
        }

    }

    @DELETE
    public Response deleteKunden() {

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

        em.getTransaction().begin();
        em.remove(em.find(Kunde.class, id));
        em.getTransaction().commit();
        em.close();

        return Response.noContent().build(); // return code is 204
    }
}
