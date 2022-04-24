package model;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

public class ProductsDAO {

    private Connection conexionBD;

    public ProductsDAO(Connection conexionBD) {
        this.conexionBD = conexionBD;
    }

    public List<Product> getProductsList(){
        List<Product> productsList = new ArrayList<Product>();
        try(ResultSet result = conexionBD.createStatement().executeQuery("SELECT * from producto")){
            while(result.next()){               
                int productID = result.getInt("id");
                //Ver si el producto es un pack
                if(isPack(productID)){
                    Pack packTemp = createPackFromDB(productID);
                    productsList.add(packTemp);
                } else {
                    Product productTemp = createProductFromDB(productID);
                    productsList.add(productTemp);
                }
            }
        } catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return productsList;
    }


    //Funcion para obtener un producto de la base de datos.
    public Product findInDB(Integer id){
        if(id == null || id == 0){
            return null;
        }

        //Comprobar que el producto existe
        try (PreparedStatement stmt = conexionBD.prepareStatement("SELECT * FROM producto WHERE id = ?")) {
            stmt.setInt(1, id);
            ResultSet result = stmt.executeQuery();
            if(result.next()){
                int productID = result.getInt("id");
                //Ver si el producto es un pack
                if(isPack(productID)){
                    Pack tempPack = createPackFromDB(productID);
                    return tempPack;
                } else {
                    Product p = createProductFromDB(productID);
                    return p;
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
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
            packProductsString += product.getID() + ",";
        }
        return packProductsString;
    }


    //Funcion para guardar en la base de datos
    public boolean saveInDB(Object obj){
        try {
            String sql = "";
            PreparedStatement stmt = null;
            Product productTMP = (Product)obj;
            if(findInDB(productTMP.getID()) == null){

                if(obj instanceof Pack){
                    Pack p = (Pack)obj;
                    insertPack(p);
                }
                sql = "INSERT INTO producto VALUES(?,?,?,?,?,?)";
                stmt = conexionBD.prepareStatement(sql);
                int i = 1;
                stmt.setInt(i++, product.getID());
                stmt.setString(i++, product.getName());
                stmt.setFloat(i++, product.getPrice());
                stmt.setInt(i++, product.getStock());
                Date startDate = Date.valueOf(product.getCatalogStartDate());
                stmt.setDate(i++, startDate);
                Date endDate = Date.valueOf(product.getCatalogFinishDate());
                stmt.setDate(i++, endDate);
            } else {
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
            }
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
            stmt.setInt(i++, pack.getID());
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
                insertPackProductsList(pack.getID(),pack.getProductList());
            } else return false;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return false;
    }

    private boolean insertPackProductsList(int id, TreeSet<Product> productsList){
        String sql = "";
        PreparedStatement stmt = null;

        for(Product product : productsList){
            try {
                sql = "INSERT INTO productos_pack values(?, ?)";
                stmt = conexionBD.prepareStatement(sql);
                int i = 1;
                stmt.setInt(i++, id);
                stmt.setInt(i++, product.getID());
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }
    }


    //Funcion para borrar en la base de datos
    public boolean deleteInDB(Integer id){
        try {
            String sql = "";
            PreparedStatement stmt = null;
            if(findInDB(id)!= null){
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
