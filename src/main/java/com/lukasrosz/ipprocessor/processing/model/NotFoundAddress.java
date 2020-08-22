package com.lukasrosz.ipprocessor.processing.model;

import lombok.Builder;
import lombok.Value;

import java.net.InetAddress;
import java.sql.Timestamp;

@Value
@Builder
public class NotFoundAddress implements Address {
    Long id;
    InetAddress address;
    Timestamp processedDate;
}
