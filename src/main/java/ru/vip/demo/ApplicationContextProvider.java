package ru.vip.demo;

import lombok.SneakyThrows;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
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
    private int allBeans = 0;
    private int countBeans = 0;
    private int count = 0;

    private ApplicationContext applicationContext;
    @Autowired
    private ConfigurableListableBeanFactory factory;

    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @EventListener
    @SneakyThrows
    public void handleContextRefresh(ContextRefreshedEvent event){
        applicationContext = event.getApplicationContext();
        printBeanContext();
    }

    private void parseAnnotationMethod( Method  method) {
        Annotation[][] arrAnnotations = method.getParameterAnnotations();
        // if (arrAnnotations == null) { return; }
        SimpleName simpleName = new SimpleName();
        for(Annotation[] annotations : arrAnnotations) {
            if(annotations == null) { return; }
            //System.out.println("  " + annotations.toString());
            for (Annotation ant : annotations) {
                //System.out.println("  " + ant.toString());
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

    private void printBean( String name){
        BeanDefinition beanDefinition = factory.getBeanDefinition(name);
        String originalClassName = beanDefinition.getBeanClassName();
        ++countBeans;
        if(countBeans != 110) { return; }  //
        try {
            if( originalClassName != null)  {
                Class <?> originalClass = Class.forName(originalClassName);
                Method[] methods = originalClass.getMethods();
                ++count;
                System.out.print("\n** bean[" + countBeans + "][" + methods.length + "]");
                System.out.println("<"+ originalClass.getSimpleName() +">[" + originalClassName + "]<<<");
                //if(countBeans != 9 && countBeans != 9 && countBeans != 9) { return; }  //
                printMethods(methods);
                System.out.println(" ");
            }
            /*
            else {
                System.out.println( " +++ bean[" + countBeans + "]::[0]=[" + name + "]<<<");
            }
            */
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void printBeanContext() {
        if(applicationContext != null) {
            allBeans = applicationContext.getBeanDefinitionCount();
            String[] names = applicationContext.getBeanDefinitionNames();
            for(String name : names){ printBean( name); }
        }
        System.out.println(" === countBeans=[" + count + "/"+ countBeans + "] === allBeans=[" + allBeans + "]  ===");

        Runtime process = Runtime.getRuntime();
        System.out.println("       **************   Всего памяти: [" + process.totalMemory() + "] *************\n");

        // process.exit(15);
        process.halt(7);
    }
}
