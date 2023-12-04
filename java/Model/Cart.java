package Model;

import java.io.Serializable;

public class Cart implements Serializable {

    private static final long id = 1L;
    private String listId;
    private String cartId;
    private String cartCode;
    private String cartName;
    private String cartPrice;
    private String cartQty;
    private String cartAmnt;
    private String retNameCart;

    public Cart() {
    }

    public String getRetNameCart() {
        return retNameCart;
    }

    public void setRetNameCart(String retNameCart) {
        this.retNameCart = retNameCart;
    }

    public String getCartAmnt() {
        return cartAmnt;
    }

    public void setCartAmnt(String cartAmnt) {
        this.cartAmnt = cartAmnt;
    }

    public static long getId() {
        return id;
    }

    public String getListId() {
        return listId;
    }

    public void setListId(String listId) {
        this.listId = listId;
    }

    public String getCartId() {
        return cartId;
    }

    public void setCartId(String cartId) {
        this.cartId = cartId;
    }

    public String getCartCode() {
        return cartCode;
    }

    public void setCartCode(String cartCode) {
        this.cartCode = cartCode;
    }

    public String getCartName() {
        return cartName;
    }

    public void setCartName(String cartName) {
        this.cartName = cartName;
    }

    public String getCartPrice() {
        return cartPrice;
    }

    public void setCartPrice(String cartPrice) {
        this.cartPrice = cartPrice;
    }

    public String getCartQty() {
        return cartQty;
    }

    public void setCartQty(String cartQty) {
        this.cartQty = cartQty;
    }
}
