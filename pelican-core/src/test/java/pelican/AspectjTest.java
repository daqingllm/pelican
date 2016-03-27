package pelican;

import com.dianping.pelican.annotation.Loggable;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;
import org.testng.annotations.Test;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * Created by liming_liu on 15/10/9.
 */
public class AspectjTest {

    @Loggable
    private IFoo foo = new Foo();

    @Test
    public void testLogging() throws IllegalAccessException {
        prepareCglib();
        Foo.printFoo();
        Foo.printNoLog();
        foo.printAsObject();
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
            @Override
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
        }
    }
}
