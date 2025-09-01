package com.hp.gstreviewfeedbackapp.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.session.SessionInformationExpiredStrategy;

import com.hp.gstreviewfeedbackapp.service.impl.CustomAuthenticationProvider;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {
	@Bean
	public static PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Autowired
	private CustomAuthenticationProvider authProvider;

	@Bean
	public AuthenticationManager authManager(HttpSecurity http) throws Exception {
		AuthenticationManagerBuilder authenticationManagerBuilder = http
				.getSharedObject(AuthenticationManagerBuilder.class);
		authenticationManagerBuilder.authenticationProvider(authProvider);
		return authenticationManagerBuilder.build();
	}

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http.authorizeHttpRequests((authorize) -> {
			authorize.antMatchers("/forgot", "/validateUsernameAndDob", "/validateOtp", "/saveResetPassword",
					"/validateEmailAndDob", "/validateUserNameOtp", "/regenerateOtp", "/dist/**", "/image/**",
					"/plugins/**", "/sessionExpiredOrConcurrentSessionlogout").permitAll();
			authorize.antMatchers("/resetPassword").authenticated();
			authorize.antMatchers("/welcome").hasAnyRole("HQ", "ADMIN", "FO", "RU", "RM", "AP", "VW", "L1", "L2", "L3",
					"MCM", "ScrutinyFO", "ScrutinyUpload", "ScrutinyRU", "CAG_HQ", "CAG_FO", "Enforcement_HQ",
					"Enforcement_FO", "Enforcement_SVO", "FOA");
			authorize.antMatchers("/hq/**").hasRole("HQ");
			authorize.antMatchers("/admin/**").hasRole("ADMIN");
			authorize.antMatchers("/fo/**").hasRole("FO");
			authorize.antMatchers("/ru/**").hasRole("RU");
			authorize.antMatchers("/rm/**").hasRole("RM");
			authorize.antMatchers("/ap/**").hasRole("AP");
			authorize.antMatchers("/vw/**").hasRole("VW");
			authorize.antMatchers("/l1/**").hasRole("L1");
			authorize.antMatchers("/l2/**").hasRole("L2");
			authorize.antMatchers("/l3/**").hasRole("L3");
			authorize.antMatchers("/mcm/**").hasRole("MCM");
			authorize.antMatchers("/scrutiny_fo/**").hasRole("ScrutinyFO");
			authorize.antMatchers("/scrutiny_hq/**").hasRole("ScrutinyUpload");
			authorize.antMatchers("/scrutiny_ru/**").hasRole("ScrutinyRU");
			authorize.antMatchers("/cag_hq/**").hasRole("CAG_HQ");
			authorize.antMatchers("/cag_fo/**").hasRole("CAG_FO");
			authorize.antMatchers("/enforcement_hq/**").hasRole("Enforcement_HQ");
			authorize.antMatchers("/enforcement_fo/**").hasRole("Enforcement_FO");
			authorize.antMatchers("/enforcement_svo/**").hasRole("Enforcement_SVO");
			authorize.antMatchers("/foa/**").hasRole("FOA");
		});
		http.formLogin(form -> form.loginPage("/login").loginProcessingUrl("/authenticate")
				.defaultSuccessUrl("/welcome", true).permitAll());
		http.logout(logout -> logout.logoutUrl("/logout").logoutSuccessUrl("/login").invalidateHttpSession(true)
				.deleteCookies("JSESSIONID"));
		http.csrf(Customizer.withDefaults());
		http.sessionManagement().maximumSessions(1)
				.expiredSessionStrategy(sessionInformationExpiredStrategy("/sessionExpiredOrConcurrentSessionlogout"))
				.and().sessionCreationPolicy(SessionCreationPolicy.ALWAYS);
		return http.build();
	}

	@Bean
	public SessionInformationExpiredStrategy sessionInformationExpiredStrategy(String url) {
		return new CustomSessionInformationExpiredStrategy(url);
	}
}
