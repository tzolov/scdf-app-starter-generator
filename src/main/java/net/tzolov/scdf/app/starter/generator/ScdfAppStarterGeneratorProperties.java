package net.tzolov.scdf.app.starter.generator;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author Christian Tzolov
 */
@ConfigurationProperties("app.starter")
public class ScdfAppStarterGeneratorProperties {

	private final List<AppDefinition> apps = new ArrayList<>();

	private File outputFolder = new File("./target/output");

	private String parentAppName = "image-processing";

	private String parentAppVersion = "1.0.0.BUILD-SNAPSHOT";

	private String springCloudStreamVersion = "2.0.0.RELEASE";

	private String springCloudDependenciesParentVersion = "2.0.2.RELEASE";

	public File getOutputFolder() {
		return outputFolder;
	}

	public void setOutputFolder(File outputFolder) {
		this.outputFolder = outputFolder;
	}

	public String getParentAppName() {
		return parentAppName;
	}

	public void setParentAppName(String parentAppName) {
		this.parentAppName = parentAppName;
	}

	public String getParentAppVersion() {
		return parentAppVersion;
	}

	public void setParentAppVersion(String parentAppVersion) {
		this.parentAppVersion = parentAppVersion;
	}

	public String getSpringCloudStreamVersion() {
		return springCloudStreamVersion;
	}

	public void setSpringCloudStreamVersion(String springCloudStreamVersion) {
		this.springCloudStreamVersion = springCloudStreamVersion;
	}

	public String getSpringCloudDependenciesParentVersion() {
		return springCloudDependenciesParentVersion;
	}

	public void setSpringCloudDependenciesParentVersion(String springCloudDependenciesParentVersion) {
		this.springCloudDependenciesParentVersion = springCloudDependenciesParentVersion;
	}

	public List<AppDefinition> getApps() {
		return apps;
	}
}
