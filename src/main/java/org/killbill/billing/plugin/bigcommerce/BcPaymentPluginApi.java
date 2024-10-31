package org.killbill.billing.plugin.bigcommerce;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.joda.time.DateTime;

import org.killbill.billing.osgi.libs.killbill.OSGIKillbillAPI;
import org.killbill.billing.catalog.api.Currency;
import org.killbill.billing.payment.api.PaymentMethodPlugin;
import org.killbill.billing.payment.api.PluginProperty;
import org.killbill.billing.payment.api.TransactionType;
import org.killbill.billing.payment.plugin.api.GatewayNotification;
import org.killbill.billing.payment.plugin.api.HostedPaymentPageFormDescriptor;
import org.killbill.billing.payment.plugin.api.PaymentMethodInfoPlugin;
import org.killbill.billing.payment.plugin.api.PaymentPluginApi;
import org.killbill.billing.payment.plugin.api.PaymentPluginApiException;
import org.killbill.billing.payment.plugin.api.PaymentTransactionInfoPlugin;
import org.killbill.billing.payment.plugin.api.PaymentPluginStatus;
import org.killbill.billing.account.api.Account;
import org.killbill.billing.account.api.AccountApiException;
import org.killbill.billing.invoice.api.Invoice;
import org.killbill.billing.invoice.api.InvoiceItem;
import org.killbill.billing.invoice.api.InvoiceApiException;
import org.killbill.billing.util.callcontext.CallContext;
import org.killbill.billing.util.callcontext.TenantContext;
import org.killbill.billing.util.entity.Pagination;

