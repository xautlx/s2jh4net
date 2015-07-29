package lab.s2jh.core.mq;

/**
 * 消息（接收）组件监听器接口定义
 */
public interface BrokeredMessageListener {

    /**
     * 开启消息接收监听
     */
    public void startup();

    /**
     * 关闭消息接收监听
     */
    public void shutdown();
}
