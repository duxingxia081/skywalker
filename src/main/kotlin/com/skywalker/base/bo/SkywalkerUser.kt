package com.skywalker.base.bo

import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "mho_skywalker_user")
data class SkywalkerUser(
    @Id
    var userId: Long = 0L,
    var userName: String = "",
    var nickname: String? = null,
    var headImage: String? = null
)