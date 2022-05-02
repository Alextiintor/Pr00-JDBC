package model;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

import javax.persistence.EntityManager;
import javax.persistence.Query;

public class ProductsDAO {

    private EntityManager em;

    public ProductsDAO(EntityManager em) {
        this.em = em;
    }

    public List<Product> getProductsList(){
        List<Product> productsList = new ArrayList<Product>();
        
        Query q = em.createNativeQuery("Select * from products", Product.class);
        @SuppressWarnings("unchecked")
		List<Product> results = q.getResultList();
        for(Product p : results){
        	while(p!=null) {
        		int productID = p.getId();
                //Ver si el producto es un pack
                if(isPack(productID)){
                    Pack packTemp = createPackFromDB(productID);
                    productsList.add(packTemp);
                } else {
                    Product productTemp = createProductFromDB(productID);
                    productsList.add(productTemp);
                }
        	}

        }
        return productsList;
    }


    //Funcion para obtener un producto de la base de datos.
    public Product findInDB(Integer id){
        if(id == null || id == 0){
            return null;
        }
    	Product p = em.find(Product.class, id);
    	if(p!=null){
            int productID = p.getId();
            //Ver si el producto es un pack
            if(isPack(productID)){
                Pack tempPack = createPackFromDB(productID);
                return tempPack;
            } else {
                Product tempProduct = createProductFromDB(productID);
                return tempProduct;
            }
        }

        return null;
    }

