package ru.rent.demo.datasource;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

public class MasterSlaveRoutingDataSource extends AbstractRoutingDataSource {

    private static final ThreadLocal<DbType> context = new ThreadLocal<>();

    public static void setDbType(DbType dbType) {
        context.set(dbType);
    }

    public static void clearDbType() {
        context.remove();
    }

    @Override
    protected Object determineCurrentLookupKey() {
        DbType dbType = context.get() == null ? DbType.MASTER : context.get();
        System.out.println("Используется БД: " + dbType + " Для потока: " + Thread.currentThread().getName());
        return dbType;
    }
}