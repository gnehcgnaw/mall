package com.beatshadow.mall.mqtt.envent;

import java.util.EventListener;
import java.util.EventObject;
import java.util.Observable;
import java.util.Observer;

/**
 * Java事件/监听器编程模式
 * 观察者API
 * 观察者模式扩展
 *  @see java.util.Observable   可观对象(消息发布者)
 *  @see java.util.Observer  观察者
 *
 *  标准化接口
 *  @see java.util.EventObject   事件对象
 *  @see java.util.EventListener     事件监听器
 * @author : <a href="mailto:gnehcgnaw@gmail.com">gnehcgnaw</a>
 * @since : 2021/1/22 16:03
 */
public class ObserverDemo {
    public static void main(String[] args) {
        EventObservable observable = new EventObservable();
        //为可观察对象添加观察者（监听者）
        observable.addObserver(new EventObserver());
        //发布消息（事件）
        observable.notifyObservers("Hello World");
    }

    static class EventObservable extends Observable {
        @Override
        public synchronized void setChanged() {
            super.setChanged();
        }

        public void notifyObservers(Object arg) {
            setChanged();
            super.notifyObservers(new EventObject(arg));
            clearChanged();
        }
    }

    static class EventObserver implements Observer, EventListener {
        @Override
        public void update(Observable o, Object event) {
            System.out.println("收到事件 ： "+event);
        }
    }
}
