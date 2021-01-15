package ru.vip.demo;

 import org.springframework.beans.factory.annotation.Autowired;
 import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import ru.vip.demo.parse.SimpleName;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

@Component
public class ApplicationContextProvider {
    private String TARGET_NAME = "Jxxx";
    private int NUMBER_BEAN = 64;
    private int FLAG_TO_STRING = 0;
    private int CNT_METHOD = 1;
    private int countBeans = 0;
    private int count = 0;

    @Autowired private ApplicationContext applicationContext;
    @Autowired private ConfigurableListableBeanFactory factory;

    private SimpleName simpleName = new SimpleName();

    public void setTARGET_NAME(String TARGET_NAME) { this.TARGET_NAME = TARGET_NAME; }
    public void setNUMBER_BEAN(int NUMBER_BEAN) { this.NUMBER_BEAN = NUMBER_BEAN; }
    public void setFLAG_TO_STRING(int FLAG_TO_STRING) { this.FLAG_TO_STRING = FLAG_TO_STRING; }
    public void setCNT_METHOD(int CNT_METHOD) { this.CNT_METHOD = CNT_METHOD; }

    public void handleApplicationContext(){
        int status = -1;
        if (applicationContext != null) {
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
        boolean flagMetod = false;
        boolean flagPrint = false;
        ++countBeans;
        if(TARGET_NAME == null || NUMBER_BEAN == countBeans || name.indexOf(TARGET_NAME) >= 0) {
            flagPrint = true;
        } else { return; }
        if (NUMBER_BEAN == countBeans) { flagMetod = true; }
        if (originalClassName != null) {
           ++count;
           try {
                Class<?> originalClass = Class.forName(originalClassName);
                Annotation[] annotations = originalClass.getAnnotations();
                Method[] methods = originalClass.getMethods();
                System.out.print("** Bean:[" + countBeans + "]@:[" + annotations.length + "]M:[" + methods.length + "]");
                System.out.println("<" + originalClass.getSimpleName() + ">[" + originalClassName + "]<<<");
                if (flagMetod) {
                    parseAnnotationMethod( annotations);
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
            String str = annotation.toString();
            if (FLAG_TO_STRING > 0) { System.out.println("->[" + str + "];"); }
            System.out.println("  " + simpleName.get( str));
        }
    }

    private void printMethods( Method[] methods){
        int cnt = 0;
        for (Method method : methods) {
          if (cnt > CNT_METHOD) { return; }
          Annotation[] annotations = method.getAnnotations();
          System.out.print("M[" + ++cnt + "]@[" + annotations.length + "]<" + method.getName() + ">");
          System.out.println("(" + method.getParameterCount() + ")");
          parseAnnotationMethod( annotations);
          System.out.println("  " + simpleName.get(method.toString()) + "{\n  }");
       }
    }
}
