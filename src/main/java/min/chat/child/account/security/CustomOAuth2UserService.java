package min.chat.child.account.security;

import lombok.RequiredArgsConstructor;
import min.chat.child.account.entity.Account;
import min.chat.child.account.repository.AccountRepository;
import min.chat.child.account.security.CustomUserDetails;
import min.chat.child.grade.entity.Grade;
import min.chat.child.grade.repository.GradeRepository;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {


    private final AccountRepository accountRepository;
    private final GradeRepository gradeRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest request) {


        OAuth2User oauthUser = super.loadUser(request);

        Map<String, Object> attr = oauthUser.getAttributes();

        String email = (String) attr.get("email");
        String name = (String) attr.get("name");

        if(email == null){
            throw new RuntimeException("Google email not provided");
        }

        Account account = accountRepository.findById(email)
                .orElseGet(() -> {

                    Grade grade = gradeRepository.findByName("FREE").orElse(null);

                    Account newUser = Account.builder()
                            .email(email)
                            .nickname(name)
                            .provider("google")
                            .role("USER")
                            .grade(grade)
                            .build();

                    return accountRepository.save(newUser);
                });

        CustomUserDetails userDetails = new CustomUserDetails(account);

        return new OAuthUserAdapter(userDetails, attr);
    }
}