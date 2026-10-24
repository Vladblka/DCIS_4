package ru.example.instruments.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.example.instruments.model.MusicalInstrument;
import ru.example.instruments.repository.MusicalInstrumentRepository;

import java.util.List;

@Service
@Transactional
public class MusicalInstrumentService {
    private final MusicalInstrumentRepository repository;

    public MusicalInstrumentService(MusicalInstrumentRepository repository) {
        this.repository = repository;
    }

    public MusicalInstrument add(MusicalInstrument instrument) { return repository.save(instrument); }
    public List<MusicalInstrument> all() { return repository.findAll(); }
    public List<MusicalInstrument> searchByBrand(String brand) { return repository.findByBrand(brand); }
    public boolean update(MusicalInstrument instrument) { return repository.update(instrument) > 0; }
    public boolean delete(long id) { return repository.deleteById(id) > 0; }
}
