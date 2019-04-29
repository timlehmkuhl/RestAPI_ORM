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

    //Laedt alle Datensaetze aus der DB und gibt diese zurueck
    @GET
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Collection<Kauf> getKaeufe() {
        Query q = em.createQuery("select k from Kauf k");
        List<Kauf> list = q.getResultList();
        em.close();

        return list;  // return code is 200
    }

    //Laedt einen einzelnen Datensatz mit beliebiger ID aus der DB und gibt diesen zurueck
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

    //Fuegt einen neuen Datensatz in die DB ein
    @POST
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response postKauf(@NotNull Kauf kauf, @Context UriInfo uriInfo) {

        kauf.getPositions().forEach(x -> x.setKauf(kauf));  //Der Position einen Kauf zuordnen

        em.getTransaction().begin();
        em.persist(kauf);
        em.getTransaction().commit();
        em.close();

        URI uri = uriInfo.getAbsolutePathBuilder().path(Integer.toString(kauf.kaufID)).build(); // Neue id zu URI anfuegen

        return Response.created(uri).entity(kauf).build(); // return code is 201
    }

    //Einen beliebigen Datensatz aendern und ungeaenderte Attribute aus null/0 setzen
    @PUT
    @Path("{id}")
    public Response putKauf(@PathParam("id") int id, @NotNull Kauf kauf, @Context UriInfo uriInfo) {

        Kauf findKauf = em.find(Kauf.class, id);

        boolean exists = findKauf != null;
        kauf.kaufID = id;
        if (!exists) {
            return postKauf(kauf, uriInfo);     //wenn der Kauf nicht existiert, erstelle einen neuen
        } else {
            //Ausgewaelter Kaeuf loeschen und mit neuen Werten einfuegen

            kauf.getPositions().forEach(x -> x.setKauf(kauf));      //Der Position einen Kauf zuordnen
            em.getTransaction().begin();
            em.merge(kauf);
            em.getTransaction().commit();


            em.close();
            return Response.ok(kauf).build(); // return code is 200
        }
    }


    //Alle Datensaetze aus der DB loeschen
    @DELETE
    public Response deleteKaeufe() {

        em.getTransaction().begin();
        Query q = em.createQuery("Delete FROM Position ");
        Query q2 = em.createQuery("Delete FROM Kauf");
        q.executeUpdate();
        q2.executeUpdate();
        em.getTransaction().commit();
        em.close();

        return Response.noContent().build(); // return code is 204
    }

    //Einen beliegigen Datensatz loeschen
    @DELETE
    @Path("{id}")
    public Response deleteKunde(@PathParam("id") int id) {

        em.getTransaction().begin();
       //Um fk Beziehungsprobleme auszuloesen erst die Positionen loeschen
        Query qD =  em.createQuery("delete from Position p where p.kauf.kaufID = :sqlWhere");
        qD.setParameter("sqlWhere", id);
        qD.executeUpdate();
        em.remove(em.find(Kauf.class, id));
        em.getTransaction().commit();
        em.close();

        return Response.noContent().build(); // return code is 204
    }
}