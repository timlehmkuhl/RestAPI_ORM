package resources;

import model.Kunde;

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
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Path("/kunden")
public class KundeResource {

    final static Map<Integer, Kunde> kunden = new ConcurrentHashMap<>();

    @GET
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Collection<Kunde> getAccounts() {
        return kunden.values();  // return code is 200
    }

    @GET
    @Path("{id}")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response getKunde(@PathParam("id") int id) {
        Kunde kunde = kunden.get(id);
        if (kunde == null) {
            return Response.status(Response.Status.NOT_FOUND).build(); // return code is 404
        }
        return Response.ok(kunde).build(); // return code is 200
    }

    @POST
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response postKunde(@NotNull Kunde kunde, @Context UriInfo uriInfo) {
        boolean validId = kunde.kundenID > 0 && kunden.get(kunde.kundenID) == null;
        if (!validId) {
            kunde.kundenID = Kunde.nextId.getAndIncrement();
        }
        kunden.put(kunde.kundenID, kunde);
        URI uri = uriInfo.getAbsolutePathBuilder().path(Integer.toString(kunde.kundenID)).build(); // append new id to URI

       /* System.out.println("POSTET: " +  kunde.kundenId + "" );
        System.out.println(kunde.kundenId);
        kunde.getEntries().forEach(x -> System.out.println("value: " + x.value + ", date: " + x.date));*/

        return Response.created(uri).entity(kunde).build(); // return code is 201
    }

    @PUT
    @Path("{id}")
    public Response putKunde(@PathParam("id") int id, @NotNull Kunde kunde, @Context UriInfo uriInfo) {
        boolean exists = kunden.get(id) != null;
        kunde.kundenID = id;
        if (!exists) {
            return postKunde(kunde, uriInfo);
        } else {
            kunden.put(id, kunde);
            return Response.ok(kunde).build(); // return code is 200
        }
    }
/*
    @PATCH
    @Path("{id}")
    public Response patchAccount(@PathParam("id") int id, @NotNull Account patchedAccount) {
        Account account = accounts.get(id);
        boolean exists = account != null;
        if (!exists) {
            return Response.status(404).build(); // return code is 404
        } else {
            if (patchedAccount.owner != null) {
                account.owner = patchedAccount.owner;
            }
            if (patchedAccount.entries != null) {
                account.entries = patchedAccount.entries;
            }
            return Response.ok(account).build(); // return code is 200
        }
    }

    @DELETE
    public Response deleteAccounts() {
        accounts.clear();
        System.out.println("DELETED ALL");
        return Response.noContent().build(); // return code is 204
    }

    @DELETE
    @Path("{id}")
    public Response deleteAccount(@PathParam("id") int id) {
        accounts.remove(id);
        System.out.println("POSTET: " +  id + "" );
        return Response.noContent().build(); // return code is 204
    }*/
}
