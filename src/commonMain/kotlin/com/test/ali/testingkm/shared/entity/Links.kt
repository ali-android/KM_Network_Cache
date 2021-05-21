package com.test.ali.testingkm.shared.entity

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Links(@SerialName("mission_patch")
                 val missionPatchUrl: String?,
                 @SerialName("article_link")
                 val articleUrl: String?) {
}