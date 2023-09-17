package db.migration;

import org.flywaydb.core.api.migration.BaseJavaMigration;
import org.flywaydb.core.api.migration.Context;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 * Encode user password.
 */
public class V1_7_1__update_user_password extends BaseJavaMigration {

    @Override
    public void migrate(Context context) throws Exception {

        final PasswordEncoder encoder = new BCryptPasswordEncoder();
        ResultSet users = null;
        PreparedStatement getUsersStmt = null;
        PreparedStatement encodePasswordStmt = null;
        try {
            Connection connection = context.getConnection();
            getUsersStmt = connection.prepareStatement("SELECT user_name, password FROM users");
            encodePasswordStmt = connection.prepareStatement("UPDATE users SET password=? WHERE user_name=?");

            users = getUsersStmt.executeQuery();
            while (users.next()) {
                final String userName = users.getString(1);
                final String password = users.getString(2);
                encodePasswordStmt.setString(1, encoder.encode(password));
                encodePasswordStmt.setString(2, userName);
                encodePasswordStmt.addBatch();
            }
            encodePasswordStmt.executeBatch();
            connection.commit();
        } catch(Exception e) {
            e.printStackTrace();
        } finally {
            if(users != null && !users.isClosed()) {
                users.close();
            }
            if(getUsersStmt != null && !getUsersStmt.isClosed()) {
                getUsersStmt.close();
            }
            if(encodePasswordStmt != null && !encodePasswordStmt.isClosed()) {
                encodePasswordStmt.close();
            }
        }
    }

}
