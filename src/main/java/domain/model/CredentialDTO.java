package domain.model;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CredentialDTO {
    private String username;
    private String password;
}
