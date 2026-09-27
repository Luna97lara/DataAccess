package ui;

import domain.service.CredentialService;
import jakarta.inject.Inject;

public class CredentialUI {
    public final CredentialService credentialService;
    @Inject
    public CredentialUI(CredentialService credentialService) {
        this.credentialService=credentialService;
    }

    public boolean login(String username, String password){
        return credentialService.login(username, password);
    }

}
