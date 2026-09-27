package domain.service;

import dao.Interfaces.CredentialRepository;
import dao.Repositories.JDBCCredentialRepository;
import dao.model.Credential;
import domain.model.CredentialDTO;
import jakarta.inject.Inject;
import lombok.*;

import java.util.List;


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
