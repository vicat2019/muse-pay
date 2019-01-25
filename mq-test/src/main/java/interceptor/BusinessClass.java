package interceptor;

/**
 * @program: muse-pay
 * @description: 业务组件接口的实现类
 * @author: Vincent
 * @create: 2019-01-03 17:28
 **/
public class BusinessClass implements BusinessFace {
    @Override
    public void doSomething() {
        System.out.println("在业务组件 BusinessClass 中调用方法: doSomething()");
    }
}
