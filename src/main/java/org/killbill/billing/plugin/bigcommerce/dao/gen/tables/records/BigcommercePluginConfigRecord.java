/*
 * This file is generated by jOOQ.
 */
package org.killbill.billing.plugin.bigcommerce.dao.gen.tables.records;


import org.jooq.Field;
import org.jooq.Record2;
import org.jooq.Row2;
import org.jooq.impl.UpdatableRecordImpl;
import org.killbill.billing.plugin.bigcommerce.dao.gen.tables.BigcommercePluginConfig;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class BigcommercePluginConfigRecord extends UpdatableRecordImpl<BigcommercePluginConfigRecord> implements Record2<String, String> {

    private static final long serialVersionUID = 1L;

    /**
     * Setter for <code>killbill.bigcommerce_plugin_config.tenant_id</code>.
     */
    public void setTenantId(String value) {
        set(0, value);
    }

    /**
     * Getter for <code>killbill.bigcommerce_plugin_config.tenant_id</code>.
     */
    public String getTenantId() {
        return (String) get(0);
    }

    /**
     * Setter for <code>killbill.bigcommerce_plugin_config.url</code>.
     */
    public void setUrl(String value) {
        set(1, value);
    }

    /**
     * Getter for <code>killbill.bigcommerce_plugin_config.url</code>.
     */
    public String getUrl() {
        return (String) get(1);
    }

    // -------------------------------------------------------------------------
    // Primary key information
    // -------------------------------------------------------------------------

    @Override
    public Record2<String, String> key() {
        return (Record2) super.key();
    }

    // -------------------------------------------------------------------------
    // Record2 type implementation
    // -------------------------------------------------------------------------

    @Override
    public Row2<String, String> fieldsRow() {
        return (Row2) super.fieldsRow();
    }

    @Override
    public Row2<String, String> valuesRow() {
        return (Row2) super.valuesRow();
    }

    @Override
    public Field<String> field1() {
        return BigcommercePluginConfig.BIGCOMMERCE_PLUGIN_CONFIG.TENANT_ID;
    }

    @Override
    public Field<String> field2() {
        return BigcommercePluginConfig.BIGCOMMERCE_PLUGIN_CONFIG.URL;
    }

    @Override
    public String component1() {
        return getTenantId();
    }

    @Override
    public String component2() {
        return getUrl();
    }

    @Override
    public String value1() {
        return getTenantId();
    }

    @Override
    public String value2() {
        return getUrl();
    }

    @Override
    public BigcommercePluginConfigRecord value1(String value) {
        setTenantId(value);
        return this;
    }

    @Override
    public BigcommercePluginConfigRecord value2(String value) {
        setUrl(value);
        return this;
    }

    @Override
    public BigcommercePluginConfigRecord values(String value1, String value2) {
        value1(value1);
        value2(value2);
        return this;
    }

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    /**
     * Create a detached BigcommercePluginConfigRecord
     */
    public BigcommercePluginConfigRecord() {
        super(BigcommercePluginConfig.BIGCOMMERCE_PLUGIN_CONFIG);
    }

    /**
     * Create a detached, initialised BigcommercePluginConfigRecord
     */
    public BigcommercePluginConfigRecord(String tenantId, String url) {
        super(BigcommercePluginConfig.BIGCOMMERCE_PLUGIN_CONFIG);

        setTenantId(tenantId);
        setUrl(url);
    }
}