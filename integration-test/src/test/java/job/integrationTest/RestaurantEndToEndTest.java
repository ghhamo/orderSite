package job.integrationTest;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import okhttp3.*;
import org.junit.jupiter.api.*;

import java.io.IOException;
import java.util.Random;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_METHOD)
public class RestaurantEndToEndTest {

    private final String uri = "http://localhost:8080/api/restaurants";

    private UUID id;
    private String name;
    private String address;
    private int tables;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final Random random = new Random();
    private final OkHttpClient client = new OkHttpClient();

    @BeforeEach
    public void beforeSetup() throws Exception {
        name = UUID.randomUUID().toString();
        address = UUID.randomUUID().toString();
        tables = random.nextInt(1000);

        ObjectNode restaurantJson = objectMapper.createObjectNode()
                .put("name", name)
                .put("address", address)
                .put("tables", tables);

        RequestBody body = RequestBody.create(
                restaurantJson.toString(), MediaType.parse("application/json"));
        Request request = new Request.Builder()
                .url(uri)
                .post(body)
                .build();

        Response response = client.newCall(request).execute();
        assertEquals(201, response.code());

        assert response.body() != null;
        String responseContent = response.body().string();
        JsonNode jsonResponse = objectMapper.readTree(responseContent);
        id = UUID.fromString(jsonResponse.get("id").asText());
    }

    @AfterEach
    public void afterSetUp() throws IOException {
        Request request = new Request.Builder()
                .url(uri + "/" + id)
                .delete()
                .build();

        Response response = client.newCall(request).execute();
        assertEquals(204, response.code());
    }

    @Test
    public void testGetAllRestaurant() throws Exception {
        Request request = new Request.Builder()
                .url(uri + "?pageIndex=0&pageSize=20")
                .get()
                .build();

        Response response = client.newCall(request).execute();
        assertEquals(200, response.code());
        assert response.body() != null;
        String responseContent = response.body().string();
        JsonNode jsonResponse = objectMapper.readTree(responseContent);
        assertTrue(jsonResponse.isArray(), "Response should be an array");

        for (JsonNode restaurantNode : jsonResponse) {
            String restaurantId = restaurantNode.get("id").asText();
            String restaurantName = restaurantNode.get("name").asText();
            String restaurantAddress = restaurantNode.get("address").asText();
            int restaurantTables = restaurantNode.get("tables").asInt();
            assertNotNull(restaurantId);
            assertNotNull(restaurantName);
            assertNotNull(restaurantAddress);
            assertTrue(restaurantTables >= 0);
        }
    }

    @Test
    public void testAddProductToRestaurant() throws Exception {
        //{restaurantId}/products/{productId}
        //Create product
        String nName = String.valueOf(UUID.randomUUID());
        String nBrand = String.valueOf(UUID.randomUUID());
        String nDescription = String.valueOf(UUID.randomUUID());
        int nPrice = random.nextInt(10, 1000000);
        ObjectNode productJson = objectMapper.createObjectNode()
                .put("name", nName)
                .put("brand", nBrand)
                .put("description", nDescription)
                .put("price", nPrice);
        RequestBody body = RequestBody.create(
                productJson.toString(), MediaType.parse("application/json"));
        Request productRequest = new Request.Builder()
                .url("http://localhost:8080/api/products")
                .post(body)
                .build();
        Response productResponse = client.newCall(productRequest).execute();
        assertEquals(201, productResponse.code());
        assert productResponse.body() != null;
        String productResponseContent = productResponse.body().string();
        JsonNode productJsonResponse = objectMapper.readTree(productResponseContent);
        UUID productId = UUID.fromString(productJsonResponse.get("id").asText());
        //Add restaurant to product
        ObjectNode restaurantJson = objectMapper.createObjectNode()
                .put("restaurantId", id.toString())
                .put("productId", productId.toString());
        body = RequestBody.create(
                restaurantJson.toString(), MediaType.parse("application/json"));
        Request restaurantRequest = new Request.Builder()
                .url(uri + "/" + id + "/products/" + productId)
                .post(body)
                .build();
        Response restaurantResponse = client.newCall(restaurantRequest).execute();
        assertEquals(200, restaurantResponse.code());
        assert restaurantResponse.body() != null;
        String restaurantResponseContent = restaurantResponse.body().string();
        JsonNode restaurantJsonResponse = objectMapper.readTree(restaurantResponseContent);
        assertEquals(name, restaurantJsonResponse.get("name").asText());
        assertEquals(address, restaurantJsonResponse.get("address").asText());
        assertEquals(tables, restaurantJsonResponse.get("tables").asInt());
    }

