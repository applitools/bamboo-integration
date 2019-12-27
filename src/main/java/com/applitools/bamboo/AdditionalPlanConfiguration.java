package com.applitools.bamboo;

import com.atlassian.bamboo.plan.Plan;
import com.atlassian.bamboo.plan.cache.ImmutablePlan;
import com.atlassian.bamboo.plan.configuration.MiscellaneousPlanConfigurationPlugin;
import com.atlassian.bamboo.template.TemplateRenderer;
import com.atlassian.bamboo.utils.error.ErrorCollection;
import com.atlassian.bamboo.utils.error.SimpleErrorCollection;
import com.atlassian.bamboo.v2.build.BaseBuildConfigurationAwarePlugin;
import com.atlassian.bamboo.ww2.actions.build.admin.create.BuildConfiguration;
import com.atlassian.plugin.spring.scanner.annotation.component.Scanned;
import com.atlassian.plugin.spring.scanner.annotation.imports.ComponentImport;
import com.opensymphony.xwork2.TextProvider;
import org.apache.commons.validator.routines.UrlValidator;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;


@Scanned
public class AdditionalPlanConfiguration extends BaseBuildConfigurationAwarePlugin implements MiscellaneousPlanConfigurationPlugin {

    private TextProvider textProvider;

    public static final String APPLITOOLS_API_KEY_FIELD = "custom.applitools.api_key";
    public static final String ENABLE_APPLITOOLS_SUPPORT_FIELD = "custom.applitools.support";
    public static final String APPLITOOLS_SERVER_URL_FIELD = "custom.applitools.server_url";
    public static final String APPLITOOLS_NOTIFY_BY_COMPLETION_FIELD = "custom.applitools.notify_by_completion";
    public static final String APPLITOOLS_API_KEY_ERROR_KEY = "applitools.api.key.error";
    public static final String APPLITOOLS_SERVER_URL_ERROR_KEY = "applitools.server.url.error";

    public AdditionalPlanConfiguration(@ComponentImport TemplateRenderer templateRenderer, @ComponentImport TextProvider textProvider) {
        super();
        setTemplateRenderer(templateRenderer);
        this.textProvider = textProvider;
    }

    public boolean isApplicableTo(ImmutablePlan plan) {
        // Only for plan type CHAIN (typeId is 1)
        return plan.getEntityType().getTypeId() == 1;
    }

    @Override
    protected void populateContextForEdit(@NotNull Map<String, Object> context, @NotNull BuildConfiguration buildConfiguration, @Nullable Plan plan) {
        super.populateContextForEdit(context, buildConfiguration, plan);
    }

    @Override
    public void addDefaultValues(@NotNull BuildConfiguration buildConfiguration) {
        buildConfiguration.getProjectConfig();
    }

    @Override
    @NotNull
    public ErrorCollection validate(@NotNull BuildConfiguration buildConfiguration) {
        String apiKey = buildConfiguration.getString(APPLITOOLS_API_KEY_FIELD);
        String serverUrl = buildConfiguration.getString(APPLITOOLS_SERVER_URL_FIELD);
        UrlValidator urlValidator = new UrlValidator();
        ErrorCollection errorCollection = new SimpleErrorCollection();
        if (StringUtils.isEmpty(apiKey)) {
            errorCollection.addError(APPLITOOLS_API_KEY_FIELD, textProvider.getText(APPLITOOLS_API_KEY_ERROR_KEY));
        }
        if (!urlValidator.isValid(serverUrl)) {
            errorCollection.addError(APPLITOOLS_SERVER_URL_FIELD, textProvider.getText(APPLITOOLS_SERVER_URL_ERROR_KEY));
        }
        return errorCollection;
    }
}
