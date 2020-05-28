package com.ycc.register.info;

import static com.ycc.register.utils.YmlUtil.getValue;

/**
 * @author MysticalYcc
 * @date 2020/5/28
 */
public class DataInfo {
  public static int port = Integer.parseInt(String.valueOf(getValue("app.yml", "server.port")));
}
