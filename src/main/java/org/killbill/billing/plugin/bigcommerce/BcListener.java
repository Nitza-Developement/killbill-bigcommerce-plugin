package org.killbill.billing.plugin.bigcommerce;

import org.killbill.billing.account.api.Account;
import org.killbill.billing.account.api.AccountApiException;
import org.killbill.billing.notification.plugin.api.ExtBusEvent;
import org.killbill.billing.osgi.libs.killbill.OSGIKillbillAPI;
import org.killbill.billing.osgi.libs.killbill.OSGIKillbillEventDispatcher;
import org.killbill.billing.plugin.api.PluginTenantContext;
import org.killbill.billing.util.callcontext.TenantContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BcListener implements OSGIKillbillEventDispatcher.OSGIKillbillEventHandler {

    private static final Logger logger = LoggerFactory.getLogger(BcListener.class);

    private final OSGIKillbillAPI killbillAPI;

    public BcListener(final OSGIKillbillAPI killbillAPI) {
        this.killbillAPI = killbillAPI;
    }

    @Override
    public void handleKillbillEvent(final ExtBusEvent killbillEvent) {

        final TenantContext context = new PluginTenantContext(
                killbillEvent.getAccountId(),
                killbillEvent.getTenantId());

        switch (killbillEvent.getEventType()) {

            case ACCOUNT_CREATION:
                try {

                    final Account account = killbillAPI.getAccountUserApi()
                            .getAccountById(killbillEvent.getAccountId(), context);

                    logger.info("Account information: " + account);

                } catch (final AccountApiException e) {

                    logger.warn("Unable to find account", e);
                }
                break;

            default:
                break;

        }
    }
}
