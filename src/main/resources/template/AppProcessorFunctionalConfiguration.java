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

package org.springframework.cloud.stream.app.{{app-name-pkg}}.processor;

import java.util.function.Function;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.stream.annotation.EnableBinding;

import org.springframework.cloud.stream.app.{{app-name-pkg}}.common.OnMissingStreamFunctionDefinitionCondition;
import org.springframework.cloud.stream.messaging.Processor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.messaging.Message;


/**
 * Functional Processor:
 * http://cloud.spring.io/spring-cloud-static/spring-cloud-stream/2.1.0.RC4/single/spring-cloud-stream.html#_spring_cloud_function
 *
 * @author Christian Tzolov
 */
@Configuration
@EnableBinding(Processor.class)
@EnableConfigurationProperties({ {{AppName}}ProcessorProperties.class })
public class {{AppName}}ProcessorConfiguration {

	private static final Log logger = LogFactory.getLog({{AppName}}ProcessorConfiguration.class);

	@Autowired
	private {{AppName}}ProcessorProperties properties;


	// Use the spring.cloud.stream.function.definition to override the default function composition.
	@Bean
	@Conditional(OnMissingStreamFunctionDefinitionCondition.class)
	public IntegrationFlow defaultProcessorFlow(Processor processor) {
		return IntegrationFlows
			.from(processor.input())
			.transform(Message.class, text().andThen(trim()).andThen(upper())::apply)
			.channel(processor.output())
			.get();
	}

	@Bean
	public Function<Message<?>, String> text() {
		return message -> message.getPayload().toString();
	}

	@Bean
	public Function<String, String> trim() {
		return s -> s.trim();
	}

	@Bean
	public Function<String, String> upper() {
		return s -> s.toUpperCase();
	}
}
