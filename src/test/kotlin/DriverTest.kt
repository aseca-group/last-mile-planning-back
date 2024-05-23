import aseca.lastmile.modules.driver.model.Driver
import junit.framework.TestCase.assertEquals
import kotlin.test.Test

class DriverTest {
    @Test
    fun test01_createDriver() {
        val driver = Driver(1, "John Doe")
        assertEquals(1, driver.id)
        assertEquals("John Doe", driver.name)
    }
}