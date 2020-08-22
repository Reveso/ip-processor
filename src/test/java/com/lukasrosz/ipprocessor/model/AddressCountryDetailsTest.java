package com.lukasrosz.ipprocessor.model;

import com.lukasrosz.ipprocessor.processing.model.AddressCountryDetails;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class AddressCountryDetailsTest {

    @Test
    void shouldCreateCountryDetails() {
        assertDoesNotThrow(() ->
                new AddressCountryDetails("US", "USA", "United States", "\uD83C\uDDFA\uD83C\uDDF8"));
    }

    @Test
    void shouldCreateCountryDetailsWithoutCountryEmoji() {
        assertDoesNotThrow(() ->
                new AddressCountryDetails("EN", "ENG", "England", null));
    }

    @Test
    void shouldThrowNullExceptionForCountryCode() {
        assertThrows(NullPointerException.class, () ->
                new AddressCountryDetails(null, "ENG", "England", null));
    }

    @Test
    void shouldThrowNullExceptionForCountryCode3() {
        assertThrows(NullPointerException.class, () ->
                new AddressCountryDetails("EN", null, "England", null));
    }

    @Test
    void shouldThrowNullExceptionForCountryName() {
        assertThrows(NullPointerException.class, () ->
                new AddressCountryDetails("EN", "ENG", null, null));
    }

    @Test
    void shouldThrowIllegalArgumentExceptionForEmptyCountryCode() {
        assertThrows(IllegalArgumentException.class, () ->
                new AddressCountryDetails("", "ENG", "England", null));
    }

    @Test
    void shouldThrowIllegalArgumentExceptionForEmptyCountryCode3() {
        assertThrows(IllegalArgumentException.class, () ->
                new AddressCountryDetails("EN", "", "England", null));
    }

    @Test
    void shouldThrowIllegalArgumentExceptionForEmptyCountryName() {
        assertThrows(IllegalArgumentException.class, () ->
                new AddressCountryDetails("EN", "ENG", "", null));
    }



}
