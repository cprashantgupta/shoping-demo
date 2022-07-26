package com.hypretail.vcart.config.timezone;

import com.hypretail.vcart.VcartApp;
import com.hypretail.vcart.repository.timezone.DateTimeWrapper;
import com.hypretail.vcart.repository.timezone.DateTimeWrapperRepository;

import io.micronaut.context.annotation.Value;
import io.micronaut.data.jdbc.runtime.JdbcOperations;
import io.micronaut.test.annotation.MicronautTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;
import javax.transaction.Transactional;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.*;
import java.time.format.DateTimeFormatter;

import static java.lang.String.format;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;


/**
 * Integration tests for the UTC Hibernate configuration.
 */
@MicronautTest(application = VcartApp.class, environments = {"test"})
public class HibernateTimeZoneIT {

    @Inject DateTimeWrapperRepository dateTimeWrapperRepository;
    @Inject JdbcOperations jdbcOperations;

    @Value("${jpa.default.hibernate.jdbc.time_zone:UTC}")
    private String zoneId;

    private DateTimeWrapper dateTimeWrapper;
    private DateTimeFormatter dateTimeFormatter;
    private DateTimeFormatter timeFormatter;
    private DateTimeFormatter dateFormatter;

    @BeforeEach
    public void setup() {
        dateTimeWrapper = new DateTimeWrapper();
        dateTimeWrapper.setInstant(Instant.parse("2014-11-12T05:50:00.1Z"));
        dateTimeWrapper.setLocalDateTime(LocalDateTime.parse("2014-11-12T07:50:00.1"));
        dateTimeWrapper.setOffsetDateTime(OffsetDateTime.parse("2011-12-14T08:30:00.1Z"));
        dateTimeWrapper.setZonedDateTime(ZonedDateTime.parse("2011-12-14T08:30:00.1Z"));
        dateTimeWrapper.setLocalTime(LocalTime.parse("14:30:00"));
        dateTimeWrapper.setOffsetTime(OffsetTime.parse("14:30:00+02:00"));
        dateTimeWrapper.setLocalDate(LocalDate.parse("2016-09-10"));

        dateTimeFormatter = DateTimeFormatter
            .ofPattern("yyyy-MM-dd HH:mm:ss.S")
            .withZone(ZoneId.of(zoneId));

        timeFormatter = DateTimeFormatter
            .ofPattern("HH:mm:ss")
            .withZone(ZoneId.of(zoneId));

        dateFormatter = DateTimeFormatter
            .ofPattern("yyyy-MM-dd");
    }

    @Test
    @Transactional
    public void storeInstantWithUtcConfigShouldBeStoredOnGMTTimeZone() {
        dateTimeWrapperRepository.saveAndFlush(dateTimeWrapper);

        String request = generateSqlRequest("instant", dateTimeWrapper.getId());
        ResultSet resultSet = jdbcOperations.prepareStatement(request, statement -> statement.executeQuery());
        String expectedValue = dateTimeFormatter.format(dateTimeWrapper.getInstant());

        assertThatDateStoredValueIsEqualToInsertDateValueOnGMTTimeZone(resultSet, expectedValue);
    }

    @Test
    @Transactional
    public void storeLocalDateTimeWithUtcConfigShouldBeStoredOnGMTTimeZone() {
        dateTimeWrapperRepository.saveAndFlush(dateTimeWrapper);

        String request = generateSqlRequest("local_date_time", dateTimeWrapper.getId());
        ResultSet resultSet = jdbcOperations.prepareStatement(request, statement -> statement.executeQuery());
        String expectedValue = dateTimeWrapper
            .getLocalDateTime()
            .atZone(ZoneId.systemDefault())
            .format(dateTimeFormatter);

        assertThatDateStoredValueIsEqualToInsertDateValueOnGMTTimeZone(resultSet, expectedValue);
    }

