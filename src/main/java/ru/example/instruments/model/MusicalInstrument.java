package ru.example.instruments.model;

import java.math.BigDecimal;

public class MusicalInstrument {
    private Long id;
    private String name;
    private String brand;
    private String instrumentType;
    private String material;
    private BigDecimal price;
    private Integer quantity;

    public MusicalInstrument() {
    }

    public MusicalInstrument(Long id, String name, String brand, String instrumentType,
                            String material, BigDecimal price, Integer quantity) {
        this.id = id;
        this.name = name;
        this.brand = brand;
        this.instrumentType = instrumentType;
        this.material = material;
        this.price = price;
        this.quantity = quantity;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getBrand() { return brand; }
    public void setBrand(String brand) { this.brand = brand; }
    public String getInstrumentType() { return instrumentType; }
    public void setInstrumentType(String instrumentType) { this.instrumentType = instrumentType; }
    public String getMaterial() { return material; }
    public void setMaterial(String material) { this.material = material; }
    public BigDecimal getPrice() { return price; }
    public void setPrice(BigDecimal price) { this.price = price; }
    public Integer getQuantity() { return quantity; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }

    @Override
    public String toString() {
        return "№" + id + " | " + name + " | бренд: " + brand
                + " | тип: " + instrumentType + " | материал: " + material
                + " | цена: " + price + " | количество: " + quantity;
    }
}
