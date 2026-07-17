package com.springboot.mq;

import com.ibm.mq.jakarta.jms.MQConnectionFactory;
import com.ibm.msg.client.jakarta.wmq.WMQConstants;
import jakarta.jms.ConnectionFactory;

import jakarta.jms.JMSException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.core.JmsTemplate;

@Configuration
public class JmsConfig {

    @Value("${ibm.mq.queueManager}")
    private String queueManager;

    @Value("${ibm.mq.channel}")
    private String channel;

    @Value("${ibm.mq.connName}")
    private String connName;

    @Value("${ibm.mq.user:#{null}}")
    private String user;

    @Value("${ibm.mq.password:#{null}}")
    private String password;

    @Value("${ibm.mq.transacted:false}")
    private boolean transacted;

    /**
     * 1. Define the Raw IBM MQ Connection Factory
     */
    @Bean
    public MQConnectionFactory mqConnectionFactory() {
        try {
            MQConnectionFactory factory = new MQConnectionFactory();
            System.out.println("Initializing IBM MQ Connection Factory with the following parameters:");
            System.out.println("Queue Manager: " + queueManager);
            System.out.println("Channel: " + channel);
            System.out.println("ConnName: " + connName);
            System.out.println("User: " + user);
            System.out.println("Password: " + password);
            System.out.println("Transacted: " + transacted);

            // Core Connection Properties
            factory.setQueueManager(queueManager);
            factory.setChannel(channel);

            // Expects "host(port)" format, e.g., "localhost(1414)"
            factory.setConnectionNameList(connName);

            // Establish Connection Mode (Client Mode via TCP/IP)
            factory.setTransportType(WMQConstants.WMQ_CM_CLIENT);

            // Explicitly set credentials on the connection factory object
            factory.setStringProperty(WMQConstants.USERID, user);
            factory.setStringProperty(WMQConstants.PASSWORD, password);

            // Enable MQCSP authentication if your server requires it
            factory.setBooleanProperty(WMQConstants.USER_AUTHENTICATION_MQCSP, false);

            return factory;
        } catch (Exception e) {
            throw new IllegalStateException("Failed to initialize IBM MQ Factory", e);
        }
    }

    /**

    /**
     * 3. Configure JmsTemplate for Outbound Message Production
     */
    @Bean
    public JmsTemplate jmsTemplate(ConnectionFactory pooledConnectionFactory) {
        JmsTemplate template = new JmsTemplate(pooledConnectionFactory);

        // Optional: Ensure messages are handled inside transactions
        template.setSessionTransacted(transacted);

        return template;
    }

    /**
     * 4. Configure Container Factory for Inbound @JmsListener Execution
     */
    @Bean
    public DefaultJmsListenerContainerFactory jmsListenerContainerFactory(ConnectionFactory pooledConnectionFactory) {
        DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
        factory.setConnectionFactory(pooledConnectionFactory);

        // Setup performance concurrency boundaries (e.g., 3 to 10 background worker threads)
        factory.setConcurrency("3-10");

        // Explicitly set the session acknowledgment type
        factory.setSessionTransacted(transacted);
        if( transacted ) {
            System.out.println("Setting session acknowledgment mode to AUTO_ACKNOWLEDGE for transacted sessions.");
            factory.setSessionAcknowledgeMode(jakarta.jms.Session.AUTO_ACKNOWLEDGE);
        }
        else {
            System.out.println("Setting session acknowledgment mode to CLIENT_ACKNOWLEDGE for non-transacted sessions.");
            factory.setSessionAcknowledgeMode(jakarta.jms.Session.CLIENT_ACKNOWLEDGE);
        }

        return factory;
    }
}