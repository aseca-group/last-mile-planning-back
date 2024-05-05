import com.example.model.Delivery
import com.example.model.Schedule
import com.example.model.Status
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

class ScheduleTest {
    @Test
    fun test01_scheduleWithoutDeliveriesShouldReturnEmptyList() {
        val schedule = Schedule(1, emptyList())

        assert(schedule.deliveries.isEmpty())
    }

    @Test
    fun test02_scheduleWithOneShouldReturnThatDelivery() {
        val delivery = Delivery(1, "Address 1")
        val schedule = Schedule(1, listOf(delivery))

        assert(schedule.deliveries.contains(delivery))
    }


    @Test
    fun test03_startScheduleShouldUpdateAllDeliveriesToInTransitAndTheFirstOneToOutForDelivery() {
        val delivery1 = Delivery(1, "Address 1")
        val delivery2 = Delivery(2, "Address 2")
        val delivery3 = Delivery(3, "Address 3")
        val schedule = Schedule(1, listOf(delivery1, delivery2, delivery3))

        val updatedDeliveryRoute = schedule.start()

        assertEquals(Status.OUT_FOR_DELIVERY, updatedDeliveryRoute.deliveries.first().status)
        assertEquals(Status.IN_TRANSIT, updatedDeliveryRoute.deliveries[1].status)
        assertEquals(Status.IN_TRANSIT, updatedDeliveryRoute.deliveries[2].status)
    }

    @Test
    fun test04_currentDeliveryShouldReturnTheFirstDeliveryWithStatusOutForDelivery() {
        val delivery1 = Delivery(1, "Address 1")
        val delivery2 = Delivery(2, "Address 2")
        val schedule = Schedule(1, listOf(delivery1, delivery2)).start()

        val currentDelivery = schedule.currentDelivery()

        assertNotNull(currentDelivery)
        assertEquals(delivery1.id, currentDelivery.id)
    }

    @Test
    fun test05_updateCurrentDeliveryStatusShouldUpdateTheStatusOfTheCurrentDeliveryAndChangeTheNextDeliveryStatusToOutForDelivery() {
        val delivery1 = Delivery(1, "Address 1")
        val delivery2 = Delivery(2, "Address 2")
        val schedule = Schedule(1, listOf(delivery1, delivery2)).start()

        val updatedDeliveryRoute = schedule.updateCurrentDeliveryStatus(Status.DELIVERED)

        assertEquals(Status.DELIVERED, updatedDeliveryRoute.deliveries.first().status)
        assertEquals(Status.OUT_FOR_DELIVERY, updatedDeliveryRoute.deliveries[1].status)
    }

    @Test
    fun test06_updateCurrentDeliveryStatusShouldNotChangeTheStatusOfTheCurrentDeliveryIfThereIsNoCurrentDelivery() {
        val delivery1 = Delivery(1, "Address 1")
        val delivery2 = Delivery(2, "Address 2")
        val schedule = Schedule(1, listOf(delivery1, delivery2))

        val updatedDeliveryRoute = schedule.updateCurrentDeliveryStatus(Status.DELIVERED)

        assertEquals(delivery1, updatedDeliveryRoute.deliveries.first())
        assertEquals(delivery2, updatedDeliveryRoute.deliveries[1])
    }
}