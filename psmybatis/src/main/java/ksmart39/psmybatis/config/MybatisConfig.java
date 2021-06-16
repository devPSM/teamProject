package ksmart39.psmybatis.config;


import javax.sql.DataSource;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@MapperScan(basePackages = "ksmart39.psmybatis.dao", sqlSessionFactoryRef = "mybatisSqlSessionFactory")
public class MybatisConfig {
	
	@Bean("mybatisSqlSessionFactory")
	public SqlSessionFactory mybatisSqlSessionFactory (DataSource dataSource
													 , ApplicationContext ctx) throws Exception {
		
		SqlSessionFactoryBean mybatisSqlSessionFactoryBean = new SqlSessionFactoryBean();
		mybatisSqlSessionFactoryBean.setDataSource(dataSource);
		mybatisSqlSessionFactoryBean.setMapperLocations(ctx.getResources("classpath:/mapper/**/*.xml"));
		mybatisSqlSessionFactoryBean.setTypeAliasesPackage("ksmart39.psmybatis.domain");
		
		return mybatisSqlSessionFactoryBean.getObject();
	}
	
	@Bean("mybatisSqlSessionTemplate")
	public SqlSessionTemplate mybatisSqlSessionTemplate(@Qualifier(value = "mybatisSqlSessionFactory") SqlSessionFactory sqlSessionFactory) {
		return new SqlSessionTemplate(sqlSessionFactory);
	}
	
}
