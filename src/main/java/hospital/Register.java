package hospital;

import org.joda.time.DateTime;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @Author Hanyu.Wang
 * @Date 2023/5/11 01:29
 * @Description
 * @Version 1.0
 **/
public class Register {
    public static final String TOKEN = "iTFEO5g13LvCizaZmKKk3K2n5nT2qEcUk5cmkzEyQMofX5fHrn/Y0V0ZkC/CQMxMMYWzI8kAxK02l7MZfws73lcSYjzwOrHnYOXPioyQeUU=";


    public static final String PARAMS = "{\"patientCode\":\"09961384\",\"sourceCode\":\"76527||1295\",\"deptCode\":\"312\",\"doctorCode\":\"2464\",\"treatmentDate\":\"2023-05-12\",\"treatmentPeriodType\":\"AM\",\"timeIntervalCode\":\"08:00-08:30\"}";
    public static final String BYB_ZJ_PARAMS = "{\"patientCode\":\"09961384\",\"sourceCode\":\"77148||246\",\"deptCode\":\"213\",\"doctorCode\":\"3030\",\"treatmentDate\":\"2023-05-11\",\"treatmentPeriodType\":\"PM\"}";

    public static final long startTime = 1683755999000L; //;
    public static final long endTime = startTime + 60 * 1000;


    public static void main(String[] args) throws Exception {
        concurrentRob();
    }

    public static void concurrentRob() throws Exception {
        ExecutorService executorService = Executors.newFixedThreadPool(256);

        long now = System.currentTimeMillis();
        while (now < startTime) {
            log(String.format("sleep %d ms", startTime - now));
            Thread.sleep(startTime - now);
            now = System.currentTimeMillis();
        }

        log("start rob...");

        AtomicBoolean success = new AtomicBoolean(false);

        while (!success.get()) {

            if (System.currentTimeMillis() > endTime) {
                break;
            }

            executorService.execute(() -> {
                String execResult = null;
                try {
                    execResult = execCurl(CMDS);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (execResult.contains("SUCCESS")) {
                    success.set(true);
                }
                log(execResult);
            });

            Thread.sleep(1);
        }

        log("end rob...");

        executorService.shutdown();
    }

    public static void log(String x) {
        System.out.println(new DateTime() + ": " + x);
    }

    /**
     * curl
     * -H 'Host: aceso.bjhsyuntai.com'
     * -H 'content-type: application/json;charset=UTF-8'
     * -H 'R-A-Token: Sg+JODkk5Nw+Hjf0NKfwcK39LF8Z9iDBaSity0+ttjM9dG5w5+zK7x1tKZMLnp5Gw97V6vA6MJG3XjsMLMLOqA6InH1LJe6sTFqoWjd9Dww='
     * -H 'A-Ticket: y1g9DMn0ZUbMkxLtEgODO26ITVyqGMAe440Hk7i4PKf97IyCF44gXZF9BEd6xPAdWPAFWmkTLPgyPbi_dj9uGJUGZVRUqsCBuQDm9QXyV8g.'
     * -H 'Hos-Code: 02110001'
     * -H 'U-U-Ticket: YEwPaHf7CNJaFacfHlEONoBQQ7tWrzoVHdXUmyFqbsRRSOr6E7wRN_PPFjC4alZ2cZhxjg..'
     * -H 'Client-Channel: WECHAT_MP'
     * -H 'App-Code: 02110001_HUANZHEDUAN'
     * -H 'User-Agent: Mozilla/5.0 (iPhone; CPU iPhone OS 16_4_1 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Mobile/15E148 MicroMessenger/8.0.37(0x18002526) NetType/WIFI Language/zh_CN'
     * -H 'Referer: https://servicewechat.com/wx8dd7d6fe6d5dda8a/25/page-frame.html'
     * --data-binary '{"patientCode":"09961384","sourceCode":"76527||1295","deptCode":"312","doctorCode":"2464","treatmentDate":"2023-05-12","treatmentPeriodType":"AM","timeIntervalCode":"08:00-08:30"}'
     * --compressed 'https://aceso.bjhsyuntai.com/api/mobile/source/locking'
     */

    public static final String[] CMDS = {"curl",
            "-H", "Host: aceso.bjhsyuntai.com",
            "-H", "content-type: application/json;charset=UTF-8",
            "-H", "R-A-Token: " + TOKEN,
            "-H", "A-Ticket: y1g9DMn0ZUbMkxLtEgODO26ITVyqGMAe440Hk7i4PKf97IyCF44gXZF9BEd6xPAdWPAFWmkTLPgyPbi_dj9uGJUGZVRUqsCBuQDm9QXyV8g.",
            "-H", "Hos-Code: 02110001",
            "-H", "U-U-Ticket: YEwPaHf7CNJaFacfHlEONoBQQ7tWrzoVHdXUmyFqbsRRSOr6E7wRN_PPFjC4alZ2cZhxjg..",
            "-H", "Client-Channel: WECHAT_MP",
            "-H", "App-Code: 02110001_HUANZHEDUAN",
            "-H", "User-Agent: Mozilla/5.0 (iPhone; CPU iPhone OS 16_4_1 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Mobile/15E148 MicroMessenger/8.0.37(0x18002526) NetType/WIFI Language/zh_CN",
            "-H", "Referer: https://servicewechat.com/wx8dd7d6fe6d5dda8a/25/page-frame.html",
            "--data-binary", BYB_ZJ_PARAMS,
            "--compressed", "https://aceso.bjhsyuntai.com/api/mobile/source/locking"};

    public static String execCurl(String[] cmds) throws InterruptedException {
        ProcessBuilder process = new ProcessBuilder(cmds);
        Process p;
        try {
            p = process.start();
            BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
            StringBuilder builder = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                builder.append(line);
                builder.append(System.getProperty("line.separator"));
            }
            return builder.toString();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
