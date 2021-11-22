package com.product;

import java.math.BigDecimal;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class ObservableProduct {
    private StringProperty name;
    private IntegerProperty countPerUnit;
    private IntegerProperty quantity;
    private StringProperty importPrice;
    private StringProperty exportPrice;

    private IntegerProperty userQuantity = new SimpleIntegerProperty(0);
    private StringProperty userAmount = new SimpleStringProperty("0");

    public ObservableProduct(String name, int countPerUnit, int quantity, BigDecimal importPrice,
            BigDecimal exportPrice) {
        this.name = new SimpleStringProperty(name);
        this.countPerUnit = new SimpleIntegerProperty(countPerUnit);
        this.quantity = new SimpleIntegerProperty(quantity);
        this.importPrice = new SimpleStringProperty(importPrice.toString());
        this.exportPrice = new SimpleStringProperty(exportPrice.toString());
    }

    // * gets, sets, property
    // name
    public String getName() {
        return name.get();
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public StringProperty nameProperty() {
        return name;
    }

    // count per unit
    public int getCountPerUnit() {
        return countPerUnit.get();
    }

    public void setCountPerUnit(int countperunit) {
        this.countPerUnit.get();
    }

    public IntegerProperty countPerUnitProperty() {
        return countPerUnit;
    }

    // quantity
    public int getQuantity() {
        return quantity.get();
    }

    public void setQuantity(int quantity) {
        this.quantity.set(quantity);
    }

    public IntegerProperty quantityProperty() {
        return quantity;
    }

    // import price
    public BigDecimal getImportPrice() {
        return new BigDecimal(importPrice.get());
    }

    public void setImportPrice(BigDecimal importPrice) {
        this.importPrice.set(importPrice.toString());
    }

    public StringProperty importPriceProperty() {
        return importPrice;
    }

    // export price
    public BigDecimal getExportPrice() {
        return new BigDecimal(exportPrice.get());
    }

    public void setExportPrice(BigDecimal exportPrice) {
        this.exportPrice.set(exportPrice.toString());
    }

    public StringProperty exportPriceProperty() {
        return exportPrice;
    }

    // count per unit
    public int getUserQuantity() {
        return userQuantity.get();
    }

    public void setUserQuantity(int userQuantity) {
        this.userQuantity.get();
    }

    public IntegerProperty userQuantityProperty() {
        return userQuantity;
    }

    // user amount
    public BigDecimal getUserAmount() {
        return new BigDecimal(userAmount.get());
    }

    public void setUserAmount(BigDecimal userAmount) {
        this.userAmount.set(userAmount.toString());
    }

    public StringProperty userAmountProperty() {
        return userAmount;
    }
}
