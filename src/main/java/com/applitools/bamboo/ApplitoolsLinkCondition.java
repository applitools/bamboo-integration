package com.applitools.bamboo;

import com.atlassian.bamboo.plan.Plan;
import com.atlassian.bamboo.plan.PlanKey;
import com.atlassian.bamboo.plan.PlanKeys;
import com.atlassian.bamboo.plan.PlanManager;
import com.atlassian.plugin.spring.scanner.annotation.component.Scanned;
import com.atlassian.plugin.spring.scanner.annotation.imports.ComponentImport;

import javax.inject.Inject;
import java.util.Map;

public abstract class ApplitoolsLinkCondition implements com.atlassian.plugin.web.Condition{
    private PlanManager planManager;
    private static String PLAN_KEY = "planKey";
    public ApplitoolsLinkCondition(PlanManager planManager) {
        this.planManager = planManager;
    }

    @Override
    public void init(Map<String, String> map) {
    }

    @Override
    public boolean shouldDisplay(Map<String, Object> map) {
        Map<String, Object> configObjects = getConfigObjects((String)map.get(PLAN_KEY));
        return !(configObjects.get(ApplitoolsConfiguration.ApplitoolsConfigurationKey) == null);
    }

    private Map<String, Object> getConfigObjects(String planKey) {
        return getPlan(planKey).getBuildDefinition().getConfigObjects();
    }

    abstract protected PlanKey getPlanKey(String planKey);

    private Plan getPlan(String planKey) {
        return getPlanManager().getPlanByKey(getPlanKey(planKey));
    }

    public PlanManager getPlanManager() {
        return planManager;
    }
}
