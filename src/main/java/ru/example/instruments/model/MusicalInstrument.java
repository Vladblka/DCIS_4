package ru.example.instruments.model;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public class MusicalInstrument {
    private Long id;

    @NotBlank(message = "Укажите название инструмента")
    @Size(max = 120, message = "Название не должно быть длиннее 120 символов")
    private String name;

    @NotBlank(message = "Укажите бренд")
    @Size(max = 120, message = "Бренд не должен быть длиннее 120 символов")
    private String brand;

    @NotBlank(message = "Укажите тип инструмента")
    @Size(max = 80, message = "Тип инструмента не должен быть длиннее 80 символов")
    private String instrumentType;

    @NotBlank(message = "Укажите материал")
    @Size(max = 120, message = "Материал не должен быть длиннее 120 символов")
    private String material;

    @NotNull(message = "Укажите цену")
    @DecimalMin(value = "0.0", message = "Цена не может быть отрицательной")
    @Digits(integer = 10, fraction = 2, message = "Цена: не более 10 цифр до запятой и 2 после")
    private BigDecimal price;

    @NotNull(message = "Укажите количество")
    @Min(value = 0, message = "Количество не может быть отрицательным")
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
