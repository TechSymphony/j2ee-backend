package com.tech_symfony.auth_server.config;

import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.MediaType;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.core.oidc.OidcScopes;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.server.authorization.client.InMemoryRegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configuration.OAuth2AuthorizationServerConfiguration;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configurers.OAuth2AuthorizationServerConfigurer;
import org.springframework.security.oauth2.server.authorization.settings.AuthorizationServerSettings;
import org.springframework.security.oauth2.server.authorization.settings.ClientSettings;
import org.springframework.security.oauth2.server.authorization.settings.TokenSettings;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.security.web.util.matcher.MediaTypeRequestMatcher;
import org.springframework.util.StringUtils;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.time.Duration;
import java.util.UUID;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

	@Value("${BASE_URL:}")
	String BASE_URL;
	@Value("${FRONTEND_URL:}")
	String FRONTEND_URL;

	public String getFRONTEND_URL() {
		if (StringUtils.isEmpty(FRONTEND_URL)) {
			FRONTEND_URL = "http://localhost:3000";
		}
		return FRONTEND_URL;
	}

	@Bean
	@Order(1)
	public SecurityFilterChain authorizationServerSecurityFilterChain(HttpSecurity http)
			throws Exception {
		OAuth2AuthorizationServerConfiguration.applyDefaultSecurity(http);
		http.getConfigurer(OAuth2AuthorizationServerConfigurer.class)
				.oidc(Customizer.withDefaults());	// Enable OpenID Connect 1.0
		http
				// Redirect to the login page when not authenticated from the
				// authorization endpoint
				.exceptionHandling((exceptions) -> exceptions
						.defaultAuthenticationEntryPointFor(
								new LoginUrlAuthenticationEntryPoint("/login"),
								new MediaTypeRequestMatcher(MediaType.TEXT_HTML)
						)
				)
				// Accept access tokens for User Info and/or Client Registration
				.oauth2ResourceServer((resourceServer) -> resourceServer
						.jwt(Customizer.withDefaults()));

		return http.cors(Customizer.withDefaults()).build();
	}

	@Bean
	@Order(2)
	public SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http)
			throws Exception {
		http
				.authorizeHttpRequests((authorize) -> authorize
						.requestMatchers(
								"/.well-known/openid-configuration",
								"/login",
								"/error",
								"/webjars/**",
								"/images/**",
								"/css/**",
								"/assests/**",
								"/actuator/**",
								"/favicon.ico")
						.permitAll()
						.anyRequest().authenticated()
				)
				// Form login handles the redirect to the login page from the
				// authorization server filter chain
//				.oauth2Login(oauth -> oauth.loginPage("/login"))
				.formLogin(formLogin-> formLogin.loginPage("/login").permitAll() );

		return http.cors(Customizer.withDefaults()).build();
	}

	@Bean
	public CorsConfigurationSource corsConfigurationSource() {
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		CorsConfiguration config = new CorsConfiguration();
		config.addAllowedHeader("*");
		config.addAllowedMethod("*");

		config.addAllowedOrigin(getFRONTEND_URL());
		config.setAllowCredentials(true);
		source.registerCorsConfiguration("/**", config);
		return source;
	}

	@Bean
	public UserDetailsService userDetailsService() {
		UserDetails userDetails = User.withDefaultPasswordEncoder()
				.username("user")
				.password("password")
				.roles("USER")
				.build();

		return new InMemoryUserDetailsManager(userDetails);
	}

	@Bean
	public RegisteredClientRepository registeredClientRepository() {
		RegisteredClient publicClient = RegisteredClient.withId(UUID.randomUUID().toString())
				.clientId("public-client")
				.clientAuthenticationMethod(ClientAuthenticationMethod.NONE)
				.authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
				.redirectUri("https://oauth.pstmn.io/v1/browser-callback")
				.redirectUri("https://oauth.pstmn.io/v1/callback")
				.redirectUri("http://127.0.0.1:8080/login/oauth2/code/myoauth2")
				.redirectUri(BASE_URL + "/swagger-ui/oauth2-redirect.html")
				// for frontend
				.redirectUri(getFRONTEND_URL()+"/callback")
				.postLogoutRedirectUri(getFRONTEND_URL())
				.scope(OidcScopes.OPENID)
				.scope(OidcScopes.PROFILE)
				.tokenSettings(TokenSettings.builder()
						.accessTokenTimeToLive(Duration.ofDays(1000))
						.refreshTokenTimeToLive(Duration.ofDays(7))
						.build())
				.clientSettings(ClientSettings.builder()
						.requireAuthorizationConsent(false)
						.requireProofKey(true)
						.build()
				)
				.build();

		return new InMemoryRegisteredClientRepository(publicClient);
	}


	@Bean
	public JWKSource<SecurityContext> jwkSource(
			@Value("${jwt.key.id}") String id,
			@Value("${jwt.key.private}") RSAPrivateKey privateKey,
			@Value("${jwt.key.public}") RSAPublicKey publicKey) {
		var rsa = new RSAKey.Builder(publicKey)
				.privateKey(privateKey)
				.keyID(id)
				.build();
		var jwk = new JWKSet(rsa);
		return new ImmutableJWKSet<>(jwk);
	}


	@Bean
	public JwtDecoder jwtDecoder(JWKSource<SecurityContext> jwkSource) {
		return OAuth2AuthorizationServerConfiguration.jwtDecoder(jwkSource);
	}

	@Bean
	public AuthorizationServerSettings authorizationServerSettings() {
		return AuthorizationServerSettings.builder().build();
	}

}