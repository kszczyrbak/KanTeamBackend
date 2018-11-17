package szczkrzy.kanteam;

import org.junit.*;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.test.context.junit4.SpringRunner;
import szczkrzy.kanteam.model.entities.KTUser;
import szczkrzy.kanteam.model.requests.LoginRequest;
import szczkrzy.kanteam.model.requests.SignupRequest;

import java.util.Random;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = KanTeamBackendApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserAndAuthControllersTests {

    @LocalServerPort
    private int port;

    private TestRestTemplate restTemplate = new TestRestTemplate();

    private static HttpHeaders headers = new HttpHeaders();

    private static KTUser testUser;

    @BeforeClass
    public static void initHeaders() {
        headers.add("Authorization", "Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJrc3pjenlyYmFrOEBnbWFpbC5jb20iLCJzY29wZXMiOlt7ImF1dGhvcml0eSI6IkFETUlOIn1dLCJpYXQiOjE1MzEwNzMyMjIsImV4cCI6MTUzMTg2NDgwMH0.F1E1tWf5rvICFOBix0QnTgUBUW3KQ4dItj5eefELX2k");
    }

    @Before
    public void initTestUser() {
        testUser = postTestUser().getBody();
    }

    @After
    public void removeTestUser() {
        if (testUser != null)
            removeUser(testUser);

    }

    @Test
    public void testRegister() {
        ResponseEntity<KTUser> response = postTestUser();

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    public void testLogin() {
        LoginRequest loginRequest = new LoginRequest(testUser.getEmail(), "test");
        ResponseEntity<String> response = login(loginRequest);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    public void testRetrieveUser() {
        ResponseEntity<KTUser> response = getUser(testUser.getId());

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    public void testRetrieveAllUsers() {
        HttpEntity<String> entity = new HttpEntity<>(null, headers);

        ResponseEntity<String> response = restTemplate.exchange(
                createURLWithPort("/api/users/"),
                HttpMethod.GET, entity, String.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    public void testUpdateUser() {
        testUser.setFullName("TestKappa");

        ResponseEntity<KTUser> response = updateUser(testUser);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    public void testRemoveUser() {
        int testUserId = testUser.getId();
        ResponseEntity<String> response2 = removeUser(testUser);
        assertEquals(response2.getStatusCode(), HttpStatus.OK);

        ResponseEntity<KTUser> response = getUser(testUserId);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    public void testRemoveUserById() {
        int testUserId = testUser.getId();
        ResponseEntity<String> response2 = removeUserById(testUser.getId());
        assertEquals(response2.getStatusCode(), HttpStatus.OK);

        ResponseEntity<KTUser> response = getUser(testUserId);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    private String createURLWithPort(String uri) {
        return "http://localhost:" + port + uri;
    }

    private ResponseEntity<KTUser> postTestUser() {
        Random random = new Random();
        String email = "test" + random.nextInt(10000) + "@gmail.com";
        SignupRequest user = new SignupRequest(email, "test", "TEST_NAME_SHOULD_BE_DELETED");
        HttpEntity<SignupRequest> entity2 = new HttpEntity<>(user, null);

        return restTemplate.exchange(
                createURLWithPort("/auth/signup/"),
                HttpMethod.POST, entity2, KTUser.class);
    }

    private ResponseEntity<KTUser> getUser(int id) {
        HttpEntity<String> entity = new HttpEntity<>(null, headers);

        return restTemplate.exchange(
                createURLWithPort("/api/users/" + id),
                HttpMethod.GET, entity, KTUser.class);
    }

    private ResponseEntity<KTUser> updateUser(KTUser user) {
        HttpEntity<KTUser> entity = new HttpEntity<>(user, headers);

        return restTemplate.exchange(
                createURLWithPort("/api/users/"),
                HttpMethod.PUT, entity, KTUser.class);
    }

    private ResponseEntity<String> removeUserById(int id) {
        HttpEntity<Integer> entity = new HttpEntity<>(id, headers);

        testUser = null;

        return restTemplate.exchange(
                createURLWithPort("/api/users/" + id),
                HttpMethod.DELETE, entity, String.class);
    }

    private ResponseEntity<String> removeUser(KTUser user) {
        HttpEntity<KTUser> entity = new HttpEntity<>(user, headers);

        testUser = null;

        return restTemplate.exchange(
                createURLWithPort("/api/users/"),
                HttpMethod.DELETE, entity, String.class);
    }

    private ResponseEntity<String> login(LoginRequest loginRequest) {
        HttpEntity<LoginRequest> entity = new HttpEntity<>(loginRequest, null);

        return restTemplate.exchange(
                createURLWithPort("/auth/login/"),
                HttpMethod.POST, entity, String.class);
    }


}
