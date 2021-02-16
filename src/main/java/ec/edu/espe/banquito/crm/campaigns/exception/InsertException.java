/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.edu.espe.banquito.crm.campaigns.exception;

/**
 *
 * @author cofre
 */
public class InsertException extends Exception {
    private final String tableName;

    public InsertException(String tableName, String message) {
        super(message);
        this.tableName = tableName;
    }

    public InsertException(String tableName, String message, Throwable cause) {
        super(message, cause);
        this.tableName = tableName;
    }

    public String getTableName() {
        return tableName;
    }
}
