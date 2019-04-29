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


@Path("/kaeufe")
public class KaufResource {
    EntityManagerFactory emf = Persistence.createEntityManagerFactory("mariadb-localhost");
    EntityManager em = emf.createEntityManager();

    @GET
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Collection<Kauf> getKaeufe() {
        Query q = em.createQuery("select k from Kauf k");
        List<Kauf> list = q.getResultList();
        em.close();

        return list;  // return code is 200
    }

    @GET
    @Path("{id}")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response getKauf(@PathParam("id") int id) {

        //Gesuchten Kunden aus DB holen
        Kauf kauf = em.find(Kauf.class, id);
        em.close();
        if (kauf == null) {
            return Response.status(Response.Status.NOT_FOUND).build(); // return code is 404
        }

        return Response.ok(kauf).build(); // return code is 200
    }

    @POST
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response postKauf(@NotNull Kauf kauf, @Context UriInfo uriInfo) {

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
    public Response putKauf(@PathParam("id") int id, @NotNull Kauf kauf, @Context UriInfo uriInfo) {

        Kauf findKauf = em.find(Kauf.class, id);

        boolean exists = findKauf != null;
        kauf.kaufID = id;
        if (!exists) {
            return postKauf(kauf, uriInfo);
        } else {
            //Ausgewaelten Kunden loeschen und mit neuen Werten einfügen

            kauf.getPositions().forEach(x -> x.setKauf(kauf));
            em.getTransaction().begin();
            em.merge(kauf);
            em.getTransaction().commit();


            em.close();
            return Response.ok(kauf).build(); // return code is 200
        }
    }



    @DELETE
    public Response deleteKaeufe() {

        em.getTransaction().begin();
        Query q = em.createQuery("Delete FROM Position ");
        Query q2 = em.createQuery("Delete FROM Kauf");
        q.executeUpdate();
        q2.executeUpdate();
        em.getTransaction().commit();
        em.close();
        System.out.println("DELETED ALL");
        return Response.noContent().build(); // return code is 204
    }

    @DELETE
    @Path("{id}")
    public Response deleteKunde(@PathParam("id") int id) {

        em.getTransaction().begin();
       // em.remove(em.find(Kauf.class, id));
        Query qD =  em.createQuery("delete from Position p where p.kauf.kaufID = :sqlWhere");
        qD.setParameter("sqlWhere", id);
        qD.executeUpdate();
        em.remove(em.find(Kauf.class, id));
        em.getTransaction().commit();
        em.close();

        return Response.noContent().build(); // return code is 204
    }
}