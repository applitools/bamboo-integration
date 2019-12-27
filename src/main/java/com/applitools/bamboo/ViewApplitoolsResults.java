package com.applitools.bamboo;

import com.atlassian.bamboo.build.BuildDefinition;
import com.atlassian.bamboo.build.ChainResultsAction;
import com.atlassian.bamboo.plan.Plan;
import com.atlassian.bamboo.plan.PlanKey;
import com.atlassian.bamboo.plan.PlanKeys;
import com.atlassian.bamboo.plan.cache.ImmutableJob;
import com.atlassian.bamboo.plan.cache.ImmutablePlan;
import com.atlassian.bamboo.task.TaskDefinition;
import com.atlassian.plugin.spring.scanner.annotation.component.Scanned;
import com.atlassian.plugin.spring.scanner.annotation.imports.ComponentImport;
import org.apache.commons.lang3.StringUtils;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

@Scanned
public class ViewApplitoolsResults extends ChainResultsAction {
    //    public static final String APPLITOOLS_SERVER = "https://eyes.applitools.com";
    public String batchId, iframeUrl;

    public String execute() throws Exception {
        this.batchId = PlanUidUtils.getBatchId(getBuildKey(), getBuildNumber());
        ApplitoolsConfiguration applitoolsConfiguration = getApplitoolsConfiguration();

        if (applitoolsConfiguration != null) {
            this.iframeUrl = applitoolsConfiguration.getIframeUrl(batchId);
            return "success";
        } else {
            return "notask";
        }
    }

    private ApplitoolsConfiguration getApplitoolsConfiguration() {
        return (ApplitoolsConfiguration) getBuildDefinition().getConfigObjects().get(ApplitoolsConfiguration.ApplitoolsConfigurationKey);
    }

    private BuildDefinition getBuildDefinition() {
        return getCurrentPlan().getBuildDefinition();
    }

    private ImmutablePlan getCurrentPlan() {
        ImmutablePlan result = getResultsSummary().getImmutablePlan();
        PlanKey planKey;
        if (result.getEntityType().getTypeId() == 1) {
            planKey = result.getPlanKey();
        } else {
            planKey = PlanKeys.getChainKeyIfJobKey(result.getPlanKey());
        }

        if (planKey == null) {
            return result;
        } else {
            return planManager.getPlanByKey(planKey);
        }
    }
}
