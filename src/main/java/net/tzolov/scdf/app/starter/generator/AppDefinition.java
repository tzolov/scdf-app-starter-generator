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

	public enum ProgrammingModel {

		default_model(""), reactive("Reactive"),
		reactive_stream("ReactiveStream"), functional("Functional");

		private final String name;

		ProgrammingModel(String name) {
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
	private AppDefinition.ProgrammingModel programmingModel = ProgrammingModel.default_model;

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

	public ProgrammingModel getProgrammingModel() {
		return programmingModel;
	}

	public void setProgrammingModel(ProgrammingModel programmingModel) {
		this.programmingModel = programmingModel;
	}

	public String getAppVersion() {
		return appVersion;
	}

	public void setAppVersion(String appVersion) {
		this.appVersion = appVersion;
	}
}
