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

package org.springframework.cloud.stream.app.{{app-name-pkg}}.{{type}};

import java.util.concurrent.TimeUnit;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.stream.messaging.Source;
import org.springframework.cloud.stream.test.binder.MessageCollector;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.Message;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author Christian Tzolov
 */
@SuppressWarnings("SpringJavaAutowiringInspection")
@RunWith(SpringRunner.class)
@SpringBootTest(
		webEnvironment = SpringBootTest.WebEnvironment.NONE,
		properties = {
				"debug=false",
				"logging.level.*=INFO",
		})
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
public abstract class {{AppName}}{{Type}}IntegrationTests {

	@Autowired
	protected Source channels;

	@Autowired
	protected MessageCollector messageCollector;

	@TestPropertySource(properties = {
			"debug=true",
			"logging.level.*=DEBUG",
	})
	public static class {{AppName}}PayloadTests extends {{AppName}}{{Type}}IntegrationTests {

        @Ignore("TODO: Remove after test is implemented")
		@Test
		public void testOne() throws InterruptedExceptio {

			Message<?> received = messageCollector.forChannel(this.channels.output()).poll(10, TimeUnit.SECONDS);

			Assert.assertNotNull(received);
		}
	}

	@SpringBootConfiguration
	@EnableAutoConfiguration
	@Import({{AppName}}{{Type}}Configuration.class)
	public static class Test{{AppName}}{{Type}}Application {

		//@Bean
		//public Object myMockBean() {
		// Create here your custom Mock instances to be used with this ITests
		//}
	}

}
