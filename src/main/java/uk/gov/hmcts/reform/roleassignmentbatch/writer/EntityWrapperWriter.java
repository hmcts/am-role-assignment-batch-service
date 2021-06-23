package uk.gov.hmcts.reform.roleassignmentbatch.writer;

import java.util.Collections;
import java.util.List;

import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import uk.gov.hmcts.reform.roleassignmentbatch.entities.EntityWrapper;
import uk.gov.hmcts.reform.roleassignmentbatch.entities.HistoryEntity;
import uk.gov.hmcts.reform.roleassignmentbatch.entities.RequestEntity;

public class EntityWrapperWriter implements ItemWriter<EntityWrapper> {

    @Autowired
    private JdbcBatchItemWriter<HistoryEntity> historyWriter;
    @Autowired
    private JdbcBatchItemWriter<RequestEntity> requestEntityWriter;

    @Override
    public void write(List<? extends EntityWrapper> items) throws Exception {
        for (EntityWrapper item : items) {
            requestEntityWriter.write(Collections.singletonList(item.getRequestEntity()));
            historyWriter.write(Collections.singletonList(item.getHistoryEntity()));
        }
    }
}
