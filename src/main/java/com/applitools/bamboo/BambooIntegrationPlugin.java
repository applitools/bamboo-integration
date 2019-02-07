package com.applitools.bamboo;

import com.atlassian.plugin.spring.scanner.annotation.export.ExportAsService;
import com.atlassian.plugin.spring.scanner.annotation.imports.ComponentImport;
import com.atlassian.sal.api.ApplicationProperties;

import javax.inject.Inject;
import javax.inject.Named;

@ExportAsService({BambooIntegrationPlugin.class})
@Named("BambooIntegrationPlugin")
public class BambooIntegrationPlugin {
    @ComponentImport
    private final ApplicationProperties applicationProperties;

    @Inject
    public BambooIntegrationPlugin(final ApplicationProperties applicationProperties) {
        this.applicationProperties = applicationProperties;
    }

    public String getName() {
        if (null != applicationProperties) {
            return "ApplitoolsComponent:" + applicationProperties.getDisplayName();
        }

        return "Applitools Bamboo integration plugin";
    }
}