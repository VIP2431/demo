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
    private int countBeans = 0;
    private int count = 0;

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
    }

    public void printBeanContext() {
        int allBeans = applicationContext.getBeanDefinitionCount();
        String[] names = applicationContext.getBeanDefinitionNames();
        for(String name : names){ printBean( name); }
        System.out.println(" === countBeans=[" + count + "/"+ countBeans + "] === allBeans=[" + allBeans + "]  ===");
        Runtime process = Runtime.getRuntime();
        System.out.println("       **************   Всего памяти: [" + process.totalMemory() + "] *************\n");
        // process.exit(15);
        process.halt(25);
    }

    private void printBean( String name){
        BeanDefinition beanDefinition = factory.getBeanDefinition(name);
        String originalClassName = beanDefinition.getBeanClassName();
        ++countBeans;
        try {
            if( originalClassName != null)  {
                Class <?> originalClass = Class.forName(originalClassName);
                Method[] methods = originalClass.getMethods();
                ++count;
                System.out.print("** Bean[" + countBeans + "][" + methods.length + "]");
                System.out.println("<"+ originalClass.getSimpleName() +">[" + originalClassName + "]<<<");
                printMethods(methods);
            }
            else {
                System.out.println( "-- Bean[" + countBeans + "][0]=[" + name + "]<<<");
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void parseAnnotationMethod( Method  method) {
        Annotation[][] arrAnnotations = method.getParameterAnnotations();
        SimpleName simpleName = new SimpleName();
        for(Annotation[] annotations : arrAnnotations) {
            if(annotations == null) { return; }
            for (Annotation ant : annotations) {
                System.out.println("  " + simpleName.get(ant.toString()));
            }
        }
    }

    private void printMethods( Method[] methods){
        int cnt = 0;
        SimpleName simpleName = new SimpleName();
        for (Method method : methods) {
          System.out.print("[" + ++cnt + "]<" + method.getName() + ">");
          System.out.print("(" + method.getParameterCount() + ")");
          String str = method.toString();
          System.out.println("->[" + str + "];");

          parseAnnotationMethod( method);
          System.out.println("  " + simpleName.get( str) + "{\n  }");
       }
    }


}
