package com.devsuperior.user_request_sb.step;

import com.devsuperior.user_request_sb.dto.UserDTO;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
public class fetchUserDataAndStoreDBStepConfig {

    private final PlatformTransactionManager transactionManager;

    @Value("${chunkSize}")
    private int chunkSize;

    public fetchUserDataAndStoreDBStepConfig(PlatformTransactionManager transactionManager) {
        this.transactionManager = transactionManager;
    }

    @Bean
    public Step fetchUserDataAndStoreDBStep(ItemReader<UserDTO> fetchUserDataReader, ItemWriter<UserDTO> insertUserDataDBWriter, JobRepository jobRepository, DataSourceTransactionManager transactionManager) {
        return new StepBuilder("fetchUserDataAndStoreDBStep", jobRepository)
                .<UserDTO, UserDTO>chunk(chunkSize, transactionManager)
                .reader(fetchUserDataReader)
                .writer(insertUserDataDBWriter)
                .build();
    }

}
