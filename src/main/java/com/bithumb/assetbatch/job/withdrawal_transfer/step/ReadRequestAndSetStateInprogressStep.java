package com.bithumb.assetbatch.job.withdrawal_transfer.step;

import com.bithumb.assetbatch.repository.MvpSettleRequestDataRepository;
import com.bithumb.assetbatch.repository.entity.MvpSettleRequestData;
import com.bithumb.assetbatch.repository.enums.SendFlag;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.support.ListItemReader;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class ReadRequestAndSetStateInprogressStep implements ItemProcessor<MvpSettleRequestData, MvpSettleRequestData>, ItemWriter<MvpSettleRequestData>, StepExecutionListener {
    private final MvpSettleRequestDataRepository repository;
    private List<MvpSettleRequestData> requestList = new ArrayList<>();

    /**
     * Custom Reader
     * @return
     */
    public ListItemReader<MvpSettleRequestData> getCustomReader() {
        return new ListItemReader<>(repository.findAllBySendFlag(SendFlag.OPENED));
    }

    /**
     * step 전에 수행.
     * @param stepExecution
     */
    @Override
    public void beforeStep(StepExecution stepExecution) {}

    @Override
    public MvpSettleRequestData process(MvpSettleRequestData item) throws Exception {
        item.setSendFlag(SendFlag.INPROGRESS);
        return item;
    }

    @Override
    public void write(List<? extends MvpSettleRequestData> items) throws Exception {
        requestList.addAll(items);
        repository.saveAll(items);
    }

    /**
     * step 종료 후에 수행.
     * jobExecutionContext 에 다음 스탭에서 사용할 데이터 추가
     * @param stepExecution
     * @return
     */
    @Override
    public ExitStatus afterStep(StepExecution stepExecution) {
        ExecutionContext executionContext = stepExecution.getJobExecution().getExecutionContext();
        executionContext.put("requestList", requestList);
        return ExitStatus.COMPLETED;
    }
}
