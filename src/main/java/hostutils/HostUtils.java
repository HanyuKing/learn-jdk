package hostutils;

import org.apache.commons.lang3.StringUtils;

/**
 * @author wanghanyu
 * @create 2017-12-16 14:06
 */
public class HostUtils {
    private static final String http = "http://";
    private static final String https = "https://";

    public static void main(String[] args) {
        String host1 = "http://mprom.shop.jd.com:80/prom/discount/view.action";
        System.out.println(getHost(host1));
        String host2 = "http://mprom.shop.jd.com/prom/discount/view.action";
        System.out.println(getHost(host2));
        String host3 = "https://mprom.shop.jd.com/prom/discount/view.action";
        System.out.println(getHost(host3));
        String host4 = "https://mprom.shop.jd.com:80/prom/discount/view.action";
        System.out.println(getHost(host4));
        String host5 = "http://j-one.jd.com/index#L3N5c3RlbUFwcC9hcHBJbmRleD9pZD0xMDg4JmFwcElkPTEwODgmbWVudT1pbmZvJnN0ZXBGbGFnPXByb2R1Y3RFbnYvcHJvZHVjdEVudg==";
        System.out.println(getHost(host5));
        String host6 = "https://www.google.com.hk/search?q=hibernate+validator+date+pattern&oq=hibernate+validator+date&aqs=chrome.3.69i57j0l5.8383j0j7&sourceid=chrome&ie=UTF-8";
        System.out.println(getHost(host6));
        String host7 = "https://zh.wikipedia.org/wiki/HTTP%E5%A4%B4%E5%AD%97%E6%AE%B5";
        System.out.println(getHost(host7));
    }
    private static String getHost(String host) {
        if(StringUtils.isEmpty(host)) {
            return null;
        }

        int hostIndex = host.indexOf(http) == 0 ? http.length()
                : (host.indexOf(https) == 0 ? https.length() : -1);

        int hostEndIndex = -1;
        if(hostIndex > 0) {
            host = host.substring(hostIndex, host.length());
            if((hostEndIndex = host.indexOf(":")) > 0 || (hostEndIndex = host.indexOf("/")) > 0) { // port  or  action
                host = host.substring(0, hostEndIndex);
            }
        }

        return host;
    }
}
