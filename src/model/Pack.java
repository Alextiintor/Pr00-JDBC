package model;

import java.time.LocalDate;
import java.util.TreeSet;
import javax.persistence.*;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public final class Pack extends Product{
	
	private static final long serialVersionUID = 12323151345l;
	@OneToMany 
	@ElementCollection
	@CollectionTable(
		name="pack_productes",
		joinColumns= @JoinColumn(name="idpack"))
	@Column(name="idproduct")
    private TreeSet<Product> productList = new TreeSet<Product>(); 
    private int discount;
    
    public Pack() {};
    
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
