package interceptor;

/**
 * @program: muse-pay
 * @description: 测试
 * @author: Vincent
 * @create: 2019-01-03 17:40
 **/
public class TestInterceptor {

    public static void main(String[] args) {
        // 创建动态代理处理工具
        DynamicProxyHandler handler = new DynamicProxyHandler();
        // 创建业务组件对象
        BusinessFace business = new BusinessClass();
        // 创建业务组件对象，并用动态代理绑定代理类
        BusinessFace businessProxy = (BusinessFace) handler.getProxy(business);
        // 调用业务组件中的方法，演示拦截器效果
        businessProxy.doSomething();
    }
}
