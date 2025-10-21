package ru.rent.demo.datasource.routing;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import ru.rent.demo.datasource.DbType;
import ru.rent.demo.datasource.MasterSlaveRoutingDataSource;

@Aspect
@Component
public class ReadOnlyInterceptor {

    @Around("@annotation(ReadOnlyRepository)")
    public Object proceed(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        try {
            MasterSlaveRoutingDataSource.setDbType(DbType.SLAVE);
            return proceedingJoinPoint.proceed();
        } finally {
            MasterSlaveRoutingDataSource.clearDbType();
        }
    }
}