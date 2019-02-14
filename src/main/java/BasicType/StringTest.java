package BasicType;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.junit.Test;

/**
 * @author Hanyu King
 * @since 2018-06-28 16:10
 */
public class StringTest {

    @Test
    public void testFormatGCLog() {
        String gcLog = "[SharedHeap::process_strong_roots[SharedHeap::process_strong_roots[SharedHeap::process_strong_roots[SharedHeap::process_strong_roots[SharedHeap::process_strong_roots[SharedHeap::process_strong_roots[SharedHeap::process_strong_roots[SharedHeap::process_strong_roots[SharedHeap::process_strong_roots[SharedHeap::process_strong_roots[SharedHeap::process_strong_roots[SharedHeap::process_strong_roots[SharedHeap::process_strong_roots[SharedHeap::process_strong_roots[SharedHeap::process_strong_roots[SharedHeap::process_strong_roots[SharedHeap::process_strong_roots[SharedHeap::process_strong_roots[SharedHeap::process_strong_roots[SharedHeap::process_strong_roots[SharedHeap::process_strong_roots[SharedHeap::process_strong_roots[SharedHeap::process_strong_roots[SharedHeap::process_strong_roots[SharedHeap::process_strong_roots[SharedHeap::process_strong_roots[SharedHeap::process_strong_roots[SharedHeap::process_strong_roots[SharedHeap::process_strong_roots[SharedHeap::process_strong_roots[SharedHeap::process_strong_roots[SharedHeap::process_strong_roots[SharedHeap::process_strong_roots[SharedHeap::process_strong_roots[SharedHeap::process_strong_roots[SharedHeap::process_strong_roots[SharedHeap::process_strong_roots[SharedHeap::process_strong_roots[SharedHeap::process_strong_roots[SharedHeap::process_strong_roots[SharedHeap::process_strong_roots[SharedHeap::process_strong_roots[SharedHeap::process_strong_roots[SystemDictionary_OOPS_DO[StringTable_possibly_parallel_oops_do[StringTable_possibly_parallel_oops_do[StringTable_possibly_parallel_oops_do[StringTable_possibly_parallel_oops_do[StringTable_possibly_parallel_oops_do[StringTable_possibly_parallel_oops_do[StringTable_possibly_parallel_oops_do[StringTable_possibly_parallel_oops_do[StringTable_possibly_parallel_oops_do[StringTable_possibly_parallel_oops_do[StringTable_possibly_parallel_oops_do[StringTable_possibly_parallel_oops_do[StringTable_possibly_parallel_oops_do[StringTable_possibly_parallel_oops_do[StringTable_possibly_parallel_oops_do[StringTable_possibly_parallel_oops_do[StringTable_possibly_parallel_oops_do[StringTable_possibly_parallel_oops_do[StringTable_possibly_parallel_oops_do[StringTable_possibly_parallel_oops_do[StringTable_possibly_parallel_oops_do[StringTable_possibly_parallel_oops_do[StringTable_possibly_parallel_oops_do[StringTable_possibly_parallel_oops_do[StringTable_possibly_parallel_oops_do[StringTable_possibly_parallel_oops_do[StringTable_possibly_parallel_oops_do, 0.0011190 secs]\n" +
                ", 0.0008810 secs]\n" +
                "[StringTable_possibly_parallel_oops_do[StringTable_possibly_parallel_oops_do[StringTable_possibly_parallel_oops_do[StringTable_possibly_parallel_oops_do, 0.0011710 secs]\n" +
                "[StringTable_possibly_parallel_oops_do[StringTable_possibly_parallel_oops_do[StringTable_possibly_parallel_oops_do[StringTable_possibly_parallel_oops_do[StringTable_possibly_parallel_oops_do[StringTable_possibly_parallel_oops_do, 0.0013440 secs]\n" +
                "[StringTable_possibly_parallel_oops_do[StringTable_possibly_parallel_oops_do, 0.0014080 secs]\n" +
                "[StringTable_possibly_parallel_oops_do, 0.0014450 secs]\n" +
                "[StringTable_possibly_parallel_oops_do, 0.0013850 secs]\n" +
                ", 0.0013210 secs]\n" +
                ", 0.0013040 secs]\n" +
                ", 0.0012880 secs]\n" +
                ", 0.0012380 secs]\n" +
                ", 0.0012040 secs]\n" +
                ", 0.0011570 secs]\n" +
                ", 0.0011080 secs]\n" +
                ", 0.0000250 secs]\n" +
                ", 0.0011150 secs]\n" +
                ", 0.0011130 secs]\n" +
                ", 0.0010260 secs]\n" +
                ", 0.0011270 secs]\n" +
                ", 0.0008280 secs]\n" +
                ", 0.0010250 secs]\n" +
                ", 0.0008390 secs]\n" +
                ", 0.0008360 secs]\n" +
                ", 0.0011150 secs]\n" +
                ", 0.0011180 secs]\n" +
                ", 0.0011110 secs]\n" +
                ", 0.0008240 secs]\n" +
                ", 0.0011070 secs]\n" +
                ", 0.0011260 secs]\n" +
                ", 0.0008160 secs]\n" +
                ", 0.0008340 secs]\n" +
                ", 0.0009710 secs]\n" +
                ", 0.0008350 secs]\n" +
                ", 0.0010880 secs]\n" +
                ", 0.0007830 secs]\n" +
                ", 0.0007990 secs]\n" +
                ", 0.0008380 secs]\n" +
                ", 0.0011220 secs]\n" +
                ", 0.0008090 secs]\n" +
                ", 0.0006540 secs]\n" +
                "[StringTable_possibly_parallel_oops_do, 0.0015020 secs]\n" +
                ", 0.0041990 secs]\n" +
                ", 0.0088860 secs]\n" +
                ", 0.0097190 secs]\n" +
                ", 0.0092850 secs]\n" +
                ", 0.0093310 secs]\n" +
                ", 0.0102290 secs]\n" +
                ", 0.0098990 secs]\n" +
                ", 0.0096310 secs]\n" +
                ", 0.0105430 secs]\n" +
                ", 0.0108390 secs]\n" +
                ", 0.0097710 secs]\n" +
                ", 0.0108040 secs]\n" +
                ", 0.0106210 secs]\n" +
                ", 0.0099310 secs]\n" +
                ", 0.0102200 secs]\n" +
                ", 0.0107670 secs]\n" +
                ", 0.0110420 secs]\n" +
                ", 0.0105650 secs]\n" +
                ", 0.0108720 secs]\n" +
                ", 0.0111550 secs]\n" +
                ", 0.0116770 secs]\n" +
                ", 0.0116210 secs]\n" +
                ", 0.0108000 secs]\n" +
                ", 0.0116470 secs]\n" +
                ", 0.0118270 secs]\n" +
                ", 0.0475670 secs]\n" +
                ", 0.0101030 secs]\n" +
                ", 0.0110890 secs]\n" +
                "[StringTable_possibly_parallel_oops_do, 0.0472310 secs]\n" +
                ", 0.0124810 secs]\n" +
                ", 0.0120450 secs]\n" +
                ", 0.0119950 secs]\n" +
                ", 0.0480920 secs]\n" +
                ", 0.0110790 secs]\n" +
                ", 0.0114850 secs]\n" +
                ", 0.0115100 secs]\n" +
                ", 0.0103760 secs]\n" +
                ", 0.0477640 secs]\n" +
                ", 0.0102690 secs]\n" +
                ", 0.0487940 secs]\n" +
                ", 0.0486250 secs]\n" +
                ", 0.0491140 secs]\n" +
                ", 0.0484270 secs]\n" +
                ", 0.0000140 secs]\n" +
                ", 0.0490030 secs]\n" +
                ", 1.2494170 secs]";

    }

