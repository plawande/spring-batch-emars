package com.batch.emars.batch;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.step.skip.SkipLimitExceededException;
import org.springframework.batch.core.step.skip.SkipPolicy;
import org.springframework.batch.item.file.FlatFileParseException;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class FileVerificationSkipper implements SkipPolicy {
    @Override
    public boolean shouldSkip(Throwable exception, int skipCount) throws SkipLimitExceededException {
        if(exception instanceof FlatFileParseException) {
            FlatFileParseException flatFileParseException = (FlatFileParseException) exception;
            log.error("Line no. {} : {} \n", flatFileParseException.getLineNumber(), flatFileParseException.getInput()); //instead of console logging please use logback
            return true;
        }
        return false;
    }
}
