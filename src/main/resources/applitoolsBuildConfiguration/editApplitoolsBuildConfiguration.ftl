[@ui.bambooSection titleKey="applitools.build.config.title"]
    [@ww.checkbox labelKey="applitools.support.label" name="custom.applitools.support" toggle='true'/]
    [@ui.bambooSection dependsOn="custom.applitools.support" titleKey="applitools.build.settings.title" showOn='true']
        [@ww.textfield labelKey="applitools.api.key.label" name="custom.applitools.api_key" required="true"/]
        [@ww.textfield labelKey="applitools.api.serverUrl.label" name="custom.applitools.server_url" required="false" required="true"/]
        [@ww.checkbox labelKey="applitools.notifyByCompletion.label" name="custom.applitools.notify_by_completion" toggle='true'/]
    [/@ui.bambooSection]
[/@ui.bambooSection]
