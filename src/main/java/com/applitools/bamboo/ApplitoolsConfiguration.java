package com.applitools.bamboo;

import java.net.URI;
import java.net.URISyntaxException;

public class ApplitoolsConfiguration {
    private static String TRUE = "true";
    public static String ApplitoolsConfigurationKey = "applitools";

    public static final String RESULT_PATH = "/app/batchesnoauth/";
    public static final String QUERY_STRING_BEFORE_BATCH_ID = "startInfoBatchId=";
    public static final String QUERY_STRING_AFTER_BATCH_ID = "&hideBatchList=true&intercom=false";
    public final static String BATCH_NOTIFICATION_PATH = "/api/sessions/batches/%s/close/bypointerid";

    private String apiKey;
    private URI serverUrl;
    private boolean notifyByCompletion;
    private String iframeUrlHost;

    public ApplitoolsConfiguration() {
        this.notifyByCompletion = false;
    }

    public String getApiKey() {
        return apiKey;
    }

    public URI getServerUrl() {
        return serverUrl;
    }

    private void setIframeUrlHost(URI serverUrl) {
        String serverUrlHost = serverUrl.getHost();
        if (serverUrlHost != null) {
            String iframeUrlHost = serverUrlHost.replaceAll("^(.*)api.", "$1.");
            this.iframeUrlHost = iframeUrlHost == null ? serverUrlHost : iframeUrlHost;
        }
    }

    public boolean getNotifyByCompletion() {
        return notifyByCompletion;
    }

    public void setApiKey(String value) {
        this.apiKey = value;
    }

    public void setServerUrl(URI value) {
        this.serverUrl = value;
        setIframeUrlHost(serverUrl);
    }

    public void setServerUrl(String value) {
        setServerUrl(URI.create(value));
    }

    public void setNotifyByCompletion(boolean value) {
        this.notifyByCompletion = value;
    }

    public void setNotifyByCompletion(String value) {
        if (value == null) {
            setNotifyByCompletion(false);
        } else {
            setNotifyByCompletion(TRUE.equalsIgnoreCase(value));
        }
    }

    public String getIframeUrl(String batchId) {
        URI result;
        String query = QUERY_STRING_BEFORE_BATCH_ID + batchId + QUERY_STRING_AFTER_BATCH_ID;
        try {
            result =
                    new URI(
                            getServerUrl().getScheme(),
                            getServerUrl().getUserInfo(),
                            iframeUrlHost,
                            getServerUrl().getPort(),
                            RESULT_PATH,
                            query,
                            getServerUrl().getFragment()
                    );
        } catch (URISyntaxException e) {
            result = getServerUrl();
        }
        return result.toString();
    }

    public String getBatchNotificationUrl(String batchId) throws URISyntaxException {
        String result;
        result = new URI(
                getServerUrl().getScheme(),
                getServerUrl().getUserInfo(),
                getServerUrl().getHost(),
                getServerUrl().getPort(),
                String.format(BATCH_NOTIFICATION_PATH, batchId),
                "apiKey=" + getApiKey(),
                getServerUrl().getFragment()
        ).toString();
        return result;
    }
}
