package resources;

import model.Kauf;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.net.URI;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Path("/kaeufe")
public class KaufResource {
    EntityManagerFactory emf = Persistence.createEntityManagerFactory("mariadb-localhost");
    EntityManager em = emf.createEntityManager();
   // final static Map<Integer, Kunde> kundenMap = new ConcurrentHashMap<>();

    @GET
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Collection<Kauf> getKaeufe() {
    Query q = em.createQuery("select k from Kauf k");
            List<Kauf> list = q.getResultList();
            em.close();

     //   list.stream().forEach(x -> x.getPositions().forEach( c -> c.setKauf(null)));
        return list;  // return code is 200
    }

    @GET
    @Path("{id}")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response getKauf(@PathParam("id") int id) {

        //Gesuchten Kunden aus DB holen
        Kauf kauf =  em.find(Kauf.class, id);

        if (kauf == null) {
            return Response.status(Response.Status.NOT_FOUND).build(); // return code is 404
        }

        return Response.ok(kauf).build(); // return code is 200
    }

   @POST
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response postKauf(@NotNull Kauf kauf, @Context UriInfo uriInfo) {
        int maxKundenID;
       int maxPositionID;
        //neue KundenID heruasfinden
        try {
            Query q = em.createQuery("Select max(k.kaufID) From Kauf k");
            Object maxKundenIDObject = q.getSingleResult();
            maxKundenID = maxKundenIDObject == null ? 0 : (int) maxKundenIDObject;
        } catch (Exception e) {
           maxKundenID = 0;
        }

       boolean validId = kauf.kaufID > 0 && maxKundenID == 0;
       if (!validId) {
           kauf.kaufID = maxKundenID+1;
       }

        //Positions ID setzen
       try {
           Query q = em.createQuery("Select max(p.id) From Position p");
           Object maxPositionIDObject = q.getSingleResult();
           maxPositionID = maxPositionIDObject == null ? 0 : (int) maxPositionIDObject;
       } catch (Exception e) {
           maxPositionID = 0;
       }

       for (int i = 0; i < kauf.getPositions().size(); i++){
           kauf.getPositions().get(i).setId(maxPositionID + i + 1);
       }


        kauf.getPositions().forEach(x -> x.setKauf(kauf));

        em.getTransaction().begin();
        em.persist(kauf);
        em.getTransaction().commit();
        em.close();

        URI uri = uriInfo.getAbsolutePathBuilder().path(Integer.toString(kauf.kaufID)).build(); // append new id to URI

        return Response.created(uri).entity(kauf).build(); // return code is 201
    }

    @PUT
    @Path("{id}")
    public Response putKunde(@PathParam("id") int id, @NotNull Kauf kauf, @Context UriInfo uriInfo) {

        Kauf findKauf =  em.find(Kauf.class, id);   //q.getSingleResult();

        boolean exists = findKauf != null;
        kauf.kaufID = id;
        if (!exists) {
            return postKauf(kauf, uriInfo);
        } else {
            //Ausgewaelten Kunden loeschen und mit neuen Werten einfügen
            em.getTransaction().begin();
            //Query qD = em.createQuery("delete from Kauf k where k.kaufID = :sqlWhere");
         //   qD.setParameter("sqlWhere", id);
          //  qD.executeUpdate();
         //   em.remove(id);
            em.remove(findKauf);
            em.getTransaction().commit();

            kauf.getPositions().forEach(x -> x.setKauf(kauf));

            em.getTransaction().begin();
            em.persist(kauf);
            em.getTransaction().commit();
            em.close();
            return Response.ok(kauf).build(); // return code is 200
        }
    }
/*
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

        */
}
