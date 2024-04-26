import com.example.model.Delivery
import com.example.model.DeliveryPlan
import com.example.model.Driver
import junit.framework.TestCase.assertEquals
import kotlin.test.Test

class DriverTest {
    @Test
    fun test01_createDriverWithNoDeliveries() {
        val driver = Driver(1, "John Doe")
        assertEquals(1, driver.id)
        assertEquals("John Doe", driver.name)
        assertEquals(false, driver.hasRoute())

    }

    @Test
    fun test02_createDriverWithDeliveries() {
        val deliveryPlan = DeliveryPlan(1, listOf(Delivery(1, "123 Main St"), Delivery(2, "456 Elm St")))
        val driver = Driver(1, "John Doe", deliveryPlan)

        assertEquals(2, driver.deliveryPlan?.getDeliveriesCount())
    }

    @Test
    fun test03_startRoute() {
        val deliveryPlan = DeliveryPlan(1, listOf(Delivery(1, "123 Main St"), Delivery(2, "456 Elm St")))
        val driver = Driver(1, "John Doe", deliveryPlan)

        val updatedDriver = driver.startPlan()
        assertEquals(2, updatedDriver.deliveryPlan?.getDeliveriesCount())
        assertEquals("123 Main St", updatedDriver.deliveryPlan?.currentDelivery()?.address)
    }
}