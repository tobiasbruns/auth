/*
 * Copyright 2016 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package de.tobiasbruns.auth;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.AuthenticationUserDetailsService;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationProvider;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.web.client.RestTemplate;


@Configuration
@ComponentScan(basePackages = "de.tobiasbruns.auth")
//@Order(80)
public class SecurityFilterChainConfiguration {
//        extends WebSecurityConfigurerAdapter {

//    @Autowired
//    private SessionTokenFilter sessionTokenFilter;
//    @Autowired
//    private UserDetailsService userDetailsService;
//    @Autowired
//    private DaoAuthenticationProvider daoAuthenticationProvider;

//    @Override
//    protected void configure(HttpSecurity http) throws Exception {
//
//        http.csrf().disable()
//                .authorizeRequests()
//                .antMatchers("/authenticate").permitAll()
////                .antMatchers("/users").permitAll();
//                .mvcMatchers(HttpMethod.GET, "/users/{id}").authenticated()
//                .antMatchers("/users").hasRole("ADMIN");
//
//        http.addFilterBefore(sessionTokenFilter, RequestHeaderAuthenticationFilter.class);
//    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    public PreAuthenticatedAuthenticationProvider preAuthProvider() {
        PreAuthenticatedAuthenticationProvider provider = new PreAuthenticatedAuthenticationProvider();
        provider.setPreAuthenticatedUserDetailsService(getAuthDetailsService());
        return provider;
    }

    @Bean
    public AuthenticationUserDetailsService<PreAuthenticatedAuthenticationToken> getAuthDetailsService() {
//        return new UserDetailsByNameServiceWrapper<>(userDetailsService);
        return new TokenAuthenticationService();
    }


   /* @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(preAuthProvider());
        auth.authenticationProvider(daoAuthenticationProvider);
    }*/

}
