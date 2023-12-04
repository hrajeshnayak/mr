package Model;

import java.io.Serializable;
import java.util.Date;

public class Order implements Serializable {
    private static final long id = 1L;
    private String orderId;
    private String orderRet;
    private String orderDate;
    private String orderStatus;
    private Date orderDat;


    public Order() {
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getOrderRet() {
        return orderRet;
    }

    public void setOrderRet(String orderRet) {
        this.orderRet = orderRet;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public Date getOrderDat() {
        return orderDat;
    }

    public void setOrderDat(Date orderDat) {
        this.orderDat = orderDat;
    }



}
