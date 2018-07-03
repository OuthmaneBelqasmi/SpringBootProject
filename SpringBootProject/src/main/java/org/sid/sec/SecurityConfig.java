	package org.sid.sec;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter{
	
	
	@Autowired
	private DataSource dataSource;   
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {  
		auth.inMemoryAuthentication().withUser("admin").password("1234").roles("ADMIN","USER");
		auth.inMemoryAuthentication().withUser("user").password("1234").roles("USER");

		/*
		auth.jdbcAuthentication().dataSource(dataSource)
		.usersByUsernameQuery("select username asprincipal, password as credentials , active from users where username=?")
		.authoritiesByUsernameQuery("select username asprincipal, role as role from roles_users where username=?")
		.rolePrefix("ROLE_")
		.passwordEncoder( new Md5PasswordEncoder());
		*/
	}
	
	@Override
	protected void configure(HttpSecurity http) throws Exception { 
		
		http.formLogin().loginPage("/login");
		http.authorizeRequests().antMatchers("/consultercompte","/operations").hasRole("USER");
		http.authorizeRequests().antMatchers("/saveOperation","/comptes").hasRole("ADMIN");
		http.exceptionHandling().accessDeniedPage("/403");

	}

}
