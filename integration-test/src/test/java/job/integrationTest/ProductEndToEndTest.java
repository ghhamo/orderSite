package job.integrationTest;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import okhttp3.*;
import org.junit.jupiter.api.*;

import java.util.Random;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ProductEndToEndTest {

    private final String uri = "http://localhost:8080/api/products";


    private UUID id;
    private String name;
    private String brand;
    private String description;
    private double price;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final Random random = new Random();
    private final OkHttpClient client = new OkHttpClient();

    @BeforeEach
    public void beforeSetup() throws Exception {
        name = String.valueOf(UUID.randomUUID());
        brand = String.valueOf(UUID.randomUUID());
        description = String.valueOf(UUID.randomUUID());
        price = random.nextInt(10, 1000000);
        ObjectNode restaurantJson = objectMapper.createObjectNode()
                .put("name", name)
                .put("brand", brand)
                .put("description", description)
                .put("price", price);
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
    public void afterSetUp() throws Exception {
        Request request = new Request.Builder()
                .url(uri + "/" + id)
                .delete()
                .build();
        Response response = client.newCall(request).execute();
        assertEquals(204, response.code());
    }

    @Test
    public void testGetAllProduct() throws Exception {
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
            String restaurantBrand = restaurantNode.get("brand").asText();
            String restaurantDescription = restaurantNode.get("description").asText();
            int restaurantPrice = restaurantNode.get("price").asInt();
            assertNotNull(restaurantId);
            assertNotNull(restaurantName);
            assertNotNull(restaurantBrand);
            assertNotNull(restaurantDescription);
            assertTrue(restaurantPrice >= 0);
        }
    }

    @Test
    public void testAddRestaurantToProduct() throws Exception {
        //Create restaurant
        String nName = String.valueOf(UUID.randomUUID());
        String nAddress = String.valueOf(UUID.randomUUID());
        int nTables = random.nextInt(0, 100);
        ObjectNode restaurantJson = objectMapper.createObjectNode()
                .put("name", nName)
                .put("address", nAddress)
                .put("tables", nTables);
        RequestBody body = RequestBody.create(
                restaurantJson.toString(), MediaType.parse("application/json"));
        Request restaurantRequest = new Request.Builder()
                .url("http://localhost:8080/api/restaurants")
                .post(body)
                .build();
        Response restaurantResponse = client.newCall(restaurantRequest).execute();
        assertEquals(201, restaurantResponse.code());
        assert restaurantResponse.body() != null;
        String restaurantResponseContent = restaurantResponse.body().string();
        JsonNode restaurantJsonResponse = objectMapper.readTree(restaurantResponseContent);
        UUID restaurantId = UUID.fromString(restaurantJsonResponse.get("id").asText());
        //Add restaurant to product
        ObjectNode productJson = objectMapper.createObjectNode()
                .put("productId", id.toString())
                .put("restaurantId", restaurantId.toString());
        body = RequestBody.create(
                productJson.toString(), MediaType.parse("application/json"));
        Request productRequest = new Request.Builder()
                .url(uri + "/" + id + "/restaurants/" + restaurantId)
                .post(body)
                .build();
        Response productResponse = client.newCall(productRequest).execute();
        assertEquals(200, productResponse.code());
        assert productResponse.body() != null;
        String productResponseContent = productResponse.body().string();
        JsonNode productJsonResponse = objectMapper.readTree(productResponseContent);
        assertEquals(name, productJsonResponse.get("name").asText());
        assertEquals(brand, productJsonResponse.get("brand").asText());
        assertEquals(description, productJsonResponse.get("description").asText());
        assertEquals(price, productJsonResponse.get("price").asDouble());

    }

    @Test
    public void testGetProductById() throws Exception {
        Request request = new Request.Builder()
                .url(uri + "/" + id)
                .get()
                .build();
        Response response = client.newCall(request).execute();
        assertEquals(302, response.code());
        assert response.body() != null;
        String responseBody = response.body().string();
        JsonNode jsonResponse = objectMapper.readTree(responseBody);
        assertEquals(name, jsonResponse.get("name").asText());
        assertEquals(brand, jsonResponse.get("brand").asText());
        assertEquals(description, jsonResponse.get("description").asText());
        assertEquals(price, jsonResponse.get("price").asDouble());
    }

    @Test
    public void testGetProductByNameBrandAndDescription() throws Exception {
        Request request = new Request.Builder()
                .url(uri + "/compose" + "?name=" + name + "&brand=" + brand + "&description=" + description)
                .get()
                .build();
        Response response = client.newCall(request).execute();
        assertEquals(302, response.code());
        assert response.body() != null;
        String responseBody = response.body().string();
        JsonNode jsonResponse = objectMapper.readTree(responseBody);
        assertEquals(name, jsonResponse.get("name").asText());
        assertEquals(brand, jsonResponse.get("brand").asText());
        assertEquals(description, jsonResponse.get("description").asText());
        assertEquals(price, jsonResponse.get("price").asDouble());
    }

    @Test
    public void testGetRestaurantsByProductId() throws Exception {
        testAddRestaurantToProduct();
        Request request = new Request.Builder()
                .url(uri + "/" + id + "/restaurants")
                .get()
                .build();
        Response response = client.newCall(request).execute();
        assertEquals(302, response.code());
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
    public void testGetPriceOfProduct() throws Exception {
        Request request = new Request.Builder()
                .url(uri + "/" + id + "/price")
                .get()
                .build();
        Response response = client.newCall(request).execute();
        assertEquals(302, response.code());
        assert response.body() != null;
        double nPrice = Double.parseDouble(response.body().string());
        assertEquals(nPrice, price);
    }

    @Test
    public void testGetNonExistentProduct() throws Exception {
        Request request = new Request.Builder()
                .url(uri + "/" + UUID.randomUUID())
                .get()
                .build();
        Response response = client.newCall(request).execute();
        assertEquals(404, response.code());

    }

    @Test
    public void testUpdateProduct() throws Exception {
        String nDescription = String.valueOf(UUID.randomUUID());
        ObjectNode updateJson = objectMapper.createObjectNode()
                .put("id", id.toString())
                .put("description", nDescription);
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
        assertEquals(name, jsonResponse.get("name").asText());
        assertEquals(brand, jsonResponse.get("brand").asText());
        assertEquals(nDescription, jsonResponse.get("description").asText());
        assertEquals(price, jsonResponse.get("price").asDouble());
    }
}
