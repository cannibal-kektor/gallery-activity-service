package kektor.innowise.gallery.activity

import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles

@SpringBootTest
@ActiveProfiles("it")
class SimpleIT {

    @Test
    fun `check `() {
        println("HEY")
    }

}