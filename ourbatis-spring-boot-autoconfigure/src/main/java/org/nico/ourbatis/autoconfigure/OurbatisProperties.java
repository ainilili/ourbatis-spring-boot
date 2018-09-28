package org.nico.ourbatis.autoconfigure;

import java.util.Properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = OurbatisProperties.OURBATIS_PREFIX)
public class OurbatisProperties {

	public static final String OURBATIS_PREFIX = "ourbatis";

	private Properties properties = new Properties();

	public Properties getProperties() {
		return properties;
	}
	
	public String getTemplateLocations() {
		if(! properties.containsKey("templateLocations")) {
			throw new RuntimeException("No such property templateLocations defined !");
		}
		return properties.getProperty("templateLocations");
	}
	
	public String getInterfaceLocations() {
		if(! properties.containsKey("interfaceLocations")) {
			throw new RuntimeException("No such property interfaceLocations defined !");
		}
		return properties.getProperty("interfaceLocations");
	}
	
	public String getDomainLocations() {
		if(! properties.containsKey("domainLocations")) {
			throw new RuntimeException("No such property domainLocations defined !");
		}
		return properties.getProperty("domainLocations");
	}
	
	public String getPrefix() {
		return properties.getProperty("prefix");
	}
	
	public String getSuffix() {
		return properties.getProperty("suffix");
	}
	
	public String getPrint() {
		return properties.getProperty("print");
	}
	
	public String getEnable() {
		return properties.getProperty("enable");
	}
	
	public void setTemplateLocations(String templateLocations) {
		properties.setProperty("templateLocations", templateLocations);
	}
	
	public void setInterfaceLocations(String interfaceLocations) {
		properties.setProperty("interfaceLocations", interfaceLocations);
	}
	
	public void setDomainLocations(String domainLocations) {
		properties.setProperty("domainLocations", domainLocations);
	}
	
	public void setPrefix(String prefix) {
		properties.setProperty("prefix", prefix);
	}
	
	public void setSuffix(String suffix) {
		properties.setProperty("suffix", suffix);
	}
	
	public void setPrint(String print) {
		properties.setProperty("print", print);
	}
	
	public void setEnable(String enable) {
		properties.setProperty("enable", enable);
	}
	
}
