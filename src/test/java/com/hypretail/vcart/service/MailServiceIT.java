package com.hypretail.vcart.service;
import io.micronaut.context.annotation.Property;
import io.micronaut.test.annotation.MicronautTest;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.TestInstance;

/**
 * Integration tests for {@link MailService}.
 */
@Disabled("Email functionality not yet implemented for Micronaut")
@MicronautTest(transactional = false)
@Property(name = "micronaut.security.enabled", value = "false")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class MailServiceIT {

}
