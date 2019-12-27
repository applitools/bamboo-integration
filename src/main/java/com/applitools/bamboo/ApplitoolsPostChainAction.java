package com.applitools.bamboo;

import com.atlassian.bamboo.build.BuildLoggerManager;
import com.atlassian.bamboo.chains.Chain;
import com.atlassian.bamboo.chains.ChainExecution;
import com.atlassian.bamboo.chains.ChainResultsSummary;
import com.atlassian.bamboo.chains.plugins.PostChainAction;
import com.atlassian.plugin.spring.scanner.annotation.component.Scanned;
import com.atlassian.plugin.spring.scanner.annotation.imports.ComponentImport;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.DeleteMethod;
import org.apache.log4j.Logger;
import org.jetbrains.annotations.NotNull;

import java.net.URISyntaxException;

@Scanned
public class ApplitoolsPostChainAction implements PostChainAction {

    private final BuildLoggerManager buildLoggerManager;
    private Logger log = Logger.getLogger(ApplitoolsPostChainAction.class);

    public ApplitoolsPostChainAction(@ComponentImport BuildLoggerManager buildLoggerManager) {
        this.buildLoggerManager = buildLoggerManager;
    }

    @Override
    public void execute(@NotNull Chain chain, @NotNull ChainResultsSummary chainResultsSummary, @NotNull ChainExecution chainExecution) throws InterruptedException, Exception {
        log.info("Starting Applitools Post Chain action...");
        ApplitoolsConfiguration applitoolsConfiguration = (ApplitoolsConfiguration) chain.getBuildDefinition()
                .getConfigObjects()
                .get(ApplitoolsConfiguration.ApplitoolsConfigurationKey);
        if (applitoolsConfiguration != null && applitoolsConfiguration.getNotifyByCompletion()) {
            HttpClient httpClient = new HttpClient();
            try {
                DeleteMethod deleteRequest = new DeleteMethod(applitoolsConfiguration.getBatchNotificationUrl(getBatchId(chain, chainResultsSummary)));
                try {
                    int statusCode = httpClient.executeMethod(deleteRequest);
                    log.info("Delete batch is done with " + Integer.toString(statusCode) + " status");
                } finally {
                    deleteRequest.releaseConnection();
                }
            } catch (URISyntaxException e) {
                log.info("Delete batch is failed (invalid batchNotificationUrl)");
            }
        }
    }

    private String getBatchId(Chain chain, ChainResultsSummary chainResultsSummary) {
        String result;
        result = PlanUidUtils.getBatchId(chain.getPlanKey().getKey(), chainResultsSummary.getBuildNumber());
        log.info("BatchId for closeBatch call: " + result);
        return result;
    }
}
