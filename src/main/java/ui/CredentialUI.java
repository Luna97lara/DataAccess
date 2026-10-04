package ui;

import domain.model.CredentialDTO;
import domain.service.CredentialService;
import jakarta.inject.Inject;

public class CredentialUI {
    public final CredentialService credentialService;
    @Inject
    public CredentialUI(CredentialService credentialService) {
        this.credentialService=credentialService;
    }

    public boolean login(CredentialDTO credentialDTO) {
        return credentialService.login(credentialDTO);
    }

}
