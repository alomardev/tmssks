package kfu.ccsit.tmssks.device_simulator.res;

import java.net.URL;

public class R {

    final public static URL get(String resName) {
        return R.class.getResource(resName);
    }
}
