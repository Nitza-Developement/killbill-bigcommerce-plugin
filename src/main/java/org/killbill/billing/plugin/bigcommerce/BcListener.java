/*
 * Copyright 2010-2014 Ning, Inc.
 * Copyright 2014-2020 Groupon, Inc
 * Copyright 2020-2020 Equinix, Inc
 * Copyright 2014-2020 The Billing Project, LLC
 *
 * The Billing Project licenses this file to you under the Apache License, version 2.0
 * (the "License"); you may not use this file except in compliance with the
 * License.  You may obtain a copy of the License at:
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */

package org.killbill.billing.plugin.bigcommerce;

import java.util.Properties;

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

    private final OSGIKillbillAPI osgiKillbillAPI;
    private final Properties configProperties;

    public BcListener(
            final OSGIKillbillAPI killbillAPI,
            Properties configProperties) {
        this.osgiKillbillAPI = killbillAPI;
        this.configProperties = configProperties;
    }

    private static final String defaultLocale = "en_US";

    @Override
    public void handleKillbillEvent(final ExtBusEvent killbillEvent) {

        logger.info("Received event {} for object id {} of type {}",
                killbillEvent.getEventType(),
                killbillEvent.getObjectId(),
                killbillEvent.getObjectType());

        final TenantContext context = new PluginTenantContext(killbillEvent.getAccountId(),
                killbillEvent.getTenantId());

        switch (killbillEvent.getEventType()) {
            //
            // Handle ACCOUNT_CREATION
            //
            case ACCOUNT_CREATION:
                try {

                    final Account account = osgiKillbillAPI.getAccountUserApi()
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
