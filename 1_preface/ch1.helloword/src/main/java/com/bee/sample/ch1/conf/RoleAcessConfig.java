package com.bee.sample.ch1.conf;

import java.util.Arrays;

import com.bee.sample.ch1.annotation.Function;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
/**
 * Spring Boot 的一个AOP实现，用于权限
 * @author 李家智
 *
 */
@Aspect
@Configuration
public class RoleAcessConfig {
	private static final Logger logger = LoggerFactory.getLogger(RoleAcessConfig.class);
	/**
	 * 所有使用Function的注解的方法，且在Controller注解标注的类里
	 * 
	 *  all method with Function Annotation and it's Spring Controller Method
	 * 
	 * @param pjp
	 * @param function
	 * @return
	 * @throws Throwable
	 */
	@Around("within(@org.springframework.stereotype.Controller *) && @annotation(function)")
	public Object functionAccessCheck(final ProceedingJoinPoint pjp, Function function) throws Throwable {
		
		System.out.println("function access check 1");
		
		if (function != null) {
			String functionName = function.value();
			if (!canAccess(functionName)) {
				MethodSignature ms = (MethodSignature) pjp.getSignature();
				throw new RuntimeException("Can not Access " + ms.getMethod());
			}
		}else {
			System.out.println("Function annotation is null");
		}
		
		// 继续处理原有调用
		Object o = pjp.proceed();
		return o;

	}

	/**
	 * apply the AOP for all spring Controller
	 * 
	 * @param pjp
	 * @return
	 * @throws Throwable
	 */
	@Around("within(@org.springframework.stereotype.Controller *)")
	public Object functionAccessCheck2(final ProceedingJoinPoint pjp) throws Throwable {
	
		System.out.println("function access check 2");

		Object o = pjp.proceed();
		return o;

	}

	/**
	 * deifne a point cut, and apply to all the methods start with bye*
	 */
	//@Pointcut("execution(* com.bee.sample.ch1.controller.HelloworldController.*(..))")
	@Pointcut("execution(* bye*(..))")
	//@Pointcut("execution(* *..*(..))")
	public void sayByePointCut() {}

	@Around("sayByePointCut()")
	public Object sayByeAdvice(final ProceedingJoinPoint pjp) throws Throwable {
	
		System.out.println("** around ** before say bye");

		Object o = pjp.proceed();

		System.out.println("** around ** after say bye");

		return o;

	}

	/**
	 * pointcut advice before the target method
	 */
	@Before("sayByePointCut()")
	public void byeBeforeAdvice(JoinPoint joinPoint) {
        String classname = joinPoint.getTarget().getClass().getSimpleName();
        String methodname = joinPoint.getSignature().getName();
        System.out.println(classname + " ——前置通知——" + methodname);	
	}

    @AfterReturning("sayByePointCut()")
    public void myAfterAdivice(JoinPoint joinPoint){
        String classname = joinPoint.getTarget().getClass().getSimpleName();
        String methodname = joinPoint.getSignature().getName();
        System.out.println(classname + " ——后置通知——" + methodname);
    }
	

	protected boolean canAccess(String functionName) {
		if (functionName.length() == 0) {
			return true;
		} else {
			// 取出当前用户对应的所有角色，从数据库查询角色是否有访问functionName的权限。
			return false;
		}
	}

	/**
	 * 所有Controller方法
	 * @param pjp
	 * @return
	 * @throws Throwable
	 */
	@Around("@within(org.springframework.stereotype.Controller) ")
	public Object simpleAop(final ProceedingJoinPoint pjp) throws Throwable {
		try {
			Object[] args = pjp.getArgs();
			System.out.println("args:" + Arrays.asList(args));
			Object o = pjp.proceed();
			System.out.println("return :" + o);
			return o;

		} catch (Throwable e) {
			throw e;
		}
	}

}
