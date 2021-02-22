/*
 * Creation date: 22 feb. 2021
 * Company: ESPE
 * Project: Banco Banquito
 * Module: Banco Banquito - CRM
 */
package ec.edu.espe.banquito.crm.campaigns.exception;

/**
 *
 * @author Alan Quimbita
 */
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
