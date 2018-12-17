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
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.messaging.Message;
import org.springframework.cloud.stream.messaging.Sink;

import org.springframework.context.annotation.Bean;

/**
 * Functional Sink (Producer) :
 * http://cloud.spring.io/spring-cloud-static/spring-cloud-stream/2.1.0.RC3/single/spring-cloud-stream.html#_spring_cloud_function
 *
 * @author Christian Tzolov
 */
@SpringBootApplication
@EnableBinding(Sink.class)
@EnableConfigurationProperties({ {{AppName}}SinkProperties.class })
public class {{AppName}}SinkConfiguration {

	private static final Log logger = LogFactory.getLog({{AppName}}SinkConfiguration.class);

	@Autowired
	private {{AppName}}SinkProperties properties;

	public static void main(String[] args) {
		SpringApplication.run({{AppName}}SinkConfiguration.class,
			"--spring.cloud.stream.function.definition=sink");
	}

	@Bean
	public Consumer<String> sink() {
		return System.out::println;
	}
}
