package com.lukasrosz.ipprocessor.processing.infrastructure;

import com.lukasrosz.ipprocessor.processing.model.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Queue;

import static com.lukasrosz.ipprocessor.processing.infrastructure.AddressDatabaseRepository.AddressState.*;

@RequiredArgsConstructor
@Slf4j
public class AddressDatabaseRepository implements AddressRepository {

    private final JdbcTemplate jdbcTemplate;

    enum AddressState {
        PROCESSED, NOT_PROCESSED, NOT_FOUND
    }

    @Override
    public List<UnprocessedAddress> getUnprocessedAddresses(int limit) {
        RowMapper<UnprocessedAddress> rowMapper = getUnprocessedAddressRowMapper();
        List<UnprocessedAddress> addresses = jdbcTemplate.query(
                "SELECT id, address FROM ip_address WHERE processing_state = ? LIMIT ?",
                new Object[]{NOT_PROCESSED.toString(), limit}, rowMapper);
        log.info("Retrieved unprocessed IpAddresses list={}", addresses);
        return addresses;
    }

    private RowMapper<UnprocessedAddress> getUnprocessedAddressRowMapper() {
        return (resultSet, rowNum) -> {
            try {
                return new UnprocessedAddress(resultSet.getLong("id"), InetAddress.getByName(resultSet.getString("address")));
            } catch (UnknownHostException e) {
                return null;
            }
        };
    }

    @Override
    public void updateDetails(final Queue<Address> processedAddressesIds) {
        jdbcTemplate.batchUpdate(
                "update ip_address set processing_state = ?, country_code = ?, country_code3 = ?, country_name = ?, country_emoji = ?, processed_date = ? where id = ?",
                new BatchPreparedStatementSetter() {
                    public void setValues(PreparedStatement ps, int i)
                            throws SQLException {
                        Address address = processedAddressesIds.poll();
                        if (address == null) {
                            return;
                        }

                        if (address instanceof ProcessedAddress) {
                            log.info("Updating processed address in DB={}", address);
                            prepareStatement((ProcessedAddress) address, ps);
                        } else if (address instanceof NotFoundAddress) {
                            log.info("Updating not found address in DB={}", address);
                            prepareStatement((NotFoundAddress) address, ps);
                        } else if (address instanceof UnprocessedAddress) {
                            log.info("Updating unprocessed address in DB={}", address);
                            prepareStatement((UnprocessedAddress) address, ps);
                        }
                    }

                    public int getBatchSize() {
                        return processedAddressesIds.size();
                    }
                });
    }

    private void prepareStatement(ProcessedAddress address, PreparedStatement ps) throws SQLException {
        ps.setString(1, PROCESSED.toString());
        ps.setString(2, address.getCountryDetails().getCountryCode());
        ps.setString(3, address.getCountryDetails().getCountryCode3());
        ps.setString(4, address.getCountryDetails().getCountryName());
        ps.setString(5, address.getCountryDetails().getCountryEmoji());
        ps.setTimestamp(6, address.getProcessedDate());
        ps.setLong(7, address.getId());
    }

    private void prepareStatement(NotFoundAddress address, PreparedStatement ps) throws SQLException {
        ps.setString(1, NOT_FOUND.toString());
        ps.setString(2, null);
        ps.setString(3, null);
        ps.setString(4, null);
        ps.setString(5, null);
        ps.setTimestamp(6, address.getProcessedDate());
        ps.setLong(7, address.getId());
    }

    private void prepareStatement(UnprocessedAddress address, PreparedStatement ps) throws SQLException {
        ps.setString(1, NOT_PROCESSED.toString());
        ps.setString(2, null);
        ps.setString(3, null);
        ps.setString(4, null);
        ps.setString(5, null);
        ps.setTimestamp(6, null);
        ps.setLong(7, address.getId());
    }
}
