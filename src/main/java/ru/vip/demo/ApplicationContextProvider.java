package ru.vip.demo;

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
    private int CNT_METHOD = 1;
    private boolean FLAG_TO_STRING = false;
    private boolean FLAG_PRINT_METHOD = false;

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
            status = 35;
        }
        Runtime.getRuntime().halt( status);
    }

    public void printBeanContext() {
        int allBeans = applicationContext.getBeanDefinitionCount();
        String[] names = applicationContext.getBeanDefinitionNames();
        System.out.print("\n Условия:" );
        System.out.print(" TARGET_NAME=(\"" + TARGET_NAME + "\")" );
        System.out.print(" NUMBER_BEAN=(" + NUMBER_BEAN + ")");
        System.out.print(" CNT_METHOD=(" + CNT_METHOD + ")");
        System.out.print(" FLAG_TO_STRING=(" + FLAG_TO_STRING + ")");
        System.out.println(" FLAG_PRINT_METHOD=(" + FLAG_PRINT_METHOD + ")");
        System.out.println(" ");
        for(String name : names){
            ++countBeans;
            String originalClassName =  factory.getBeanDefinition(name).getBeanClassName();
            if (NUMBER_BEAN == countBeans) {
                FLAG_PRINT_METHOD = true;
            } else {
                FLAG_PRINT_METHOD = false;
            }
            if(TARGET_NAME == null || FLAG_PRINT_METHOD || name.contains(TARGET_NAME)) {
                printBean( name, originalClassName);
            }
        }
        System.out.println(" === countBeans=[" + count + "/"+ countBeans + "] === allBeans=[" + allBeans + "]  ===");
    }

    private void printBean( String name, String originalClassName) {
        if (originalClassName == null) {
           System.out.println("-- Bean:[" + countBeans + "]M:[0]=[" + name + "]<<<");
           return;
        }
        try {
            Class<?> originalClass = Class.forName(originalClassName);
            Annotation[] annotations = originalClass.getAnnotations();
            Method[] methods = originalClass.getMethods();
            ++count;
            System.out.print("** Bean:[" + countBeans + "]@:[" + annotations.length + "]M:[" + methods.length + "]");
            System.out.print("<" + originalClass.getSimpleName() + ">");
            if (FLAG_TO_STRING) {
                System.out.print("->[" + originalClassName + "]<<<");
            }
            System.out.println(" ");
            if (FLAG_PRINT_METHOD) {
                printAnnotation(annotations);
                printMethods(methods);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void printAnnotation( Annotation[] annotations) {
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
          printAnnotation( annotations);
          System.out.println("  " + simpleName.get(str) + "{\n  }");
       }
    }
}
