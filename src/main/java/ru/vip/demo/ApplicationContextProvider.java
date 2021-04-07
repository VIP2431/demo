package ru.vip.demo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import ru.vip.demo.aop.LogExecutionTime;
import ru.vip.demo.param.ProviderParamsConfig;
import ru.vip.demo.util.UtilStr;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

@Component
public class ApplicationContextProvider {

    private static final Logger log = LoggerFactory.getLogger(ApplicationContextProvider.class);

    private boolean FLAG_PRINT_METHOD = false;
    private int countBeans = 0;
    private int count = 0;
    private final StringBuffer strBuffer = new StringBuffer( 1000);
//    private final SimpleName simpleName = new SimpleName();

    private final ApplicationContext applicationContext;
    private final ConfigurableListableBeanFactory factory;
    private final ProviderParamsConfig prm;

    public ApplicationContextProvider(ApplicationContext context,
                                      ConfigurableListableBeanFactory factory,
                                      ProviderParamsConfig paramsConfig)
    {
        this.applicationContext = context;
        this.factory = factory;
        // Прием блока параметров из "application.yml" через "providerParamsConfig"
        this.prm = paramsConfig;
    }

    @LogExecutionTime
    public void handleApplicationContext(){
        //int status = -1;
        log.info("** Start *****");
        if (applicationContext != null && factory != null) {
            countBeans = 0;
            count = 0;
            log.info("{}", printHeadContext(strBuffer));
            printBeanContext();
            //status = 15;
            strBuffer.setLength( 0);
            int allBean = applicationContext.getBeanDefinitionCount();
            //strBuffer.append("\n countBeans=[" + count + "/"+ countBeans + "]");
            strBuffer.append("\n countBeans=[");
            strBuffer.append( count);
            strBuffer.append("/");
            strBuffer.append(countBeans);
            strBuffer.append("]");

            strBuffer.append( " allBeans=[");
            strBuffer.append( allBean );
            strBuffer.append("]");

         } else { strBuffer.setLength( 0); }
        strBuffer.append(" ** Exit *****");
        log.info("{}", strBuffer);
        //Runtime.getRuntime().halt( status); // Принудительное завершение Приложения с кодом status
    }

    private StringBuffer printHeadContext( StringBuffer strBuf) {
        strBuf.setLength( 0);
        strBuf.append("\n\r                        ----------------------------\n\r");
        strBuf.append(" Условия: TARGET_NAME=(\"" );
        strBuf.append( prm.getTARGET_BEAN() );
        strBuf.append("\") LIST_BEAN=(");
        int i = 0;
        for( int n : prm.getLIST_BEAN()) {
            if ( i++ > 0 ) strBuf.append(",");
            strBuf.append( n);
        }

        strBuf.append(")  CNT_METHOD=(");
        strBuf.append( prm.getCOUNT_METHOD());
        strBuf.append(") ORIGINAL_STRING=(");
        strBuf.append( prm.isORIGINAL_STRING());
        strBuf.append(")\n                        ----------------------------");
        return strBuf;
    }

    private void printBeanContext() {
         String[] names = applicationContext.getBeanDefinitionNames();
         printHeadContext(strBuffer);
         for(String name : names){
            ++countBeans;
            String originalClassName =  factory.getBeanDefinition(name).getBeanClassName();
            FLAG_PRINT_METHOD = isNUMBER_BEAN();
            if(FLAG_PRINT_METHOD
                    || prm.getTARGET_BEAN().contains("*")
                    || name.contains(prm.getTARGET_BEAN())) {
                printBean( name, originalClassName);
            }
        }
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
            if (prm.isORIGINAL_STRING()) { // Распечатка исходной строки Бина
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

    private void printMethods( Method[] methods){
        int cnt = 0;
        for (Method method : methods) {
            if (cnt >= prm.getCOUNT_METHOD()) { // Не печатать Метод если его номер больше заданного
                return;
            }
            Annotation[] annotations = method.getAnnotations();

            String str = method.toString();
            System.out.print("M[" + ++cnt + "]@[" + annotations.length + "]<" + method.getName() + ">");
            System.out.print("(" + method.getParameterCount() + ")");
            if (prm.isORIGINAL_STRING()) { // Распечатка исходной строки Метода
                System.out.print("->[" + str + "]<<<");
            }
            System.out.println(" ");

            printAnnotation( annotations);

            System.out.println("  " + UtilStr.clearToPoint(str, strBuffer) + "{\n  }");
        }
    }

    private void printAnnotation( Annotation[] annotations) {
        for(Annotation annotation : annotations) {
            String str = annotation.toString();
            if (prm.isORIGINAL_STRING()) {  // Распечатка исходной строки Анотации
                System.out.println("->[" + str + "];");
            }
            System.out.println("  " + UtilStr.clearToPoint( str, strBuffer));
        }
    }

    private boolean isNUMBER_BEAN(){
        for( int n : prm.getLIST_BEAN()) {
            if(n == countBeans) { return true; }
        }
        return false;
    }
}
