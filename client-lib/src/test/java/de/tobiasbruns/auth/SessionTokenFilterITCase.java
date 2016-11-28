package de.tobiasbruns.auth;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.junit4.SpringRunner;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static de.tobiasbruns.auth.TestToken.VALID_TOKEN;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@SpringBootTest(classes = TestConfiguration.class)
@RunWith(SpringRunner.class)
public class SessionTokenFilterITCase {

    @Autowired
    private SessionTokenFilter filter;
    @MockBean
    private RemoteTokenService remoteTokenService;

    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpServletResponse response;
    @Mock
    private FilterChain chain;


    @Before
    public void initTest() {
        when(request.getHeader(SessionTokenFilter.TOKEN_HEADER)).thenReturn(VALID_TOKEN.getToken());
        when(response.getStatus()).thenReturn(200);

        when(remoteTokenService.refreshToken(anyString())).thenReturn(TestToken.VALID_TOKEN);
    }

    @Test
    public void validToken() throws IOException, ServletException {

        filter.doFilter(request, response, chain);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        assertThat(authentication).isNotNull();
        assertThat(authentication.isAuthenticated()).isTrue();
        assertThat((TbAuthUser) authentication.getPrincipal()).hasFieldOrPropertyWithValue("username", "theo");
    }


}
