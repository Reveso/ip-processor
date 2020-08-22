package com.lukasrosz.ipprocessor.processing.application;

import com.lukasrosz.ipprocessor.processing.model.Address;
import com.lukasrosz.ipprocessor.processing.model.AddressRepository;
import com.lukasrosz.ipprocessor.processing.model.UnprocessedAddress;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Queue;

@RequiredArgsConstructor
public class AddressService {

    private final AddressRepository addressRepository;


    @Transactional
    public void update(final Queue<Address> ipAddressesIds) {
        addressRepository.updateDetails(ipAddressesIds);
    }

    @Transactional
    public List<UnprocessedAddress> getUnprocessedAddresses(int limit) {
        return addressRepository.getUnprocessedAddresses(limit);
    }

}
