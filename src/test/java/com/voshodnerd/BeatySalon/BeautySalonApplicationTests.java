package com.voshodnerd.BeatySalon;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
class BeautySalonApplicationTests {

	@Test
	@DisplayName("Checks loading contexts")
	void contextLoads() {
	}

}
