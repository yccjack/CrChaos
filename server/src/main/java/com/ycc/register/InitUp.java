package com.ycc.register;

/**
 * @author MysticalYcc
 * @date 2020/5/28
 */
public class InitUp {
    public static void main(String[] args) {
        Server server = new Server();
        try {
            server.init();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        ;
    }
}
