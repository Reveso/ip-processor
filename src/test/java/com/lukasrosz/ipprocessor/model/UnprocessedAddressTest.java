package com.lukasrosz.ipprocessor.model;

import com.lukasrosz.ipprocessor.processing.model.AddressCountryDetails;
import com.lukasrosz.ipprocessor.processing.model.ProcessedAddress;
import com.lukasrosz.ipprocessor.processing.model.UnprocessedAddress;
import lombok.val;
import org.junit.jupiter.api.Test;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class UnprocessedAddressTest {

    @Test
    void shouldCreateUnprocessedAddress() throws UnknownHostException {
        val inetAddress = InetAddress.getByName("125.13.255.151");
        assertDoesNotThrow(() -> new UnprocessedAddress(1L, inetAddress));
    }

    @Test
    void shouldThrowNullExceptionForId() throws UnknownHostException {
        val inetAddress = InetAddress.getByName("125.13.255.151");
        assertThrows(NullPointerException.class, () -> new UnprocessedAddress(null, inetAddress));
    }

    @Test
    void shouldThrowNullExceptionForAddress() {
        assertThrows(NullPointerException.class, () -> new UnprocessedAddress(1L, null));
    }

    @Test
    void shouldCreateProcessedAddress() throws UnknownHostException {
        val inetAddress = InetAddress.getByName("125.13.255.151");
        val unprocessedAddress = new UnprocessedAddress(1L, inetAddress);
        val countryDetails = AddressCountryDetails.builder()
                .countryCode("MA")
                .countryCode3("MAR")
                .countryName("Morocco")
                .build();

        ProcessedAddress processedAddress = unprocessedAddress.process(countryDetails);

        ProcessedAddress expectedProcessedAddress = ProcessedAddress.builder()
                .id(unprocessedAddress.getId())
                .address(unprocessedAddress.getAddress())
                .countryDetails(countryDetails)
                .processedDate(processedAddress.getProcessedDate())
                .build();
        assertThat(processedAddress.getProcessedDate()).isCloseTo(new Date(), 1000 * 60);
        assertEquals(expectedProcessedAddress, processedAddress);
    }
}
