package net.tzolov.scdf.app.starter.generator;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.springframework.validation.annotation.Validated;

/**
 * @author Christian Tzolov
 */
@Validated
public class AppDefinition {

	public enum AppType {source, processor, sink}

	public enum AppSubType {

		none(""), reactive("Reactive"),
		reactive_stream("ReactiveStream"), functional("Functional");

		private final String name;

		AppSubType(String name) {
			this.name = name;
		}

		String getName() {
			return this.name;
		}
	}

	@NotEmpty
	private String appName;

	@NotNull
	private AppType appType;

	@NotNull
	private AppDefinition.AppSubType appSubType = AppSubType.none;

	@NotEmpty
	private String appVersion;

	public String getAppName() {
		return appName;
	}

	public void setAppName(String appName) {
		this.appName = appName;
	}

	public AppType getAppType() {
		return appType;
	}

	public void setAppType(AppType appType) {
		this.appType = appType;
	}

	public AppSubType getAppSubType() {
		return appSubType;
	}

	public void setAppSubType(AppSubType appSubType) {
		this.appSubType = appSubType;
	}

	public String getAppVersion() {
		return appVersion;
	}

	public void setAppVersion(String appVersion) {
		this.appVersion = appVersion;
	}
}
