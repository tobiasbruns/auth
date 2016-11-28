package de.tobiasbruns.auth;


import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;

/**
 * Created by Tobias on 13.09.2016.
 */
@RunWith(MockitoJUnitRunner.class)
public class SessionTokenFilterTest {

    @InjectMocks
    private SessionTokenFilter filter;
    @Mock
    private ClientAuthenticationService authService;

    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpServletResponse response;
    @Mock
    private FilterChain chain;

    private TbAuthUser mockUser = mock(TbAuthUser.class);

    @Before
    public void initTest() {
        when(request.getHeader(SessionTokenFilter.TOKEN_HEADER)).thenReturn("token");
        when(authService.getUserFromToken(anyString())).thenReturn(mockUser);
        when(mockUser.getUsername()).thenReturn("theo");
        when(authService.refreshToken(anyString())).thenReturn(new TbAuthToken("refreshed token"));
    }

    @Test
    public void missingToken() throws IOException, ServletException {
        when(request.getHeader(SessionTokenFilter.TOKEN_HEADER)).thenReturn(null);

        assertThat(filter.getPreAuthenticatedPrincipal(request)).isNull();
    }

    @Test
    public void validTokenPrincipal() {

        assertThat(filter.getPreAuthenticatedPrincipal(request)).isEqualTo("theo");

        verify(authService).getUserFromToken("token");
    }

    @Test
    public void validTokenCredencial() {
        assertThat(filter.getPreAuthenticatedCredentials(request)).isEqualTo("token");
    }

    @Test
    public void refreshToken() throws IOException, ServletException {
        when(response.getStatus()).thenReturn(200);
        filter.refreshToken(request, response);

        verify(response).setHeader(SessionTokenFilter.TOKEN_HEADER, "refreshed token");
    }

    @Test
    public void noNewTokenInFailedRequest() throws IOException, ServletException {
        when(response.getStatus()).thenReturn(404);

        filter.refreshToken(request, response);

        verify(response, never()).setHeader(eq(SessionTokenFilter.TOKEN_HEADER), anyString());
    }
}