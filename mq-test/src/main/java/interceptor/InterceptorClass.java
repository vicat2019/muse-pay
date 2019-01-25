package interceptor;

/**
 * @program: muse-pay
 * @description: 拦截器
 * @author: Vincent
 * @create: 2019-01-03 17:29
 **/
public class InterceptorClass {

    public void before() {
        System.out.println("在拦截器 InterceptorClass 中调用方法: before()");
    }


    public void after() {
        System.out.println("在拦截器 InterceptorClass 中调用方法: after()");
    }

}
