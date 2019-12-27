package com.applitools.bamboo;

import com.atlassian.bamboo.build.BuildDefinition;
import com.atlassian.bamboo.plugin.module.ext.CustomBuildDefinitionTransformer;
import com.atlassian.plugin.spring.scanner.annotation.component.Scanned;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

@Scanned
public class AdditionalPlanConfigurationTransformer implements CustomBuildDefinitionTransformer {
    private static String TRUE = "true";
    @Override
    public void transformBuildDefinition(@NotNull Map<String, Object> configObject, @NotNull Map<String, String> parameters, @NotNull BuildDefinition buildDefinition) {
        configObject.remove(ApplitoolsConfiguration.ApplitoolsConfigurationKey);
        if (parameters.get(AdditionalPlanConfiguration.ENABLE_APPLITOOLS_SUPPORT_FIELD) != null) {
            if (parameters.get(AdditionalPlanConfiguration.ENABLE_APPLITOOLS_SUPPORT_FIELD).equalsIgnoreCase(TRUE)) {
                ApplitoolsConfiguration applitoolsConfiguration = new ApplitoolsConfiguration();
                applitoolsConfiguration.setApiKey(parameters.get(AdditionalPlanConfiguration.APPLITOOLS_API_KEY_FIELD));
                applitoolsConfiguration.setNotifyByCompletion(parameters.get(AdditionalPlanConfiguration.APPLITOOLS_NOTIFY_BY_COMPLETION_FIELD));
                applitoolsConfiguration.setServerUrl(parameters.get(AdditionalPlanConfiguration.APPLITOOLS_SERVER_URL_FIELD));
                configObject.put(ApplitoolsConfiguration.ApplitoolsConfigurationKey, applitoolsConfiguration);
            }
        }
    }
}
