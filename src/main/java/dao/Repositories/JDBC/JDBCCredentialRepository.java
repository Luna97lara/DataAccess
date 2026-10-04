package dao.Repositories.JDBC;

import dao.Repositories.Interfaces.CredentialRepository;
import dao.Repositories.common.SQLQuery;
import dao.mappers.CredentialSQLMapper;
import dao.model.Credential;
import jakarta.inject.Inject;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class JDBCCredentialRepository implements CredentialRepository {
    private final DBConnection db;
    private final CredentialSQLMapper credentialSQLMapper;

    @Inject
    public JDBCCredentialRepository(DBConnection db, CredentialSQLMapper credentialSQLMapper) {
        this.db = db;
        this.credentialSQLMapper = credentialSQLMapper;
    }

    @Override
    public Credential login(String username) {
        Credential credential = new Credential();
        try {
            Connection c = db.getConnection();
            PreparedStatement preparedStatement = c.prepareStatement(SQLQuery.CREDENTIAL_QUERY);
            preparedStatement.setString(1, username);
            ResultSet resultSet = preparedStatement.executeQuery();
            resultSet.next();
           credential=credentialSQLMapper.map(resultSet);

        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
        return credential;
    }
}

