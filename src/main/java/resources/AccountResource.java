package resources;

import model.Account;

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

@Path("/accounts")
public class AccountResource {

    final static Map<Integer, Account> accounts = new ConcurrentHashMap<>();

    @GET
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Collection<Account> getAccounts() {
        return accounts.values();  // return code is 200
    }

    @GET
    @Path("{id}")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response getAccount(@PathParam("id") int id) {
        Account account = accounts.get(id);
        if (account == null) {
            return Response.status(Response.Status.NOT_FOUND).build(); // return code is 404
        }
        return Response.ok(account).build(); // return code is 200
    }

    @POST
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response postAccount(@NotNull Account account, @Context UriInfo uriInfo) {
        boolean validId = account.id > 0 && accounts.get(account.id) == null;
        if (!validId) {
            account.id = Account.nextId.getAndIncrement();
        }
        accounts.put(account.id, account);
        URI uri = uriInfo.getAbsolutePathBuilder().path(Integer.toString(account.id)).build(); // append new id to URI
        System.out.println("POSTET: " +  account.id + "" );
        return Response.created(uri).entity(account).build(); // return code is 201
    }

    @PUT
    @Path("{id}")
    public Response putAccount(@PathParam("id") int id, @NotNull Account account, @Context UriInfo uriInfo) {
        boolean exists = accounts.get(id) != null;
        account.id = id;
        if (!exists) {
            return postAccount(account, uriInfo);
        } else {
            accounts.put(id, account);
            return Response.ok(account).build(); // return code is 200
        }
    }

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
    }
}
