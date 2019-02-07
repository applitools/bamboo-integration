package com.applitools.bamboo;

import com.atlassian.bamboo.collections.ActionParametersMap;
import com.atlassian.bamboo.task.AbstractTaskConfigurator;
import com.atlassian.bamboo.task.TaskDefinition;
import com.atlassian.bamboo.task.TaskRequirementSupport;
import com.atlassian.bamboo.utils.error.ErrorCollection;
import com.atlassian.bamboo.v2.build.agent.capability.CapabilityContext;
import com.atlassian.bamboo.v2.build.agent.capability.Requirement;
import com.atlassian.bamboo.ww2.actions.build.admin.create.UIConfigSupport;
import com.atlassian.plugin.spring.scanner.annotation.component.Scanned;
import com.atlassian.plugin.spring.scanner.annotation.imports.ComponentImport;
import com.opensymphony.xwork2.TextProvider;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import javax.inject.Inject;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;


@Scanned
public class ApplitoolsTaskConfigurator extends AbstractTaskConfigurator implements TaskRequirementSupport {
    public static final String APPLITOOLS_API_KEY = "APPLITOOLS_API_KEY";
    public static final String APPLITOOLS_SERVER_URL = "APPLITOOLS_SERVER_URL";
    public static final String APPLITOOLS_API_KEY_ERROR_KEY = "applitools.api.key.error";

    @ComponentImport
    private UIConfigSupport uiConfigBean;

    @ComponentImport
    private CapabilityContext capabilityContext;

    @ComponentImport
    private TextProvider textProvider;

    @Inject
    public ApplitoolsTaskConfigurator(@NotNull UIConfigSupport uiConfigSupport, @NotNull final CapabilityContext capabilityContext, @NotNull TextProvider textProvider) {
        super();
        this.uiConfigBean = uiConfigSupport;
        this.capabilityContext = capabilityContext;
        this.textProvider = textProvider;
    }

    public Map<String, String> generateTaskConfigMap(@NotNull final ActionParametersMap params, @Nullable final TaskDefinition previousTaskDefinition) {
        Map<String, String> result = super.generateTaskConfigMap(params, previousTaskDefinition);
        String applitoolsApiKey, applitoolsServerUrl;

        applitoolsApiKey = params.getString(APPLITOOLS_API_KEY);
        applitoolsServerUrl = params.getString(APPLITOOLS_SERVER_URL);

        result.put(APPLITOOLS_API_KEY, applitoolsApiKey);
        result.put(APPLITOOLS_SERVER_URL, applitoolsServerUrl);
        return result;
    }

    @Override
    public void populateContextForEdit(@NotNull final Map<String, Object> context, @NotNull final TaskDefinition taskDefinition) {
        super.populateContextForEdit(context, taskDefinition);
        context.put(APPLITOOLS_API_KEY, taskDefinition.getConfiguration().get(APPLITOOLS_API_KEY));
        context.put(APPLITOOLS_SERVER_URL, taskDefinition.getConfiguration().get(APPLITOOLS_SERVER_URL));
    }

    @Override
    public void populateContextForCreate(@NotNull final Map<String, Object> context) {
        super.populateContextForCreate(context);
    }

    public void validate(@NotNull final ActionParametersMap params, @NotNull final ErrorCollection errorCollection) {
        String apiKey = params.getString(APPLITOOLS_API_KEY);
        super.validate(params, errorCollection);
        errorIfEmpty(APPLITOOLS_API_KEY, APPLITOOLS_API_KEY_ERROR_KEY, apiKey, errorCollection);
    }


    private void errorIfEmpty(String key, String errorKey, String value, ErrorCollection errorCollection) {
        if (StringUtils.isEmpty(value)) {
            errorCollection.addError(key, textProvider.getText(errorKey));
        }
    }


    @NotNull
    @Override
    public Set<Requirement> calculateRequirements(@NotNull TaskDefinition taskDefinition) {
      Set<Requirement> result = new HashSet<Requirement>();
//      Requirement commandRequirement = new RequirementImpl(
//              CAPABILITY_KEY_PREFIX + taskDefinition.getConfiguration().get(SELECTED_EXECUTABLE),
//              true, ".*"
//      );
//      result.add(commandRequirement);
      return result;
    }
}
