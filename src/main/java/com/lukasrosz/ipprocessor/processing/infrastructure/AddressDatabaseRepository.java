package com.lukasrosz.ipprocessor.processing.infrastructure;

import com.lukasrosz.ipprocessor.processing.model.AddressRepository;
import com.lukasrosz.ipprocessor.processing.model.ProcessedAddress;
import com.lukasrosz.ipprocessor.processing.model.UnprocessedAddress;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Queue;

@RequiredArgsConstructor
@Slf4j
public class AddressDatabaseRepository implements AddressRepository {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<UnprocessedAddress> getUnprocessedAddresses(int limit) {
        RowMapper<UnprocessedAddress> rowMapper = getUnprocessedAddressRowMapper();
        List<UnprocessedAddress> addresses = jdbcTemplate.query(
                "SELECT id, address FROM ip_address WHERE processed = ? LIMIT ?",
                new Object[]{0, limit}, rowMapper);
        log.info("Retrieved unprocessed IpAddresses list={}", addresses);
        return addresses;
    }

    private RowMapper<UnprocessedAddress> getUnprocessedAddressRowMapper() {
        return (resultSet, rowNum) -> new UnprocessedAddress(resultSet.getLong("id"), resultSet.getString("address"));
    }

    @Override
    public void update(final Queue<ProcessedAddress> processedAddressesIds) {
        jdbcTemplate.batchUpdate(
                "update ip_address set address = ?, processed = ?, country_code = ?, country_code3 = ?, country_name = ?, country_emoji = ?, processed_date = ? where id = ?",
                new BatchPreparedStatementSetter() {
                    public void setValues(PreparedStatement ps, int i)
                            throws SQLException {
                        log.info("Setting IpAddress as processed with id={}", i);
                        ProcessedAddress address = processedAddressesIds.poll();
                        if (address == null) {
                            return;
                        }
                        ps.setString(1, address.getAddress());
                        ps.setBoolean(2, address.getProcessed());
                        ps.setString(3, address.getCountryDetails().getCountryCode());
                        ps.setString(4, address.getCountryDetails().getCountryCode3());
                        ps.setString(5, address.getCountryDetails().getCountryName());
                        ps.setString(6, address.getCountryDetails().getCountryEmoji());
                        ps.setTimestamp(7, address.getProcessedDate());
                        ps.setLong(8, address.getId());
                    }

                    public int getBatchSize() {
                        return processedAddressesIds.size();
                    }
                });
    }
}
