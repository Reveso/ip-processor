package com.lukasrosz.ipprocessor.processing.application;

import com.google.common.util.concurrent.RateLimiter;
import com.lukasrosz.ipprocessor.processing.infrastructure.IpCountryInfoApi;
import com.lukasrosz.ipprocessor.processing.model.Address;
import com.lukasrosz.ipprocessor.processing.model.AddressCountryDetails;
import com.lukasrosz.ipprocessor.processing.model.UnprocessedAddress;
import com.lukasrosz.ipprocessor.processing.model.external.IpCountryInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;

import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

@RequiredArgsConstructor
@Slf4j
public class AddressProcessor {

    private final AddressService addressService;
    private final IpCountryInfoApi ipCountryInfoApi;
    private final int batchLimit;
    private final RateLimiter rateLimiter;

    public void processAddresses() {
        List<UnprocessedAddress> unprocessedAddresses = addressService.getUnprocessedAddresses(batchLimit);
        while (!unprocessedAddresses.isEmpty()) {
            Queue<Address> processedAddresses = new ConcurrentLinkedQueue<>();
            populateProcessedAddressesQueue(unprocessedAddresses, processedAddresses);
            addressService.update(processedAddresses);
            unprocessedAddresses = addressService.getUnprocessedAddresses(batchLimit);
        }
    }

    private void populateProcessedAddressesQueue(List<UnprocessedAddress> unprocessedAddresses,
                                                 Queue<Address> addressesForUpdate) {
        unprocessedAddresses.parallelStream().forEach(unprocessedAddress -> {
            rateLimiter.acquire();
            ipCountryInfoApi.getIpCountryInfo(unprocessedAddress.getAddress())
                    .ifPresent(ipCountryInfo -> {
                                try {
                                    AddressCountryDetails countryDetails = fromIpCountryInfo(ipCountryInfo);
                                    val processedAddress = unprocessedAddress.process(countryDetails);
                                    addressesForUpdate.add(processedAddress);
                                    log.info("Processed address: {}", processedAddress);
                                } catch (IllegalArgumentException | NullPointerException exception) {
                                    addressesForUpdate.add(unprocessedAddress.notFound());
                                    log.warn("Failed to get country details for address {}, reason {}",
                                            unprocessedAddress.getAddress().getHostName(), exception.getCause());
                                }
                            }
                    );
        });
    }

    private AddressCountryDetails fromIpCountryInfo(IpCountryInfo ipCountryInfo) {
        return AddressCountryDetails.builder()
                .countryCode(ipCountryInfo.getCountryCode())
                .countryCode3(ipCountryInfo.getCountryCode3())
                .countryName(ipCountryInfo.getCountryName())
                .countryEmoji(ipCountryInfo.getCountryEmoji())
                .build();
    }
}
