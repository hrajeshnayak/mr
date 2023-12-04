package Model;

import java.io.Serializable;

public class Prod implements Serializable {
    private static final long id = 1L;

    private String prodName;
    private String catName;
    private String catCode;
    private String prodAvail;
    private String prodPrice;
    private String prodCode;
    private String retCode;





    public Prod() {
    }

    public String getProdName() {
        return prodName;
    }

    public void setProdName(String prodName) {
        this.prodName = prodName;
    }

    public String getCatName() {
        return catName;
    }

    public void setCatName(String catName) {
        this.catName = catName;
    }

    public String getCatCode() {
        return catCode;
    }

    public void setCatCode(String catCode) {
        this.catCode = catCode;
    }

    public String getProdCode() {
        return prodCode;
    }

    public void setProdCode(String prodCode) {
        this.prodCode = prodCode;
    }

    public String getProdAvail() {
        return prodAvail;
    }

    public void setProdAvail(String prodAvail) {
        this.prodAvail = prodAvail;
    }

    public String getProdPrice() {
        return prodPrice;
    }

    public void setProdPrice(String prodPrice) {
        this.prodPrice = prodPrice;
    }

    public String getRetCode() {
        return retCode;
    }

    public void setRetCode(String retCode) {
        this.retCode = retCode;
    }




}
