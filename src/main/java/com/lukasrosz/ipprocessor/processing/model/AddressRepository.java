package com.lukasrosz.ipprocessor.processing.model;

import java.util.List;
import java.util.Queue;

public interface AddressRepository {

    void updateDetails(final Queue<Address> addresses);

    List<UnprocessedAddress> getUnprocessedAddresses(int limit);
}
