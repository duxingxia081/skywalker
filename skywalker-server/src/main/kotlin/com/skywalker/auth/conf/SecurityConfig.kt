package com.skywalker.auth.conf

import com.skywalker.auth.filter.StatelessAuthenticationFilter
import com.skywalker.core.constants.ErrorConstants
import com.skywalker.core.exception.ServiceException
import org.springframework.boot.actuate.autoconfigure.security.servlet.EndpointRequest
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter


@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
class SecurityConfig(
    private val userDetailsService: UserDetailsService,
    private val statelessAuthenticationFilter: StatelessAuthenticationFilter
) : WebSecurityConfigurerAdapter(true) {
    override fun configure(http: HttpSecurity) {
        try {
            http.csrf().disable()

            http
                .exceptionHandling().and()
                .anonymous().and()
                .servletApi().and()
                .headers().cacheControl()

            http.authorizeRequests()
                .antMatchers(HttpMethod.GET, "/users/myinfo").hasRole("USER")
                .antMatchers(HttpMethod.POST, "/activity/*/activityImg").hasRole("USER")
                .and()
            http.requestMatcher(EndpointRequest.toAnyEndpoint()).authorizeRequests().anyRequest().permitAll()

            http.addFilterBefore(statelessAuthenticationFilter, UsernamePasswordAuthenticationFilter::class.java)
        } catch (e: AccessDeniedException) {
            throw ServiceException(ErrorConstants.ERROR_CODE_1001, ErrorConstants.ERROR_MSG_1001, e)
        } catch (e: Exception) {
            throw ServiceException(ErrorConstants.ERROR_CODE_9999, e.message ?: "访问权限系统错误", e)
        }
    }

    @Bean
    override fun authenticationManagerBean(): AuthenticationManager =
        super.authenticationManagerBean()

    override fun configure(auth: AuthenticationManagerBuilder) {
        auth.userDetailsService(userDetailsService).passwordEncoder(BCryptPasswordEncoder())
    }

    override fun userDetailsService() = userDetailsService

}