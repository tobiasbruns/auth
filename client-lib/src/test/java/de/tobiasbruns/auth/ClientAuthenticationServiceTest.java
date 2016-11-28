package de.tobiasbruns.auth;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.SignatureException;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import static de.tobiasbruns.auth.TestToken.*;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes=TestConfiguration.class)
@RunWith(SpringRunner.class)
public class ClientAuthenticationServiceTest {

    @Autowired
    private ClientAuthenticationService service;

    @Test
    public void validToken() {
        TbAuthUser user = service.getUserFromToken(VALID_TOKEN.getToken());
        assertThat(user).isNotNull().hasFieldOrPropertyWithValue("username","theo");
        assertThat(user.getAuthorities()).hasSize(1).containsOnlyOnce(new TbAuthAuthority("ROLE_TEST_USER"));
    }


    @Test(expected = ExpiredJwtException.class)
    public void expiredToken() {
        service.getUserFromToken(EXPIRED_TOKEN);
    }

    @Test(expected = SignatureException.class)
    public void manipulatedToken() {
        service.getUserFromToken(MANIPULATED_TOKEN);
    }

    @Ignore("needs a running token service")
    @Test
    public void refreshToken() {
        TbAuthToken newToken = service.refreshToken(VALID_TOKEN.getToken());

        assertThat(newToken).isNotNull().isNotEqualTo(VALID_TOKEN);
    }
}
