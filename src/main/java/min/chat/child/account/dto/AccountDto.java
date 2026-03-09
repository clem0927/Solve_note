package min.chat.child.account.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AccountDto {

    private String email;
    private String password;
    private String nickname;

    private String role;
    private String grade;

    private String currentPassword;
    private String newPassword;

    public AccountDto(String email, String nickname, String role, String grade) {
        this.email = email;
        this.nickname = nickname;
        this.role = role;
        this.grade = grade;
    }
}