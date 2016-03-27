package pelican;

import com.dianping.pelican.util.BeanPopulatingUtils;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by liming_liu on 15/10/14.
 */
public class BeanPoluteTest {

    @Test
    public void test() {
        Map<String, Object> hehe = new HashMap<String, Object>();
        Foo foo = new Foo();
        hehe = BeanPopulatingUtils.extractBeanToMap(foo);
//        BeanPopulatingUtils.populateBeanWithMap(foo, hehe);
        System.out.println(hehe.size());
    }

    public class Foo {
        private String a = "a";
        private String b = "b";
        private String c = "c";
        private String d = "d";

        public String getA() {
            return a;
        }

        public String getB() {
            return b;
        }

        public void setA(String a) {
            this.a = a;
        }

        public void setC(String c) {
            this.c = c;
        }
    }
}
