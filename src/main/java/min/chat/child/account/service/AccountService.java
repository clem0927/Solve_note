package min.chat.child.account.service;

import lombok.RequiredArgsConstructor;
import min.chat.child.account.dto.AccountDto;
import min.chat.child.account.entity.Account;
import min.chat.child.account.repository.AccountRepository;
import min.chat.child.grade.entity.Grade;
import min.chat.child.grade.repository.GradeRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;
    private final GradeRepository gradeRepository;

    /**
     * 일반 회원 가입 (USER)
     */
    @Transactional
    public void signUp(AccountDto dto) {

        if (accountRepository.findById(dto.getEmail()).isPresent()) {
            throw new IllegalArgumentException("이미 사용 중인 이메일입니다.");
        }

        Grade defaultGrade = gradeRepository.findByName("FREE")
                .orElseThrow(() -> new RuntimeException("기본 등급 없음"));

        Account account = Account.builder()
                .email(dto.getEmail())
                .password(passwordEncoder.encode(dto.getPassword()))
                .nickname(dto.getNickname())
                .role("USER")
                .provider("local")
                .grade(defaultGrade)
                .build();

        accountRepository.save(account);
    }

    /**
     * 비밀번호 변경
     */
    @Transactional
    public void changePassword(String email, String currentPw, String newPw) {
        Account account = accountRepository.findById(email)
                .orElseThrow(() -> new IllegalArgumentException("계정이 존재하지 않습니다."));

        // 현재 비밀번호 확인
        if (!passwordEncoder.matches(currentPw, account.getPassword())) {
            throw new IllegalArgumentException("현재 비밀번호가 일치하지 않습니다.");
        }

        account.setPassword(passwordEncoder.encode(newPw));
    }
    public void changeNickname(String email, String nickname) {

        Account account = accountRepository.findById(email)
                .orElseThrow(() -> new RuntimeException("사용자 없음"));

        account.setNickname(nickname);

        accountRepository.save(account);
    }
    /**
     * 회원 탈퇴
     */
    @Transactional
    public void deleteAccount(String email) {
        Account account = accountRepository.findById(email)
                .orElseThrow(() -> new IllegalArgumentException("계정이 존재하지 않습니다."));

        accountRepository.delete(account);
    }

    /**
     * 계정 조회
     */
    public Account getAccount(String email) {
        return accountRepository.findById(email)
                .orElseThrow(() -> new IllegalArgumentException("계정이 존재하지 않습니다."));
    }
    public PasswordEncoder getPasswordEncoder() {
        return this.passwordEncoder;
    }


}