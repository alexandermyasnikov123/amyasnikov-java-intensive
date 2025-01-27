package net.dunice.intensive.spring_boot.tasks;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

@Service
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class DummyService {

    public void printHeader(HttpServletRequest request) {
        System.out.println(request.getHeader(AddInfoHeaderFilter.REQUEST_INFO_HEADER));
    }
}
