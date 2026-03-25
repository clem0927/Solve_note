package kr.solvenote.account.service;

import lombok.RequiredArgsConstructor;
import kr.solvenote.account.entity.Account;
import kr.solvenote.account.repository.AccountRepository;
import kr.solvenote.account.security.CustomUserDetails;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final AccountRepository accountRepository;

    @Override
    public UserDetails loadUserByUsername(String email) {

        Account account = accountRepository.findById(email)
                .orElseThrow(() -> new UsernameNotFoundException("계정 없음"));

        return new CustomUserDetails(account);
    }
}