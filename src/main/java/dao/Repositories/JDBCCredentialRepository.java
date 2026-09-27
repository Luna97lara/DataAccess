package dao.Repositories;

import dao.Interfaces.CredentialRepository;
import dao.model.Credential;
import domain.model.CredentialDTO;

public class JDBCCredentialRepository implements CredentialRepository {

    @Override
    public Credential login(String username) {
        return null;
    }
}