    //Funcion que comprueba si es un pack
    private boolean isPack(int id){
    	
        try (PreparedStatement stmtPack = conexionBD.prepareStatement("SELECT * from pack where id = ?")){
            stmtPack.setInt(1, id);
            ResultSet resultPack = stmtPack.executeQuery();
            
            return resultPack.next();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return false;
    }

    //Funcion para crear un producto
    private Product createProductFromDB(int id){
        try (PreparedStatement stmt = conexionBD.prepareStatement("SELECT * FROM producto where id = ?")){
            stmt.setInt(1, id);
            ResultSet result = stmt.executeQuery();
            if(result.next()){
                return new Product(
                    result.getInt("id"), 
                    result.getString("nombre"), 
                    result.getFloat("precio"), 
                    result.getInt("stock"),
                    result.getDate("fecha_inicio").toLocalDate(), 
                    result.getDate("fecha_fin").toLocalDate()
                );
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    //Funcion para crear un pack
    private Pack createPackFromDB(int id){
        try (PreparedStatement stmt = conexionBD.prepareStatement("SELECT * FROM pack where id = ?")){
            stmt.setInt(1, id);
            ResultSet result = stmt.executeQuery();
            if(result.next()){
                TreeSet<Product> packProductList = getPackProductList(result.getInt("id"));
                return new Pack(
                    result.getInt("id"), 
                    result.getString("nombre"), 
                    result.getFloat("precio"), 
                    result.getInt("stock"), 
                    packProductList, 
                    result.getInt("dto"), 
                    result.getDate("fecha_inicio").toLocalDate(), 
                    result.getDate("fecha_fin").toLocalDate()
                );
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    //Funcion para obtener las lista de productos del pack
    public TreeSet<Product> getPackProductList(int id){
        TreeSet<Product> packProductList = new TreeSet<Product>();
        try (PreparedStatement stmt = conexionBD.prepareStatement("SELECT * from productos_pack where id_pack = ?")){
            stmt.setInt(1, id);
            ResultSet result = stmt.executeQuery();
            while(result.next()){
                Product productTemp = findInDB(result.getInt("id_producto"));
                packProductList.add(productTemp);
            }
            return packProductList;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    public String getPackProductString(int id){
        TreeSet<Product> packProductList = getPackProductList(id);
        String packProductsString = "";
        for(Product product : packProductList){
            packProductsString += product.getId() + ",";
        }
        return packProductsString;
    }


    //Funcion para guardar en la base de datos
    public boolean saveInDB(Object obj){
        Product productTMP = (Product)obj;

        if(findInDB(productTMP.getId()) == null){
            if(obj instanceof Pack){
                Pack p = (Pack)obj;
                return insertPack(p);
            } else {
                Product p = (Product)obj;
                return insertProduct(p);
            }

        } else {
            if(isPack(productTMP.getId())){
                Pack pack = (Pack)obj;
                return updatePack(pack);
            } else {
                Product p = (Product)obj;
                return updateProduct(p);
            }
        }
    }

    private boolean insertProduct(Product product){
        String sql = "";
        PreparedStatement stmt = null;

        try {
            sql = "INSERT INTO producto VALUES(?,?,?,?,?,?)";
            stmt = conexionBD.prepareStatement(sql);
            int i = 1;
            stmt.setInt(i++, product.getId());
            stmt.setString(i++, product.getName());
            stmt.setFloat(i++, product.getPrice());
            stmt.setInt(i++, product.getStock());
            Date startDate = Date.valueOf(product.getCatalogStartDate());
            stmt.setDate(i++, startDate);
            Date endDate = Date.valueOf(product.getCatalogFinishDate());
            stmt.setDate(i++, endDate);

            int rows = stmt.executeUpdate();
            if (rows == 1) return true;
            else return false;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return false;

    }

    private boolean insertPack(Pack pack){
        String sql = "";
        PreparedStatement stmt = null;
        try {
            sql = "INSERT INTO pack VALUES(?,?,?,?,?,?,?)";
            stmt = conexionBD.prepareStatement(sql);
            int i = 1;
            stmt.setInt(i++, pack.getId());
            stmt.setString(i++, pack.getName());
            stmt.setFloat(i++, pack.getPrice());
            stmt.setInt(i++, pack.getStock());
            Date startDate = Date.valueOf(pack.getCatalogStartDate());
            stmt.setDate(i++, startDate);
            Date endDate = Date.valueOf(pack.getCatalogFinishDate());
            stmt.setDate(i++, endDate);
            stmt.setInt(i++, pack.getDiscount());

            int rows = stmt.executeUpdate();
            if (rows == 1){
                return insertPackProductsList(pack.getId(),pack.getProductList());
            } else return false;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return false;
    }

    private boolean insertPackProductsList(int id, TreeSet<Product> productsList){
        String sql = "";
        PreparedStatement stmt = null;
        int counter = 0;

        for(Product product : productsList){
            try {

                if(this.findInDB(product.getId())!=null){
                    sql = "INSERT INTO productos_pack values(?, ?)";
                    stmt = conexionBD.prepareStatement(sql);
                    int i = 1;
                    stmt.setInt(i++, id);
                    stmt.setInt(i++, product.getId());
    
                    int rows = stmt.executeUpdate();
                    if(rows==1){
                        counter++;
                    }
                } else {
                    System.out.println("No existe el producto: " + product.getId());
                }

            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }

        }

        return counter == productsList.size();
    }

    private boolean updateProduct(Product product){
        String sql = "";
        PreparedStatement stmt = null;

        try {
            sql = "UPDATE producto set nombre=?, precio=?, stock=?, fecha_inicio=?, fecha_fin=? WHERE id = ?";
            stmt = conexionBD.prepareStatement(sql);
            int i = 1;
            stmt.setString(i++, product.getName());
            stmt.setFloat(i++, product.getPrice());
            stmt.setInt(i++, product.getStock());
            Date startDate = Date.valueOf(product.getCatalogStartDate());
            stmt.setDate(i++, startDate);
            Date endDate = Date.valueOf(product.getCatalogFinishDate());
            stmt.setDate(i++, endDate);
            stmt.setInt(i++, product.getId());

            int rows = stmt.executeUpdate();
            if (rows == 1) return true;
            else return false;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return false;
    }

    private boolean updatePack(Pack pack){
        String sql = "";
        PreparedStatement stmt = null;

        try {
            sql = "UPDATE pack set nombre=?, precio=?, stock=?, fecha_inicio=?, fecha_fin=?, dto=? WHERE id = ?";
            stmt = conexionBD.prepareStatement(sql);
            int i = 1;
            stmt.setString(i++, pack.getName());
            stmt.setFloat(i++, pack.getPrice());
            stmt.setInt(i++, pack.getStock());
            Date startDate = Date.valueOf(pack.getCatalogStartDate());
            stmt.setDate(i++, startDate);
            Date endDate = Date.valueOf(pack.getCatalogFinishDate());
            stmt.setDate(i++, endDate);
            stmt.setInt(i++, pack.getDiscount());
            stmt.setInt(i++, pack.getId());

            int rows = stmt.executeUpdate();
            if (rows == 1) {
                if(deletePackProductList(pack.getId())){
                    return updatePackProductList(pack.getId(), pack.getProductList());
                } else {
                    return false;
                }
            } else {
                return false;
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return false;
    }

    private boolean updatePackProductList(int id,TreeSet<Product> productsList){
        String sql = "";
        PreparedStatement stmt = null;
        int counter = 0;

        try {
            for(Product product : productsList){
                if(findInDB(product.getId())!=null){
                    sql = "INSERT into productos_pack values(?,?)";
                    stmt = conexionBD.prepareStatement(sql);
                    int i = 1;
                    stmt.setInt(i++, id);
                    stmt.setInt(i++, product.getId());
                    counter++;
                    stmt.executeUpdate();
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return counter==productsList.size();
    }

    private boolean deletePackProductList(int id){
        String sql = "";
        PreparedStatement stmt = null;

        try {
            sql = "DELETE FROM productos_pack where id_pack=?";
            stmt = conexionBD.prepareStatement(sql);
            int i = 1;
            stmt.setInt(i++, id);

            int rows = stmt.executeUpdate();
            if (rows >= 1) return true;
            else return false;
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        return false;
    }

    //Funcion para borrar en la base de datos
    public boolean deleteInDB(Integer id){
        try {
            String sql = "";
            PreparedStatement stmt = null;
            Object obj = findInDB(id);
            if(obj!= null){
                if(obj instanceof Pack){
                    Pack p = (Pack)obj;
                    if(deletePackProductList(p.getId())){
                        sql = "DELETE FROM pack where id = ?";
                        stmt = conexionBD.prepareStatement(sql);

                        int i=1;
                        stmt.setInt(i++, p.getId());
                        int rows = stmt.executeUpdate();
                        if (rows == 1) return true;
                        else return false;
                    } 
                }
                sql = "DELETE FROM producto WHERE id = ?";
                stmt = conexionBD.prepareStatement(sql);
                int i = 1;
                stmt.setInt(i++, id);
            }
            int rows = stmt.executeUpdate();
            if (rows == 1) return true;
			else return false;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return false;
    }
}
