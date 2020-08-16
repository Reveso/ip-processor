package com.lukasrosz.ipprocessor.processing.infrastructure;

import com.lukasrosz.ipprocessor.processing.model.external.CountryInfoRepository;
import com.lukasrosz.ipprocessor.processing.model.external.IpCountryInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

@RequiredArgsConstructor
@Slf4j
public class IpCountryInfoApi implements CountryInfoRepository {

    private final String API_URL;

    public Optional<IpCountryInfo> getIpCountryInfo(String ipAddress) {
        RestTemplate restTemplate = new RestTemplate();
        String requestUrl = getUrlForIp(ipAddress);

        log.info("Performing REST request to {}", requestUrl);
        IpCountryResponse response = restTemplate.getForObject(requestUrl, IpCountryResponse.class);
        log.info("REST response={} from url={}", response, requestUrl);

        return response != null ?
                Optional.of(new IpCountryInfo(response.getCountryCode(), response.getCountryCode3(),
                        response.getCountryName(), response.getCountryEmoji())) :
                Optional.empty();
    }

    private String getUrlForIp(String ipAddress) {
        return API_URL + ipAddress;
    }


}

@lombok.Value
class IpCountryResponse {
    String countryCode;
    String countryCode3;
    String countryName;
    String countryEmoji;

    private IpCountryResponse() {
        this.countryCode = null;
        this.countryCode3 = null;
        this.countryName = null;
        this.countryEmoji = null;
    }
}