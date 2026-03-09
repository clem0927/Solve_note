package min.chat.child.account.controller;

import lombok.RequiredArgsConstructor;
import min.chat.child.account.dto.AccountDto;
import min.chat.child.account.entity.Account;
import min.chat.child.account.security.CustomUserDetails;
import min.chat.child.account.security.OAuthUserAdapter;
import min.chat.child.account.service.AccountService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.crypto.password.PasswordEncoder;
@RestController
@RequestMapping("/account")
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;

    // 회원가입
    @PostMapping("/signup")
    public ResponseEntity<String> signUp(@RequestBody AccountDto dto) {
        accountService.signUp(dto);
        return ResponseEntity.ok("회원가입 완료");
    }

    // 비밀번호 변경 (로그인된 사용자 기준으로 바꾸는게 더 안전)
    @PutMapping("/password")
    public ResponseEntity<String> changePassword(@RequestBody AccountDto dto) {
        accountService.changePassword(dto.getEmail(), dto.getCurrentPassword(), dto.getNewPassword());
        return ResponseEntity.ok("비밀번호 변경 완료");
    }
    // 닉네임 변경
    @PutMapping("/nickname")
    public ResponseEntity<String> changeNickname(
            @AuthenticationPrincipal CustomUserDetails user,
            @RequestBody AccountDto dto
    ) {

        if(user == null){
            return ResponseEntity.status(401).build();
        }

        accountService.changeNickname(user.getEmail(), dto.getNickname());

        return ResponseEntity.ok("닉네임 변경 완료");
    }

    // 회원 탈퇴
    @DeleteMapping("/{email}")
    public ResponseEntity<String> deleteAccount(@PathVariable String email) {
        accountService.deleteAccount(email);
        return ResponseEntity.ok("회원 탈퇴 완료");
    }

    @GetMapping("/me")
    public ResponseEntity<?> me(Authentication auth) {

        Object principal = auth.getPrincipal();

        CustomUserDetails user;

        if (principal instanceof OAuthUserAdapter oauth) {
            user = oauth.getUserDetails();
        }
        else if (principal instanceof CustomUserDetails local) {
            user = local;
        }
        else {
            return ResponseEntity.status(401).build();
        }

        return ResponseEntity.ok(
                new AccountDto(
                        user.getEmail(),
                        user.getNickname(),
                        user.getRole(),
                        user.getGrade().getName()
                )
        );
    }
}