    @Test
    @Transactional
    public void storeOffsetDateTimeWithUtcConfigShouldBeStoredOnGMTTimeZone() {
        dateTimeWrapperRepository.saveAndFlush(dateTimeWrapper);

        String request = generateSqlRequest("offset_date_time", dateTimeWrapper.getId());
        ResultSet resultSet = jdbcOperations.prepareStatement(request, statement -> statement.executeQuery());
        String expectedValue = dateTimeWrapper
            .getOffsetDateTime()
            .format(dateTimeFormatter);

        assertThatDateStoredValueIsEqualToInsertDateValueOnGMTTimeZone(resultSet, expectedValue);
    }

    @Test
    @Transactional
    public void storeZoneDateTimeWithUtcConfigShouldBeStoredOnGMTTimeZone() {
        dateTimeWrapperRepository.saveAndFlush(dateTimeWrapper);

        String request = generateSqlRequest("zoned_date_time", dateTimeWrapper.getId());
        ResultSet resultSet = jdbcOperations.prepareStatement(request, statement -> statement.executeQuery());
        String expectedValue = dateTimeWrapper
            .getZonedDateTime()
            .format(dateTimeFormatter);

        assertThatDateStoredValueIsEqualToInsertDateValueOnGMTTimeZone(resultSet, expectedValue);
    }

    @Test
    @Transactional
    public void storeLocalTimeWithUtcConfigShouldBeStoredOnGMTTimeZoneAccordingToHis1stJan1970Value() {
        dateTimeWrapperRepository.saveAndFlush(dateTimeWrapper);

        String request = generateSqlRequest("local_time", dateTimeWrapper.getId());
        ResultSet resultSet = jdbcOperations.prepareStatement(request, statement -> statement.executeQuery());
        String expectedValue = dateTimeWrapper
            .getLocalTime()
            .atDate(LocalDate.of(1970, Month.JANUARY, 1))
            .atZone(ZoneId.systemDefault())
            .format(timeFormatter);

        assertThatDateStoredValueIsEqualToInsertDateValueOnGMTTimeZone(resultSet, expectedValue);
    }

    @Test
    @Transactional
    public void storeOffsetTimeWithUtcConfigShouldBeStoredOnGMTTimeZoneAccordingToHis1stJan1970Value() {
        dateTimeWrapperRepository.saveAndFlush(dateTimeWrapper);

        String request = generateSqlRequest("offset_time", dateTimeWrapper.getId());
        ResultSet resultSet = jdbcOperations.prepareStatement(request, statement -> statement.executeQuery());
        String expectedValue = dateTimeWrapper
            .getOffsetTime()
            .toLocalTime()
            .atDate(LocalDate.of(1970, Month.JANUARY, 1))
            .atZone(ZoneId.systemDefault())
            .format(timeFormatter);

        assertThatDateStoredValueIsEqualToInsertDateValueOnGMTTimeZone(resultSet, expectedValue);
    }

    @Test
    @Transactional
    public void storeLocalDateWithUtcConfigShouldBeStoredWithoutTransformation() {
        dateTimeWrapperRepository.saveAndFlush(dateTimeWrapper);

        String request = generateSqlRequest("local_date", dateTimeWrapper.getId());
        ResultSet resultSet = jdbcOperations.prepareStatement(request, statement -> statement.executeQuery());
        String expectedValue = dateTimeWrapper
            .getLocalDate()
            .format(dateFormatter);

        assertThatDateStoredValueIsEqualToInsertDateValueOnGMTTimeZone(resultSet, expectedValue);
    }

    private String generateSqlRequest(String fieldName, long id) {
        return format("SELECT %s FROM jhi_date_time_wrapper where id=%d", fieldName, id);
    }

    private void assertThatDateStoredValueIsEqualToInsertDateValueOnGMTTimeZone(ResultSet resultSet, String expectedValue) {
        try {
            while (resultSet.next()) {
                String dbValue = resultSet.getString(1);

                assertThat(dbValue).isNotNull();
                assertThat(dbValue).isEqualTo(expectedValue);
            }
        } catch (SQLException sqe) {
            fail("Unable to find results");
        }
    }
}
