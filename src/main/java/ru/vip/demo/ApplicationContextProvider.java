package ru.vip.demo;

 import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import ru.vip.demo.parse.SimpleName;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

@Component
public class ApplicationContextProvider {
    private String TARGET_NAME = "Jpa";
    private int NUMBER_BEAN = 64;
    private boolean FLAG_TO_STRING = true;
    private int CNT_METHOD = 1;
    private int countBeans = 0;
    private int count = 0;

    private final ApplicationContext applicationContext;
    private final ConfigurableListableBeanFactory factory;

    private final SimpleName simpleName = new SimpleName();

    public ApplicationContextProvider(ApplicationContext applicationContext, ConfigurableListableBeanFactory factory) {
        this.applicationContext = applicationContext;
        this.factory = factory;
    }

    public void setTARGET_NAME(String TARGET_NAME) { this.TARGET_NAME = TARGET_NAME; }
    public void setNUMBER_BEAN(int NUMBER_BEAN) { this.NUMBER_BEAN = NUMBER_BEAN; }
    public void setFLAG_TO_STRING( boolean FLAG_TO_STRING) { this.FLAG_TO_STRING = FLAG_TO_STRING; }
    public void setCNT_METHOD(int CNT_METHOD) { this.CNT_METHOD = CNT_METHOD; }

    public void handleApplicationContext(){
        int status = -1;
        if (applicationContext != null && factory != null) {
            printBeanContext();
            status = 15;
        }
        Runtime.getRuntime().halt( status);
    }

    public void printBeanContext() {
        int allBeans = applicationContext.getBeanDefinitionCount();
        String[] names = applicationContext.getBeanDefinitionNames();
        for(String name : names){ printBean( name); }
        System.out.println(" === countBeans=[" + count + "/"+ countBeans + "] === allBeans=[" + allBeans + "]  ===");
    }

    private void printBean( String name){
        BeanDefinition beanDefinition = factory.getBeanDefinition(name);
        String originalClassName = beanDefinition.getBeanClassName();
        boolean flagMethod = false;
        ++countBeans;
        if (NUMBER_BEAN == countBeans) { flagMethod = true; }
        if(TARGET_NAME != null && !flagMethod && !name.contains(TARGET_NAME)) { return; }
        if (originalClassName == null) {
           System.out.println("-- Bean:[" + countBeans + "]M:[0]=[" + name + "]<<<");
           return;
        }
        ++count;
        try {
                Class<?> originalClass = Class.forName(originalClassName);
                Annotation[] annotations = originalClass.getAnnotations();
                Method[] methods = originalClass.getMethods();
                System.out.print("** Bean:[" + countBeans + "]@:[" + annotations.length + "]M:[" + methods.length + "]");
                System.out.print("<" + originalClass.getSimpleName() + ">");
                if (FLAG_TO_STRING) { System.out.print("->[" + originalClassName + "]<<<"); }
                System.out.println(" ");
                if (flagMethod) {
                    printAnnotationMethod( annotations);
                    printMethods(methods);
                }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void printAnnotationMethod( Annotation[] annotations) {
        for(Annotation annotation : annotations) {
            String str = annotation.toString();
            if (FLAG_TO_STRING) { System.out.println("->[" + str + "];"); }
            System.out.println("  " + simpleName.get( str));
        }
    }

    private void printMethods( Method[] methods){
        int cnt = 0;
        for (Method method : methods) {
          if (cnt >= CNT_METHOD) { return; }
          Annotation[] annotations = method.getAnnotations();
          String str = method.toString();
          System.out.print("M[" + ++cnt + "]@[" + annotations.length + "]<" + method.getName() + ">");
          System.out.print("(" + method.getParameterCount() + ")");
          if (FLAG_TO_STRING) { System.out.print("->[" + str + "]<<<"); }
          System.out.println(" ");
          printAnnotationMethod( annotations);
          System.out.println("  " + simpleName.get(str) + "{\n  }");
       }
    }
}
