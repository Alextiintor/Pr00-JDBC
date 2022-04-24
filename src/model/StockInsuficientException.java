package model;

public class StockInsuficientException extends Exception {

    public StockInsuficientException(){
        super();
    }

    public StockInsuficientException(String message){
        super(message);
    }
    
}
