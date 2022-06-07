package com.nowcoder.community;

import com.nowcoder.community.dao.AlphaDao;
import com.nowcoder.community.service.AlphaService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.test.context.ContextConfiguration;

import java.security.AlgorithmParameterGenerator;
import java.text.SimpleDateFormat;
import java.util.*;

@SpringBootTest
@ContextConfiguration(classes = CommunityApplication.class)
//这句@ContextConfiguration注解主要是为了表明我们的测试代码是以CommunityApplication这个类作为配置类的——希望测试类中能知道配置类是哪个
class CommunityApplicationTests implements ApplicationContextAware {
								//主要是获取Spring被自动创建的容器——哪个类想得到Spring容器就实现这么一个接口
	private ApplicationContext applicationContext;


	@Override
	//想获取Spring容器就需要实现ApplicationContextAware，然后实现这个setApplicationContext方法
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		/*
		传入的这个参数就是Spring容器——这个ApplicationContext是一个接口——可以看一层层继承，最后父接口就是BeanFactory，也即Spring容器的隐藏接口
		那么这个ApplicationContext就是Spring容器BeanFactory的子接口，也就是扩展了更多的方法，功能更强，所以通常都是使用这个子接口ApplicationContext
		 */
		//那么如果有一个类实现了这个ApplicationContextAware，那么就知道这个类想要使用Spring容器，就会调用这个setApplicationContext方法，把Spring容器ApplicationContext传进来
		//那么我们只需要把这个传进来的容器暂存一下【存到一个成员变量里】，后面就可以使用这个Spring容器了
		this.applicationContext = applicationContext;      //记录一下Spring容器，方便后面使用——写一个测试方法使用Spring容器看看
	}
	@Test
	public void testApplicationContext(){          //使用Spring容器的一个简单例子，证明了这个Spring容器的存在，可见
		System.out.println(applicationContext);
		/*控制台打印出来的信息：类名@hashCode   ——证明容器存在，可见
	org.springframework.web.context.support.GenericWebApplicationContext@61fe30, started on Sun Jun 05 22:13:27 CST 2022
	 */

		////////再看看容器是怎么管理Bean的，那么首先我们需要有一个Bean，才能让它管理——简易创建了一个访问数据库的接口AlphaDao，并给出了实现类AlphaDaoHibernateImpl，并且使用@Repository注解标识，使它能作为一个bean被扫描装配到容器中
		AlphaDao alphaDao = applicationContext.getBean(AlphaDao.class);    //通过Bean的类型调用
		//但是有个问题，就是现在有两个实现这个接口的Bean，一个是用Hibernate方法实现的AlphaDaoHibernateImpl，另一个是用MyBatis实现的AlphaDaoMyBatisImpl
		//这个时候Spring容器就不知道返回哪个Bean了——这样可以在需要的Bean上再加一个注解@Primary
		//可以看到我们调用的时候不依赖具体的实现类本身，也就是不依赖具体的Bean，而是只用调用接口——面向接口编程的思想就可以，之后可以在实现类上加注解保证Bean的装配
		//也就是用户只需要实现Bean，提供Bean之间的关系，之后装配就由Spring容器来实现【Spring容器需要两个数据（用户提供的Bean和配置文件）】
		System.out.println(alphaDao.select());

		//当然如果多个实现类，用户想在这里自己返回一个具体的需要的Bean，也可以通过Bean的名字，强制返回需要的Bean
		AlphaDao alphaDao1 = applicationContext.getBean("alphaHibernate",AlphaDao.class);   //通过Bean的名字调用
		System.out.println(alphaDao1.select());
	}

	@Test
	public void testBeanManagement(){
		//测试Spring容器是否可以自动进行初始化和销毁Bean实例
		AlphaService alphaService = applicationContext.getBean(AlphaService.class);
		System.out.println(alphaService);

		AlphaService alphaService1 = applicationContext.getBean(AlphaService.class);
		System.out.println(alphaService);

		//尽管名称不一样，但是可以看到两个引用都指向了一个Bean实例【对象】，打印出来的hashcode是一样的【当然Hashcode一样不能说明就是一样的对象】
		//但是另一个方面可以看到即使我们执行两次这个操作，相当于想要获取两个实例，但是通过实验结果，看到也只实例化了一次
	}

	@Test
	public void testBeanConfig(){
		SimpleDateFormat simpleDateFormat = applicationContext.getBean(SimpleDateFormat.class);
		System.out.println(simpleDateFormat.format(new Date()));
	}

	@Autowired
	//依赖注入Bean，表示希望能够将AlhaDao自动注入给这个属性alphaDao，直接使用这个属性就可以了
	private AlphaDao alphaDao;

	@Autowired
	@Qualifier("alphaHibernate")
	private AlphaDao alphaDao1;      //我们注意给这个AlphaDao两个实现类，那么使用这种方法获取Bean的时候，如果我们希望获取到另一个实现应该再加一个注解

	@Autowired
	private SimpleDateFormat simpleDateFormat;

	@Autowired
	private AlphaService alphaService;
	@Test
	public void testDI(){     //测试依赖注入
		System.out.println(alphaDao);
		System.out.println(alphaDao1);
		System.out.println(simpleDateFormat);
		System.out.println(alphaService);
	}


}
