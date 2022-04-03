package com.bithumb.assetbatch.job.withdrawal_transfer.step;

import com.bithumb.assetbatch.repository.MvpSettleRequestDataRepository;
import com.bithumb.assetbatch.repository.entity.MvpSettleRequestData;
import com.bithumb.assetbatch.repository.enums.SendFlag;
import com.bithumb.assetbatch.service.MvpService;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.item.*;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class SendSettleMessageAndUpdateStateStep implements ItemReader<MvpSettleRequestData>, ItemProcessor<MvpSettleRequestData, MvpSettleRequestData>, ItemWriter<MvpSettleRequestData>, StepExecutionListener {
    private final MvpSettleRequestDataRepository repository;
    private final MvpService service;
    private List<MvpSettleRequestData> requestList;

    @Override
    public void beforeStep(StepExecution stepExecution) {
        ExecutionContext executionContext = stepExecution.getJobExecution().getExecutionContext();
        requestList = new ArrayList<>((List<MvpSettleRequestData>) executionContext.get("requestList"));
    }

    @Override
    public MvpSettleRequestData read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
        try {
            return requestList.remove(0);
        } catch (IndexOutOfBoundsException e) {
            return null;
        }
    }

    @Override
    public MvpSettleRequestData process(MvpSettleRequestData item) throws Exception {
        String settleResponseCode = service.sendMessage(item.getMessage());
        if ("0000".equals(settleResponseCode)) {
            item.setSendFlag(SendFlag.DONE);
        } else {
            item.setSendFlag(SendFlag.FAILED);
        }
        return item;
    }

    @Override
    public void write(List<? extends MvpSettleRequestData> items) throws Exception {
        repository.saveAll(items);
    }

    @Override
    public ExitStatus afterStep(StepExecution stepExecution) {
        return ExitStatus.COMPLETED;
    }
}
