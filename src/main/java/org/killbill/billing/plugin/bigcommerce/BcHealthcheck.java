package org.killbill.billing.plugin.bigcommerce;

import java.util.Map;

import javax.annotation.Nullable;

import org.killbill.billing.osgi.api.Healthcheck;
import org.killbill.billing.tenant.api.Tenant;

public class BcHealthcheck implements Healthcheck {

    @Override
    public HealthStatus getHealthStatus(@Nullable final Tenant tenant, @Nullable final Map properties) {
        return HealthStatus.healthy();
    }
}
