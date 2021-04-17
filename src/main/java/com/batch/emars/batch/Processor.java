package com.batch.emars.batch;

import com.batch.emars.entity.Emars;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

import java.util.Random;

@Component
public class Processor implements ItemProcessor<Emars, Emars> {
    @Override
    public Emars process(Emars emars) throws Exception {
        Random random = new Random();
        emars.setId(random.nextLong()); //need to test batch size provided by Hibernate so removed @GeneratedValue from id
        return emars;
    }
}
