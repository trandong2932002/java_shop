package com.product;

import java.math.BigDecimal;

public class Product {
    private String name;
    private int count_per_unit;
    private int quantity;
    private BigDecimal import_price;
    private BigDecimal export_price;

    public Product(String name, int count_per_unit, int quantity, BigDecimal import_price, BigDecimal export_price) {
        this.name = name;
        this.count_per_unit = count_per_unit;
        this.quantity = quantity;
        this.import_price = import_price;
        this.export_price = export_price;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCount_per_unit() {
        return count_per_unit;
    }

    public void setCount_per_unit(int count_per_unit) {
        this.count_per_unit = count_per_unit;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getImport_price() {
        return import_price;
    }

    public void setImport_price(BigDecimal import_price) {
        this.import_price = import_price;
    }

    public BigDecimal getExport_price() {
        return export_price;
    }

    public void setExport_price(BigDecimal export_price) {
        this.export_price = export_price;
    }
}
