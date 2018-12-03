package org.nico.ourbatis.autoconfigure;

import java.util.List;

import javax.annotation.PostConstruct;

import org.apache.ibatis.binding.MapperRegistry;
import org.apache.ibatis.session.SqlSessionFactory;
import org.nico.ourbatis.Ourbatis;
import org.nico.ourbatis.configure.OurbatisConfigure;
import org.nico.ourbatis.configure.OurbatisDefaultConfigue;
import org.nico.ourbatis.loader.OurbatisLoader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

@Configuration
@ConditionalOnBean({SqlSessionFactory.class})
@EnableConfigurationProperties(OurbatisProperties.class)
public class OurbatisAutoConfiguration {

	@Autowired
	private List<SqlSessionFactory> sqlSessionFactories;
	
	@Autowired(required = false)
	private OurbatisConfigure configure;
	
	@Autowired
    private OurbatisProperties properties;
	
	@PostConstruct
	public void ourbatisLoader() {
		if(! StringUtils.isEmpty(properties.getEnable())) {
			if(properties.getEnable().equalsIgnoreCase("false")) {
				return;
			}
		}
		if(configure == null) {
			configure = new OurbatisDefaultConfigue();
		}
		if(! StringUtils.isEmpty(properties.getTemplateLocations())) {
			Ourbatis.templateLocation = properties.getTemplateLocations();
		}
		if(! StringUtils.isEmpty(properties.getPrefix())) {
			Ourbatis.prefix = properties.getPrefix();
		}
		if(! StringUtils.isEmpty(properties.getSuffix())) {
			Ourbatis.suffix = properties.getSuffix();
		}
		if(! StringUtils.isEmpty(properties.getPrint())) {
			Ourbatis.print = properties.getPrint();
		}
		configure.configAdapter(Ourbatis.ASSIST_ADAPTERS);
		configure.configWrapper(
				Ourbatis.JDBC_NAME_WRAPPERS,
				Ourbatis.TABLE_NAME_WRAPPERS,
				Ourbatis.MAPPER_NAME_WRAPPERS,
				Ourbatis.JAVA_TYPE_WRAPPERS);
		
		if(! CollectionUtils.isEmpty(sqlSessionFactories)) {
			sqlSessionFactories.forEach(sqlSessionFactory -> {
				MapperRegistry mapperRegistry = sqlSessionFactory.getConfiguration().getMapperRegistry();
				if(! CollectionUtils.isEmpty(mapperRegistry.getMappers())) {
					String mapperLocations = mapperRegistry.getMappers().iterator().next().getPackage().getName();
					OurbatisLoader loader = new OurbatisLoader(
							sqlSessionFactory,
							Ourbatis.templateLocation,
							mapperLocations);
					
					String domainLocationsStr = properties.getDomainLocations();
					if(! StringUtils.isEmpty(domainLocationsStr)) {
						String[] domainLocationArray = domainLocationsStr.split(";");
						for(String domainLocation: domainLocationArray) {
							loader.add(domainLocation);
						}
					}
					
					String domainClassArrayStr = properties.getDomainClasses();
					if(! StringUtils.isEmpty(domainClassArrayStr)) {
						String[] domainClassArray = domainClassArrayStr.split(";");
						for(String domainClassUri: domainClassArray) {
							try {
								Class<?> domainClass = Class.forName(domainClassUri);
								loader.add(domainClass);
							} catch (ClassNotFoundException e) {
								e.printStackTrace();
							}
						}
					}
					
					loader.build();
				}
			});
		}
	}
}
