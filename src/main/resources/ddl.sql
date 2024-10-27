CREATE TABLE bigcommerce_plugin_config (
  tenant_id VARCHAR(255) NOT NULL,
  url VARCHAR(255) NOT NULL,
  PRIMARY KEY (tenant_id, url)
);