    @Test
    public void testGetRestaurantsByProductId() throws Exception {
        testAddProductToRestaurant();
        Request request = new Request.Builder()
                .url(uri + "/" + id + "/products")
                .get()
                .build();
        Response response = client.newCall(request).execute();
        assertEquals(302, response.code());
        assert response.body() != null;
        String responseContent = response.body().string();
        JsonNode jsonResponse = objectMapper.readTree(responseContent);
        assertTrue(jsonResponse.isArray(), "Response should be an array");
        for (JsonNode productNode : jsonResponse) {
            String productId = productNode.get("id").asText();
            String productName = productNode.get("name").asText();
            String productBrand = productNode.get("brand").asText();
            String productDescription = productNode.get("description").asText();
            int productPrice = productNode.get("price").asInt();
            assertNotNull(productId);
            assertNotNull(productName);
            assertNotNull(productBrand);
            assertNotNull(productDescription);
            assertTrue(productPrice >= 0);
        }
    }

    @Test
    public void testGetRestaurantById() throws Exception {
        Request request = new Request.Builder()
                .url(uri + "/id/" + id)
                .get()
                .build();

        Response response = client.newCall(request).execute();
        assertEquals(302, response.code());

        assert response.body() != null;
        String responseContent = response.body().string();
        JsonNode jsonResponse = objectMapper.readTree(responseContent);
        assertEquals(id.toString(), jsonResponse.get("id").asText());
        assertEquals(name, jsonResponse.get("name").asText());
        assertEquals(address, jsonResponse.get("address").asText());
        assertEquals(tables, jsonResponse.get("tables").asInt());
    }

    @Test
    public void testGetRestaurantByName() throws Exception {
        Request request = new Request.Builder()
                .url(uri + "/name/" + name)
                .get()
                .build();

        Response response = client.newCall(request).execute();
        assertEquals(302, response.code());

        assert response.body() != null;
        String responseContent = response.body().string();
        JsonNode jsonResponse = objectMapper.readTree(responseContent);
        assertEquals(id.toString(), jsonResponse.get("id").asText());
        assertEquals(name, jsonResponse.get("name").asText());
        assertEquals(address, jsonResponse.get("address").asText());
        assertEquals(tables, jsonResponse.get("tables").asInt());
    }

    @Test
    public void testGetRestaurantByAddress() throws Exception {
        Request request = new Request.Builder()
                .url(uri + "/address/" + address)
                .get()
                .build();

        Response response = client.newCall(request).execute();
        assertEquals(302, response.code());

        assert response.body() != null;
        String responseContent = response.body().string();
        JsonNode jsonResponse = objectMapper.readTree(responseContent);
        assertEquals(id.toString(), jsonResponse.get("id").asText());
        assertEquals(name, jsonResponse.get("name").asText());
        assertEquals(address, jsonResponse.get("address").asText());
        assertEquals(tables, jsonResponse.get("tables").asInt());
    }

    @Test
    public void testGetNonExistentRestaurant() throws Exception {
        UUID nonExistentId = UUID.randomUUID();
        Request request = new Request.Builder()
                .url(uri + "/id/" + nonExistentId)
                .get()
                .build();
        Response response = client.newCall(request).execute();
        assertEquals(404, response.code());
    }

    @Test
    public void testUpdateRestaurant() throws Exception {
        int newTables = random.nextInt(1000);
        ObjectNode updateJson = objectMapper.createObjectNode()
                .put("id", id.toString())
                .put("tables", newTables);

        RequestBody body = RequestBody.create(
                updateJson.toString(), MediaType.parse("application/json"));
        Request request = new Request.Builder()
                .url(uri)
                .patch(body)
                .build();

        Response response = client.newCall(request).execute();
        assertEquals(200, response.code());

        assert response.body() != null;
        String responseContent = response.body().string();
        JsonNode jsonResponse = objectMapper.readTree(responseContent);
        assertEquals(id.toString(), jsonResponse.get("id").asText());
        assertEquals(name, jsonResponse.get("name").asText());
        assertEquals(address, jsonResponse.get("address").asText());
        assertEquals(newTables, jsonResponse.get("tables").asInt());
    }
}
