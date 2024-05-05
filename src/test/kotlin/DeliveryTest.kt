import com.example.model.Delivery
import com.example.model.Status
import org.junit.Test
import kotlin.test.assertEquals

class DeliveryTest {

    @Test
    fun test01_newDeliveryShouldHaveIdAndAddress() {
        val delivery = Delivery(1, "Address 1")
        assertEquals(1, delivery.id)
        assertEquals("Address 1", delivery.address)
    }
    @Test
    fun test02_newDeliveryStatusShouldBePending() {
        val delivery = Delivery(1, "Address 1")
        assertEquals(Status.PENDING, delivery.status)
    }

    @Test
    fun test03_newDeliveryWithStatusShouldHaveThatStatus() {
        val delivery = Delivery(1, "Address 1", Status.DELIVERED)
        assertEquals(Status.DELIVERED, delivery.status)
    }

}