package domain.service;

import dao.Repositories.JDBC.JDBCCredentialRepository;
import dao.model.Credential;
import jakarta.inject.Inject;


public class CredentialService {
    private final JDBCCredentialRepository credentialRepository;

    @Inject
    public CredentialService(JDBCCredentialRepository credentialRepository) { this.credentialRepository = credentialRepository; }

    public boolean login(String username, String password) {
        Credential credentialDTO=credentialRepository.login(username);
        if(credentialDTO.getPassword().equals(password)) {
            return true;
        }
        return false;
    }
}
