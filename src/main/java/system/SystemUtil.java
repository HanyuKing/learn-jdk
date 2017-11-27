package system;

import java.io.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class SystemUtil {

    public static void main(String[] args) throws Exception {
        String command = "top -b -n 1";
        List<String> pids = new ArrayList<String>();

        final BufferedReader read = exec(command);

        String line = null;
        while((line = read.readLine())!=null){
            if(line.indexOf("java") > 0) {
                pids.add(line.substring(0, line.trim().indexOf(" ")));
            }
        }
        read.close();

        Map<String, List<String>> tids = new ConcurrentHashMap<String, List<String>>();

        for(String pid : pids) {
            command = "top -b -Hp " + pid + " -n 1";
            BufferedReader read2 = exec(command);

            int maxCount = 10;
            int i = 0;

            List<String> cpus = new ArrayList<String>();
            while(i < maxCount && ((line = read2.readLine())!=null)) {
                // 排除CPU，内存等系统信息
                line = line.trim();
                if(line.equals("") || line.indexOf("top") == 0
                        || line.indexOf("Tasks") == 0 || line.indexOf("Cpu(s)") == 0
                        || line.indexOf("Mem") == 0 || line.indexOf("Swap") == 0
                        || line.indexOf("PID") == 0) {
                    continue;
                }
                String tid = line.substring(0, line.trim().indexOf(" "));
                int bigSIndex = line.lastIndexOf("S");
                String line2 = line.substring(bigSIndex + 1, line.length()).trim();
                String cpu = line2.substring(0, line2.indexOf(" ")).trim();
                cpus.add(tid + ":" + cpu);

                i++;
            }

            read2.close();
            tids.put(pid, cpus);
        }

        Map<String, String> maxCpus = new HashMap();

        for(Map.Entry<String, List<String>> entry : tids.entrySet()) {
            List<String> values = entry.getValue();
            if(values == null || values.size() <= 0) {
                continue;
            }
            Collections.sort(values, new Comparator<String>() {
                public int compare(String o1, String o2) {
                    double result = Double.valueOf(o2.split(":")[1]) - Double.valueOf(o1.split(":")[1]);
                    if(result > 0) {
                        return 1;
                    } else if(result < 0) {
                        return -1;
                    } else {
                        return 0;
                    }
                }
            });
            maxCpus.put(entry.getKey(), entry.getValue().get(0).split(":")[0]);
        }

        for(String pid : pids) {
            String maxCpuT = maxCpus.get(pid);
            if(maxCpuT == null || Double.valueOf(pid).compareTo(0.0) < 0) {
                continue;
            }
            String hxPid = "0x" + Long.toHexString(Long.valueOf(maxCpuT));
            System.out.println(pid + " : " + hxPid);
            BufferedReader read3 = exec("jstack -l " + pid);

            StringBuilder jstack = new StringBuilder();
            String line3 = null;
            int startIndex = 0;
            int endIndex = startIndex;
            int index = 0;
            while((line3 = read3.readLine()) != null) {
                System.out.println(line3);
//                jstack.append(line3).append("\n");
//
//                if(line3.indexOf(hxPid) > 0) {
//                    startIndex = index;
//                    endIndex = startIndex;
//                }
//
//                if(startIndex != 0) {
//                    if(line3.trim().indexOf("\"") >= 0) {
//                        endIndex = index;
//                        break;
//                    }
//                }
//
//                index += line3.length() + 1;
            }

            read3.close();

            System.out.println("pid: " + pid + " tid: " + maxCpuT + " error stack: " + jstack.substring(startIndex, endIndex));
        }
    }

    public static BufferedReader exec(String command) throws IOException, InterruptedException {
        String[] cmds = {"/bin/sh","-c",command};
        Process pro = Runtime.getRuntime().exec(cmds);
        pro.waitFor();
        InputStream in = pro.getInputStream();
        BufferedReader read = new BufferedReader(new InputStreamReader(in));
        return read;
    }
}