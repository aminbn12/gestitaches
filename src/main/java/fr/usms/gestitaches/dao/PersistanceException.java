package fr.usms.gestitaches.dao;

/**
 * Exception levée lorsqu'une erreur d'accès à la base de données survient.
 */
public class PersistanceException extends RuntimeException {

    public PersistanceException(String message, Throwable cause) {
        super(message, cause);
    }
}
