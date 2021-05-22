package com.cloud.x20.ribbon.config;

import com.netflix.client.config.IClientConfig;
import com.netflix.loadbalancer.AbstractLoadBalancerRule;
import com.netflix.loadbalancer.ILoadBalancer;
import com.netflix.loadbalancer.Server;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
 * 接口：
 *      public interface IRule {
 *          public Server choose(Object key);
 *          public void setLoadBalancer(ILoadBalancer lb);
 *          public ILoadBalancer getLoadBalancer();
 *      }
 * 抽象类：
 *      public abstract class AbstractLoadBalancerRule implements IRule, IClientConfigAware {
 *          private ILoadBalancer lb;
 *          @Override
 *          public void setLoadBalancer(ILoadBalancer lb){
 *              this.lb = lb;
 *          }
 *          @Override
 *          public ILoadBalancer getLoadBalancer(){
 *              return lb;
 *          }
 *      }
 * 实现类：
 *      public class RoundRobinRule extends AbstractLoadBalancerRule {}
 *      public class RandomRule extends AbstractLoadBalancerRule {}
 *
 * @author Liuweiwei
 * @since 2021-05-19
 */
public class MyLoadBalanceRule extends AbstractLoadBalancerRule {

    private final Logger LOGGER = LogManager.getLogger(MyLoadBalanceRule.class);

    @Autowired
    private ILoadBalancer lb;

    @Override
    public void initWithNiwsConfig(IClientConfig clientConfig) {
        LOGGER.info("abstract class LoadBalancerRule ->" + clientConfig.getProperties());
    }

    @Override
    public Server choose(Object key) {
        if (lb == null) {
            return null;
        }
        Server server = null;
        int total = 0;
        int currentIndex = 0;
        while (server == null) {
            if (Thread.interrupted()) {
                return null;
            }
            List<Server> upList = lb.getReachableServers();
            List<Server> allList = lb.getAllServers();
            int serverCount = allList.size();
            if (serverCount == 0) {
                return null;
            }
            /*
            int index = chooseRandomInt(serverCount);
            server = upList.get(index);
            */
            if (total < 5) {
                server = upList.get(currentIndex);
                total++;
            } else {
                total = 0;
                currentIndex++;
                if (currentIndex >= upList.size()) {
                    currentIndex = 0;
                }
            }
            if (server == null) {
                Thread.yield();
                continue;
            }
            if (server.isAlive()) {
                return (server);
            }
            server = null;
            Thread.yield();
        }
        return server;
    }

    protected int chooseRandomInt(int serverCount) {
        return ThreadLocalRandom.current().nextInt(serverCount);
    }
}
