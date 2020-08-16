package com.lukasrosz.ipprocessor.processing.model;

import java.util.List;
import java.util.Queue;

public interface AddressRepository {

    void update(final Queue<ProcessedAddress> processedAddresses);

    List<UnprocessedAddress> getUnprocessedAddresses(int limit);
}
