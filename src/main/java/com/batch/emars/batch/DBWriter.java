package com.batch.emars.batch;

import com.batch.emars.entity.Emars;
import com.batch.emars.repository.EmarsRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public class DBWriter implements ItemWriter<Emars> {

    @Autowired
    private EmarsRepository emarsRepository;

    @Override
    public void write(List<? extends Emars> emarsList) throws Exception {
        log.info("Data saved for Emars list: {}", emarsList);
        emarsRepository.saveAll(emarsList);
    }
}
