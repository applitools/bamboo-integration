package com.applitools.bamboo;

import com.atlassian.bamboo.plan.PlanKey;
import com.atlassian.bamboo.plan.PlanKeys;
import com.atlassian.bamboo.plan.PlanManager;
import com.atlassian.plugin.spring.scanner.annotation.component.Scanned;
import com.atlassian.plugin.spring.scanner.annotation.imports.ComponentImport;

@Scanned
public class ShowLinkConditionChainResults extends ApplitoolsLinkCondition {
    public ShowLinkConditionChainResults(@ComponentImport PlanManager planManager) {
        super(planManager);
    }

    protected PlanKey getPlanKey(String planKey) {
        return PlanKeys.getPlanKey(planKey);
    }
}
