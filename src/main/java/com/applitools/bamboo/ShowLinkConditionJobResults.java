package com.applitools.bamboo;

import com.atlassian.bamboo.plan.PlanKey;
import com.atlassian.bamboo.plan.PlanKeys;
import com.atlassian.bamboo.plan.PlanManager;
import com.atlassian.plugin.spring.scanner.annotation.component.Scanned;
import com.atlassian.plugin.spring.scanner.annotation.imports.ComponentImport;

@Scanned
public class ShowLinkConditionJobResults extends ApplitoolsLinkCondition {

    public ShowLinkConditionJobResults(@ComponentImport PlanManager planManager) {
        super(planManager);
    }
    protected PlanKey getPlanKey(String planKey) {
        PlanKey jobKey = PlanKeys.getPlanKey(planKey);
        return PlanKeys.getChainKeyIfJobKey(jobKey);
    }
}
