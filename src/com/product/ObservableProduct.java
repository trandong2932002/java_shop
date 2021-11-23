package com.product;

import java.math.BigDecimal;

import javafx.beans.binding.ObjectBinding;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class ObservableProduct {
    private StringProperty name;
    private StringProperty countPerUnit;
    private StringProperty quantity;
    private StringProperty importPrice;
    private StringProperty exportPrice;

    private StringProperty userQuantity = new SimpleStringProperty("0");
    private StringProperty userAmount = new SimpleStringProperty("0");

    // * userAmount -> objectBinding -> userQuantity (-> : bind to)
    ObjectBinding<String> objectBinding = new ObjectBinding<String>() {
        {
            bind(userQuantity);
        }

        @Override
        protected String computeValue() {
            return getExportPrice().multiply(new BigDecimal(getUserQuantity())).toString();
        }
    };

    public ObservableProduct(String name, String countPerUnit, String quantity, BigDecimal importPrice,
            BigDecimal exportPrice) {
        this.name = new SimpleStringProperty(name);
        this.countPerUnit = new SimpleStringProperty(countPerUnit);
        this.quantity = new SimpleStringProperty(quantity);
        this.importPrice = new SimpleStringProperty(importPrice.toString());
        this.exportPrice = new SimpleStringProperty(exportPrice.toString());
        
        userAmount.bind(objectBinding);
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
        return Integer.parseInt(countPerUnit.get());
    }

    public void setCountPerUnit(int countperunit) {
        this.countPerUnit.get();
    }

    public StringProperty countPerUnitProperty() {
        return countPerUnit;
    }

    // quantity
    public int getQuantity() {
        return Integer.parseInt(quantity.get());
    }

    public void setQuantity(int quantity) {
        this.quantity.set(quantity + "");
    }

    public StringProperty quantityProperty() {
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
        return Integer.parseInt(userQuantity.get());
    }

    public void setUserQuantity(int userQuantity) {
        this.userQuantity.set(userQuantity + "");
    }

    public StringProperty userQuantityProperty() {
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
