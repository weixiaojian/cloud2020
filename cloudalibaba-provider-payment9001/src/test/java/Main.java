import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.NamingFactory;
import com.alibaba.nacos.api.naming.NamingService;
import com.alibaba.nacos.api.naming.listener.Event;
import com.alibaba.nacos.api.naming.listener.EventListener;
import com.alibaba.nacos.api.naming.listener.NamingEvent;

import java.util.Properties;

/**
 * @author langao_q
 * @since 2021-12-09 10:28
 */
public class Main {

    public static void main(String[] args) throws NacosException {

        Properties properties = new Properties();
        properties.setProperty("serverAddr", System.getProperty("serverAddr"));
        properties.setProperty("namespace", System.getProperty("namespace"));

        NamingService naming = NamingFactory.createNamingService(properties);

        naming.registerInstance("nacos-payment-provider", "11.11.11.11", 8888, "TEST1");

        naming.registerInstance("nacos-payment-provider", "2.2.2.2", 9999, "DEFAULT");

        System.out.println(naming.getAllInstances("nacos-payment-provider"));

        naming.deregisterInstance("nacos-payment-provider", "2.2.2.2", 9999, "DEFAULT");

        System.out.println(naming.getAllInstances("nacos-payment-provider"));

        naming.subscribe("nacos-payment-provider", new EventListener() {
            @Override
            public void onEvent(Event event) {
                System.out.println(((NamingEvent)event).getServiceName());
                System.out.println(((NamingEvent)event).getInstances());
            }
        });
    }

}
