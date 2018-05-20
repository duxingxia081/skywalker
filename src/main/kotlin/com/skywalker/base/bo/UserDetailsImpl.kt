package com.skywalker.base.bo

import com.skywalker.base.bo.MhoSkywalkerUser
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

class UserDetailsImpl(val mhoSkywalkerUser: MhoSkywalkerUser) : UserDetails {

    override fun getUsername() = mhoSkywalkerUser.userName
    override fun getPassword() = mhoSkywalkerUser.password
    override fun getAuthorities() = mutableListOf(GrantedAuthority { "ROLE_USER" })
    override fun isEnabled() = true
    override fun isCredentialsNonExpired() = true
    override fun isAccountNonExpired() = true
    override fun isAccountNonLocked() = true

}