/*
 * Copyright 2018 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springframework.cloud.stream.app.{{app-name-pkg}}.sink;

import java.util.function.Consumer;
import java.util.function.Function;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.NoneNestedConditions;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.app.twitter.common.OnMissingStreamFunctionDefinitionCondition;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.messaging.Message;
import org.springframework.cloud.stream.messaging.Sink;

import org.springframework.context.annotation.Bean;

/**
 * Functional Sink (Producer) :
 * http://cloud.spring.io/spring-cloud-static/spring-cloud-stream/2.1.0.RC4/single/spring-cloud-stream.html#_spring_cloud_function
 *
 * @author Christian Tzolov
 */
@Configuration
@EnableBinding(Sink.class)
@EnableConfigurationProperties({ {{AppName}}SinkProperties.class })
public class {{AppName}}SinkConfiguration {

	private static final Log logger = LogFactory.getLog({{AppName}}SinkConfiguration.class);

	@Autowired
	private {{AppName}}SinkProperties properties;

	// Use the spring.cloud.stream.function.definition to override the default
	// function composition. For example you can drop the toUpperCase transformation:
	// --spring.cloud.stream.function.definition=toText|sinkConsumer

	@Bean
	@ConditionalOnMissingBean
	@Conditional(OnMissingStreamFunctionDefinitionCondition.class)
	public IntegrationFlow mySinkFlow(Sink sink) {

		return IntegrationFlows
			.from(sink.input())
			.transform(Message.class, toText().andThen(upper())::apply)
			.handle(sinkConsumer())
			.get();
	}

	@Bean
	public Consumer<String> sinkConsumer() {
		return System.out::println;
	}

	@Bean
	public Function<Message<?>, String> toText() {
		return message -> message.getPayload().toString();
	}

	@Bean
	public Function<String, String> upper() {
		return message -> message.toUpperCase();
	}
}
