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


@Path("/kunden")
public class KundeResource {
    EntityManagerFactory emf = Persistence.createEntityManagerFactory("mariadb-localhost");
    EntityManager em = emf.createEntityManager();

    //Laedt alle Datensaetze aus der DB und gibt diese zurueck
    @GET
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Collection<Kunde> getKunden() {
    Query q = em.createQuery("select k from Kunde k");
            List<Kunde> list = q.getResultList();
            em.close();

        return list;  // return code is 200
    }

    //Laedt einen einzelnen Datensatz mit beliebiger ID aus der DB und gibt diesen zurueck
    @GET
    @Path("{id}")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response getKunde(@PathParam("id") int id) {

        //Gesuchten Kunden aus DB holen
        Kunde kunde =  em.find(Kunde.class, id);
        em.close();
        if (kunde == null) {
            return Response.status(Response.Status.NOT_FOUND).build(); // return code is 404
        }

        return Response.ok(kunde).build(); // return code is 200
    }

    //Fuegt einen neuen Datensatz in die DB ein
    @POST
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response postKunde(@NotNull Kunde kunde, @Context UriInfo uriInfo) {

        //Kunde in Datenbank
        em.getTransaction().begin();
        em.persist(kunde);
        em.getTransaction().commit();
        em.close();

        URI uri = uriInfo.getAbsolutePathBuilder().path(Integer.toString(kunde.kundenID)).build(); // Neue id zu URI anfuegen

        return Response.created(uri).entity(kunde).build(); // return code is 201
    }

    //Einen beliebigen Datensatz aendern und ungeaenderte Attribute aus null/0 setzen
    @PUT
    @Path("{id}")
    public Response putKunde(@PathParam("id") int id, @NotNull Kunde kunde, @Context UriInfo uriInfo) {

        Kunde findKunde =  em.find(Kunde.class, id);   //q.getSingleResult();

        boolean exists = findKunde != null;
        kunde.kundenID = id;
        if (!exists) {
            return postKunde(kunde, uriInfo);   //wenn der Kunde nicht existiert, erstelle einen neuen
        } else {
            //Ausgewaelten Kunden loeschen und mit neuen Werten einfügen

            em.getTransaction().begin();
            em.merge(kunde);
            em.getTransaction().commit();
            em.close();
            return Response.ok(kunde).build(); // return code is 200
        }
    }

    //Werte aus einem beliegigem Datensatz aendern (laut Aufgabenstellung nicht gefordert)
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

            if (kunde.anschrift == null){
                kunde.anschrift = patchedKunde.anschrift;
                System.out.println("TEST");
            }

            em.getTransaction().begin();
            em.persist(kunde);
            em.getTransaction().commit();
            em.close();


            return Response.ok(kunde).build(); // return code is 200
        }

    }

    //Alle Datensaetze aus der DB loeschen
    @DELETE
    public Response deleteKunden() {

        em.getTransaction().begin();
        Query q =  em.createQuery("Delete FROM Kunde");
        q.executeUpdate();
        em.getTransaction().commit();
        em.close();

        return Response.noContent().build(); // return code is 204
    }

    //Einen beliegigen Datensatz loeschen
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
