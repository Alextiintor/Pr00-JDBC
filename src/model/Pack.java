package model;

import java.time.LocalDate;
import java.util.TreeSet;

public final class Pack extends Product{

    //private ArrayList<Integer> productList = new ArrayList<Integer>();
    private TreeSet<Product> productList = new TreeSet<Product>(); 
    private int discount;

    public Pack(int idProduct, String name, float price, int stock, TreeSet<Product> productList, int discount, LocalDate catalogStartDate , LocalDate catalogFinishDate) {
        super(idProduct, name, price, stock, catalogStartDate, catalogFinishDate);
        this.productList = productList;
        this.discount = discount;
    }

    public int getDiscount() {
        return discount;
    }

    public void setDiscount(int discount) {
        this.discount = discount;
    }

    public void addProduct(Product p){
        this.productList.add(p);
    }

    public void removeProduct(int num){
        this.productList.remove(num);
    }

    @Override
    public String toString() {
        return super.toString() + " Pack [discount=" + discount + ", productList=" + productList + "]";
    }

    public TreeSet<Product> getProductList(){
        return this.productList;
    }

    @Override
    public boolean equals(Object obj){
        if (obj==null || obj.getClass() != this.getClass()) {
            return false;
        }

        Pack pack = (Pack) obj;
        
        if (!this.productList.equals(pack.productList)) {
            return false;
        }

        return true;
    }
}
