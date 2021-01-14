package ru.vip.demo;

 import lombok.SneakyThrows;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import ru.vip.demo.parse.SimpleName;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

@Component
public class ApplicationContextProvider {
    private String TARGET_NAME = "Jxxx";
    private int NUMBER_BEAN = 64;

    public void setTARGET_NAME(String TARGET_NAME) {
        this.TARGET_NAME = TARGET_NAME;
    }

    public void setNUMBER_BEAN(int NUMBER_BEAN) {
        this.NUMBER_BEAN = NUMBER_BEAN;
    }

    private int countBeans = 0;
    private int count = 0;
    private SimpleName simpleName = new SimpleName();
    private ApplicationContext applicationContext;
    private final ConfigurableListableBeanFactory factory;

    public ApplicationContextProvider(ConfigurableListableBeanFactory factory) {
        this.factory = factory;
    }

    @EventListener
    @SneakyThrows
    public void handleContextRefresh(ContextRefreshedEvent event){
        applicationContext = event.getApplicationContext();
        printBeanContext();
        Runtime.getRuntime().halt(25);
    }

    public void printBeanContext() {
        int allBeans = applicationContext.getBeanDefinitionCount();
        String[] names = applicationContext.getBeanDefinitionNames();
        for(String name : names){
            printBean( name);
        }
        System.out.println(" === countBeans=[" + count + "/"+ countBeans + "] === allBeans=[" + allBeans + "]  ===");
    }

    private void printBean( String name){
        BeanDefinition beanDefinition = factory.getBeanDefinition(name);
        String originalClassName = beanDefinition.getBeanClassName();
        boolean flagMetod = false;
        boolean flagPrint = false;
        ++countBeans;
        if(TARGET_NAME == null || NUMBER_BEAN == countBeans || name.indexOf(TARGET_NAME) >= 0) {
            flagPrint = true;
        } else { return; }
        if (NUMBER_BEAN == countBeans) {
            flagMetod = true;
        }
        if (originalClassName != null) {
           ++count;
           try {
                Class<?> originalClass = Class.forName(originalClassName);
                Annotation[] annotations = originalClass.getAnnotations();
                Method[] methods = originalClass.getMethods();
                System.out.print("** Bean:[" + countBeans + "]@:[" + annotations.length + "]M:[" + methods.length + "]");
                System.out.println("<" + originalClass.getSimpleName() + ">[" + originalClassName + "]<<<");
                if (flagMetod) {
                    for(Annotation annotation : annotations) {
                        System.out.println("  " + simpleName.get(annotation.toString()));
                    }
                    printMethods(methods);
                }
           } catch (Exception e) {
                e.printStackTrace();
           }
       } else {
           System.out.println("-- Bean:[" + countBeans + "]M:[0]=[" + name + "]<<<");
       }
    }

    private void parseAnnotationMethod( Annotation[] annotations) {
        for(Annotation annotation : annotations) {
            System.out.println("  " + simpleName.get(annotation.toString()));
        }
    }

    private void printMethods( Method[] methods){
        int cnt = 0;
        for (Method method : methods) {
          Annotation[] annotations = method.getAnnotations();
          System.out.print("[" + ++cnt + "]@[" + annotations.length + "]<" + method.getName() + ">");
          System.out.print("(" + method.getParameterCount() + ")");
          String str = method.toString();
          System.out.println("->[" + str + "];");
          parseAnnotationMethod( annotations);
          System.out.println("  " + simpleName.get( str) + "{\n  }");
       }
    }
}
