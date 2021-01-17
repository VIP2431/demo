package ru.vip.demo;

 import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import ru.vip.demo.parse.SimpleName;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

@Component
public class ApplicationContextProvider {
    private String TARGET_NAME = "Data?";
    private int[] NUMBER_BEAN = { 37, 64};
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
    public void setNUMBER_BEAN(int[] NUMBER_BEAN) { this.NUMBER_BEAN = NUMBER_BEAN; }
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
         String[] names = applicationContext.getBeanDefinitionNames();
         printHeadContext();
         for(String name : names){
            ++countBeans;
            String originalClassName =  factory.getBeanDefinition(name).getBeanClassName();
            FLAG_PRINT_METHOD = isNUMBER_BEAN();
            if( FLAG_PRINT_METHOD
                    || TARGET_NAME == null
                    || name.contains(TARGET_NAME)) {
                printBean( name, originalClassName);
            }
        }
        System.out.print(" === countBeans=[" + count + "/"+ countBeans + "]");
        System.out.println(" === allBeans=[" + applicationContext.getBeanDefinitionCount() + "]  ===");
    }

    private boolean isNUMBER_BEAN(){
 //       for(int n=0; n < NUMBER_BEAN.length; ++n) {
        for( int n : NUMBER_BEAN) {
            if( n == countBeans) { return true; }
        }
        return false;
    }

    private void printHeadContext() {
        System.out.println("                        ----------------------------");
        System.out.print(" Условия:" );
        System.out.print(" TARGET_NAME=(\"" + TARGET_NAME + "\")" );
        System.out.print(" NUMBER_BEAN=(");
        for(int n =0; n < NUMBER_BEAN.length; ++n) {
            if ( n>0 ) System.out.print(",");
            System.out.print(" " + NUMBER_BEAN[n]);
        }
        System.out.print(")");
        System.out.print(" CNT_METHOD=(" + CNT_METHOD + ")");
        System.out.println(" FLAG_TO_STRING=(" + FLAG_TO_STRING + ")");
        System.out.println("                        ----------------------------");
    }

    private void printBean( String name, String originalClassName) {
        if (originalClassName == null) {
           System.out.println("-- Bean:[" + countBeans + "]M:[0]=[" + name + "]<<<");
           return;
        }
        try {
            Class<?> originalClass = Class.forName(originalClassName);
            Method[] methods = originalClass.getMethods();
            Annotation[] annotations = originalClass.getAnnotations();

            ++count;
            System.out.print("** Bean:[" + countBeans + "]<"
                    + originalClass.getSimpleName() + ">@:["
                    + annotations.length + "]M:[" + methods.length + "]");
            if (FLAG_TO_STRING) {
                System.out.print("->[" + originalClassName + "]<<<");
            }
            System.out.println(" ");
            if (FLAG_PRINT_METHOD) {
                printAnnotation(annotations);
                printMethods(methods);
            }
        } catch ( NullPointerException e) {
            System.out.println( "  ***** Exception: " + e + " <" + name + ".Class>");
        } catch ( Exception e) {
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
