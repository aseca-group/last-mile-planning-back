package aseca.lastmile.modules.driver.model

import kotlinx.serialization.Serializable

@Serializable
data class CreateDriverDTO(
    val name: String
)