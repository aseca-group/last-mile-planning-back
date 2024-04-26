import com.example.model.Delivery
import com.example.model.DeliveryRoute
import com.example.model.Status
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

class DeliveryRouteTest {
    @Test
    fun test01_deliveryRouteWithoutDeliveriesShouldReturnEmptyList() {
        val deliveryRoute = DeliveryRoute(1, emptyList())

        assert(deliveryRoute.deliveries.isEmpty())
    }

    @Test
    fun test02_deliveryRouteWithOneShouldReturnThatDelivery() {
        val delivery = Delivery(1, "Address 1")
        val deliveryRoute = DeliveryRoute(1, listOf(delivery))

        assert(deliveryRoute.deliveries.contains(delivery))
    }


    @Test
    fun test03_startDeliveryRouteShouldUpdateAllDeliveriesToInTransitAndTheFirstOneToOutForDelivery() {
        val delivery1 = Delivery(1, "Address 1")
        val delivery2 = Delivery(2, "Address 2")
        val delivery3 = Delivery(3, "Address 3")
        val deliveryRoute = DeliveryRoute(1, listOf(delivery1, delivery2, delivery3))

        val updatedDeliveryRoute = deliveryRoute.start()

        assertEquals(Status.OUT_FOR_DELIVERY, updatedDeliveryRoute.deliveries.first().status)
        assertEquals(Status.IN_TRANSIT, updatedDeliveryRoute.deliveries[1].status)
        assertEquals(Status.IN_TRANSIT, updatedDeliveryRoute.deliveries[2].status)
    }

    @Test
    fun test04_currentDeliveryShouldReturnTheFirstDeliveryWithStatusOutForDelivery() {
        val delivery1 = Delivery(1, "Address 1")
        val delivery2 = Delivery(2, "Address 2")
        val deliveryRoute = DeliveryRoute(1, listOf(delivery1, delivery2)).start()

        val currentDelivery = deliveryRoute.currentDelivery()

        assertNotNull(currentDelivery)
        assertEquals(delivery1.id, currentDelivery.id)
    }

    @Test
    fun test05_updateCurrentDeliveryStatusShouldUpdateTheStatusOfTheCurrentDeliveryAndChangeTheNextDeliveryStatusToOutForDelivery() {
        val delivery1 = Delivery(1, "Address 1")
        val delivery2 = Delivery(2, "Address 2")
        val deliveryRoute = DeliveryRoute(1, listOf(delivery1, delivery2)).start()

        val updatedDeliveryRoute = deliveryRoute.updateCurrentDeliveryStatus(Status.DELIVERED)

        assertEquals(Status.DELIVERED, updatedDeliveryRoute.deliveries.first().status)
        assertEquals(Status.OUT_FOR_DELIVERY, updatedDeliveryRoute.deliveries[1].status)
    }
}