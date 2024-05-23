package aseca.lastmile.modules.delivery.model

import kotlinx.serialization.Serializable

@Serializable
data class CreateDeliveryDTO(
    val status: Status,
    val addressId: Int,
    val driverId: Int
    )