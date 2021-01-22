package ru.vip.demo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import ru.vip.demo.Param.ProviderParamsConfig;
import ru.vip.demo.parse.SimpleName;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

@Component
public class ApplicationContextProvider {

    private static final Logger log = LoggerFactory.getLogger(ApplicationContextProvider.class);

    private String TARGET_NAME = "Data?";
    private int CNT_METHOD = 1;
    private int[] NUMBER_BEAN = { 37, 64};

    private boolean FLAG_PRINT_METHOD = false;
    private int countBeans = 0;
    private int count = 0;

    private final SimpleName simpleName = new SimpleName();

    private final ApplicationContext applicationContext;
    private final ConfigurableListableBeanFactory factory;
    private final ProviderParamsConfig paramsConfig;

    public ApplicationContextProvider(ApplicationContext applicationContext,
                                      ConfigurableListableBeanFactory factory,
                                      ProviderParamsConfig providerParamsConfig)
    {
        this.applicationContext = applicationContext;
        this.factory = factory;
        this.paramsConfig = providerParamsConfig;
    }

    public void handleApplicationContext(){
        int status = -1;
        log.info("****** TEST LOGGER ******* Info -->  Start ApplicationContextProvider  *** info ***");
        TARGET_NAME = paramsConfig.getTARGET_BEAN();// Строка поиска для распечатки списка Бинов null - печатать все Бины
		CNT_METHOD = paramsConfig.getCOUNT_METHOD();// Распечатывает указанное Количество методов Бина
		NUMBER_BEAN = paramsConfig.getLIST_BEAN();  // Распечатывает заданный список бинов и их методы
        if (applicationContext != null && factory != null) {
            printBeanContext();
            status = 15;
        }
        log.info("****** TEST LOGGER ******* Info -->  Exit ApplicationContextProvider  *** info ***");
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
        for( int n : NUMBER_BEAN) {
            if(n == countBeans) { return true; }
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
        System.out.println(" FLAG_TO_STRING=(" + paramsConfig.isORIGINAL_STRING() + ")");
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
            if (paramsConfig.isORIGINAL_STRING()) { // Распечатка исходной строки Бина
                System.out.print("->[" + originalClassName + "]<<<");
            }
            System.out.println(" ");
            if (FLAG_PRINT_METHOD) { // Распечатка Метода
                printAnnotation(annotations);
                printMethods(methods);
            }
        } catch ( NullPointerException e) {
            log.error( " *** ERROR **** NullPointerException: <{}.class>",name , e);
        } catch ( Exception e) {
            e.printStackTrace();
        }
    }

    private void printAnnotation( Annotation[] annotations) {
        for(Annotation annotation : annotations) {
            String str = annotation.toString();
            if (paramsConfig.isORIGINAL_STRING()) {  // Распечатка исходной строки Анотации
                System.out.println("->[" + str + "];");
            }
            System.out.println("  " + simpleName.get( str));
        }
    }

    private void printMethods( Method[] methods){
        int cnt = 0;
        for (Method method : methods) {
          if (cnt >= CNT_METHOD) { // Не печатать Метод если его номер больше заданного
              return;
          }
          Annotation[] annotations = method.getAnnotations();

          String str = method.toString();
          System.out.print("M[" + ++cnt + "]@[" + annotations.length + "]<" + method.getName() + ">");
          System.out.print("(" + method.getParameterCount() + ")");
          if (paramsConfig.isORIGINAL_STRING()) { // Распечатка исходной строки Метода
              System.out.print("->[" + str + "]<<<");
          }
          System.out.println(" ");

          printAnnotation( annotations);

          System.out.println("  " + simpleName.get(str) + "{\n  }");
       }
    }
}
