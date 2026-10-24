package ru.example.instruments.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.example.instruments.model.MusicalInstrument;

import java.util.List;

@Repository
public class MusicalInstrumentRepository {
    private final JdbcTemplate jdbcTemplate;

    public MusicalInstrumentRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final org.springframework.jdbc.core.RowMapper<MusicalInstrument> mapper = (rs, rowNum) ->
            new MusicalInstrument(
                    rs.getLong("id"), rs.getString("name"), rs.getString("brand"),
                    rs.getString("instrument_type"), rs.getString("material"),
                    rs.getBigDecimal("price"), rs.getInt("quantity"));

    public MusicalInstrument save(MusicalInstrument instrument) {
        Long id = jdbcTemplate.queryForObject(
                "INSERT INTO musical_instruments (name, brand, instrument_type, material, price, quantity) "
                        + "VALUES (?, ?, ?, ?, ?, ?) RETURNING id",
                Long.class, instrument.getName(), instrument.getBrand(), instrument.getInstrumentType(),
                instrument.getMaterial(), instrument.getPrice(), instrument.getQuantity());
        instrument.setId(id);
        return instrument;
    }

    public List<MusicalInstrument> findAll() {
        return jdbcTemplate.query("SELECT * FROM musical_instruments ORDER BY id", mapper);
    }

    public List<MusicalInstrument> findByBrand(String brand) {
        return jdbcTemplate.query("SELECT * FROM musical_instruments WHERE LOWER(brand) LIKE LOWER(?) ORDER BY id",
                mapper, "%" + brand + "%");
    }

    public int update(MusicalInstrument instrument) {
        return jdbcTemplate.update(
                "UPDATE musical_instruments SET name=?, brand=?, instrument_type=?, material=?, price=?, quantity=? WHERE id=?",
                instrument.getName(), instrument.getBrand(), instrument.getInstrumentType(), instrument.getMaterial(),
                instrument.getPrice(), instrument.getQuantity(), instrument.getId());
    }

    public int deleteById(long id) {
        return jdbcTemplate.update("DELETE FROM musical_instruments WHERE id = ?", id);
    }
}
