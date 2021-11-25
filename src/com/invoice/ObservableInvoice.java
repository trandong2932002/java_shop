package com.invoice;

import java.math.BigDecimal;

import javafx.beans.binding.ObjectBinding;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class ObservableInvoice {
    private String id;

    private StringProperty numeroSign;
    private StringProperty name;
    private StringProperty quantity = new SimpleStringProperty("0");
    private StringProperty unitPrice;
    private StringProperty price = new SimpleStringProperty("0");

    // * quantity -> objectBinding -> Price (Price = unitPrice * quantity)
    ObjectBinding<String> objectBinding = new ObjectBinding<String>() {
        {
            bind(quantity);
        }

        @Override
        protected String computeValue() {
            return getUnitPrice().multiply(new BigDecimal(getQuantity())).toString();
        }
    };

    public ObservableInvoice(String id, int num, String name, int quantity, BigDecimal unitPrice) {
        this.id = id;
        numeroSign = new SimpleStringProperty(num + "");
        this.name = new SimpleStringProperty(name);
        this.quantity = new SimpleStringProperty(quantity + "");
        this.unitPrice = new SimpleStringProperty(unitPrice.toString());

        price.bind(objectBinding);
    }

    // *gets, sets, property
    // id
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    // numero sign
    public int getNumeroSign() {
        return Integer.parseInt(numeroSign.get());
    }

    public void setNumeroSign(int numeroSign) {
        this.numeroSign.set(numeroSign + "");
    }

    public StringProperty numeroSignProperty() {
        return numeroSign;
    }

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

    // unit price
    public BigDecimal getUnitPrice() {
        return new BigDecimal(unitPrice.get());
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice.set(unitPrice.toString());
    }

    public StringProperty unitPriceProperty() {
        return unitPrice;
    }

    // price
    public BigDecimal getPrice() {
        return new BigDecimal(price.get());
    }

    public void setPrice(BigDecimal price) {
        this.price.set(price.toString());
    }

    public StringProperty priceProperty() {
        return price;
    }
}
