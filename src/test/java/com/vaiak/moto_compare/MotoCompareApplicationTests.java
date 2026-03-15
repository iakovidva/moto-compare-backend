package com.vaiak.moto_compare;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@ActiveProfiles("test")
class MotoCompareApplicationTests {

	@Test
	void contextLoads() {
	}

    @Test
    void dummyFailingTest() {
        assertTrue(false);
    }
}
