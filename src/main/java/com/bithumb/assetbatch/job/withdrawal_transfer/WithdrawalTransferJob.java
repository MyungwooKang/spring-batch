package com.bithumb.assetbatch.job.withdrawal_transfer;

import com.bithumb.assetbatch.job.withdrawal_transfer.step.ReadRequestAndSetStateInprogressStep;
import com.bithumb.assetbatch.job.withdrawal_transfer.step.SendSettleMessageAndUpdateStateStep;
import com.bithumb.assetbatch.repository.MvpSettleRequestDataRepository;
import com.bithumb.assetbatch.repository.entity.MvpSettleRequestData;
import com.bithumb.assetbatch.repository.enums.SendFlag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
@RequiredArgsConstructor
@EnableBatchProcessing
public class WithdrawalTransferJob {
    private static final String JOB_NAME = "WithdrawalTransferJob";

    private final ReadRequestAndSetStateInprogressStep step1;
    private final SendSettleMessageAndUpdateStateStep step2;

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    private final MvpSettleRequestDataRepository repository; //TODO Remove .(테스트용)

    // TODO Remove
    private void setTestData() {
        for (int i = 0; i < 100; i++) {
            repository.save(MvpSettleRequestData.builder()
                                                .sendFlag(SendFlag.OPENED)
                                                .message("test" + i) // TODO 전문
                                                .build());
        }
    }

    @Bean(name = JOB_NAME)
    public Job WithdrawalTransferJob() throws Exception {
        setTestData(); //TODO Remove
        Job job = jobBuilderFactory.get("WithdrawalTransferJob")
                                   .incrementer(new RunIdIncrementer()) // TODO Remove
                                   .start(readAndSetStatusInprogress(null))
                                   .next(sendSettleMessageAndUpdateState())
                                   .build();

        return job;
    }


    @Bean(name = JOB_NAME + "_step1")
    @JobScope
    public Step readAndSetStatusInprogress(@Value("#{jobParameters[chunkSize]}") String chunkSize) throws Exception {
        System.out.println(chunkSize);
        return stepBuilderFactory.get("readAndSetStatusInprogress")
                                 .<MvpSettleRequestData, MvpSettleRequestData>chunk(Integer.parseInt(chunkSize))
                                 .reader(step1.getCustomReader())
                                 .processor(step1)
                                 .writer(step1)
                                 .build();
    }

    @Bean(name = JOB_NAME + "_step2")
    public Step sendSettleMessageAndUpdateState() throws Exception {
        return stepBuilderFactory.get("sendSettleMessageAndUpdateState")
                                 .<MvpSettleRequestData, MvpSettleRequestData>chunk(1)
                                 .reader(step2)
                                 .processor(step2)
                                 .writer(step2)
                                 .build();
    }
}