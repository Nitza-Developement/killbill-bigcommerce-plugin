package org.killbill.billing.plugin.bigcommerce.dao;

import java.sql.SQLException;
import java.sql.Connection;

import javax.sql.DataSource;
import org.jooq.impl.DSL;

import org.killbill.billing.plugin.bigcommerce.dao.gen.tables.records.BigcommercePluginConfigRecord;
import static org.killbill.billing.plugin.bigcommerce.dao.gen.tables.BigcommercePluginConfig.BIGCOMMERCE_PLUGIN_CONFIG;

import org.killbill.billing.plugin.dao.PluginDao;;

public class BigcommerceDao extends PluginDao {

    public BigcommerceDao(DataSource dataSource) throws SQLException {
        super(dataSource);
    }

    public void addConfig(final String kbTenantId, final String url) throws SQLException {

        BigcommercePluginConfigRecord record = retrieveConfig(kbTenantId);

        if (record == null) {
            createConfig(kbTenantId, url);
        } else {
            updateConfig(kbTenantId, url);
        }

    }

    public void createConfig(final String kbTenantId, final String url) throws SQLException {

        execute(dataSource.getConnection(),
                new WithConnectionCallback<Void>() {
                    @Override
                    public Void withConnection(final Connection conn) throws SQLException {
                        DSL.using(conn, dialect, settings)
                                .insertInto(BIGCOMMERCE_PLUGIN_CONFIG,
                                        BIGCOMMERCE_PLUGIN_CONFIG.TENANT_ID,
                                        BIGCOMMERCE_PLUGIN_CONFIG.URL)
                                .values(kbTenantId, url)
                                .execute();
                        return null;
                    }
                });
    }

    public BigcommercePluginConfigRecord retrieveConfig(final String knTenantId) throws SQLException {

        return execute(dataSource.getConnection(),
                new WithConnectionCallback<BigcommercePluginConfigRecord>() {
                    @Override
                    public BigcommercePluginConfigRecord withConnection(final Connection conn) throws SQLException {

                        return DSL.using(conn, dialect, settings)
                                .selectFrom(BIGCOMMERCE_PLUGIN_CONFIG)
                                .where(BIGCOMMERCE_PLUGIN_CONFIG.TENANT_ID.equal(knTenantId))
                                .fetchOne();
                    }
                });
    }

    public void updateConfig(final String kbTenantId, final String url) throws SQLException {

        execute(dataSource.getConnection(),
                new WithConnectionCallback<Void>() {
                    @Override
                    public Void withConnection(final Connection conn) throws SQLException {
                        DSL.using(conn, dialect, settings)
                                .update(BIGCOMMERCE_PLUGIN_CONFIG)
                                .set(BIGCOMMERCE_PLUGIN_CONFIG.URL, url)
                                .where(BIGCOMMERCE_PLUGIN_CONFIG.TENANT_ID.equal(kbTenantId))
                                .execute();
                        return null;
                    }
                });
    }

    public String getUrl(final String kbTenantId) throws SQLException {

        BigcommercePluginConfigRecord record = retrieveConfig(kbTenantId);

        if (record == null) {
            return null;
        }

        return record.getUrl();
    }
}
