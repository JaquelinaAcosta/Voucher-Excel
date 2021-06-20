package com.voucherExcel.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.provider.error.OAuth2AccessDeniedHandler;

//se habilita la configuracion para activar un servidor de recursos
@Configuration
@EnableResourceServer
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {

	@Override
	public void configure(HttpSecurity http) throws Exception {

		  http
		  //.anonymous().disable()
		  .authorizeRequests()
		  //permitimos las url /oauth
		  .antMatchers("/oauth/**").permitAll()
		  //definimos seguridad para cualquier request /api/**
		  .antMatchers("/middlewareVoucher/**").permitAll()
		  .antMatchers("/apiVoucher/**").permitAll()
		  .antMatchers("/**").permitAll() 
		  .anyRequest().authenticated()
		  //devolver 403 si no tiene privilegios de acceso
		  .and().exceptionHandling().accessDeniedHandler(new OAuth2AccessDeniedHandler());
	
	}

}