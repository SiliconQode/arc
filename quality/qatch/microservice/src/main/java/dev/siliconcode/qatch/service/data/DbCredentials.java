package dev.siliconcode.qatch.service.data;

import lombok.NonNull;

/**
 * A record representing all the information necessary to open a connection to a database using JDBC.
 *
 * @param type The type of database to connect to (i.e., mysql, postgres, sqlite, etc), cannot be null
 * @param driver The fully qualified driver class name, cannot be null
 * @param url The url to be used to connect to the database, cannot be null
 * @param user The username to be used to connect to the database (can be null, if using sqlite or derby), cannot be null
 * @param pass The password to be used ot connect to th edatabase (can be null, if using sqlite or derby), cannot be null
 *
 * @author Isaac Griffith
 * @version 2.0.0
 */
public record DbCredentials(@NonNull String type, @NonNull String driver, @NonNull String url, @NonNull String user, @NonNull String pass) {
}
