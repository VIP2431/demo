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

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

@Component
public class ApplicationContextProvider {
    private int allBeans = 0;
    private int countBeans = 0;
    private int count = 0;
    private StringBuffer strOut = new StringBuffer(500);
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

    private void parseAnotionMethod( Method  method) {
        Annotation[][] arrAnnotations = method.getParameterAnnotations();
        if (arrAnnotations == null) { return; }
        for(Annotation[] annotations : arrAnnotations) {
            if(annotations == null) { return; }
            //System.out.println("  " + annotations.toString());
            for (Annotation ant : annotations) {
                //System.out.println("  " + ant.toString());
                System.out.println("  @" + parseString(ant.toString()));
            }
        }
    }

    private void printMethods( Method[] methods){
        int cnt = 0;
        for (Method method : methods) {
          System.out.print("[" + ++cnt + "]<" + method.getName() + ">");
          System.out.print("(" + method.getParameterCount() + ")");
          String str = method.toString();
          System.out.println("->[" + str + "];");

          parseAnotionMethod( method);
          System.out.println("  " + parseString( str) + " {\n  }");
       }
    }

    private void printBean( String name){
        BeanDefinition beanDefinition = factory.getBeanDefinition(name);
        String originalClassName = beanDefinition.getBeanClassName();
        ++countBeans;
        //if(countBeans != 3 && countBeans != 7) { return; }  //
        try {
            if( originalClassName != null)  {
                Class <?> originalClass = Class.forName(originalClassName);
                if (originalClass != null) {
                    Method[] methods = originalClass.getMethods();
                    if(methods != null) {
                        ++count;
                        System.out.print("\n** bean[" + countBeans + "][" + methods.length + "]");
                        System.out.println("<"+ originalClass.getSimpleName() +">[" + originalClassName + "]<<<");
                        if(countBeans != 9 && countBeans != 9 && countBeans != 9) { return; }  //
                        printMethods(methods);
                        System.out.println(" ");
                        return;
                    }
                }
            }
            // System.out.println( " +++ bean[" + countBeans + "]::[0]=[" + name + "]<<<");
        }catch (Exception e){ e.printStackTrace(); }
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

    // Убирает точки. Из строки "org.spring.Persona" делает "Persona"
    private String parseString( String  str) {
        int i = 0;
        int iCopy = 0;
        int len = str.length();
        strOut.setLength( 0);
        for( i=0; i < len; ++i) {
            char c = str.charAt(i);
            if(c == ' ' || c == ',' || c == '(' || c == ')')  {
                strOut.append(str.substring( iCopy, ++i));
                iCopy = i;
            } else if ( c == '.') {
                iCopy = ++i;
            }
        }
        if(len >= i) { strOut.append(str.substring( iCopy, i)); }
        return String.valueOf(strOut);
    }

}
