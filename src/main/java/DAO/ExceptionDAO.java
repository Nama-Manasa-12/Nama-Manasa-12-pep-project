package DAO;

public class ExceptionDAO extends RuntimeException{
    public ExceptionDAO(){
        super();
}
public ExceptionDAO(Throwable cause){
    super(cause);
    
}

}
