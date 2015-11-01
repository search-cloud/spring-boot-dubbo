package org.mvnsearch.spring.boot.dubbo;

import com.alibaba.dubbo.config.spring.schema.DubboBeanDefinitionParser;
import com.alibaba.dubbo.rpc.service.EchoService;
import org.springframework.beans.BeansException;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.util.Map;

/**
 * dubbo health indicator
 *
 * @author linux_china
 */
public class DubboHealthIndicator implements HealthIndicator, ApplicationContextAware {
    private ApplicationContext applicationContext;

    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    public Health health() {
        if (!DubboBeanDefinitionParser.referenceBeanList.isEmpty()) {
            try {
                for (Map.Entry<String, String> entry : DubboBeanDefinitionParser.referenceBeanList.entrySet()) {
                    EchoService echoService = (EchoService) applicationContext.getBean(entry.getKey());
                    echoService.$echo("Hello");
                }
            } catch (Exception e) {
                return Health.down().withDetail("Dubbo", e.getMessage()).build();
            }
        }
        return Health.up().build();
    }
}