import org.killbill.billing.plugin.bigcommerce.dao.BigcommerceDao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BcPaymentPluginApi implements PaymentPluginApi {

    private static final Logger logger = LoggerFactory.getLogger(BcPaymentPluginApi.class);
    private OSGIKillbillAPI killbillAPI;
    private final BigcommerceDao dao;

    public BcPaymentPluginApi(
            final OSGIKillbillAPI killbillAPI,
            final BigcommerceDao dao) {
        this.killbillAPI = killbillAPI;
        this.dao = dao;

    }

    @Override
    public PaymentTransactionInfoPlugin purchasePayment(
            final UUID kbAccountId,
            final UUID kbPaymentId,
            final UUID kbTransactionId,
            final UUID kbPaymentMethodId,
            final BigDecimal amount,
            final Currency currency,
            final Iterable<PluginProperty> properties,
            final CallContext context) throws PaymentPluginApiException {

        logger.info("purchasePayment");
        logger.info("Account Id: " + kbAccountId);
        logger.info("Payment Id: " + kbPaymentId);
        logger.info("Transaction Id: " + kbTransactionId);
        logger.info("Payment Method Id: " + kbPaymentMethodId);
        logger.info("Amount: " + amount);
        logger.info("Currency: " + currency);
        logger.info("Properties: " + properties);

        PaymentTransactionInfoPlugin paymentTransactionInfoPlugin;

        try {
            final Account account = killbillAPI.getAccountUserApi().getAccountById(kbAccountId, context);

            // Get external key = bigcommerce customer id
            final Number externalKey = Integer.parseInt(account.getExternalKey());

            if (properties.iterator().hasNext()) {

                final PluginProperty prop = properties.iterator().next();

                UUID invoiceId = UUID.fromString(prop.getValue().toString());

                Invoice invoice = killbillAPI.getInvoiceUserApi().getInvoice(invoiceId, context);

                List<InvoiceItem> items = invoice.getInvoiceItems();

                List<Map<String, Object>> products = new ArrayList<>();

                for (final InvoiceItem invoiceItem : items) {

                    String productName = invoiceItem.getProductName();
                    String prettyPlanName = invoiceItem.getPrettyPlanName();

                    if (prettyPlanName != null && productName != null) {
                        Map<String, Object> product = getProduct(productName, prettyPlanName);
                        products.add(product);
                    }

                }

                final Map<String, Object> data = new HashMap<String, Object>();

                data.put("customer_id", externalKey);
                data.put("products", products);

                logger.info("DATA:" + data.toString());

                String url;
                try {
                    url = dao.getUrl(context.getTenantId().toString());
                } catch (SQLException e) {
                    throw new InvoiceApiException(null, 0, e.getMessage());

                }

                final ApiClient apiClient = new ApiClient(url);
                final Integer status = apiClient.pay(data);

                if (status == 200) {
                    paymentTransactionInfoPlugin = new BcPaymentTransactionInfoPlugin(
                            kbPaymentId,
                            kbTransactionId,
                            TransactionType.PURCHASE,
                            amount,
                            currency,
                            PaymentPluginStatus.PROCESSED,
                            null,
                            null,
                            null,
                            null,
                            new DateTime(),
                            null,
                            null);
                } else {
                    throw new InvoiceApiException(null, 0, "Payment of invoice failed in flask api");
                }

            } else
                throw new InvoiceApiException(null, 0, "Invoice id not found");

        } catch (AccountApiException | InvoiceApiException e) {

            paymentTransactionInfoPlugin = new BcPaymentTransactionInfoPlugin(
                    kbPaymentId,
                    kbTransactionId,
                    TransactionType.PURCHASE,
                    amount,
                    currency,
                    PaymentPluginStatus.ERROR,
                    e.getMessage(),
                    String.valueOf(e.getCode()),
                    null,
                    null,
                    new DateTime(),
                    null,
                    null);
        }

        return paymentTransactionInfoPlugin;
    }

    public Map<String, Object> getProduct(final String productName, final String prettyPlanName) {

        Integer quantity = 1;
        Integer product = Integer.parseInt(productName.split("product-")[1]);

        if (prettyPlanName.contains("BIANNUAL")) {
            quantity = 6;
        } else if (prettyPlanName.contains("QUARTERLY")) {
            quantity = 3;
        }

        Map<String, Object> data = new HashMap<String, Object>();
        data.put("product_id", product);
        data.put("quantity", quantity);

        return data;
    }

    @Override
    public void addPaymentMethod(final UUID kbAccountId, final UUID kbPaymentMethodId,
            final PaymentMethodPlugin paymentMethodProps, final boolean setDefault,
            final Iterable<PluginProperty> properties, final CallContext context) throws PaymentPluginApiException {

        logger.info("Account Id: " + kbAccountId);
        logger.info("Payment Method Id: " + kbPaymentMethodId);
        logger.info("Payment Method: " + paymentMethodProps);
        logger.info("Set Default: " + setDefault);
        logger.info("Properties: " + properties);

    }

    @Override
    public PaymentTransactionInfoPlugin authorizePayment(final UUID kbAccountId, final UUID kbPaymentId,
            final UUID kbTransactionId, final UUID kbPaymentMethodId, final BigDecimal amount, final Currency currency,
            final Iterable<PluginProperty> properties, final CallContext context) throws PaymentPluginApiException {

        return null;
    }

    @Override
    public PaymentTransactionInfoPlugin capturePayment(final UUID kbAccountId, final UUID kbPaymentId,
            final UUID kbTransactionId, final UUID kbPaymentMethodId, final BigDecimal amount, final Currency currency,
            final Iterable<PluginProperty> properties, final CallContext context) throws PaymentPluginApiException {
        return null;
    }

    @Override
    public PaymentTransactionInfoPlugin voidPayment(final UUID kbAccountId, final UUID kbPaymentId,
            final UUID kbTransactionId, final UUID kbPaymentMethodId, final Iterable<PluginProperty> properties,
            final CallContext context) throws PaymentPluginApiException {
        return null;
    }

    @Override
    public PaymentTransactionInfoPlugin creditPayment(final UUID kbAccountId, final UUID kbPaymentId,
            final UUID kbTransactionId, final UUID kbPaymentMethodId, final BigDecimal amount, final Currency currency,
            final Iterable<PluginProperty> properties, final CallContext context) throws PaymentPluginApiException {
        return null;
    }

    @Override
    public PaymentTransactionInfoPlugin refundPayment(final UUID kbAccountId, final UUID kbPaymentId,
            final UUID kbTransactionId, final UUID kbPaymentMethodId, final BigDecimal amount, final Currency currency,
            final Iterable<PluginProperty> properties, final CallContext context) throws PaymentPluginApiException {
        return null;
    }

    @Override
    public List<PaymentTransactionInfoPlugin> getPaymentInfo(final UUID kbAccountId, final UUID kbPaymentId,
            final Iterable<PluginProperty> properties, final TenantContext context) throws PaymentPluginApiException {
        return null;
    }

    @Override
    public Pagination<PaymentTransactionInfoPlugin> searchPayments(final String searchKey, final Long offset,
            final Long limit, final Iterable<PluginProperty> properties, final TenantContext context)
            throws PaymentPluginApiException {
        return null;
    }

    @Override
    public void deletePaymentMethod(final UUID kbAccountId, final UUID kbPaymentMethodId,
            final Iterable<PluginProperty> properties, final CallContext context) throws PaymentPluginApiException {

    }

    @Override
    public PaymentMethodPlugin getPaymentMethodDetail(final UUID kbAccountId, final UUID kbPaymentMethodId,
            final Iterable<PluginProperty> properties, final TenantContext context) throws PaymentPluginApiException {
        return null;
    }

    @Override
    public void setDefaultPaymentMethod(final UUID kbAccountId, final UUID kbPaymentMethodId,
            final Iterable<PluginProperty> properties, final CallContext context) throws PaymentPluginApiException {

    }

    @Override
    public List<PaymentMethodInfoPlugin> getPaymentMethods(final UUID kbAccountId, final boolean refreshFromGateway,
            final Iterable<PluginProperty> properties, final CallContext context) throws PaymentPluginApiException {
        return null;
    }

    @Override
    public Pagination<PaymentMethodPlugin> searchPaymentMethods(final String searchKey, final Long offset,
            final Long limit, final Iterable<PluginProperty> properties, final TenantContext context)
            throws PaymentPluginApiException {
        return null;
    }

    @Override
    public void resetPaymentMethods(final UUID kbAccountId, final List<PaymentMethodInfoPlugin> paymentMethods,
            final Iterable<PluginProperty> properties, final CallContext context) throws PaymentPluginApiException {

    }

    @Override
    public HostedPaymentPageFormDescriptor buildFormDescriptor(final UUID kbAccountId,
            final Iterable<PluginProperty> customFields, final Iterable<PluginProperty> properties,
            final CallContext context) throws PaymentPluginApiException {
        return null;
    }

    @Override
    public GatewayNotification processNotification(final String notification, final Iterable<PluginProperty> properties,
            final CallContext context) throws PaymentPluginApiException {
        return null;
    }
}
