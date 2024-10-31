package org.killbill.billing.plugin.bigcommerce;

import java.sql.SQLException;
import java.util.Optional;
import javax.inject.Named;
import javax.inject.Singleton;

import org.jooby.mvc.GET;
import org.jooby.mvc.Local;
import org.jooby.mvc.Path;
import org.jooby.Result;
import org.jooby.Results;
import org.killbill.billing.tenant.api.Tenant;
import org.killbill.billing.osgi.libs.killbill.OSGIKillbillDataSource;
import org.killbill.billing.plugin.bigcommerce.dao.BigcommerceDao;
import org.jooq.exception.DataAccessException;

import com.google.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Singleton
@Path("/")
public class BcServlet {

    private static final Logger logger = LoggerFactory.getLogger(BcServlet.class);
    private OSGIKillbillDataSource dataSource;

    @Inject
    public BcServlet(OSGIKillbillDataSource dataSource) {
        this.dataSource = dataSource;
    }

    /**
     * Kill Bill automatically injects Tenant object in this method when this end
     * point is accessed with the X-Killbill-ApiKey and X-Killbill-ApiSecret headers
     * 
     * @param tenant
     */
    @GET
    public Result get(
            @Local @Named("killbill_tenant") final Optional<Tenant> tenant,
            @Named("url") final Optional<String> url) {

        if (tenant != null && tenant.isPresent() && url != null && url.isPresent()) {

            try {

                BigcommerceDao bigcommerceDao = new BigcommerceDao(dataSource.getDataSource());
                bigcommerceDao.addConfig(tenant.get().getId().toString(), url.get());

            } catch (SQLException e) {

                logger.error(e.getMessage());
                return Results.with("Error to add config", 500);

            } catch (DataAccessException e) {
                logger.error(e.getMessage());
                return Results.with("Create table in killbill database with the following script https://github.com/Nitza-Developement/killbill-bigcommerce-plugin/blob/main/src/main/resources/ddl.sql", 500);
            }

            return Results.ok("Configured URL: " + url.get());

        }

        return Results.noContent();

    }

}