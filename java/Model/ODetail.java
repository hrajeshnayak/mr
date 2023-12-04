package Model;

import java.io.Serializable;


public class ODetail implements Serializable {

    private static final long id = 1L;
    private String orderProdName;
    private String orderProdPrice;
    private String orderProdQty;
    private String orderProdAmount;

    public ODetail() {
    }


    public String getOrderProdName() {
        return orderProdName;
    }

    public void setOrderProdName(String orderProdName) {
        this.orderProdName = orderProdName;
    }

    public String getOrderProdPrice() {
        return orderProdPrice;
    }

    public void setOrderProdPrice(String orderProdPrice) {
        this.orderProdPrice = orderProdPrice;
    }

    public String getOrderProdQty() {
        return orderProdQty;
    }

    public void setOrderProdQty(String orderProdQty) {
        this.orderProdQty = orderProdQty;
    }

    public String getOrderProdAmount() {
        return orderProdAmount;
    }

    public void setOrderProdAmount(String orderProdAmount) {
        this.orderProdAmount = orderProdAmount;
    }
}
