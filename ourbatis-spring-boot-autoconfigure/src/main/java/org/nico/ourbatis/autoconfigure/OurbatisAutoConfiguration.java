package org.nico.ourbatis.autoconfigure;

import javax.annotation.PostConstruct;

import org.apache.ibatis.session.SqlSessionFactory;
import org.nico.noson.util.string.StringUtils;
import org.nico.ourbatis.Ourbatis;
import org.nico.ourbatis.configure.OurbatisConfigure;
import org.nico.ourbatis.configure.OurbatisDefaultConfigue;
import org.nico.ourbatis.loader.OurbatisLoader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnBean({SqlSessionFactory.class})
@EnableConfigurationProperties(OurbatisProperties.class)
public class OurbatisAutoConfiguration {

	@Autowired
	private SqlSessionFactory sqlSessionFactory;
	
	@Autowired(required = false)
	private OurbatisConfigure configure;
	
	@Autowired
    private OurbatisProperties properties;
	
	@PostConstruct
	public void ourbatisLoader() {
		if(StringUtils.isNotBlank(properties.getEnable())) {
			if(properties.getEnable().equalsIgnoreCase("false")) {
				return;
			}
		}
		if(configure == null) {
			configure = new OurbatisDefaultConfigue();
		}
		if(StringUtils.isNotBlank(properties.getPrefix())) {
			Ourbatis.prefix = properties.getPrefix();
		}
		if(StringUtils.isNotBlank(properties.getSuffix())) {
			Ourbatis.suffix = properties.getSuffix();
		}
		if(StringUtils.isNotBlank(properties.getPrint())) {
			Ourbatis.print = properties.getPrint();
		}
		configure.configAdapter(Ourbatis.ASSIST_ADAPTERS);
		configure.configWrapper(
				Ourbatis.JDBC_NAME_WRAPPERS,
				Ourbatis.TABLE_NAME_WRAPPERS,
				Ourbatis.MAPPER_NAME_WRAPPERS,
				Ourbatis.JAVA_TYPE_WRAPPERS);
		
		OurbatisLoader loader = new OurbatisLoader(
				sqlSessionFactory,
				properties.getTemplateLocations(),
				properties.getInterfaceLocations());
		
		loader.add(properties.getDomainLocations());
		loader.build();
	}
}
