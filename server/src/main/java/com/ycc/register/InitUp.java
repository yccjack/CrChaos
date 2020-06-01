package com.ycc.register;

import java.io.IOException;

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

        }catch (Exception e){

        }
        ;
    }
}
