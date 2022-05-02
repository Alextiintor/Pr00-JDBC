package model;

import java.io.Serializable;
import java.time.LocalDate;
import javax.persistence.*;


@Entity
public class Product implements Serializable, Comparable<Product> {
    private static final long serialVersionUID = 12323151345l;
    @Id
    private int id;
    private String name;
    private float price;
    private Integer stock;
    private LocalDate catalogStartDate;
    private LocalDate catalogFinishDate;
    
    public Product(){};
    
    public Product(int idProduct, String name, float price, int stock, LocalDate catalogStartDate, LocalDate catalogFinishDate) {
        this.id = idProduct;
        this.name = name;
        this.price = price;
        this.stock = stock;
        this.catalogStartDate = catalogStartDate;
        this.catalogFinishDate = catalogFinishDate;
    }
    
    public int getId() {
        return id;
    }

    public void setId(int idProduct) {
        this.id = idProduct;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public LocalDate getCatalogStartDate(){
        return this.catalogStartDate;
    }

    public void setCatalogStartDate(LocalDate date){
        this.catalogStartDate = date;
    }

    public LocalDate getCatalogFinishDate(){
        return this.catalogFinishDate;
    }

    public void setCatalogFinishDate(LocalDate date){
        this.catalogFinishDate = date;
    }

    @Override
    public String toString() {
        return "Product [idProduct=" + id + ", name=" + name + ", price=" + price + ", stock=" + stock + ", catalogFinishDate=" + catalogFinishDate + ", catalogStartDate=" + catalogStartDate +"]";
    }

    public void putStock(int stock){
        this.stock = this.stock+stock;
    }

    public void takeStock(int stock) throws StockInsuficientException{
        if(stock>this.stock){
            throw new StockInsuficientException("No hay suficiente Stock");
        } else {
            this.stock = this.stock-stock;
        }
    }

    public void showDiscontinued(LocalDate date){
        
    }

    @Override
    public boolean equals(Object obj){
        if(obj == null || obj.getClass() != this.getClass()){
            return false;
        }

        Product product = (Product) obj;
        
        if(!this.name.equals(product.name)) {
            return false;
        }

        return true;
    }

    @Override
    public int compareTo(Product o) {
        if(this.id > o.id){
            return 1;
        } else if (this.id < o.id){
            return -1;
        } else {
            return 0;
        }
    }


}
