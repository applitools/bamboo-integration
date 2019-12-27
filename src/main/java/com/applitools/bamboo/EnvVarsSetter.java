package com.applitools.bamboo;

import com.atlassian.bamboo.build.CustomPreBuildAction;
import com.atlassian.bamboo.utils.error.ErrorCollection;
import com.atlassian.bamboo.utils.error.SimpleErrorCollection;
import com.atlassian.bamboo.v2.build.BuildContext;
import com.atlassian.bamboo.ww2.actions.build.admin.create.BuildConfiguration;
import com.atlassian.plugin.spring.scanner.annotation.component.Scanned;

import java.util.Map;

@Scanned
public class EnvVarsSetter implements CustomPreBuildAction {
    private static final String BATCH_ID = "APPLITOOLS_BATCH_ID";
    private static final String BATCH_NAME = "APPLITOOLS_BATCH_NAME";
    private static final String APPLITOOLS_API_KEY = "APPLITOOLS_API_KEY";
    private static final String APPLITOOLS_SERVER_URL = "APPLITOOLS_SERVER_URL";
    private static final String SEQUENCE_NAME = "APPLITOOLS_SEQUENCE_NAME";

    BuildContext buildContext, topLevelBuildContext;
    public ErrorCollection validate(BuildConfiguration buildConfiguration) {
        return new SimpleErrorCollection();
    }

    public void init(BuildContext buildContext) {
       this.buildContext = buildContext;
    }

    public BuildContext call() {
        ApplitoolsConfiguration applitoolsConfiguration = getApplitoolsConfiguration();
        if (applitoolsConfiguration != null) {
            setEnvVars(applitoolsConfiguration);
        }
        return buildContext;
    }

    private void setEnvVars(ApplitoolsConfiguration applitoolsConfiguration) {
        Map<String, String> customBuildData = getCustomBuildData();
        customBuildData.put(APPLITOOLS_API_KEY, applitoolsConfiguration.getApiKey());
        customBuildData.put(APPLITOOLS_SERVER_URL, applitoolsConfiguration.getServerUrl().toString());
        customBuildData.put(SEQUENCE_NAME, getTopLevelBuildContext().getDisplayName());

        customBuildData.put(
                BATCH_NAME,
                getTopLevelBuildContext().getDisplayName() + getTopLevelBuildContext().getBuildNumber()
        );

        customBuildData.put(
                BATCH_ID, PlanUidUtils.getBatchId(
                        getTopLevelBuildContext().getTypedPlanKey().getKey(),
                        getTopLevelBuildContext().getBuildNumber()
                )
        );
    }

    private BuildContext getTopLevelBuildContext() {
        if (topLevelBuildContext == null) {
            this.topLevelBuildContext = buildContext;
            do {
                this.topLevelBuildContext = this.topLevelBuildContext.getParentBuildContext();
            } while ( this.topLevelBuildContext.getParentBuildContext() != null);
        }
        return topLevelBuildContext;
    }

    private Map<String, String> getCustomBuildData() {
        return getTopLevelBuildContext().getCurrentResult().getCustomBuildData();
    }

    private Map<String, Object> getConfigObjects() {
        return getTopLevelBuildContext().getBuildDefinition().getConfigObjects();
    }

    private ApplitoolsConfiguration getApplitoolsConfiguration() {
        return (ApplitoolsConfiguration) getConfigObjects().get(ApplitoolsConfiguration.ApplitoolsConfigurationKey);
    }
}
