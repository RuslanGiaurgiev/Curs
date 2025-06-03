package com.dealer.client;

import com.dealer.model.Brand;
import com.dealer.model.CarModel;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.client.fluent.Request;
import org.apache.http.entity.ContentType;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.DoubleSummaryStatistics;
import java.util.List;
import java.util.stream.Collectors;

public class ApiClient {
    private final String baseUrl, authHeader;
    private final ObjectMapper mapper = new ObjectMapper();

    public ApiClient(String baseUrl, String user, String pass) {
        this.baseUrl = baseUrl;
        String creds = user + ":" + pass;
        this.authHeader = "Basic " +
                Base64.getEncoder().encodeToString(creds.getBytes(StandardCharsets.UTF_8));
    }

    private Request withAuth(Request r) {
        return r.addHeader("Authorization", authHeader)
                .connectTimeout(2000).socketTimeout(2000);
    }

    private String get(String path) throws Exception {
        return withAuth(Request.Get(baseUrl + path))
                .execute().returnContent().asString();
    }

    private String post(String path, Object body) throws Exception {
        String json = mapper.writeValueAsString(body);
        return withAuth(Request.Post(baseUrl + path)
                .bodyString(json, ContentType.APPLICATION_JSON))
                .execute().returnContent().asString();
    }

    private String put(String path, Object body) throws Exception {
        String json = mapper.writeValueAsString(body);
        return withAuth(Request.Put(baseUrl + path)
                .bodyString(json, ContentType.APPLICATION_JSON))
                .execute().returnContent().asString();
    }

    private int delete(String path) throws Exception {
        return withAuth(Request.Delete(baseUrl + path))
                .execute().returnResponse().getStatusLine().getStatusCode();
    }

    // Brands
    public List<Brand> listBrands() throws Exception {
        return mapper.readValue(get("/api/brands"), new TypeReference<>() {});
    }
    public Brand createBrand(Brand b) throws Exception {
        return mapper.readValue(post("/api/brands", b), Brand.class);
    }
    public Brand updateBrand(Brand b) throws Exception {
        return mapper.readValue(put("/api/brands/" + b.getId(), b), Brand.class);
    }
    public boolean deleteBrand(Long id) throws Exception {
        return delete("/api/brands/" + id) == 204;
    }

    // CarModels
    public List<CarModel> listModels() throws Exception {
        return mapper.readValue(get("/api/models"), new TypeReference<>() {});
    }
    public CarModel createModel(CarModel m) throws Exception {
        return mapper.readValue(post("/api/models", m), CarModel.class);
    }
    public CarModel updateModel(CarModel m) throws Exception {
        return mapper.readValue(put("/api/models/" + m.getId(), m), CarModel.class);
    }
    public boolean deleteModel(Long id) throws Exception {
        return delete("/api/models/" + id) == 204;
    }

    public DoubleSummaryStatistics getPriceStats(List<CarModel> list) {
        return list.stream()
                .collect(Collectors.summarizingDouble(CarModel::getPrice));
    }
}
