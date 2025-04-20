package uml.stock;

import org.apache.rocketmq.spring.annotation.RocketMQTransactionListener;
import org.apache.rocketmq.spring.core.RocketMQLocalTransactionListener;
import org.apache.rocketmq.spring.core.RocketMQLocalTransactionState;
import org.springframework.messaging.Message;

/**
 * @Author Hanyu.Wang
 * @Date 2025/3/15 21:13
 * @Description
 * @Version 1.0
 **/
public class StockUpdate {
    public void update(Long sku, Long count, Long orderId) {
        //mysqlInsertLog(orderId, sku, count);

    }

    @RocketMQTransactionListener
    class TransactionListenerImpl implements RocketMQLocalTransactionListener {

        @Override
        public RocketMQLocalTransactionState executeLocalTransaction(Message message, Object o) {
            // 1
            //redisLuaUpdate(sku, count);
            // 2
            return null;
        }

        @Override
        public RocketMQLocalTransactionState checkLocalTransaction(Message message) {

            return null;
        }
    }

}
// 5000w
// a(1000),b(5000),c(100),d(1000),e,f

// a,d
// b,c
// a,b,c

/*
a,d
b,c

 */
