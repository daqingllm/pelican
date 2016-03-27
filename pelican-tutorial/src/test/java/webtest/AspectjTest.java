package webtest;

import com.dianping.pelican.annotation.Loggable;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;
import org.testng.annotations.Test;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * Created by liming_liu on 15/10/9.
 */
public class AspectjTest {

    @Loggable
    private IFoo foo = prepareIHFoo();

    @Test
    public void testLogging() throws IllegalAccessException {
//        prepareCglib();
        Foo.printFoo();
        Foo.printNoLog();
        foo.printAsObject();
    }

    private IFoo prepareIHFoo() {
        MyIH myIH = new MyIH(new Foo());
        return myIH.getFoo();
    }

    private class MyIH implements InvocationHandler {

        private IFoo foo;

        public MyIH(IFoo foo) {
            this.foo = foo;
        }

        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            System.out.println(method.getName() + " invoke!");
            return method.invoke(foo, args);
        }

        public IFoo getFoo() {
            return (IFoo) Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(),
                    new Class[] {IFoo.class}, this);
        }
    }

    private void prepareCglib() throws IllegalAccessException {
        Field[] fields = this.getClass().getDeclaredFields();
        for (Field field : fields) {
            Annotation[] annotations = field.getAnnotations();
            for (Annotation annotation : annotations) {
                if (annotation.annotationType().equals(Loggable.class)) {
                    field.set(this, enhanceField(field.get(this)));
                }
            }
        }
    }

    private Object enhanceField(Object obj) {
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(obj.getClass());
        enhancer.setCallback(new MethodInterceptor() {
//            @Override
            public Object intercept(Object o, Method method, Object[] objects,
                                    MethodProxy methodProxy) throws Throwable {
                Object result;
                System.out.println(method.getName() + " invoke begin!");
                result = methodProxy.invokeSuper(o, objects);
                System.out.println(method.getName() + " invoke end!");
                return result;
            }
        });

        return enhancer.create();
    }

    public interface IFoo {
        void printAsObject();
    }

    public static class Foo implements IFoo {

        public Foo() {
            System.out.println("Foo created!");
        }

        @Loggable
        public static void printFoo() {
            System.out.println("foo!");

        }

        public static void printNoLog() {
            System.out.println("nothing around me!");
        }

        public void printAsObject() {
            System.out.println("object foo!");
            printPrivateLog();
        }

        private void printPrivateLog() {
            System.out.println("this method is private!");
        }
    }
}
