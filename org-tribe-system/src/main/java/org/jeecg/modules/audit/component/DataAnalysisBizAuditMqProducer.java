package org.jeecg.modules.audit.component;

import com.fasterxml.jackson.databind.ObjectMapper;
import javax.annotation.PreDestroy;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.common.message.Message;
import org.jeecg.modules.audit.vo.DataAnalysisBizAuditPayload;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
public class DataAnalysisBizAuditMqProducer implements InitializingBean {

    private static final Logger logger = LoggerFactory.getLogger(DataAnalysisBizAuditMqProducer.class);

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Value("${gk-nexus.oauth.audit-rocketmq-enabled:true}")
    private boolean enabled;

    @Value("${gk-nexus.oauth.audit-rocketmq-namesrv-addr:}")
    private String namesrvAddr;

    @Value("${gk-nexus.oauth.audit-rocketmq-producer-group:gk-data-analysis-audit-producer}")
    private String producerGroup;

    @Value("${gk-nexus.oauth.audit-rocketmq-topic:audit-log-topic}")
    private String topic;

    @Value("${gk-nexus.oauth.audit-rocketmq-tag:audit-log}")
    private String tag;

    private DefaultMQProducer producer;

    @Override
    public void afterPropertiesSet() throws Exception {
        if (!enabled) {
            logger.info("Data analysis business audit RocketMQ producer is disabled");
            return;
        }
        if (!StringUtils.hasText(namesrvAddr)) {
            logger.warn("Data analysis business audit RocketMQ producer has no namesrv address configured");
            return;
        }
        DefaultMQProducer mqProducer = new DefaultMQProducer(producerGroup);
        mqProducer.setNamesrvAddr(namesrvAddr);
        mqProducer.start();
        producer = mqProducer;
        logger.info("Started data analysis business audit RocketMQ producer, group={}, topic={}, tag={}",
            producerGroup, topic, tag);
    }

    public void send(DataAnalysisBizAuditPayload payload) throws Exception {
        if (producer == null) {
            throw new IllegalStateException("Data analysis business audit RocketMQ producer is not available");
        }
        byte[] body = objectMapper.writeValueAsBytes(payload);
        Message message = new Message(topic, tag, payload.getEventId(), body);
        producer.send(message);
    }

    @PreDestroy
    public void destroy() {
        if (producer != null) {
            producer.shutdown();
            logger.info("Stopped data analysis business audit RocketMQ producer");
        }
    }
}
