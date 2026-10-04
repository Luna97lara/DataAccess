package domain.service;

import dao.Repositories.JDBC.JDBCCredentialRepository;
import dao.model.Credential;
import domain.model.CredentialDTO;
import jakarta.inject.Inject;


public class CredentialService {
    private final JDBCCredentialRepository credentialRepository;

    @Inject
    public CredentialService(JDBCCredentialRepository credentialRepository) { this.credentialRepository = credentialRepository; }

    public boolean login(CredentialDTO credentialDTO) {
        Credential credential=credentialRepository.login(credentialDTO.getUsername());
        return credentialDTO.getPassword().equals(credential.getPassword());
    }
}
