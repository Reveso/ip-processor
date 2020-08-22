package com.lukasrosz.ipprocessor.model;

import com.lukasrosz.ipprocessor.processing.model.AddressCountryDetails;
import com.lukasrosz.ipprocessor.processing.model.ProcessedAddress;
import lombok.val;
import org.junit.jupiter.api.Test;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.sql.Timestamp;
import java.time.Instant;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ProcessedAddressTest {

    @Test
    void shouldCreateProcessedAddress() throws UnknownHostException {
        val inetAddress = InetAddress.getByName("125.13.255.151");
        val countryDetails = AddressCountryDetails.builder()
                .countryCode("EN").countryCode3("ENG").countryName("England").build();
        val processedDate = Timestamp.from(Instant.now());

        assertDoesNotThrow(() ->
                new ProcessedAddress(1L, inetAddress, countryDetails, processedDate));
    }

    @Test
    void shouldThrowNullExceptionForId() throws UnknownHostException {
        val inetAddress = InetAddress.getByName("125.13.255.151");
        val countryDetails = AddressCountryDetails.builder()
                .countryCode("EN").countryCode3("ENG").countryName("England").build();
        val processedDate = Timestamp.from(Instant.now());

        assertThrows(NullPointerException.class, () ->
                new ProcessedAddress(null, inetAddress, countryDetails, processedDate));
    }

    @Test
    void shouldThrowNullExceptionForAddress() throws UnknownHostException {
        val countryDetails = AddressCountryDetails.builder()
                .countryCode("EN").countryCode3("ENG").countryName("England").build();
        val processedDate = Timestamp.from(Instant.now());

        assertThrows(NullPointerException.class, () ->
                new ProcessedAddress(1L, null, countryDetails, processedDate));
    }

    @Test
    void shouldThrowNullExceptionForCountryDetails() throws UnknownHostException {
        val inetAddress = InetAddress.getByName("125.13.255.151");
        val processedDate = Timestamp.from(Instant.now());

        assertThrows(NullPointerException.class, () ->
                new ProcessedAddress(1L, inetAddress, null, processedDate));
    }

    @Test
    void shouldThrowNullExceptionForProcessedDate() throws UnknownHostException {
        val inetAddress = InetAddress.getByName("125.13.255.151");
        val countryDetails = AddressCountryDetails.builder()
                .countryCode("EN").countryCode3("ENG").countryName("England").build();

        assertThrows(NullPointerException.class, () ->
                new ProcessedAddress(1L, inetAddress, countryDetails, null));
    }
}
