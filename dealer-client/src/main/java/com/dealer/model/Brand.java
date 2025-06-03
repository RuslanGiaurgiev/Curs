package com.dealer.model;

import java.util.List;

public class Brand {
    private Long id;
    private String name;
    private List<CarModel> models;

    public Brand() {}

    public Brand(Long id, String name, List<CarModel> models) {
        this.id = id;
        this.name = name;
        this.models = models;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public List<CarModel> getModels() { return models; }
    public void setModels(List<CarModel> models) { this.models = models; }

    @Override
    public String toString() {
        return name != null ? name : "«unknown»";
    }
}
