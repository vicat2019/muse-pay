package interceptor;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * @program: muse-pay
 * @description: 动态代理处理器工具
 * @author: Vincent
 * @create: 2019-01-03 17:30
 **/
public class DynamicProxyHandler implements InvocationHandler {

    private Object business;

    private InterceptorClass interceptor = new InterceptorClass();


    /**
     * 动态生成一个代理对象，并绑定被代理类和代理处理器
     * @param business
     * @return 代理对象
     */
    public Object getProxy(Object business) {
        this.business = business;

        return Proxy.newProxyInstance(business.getClass().getClassLoader(), business.getClass().getInterfaces(),
                this);
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Object result = null;

        interceptor.before();
        result = method.invoke(business, args);
        interceptor.after();

        System.out.println("执行结果=" + result);

        return null;
    }



































}
