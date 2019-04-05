import model.Account;
import org.glassfish.jersey.client.HttpUrlConnectorProvider;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import resources.AccountResource;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.Response;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@TestInstance(Lifecycle.PER_CLASS)
public class AccountResourceTest extends JerseyTest {

    @Override
    protected Application configure() {
        return new ResourceConfig(AccountResource.class);
    }

    @BeforeEach
    public void prepareTest() {
        target().path("accounts").request().delete();
        String a1 = "{\"owner\": \"A\", \"entries\": [{\"value\": 20}, {\"value\": -10}]}";
        target("accounts/1").request().put(Entity.json(a1));
        String a2 = "{\"owner\": \"B\"}";
        target("accounts/2").request().put(Entity.json(a2));
    }

    @Test
    public void testGetAccount() {
        Response res = target("accounts/1").request().get(Response.class);
        assertEquals(200, res.getStatus());
        Account account = res.readEntity(Account.class);
        assertEquals(10, account.balance());
        assertEquals("A", account.owner);
    }

    @Test
    public void testGetAccounts() {
        Response res = target("accounts").request().get(Response.class);
        assertEquals(200, res.getStatus());
        List<Account> accounts = res.readEntity(new GenericType<List<Account>>() {});
        assertEquals(2, accounts.size());
        assertEquals(10, accounts.get(0).balance());
        assertEquals("B", accounts.get(1).owner);
    }

    @Test
    public void testPostAccount() {
        String a = "{\"owner\": \"C\", \"entries\": [{\"value\": 30}, {\"value\": 20}]}";
        Response res = target("accounts").request().post(Entity.json(a));
        assertEquals(201, res.getStatus());
        Account account = res.readEntity(Account.class);
        assertEquals(50, account.balance());
        assertEquals("C", account.owner);
    }

    @Test
    public void testPutAccount() {
        String a = "{\"owner\": \"Changed\"}";
        Response res = target("accounts/1").request().put(Entity.json(a));
        assertEquals(200, res.getStatus());
        Account account = res.readEntity(Account.class);
        assertEquals(0, account.balance());
        assertEquals("Changed", account.owner);
    }

    @Test
    public void testPatchAccount() {
        String a = "{\"owner\": \"Changed\"}";
        Response res = target("accounts/1").request().build("PATCH", Entity.json(a))
                .property(HttpUrlConnectorProvider.SET_METHOD_WORKAROUND, true).invoke();
        assertEquals(200, res.getStatus());
        Account account = res.readEntity(Account.class);
        assertEquals(10, account.balance());
        assertEquals("Changed", account.owner);
    }

    @Test
    public void testDeleteAccount() {
        Response res = target("accounts/2").request().delete();
        assertEquals(204, res.getStatus());

        res = target("accounts/2").request().get(Response.class);
        assertEquals(404, res.getStatus());
    }

    // Workaround: https://github.com/jersey/jersey/issues/3662
    @BeforeAll
    public void before() throws Exception {
        super.setUp();
    }

    @AfterAll
    public void after() throws Exception {
        super.tearDown();
    }
}
