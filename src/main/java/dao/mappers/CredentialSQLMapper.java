package dao.mappers;

import dao.model.Credential;

import java.sql.ResultSet;
import java.sql.SQLException;

public class CredentialSQLMapper {
    public Credential map(ResultSet rs) throws SQLException {
        Credential credential = new Credential();
        credential.setUsername(rs.getString("username"));
        credential.setPassword(rs.getString("password"));
        return credential;
    }
}
