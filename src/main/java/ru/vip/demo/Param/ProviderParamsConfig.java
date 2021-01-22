package ru.vip.demo.Param;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "provider.params") // Передача группы параметров из application.yml
public class ProviderParamsConfig {

    // Передача группы параметров из application.yml
    private String TARGET_BEAN; // = "DataJpa";
    private int COUNT_METHOD; // = 1;
    private int[] LIST_BEAN; // = { 1, 7, 23 };
    private boolean ORIGINAL_STRING; // = false;

    public String getTARGET_BEAN() {
        return TARGET_BEAN;
    }

    public ProviderParamsConfig setTARGET_BEAN(String TARGET_BEAN) {
        this.TARGET_BEAN = TARGET_BEAN;
        return this;
    }

    public int getCOUNT_METHOD() {
        return COUNT_METHOD;
    }

    public ProviderParamsConfig setCOUNT_METHOD(int COUNT_METHOD) {
        this.COUNT_METHOD = COUNT_METHOD;
        return this;
    }

    public int[] getLIST_BEAN() {
        return LIST_BEAN;
    }

    public ProviderParamsConfig setLIST_BEAN(int[] LIST_BEAN) {
        this.LIST_BEAN = LIST_BEAN;
        return this;
    }

    public boolean isORIGINAL_STRING() {
        return ORIGINAL_STRING;
    }

    public ProviderParamsConfig setORIGINAL_STRING(boolean ORIGINAL_STRING) {
        this.ORIGINAL_STRING = ORIGINAL_STRING;
        return this;
    }
}
