/*
 * Copyright (c) 2017 Christian P. Lerch, Vienna, Austria <christian.p.lerch@gmail.com>
 * All rights reserved.
 */
package kmworks.util.persist.ds;

import static com.google.common.base.Preconditions.*;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.logging.Logger;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.sql.DataSource;
import kmworks.util.config.PropertyMap;

/**
 * Created by cpl on 24.06.2015.
 */
public final class DataSourceProxy implements DataSource {
    
    private final DataSource ds;
    private final PropertyMap dsProperties;
    
    public DataSourceProxy(@Nonnull final DataSource ds) {
        this(ds, null);
    }

    public DataSourceProxy(@Nonnull final DataSource ds, @Nullable final PropertyMap dsProperties) {
        checkNotNull(ds);
        this.ds = ds;
        this.dsProperties = dsProperties == null ? PropertyMap.of() : dsProperties;
    }
    
    public PropertyMap getProperties() {
        return dsProperties;
    }

    @Override
    public Connection getConnection() throws SQLException {
        return ds.getConnection();
    }

    @Override
    public Connection getConnection(@Nonnull final String username, @Nonnull final String password) throws SQLException {
        return ds.getConnection(username, password);
    }

    @Override
    public PrintWriter getLogWriter() throws SQLException {
        return ds.getLogWriter();
    }

    @Override
    public void setLogWriter(@Nonnull final PrintWriter out) throws SQLException {
        ds.setLogWriter(out);
    }

    @Override
    public int getLoginTimeout() throws SQLException {
        return ds.getLoginTimeout();
    }

    @Override
    public void setLoginTimeout(int seconds) throws SQLException {
        ds.setLoginTimeout(seconds);
    }

    @Override
    public Logger getParentLogger() throws SQLFeatureNotSupportedException {
        return ds.getParentLogger();
    }

    @Override
    public <T> T unwrap(@Nonnull final Class<T> iface) throws SQLException {
        return ds.unwrap(iface);
    }

    @Override
    public boolean isWrapperFor(@Nonnull final Class<?> iface) throws SQLException {
        return ds.isWrapperFor(iface);
    }

    @Override
    public String toString() {
        return ds.toString();
    }

}
