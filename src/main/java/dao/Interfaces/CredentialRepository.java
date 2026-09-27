package dao.Interfaces;

import dao.model.Credential;

public interface CredentialRepository {
    Credential login(String username);
}
