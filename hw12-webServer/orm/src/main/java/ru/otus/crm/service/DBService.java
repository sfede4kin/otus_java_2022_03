package ru.otus.crm.service;

import org.hibernate.cfg.Configuration;
import ru.otus.core.repository.DataTemplateHibernate;
import ru.otus.core.repository.HibernateUtils;
import ru.otus.core.sessionmanager.TransactionManagerHibernate;
import ru.otus.crm.dbmigrations.MigrationsExecutorFlyway;
import ru.otus.crm.model.Address;
import ru.otus.crm.model.Client;
import ru.otus.crm.model.Phone;

public final class DBService {
    private static final String HIBERNATE_CFG_FILE = "hibernate.cfg.xml";
    private static final Configuration configuration = new Configuration().configure(HIBERNATE_CFG_FILE);
    private static final String dbUrl = configuration.getProperty("hibernate.connection.url");
    private static final String dbUserName = configuration.getProperty("hibernate.connection.username");
    private static final String dbPassword = configuration.getProperty("hibernate.connection.password");

    public static DBServiceClient initDBServiceClient(){
        new MigrationsExecutorFlyway(dbUrl, dbUserName, dbPassword).executeMigrations();
        var sessionFactory = HibernateUtils.buildSessionFactory(configuration, Client.class, Address.class, Phone.class);
        var transactionManager = new TransactionManagerHibernate(sessionFactory);
        var clientTemplate = new DataTemplateHibernate<>(Client.class);
        return new DbServiceClientImpl(transactionManager, clientTemplate);
    }
}
