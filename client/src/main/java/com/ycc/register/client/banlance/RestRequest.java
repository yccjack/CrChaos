package com.ycc.register.client.banlance;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
/**
 * @author MysticalYcc
 * @date 2020/5/29
 */
public class RestRequest {
    public Map<String, Set<String>> services = new HashMap<>();

    public void setServices(Map<String, Set<String>> services){
        this.services = services;
    }
}
