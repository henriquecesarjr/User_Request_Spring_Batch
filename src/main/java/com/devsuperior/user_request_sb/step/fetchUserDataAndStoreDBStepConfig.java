package com.devsuperior.user_request_sb.step;

import com.devsuperior.user_request_sb.dto.UserDTO;
import com.devsuperior.user_request_sb.entities.User;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
public class fetchUserDataAndStoreDBStepConfig {

    @Autowired
    @Qualifier("transactionManagerApp")
    private PlatformTransactionManager transactionManager;

    @Value("${chunkSize}")
    private int chunkSize;

//    public fetchUserDataAndStoreDBStepConfig(PlatformTransactionManager transactionManager) {
//        this.transactionManager = transactionManager;
//    }

    @Bean
    public Step fetchUserDataAndStoreDBStep(ItemReader<UserDTO> fetchUserDataReader,
                                            ItemProcessor<UserDTO, User> selectFieldsUserDataProcessor,
                                            ItemWriter<User> insertUserDataDBWriter,
                                            JobRepository jobRepository,
                                            DataSourceTransactionManager transactionManager) {
        return new StepBuilder("fetchUserDataAndStoreDBStep", jobRepository)
                .<UserDTO, User>chunk(chunkSize, transactionManager)
                .reader(fetchUserDataReader)
                .processor(selectFieldsUserDataProcessor)
                .writer(insertUserDataDBWriter)
                .build();
    }

}
