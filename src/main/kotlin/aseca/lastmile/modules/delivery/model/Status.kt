package aseca.lastmile.modules.delivery.model

import kotlinx.serialization.Serializable

@Serializable
enum class Status {
    PENDING,
    IN_PROGRESS,
    COMPLETED,
}