package net.dunice.intensive.spring_boot.tasks;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SelfTransactionCallsService {
    @Lazy
    private final SelfTransactionCallsService self;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void doInTransaction() {
        System.out.println("doInTransaction");
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void doInNewTransaction() {
        self.doInTransaction();
    }
}
