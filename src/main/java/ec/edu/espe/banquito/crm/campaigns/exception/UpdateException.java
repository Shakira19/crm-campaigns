package ec.edu.espe.banquito.crm.campaigns.exception;

public class UpdateException extends Exception {

    private final String tableName;

    public UpdateException(String tableName, String message) {
        super(message);
        this.tableName = tableName;
    }

    public UpdateException(String tableName, String message, Throwable cause) {
        super(message, cause);
        this.tableName = tableName;
    }

    public String getTableName() {
        return tableName;
    }

}
