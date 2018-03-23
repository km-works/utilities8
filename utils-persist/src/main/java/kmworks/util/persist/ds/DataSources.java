/*
 * Copyright (c) 2017 Christian P. Lerch, Vienna, Austria <christian.p.lerch@gmail.com>
 * All rights reserved.
 */
package kmworks.util.persist.ds;

import static com.google.common.base.Preconditions.*;
import com.google.common.collect.Maps;
import com.google.inject.Provider;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.concurrent.ConcurrentMap;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
//import javax.inject.Provider;
import javax.sql.DataSource;
import kmworks.util.config.PropertyMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by cpl on 21.06.2015.
 */
public final class DataSources {

    private static final String DEFAULT_DS_NAME = "_DEFAULT_";

    private static final DataSources INSTANCE = new DataSources();

    private final ConcurrentMap<String, DataSourceProxy> dataSources = Maps.newConcurrentMap();

    private DataSources() {}
    
    @Nullable
    public static Connection getConnection() {
        return getConnection(DEFAULT_DS_NAME);
    }

    @Nullable
    public static Connection getConnection(@Nonnull final String dsName) {
        final DataSource ds = getDataSource(dsName);
        if (ds != null) {
            try {
                return ds.getConnection();
            } catch (SQLException ex) {}
        }
        return null;
    }

    @Nullable
    public static DataSource getDataSource() {
        return getDataSource(DEFAULT_DS_NAME);
    }

    @Nullable
    public static DataSource getDataSource(@Nonnull final String dsName) {
        checkNotNull(dsName);
        return INSTANCE.dataSources.get(dsName);
    }

    @Nullable
    public static Provider<DataSource> getProvider() {
        return getProvider(DEFAULT_DS_NAME);
    }

    @Nullable
    public static Provider<DataSource> getProvider(@Nonnull final String dsName) {
        checkNotNull(dsName);
        return () -> INSTANCE.dataSources.get(dsName);
    }

    @Nullable
    public static PropertyMap getDataSourceProperties() {
        return getDataSourceProperties(DEFAULT_DS_NAME);
    }

    @Nullable
    public static PropertyMap getDataSourceProperties(@Nonnull final String dsName) {
        checkNotNull(dsName);
        DataSourceProxy dsp = INSTANCE.dataSources.get(dsName);
        return dsp == null ? null : dsp.getProperties();
    }

    public static void registerDefaultDataSource(
            @Nonnull final DataSource ds, 
            @Nullable final PropertyMap dsProperties) {
        checkNotNull(ds);
        registerDefaultDataSource(new DataSourceProxy(ds, dsProperties));
    }
    
    public static void registerDefaultDataSource(@Nonnull final DataSourceProxy dsp) {
        checkNotNull(dsp);
        registerNamedDataSource(DEFAULT_DS_NAME, dsp);
    }

    public static void registerNamedDataSource(
            @Nonnull final String dsName, 
            @Nonnull final DataSource ds,
            @Nullable final PropertyMap dsProperties) {
        checkNotNull(dsName);
        checkNotNull(ds);
        registerNamedDataSource(dsName, new DataSourceProxy(ds, dsProperties));
    }
    
    public static void registerNamedDataSource(
            @Nonnull final String dsName, 
            @Nonnull final DataSourceProxy dsp) {
        checkNotNull(dsName);
        checkNotNull(dsp);
        INSTANCE.dataSources.put(dsName, dsp);
    }
    
    private static final Class SELF = DataSources.class;
    private static final Logger LOG = LoggerFactory.getLogger(SELF);
}