    @Test
    public void testFormat() {
        System.out.println(String.format("adasd%ss%sasd", 123, 456));

        System.out.println(Long.valueOf("123"));
    }

    @Test
    public void findStr() {
        String sourceStr = "{\"code\":200,\"msg\":\"操作成功\",\"attach\":[{\"timestamp\":\"2018-07-26T15:12:13.586773805+08:00\",\"host\":\"10.187.120.142\",\"clientName\":\"10.187.120.142_20180719143342_161938_15_1.4.5-SNAPSHOT_[2018.07.25 10.25.03.229]_[903172125]_15999499_auth\",\"currentVersion\":\"2018-05-30 14:43:30.949776+0800\",\"tempVersion\":\"\",\"spaceId\":\"1967\",\"configVersion\":\"2018-03-28 11:26:17.004350+0800\",\"configId\":0},{\"timestamp\":\"2018-07-26T15:12:32.704762037+08:00\",\"host\":\"10.190.39.66\",\"clientName\":\"10.190.39.66_20180719204158_377177_15_1.4.5-SNAPSHOT_[2018.07.19 21.02.27.948]_[1343491872]_15999499_auth\",\"currentVersion\":\"2018-05-30 14:43:30.949776+0800\",\"tempVersion\":\"\",\"spaceId\":\"1967\",\"configVersion\":\"2018-03-28 11:26:17.004350+0800\",\"configId\":0},{\"timestamp\":\"2018-07-26T15:12:17.701502548+08:00\",\"host\":\"10.190.39.67\",\"clientName\":\"10.190.39.67_20180719204158_397670_15_1.4.5-SNAPSHOT_[2018.07.19 21.02.32.144]_[1844591272]_15999499_auth\",\"currentVersion\":\"2018-05-30 14:43:30.949776+0800\",\"tempVersion\":\"\",\"spaceId\":\"1967\",\"configVersion\":\"2018-03-28 11:26:17.004350+0800\",\"configId\":0},{\"timestamp\":\"2018-07-26T15:12:29.3059048+08:00\",\"host\":\"10.186.32.99\",\"clientName\":\"10.186.32.99_20180719204158_289432_15_1.4.5-SNAPSHOT_[2018.07.19 21.01.24.59]_[1878282622]_15999499_auth\",\"currentVersion\":\"2018-05-30 14:43:30.949776+0800\",\"tempVersion\":\"\",\"spaceId\":\"1967\",\"configVersion\":\"2018-03-28 11:26:17.004350+0800\",\"configId\":0},{\"timestamp\":\"2018-07-26T15:11:37.502579891+08:00\",\"host\":\"10.187.120.148\",\"clientName\":\"10.187.120.148_20180719143342_11918_15_1.4.5-SNAPSHOT_[2018.07.25 10.24.27.340]_[1524028026]_15999499_auth\",\"currentVersion\":\"2018-05-30 14:43:30.949776+0800\",\"tempVersion\":\"\",\"spaceId\":\"1967\",\"configVersion\":\"2018-03-28 11:26:17.004350+0800\",\"configId\":0},{\"timestamp\":\"2018-07-26T15:12:05.587209003+08:00\",\"host\":\"10.189.147.99\",\"clientName\":\"10.189.147.99_z.man.jd.com_149_28_1.4.5-SNAPSHOT_[2017.11.03 21.11.53.132]_[33105947]_auth\",\"currentVersion\":\"2018-05-30 14:43:30.949776+0800\",\"tempVersion\":\"\",\"spaceId\":\"1967\",\"configVersion\":\"2018-03-28 11:26:17.004350+0800\",\"configId\":0},{\"timestamp\":\"2018-07-26T15:11:52.804558002+08:00\",\"host\":\"10.174.113.146\",\"clientName\":\"10.174.113.146_20180725185156_542997_29_1.4.5-SNAPSHOT_[2018.07.25 19.06.48.788]_[1170625287]_15999499_auth\",\"currentVersion\":\"2018-05-30 14:43:30.949776+0800\",\"tempVersion\":\"\",\"spaceId\":\"1967\",\"configVersion\":\"2018-03-28 11:26:17.004350+0800\",\"configId\":0},{\"timestamp\":\"2018-07-26T15:11:44.014083393+08:00\",\"host\":\"10.177.13.15\",\"clientName\":\"10.177.18.51_20180724112910_349935_15_1.4.5-SNAPSHOT_[2018.07.24 11.33.28.225]_[1160373085]_15999499_auth\",\"currentVersion\":\"2018-05-30 14:43:30.949776+0800\",\"tempVersion\":\"\",\"spaceId\":\"1967\",\"configVersion\":\"2018-03-28 11:26:17.004350+0800\",\"configId\":0},{\"timestamp\":\"2018-07-26T15:12:14.273884893+08:00\",\"host\":\"10.185.173.15\",\"clientName\":\"10.185.173.15_20180719204158_382790_15_1.4.5-SNAPSHOT_[2018.07.19 21.05.04.744]_[851113786]_15999499_auth\",\"currentVersion\":\"2018-05-30 14:43:30.949776+0800\",\"tempVersion\":\"\",\"spaceId\":\"1967\",\"configVersion\":\"2018-03-28 11:26:17.004350+0800\",\"configId\":0},{\"timestamp\":\"2018-07-26T15:11:55.351457046+08:00\",\"host\":\"10.174.113.152\",\"clientName\":\"10.174.113.152_20180725185156_292868_29_1.4.5-SNAPSHOT_[2018.07.25 19.02.50.909]_[1204576764]_15999499_auth\",\"currentVersion\":\"2018-05-30 14:43:30.949776+0800\",\"tempVersion\":\"\",\"spaceId\":\"1967\",\"configVersion\":\"2018-03-28 11:26:17.004350+0800\",\"configId\":0},{\"timestamp\":\"2018-07-26T15:12:25.105882626+08:00\",\"host\":\"10.181.50.114\",\"clientName\":\"10.181.50.114_20180319210225_411730_29_1.4.5-SNAPSHOT_[2018.03.19 21.26.04.807]_[826342308]_6943843b_auth\",\"currentVersion\":\"2018-05-30 14:43:30.949776+0800\",\"tempVersion\":\"\",\"spaceId\":\"1967\",\"configVersion\":\"2018-03-28 11:26:17.004350+0800\",\"configId\":0},{\"timestamp\":\"2018-07-26T15:12:33.255295057+08:00\",\"host\":\"10.191.95.120\",\"clientName\":\"10.191.95.120_20180719204158_260194_15_1.4.5-SNAPSHOT_[2018.07.19 21.02.27.608]_[470454380]_15999499_auth\",\"currentVersion\":\"2018-05-30 14:43:30.949776+0800\",\"tempVersion\":\"\",\"spaceId\":\"1967\",\"configVersion\":\"2018-03-28 11:26:17.004350+0800\",\"configId\":0},{\"timestamp\":\"2018-07-26T15:11:49.355813498+08:00\",\"host\":\"10.181.61.51\",\"clientName\":\"10.181.61.51_z.man.jd.com_52_28_1.4.5-SNAPSHOT_[2018.05.07 18.09.12.964]_[165335331]_auth\",\"currentVersion\":\"2018-05-30 14:43:30.949776+0800\",\"tempVersion\":\"\",\"spaceId\":\"1967\",\"configVersion\":\"2018-03-28 11:26:17.004350+0800\",\"configId\":0},{\"timestamp\":\"2018-07-26T15:11:43.279113769+08:00\",\"host\":\"10.190.44.68\",\"clientName\":\"10.190.44.68_20180719143342_647981_15_1.4.5-SNAPSHOT_[2018.07.25 10.25.37.705]_[1389504556]_15999499_auth\",\"currentVersion\":\"2018-05-30 14:43:30.949776+0800\",\"tempVersion\":\"\",\"spaceId\":\"1967\",\"configVersion\":\"2018-03-28 11:26:17.004350+0800\",\"configId\":0},{\"timestamp\":\"2018-07-26T15:11:35.741097845+08:00\",\"host\":\"10.177.13.15\",\"clientName\":\"10.177.17.247_20180709183628_552111_15_1.4.5-SNAPSHOT_[2018.07.09 18.39.22.548]_[2036439117]_15999499_auth\",\"currentVersion\":\"2018-05-30 14:43:30.949776+0800\",\"tempVersion\":\"\",\"spaceId\":\"1967\",\"configVersion\":\"2018-03-28 11:26:17.004350+0800\",\"configId\":0},{\"timestamp\":\"2018-07-26T15:11:51.625846868+08:00\",\"host\":\"10.174.113.145\",\"clientName\":\"10.174.113.145_20180725185156_539599_29_1.4.5-SNAPSHOT_[2018.07.25 19.06.47.695]_[684832610]_15999499_auth\",\"currentVersion\":\"2018-05-30 14:43:30.949776+0800\",\"tempVersion\":\"\",\"spaceId\":\"1967\",\"configVersion\":\"2018-03-28 11:26:17.004350+0800\",\"configId\":0},{\"timestamp\":\"2018-07-26T15:11:38.551154943+08:00\",\"host\":\"10.188.66.90\",\"clientName\":\"10.188.66.90_z.man.jd.com_151_31_1.4.5-SNAPSHOT_[2017.10.31 21.45.36.367]_[520333581]_auth\",\"currentVersion\":\"2018-05-30 14:43:30.949776+0800\",\"tempVersion\":\"\",\"spaceId\":\"1967\",\"configVersion\":\"2018-03-28 11:26:17.004350+0800\",\"configId\":0},{\"timestamp\":\"2018-07-26T15:11:36.665962098+08:00\",\"host\":\"10.186.32.98\",\"clientName\":\"10.186.32.98_20180719204158_286552_15_1.4.5-SNAPSHOT_[2018.07.19 21.01.21.146]_[1401766339]_15999499_auth\",\"currentVersion\":\"2018-05-30 14:43:30.949776+0800\",\"tempVersion\":\"\",\"spaceId\":\"1967\",\"configVersion\":\"2018-03-28 11:26:17.004350+0800\",\"configId\":0},{\"timestamp\":\"2018-07-26T15:12:19.491369219+08:00\",\"host\":\"10.190.44.69\",\"clientName\":\"10.190.44.69_20180719143342_12232_15_1.4.5-SNAPSHOT_[2018.07.25 10.26.14.51]_[9438337]_15999499_auth\",\"currentVersion\":\"2018-05-30 14:43:30.949776+0800\",\"tempVersion\":\"\",\"spaceId\":\"1967\",\"configVersion\":\"2018-03-28 11:26:17.004350+0800\",\"configId\":0},{\"timestamp\":\"2018-07-26T15:12:20.193460033+08:00\",\"host\":\"10.186.32.97\",\"clientName\":\"10.186.32.97_20180719204158_225106_15_1.4.5-SNAPSHOT_[2018.07.19 21.01.17.371]_[1117005366]_15999499_auth\",\"currentVersion\":\"2018-05-30 14:43:30.949776+0800\",\"tempVersion\":\"\",\"spaceId\":\"1967\",\"configVersion\":\"2018-03-28 11:26:17.004350+0800\",\"configId\":0},{\"timestamp\":\"2018-07-26T15:12:28.853160603+08:00\",\"host\":\"10.174.113.148\",\"clientName\":\"10.174.113.148_20180725185156_544018_29_1.4.5-SNAPSHOT_[2018.07.25 19.08.25.88]_[1203234917]_15999499_auth\",\"currentVersion\":\"2018-05-30 14:43:30.949776+0800\",\"tempVersion\":\"\",\"spaceId\":\"1967\",\"configVersion\":\"2018-03-28 11:26:17.004350+0800\",\"configId\":0},{\"timestamp\":\"2018-07-26T15:11:38.807776219+08:00\",\"host\":\"10.187.21.248\",\"clientName\":\"10.187.21.248_20180719143342_650985_15_1.4.5-SNAPSHOT_[2018.07.25 10.24.29.601]_[2063877420]_15999499_auth\",\"currentVersion\":\"2018-05-30 14:43:30.949776+0800\",\"tempVersion\":\"\",\"spaceId\":\"1967\",\"configVersion\":\"2018-03-28 11:26:17.004350+0800\",\"configId\":0},{\"timestamp\":\"2018-07-26T15:12:29.584365947+08:00\",\"host\":\"10.174.113.147\",\"clientName\":\"10.174.113.147_20180725185156_538972_29_1.4.5-SNAPSHOT_[2018.07.25 19.08.25.644]_[2081845460]_15999499_auth\",\"currentVersion\":\"2018-05-30 14:43:30.949776+0800\",\"tempVersion\":\"\",\"spaceId\":\"1967\",\"configVersion\":\"2018-03-28 11:26:17.004350+0800\",\"configId\":0},{\"timestamp\":\"2018-07-26T15:11:45.228023414+08:00\",\"host\":\"10.190.39.69\",\"clientName\":\"10.190.39.69_20180719204158_562687_15_1.4.5-SNAPSHOT_[2018.07.19 21.02.31.688]_[448494669]_15999499_auth\",\"currentVersion\":\"2018-05-30 14:43:30.949776+0800\",\"tempVersion\":\"\",\"spaceId\":\"1967\",\"configVersion\":\"2018-03-28 11:26:17.004350+0800\",\"configId\":0},{\"timestamp\":\"2018-07-26T15:11:39.443616145+08:00\",\"host\":\"10.174.113.151\",\"clientName\":\"10.174.113.151_20180725185156_518994_29_1.4.5-SNAPSHOT_[2018.07.25 19.11.35.635]_[410939776]_15999499_auth\",\"currentVersion\":\"2018-05-30 14:43:30.949776+0800\",\"tempVersion\":\"\",\"spaceId\":\"1967\",\"configVersion\":\"2018-03-28 11:26:17.004350+0800\",\"configId\":0},{\"timestamp\":\"2018-07-26T15:12:12.967988708+08:00\",\"host\":\"10.187.120.141\",\"clientName\":\"10.187.120.141_20180719143342_11584_15_1.4.5-SNAPSHOT_[2018.07.25 10.25.01.830]_[454873997]_15999499_auth\",\"currentVersion\":\"2018-05-30 14:43:30.949776+0800\",\"tempVersion\":\"\",\"spaceId\":\"1967\",\"configVersion\":\"2018-03-28 11:26:17.004350+0800\",\"configId\":0},{\"timestamp\":\"2018-07-26T15:12:31.399599303+08:00\",\"host\":\"10.173.73.92\",\"clientName\":\"10.173.73.92_20180719204158_381380_19_1.4.5-SNAPSHOT_[2018.07.19 21.06.51.333]_[1500563939]_15999499_auth\",\"currentVersion\":\"2018-05-30 14:43:30.949776+0800\",\"tempVersion\":\"\",\"spaceId\":\"1967\",\"configVersion\":\"2018-03-28 11:26:17.004350+0800\",\"configId\":0},{\"timestamp\":\"2018-07-26T15:12:27.147481484+08:00\",\"host\":\"10.186.32.100\",\"clientName\":\"10.186.32.100_20180719204158_250275_15_1.4.5-SNAPSHOT_[2018.07.19 21.01.24.788]_[1849996853]_15999499_auth\",\"currentVersion\":\"2018-05-30 14:43:30.949776+0800\",\"tempVersion\":\"\",\"spaceId\":\"1967\",\"configVersion\":\"2018-03-28 11:26:17.004350+0800\",\"configId\":0},{\"timestamp\":\"2018-07-26T15:11:43.512730853+08:00\",\"host\":\"10.190.44.67\",\"clientName\":\"10.190.44.67_20180719143342_122930_19_1.4.5-SNAPSHOT_[2018.07.25 10.25.37.880]_[493101469]_15999499_auth\",\"currentVersion\":\"2018-05-30 14:43:30.949776+0800\",\"tempVersion\":\"\",\"spaceId\":\"1967\",\"configVersion\":\"2018-03-28 11:26:17.004350+0800\",\"configId\":0},{\"timestamp\":\"2018-07-26T15:12:31.787021967+08:00\",\"host\":\"10.181.50.118\",\"clientName\":\"10.181.50.118_20180725185156_651604_29_1.4.5-SNAPSHOT_[2018.07.25 18.55.27.497]_[843934084]_15999499_auth\",\"currentVersion\":\"2018-05-30 14:43:30.949776+0800\",\"tempVersion\":\"\",\"spaceId\":\"1967\",\"configVersion\":\"2018-03-28 11:26:17.004350+0800\",\"configId\":0},{\"timestamp\":\"2018-07-26T15:12:27.814396494+08:00\",\"host\":\"10.173.73.90\",\"clientName\":\"10.173.73.90_20180719143342_377654_15_1.4.5-SNAPSHOT_[2018.07.25 10.23.21.178]_[1518063683]_15999499_auth\",\"currentVersion\":\"2018-05-30 14:43:30.949776+0800\",\"tempVersion\":\"\",\"spaceId\":\"1967\",\"configVersion\":\"2018-03-28 11:26:17.004350+0800\",\"configId\":0},{\"timestamp\":\"2018-07-26T15:12:03.688757806+08:00\",\"host\":\"10.174.113.149\",\"clientName\":\"10.174.113.149_20180725185156_525298_29_1.4.5-SNAPSHOT_[2018.07.25 19.09.59.973]_[2081845460]_15999499_auth\",\"currentVersion\":\"2018-05-30 14:43:30.949776+0800\",\"tempVersion\":\"\",\"spaceId\":\"1967\",\"configVersion\":\"2018-03-28 11:26:17.004350+0800\",\"configId\":0},{\"timestamp\":\"2018-07-26T15:12:17.413498274+08:00\",\"host\":\"10.174.113.144\",\"clientName\":\"10.174.113.144_20180725185156_540443_29_1.4.5-SNAPSHOT_[2018.07.25 19.05.13.18]_[410939776]_15999499_auth\",\"currentVersion\":\"2018-05-30 14:43:30.949776+0800\",\"tempVersion\":\"\",\"spaceId\":\"1967\",\"configVersion\":\"2018-03-28 11:26:17.004350+0800\",\"configId\":0},{\"timestamp\":\"2018-07-26T15:12:08.600358346+08:00\",\"host\":\"10.173.73.91\",\"clientName\":\"10.173.73.91_20180719204158_397992_15_1.4.5-SNAPSHOT_[2018.07.19 21.05.16.723]_[297607435]_15999499_auth\",\"currentVersion\":\"2018-05-30 14:43:30.949776+0800\",\"tempVersion\":\"\",\"spaceId\":\"1967\",\"configVersion\":\"2018-03-28 11:26:17.004350+0800\",\"configId\":0},{\"timestamp\":\"2018-07-26T15:12:19.212362612+08:00\",\"host\":\"10.191.61.127\",\"clientName\":\"10.191.61.127_20180719204158_323419_15_1.4.5-SNAPSHOT_[2018.07.19 21.02.27.991]_[263382364]_15999499_auth\",\"currentVersion\":\"2018-05-30 14:43:30.949776+0800\",\"tempVersion\":\"\",\"spaceId\":\"1967\",\"configVersion\":\"2018-03-28 11:26:17.004350+0800\",\"configId\":0},{\"timestamp\":\"2018-07-26T15:11:42.241466479+08:00\",\"host\":\"10.181.200.78\",\"clientName\":\"10.181.200.78_z.man.jd.com_178_28_1.4.5-SNAPSHOT_[2017.10.31 21.45.48.541]_[2005934443]_auth\",\"currentVersion\":\"2018-05-30 14:43:30.949776+0800\",\"tempVersion\":\"\",\"spaceId\":\"1967\",\"configVersion\":\"2018-03-28 11:26:17.004350+0800\",\"configId\":0},{\"timestamp\":\"2018-07-26T15:11:42.651371161+08:00\",\"host\":\"10.191.93.211\",\"clientName\":\"10.191.93.211_20180719143342_26761_15_1.4.5-SNAPSHOT_[2018.07.25 10.25.36.947]_[2027510578]_15999499_auth\",\"currentVersion\":\"2018-05-30 14:43:30.949776+0800\",\"tempVersion\":\"\",\"spaceId\":\"1967\",\"configVersion\":\"2018-03-28 11:26:17.004350+0800\",\"configId\":0},{\"timestamp\":\"2018-07-26T15:12:18.938359382+08:00\",\"host\":\"10.189.4.160\",\"clientName\":\"10.189.4.160_z.man.jd.com_142_28_1.4.5-SNAPSHOT_[2017.11.17 16.45.42.615]_[33105947]_auth\",\"currentVersion\":\"2018-05-30 14:43:30.949776+0800\",\"tempVersion\":\"\",\"spaceId\":\"1967\",\"configVersion\":\"2018-03-28 11:26:17.004350+0800\",\"configId\":0},{\"timestamp\":\"2018-07-26T15:12:24.198758924+08:00\",\"host\":\"10.186.32.102\",\"clientName\":\"10.186.32.102_20180719204158_619325_15_1.4.5-SNAPSHOT_[2018.07.19 21.01.21.848]_[604357302]_15999499_auth\",\"currentVersion\":\"2018-05-30 14:43:30.949776+0800\",\"tempVersion\":\"\",\"spaceId\":\"1967\",\"configVersion\":\"2018-03-28 11:26:17.004350+0800\",\"configId\":0},{\"timestamp\":\"2018-07-26T15:12:17.715991844+08:00\",\"host\":\"10.174.113.143\",\"clientName\":\"10.174.113.143_20180725185156_544337_29_1.4.5-SNAPSHOT_[2018.07.25 19.05.13.171]_[131502541]_15999499_auth\",\"currentVersion\":\"2018-05-30 14:43:30.949776+0800\",\"tempVersion\":\"\",\"spaceId\":\"1967\",\"configVersion\":\"2018-03-28 11:26:17.004350+0800\",\"configId\":0},{\"timestamp\":\"2018-07-26T15:12:29.573622967+08:00\",\"host\":\"10.187.98.87\",\"clientName\":\"10.187.98.87_20180724123017_129162_15_1.4.5-SNAPSHOT_[2018.07.24 12.35.43.543]_[529054460]_15999499_auth\",\"currentVersion\":\"2018-05-30 14:43:30.949776+0800\",\"tempVersion\":\"\",\"spaceId\":\"1967\",\"configVersion\":\"2018-03-28 11:26:17.004350+0800\",\"configId\":0},{\"timestamp\":\"2018-07-26T15:12:03.970322738+08:00\",\"host\":\"10.174.113.150\",\"clientName\":\"10.174.113.150_20180725185156_527478_29_1.4.5-SNAPSHOT_[2018.07.25 19.09.59.889]_[55008394]_15999499_auth\",\"currentVersion\":\"2018-05-30 14:43:30.949776+0800\",\"tempVersion\":\"\",\"spaceId\":\"1967\",\"configVersion\":\"2018-03-28 11:26:17.004350+0800\",\"configId\":0},{\"timestamp\":\"2018-07-26T15:12:09.042991456+08:00\",\"host\":\"10.181.2.52\",\"clientName\":\"10.181.2.52_20180719143342_438763_15_1.4.5-SNAPSHOT_[2018.07.25 10.25.02.246]_[980100973]_15999499_auth\",\"currentVersion\":\"2018-05-30 14:43:30.949776+0800\",\"tempVersion\":\"\",\"spaceId\":\"1967\",\"configVersion\":\"2018-03-28 11:26:17.004350+0800\",\"configId\":0},{\"timestamp\":\"2018-07-26T15:11:50.145800198+08:00\",\"host\":\"10.185.173.14\",\"clientName\":\"10.185.173.14_20180719204158_399767_15_1.4.5-SNAPSHOT_[2018.07.19 21.06.34.352]_[1894818837]_15999499_auth\",\"currentVersion\":\"2018-05-30 14:43:30.949776+0800\",\"tempVersion\":\"\",\"spaceId\":\"1967\",\"configVersion\":\"2018-03-28 11:26:17.004350+0800\",\"configId\":0},{\"timestamp\":\"2018-07-26T15:12:06.284746517+08:00\",\"host\":\"10.189.245.156\",\"clientName\":\"10.189.245.156_z.man.jd.com_155_31_1.4.5-SNAPSHOT_[2017.11.23 22.04.35.549]_[203137780]_auth\",\"currentVersion\":\"2018-05-30 14:43:30.949776+0800\",\"tempVersion\":\"\",\"spaceId\":\"1967\",\"configVersion\":\"2018-03-28 11:26:17.004350+0800\",\"configId\":0},{\"timestamp\":\"2018-07-26T15:11:36.275006956+08:00\",\"host\":\"10.184.84.30\",\"clientName\":\"10.184.84.30_20180719143342_173829_15_1.4.5-SNAPSHOT_[2018.07.25 10.24.27.117]_[880020352]_15999499_auth\",\"currentVersion\":\"2018-05-30 14:43:30.949776+0800\",\"tempVersion\":\"\",\"spaceId\":\"1967\",\"configVersion\":\"2018-03-28 11:26:17.004350+0800\",\"configId\":0},{\"timestamp\":\"2018-07-26T15:12:26.586469133+08:00\",\"host\":\"10.173.73.89\",\"clientName\":\"10.173.73.89_20180719143342_149525_15_1.4.5-SNAPSHOT_[2018.07.25 10.23.20.426]_[2116746413]_15999499_auth\",\"currentVersion\":\"2018-05-30 14:43:30.949776+0800\",\"tempVersion\":\"\",\"spaceId\":\"1967\",\"configVersion\":\"2018-03-28 11:26:17.004350+0800\",\"configId\":0}]}";
        String myHost = "10.177.18.51,10.177.17.247,10.191.95.120,10.191.61.127,10.190.39.69,10.190.39.67,10.190.39.66,10.186.32.99,10.186.32.98,10.186.32.97,10.186.32.102,10.186.32.100,10.185.173.15,10.185.173.14,10.173.73.92,10.173.73.91,10.191.93.211,10.190.44.69,10.190.44.68,10.190.44.67,10.181.2.52,10.187.21.248,10.187.120.148,10.187.120.142,10.187.120.141,10.184.84.30,10.187.98.87,10.173.73.90,10.173.73.89,";

        JSONObject jsonObject = JSON.parseObject(sourceStr);

        JSONArray jsonArray = (JSONArray) jsonObject.get("attach");

        for(int i = 0; i < jsonArray.size(); i++) {
            String allHost = jsonArray.getJSONObject(i).getString("host");
            if(!myHost.contains(allHost)) {
                System.out.println(allHost);
            }
        }
    }

    @Test
    public void testJSON() {
        String s = "asdasdad\nsassdfsfsdf";
        System.out.println(JSON.toJSONString(s));
    }

    @Test
    public void testReplaceAll() {
        String s = "aaaaa            aaaaaaa         ";
        System.out.print(s);
        System.out.print("ssssssssssss");
        System.out.print(s.replaceAll(" ", ""));
        System.out.print("ssssssssssss");
    }
}
