package com.batch.emars.config;

import com.batch.emars.batch.FileVerificationSkipper;
import com.batch.emars.entity.Emars;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.FlatFileParseException;
import org.springframework.batch.item.file.LineMapper;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.convert.ApplicationConversionService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.convert.support.DefaultConversionService;
import org.springframework.core.io.Resource;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Configuration
@EnableBatchProcessing
public class SpringBatchConfig {

    @Bean
    public Job job(JobBuilderFactory jobBuilderFactory,
                   @Qualifier("emarsStep") Step step) {

        return jobBuilderFactory.get("Emars-Load")
                .incrementer(new RunIdIncrementer())
                .start(step)
                .build();
    }

    @Bean
    @Qualifier("emarsStep")
    public Step step(StepBuilderFactory stepBuilderFactory,
                     ItemReader<Emars> itemReader,
                     ItemProcessor<Emars, Emars> itemProcessor,
                     ItemWriter<Emars> itemWriter,
                     FileVerificationSkipper fileVerificationSkipper) {

        return stepBuilderFactory.get("Emars-file-load")
                .<Emars, Emars>chunk(1000)
                .reader(itemReader)
                .processor(itemProcessor)
                .writer(itemWriter)
                .faultTolerant()
                //.skipLimit(2)
                //.skip(FlatFileParseException.class)
                .skipPolicy(fileVerificationSkipper)
                .build();
    }

    @Bean
    public FlatFileItemReader<Emars> itemReader(@Value("${input}") Resource resource) {
        FlatFileItemReader<Emars> flatFileItemReader = new FlatFileItemReader<>();
        flatFileItemReader.setResource(resource);
        flatFileItemReader.setName("CSV-Reader");
        flatFileItemReader.setLinesToSkip(1);
        flatFileItemReader.setLineMapper(lineMapper());
        return flatFileItemReader;
    }

    @Bean
    public LineMapper<Emars> lineMapper() {
        DefaultLineMapper<Emars> defaultLineMapper = new DefaultLineMapper<>();

        DelimitedLineTokenizer lineTokenizer = new DelimitedLineTokenizer();
        lineTokenizer.setDelimiter(",");
        lineTokenizer.setStrict(false);
        lineTokenizer.setNames(new String[] {"uploadLink", "bciPn", "description", "level", "type", "username", "bciPnParent", "serialNo", "computedRohsStatus", "justification", "bomUploadDate", "parentSerialNo", "site", "cpKey", "justificationForRohsParent", "comments"});
        defaultLineMapper.setLineTokenizer(lineTokenizer);

        BeanWrapperFieldSetMapper<Emars> fieldSetMapper = new BeanWrapperFieldSetMapper<>();
        fieldSetMapper.setTargetType(Emars.class);
        fieldSetMapper.setConversionService(DateConversionService());
        defaultLineMapper.setFieldSetMapper(fieldSetMapper);

        return defaultLineMapper;
    }

    @Bean
    public ConversionService DateConversionService() {
        DefaultConversionService conversionService = new DefaultConversionService();
        DefaultConversionService.addDefaultConverters(conversionService);
        conversionService.addConverter(new Converter<String, LocalDate>() {
            @Override
            public LocalDate convert(String text) {
                if(text == null || text.equalsIgnoreCase("NULL"))  //this condition can be refined as per need
                    return null;
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
                return LocalDate.parse(text, formatter);
            }
        });
        return conversionService;
    }
}

//https://stackoverflow.com/questions/29031155/spring-batch-skip-record-on-process/29034232
//The table batchdb.batch_step_execution has a lot of important details like skipped/filter counts
