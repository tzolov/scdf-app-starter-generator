package net.tzolov.scdf.app.starter.generator;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

/**
 * @author Christian Tzolov
 */
@ConfigurationProperties("app.starter")
@Validated
public class ScdfAppStarterGeneratorProperties {

	/**
	 * List of source, processor and sink Applications part for this application theme.
	 */
	@NotEmpty
	private final List<AppDefinition> apps = new ArrayList<>();

	/**
	 * Location where the project source files are written.
	 */
	@NotNull
	private File outputFolder = new File("./target/output");

	/**
	 * Name of the application theme common for all applications in this theme.
	 */
	@NotEmpty
	private String parentAppName;

	/**
	 * Version for the application theme.
	 */
	@NotEmpty
	private String parentAppVersion;

	/**
	 * Spring Cloud Stream version to use.
	 */
	@NotEmpty
	private String springCloudStreamVersion;

	/**
	 * Spring Cloud Dependency version.
	 */
	@NotEmpty
	private String springCloudDependenciesParentVersion;

	/**
	 * App Starter Build Version
	 */
	@NotEmpty
	private String appStartersBuildVersion;

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

	public String getAppStartersBuildVersion() {
		return appStartersBuildVersion;
	}

	public void setAppStartersBuildVersion(String appStartersBuildVersion) {
		this.appStartersBuildVersion = appStartersBuildVersion;
	}

	public List<AppDefinition> getApps() {
		return apps;
	}
}
