package com.applitools.bamboo;

import com.atlassian.bamboo.build.logger.BuildLogger;
import com.atlassian.bamboo.configuration.ConfigurationMap;
import com.atlassian.bamboo.process.ExternalProcessBuilder;
import com.atlassian.bamboo.process.ProcessService;
import com.atlassian.bamboo.task.*;
import com.atlassian.bamboo.v2.build.agent.capability.CapabilityContext;
import com.atlassian.plugin.spring.scanner.annotation.component.Scanned;
import com.atlassian.plugin.spring.scanner.annotation.imports.ComponentImport;
import com.atlassian.utils.process.ExternalProcess;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;

import javax.inject.Inject;
import java.util.Map;

@Scanned
public class ApplitoolsTaskRunner implements TaskType {
    private static final String BATCH_ID = "APPLITOOLS_BATCH_ID";
    private static final String BATCH_NAME = "APPLITOOLS_BATCH_NAME";
    private static final String APPLITOOLS_API_KEY = "APPLITOOLS_API_KEY";
    private static final String APPLITOOLS_SERVER_URL = "APPLITOOLS_SERVER_URL";
    private static final String SEQUENCE_NAME = "APPLITOOLS_SEQUENCE_NAME";


    @ComponentImport
    private final ProcessService processService;

    @ComponentImport
    private final CapabilityContext capabilityContext;

    @Inject
    public ApplitoolsTaskRunner(@NotNull final ProcessService processService, @NotNull final CapabilityContext capabilityContext) {
        this.processService = processService;
        this.capabilityContext = capabilityContext;
    }

    @Override
    public TaskResult execute(final TaskContext taskContext) throws TaskException {
        final TaskResultBuilder builder = TaskResultBuilder.newBuilder(taskContext).success();

        ConfigurationMap configMap = taskContext.getConfigurationMap();
        String apiKey = configMap.get(ApplitoolsTaskConfigurator.APPLITOOLS_API_KEY);
        String batchName = taskContext.getBuildContext().getDisplayName();
        String projectName = taskContext.getBuildContext().getProjectName();
        String serverUrl = StringUtils.defaultIfEmpty(configMap.get(ApplitoolsTaskConfigurator.APPLITOOLS_SERVER_URL), ViewApplitoolsResults.APPLITOOLS_SERVER);
        String batchId = PlanUidUtils.getBatchId(taskContext.getBuildContext().getTypedPlanKey().getKey(), taskContext.getBuildContext().getBuildNumber());
        Map<String, String> customBuildData = taskContext.getBuildContext().getParentBuildContext().getCurrentResult().getCustomBuildData();
        customBuildData.put(BATCH_ID, batchId);
        customBuildData.put(BATCH_NAME, batchName);
        customBuildData.put(APPLITOOLS_API_KEY, apiKey);
        customBuildData.put(APPLITOOLS_SERVER_URL, serverUrl);
        customBuildData.put(SEQUENCE_NAME, projectName);
        return builder.success().build();
    }
}
