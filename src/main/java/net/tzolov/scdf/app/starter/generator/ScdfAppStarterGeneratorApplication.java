package net.tzolov.scdf.app.starter.generator;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

import com.samskivert.mustache.Mustache;
import com.samskivert.mustache.Template;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.util.Assert;
import org.springframework.util.FileCopyUtils;

@SpringBootApplication
@EnableConfigurationProperties(ScdfAppStarterGeneratorProperties.class)
public class ScdfAppStarterGeneratorApplication implements CommandLineRunner {

	@Autowired
	private ScdfAppStarterGeneratorProperties properties;

	public static void main(String[] args) {
		SpringApplication.run(ScdfAppStarterGeneratorApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		if (!properties.getOutputFolder().exists()) {
			properties.getOutputFolder().mkdir();
		}

		Assert.isTrue(properties.getOutputFolder().isDirectory(), "Output folder is expected");

		Map<String, Object> templateProperties = new HashMap<>();

		templateProperties.put("parent-app-name", properties.getParentAppName());
		templateProperties.put("ParentAppName", camelCase(properties.getParentAppName()));
		templateProperties.put("parent-app-name-pkg", toPkg(properties.getParentAppName()));
		templateProperties.put("parent-app-version", properties.getParentAppVersion());

		templateProperties.put("apps", properties.getApps());

		templateProperties.put("app-starters-build-version", properties.getSpringCloudStreamVersion());
		templateProperties.put("spring-cloud-dependencies-parent-version", properties.getSpringCloudDependenciesParentVersion());
		templateProperties.put("spring-cloud-stream-version", properties.getSpringCloudStreamVersion());

		// ---------------------------------
		// App Parent POM
		// ---------------------------------
		File appParentDir = file(properties.getOutputFolder(), properties.getParentAppName());
		appParentDir.mkdirs();
		FileCopyUtils.copy(
				materialize("classpath:/template/app-parent-pom.xml", templateProperties),
				new FileWriter(file(appParentDir, "pom.xml")));

		// ---------------------------------
		// App Parent README
		// ---------------------------------
		appParentDir.mkdirs();
		FileCopyUtils.copy(
				materialize("classpath:/template/README-parent.adoc", templateProperties),
				new FileWriter(file(appParentDir, "README.adoc")));

		// ---------------------------------
		// App Dependency Sub Project
		// ---------------------------------
		File appDependencyDir = file(appParentDir, properties.getParentAppName() + "-app-dependencies");
		appDependencyDir.mkdir();
		FileCopyUtils.copy(
				materialize("classpath:/template/app-dependencies-pom.xml", templateProperties),
				new FileWriter(file(appDependencyDir, "pom.xml")));


		// ---------------------------------
		// App Common Sub Project
		// ---------------------------------
		File appCommonDir = file(appParentDir, "spring-cloud-starter-stream-common-" + properties.getParentAppName());
		appCommonDir.mkdir();
		FileCopyUtils.copy(
				materialize("classpath:/template/app-common-pom.xml", templateProperties),
				new FileWriter(file(appCommonDir, "pom.xml")));

		File appCommonSrcDir = toDirs(appCommonDir,
				"src.main.java.org.springframework.cloud.stream.app." + toPkg(properties.getParentAppName()) + ".common");
		appCommonSrcDir.mkdirs();
		FileCopyUtils.copy(
				materialize("classpath:/template/AppStarterCommon.java", templateProperties),
				new FileWriter(file(appCommonSrcDir, camelCase(properties.getParentAppName()) + "Common.java")));

		// ---------------------------------
		// App Test Support Sup Project
		// ---------------------------------
		File appTestSupportDir = file(appParentDir, properties.getParentAppName() + "-app-starters-test-support");
		appTestSupportDir.mkdir();
		FileCopyUtils.copy(
				materialize("classpath:/template/app-test-support-pom.xml", templateProperties),
				new FileWriter(file(appTestSupportDir, "pom.xml")));

		File testSupportSrcDir = toDirs(appTestSupportDir,
				"src.main.java.org.springframework.cloud.stream.app.test." + toPkg(properties.getParentAppName()));
		testSupportSrcDir.mkdirs();

		// ---------------------------------
		// Child projects of type : source, processor or sink
		// ---------------------------------
		for (AppDefinition appDefinition : properties.getApps()) {
			generateChildProject(appParentDir, templateProperties, appDefinition);
		}
	}

	private void generateChildProject(File appStarterRootDirectory, Map<String, Object> templateProperties, AppDefinition appDefinition) throws IOException {

		templateProperties.put("app-name", appDefinition.getAppName());
		templateProperties.put("AppName", camelCase(appDefinition.getAppName()));
		templateProperties.put("app-name-pkg", toPkg(appDefinition.getAppName()));

		templateProperties.put("type", appDefinition.getAppType());
		templateProperties.put("Type", capitalize(appDefinition.getAppType()));

		// app POM
		File appDir = file(appStarterRootDirectory, "spring-cloud-starter-stream-" + appDefinition.getAppType() + "-" + appDefinition.getAppName());
		appDir.mkdir();
		FileCopyUtils.copy(
				materialize("classpath:/template/app-" + appDefinition.getAppType() + "-pom.xml", templateProperties),
				new FileWriter(file(appDir, "pom.xml")));

		String appPackageName = String.format("org.springframework.cloud.stream.app.%s.%s", toPkg(appDefinition.getAppName()), appDefinition.getAppType());
		File appMainSrcDir = toDirs(appDir, "src.main.java." + appPackageName);
		appMainSrcDir.mkdirs();

		File appMetaInfDir = toDirs(appDir, "src.main.resources.META-INF");
		appMetaInfDir.mkdirs();

		// META-INF/spring.providers
		FileCopyUtils.copy(
				"provides: spring-cloud-starter-stream-" + appDefinition.getAppType() + "-" + appDefinition.getAppName(),
				new FileWriter(file(appMetaInfDir, "spring.providers")));

		// META-INF/spring-configuration-metadata-whitelist.properties
		String propertyClassName = String.format("%s%sProperties", camelCase(appDefinition.getAppName()), capitalize(appDefinition.getAppType()));
		FileCopyUtils.copy(
				String.format("configuration-properties.classes=org.springframework.cloud.stream.app.%s.%s.%s",
						toPkg(appDefinition.getAppName()), appDefinition.getAppType(), propertyClassName),
				new FileWriter(file(appMetaInfDir, "spring-configuration-metadata-whitelist.properties")));

		// app Properties Class
		FileCopyUtils.copy(
				materialize("classpath:/template/PropertiesClass.java", templateProperties),
				new FileWriter(file(appMainSrcDir, propertyClassName + ".java")));

		// app Configurations Class
		String configurationClassName = camelCase(appDefinition.getAppName() + "-" + appDefinition.getAppType() + "-configuration.java");
		FileCopyUtils.copy(
				materialize("classpath:/template/App" + capitalize(appDefinition.getAppType()) + "Configuration.java", templateProperties),
				new FileWriter(file(appMainSrcDir, configurationClassName)));

		// TESTS
		if (!appDefinition.getAppType().equalsIgnoreCase("sink")) {
			File appTestSrcDir = toDirs(appDir, "src.test.java." + appPackageName);
			appTestSrcDir.mkdirs();

			String integrationTestClassName = camelCase(appDefinition.getAppName() + "-" + appDefinition.getAppType() + "-integration-tests.java");
			FileCopyUtils.copy(
					materialize("classpath:/template/App" + capitalize(appDefinition.getAppType()) + "IntegrationTests.java", templateProperties),
					new FileWriter(file(appTestSrcDir, integrationTestClassName)));
		}


		// README
		FileCopyUtils.copy(
				materialize("classpath:/template/README.adoc", templateProperties),
				new FileWriter(file(appDir, "README.adoc")));

	}

	private String toPkg(String text) {
		return text.replace("-", ".");
	}

	private String capitalize(String text) {
		return text.substring(0, 1).toUpperCase() + text.substring(1);
	}

	private String camelCase(String text) {
		String[] parts = text.split("-");
		StringBuilder sb = new StringBuilder();
		for (String p : parts) {
			sb.append(capitalize(p));
		}
		return sb.toString();
	}

	private File toDirs(File parent, String packageName) {
		String[] names = packageName.split("\\.");
		File result = parent;
		for (String p : names) {
			result = file(result, p);
		}
		return result;
	}

	private File file(File parent, String child) {
		return new File(parent, child);
	}

	private String materialize(String templatePath, Map<String, Object> templateProperties) throws IOException {
		try (InputStreamReader resourcesTemplateReader = new InputStreamReader(
				new DefaultResourceLoader().getResource(templatePath).getInputStream())) {
			Template resourceTemplate = Mustache.compiler().compile(resourcesTemplateReader);
			return resourceTemplate.execute(templateProperties);
		}
	}
}
