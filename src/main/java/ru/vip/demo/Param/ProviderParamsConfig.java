package ru.vip.demo.Param;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "provider.params") // Передача группы параметров из application.yml
public class ProviderParamsConfig {

    // Передача группы параметров из application.yml
    private String TARGET_BEAN = "DataJpa"; // Definition param
    private int COUNT_METHOD= 1;
    private int[] LIST_BEAN = { 1, 7, 23 };
    private boolean ORIGINAL_STRING = false;
}
