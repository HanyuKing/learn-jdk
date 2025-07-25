package other;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.GsonBuilder;
import org.apache.dubbo.common.json.GsonUtils;
import org.junit.Test;

import java.util.*;
import java.util.regex.Pattern;

/**
 * @author wanghanyu
 * @create 2017-09-14 15:05
 */
public class JSONTest {
    @Test
    public void parseProductId() {
        String json = "{\n" +
                "    \"code\": 200,\n" +
                "    \"data\": {\n" +
                "        \"page\": 1,\n" +
                "        \"pageSize\": 200,\n" +
                "        \"totalCount\": 140,\n" +
                "        \"hasMore\": false,\n" +
                "        \"dataList\": [\n" +
                "            {\n" +
                "                \"uniqueProductId\": \"330c21aabe084810b890e55f8a68973a\",\n" +
                "                \"uniqueContentId\": \"3f9b66548ff342aa96f515b600b50165\",\n" +
                "                \"uniqueImageId\": \"bfc32b1a55c54ebaab6aee4ecc39f1ea.jjewelry-aigc-api.1ck4BvW1ky_0\",\n" +
                "                \"detail\": \"{\\\"exposeFields\\\":{\\\"productImages\\\":[\\\"https://static.zaohaowu.com/jjewelry/web/resources/product/20250708/12bea7ef4d034de1a0df1c7ab0fbb840/resource/63f6651df47b409dafd06fb25e92d582/8c9c384507621f7c4242dfea6fa1dd82/webp/8c9c384507621f7c4242dfea6fa1dd82.webp\\\",\\\"https://static.zaohaowu.com/jjewelry/web/resources/product/20250708/c5c352fb35a74733a0223d9e9ff7b88a/resource/44a86d01033c429bac3205cc03231668/6c7937763b08d93b4debb38929d02807/webp/6c7937763b08d93b4debb38929d02807.webp\\\"],\\\"price\\\":39.9,\\\"title\\\":\\\"MC蛋糕键帽\\\"}}\",\n" +
                "                \"status\": 1,\n" +
                "                \"productionStatus\": 2,\n" +
                "                \"uniqueUser\": {\n" +
                "                    \"id\": \"290ff560011f48458b29737a2b1ad36a\",\n" +
                "                    \"nickname\": \"CapGenius键设局\",\n" +
                "                    \"avatar\": \"https://static.zaohaowu.com/jjewelry/web/images/avatar/b83b0a4c7ee745c68d1313969d466527/188901c30a072a14ca4f8b573a78de3d.webp\",\n" +
                "                    \"accountLevel\": \"officialQualityCreator\"\n" +
                "                },\n" +
                "                \"modifiedTime\": \"2025-07-08 10:52:26\",\n" +
                "                \"createTime\": \"2025-07-08 10:42:38\"\n" +
                "            },\n" +
                "            {\n" +
                "                \"uniqueProductId\": \"9f48ccb9d886404c90bdd1802302c647\",\n" +
                "                \"uniqueContentId\": \"85871a5e749440dc90464973010476fb\",\n" +
                "                \"uniqueImageId\": \"906e3f7878bc4e1e8caa80ade4fef0c4.jjewelry-aigc-api.l0ipvFzM44_2\",\n" +
                "                \"uniqueWantItId\": \"2e1b64e1aa144a7dbdf9e0951651f3f5\",\n" +
                "                \"detail\": \"{\\\"exposeFields\\\":{\\\"productImages\\\":[\\\"https://static.zaohaowu.com/jjewelry/web/resources/product/20250708/2e214804b2384843a40f8c51cd887fa7/resource/b481be19295c471cb4c8e0bd0b5c2ea7/9341fa86829428029f6af798586c9e01/webp/9341fa86829428029f6af798586c9e01.webp\\\",\\\"https://static.zaohaowu.com/jjewelry/web/resources/product/20250708/1d6db53c1df249cb96721b3479225e10/resource/98008603cb7e4ed884fc719a502b3ebb/6e74cc8372d11e43c4279b4a8d2e0741/webp/6e74cc8372d11e43c4279b4a8d2e0741.webp\\\"],\\\"price\\\":39.9,\\\"title\\\":\\\"惊喜蛋糕键帽\\\"}}\",\n" +
                "                \"status\": 1,\n" +
                "                \"productionStatus\": 2,\n" +
                "                \"uniqueUser\": {\n" +
                "                    \"id\": \"ee8f83c674d24451b37ebc708afe64ad\",\n" +
                "                    \"nickname\": \"Caroline\",\n" +
                "                    \"avatar\": \"https://static.zaohaowu.com/jjewelry/web/images/avatar/cedb12849c08436da3f79619cbd7e133/9f75931b25e14c0a7fcac2883accaefe.webp\",\n" +
                "                    \"accountLevel\": \"officialQualityCreator\"\n" +
                "                },\n" +
                "                \"ticketCountUpdateTime\": \"2025-07-08 09:33:23\",\n" +
                "                \"modifiedTime\": \"2025-07-08 09:25:16\",\n" +
                "                \"createTime\": \"2025-07-08 09:25:11\"\n" +
                "            },\n" +
                "            {\n" +
                "                \"uniqueProductId\": \"867d590ac24541a29df37330ab57c995\",\n" +
                "                \"uniqueContentId\": \"7a84f244e3e0421aa17b97eeb3af76d3\",\n" +
                "                \"uniqueImageId\": \"909a551c0a0a4442ab9d6ec4155bcd67.jjewelry-aigc-api.3TIJ21c88q_2\",\n" +
                "                \"detail\": \"{\\\"exposeFields\\\":{\\\"productImages\\\":[\\\"https://static.zaohaowu.com/jjewelry/web/resources/product/20250708/64ec152c09664c668bb88870873cb247/resource/d771ad0af8fd40ff8a6d8afa843208ec/4c8827ef6c5339801e13824512cfcb0d/webp/4c8827ef6c5339801e13824512cfcb0d.webp\\\",\\\"https://static.zaohaowu.com/jjewelry/web/resources/product/20250708/2670d46d3ed24abdbb9467b804127b9f/resource/2ae566b5dfe64151b3849b3c3f093591/f65fcab4291aaa67b21181068d7c9f07/webp/f65fcab4291aaa67b21181068d7c9f07.webp\\\"],\\\"price\\\":39.9,\\\"title\\\":\\\"咖啡拉花键帽\\\"}}\",\n" +
                "                \"status\": 1,\n" +
                "                \"productionStatus\": 2,\n" +
                "                \"uniqueUser\": {\n" +
                "                    \"id\": \"88fa02ef3a0c44b7979bfc18347e2060\",\n" +
                "                    \"nickname\": \"Now\",\n" +
                "                    \"avatar\": \"https://static.zaohaowu.com/jjewelry/web/images/avatar/ce783a5598194037ac66b7e26075caee/bbb7b97796dcf81cd2af317a8360db52.webp\",\n" +
                "                    \"accountLevel\": \"\"\n" +
                "                },\n" +
                "                \"modifiedTime\": \"2025-07-08 08:46:58\",\n" +
                "                \"createTime\": \"2025-07-08 08:46:55\"\n" +
                "            },\n" +
                "            {\n" +
                "                \"uniqueProductId\": \"1a41a7a8b58e4dca8701a1686959fcea\",\n" +
                "                \"uniqueContentId\": \"5d052e65381a446cbb8aef0483cc6d8b\",\n" +
                "                \"uniqueImageId\": \"0beba9560207449ab2dcd024b85ff9e1.jjewelry-aigc-api.PgFQJNWGYf_3\",\n" +
                "                \"detail\": \"{\\\"exposeFields\\\":{\\\"productStage\\\":0,\\\"productImages\\\":[\\\"https://static.zaohaowu.com/jjewelry/web/resources/product/20250708/26a8217f396648419d76dd0fde0396ca/resource/7395f743ab2d44eb991ec9308f8f053b/ec53fd2dfde8f4b0e2de8173c5121297/webp/ec53fd2dfde8f4b0e2de8173c5121297.webp\\\",\\\"https://static.zaohaowu.com/jjewelry/web/resources/product/20250708/dc8ff68602ec402bb8a0b30408ad417e/resource/6a4eeb52b6cc4555bd0520c3ab89a939/45d58c28faa386036aa6d2d9e7f40711/webp/45d58c28faa386036aa6d2d9e7f40711.webp\\\"],\\\"price\\\":39.9,\\\"title\\\":\\\"指点江山键帽\\\"}}\",\n" +
                "                \"status\": 1,\n" +
                "                \"productionStatus\": 2,\n" +
                "                \"uniqueUser\": {\n" +
                "                    \"id\": \"8755e54e019f419b8495681da67acb78\",\n" +
                "                    \"nickname\": \"Listz\",\n" +
                "                    \"avatar\": \"https://static.zaohaowu.com/jjewelry/web/images/avatar/51bc51d6668d4aeb8702717783d8989f/d3661e58711a92749f00c9ca1c420fd8.webp\",\n" +
                "                    \"accountLevel\": \"\"\n" +
                "                },\n" +
                "                \"modifiedTime\": \"2025-07-08 07:57:30\",\n" +
                "                \"createTime\": \"2025-07-08 07:57:27\"\n" +
                "            },\n" +
                "            {\n" +
                "                \"uniqueProductId\": \"2682f7ecaec2461484eb53ca27fddae4\",\n" +
                "                \"uniqueContentId\": \"ecd389b3c21241718af65d3f399591b8\",\n" +
                "                \"uniqueImageId\": \"1aae7b2d116f4e52b76ef59c07aa93ac.jjewelry-aigc-api.2gfqlHakbR_0\",\n" +
                "                \"detail\": \"{\\\"exposeFields\\\":{\\\"productStage\\\":0,\\\"productImages\\\":[\\\"https://static.zaohaowu.com/jjewelry/web/resources/product/20250708/1b5c733d81db4572a28eaedda18d7c56/resource/75c0c86b763b4ee99abe31034880e125/d0ae513c81a40a8284626dbdc4ff440b/webp/d0ae513c81a40a8284626dbdc4ff440b.webp\\\",\\\"https://static.zaohaowu.com/jjewelry/web/resources/product/20250708/b6179d05235041da809391a73e016afc/resource/4be4416e6558409391dcf577cfb2e3e9/1bc3f4ae2f21a397a30aaa2cbaadeb9c/webp/1bc3f4ae2f21a397a30aaa2cbaadeb9c.webp\\\"],\\\"price\\\":39.9,\\\"title\\\":\\\"滑板少女键帽\\\"}}\",\n" +
                "                \"status\": 1,\n" +
                "                \"productionStatus\": 2,\n" +
                "                \"uniqueUser\": {\n" +
                "                    \"id\": \"68f4bc049a3e4adda9498b23422a7069\",\n" +
                "                    \"nickname\": \"Laney\",\n" +
                "                    \"avatar\": \"https://static.zaohaowu.com/jjewelry/web/images/avatar/b7136a9e994547d39cf271779933abdb/938c5c54ee6438e98480a0c2256f8688.webp\",\n" +
                "                    \"accountLevel\": \"\"\n" +
                "                },\n" +
                "                \"modifiedTime\": \"2025-07-08 07:55:47\",\n" +
                "                \"createTime\": \"2025-07-08 07:55:43\"\n" +
                "            },\n" +
                "            {\n" +
                "                \"uniqueProductId\": \"35c663c4e515464e9a550d74b6f18aa3\",\n" +
                "                \"uniqueContentId\": \"de8c14aa973c401a9e096ee203e014c9\",\n" +
                "                \"uniqueImageId\": \"b8841f23dff542c4b0412275328ea68c.jjewelry-aigc-api.AYdpYRKoW6_0\",\n" +
                "                \"detail\": \"{\\\"exposeFields\\\":{\\\"productStage\\\":0,\\\"productImages\\\":[\\\"https://static.zaohaowu.com/jjewelry/web/resources/product/20250708/f003353fc9664c839fcae09f822c6304/resource/6362569c509d4c89a73a7ae77ecab558/b17e1a40094b270db6faad4f331be727/webp/b17e1a40094b270db6faad4f331be727.webp\\\",\\\"https://static.zaohaowu.com/jjewelry/web/resources/product/20250708/a310494bb5ba4d9ea266184e2c389f8c/resource/d7b37a1466e84f19b77ba669e9dcd5ec/55f540abfe09c476f7b74dd97a3552ba/webp/55f540abfe09c476f7b74dd97a3552ba.webp\\\"],\\\"price\\\":39.9,\\\"title\\\":\\\"草地方块键帽\\\"}}\",\n" +
                "                \"status\": 1,\n" +
                "                \"productionStatus\": 2,\n" +
                "                \"uniqueUser\": {\n" +
                "                    \"id\": \"871b8463759e4c01a4dfb3102a3911ec\",\n" +
                "                    \"nickname\": \"不现实商店Odditystore\",\n" +
                "                    \"avatar\": \"https://static.zaohaowu.com/jjewelry/web/images/avatar/bc227d2160a2482cb59210dfb69c86cc/bc1a75664631b42a9c0ceb90781beb5e.webp\",\n" +
                "                    \"accountLevel\": \"officialQualityCreator\"\n" +
                "                },\n" +
                "                \"modifiedTime\": \"2025-07-08 11:04:22\",\n" +
                "                \"createTime\": \"2025-07-08 07:53:39\"\n" +
                "            },\n" +
                "            {\n" +
                "                \"uniqueProductId\": \"f2b195da381b4b39ac33f87abe2326f0\",\n" +
                "                \"uniqueContentId\": \"b05078e97f3f4b0eb75a343c826bd59a\",\n" +
                "                \"uniqueImageId\": \"4b7eaa34c9104c399d4db5da31e1d25d.jjewelry-aigc-api.e5uW6nU75J_2\",\n" +
                "                \"detail\": \"{\\\"exposeFields\\\":{\\\"productStage\\\":0,\\\"productImages\\\":[\\\"https://static.zaohaowu.com/jjewelry/web/resources/product/20250708/d754ba9244034450be05791373896cc1/resource/27fffee2448642eab47cbb0935e3709e/e3cdf63b45e36eb95b1dd54e8f9c0e4e/webp/e3cdf63b45e36eb95b1dd54e8f9c0e4e.webp\\\",\\\"https://static.zaohaowu.com/jjewelry/web/resources/product/20250708/ae1f7f45b1664b0a956b7c9290b797ac/resource/9605416b2a5b44feae4c54f77d6f9193/3d208c8ce8cee67da702226824e89f9a/webp/3d208c8ce8cee67da702226824e89f9a.webp\\\"],\\\"price\\\":39.9,\\\"title\\\":\\\"像素树苗键帽\\\"}}\",\n" +
                "                \"status\": 1,\n" +
                "                \"productionStatus\": 2,\n" +
                "                \"uniqueUser\": {\n" +
                "                    \"id\": \"290ff560011f48458b29737a2b1ad36a\",\n" +
                "                    \"nickname\": \"CapGenius键设局\",\n" +
                "                    \"avatar\": \"https://static.zaohaowu.com/jjewelry/web/images/avatar/b83b0a4c7ee745c68d1313969d466527/188901c30a072a14ca4f8b573a78de3d.webp\",\n" +
                "                    \"accountLevel\": \"officialQualityCreator\"\n" +
                "                },\n" +
                "                \"modifiedTime\": \"2025-07-08 07:49:34\",\n" +
                "                \"createTime\": \"2025-07-08 07:49:28\"\n" +
                "            },\n" +
                "            {\n" +
                "                \"uniqueProductId\": \"3f5bea686de94874865d78f6e2abd3da\",\n" +
                "                \"uniqueContentId\": \"8a0320ed064447f08ee213dac1fce99a\",\n" +
                "                \"uniqueImageId\": \"9556212c5db749628b3e009f2b0e1704.jjewelry-aigc-api.V1ykVjxm7D_0\",\n" +
                "                \"detail\": \"{\\\"exposeFields\\\":{\\\"productImages\\\":[\\\"https://static.zaohaowu.com/jjewelry/web/resources/product/20250708/591bedc398224b9fa7e236f5a35439da/resource/0c509fb6ee974f218ee21991c06f436b/934462ac87ae57b7d6d79ef142f3f7ce/webp/934462ac87ae57b7d6d79ef142f3f7ce.webp\\\",\\\"https://static.zaohaowu.com/jjewelry/web/resources/product/20250708/1cdd9b62e9e24830916274340eddf289/resource/c49e7178024646c9a63bccf17defefe0/a760e54583e19f0b5ee98a1896c5f3f1/webp/a760e54583e19f0b5ee98a1896c5f3f1.webp\\\"],\\\"price\\\":39.9,\\\"title\\\":\\\"秘果奇遇键帽\\\"}}\",\n" +
                "                \"status\": 1,\n" +
                "                \"productionStatus\": 2,\n" +
                "                \"uniqueUser\": {\n" +
                "                    \"id\": \"290ff560011f48458b29737a2b1ad36a\",\n" +
                "                    \"nickname\": \"CapGenius键设局\",\n" +
                "                    \"avatar\": \"https://static.zaohaowu.com/jjewelry/web/images/avatar/b83b0a4c7ee745c68d1313969d466527/188901c30a072a14ca4f8b573a78de3d.webp\",\n" +
                "                    \"accountLevel\": \"officialQualityCreator\"\n" +
                "                },\n" +
                "                \"modifiedTime\": \"2025-07-08 08:02:34\",\n" +
                "                \"createTime\": \"2025-07-08 06:48:17\"\n" +
                "            },\n" +
                "            {\n" +
                "                \"uniqueProductId\": \"516613d8e09942ebbfd178b06c9aea7b\",\n" +
                "                \"uniqueContentId\": \"9a5fffd028224718933eb1f07b727294\",\n" +
                "                \"uniqueImageId\": \"b4b2e0ff66d34c97acca065064b11a23.jjewelry-aigc-api.SGZLiKgtby_2\",\n" +
                "                \"uniqueWantItId\": \"10a980617b194a2d93df5be34254ccc4\",\n" +
                "                \"detail\": \"{\\\"exposeFields\\\":{\\\"productStage\\\":1,\\\"productImages\\\":[\\\"https://static.zaohaowu.com/jjewelry/web/resources/product/20250626/328025fc2c4b4b7f8bace63837820565/resource/3d087a70dfb84be98e813fa9b0d95916/fb0a042b20e78a320af42f80c0c16267/webp/fb0a042b20e78a320af42f80c0c16267.webp\\\",\\\"https://static.zaohaowu.com/jjewelry/web/resources/product/20250626/f660ee2e7b1243f79ac596cc9cc8ede8/resource/763f40246db54c578d2f15875772ae3c/09887569f91873883d32dfa2a7d60d12/webp/09887569f91873883d32dfa2a7d60d12.webp\\\"],\\\"price\\\":29.9,\\\"title\\\":\\\"游灯会戒指\\\"}}\",\n" +
                "                \"status\": 1,\n" +
                "                \"productionStatus\": 2,\n" +
                "                \"uniqueUser\": {\n" +
                "                    \"id\": \"ad22ef6af0334a9096bcb89fe569b4c2\",\n" +
                "                    \"nickname\": \"Yolo\",\n" +
                "                    \"avatar\": \"https://static.zaohaowu.com/jjewelry/web/images/avatar/f5704b8ccddc4b13b1ce1fa79c1a7734/e1f83f8756c2a0a2fa4703adbe4e096c.webp\",\n" +
                "                    \"accountLevel\": \"officialQualityCreator\"\n" +
                "                },\n" +
                "                \"ticketCountUpdateTime\": \"2025-06-26 08:25:00\",\n" +
                "                \"modifiedTime\": \"2025-07-08 02:33:54\",\n" +
                "                \"createTime\": \"2025-06-26 07:53:43\"\n" +
                "            },\n" +
                "            {\n" +
                "                \"uniqueProductId\": \"e09a62c804b04c49ae18690caa9c97b6\",\n" +
                "                \"uniqueContentId\": \"6f9bf90e579442cea813cf3657302518\",\n" +
                "                \"uniqueImageId\": \"e312a372a98b4439893670e066cb7b0a.jjewelry-aigc-api.hvVZzPjg2P_1\",\n" +
                "                \"uniqueWantItId\": \"f430bb8b448a47bf9cec3ff2e279babc\",\n" +
                "                \"detail\": \"{\\\"exposeFields\\\":{\\\"productStage\\\":1,\\\"productImages\\\":[\\\"https://static.zaohaowu.com/jjewelry/web/resources/product/20250704/1a0f21efddc049f1966ee6c55c7f6098/resource/6ea09351899b4d259b5c6e09062bcc02/a5eef95a3700b39d6b260cfc2b52f2c4/webp/a5eef95a3700b39d6b260cfc2b52f2c4.webp\\\",\\\"https://static.zaohaowu.com/jjewelry/web/resources/product/20250704/07623016908648cf9b1b619160e00e28/resource/ff3d2656612e4269a92eec936d9ccbc5/eba142d1de96ac7666caf19f8c1ba431/webp/eba142d1de96ac7666caf19f8c1ba431.webp\\\"],\\\"price\\\":29.9,\\\"title\\\":\\\"小恶魔戒指\\\"}}\",\n" +
                "                \"status\": 1,\n" +
                "                \"productionStatus\": 2,\n" +
                "                \"uniqueUser\": {\n" +
                "                    \"id\": \"68488d4fe7d7441084c7aa0060258d93\",\n" +
                "                    \"nickname\": \"我好想你不分黑夜白天.\",\n" +
                "                    \"avatar\": \"https://static.zaohaowu.com/jjewelry/web/images/avatar/d4d4b8b01d9e44b9ae943e29b67f1724/1da80304e7ec2f659841e63f6aaf75c5.webp\",\n" +
                "                    \"accountLevel\": \"\"\n" +
                "                },\n" +
                "                \"ticketCountUpdateTime\": \"2025-07-04 07:32:31\",\n" +
                "                \"modifiedTime\": \"2025-07-08 02:33:18\",\n" +
                "                \"createTime\": \"2025-07-04 07:32:31\"\n" +
                "            },\n" +
                "            {\n" +
                "                \"uniqueProductId\": \"dcdaf6b337704c478ab2890c3d6c7351\",\n" +
                "                \"uniqueContentId\": \"1923488229774899b34bf9d9d7f4b446\",\n" +
                "                \"uniqueImageId\": \"a5cd48d0fed4414bbf898882721436cc.jjewelry-aigc-api.V8LG5fs3ko_1\",\n" +
                "                \"uniqueWantItId\": \"2989f1c3cc934864bb41105640eb67aa\",\n" +
                "                \"detail\": \"{\\\"exposeFields\\\":{\\\"productStage\\\":1,\\\"productImages\\\":[\\\"https://static.zaohaowu.com/jjewelry/web/resources/product/20250704/21dc723992ec4f5495cf518a844977d0/resource/54ab151f300a4d34a2c945369dd3238e/639283226af6926b1cd833ba148868ac/webp/639283226af6926b1cd833ba148868ac.webp\\\",\\\"https://static.zaohaowu.com/jjewelry/web/resources/product/20250704/f67eb2c256fd47d8809192214031ad1e/resource/de074c927abf4682a02b6ab6f46bdfcf/cef904f5db2e06a63e92893d2841d4f4/webp/cef904f5db2e06a63e92893d2841d4f4.webp\\\"],\\\"price\\\":29.9,\\\"title\\\":\\\"Doki幽灵鬼鬼戒指\\\"}}\",\n" +
                "                \"status\": 1,\n" +
                "                \"productionStatus\": 2,\n" +
                "                \"uniqueUser\": {\n" +
                "                    \"id\": \"68488d4fe7d7441084c7aa0060258d93\",\n" +
                "                    \"nickname\": \"我好想你不分黑夜白天.\",\n" +
                "                    \"avatar\": \"https://static.zaohaowu.com/jjewelry/web/images/avatar/d4d4b8b01d9e44b9ae943e29b67f1724/1da80304e7ec2f659841e63f6aaf75c5.webp\",\n" +
                "                    \"accountLevel\": \"\"\n" +
                "                },\n" +
                "                \"ticketCountUpdateTime\": \"2025-07-04 07:35:46\",\n" +
                "                \"modifiedTime\": \"2025-07-08 02:32:55\",\n" +
                "                \"createTime\": \"2025-07-04 07:35:46\"\n" +
                "            },\n" +
                "            {\n" +
                "                \"uniqueProductId\": \"74671743d5a24ceabb07e562bc51edd3\",\n" +
                "                \"uniqueContentId\": \"9dacb9dcf6c9439babbe42eed6f9b0d1\",\n" +
                "                \"uniqueImageId\": \"38f288cc63ab441f9eaf3f436346b44a.jjewelry-aigc-api.oaWSFvN2nu_3\",\n" +
                "                \"uniqueWantItId\": \"ecf7a498232b4194afa7a4b3b0f8c2dc\",\n" +
                "                \"detail\": \"{\\\"exposeFields\\\":{\\\"productStage\\\":1,\\\"productImages\\\":[\\\"https://static.zaohaowu.com/jjewelry/web/resources/product/20250701/fc790a21a7cd467aa83d8b0f4dda7350/resource/9868333f8e4b46a685647778cbb1665b/3becefd8d32161163e19654a6a72c1ac/webp/3becefd8d32161163e19654a6a72c1ac.webp\\\",\\\"https://static.zaohaowu.com/jjewelry/web/resources/product/20250701/379f3916e9194fddb68206036c1a5ccc/resource/becaae3258d34c37926fb792c5347447/9b67df35cfed80086e26a200ee83e71b/webp/9b67df35cfed80086e26a200ee83e71b.webp\\\"],\\\"price\\\":29.9,\\\"title\\\":\\\"花朵戒指\\\"}}\",\n" +
                "                \"status\": 1,\n" +
                "                \"productionStatus\": 2,\n" +
                "                \"uniqueUser\": {\n" +
                "                    \"id\": \"e72b819d6a9d4f48aeab75f5b28189ca\",\n" +
                "                    \"nickname\": \"甜钰子\",\n" +
                "                    \"avatar\": \"https://static.zaohaowu.com/jjewelry/web/images/avatar/user-HMQoCE-2.png\",\n" +
                "                    \"accountLevel\": \"\"\n" +
                "                },\n" +
                "                \"ticketCountUpdateTime\": \"2025-07-01 07:09:32\",\n" +
                "                \"modifiedTime\": \"2025-07-08 02:33:32\",\n" +
                "                \"createTime\": \"2025-07-01 06:34:13\"\n" +
                "            },\n" +
                "            {\n" +
                "                \"uniqueProductId\": \"002306d942854e2ea9f7a8a7613475c1\",\n" +
                "                \"uniqueContentId\": \"2bf5007216d94270a483dac0a9c8d7c8\",\n" +
                "                \"uniqueImageId\": \"fa3ff57c5c4440dca45ce991896d0a5d.jjewelry-aigc-api.MSgabtLK99_0\",\n" +
                "                \"uniqueWantItId\": \"878e3d07ef684c69888450665e439cc4\",\n" +
                "                \"detail\": \"{\\\"exposeFields\\\":{\\\"productStage\\\":1,\\\"productImages\\\":[\\\"https://static.zaohaowu.com/jjewelry/web/resources/product/20250610/3722f468781f4b899e8685611fa20820/resource/17387f3d92444e8891e1fe74cc403515/47a0d639e8cc6ab7a2e5677931f148a1/webp/47a0d639e8cc6ab7a2e5677931f148a1.webp\\\",\\\"https://static.zaohaowu.com/jjewelry/web/resources/product/20250610/ff83d3a0a74d4c728cfc4c3ae9fa3a53/resource/7b3ffa0e68254032a821aa831a55f9af/9e4a0f5f3f534553a54c390b90884c81/webp/9e4a0f5f3f534553a54c390b90884c81.webp\\\"],\\\"price\\\":39.9,\\\"title\\\":\\\"机器人与兔子键帽\\\"}}\",\n" +
                "                \"status\": 1,\n" +
                "                \"productionStatus\": 2,\n" +
                "                \"uniqueUser\": {\n" +
                "                    \"id\": \"4e1be92e4e0949ed9fecc1dc0025f26b\",\n" +
                "                    \"nickname\": \"huassaue\",\n" +
                "                    \"avatar\": \"https://static.zaohaowu.com/jjewelry/web/images/avatar/d9ed23cc36f44ab6b85884d7dcd3aeb0/f3053858faef04d4f599810df643bdea.webp\",\n" +
                "                    \"accountLevel\": \"\"\n" +
                "                },\n" +
                "                \"ticketCountUpdateTime\": \"2025-06-12 15:09:18\",\n" +
                "                \"modifiedTime\": \"2025-07-06 17:14:59\",\n" +
                "                \"createTime\": \"2025-06-10 11:04:49\"\n" +
                "            },\n" +
                "            {\n" +
                "                \"uniqueProductId\": \"80c2a610fefc47f18eeee2d027e091ce\",\n" +
                "                \"uniqueContentId\": \"6c1af2713ee44660aad9d7317ba3ce5b\",\n" +
                "                \"uniqueImageId\": \"489b49b7b72740d496eeca58bffa6945.jjewelry-aigc-api.KVKDD9Kpxv_0\",\n" +
                "                \"uniqueWantItId\": \"850a829a9f804f1eaab0d855870f50de\",\n" +
                "                \"detail\": \"{\\\"exposeFields\\\":{\\\"productStage\\\":1,\\\"productImages\\\":[\\\"https://static.zaohaowu.com/jjewelry/web/resources/product/20250619/95f1f40585294a549c62182f87132096/resource/a938c6f4a9bc4f179391d3961b183484/a4af3a1708d242ec7778996cf2cca2b4/webp/a4af3a1708d242ec7778996cf2cca2b4.webp\\\",\\\"https://static.zaohaowu.com/jjewelry/web/resources/product/20250619/3e540e7a5a644ab3b6e85ac2f1a4d5ab/resource/17b5eebef4d94f87b482a2540ef7a279/d9f7bc80a35702b597a9c9a41b29a748/webp/d9f7bc80a35702b597a9c9a41b29a748.webp\\\"],\\\"price\\\":39.9,\\\"title\\\":\\\"罗曼城键帽\\\"}}\",\n" +
                "                \"status\": 1,\n" +
                "                \"productionStatus\": 2,\n" +
                "                \"uniqueUser\": {\n" +
                "                    \"id\": \"73faebe5dd3f4a0e8adbec7d001922f1\",\n" +
                "                    \"nickname\": \"Whiskey \",\n" +
                "                    \"avatar\": \"https://static.zaohaowu.com/jjewelry/web/images/avatar/1ff6998d5bd04ca2ad67ec7a1265bfb1/d0d163ce198446ffdc998e0e2aa579d3.webp\",\n" +
                "                    \"accountLevel\": \"\"\n" +
                "                },\n" +
                "                \"ticketCountUpdateTime\": \"2025-06-27 04:50:58\",\n" +
                "                \"modifiedTime\": \"2025-07-06 17:13:28\",\n" +
                "                \"createTime\": \"2025-06-19 13:36:56\"\n" +
                "            },\n" +
                "            {\n" +
                "                \"uniqueProductId\": \"31bd5d7840054404a9441b50bcec40a3\",\n" +
                "                \"uniqueContentId\": \"ef01f47687364a85b73287165b52f3fe\",\n" +
                "                \"uniqueImageId\": \"27a7d2e08f534091ae8b7b960a4e3c83.jjewelry-aigc-api.M6o0dnLTqg_3\",\n" +
                "                \"uniqueWantItId\": \"735f572ec0254eedb4098e5b6a930d7c\",\n" +
                "                \"detail\": \"{\\\"exposeFields\\\":{\\\"productStage\\\":0,\\\"productImages\\\":[\\\"https://static.zaohaowu.com/jjewelry/web/resources/product/20250704/61a7ccdb4c3d4ea2b7c2734f75f22d37/resource/3ff5e2151b3d4fd8b815f61ab5d999ce/ac6b025960d9d487954e622adb2ea9ed/webp/ac6b025960d9d487954e622adb2ea9ed.webp\\\",\\\"https://static.zaohaowu.com/jjewelry/web/resources/product/20250704/bbb3915b5a2c406189ae345ff5fed5e9/resource/bb6ef564a8d54b8ab90aeca5587b0874/61bd581e32f32971a2470270f9b30363/webp/61bd581e32f32971a2470270f9b30363.webp\\\"],\\\"price\\\":29.9,\\\"title\\\":\\\"水獭宝宝戒指\\\"}}\",\n" +
                "                \"status\": 1,\n" +
                "                \"productionStatus\": 2,\n" +
                "                \"uniqueUser\": {\n" +
                "                    \"id\": \"6ae3543c12494d1da11ebc8853ba55b7\",\n" +
                "                    \"nickname\": \"兔屎咖啡\",\n" +
                "                    \"avatar\": \"https://static.zaohaowu.com/jjewelry/web/images/avatar/user-HMQoCE-2.png\",\n" +
                "                    \"accountLevel\": \"\"\n" +
                "                },\n" +
                "                \"ticketCountUpdateTime\": \"2025-07-04 07:37:54\",\n" +
                "                \"modifiedTime\": \"2025-07-06 17:01:42\",\n" +
                "                \"createTime\": \"2025-07-04 07:37:54\"\n" +
                "            },\n" +
                "            {\n" +
                "                \"uniqueProductId\": \"df871992ba2e4fc597276d51e36cfdb8\",\n" +
                "                \"uniqueContentId\": \"427f2bfff7474a61b318332ed1e18dd6\",\n" +
                "                \"uniqueImageId\": \"8a2db0df6124416ebd04b59aba4e362a.jjewelry-aigc-api.lq9CRIWYxR_0\",\n" +
                "                \"uniqueWantItId\": \"e61223280b7d46918cc66d3a49d9a063\",\n" +
                "                \"detail\": \"{\\\"exposeFields\\\":{\\\"productStage\\\":0,\\\"productImages\\\":[\\\"https://static.zaohaowu.com/jjewelry/web/resources/product/20250701/ee3e78a8fd7f42efaf3895ec5a2d8afd/resource/997f9df868e340f6839763c13d6b5693/79ed5ce40e24a58970bc584776bb198b/webp/79ed5ce40e24a58970bc584776bb198b.webp\\\",\\\"https://static.zaohaowu.com/jjewelry/web/resources/product/20250701/ae496f43427d4f5a98f320699d3c57d4/resource/b9e0857022794019b7e5dffd11848035/b2ac55fbe7b810418a5264b526ca2a14/webp/b2ac55fbe7b810418a5264b526ca2a14.webp\\\"],\\\"price\\\":29.9,\\\"title\\\":\\\"夏日清凉戒指\\\"}}\",\n" +
                "                \"status\": 1,\n" +
                "                \"productionStatus\": 2,\n" +
                "                \"uniqueUser\": {\n" +
                "                    \"id\": \"eb40816897f34008b5355e655421922a\",\n" +
                "                    \"nickname\": \"123造好物er\",\n" +
                "                    \"avatar\": \"https://static.zaohaowu.com/jjewelry/web/images/avatar/user-HMQoCE-2.png\",\n" +
                "                    \"accountLevel\": \"\"\n" +
                "                },\n" +
                "                \"ticketCountUpdateTime\": \"2025-07-01 07:18:44\",\n" +
                "                \"modifiedTime\": \"2025-07-06 17:01:44\",\n" +
                "                \"createTime\": \"2025-07-01 06:43:18\"\n" +
                "            },\n" +
                "            {\n" +
                "                \"uniqueProductId\": \"e1677616689544ff93f7c738b1f265fa\",\n" +
                "                \"uniqueContentId\": \"15f259ae4be445458756cce140b64e0c\",\n" +
                "                \"uniqueImageId\": \"ded6fa87fed748a78fa70f7bbc08916f.jjewelry-aigc-api.8Jp71pGtkN_2\",\n" +
                "                \"uniqueWantItId\": \"de35b11246fa401fbfd9a2a4ad7d2e44\",\n" +
                "                \"detail\": \"{\\\"exposeFields\\\":{\\\"productStage\\\":1,\\\"productImages\\\":[\\\"https://static.zaohaowu.com/jjewelry/web/resources/product/20250625/454ec19bcd474427baea90c1e441c7a5/resource/923cf6e0f7864076a3e2c7e7892f0ddc/46971289ded4238c69901a81c2f2e04d/webp/46971289ded4238c69901a81c2f2e04d.webp\\\",\\\"https://static.zaohaowu.com/jjewelry/web/resources/product/20250625/f10314983aff488ea48e4081d969155f/resource/24dd8d10f9d7402cb944d1e33e81140a/2aa71a81f725c5920e3422c613f24377/webp/2aa71a81f725c5920e3422c613f24377.webp\\\"],\\\"price\\\":29.9,\\\"title\\\":\\\"优雅的美人鱼戒指\\\"}}\",\n" +
                "                \"status\": 1,\n" +
                "                \"productionStatus\": 2,\n" +
                "                \"uniqueUser\": {\n" +
                "                    \"id\": \"059fb74c3bcb4e44ae392195f7d70bca\",\n" +
                "                    \"nickname\": \"鸢尾\",\n" +
                "                    \"avatar\": \"https://static.zaohaowu.com/jjewelry/web/images/avatar/249ac84b04744b1394af619cffe8e4c0/69bc678c7c60eca30f62b69e14ef5a14.webp\",\n" +
                "                    \"accountLevel\": \"officialQualityCreator\"\n" +
                "                },\n" +
                "                \"ticketCountUpdateTime\": \"2025-06-25 04:35:09\",\n" +
                "                \"modifiedTime\": \"2025-07-06 16:53:49\",\n" +
                "                \"createTime\": \"2025-06-25 04:12:20\"\n" +
                "            },\n" +
                "            {\n" +
                "                \"uniqueProductId\": \"2ef0d58322a64e6cbd6663c75a443a53\",\n" +
                "                \"uniqueContentId\": \"b250fb9316484484a4383c28311fc505\",\n" +
                "                \"uniqueImageId\": \"5786c30f53634f91b52cfc7f5c833b5a.jjewelry-aigc-api.hZVQsVoDul_3\",\n" +
                "                \"uniqueWantItId\": \"17c43e03667041268ef3eecfc6eca9d5\",\n" +
                "                \"detail\": \"{\\\"exposeFields\\\":{\\\"productStage\\\":1,\\\"productImages\\\":[\\\"https://static.zaohaowu.com/jjewelry/web/resources/product/20250625/76d0ebf2196147ea97789e37a53fdcad/resource/1b7444e7182d4b358e6074eb4b1b3ada/8b0cad4e10fe49bb3e4b3ce8d42547d9/webp/8b0cad4e10fe49bb3e4b3ce8d42547d9.webp\\\",\\\"https://static.zaohaowu.com/jjewelry/web/resources/product/20250625/ec49f05555ec402d890ca8be40dae02a/resource/78ee7f74b981400ab028ff2594cc9c97/9f4a1f5929a44603bf99b5fb965c60ec/webp/9f4a1f5929a44603bf99b5fb965c60ec.webp\\\"],\\\"price\\\":29.9,\\\"title\\\":\\\"没睡醒的小猫戒指\\\"}}\",\n" +
                "                \"status\": 1,\n" +
                "                \"productionStatus\": 2,\n" +
                "                \"uniqueUser\": {\n" +
                "                    \"id\": \"6639e45b2c514acfbc14f456897848cb\",\n" +
                "                    \"nickname\": \"造好物er-lQ3dSHqS\",\n" +
                "                    \"avatar\": \"https://static.zaohaowu.com/jjewelry/web/images/avatar/user-HMQoCE-2.png\",\n" +
                "                    \"accountLevel\": \"\"\n" +
                "                },\n" +
                "                \"ticketCountUpdateTime\": \"2025-06-25 04:18:35\",\n" +
                "                \"modifiedTime\": \"2025-07-06 16:53:51\",\n" +
                "                \"createTime\": \"2025-06-25 04:18:28\"\n" +
                "            },\n" +
                "            {\n" +
                "                \"uniqueProductId\": \"af9a01deedab49d0a59a5ecb6220f19b\",\n" +
                "                \"uniqueContentId\": \"116b7458f0d640bb832661b3cd7a0746\",\n" +
                "                \"uniqueImageId\": \"ae3e1803884a45f29d11e9c2d1655fd6.jjewelry-aigc-api.NtVKMh6Fd8_3\",\n" +
                "                \"uniqueWantItId\": \"1b3187cbaabf4b1b9644925e0a646b9a\",\n" +
                "                \"detail\": \"{\\\"exposeFields\\\":{\\\"productStage\\\":0,\\\"productImages\\\":[\\\"https://static.zaohaowu.com/jjewelry/web/resources/product/20250704/dc623547e80043e08d09684288f5a5fd/resource/0ba58b060a344942b61d602eba9b9869/7d0997e7399c9321beb9e4fdee982f34/webp/7d0997e7399c9321beb9e4fdee982f34.webp\\\",\\\"https://static.zaohaowu.com/jjewelry/web/resources/product/20250704/19b89789029c401cb1eb66cc2c27c572/resource/2a6a95f1d6a845209bbd1a1782754097/7129f90595b3af4f50f08c4a34b84584/webp/7129f90595b3af4f50f08c4a34b84584.webp\\\"],\\\"price\\\":29.9,\\\"title\\\":\\\"火焰素圈戒指\\\"}}\",\n" +
                "                \"status\": 1,\n" +
                "                \"productionStatus\": 2,\n" +
                "                \"uniqueUser\": {\n" +
                "                    \"id\": \"bb1737a36f144322929663983362b9a6\",\n" +
                "                    \"nickname\": \"造好物er-82fc8c35\",\n" +
                "                    \"avatar\": \"https://static.zaohaowu.com/jjewelry/web/images/avatar/user-HMQoCE-2.png\",\n" +
                "                    \"accountLevel\": \"\"\n" +
                "                },\n" +
                "                \"ticketCountUpdateTime\": \"2025-07-04 07:26:21\",\n" +
                "                \"modifiedTime\": \"2025-07-06 16:53:52\",\n" +
                "                \"createTime\": \"2025-07-04 07:26:20\"\n" +
                "            },\n" +
                "            {\n" +
                "                \"uniqueProductId\": \"f63d59c32c3243b98fc7282ccaf52e1d\",\n" +
                "                \"uniqueContentId\": \"8fa7c309b50e4d708c5f988976493c6d\",\n" +
                "                \"uniqueImageId\": \"450d707a7c594cff9ebe77f976022c71.jjewelry-aigc-api.Wl7RrNiQIk_2\",\n" +
                "                \"uniqueWantItId\": \"e378ee4932394be3986f11cb80b5f161\",\n" +
                "                \"detail\": \"{\\\"exposeFields\\\":{\\\"productStage\\\":1,\\\"productImages\\\":[\\\"https://static.zaohaowu.com/jjewelry/web/resources/product/20250625/8facf387b19e43b7b1ff9b92949da6a1/resource/f04c5bb8d71c4b35b558207b76554879/79fadb261fe3bd643a34ffbd70c0874d/webp/79fadb261fe3bd643a34ffbd70c0874d.webp\\\",\\\"https://static.zaohaowu.com/jjewelry/web/resources/product/20250625/c4769d6c847c412986f1089b4dfcf17a/resource/ff7b1c599db34219b29150568510c3e7/0a71b9383c1f66ba4aad1cd4e196712c/webp/0a71b9383c1f66ba4aad1cd4e196712c.webp\\\"],\\\"price\\\":29.9,\\\"title\\\":\\\"缪斯戒指\\\"}}\",\n" +
                "                \"status\": 1,\n" +
                "                \"productionStatus\": 2,\n" +
                "                \"uniqueUser\": {\n" +
                "                    \"id\": \"51061baaa2084547ada33b1cfb637e57\",\n" +
                "                    \"nickname\": \"豹豹宝宝饱饱\",\n" +
                "                    \"avatar\": \"https://static.zaohaowu.com/jjewelry/web/images/avatar/e449004602374f2f83dc8329882aca6e/95cd4c4dfde3f1191f7082d4c591c7db.webp\",\n" +
                "                    \"accountLevel\": \"\"\n" +
                "                },\n" +
                "                \"ticketCountUpdateTime\": \"2025-06-25 03:59:01\",\n" +
                "                \"modifiedTime\": \"2025-07-06 16:53:55\",\n" +
                "                \"createTime\": \"2025-06-25 03:58:42\"\n" +
                "            },\n" +
                "            {\n" +
                "                \"uniqueProductId\": \"d5fb18c9a11a429a81344929977e3f2c\",\n" +
                "                \"uniqueContentId\": \"36bcd70eb09a4463b6d8f8daf734f6f7\",\n" +
                "                \"uniqueImageId\": \"a7e0e80f7d094c6b8df7728b4f689472.jjewelry-aigc-api.Er79fpQi3T_2\",\n" +
                "                \"uniqueWantItId\": \"109a0776f88741958508d2623fb9cdb4\",\n" +
                "                \"detail\": \"{\\\"exposeFields\\\":{\\\"productStage\\\":1,\\\"productImages\\\":[\\\"https://static.zaohaowu.com/jjewelry/web/resources/product/20250625/99d41730dd6e44bb966c841cc774c552/resource/ad037c6d191f44c991fedc88d512172b/e7b9ff42c03283ff8357c350607a6a95/webp/e7b9ff42c03283ff8357c350607a6a95.webp\\\",\\\"https://static.zaohaowu.com/jjewelry/web/resources/product/20250625/68d983821eb24db19d734086328b4b4b/resource/7e7ce48e72224915ad9e6bea594ed3a7/51516b0dc8bfcfd5354d1d361b527343/webp/51516b0dc8bfcfd5354d1d361b527343.webp\\\"],\\\"price\\\":29.9,\\\"title\\\":\\\"ONE火山戒指\\\"}}\",\n" +
                "                \"status\": 1,\n" +
                "                \"productionStatus\": 2,\n" +
                "                \"uniqueUser\": {\n" +
                "                    \"id\": \"d155107da31d4f9baa095bf5cb5ea330\",\n" +
                "                    \"nickname\": \"o米o\",\n" +
                "                    \"avatar\": \"https://static.zaohaowu.com/jjewelry/web/images/avatar/22b5bb5a26d9472f85d37730d1bfdbb2/d63aaf30bda4888ccd9eed126bb40208.webp\",\n" +
                "                    \"accountLevel\": \"\"\n" +
                "                },\n" +
                "                \"ticketCountUpdateTime\": \"2025-06-26 05:22:14\",\n" +
                "                \"modifiedTime\": \"2025-07-06 16:53:56\",\n" +
                "                \"createTime\": \"2025-06-25 04:02:19\"\n" +
                "            },\n" +
                "            {\n" +
                "                \"uniqueProductId\": \"46eb86b87cef4c74b32cb470d0da87fd\",\n" +
                "                \"uniqueContentId\": \"e905efb70bb5477893f4dd62e6ffb27b\",\n" +
                "                \"uniqueImageId\": \"38512a57e7fa4b3093f3cdcfa2052cb1.jjewelry-aigc-api.nP1OtLNzRc_3\",\n" +
                "                \"uniqueWantItId\": \"ea6da381df1e4906b81b6380b2e2678b\",\n" +
                "                \"detail\": \"{\\\"exposeFields\\\":{\\\"productStage\\\":1,\\\"productImages\\\":[\\\"https://static.zaohaowu.com/jjewelry/web/resources/product/20250625/f889b36e3e874751b3c5e9f902f50af2/resource/40bab4f979e14397828bddc8bef5830d/2cc326b3cb1ef90aa39f9a19212449f1/webp/2cc326b3cb1ef90aa39f9a19212449f1.webp\\\",\\\"https://static.zaohaowu.com/jjewelry/web/resources/product/20250625/34ec8f40b682444195797d2ba0443fd3/resource/7050d63fbb3a4af58bf2ab5a3b1f364f/c77dd3a11ca03f859762cfa7d01079f7/webp/c77dd3a11ca03f859762cfa7d01079f7.webp\\\"],\\\"price\\\":29.9,\\\"title\\\":\\\"小猫咪戒指\\\"}}\",\n" +
                "                \"status\": 1,\n" +
                "                \"productionStatus\": 2,\n" +
                "                \"uniqueUser\": {\n" +
                "                    \"id\": \"56f8e06c1c4843f2ae87b2d815b7e2c1\",\n" +
                "                    \"nickname\": \"Milk\",\n" +
                "                    \"avatar\": \"https://static.zaohaowu.com/jjewelry/web/images/avatar/f5368ca77f8e4c7b9e262b02452380f1/ff9abd18cd95847a0e260b1cd8902fc5.webp\",\n" +
                "                    \"accountLevel\": \"\"\n" +
                "                },\n" +
                "                \"ticketCountUpdateTime\": \"2025-07-05 10:55:46\",\n" +
                "                \"modifiedTime\": \"2025-07-06 16:53:58\",\n" +
                "                \"createTime\": \"2025-06-25 04:34:04\"\n" +
                "            },\n" +
                "            {\n" +
                "                \"uniqueProductId\": \"fe971a82044e446ab16681350fac019b\",\n" +
                "                \"uniqueContentId\": \"9bd06ea06a03435789120d650a272e1a\",\n" +
                "                \"uniqueImageId\": \"94e91ed8263d4ab1a3440645c51bb83e.jjewelry-aigc-api.7GX2inPPoH_1\",\n" +
                "                \"uniqueWantItId\": \"feed9f617b344233af31cf356fe2d5a9\",\n" +
                "                \"detail\": \"{\\\"exposeFields\\\":{\\\"productStage\\\":1,\\\"productImages\\\":[\\\"https://static.zaohaowu.com/jjewelry/web/resources/product/20250625/70a69bb1c0d24e24bb6937f3331c7f76/resource/42f4b931b23b4d938c99a83c9e2de13c/e363e23739cc954e603b4d5ffadc4e57/webp/e363e23739cc954e603b4d5ffadc4e57.webp\\\",\\\"https://static.zaohaowu.com/jjewelry/web/resources/product/20250625/314c134b381048c9ae037116118e74e8/resource/b4758c6d002b48a4b3dfe852b0562a9d/9376145e0cef77be3e5e0d489f26adb1/webp/9376145e0cef77be3e5e0d489f26adb1.webp\\\"],\\\"price\\\":29.9,\\\"title\\\":\\\"小狮子戒指\\\"}}\",\n" +
                "                \"status\": 1,\n" +
                "                \"productionStatus\": 2,\n" +
                "                \"uniqueUser\": {\n" +
                "                    \"id\": \"56f8e06c1c4843f2ae87b2d815b7e2c1\",\n" +
                "                    \"nickname\": \"Milk\",\n" +
                "                    \"avatar\": \"https://static.zaohaowu.com/jjewelry/web/images/avatar/f5368ca77f8e4c7b9e262b02452380f1/ff9abd18cd95847a0e260b1cd8902fc5.webp\",\n" +
                "                    \"accountLevel\": \"\"\n" +
                "                },\n" +
                "                \"ticketCountUpdateTime\": \"2025-07-05 10:56:46\",\n" +
                "                \"modifiedTime\": \"2025-07-06 16:54:00\",\n" +
                "                \"createTime\": \"2025-06-25 04:36:06\"\n" +
                "            },\n" +
                "            {\n" +
                "                \"uniqueProductId\": \"878ebdb671284eb2876fb189c640ad78\",\n" +
                "                \"uniqueContentId\": \"4a21a1d2e74844d480da3c83bbd4dd22\",\n" +
                "                \"uniqueImageId\": \"8aa996151d6d476594cc697761657cf4.jjewelry-aigc-api.at7QPGpUz4_0\",\n" +
                "                \"uniqueWantItId\": \"d585c1cb01774714bebe36d2df58a0fd\",\n" +
                "                \"detail\": \"{\\\"exposeFields\\\":{\\\"productStage\\\":1,\\\"productImages\\\":[\\\"https://static.zaohaowu.com/jjewelry/web/resources/product/20250619/6bfbe0ad20ce4d71908f31dc26936c11/resource/e8958117d60b41929211add40ee93098/27df6de71dc2e0cd736be59533840308/webp/27df6de71dc2e0cd736be59533840308.webp\\\",\\\"https://static.zaohaowu.com/jjewelry/web/resources/product/20250619/c09ae50dfec649df9488e127ea3efa40/resource/8b6a97bcc7f64e48abbe6431f2eeb81c/a91590515f6eed50c900165ec98f4e6f/webp/a91590515f6eed50c900165ec98f4e6f.webp\\\"],\\\"price\\\":29.9,\\\"title\\\":\\\"芭比粉闪电戒指\\\"}}\",\n" +
                "                \"status\": 1,\n" +
                "                \"productionStatus\": 2,\n" +
                "                \"uniqueUser\": {\n" +
                "                    \"id\": \"1f0304b08f664343a7baca99206b0fcd\",\n" +
                "                    \"nickname\": \"Fishball RQ\",\n" +
                "                    \"avatar\": \"https://static.zaohaowu.com/jjewelry/web/images/avatar/5e769bdbb9224f859c54a43fca2eaf46/2a36c55236c4f6846295b4df2597c5ab.webp\",\n" +
                "                    \"accountLevel\": \"officialQualityCreator\"\n" +
                "                },\n" +
                "                \"ticketCountUpdateTime\": \"2025-06-19 14:31:34\",\n" +
                "                \"modifiedTime\": \"2025-07-06 16:51:20\",\n" +
                "                \"createTime\": \"2025-06-19 13:17:55\"\n" +
                "            },\n" +
                "            {\n" +
                "                \"uniqueProductId\": \"bf8a3df3efce480587a24c0977875eb4\",\n" +
                "                \"uniqueContentId\": \"0ddd17b5c30f4a21b8d49efb8a173c59\",\n" +
                "                \"uniqueImageId\": \"d00544548832462e82d86002024cbc70.jjewelry-aigc-api.TmIrm0baZI_1\",\n" +
                "                \"uniqueWantItId\": \"2084b26d4d7f4639960453bf47d2ba1a\",\n" +
                "                \"detail\": \"{\\\"exposeFields\\\":{\\\"productStage\\\":1,\\\"productImages\\\":[\\\"https://static.zaohaowu.com/jjewelry/web/resources/product/20250625/97950a60d74f40c4a377d82fdd754541/resource/7ba4991fd8164047944eb6cba0610722/b2dca8566c002a1d6df1202ab4cfab8e/webp/b2dca8566c002a1d6df1202ab4cfab8e.webp\\\",\\\"https://static.zaohaowu.com/jjewelry/web/resources/product/20250625/9e5736d01cd14e2c972b65dda565162f/resource/d3eb0df688c0443d8a63d060dfc864b5/8a4f6f6f6f67c276edd860dd5e48e147/webp/8a4f6f6f6f67c276edd860dd5e48e147.webp\\\"],\\\"price\\\":29.9,\\\"title\\\":\\\"请你吃爆米花戒指\\\"}}\",\n" +
                "                \"status\": 1,\n" +
                "                \"productionStatus\": 2,\n" +
                "                \"uniqueUser\": {\n" +
                "                    \"id\": \"ee8f83c674d24451b37ebc708afe64ad\",\n" +
                "                    \"nickname\": \"Caroline\",\n" +
                "                    \"avatar\": \"https://static.zaohaowu.com/jjewelry/web/images/avatar/cedb12849c08436da3f79619cbd7e133/9f75931b25e14c0a7fcac2883accaefe.webp\",\n" +
                "                    \"accountLevel\": \"officialQualityCreator\"\n" +
                "                },\n" +
                "                \"ticketCountUpdateTime\": \"2025-06-25 03:51:06\",\n" +
                "                \"modifiedTime\": \"2025-07-06 16:51:21\",\n" +
                "                \"createTime\": \"2025-06-25 03:29:08\"\n" +
                "            },\n" +
                "            {\n" +
                "                \"uniqueProductId\": \"abfc3a4a083a4b6196ac4651eb74fee1\",\n" +
                "                \"uniqueContentId\": \"a35b495962ec49ba9076f69e339bdec3\",\n" +
                "                \"uniqueImageId\": \"c253aaa7e0894da5837ea95c380a8987.jjewelry-aigc-api.RrknMRDQXT_3\",\n" +
                "                \"uniqueWantItId\": \"2e194a771c0f463c9d286e821088f14d\",\n" +
                "                \"detail\": \"{\\\"exposeFields\\\":{\\\"productStage\\\":1,\\\"productImages\\\":[\\\"https://static.zaohaowu.com/jjewelry/web/resources/product/20250625/2edec8db9f0b48da819f12b9d4d167c7/resource/9ff6eb05d86547b3bd841b89f43de89c/fefe5466e9ebfd4ba3d5ee3e3100d719/webp/fefe5466e9ebfd4ba3d5ee3e3100d719.webp\\\",\\\"https://static.zaohaowu.com/jjewelry/web/resources/product/20250625/a31b31c1e3c849feaeba002ead1da4ca/resource/5abee8b8b3c64bb99ed431f4a6da4d76/ad7e2bbf2870fc7ec3cdcb778d8646ed/webp/ad7e2bbf2870fc7ec3cdcb778d8646ed.webp\\\"],\\\"price\\\":29.9,\\\"title\\\":\\\"牛马のOK戒\\\"}}\",\n" +
                "                \"status\": 1,\n" +
                "                \"productionStatus\": 2,\n" +
                "                \"uniqueUser\": {\n" +
                "                    \"id\": \"b8c7f60162114780a3e51cc1ae6d7954\",\n" +
                "                    \"nickname\": \"AI摸鱼姐\",\n" +
                "                    \"avatar\": \"https://static.zaohaowu.com/jjewelry/web/images/avatar/6ad6169be9cb498f87cd5ad235be2b1d/4c63073849c3aefc2521c6765e84536f.webp\",\n" +
                "                    \"accountLevel\": \"officialQualityCreator\"\n" +
                "                },\n" +
                "                \"ticketCountUpdateTime\": \"2025-06-25 04:36:38\",\n" +
                "                \"modifiedTime\": \"2025-07-06 16:51:23\",\n" +
                "                \"createTime\": \"2025-06-25 03:41:01\"\n" +
                "            },\n" +
                "            {\n" +
                "                \"uniqueProductId\": \"df82e6e84c3d45ee97d897d43fb56f26\",\n" +
                "                \"uniqueContentId\": \"d804d1a5fb5f429999efa1a5bed1bb3b\",\n" +
                "                \"uniqueImageId\": \"028fc5e9ad484e29b4325646da796de8.jjewelry-aigc-api.SKssn0ZVke_0\",\n" +
                "                \"uniqueWantItId\": \"a7cefa136b86463ab85920bf9f2d450c\",\n" +
                "                \"detail\": \"{\\\"exposeFields\\\":{\\\"productStage\\\":1,\\\"productImages\\\":[\\\"https://static.zaohaowu.com/jjewelry/web/resources/product/20250619/b0a9df5ca58141499a1177cc4f5a2ba1/resource/d72cfa0da8da4102892bf9aeee90ca97/3cc8f0748edaa553c5cec1efa6875a40/webp/3cc8f0748edaa553c5cec1efa6875a40.webp\\\",\\\"https://static.zaohaowu.com/jjewelry/web/resources/product/20250619/02ee9c8329d8429f91b90dc8604247d6/resource/6ccb66d01c0b4bd6a38a0eabe05b06d2/b0ff4d9dd1f29561754011d0e5307bc0/webp/b0ff4d9dd1f29561754011d0e5307bc0.webp\\\"],\\\"price\\\":29.9,\\\"title\\\":\\\"小房子戒指\\\"}}\",\n" +
                "                \"status\": 1,\n" +
                "                \"productionStatus\": 2,\n" +
                "                \"uniqueUser\": {\n" +
                "                    \"id\": \"c7b1a4b9f0bb4a8f9d1db70469d8f468\",\n" +
                "                    \"nickname\": \"苹果糖\",\n" +
                "                    \"avatar\": \"https://static.zaohaowu.com/jjewelry/web/images/avatar/92fea6edb5b94e37aa888d59046c711c/571eee083b3599ec914ce37f846da3e5.webp\",\n" +
                "                    \"accountLevel\": \"\"\n" +
                "                },\n" +
                "                \"ticketCountUpdateTime\": \"2025-06-27 16:14:16\",\n" +
                "                \"modifiedTime\": \"2025-07-06 16:51:24\",\n" +
                "                \"createTime\": \"2025-06-19 13:25:32\"\n" +
                "            },\n" +
                "            {\n" +
                "                \"uniqueProductId\": \"19f63ed6fe084326a69cadf3568b57b0\",\n" +
                "                \"uniqueContentId\": \"04b3f35cdc9e42d9ab7e04e84bce619e\",\n" +
                "                \"uniqueImageId\": \"6a6188c8107b470784a3bc2db01bde8e.jjewelry-aigc-api.aHgIt40Vry_0\",\n" +
                "                \"uniqueWantItId\": \"171b1403b788403d928d4980c2e27f57\",\n" +
                "                \"detail\": \"{\\\"exposeFields\\\":{\\\"productStage\\\":1,\\\"productImages\\\":[\\\"https://static.zaohaowu.com/jjewelry/web/resources/product/20250619/9a43b128ff3f4532b97381f6cded992c/resource/b4c18e348c73488fa282a40de986ddab/c0b84c5b4e1d4e91d19f63a8cb63d2a9/webp/c0b84c5b4e1d4e91d19f63a8cb63d2a9.webp\\\",\\\"https://static.zaohaowu.com/jjewelry/web/resources/product/20250619/c3ec38f2f4674cb2acc396cc455000ab/resource/ff6c0b704c4f4376a3a7ed05887de241/39275d1c2ef16d4df871d53d007d9fbd/webp/39275d1c2ef16d4df871d53d007d9fbd.webp\\\"],\\\"price\\\":29.9,\\\"title\\\":\\\"草莓波点戒指\\\"}}\",\n" +
                "                \"status\": 1,\n" +
                "                \"productionStatus\": 2,\n" +
                "                \"uniqueUser\": {\n" +
                "                    \"id\": \"14fed238cb784ca08213a8c6afe92876\",\n" +
                "                    \"nickname\": \"looooo\",\n" +
                "                    \"avatar\": \"https://static.zaohaowu.com/jjewelry/web/images/avatar/5d536bbcf17a4f519c6d85cc87f09bdc/ed02bfdd871339662179d660bf1deaa2.webp\",\n" +
                "                    \"accountLevel\": \"\"\n" +
                "                },\n" +
                "                \"ticketCountUpdateTime\": \"2025-06-19 14:57:35\",\n" +
                "                \"modifiedTime\": \"2025-07-06 16:51:26\",\n" +
                "                \"createTime\": \"2025-06-19 13:30:42\"\n" +
                "            },\n" +
                "            {\n" +
                "                \"uniqueProductId\": \"3905ee4f86a847b8870961c81baa050f\",\n" +
                "                \"uniqueContentId\": \"f1dc8dd8a48541558fd4f584316f2859\",\n" +
                "                \"uniqueImageId\": \"d2aaf052c9c14ecb82cc908daee96896.jjewelry-aigc-api.ps76tVsd7s_2\",\n" +
                "                \"uniqueWantItId\": \"b9f1ac0ff51a4cbfb918bd87840b25c8\",\n" +
                "                \"detail\": \"{\\\"exposeFields\\\":{\\\"productStage\\\":1,\\\"productImages\\\":[\\\"https://static.zaohaowu.com/jjewelry/web/resources/product/20250619/c2fbccd02627454b8db6cedfca9bc4b9/resource/917ba17f1bcf4a55904e6a61c7fb5280/f0ecbc6b7abd95dc6885bfe31c157c95/webp/f0ecbc6b7abd95dc6885bfe31c157c95.webp\\\",\\\"https://static.zaohaowu.com/jjewelry/web/resources/product/20250619/73c643bb3158457b9fba7097b140a427/resource/85185fc188a94850b980adcded9378e0/13af42a7517f3935a6d344404f775ac3/webp/13af42a7517f3935a6d344404f775ac3.webp\\\"],\\\"price\\\":29.9,\\\"title\\\":\\\"苹果电视机戒指\\\"}}\",\n" +
                "                \"status\": 1,\n" +
                "                \"productionStatus\": 2,\n" +
                "                \"uniqueUser\": {\n" +
                "                    \"id\": \"c7b1a4b9f0bb4a8f9d1db70469d8f468\",\n" +
                "                    \"nickname\": \"苹果糖\",\n" +
                "                    \"avatar\": \"https://static.zaohaowu.com/jjewelry/web/images/avatar/92fea6edb5b94e37aa888d59046c711c/571eee083b3599ec914ce37f846da3e5.webp\",\n" +
                "                    \"accountLevel\": \"\"\n" +
                "                },\n" +
                "                \"ticketCountUpdateTime\": \"2025-06-27 16:15:13\",\n" +
                "                \"modifiedTime\": \"2025-07-06 16:51:28\",\n" +
                "                \"createTime\": \"2025-06-19 13:20:53\"\n" +
                "            },\n" +
                "            {\n" +
                "                \"uniqueProductId\": \"660564bdeb0e406abe6b14f0aeb55af3\",\n" +
                "                \"uniqueContentId\": \"cdf9c308719f4d38a1424a209da63388\",\n" +
                "                \"uniqueImageId\": \"903013272c794f8da33a41f44f724c11.jjewelry-aigc-api.nOy1oVANYY_2\",\n" +
                "                \"uniqueWantItId\": \"747354f88d294201bae7648a10a422e8\",\n" +
                "                \"detail\": \"{\\\"exposeFields\\\":{\\\"productStage\\\":1,\\\"productImages\\\":[\\\"https://static.zaohaowu.com/jjewelry/web/resources/product/20250619/8ac6c185009843b599d2706d356b0f45/resource/d202f0fd92f640f286389359ed2caa40/cb7215d62f3d08ad94b025adf40a7ff6/webp/cb7215d62f3d08ad94b025adf40a7ff6.webp\\\",\\\"https://static.zaohaowu.com/jjewelry/web/resources/product/20250619/cb58625bc6a445949e56dcd4ab023c06/resource/e03fdf2728f44d55a816c965f2337672/2eff84306b40c7dc1478f09001531dee/webp/2eff84306b40c7dc1478f09001531dee.webp\\\"],\\\"price\\\":29.9,\\\"title\\\":\\\"爱心刻字戒指\\\"}}\",\n" +
                "                \"status\": 1,\n" +
                "                \"productionStatus\": 2,\n" +
                "                \"uniqueUser\": {\n" +
                "                    \"id\": \"1f0304b08f664343a7baca99206b0fcd\",\n" +
                "                    \"nickname\": \"Fishball RQ\",\n" +
                "                    \"avatar\": \"https://static.zaohaowu.com/jjewelry/web/images/avatar/5e769bdbb9224f859c54a43fca2eaf46/2a36c55236c4f6846295b4df2597c5ab.webp\",\n" +
                "                    \"accountLevel\": \"officialQualityCreator\"\n" +
                "                },\n" +
                "                \"ticketCountUpdateTime\": \"2025-06-19 04:22:11\",\n" +
                "                \"modifiedTime\": \"2025-07-06 16:50:03\",\n" +
                "                \"createTime\": \"2025-06-19 02:56:22\"\n" +
                "            },\n" +
                "            {\n" +
                "                \"uniqueProductId\": \"39e864d688c246f68a157d77b7ac4193\",\n" +
                "                \"uniqueContentId\": \"8a0a0a8a28c042fbaa49a06881fedfb8\",\n" +
                "                \"uniqueImageId\": \"8be88a41757d48158dab5c2546eb0908.jjewelry-aigc-api.ndzOooSlqB_0\",\n" +
                "                \"uniqueWantItId\": \"f22f6c6a958e4494858516de74da2fa0\",\n" +
                "                \"detail\": \"{\\\"exposeFields\\\":{\\\"productStage\\\":1,\\\"productImages\\\":[\\\"https://static.zaohaowu.com/jjewelry/web/resources/product/20250617/5517e5378c3442b1bf5e832ceb65f536/resource/eca5104387b140758927f9f42d0bd61b/4d9434d85aa9e87a42eb78b3dcd3bf18/webp/4d9434d85aa9e87a42eb78b3dcd3bf18.webp\\\",\\\"https://static.zaohaowu.com/jjewelry/web/resources/product/20250617/aceb1b497ff54a3c9af9fa0220da7ac6/resource/4d208fac8d32408cbfa71f73c73e72c9/a196ce9c2dd08dd3ae08337ce40defe0/webp/a196ce9c2dd08dd3ae08337ce40defe0.webp\\\"],\\\"price\\\":29.9,\\\"title\\\":\\\"给心充电戒指\\\"}}\",\n" +
                "                \"status\": 1,\n" +
                "                \"productionStatus\": 2,\n" +
                "                \"uniqueUser\": {\n" +
                "                    \"id\": \"1f0304b08f664343a7baca99206b0fcd\",\n" +
                "                    \"nickname\": \"Fishball RQ\",\n" +
                "                    \"avatar\": \"https://static.zaohaowu.com/jjewelry/web/images/avatar/5e769bdbb9224f859c54a43fca2eaf46/2a36c55236c4f6846295b4df2597c5ab.webp\",\n" +
                "                    \"accountLevel\": \"officialQualityCreator\"\n" +
                "                },\n" +
                "                \"ticketCountUpdateTime\": \"2025-06-17 11:42:23\",\n" +
                "                \"modifiedTime\": \"2025-07-06 16:49:36\",\n" +
                "                \"createTime\": \"2025-06-17 10:55:43\"\n" +
                "            },\n" +
                "            {\n" +
                "                \"uniqueProductId\": \"2f430611a7b44041b8362cac12df8b01\",\n" +
                "                \"uniqueContentId\": \"8acf63a018094c13bd578a56420a0b3f\",\n" +
                "                \"uniqueImageId\": \"fecfb626c70244829de9fc3c790eca40.jjewelry-aigc-api.4LnAgK0P2j_0\",\n" +
                "                \"uniqueWantItId\": \"c2450cf52e7140d189f8f1deaf2dd948\",\n" +
                "                \"detail\": \"{\\\"exposeFields\\\":{\\\"productStage\\\":1,\\\"productImages\\\":[\\\"https://static.zaohaowu.com/jjewelry/web/resources/product/20250617/a5da74f3f9db4197b2ae5ac612647f2b/resource/1bc1a0ce987549a788e3f0dc2b7aeb30/a446bc2f38277a853e7c988b3a190f53/webp/a446bc2f38277a853e7c988b3a190f53.webp\\\",\\\"https://static.zaohaowu.com/jjewelry/web/resources/product/20250617/ddbaf023d30d4f6ea61f5b3b3f08588c/resource/294389f4c73047c9ab03458642eccb60/9fe18e39a8cf3e94badd8a5f5f6374a9/webp/9fe18e39a8cf3e94badd8a5f5f6374a9.webp\\\"],\\\"price\\\":29.9,\\\"title\\\":\\\"朋克爱心戒指\\\"}}\",\n" +
                "                \"status\": 1,\n" +
                "                \"productionStatus\": 2,\n" +
                "                \"uniqueUser\": {\n" +
                "                    \"id\": \"1f0304b08f664343a7baca99206b0fcd\",\n" +
                "                    \"nickname\": \"Fishball RQ\",\n" +
                "                    \"avatar\": \"https://static.zaohaowu.com/jjewelry/web/images/avatar/5e769bdbb9224f859c54a43fca2eaf46/2a36c55236c4f6846295b4df2597c5ab.webp\",\n" +
                "                    \"accountLevel\": \"officialQualityCreator\"\n" +
                "                },\n" +
                "                \"ticketCountUpdateTime\": \"2025-06-17 11:40:19\",\n" +
                "                \"modifiedTime\": \"2025-07-06 16:49:35\",\n" +
                "                \"createTime\": \"2025-06-17 11:00:30\"\n" +
                "            },\n" +
                "            {\n" +
                "                \"uniqueProductId\": \"d249bc9941514435bdee1fe9db218ec2\",\n" +
                "                \"uniqueContentId\": \"a77e73b5255d44d7954e7ecb5366bc93\",\n" +
                "                \"uniqueImageId\": \"5b721da83cdf423da54e8f59408b389d.jjewelry-aigc-api.Ufeu2VY0pf_1\",\n" +
                "                \"uniqueWantItId\": \"a9ca08616e3c4d96b21e507013fdb38f\",\n" +
                "                \"detail\": \"{\\\"exposeFields\\\":{\\\"productStage\\\":1,\\\"productImages\\\":[\\\"https://static.zaohaowu.com/jjewelry/web/resources/product/20250625/f1ed84ec9c834189a90ef39b7ea863de/resource/beda6d02fdc6449fb1eaf2421f991578/56dfba7f0c5286f1eea0f04066e1548d/webp/56dfba7f0c5286f1eea0f04066e1548d.webp\\\",\\\"https://static.zaohaowu.com/jjewelry/web/resources/product/20250625/8dc80852bd7d4f718355ffd7955ff7ba/resource/bf8b60ea103b4d1fb8656b526f2fea48/919dcca363180d22ea31698c60b6288f/webp/919dcca363180d22ea31698c60b6288f.webp\\\"],\\\"price\\\":29.9,\\\"title\\\":\\\"蒜鸟戒指\\\"}}\",\n" +
                "                \"status\": 1,\n" +
                "                \"productionStatus\": 2,\n" +
                "                \"uniqueUser\": {\n" +
                "                    \"id\": \"94bdd1a68504463ca2d88d1f3bb7e130\",\n" +
                "                    \"nickname\": \"造好物拖拉车司机\",\n" +
                "                    \"avatar\": \"https://static.zaohaowu.com/jjewelry/web/images/avatar/c30e4fc122754b20b345b1960b6de656/956bff90120429e92126d74e8b43d61a.webp\",\n" +
                "                    \"accountLevel\": \"officialQualityCreator\"\n" +
                "                },\n" +
                "                \"ticketCountUpdateTime\": \"2025-06-25 03:15:42\",\n" +
                "                \"modifiedTime\": \"2025-07-06 16:49:34\",\n" +
                "                \"createTime\": \"2025-06-25 03:15:33\"\n" +
                "            },\n" +
                "            {\n" +
                "                \"uniqueProductId\": \"6803b0ebfcae4a77b96bf9c535bbf22e\",\n" +
                "                \"uniqueContentId\": \"a1d09e7c30cc4e48b0b9017ae0f8128d\",\n" +
                "                \"uniqueImageId\": \"acb5c15beddd4ddba619285a7bfdb99c.jjewelry-aigc-api.6kFQUoEc5S_3\",\n" +
                "                \"uniqueWantItId\": \"a4b780c3b54d4901bd381c9967e2555d\",\n" +
                "                \"detail\": \"{\\\"exposeFields\\\":{\\\"productStage\\\":1,\\\"productImages\\\":[\\\"https://static.zaohaowu.com/jjewelry/web/resources/product/20250617/53f19a8f748640499a602c109c2d35ef/resource/dc9ff283e6134b8a8d516e9cd91d64e6/650ec9de495a240e68332cc4cee64799/webp/650ec9de495a240e68332cc4cee64799.webp\\\",\\\"https://static.zaohaowu.com/jjewelry/web/resources/product/20250617/93b275bda0d44ff69e3f96d5d13994b1/resource/223dc8ecba004199951d54640d2cb3c9/406d40574de60d95bead515981f62185/webp/406d40574de60d95bead515981f62185.webp\\\"],\\\"price\\\":29.9,\\\"title\\\":\\\"三只猫咪薄荷绿戒指\\\"}}\",\n" +
                "                \"status\": 1,\n" +
                "                \"productionStatus\": 2,\n" +
                "                \"uniqueUser\": {\n" +
                "                    \"id\": \"1f0304b08f664343a7baca99206b0fcd\",\n" +
                "                    \"nickname\": \"Fishball RQ\",\n" +
                "                    \"avatar\": \"https://static.zaohaowu.com/jjewelry/web/images/avatar/5e769bdbb9224f859c54a43fca2eaf46/2a36c55236c4f6846295b4df2597c5ab.webp\",\n" +
                "                    \"accountLevel\": \"officialQualityCreator\"\n" +
                "                },\n" +
                "                \"ticketCountUpdateTime\": \"2025-06-17 11:40:50\",\n" +
                "                \"modifiedTime\": \"2025-07-06 16:49:38\",\n" +
                "                \"createTime\": \"2025-06-17 10:46:06\"\n" +
                "            },\n" +
                "            {\n" +
                "                \"uniqueProductId\": \"c73dd115a2ec4cbc86b33fbb3101c04e\",\n" +
                "                \"uniqueContentId\": \"57c3b3508b9a456fb1590f61be71a097\",\n" +
                "                \"uniqueImageId\": \"7936158bac3a48bba5090cae8870f9ce.jjewelry-aigc-api.Q6WkrxwWhI_3\",\n" +
                "                \"uniqueWantItId\": \"ce60ee57eb6f484498dc3c47548c76cd\",\n" +
                "                \"detail\": \"{\\\"exposeFields\\\":{\\\"productStage\\\":2,\\\"productImages\\\":[\\\"https://static.zaohaowu.com/jjewelry/web/resources/product/20250617/c402025968cf40a5b1e61e4bb91aa780/resource/bc809d70867a445091e290d792536a86/7e13b9716055d47c2cb9b3d986b55753/webp/7e13b9716055d47c2cb9b3d986b55753.webp\\\",\\\"https://static.zaohaowu.com/jjewelry/web/resources/product/20250617/0c1c5a6c11114750abbad0a50f05a494/resource/1b4c1072ea584df3a8b0cf3b8d6ed772/a1ede42d9d4e581362b4792fb6d65bbd/webp/a1ede42d9d4e581362b4792fb6d65bbd.webp\\\"],\\\"price\\\":29.9,\\\"title\\\":\\\"三只猫咪银灰戒指\\\\n\\\"}}\",\n" +
                "                \"status\": 1,\n" +
                "                \"productionStatus\": 2,\n" +
                "                \"uniqueUser\": {\n" +
                "                    \"id\": \"b4858b2a64cd42bb951582fc518bd9b2\",\n" +
                "                    \"nickname\": \"造好物er-134722f7\",\n" +
                "                    \"avatar\": \"https://static.zaohaowu.com/jjewelry/web/images/avatar/user-HMQoCE-2.png\",\n" +
                "                    \"accountLevel\": \"\"\n" +
                "                },\n" +
                "                \"ticketCountUpdateTime\": \"2025-06-17 10:44:42\",\n" +
                "                \"modifiedTime\": \"2025-07-06 16:49:40\",\n" +
                "                \"createTime\": \"2025-06-17 10:44:32\"\n" +
                "            },\n" +
                "            {\n" +
                "                \"uniqueProductId\": \"faefa398bb8d48a88e63fca64b262e6c\",\n" +
                "                \"uniqueContentId\": \"66b6041a565a4053b21d6fd31fa71aaa\",\n" +
                "                \"uniqueImageId\": \"d39110291e1e408c88c1f366d4671a11.jjewelry-aigc-api.WPCLYOxNtn_0\",\n" +
                "                \"uniqueWantItId\": \"485a3e8d81744ce993d9933e2ff21e7a\",\n" +
                "                \"detail\": \"{\\\"exposeFields\\\":{\\\"productStage\\\":1,\\\"productImages\\\":[\\\"https://static.zaohaowu.com/jjewelry/web/resources/product/20250617/705d5195217e4564a9bffe53f6a4cbbb/resource/8ace9dabb69644709e3f90de9af6e783/648f5be81531700217ff3476773bba0c/webp/648f5be81531700217ff3476773bba0c.webp\\\",\\\"https://static.zaohaowu.com/jjewelry/web/resources/product/20250617/6a43442211544b51bd12196c04f08ba1/resource/20d7ec11be5a4528bfa33a0ec45d0790/11bb3e9395d9203a86c1887e3b3e5b39/webp/11bb3e9395d9203a86c1887e3b3e5b39.webp\\\"],\\\"price\\\":29.9,\\\"title\\\":\\\"大眼炸弹戒指\\\"}}\",\n" +
                "                \"status\": 1,\n" +
                "                \"productionStatus\": 2,\n" +
                "                \"uniqueUser\": {\n" +
                "                    \"id\": \"f9fbed60c1f0421fb588268bc748b402\",\n" +
                "                    \"nickname\": \"kkkk\",\n" +
                "                    \"avatar\": \"https://static.zaohaowu.com/jjewelry/web/images/avatar/8edce6b209444427b106a548cd75d13e/206a5b80195640e972fcbd93a34ab975.webp\",\n" +
                "                    \"accountLevel\": \"\"\n" +
                "                },\n" +
                "                \"ticketCountUpdateTime\": \"2025-06-17 11:25:30\",\n" +
                "                \"modifiedTime\": \"2025-07-06 16:49:43\",\n" +
                "                \"createTime\": \"2025-06-17 11:25:22\"\n" +
                "            },\n" +
                "            {\n" +
                "                \"uniqueProductId\": \"c205bd2e1dd440f1b1c43bdb64d9a465\",\n" +
                "                \"uniqueContentId\": \"bdb81ba33c7c4e28b5ffb43c829d05b5\",\n" +
                "                \"uniqueImageId\": \"3af22b281c444e12aa81272b7f0abf7e.jjewelry-aigc-api.GeDqguYI79_3\",\n" +
                "                \"uniqueWantItId\": \"badcb5a713b64424bddaaea5d7f470d4\",\n" +
                "                \"detail\": \"{\\\"exposeFields\\\":{\\\"productStage\\\":1,\\\"productImages\\\":[\\\"https://static.zaohaowu.com/jjewelry/web/resources/product/20250619/df2b61d90cc44d628e464f174d36d50c/resource/f7803a6a657c4e6c8b4a3967acba3bd0/7e9ea7eb740712104d6f04bb88c68725/webp/7e9ea7eb740712104d6f04bb88c68725.webp\\\",\\\"https://static.zaohaowu.com/jjewelry/web/resources/product/20250619/da8fd1854ee348aab5ad0593273d7b19/resource/1fb8d1f41715440eaf9dde694a3fb42b/8e8d123d56bb47973765075f828908b5/webp/8e8d123d56bb47973765075f828908b5.webp\\\"],\\\"price\\\":29.9,\\\"title\\\":\\\"克苏鲁系情侣戒の男款\\\"}}\",\n" +
                "                \"status\": 1,\n" +
                "                \"productionStatus\": 2,\n" +
                "                \"uniqueUser\": {\n" +
                "                    \"id\": \"b8c7f60162114780a3e51cc1ae6d7954\",\n" +
                "                    \"nickname\": \"AI摸鱼姐\",\n" +
                "                    \"avatar\": \"https://static.zaohaowu.com/jjewelry/web/images/avatar/6ad6169be9cb498f87cd5ad235be2b1d/4c63073849c3aefc2521c6765e84536f.webp\",\n" +
                "                    \"accountLevel\": \"officialQualityCreator\"\n" +
                "                },\n" +
                "                \"ticketCountUpdateTime\": \"2025-06-19 03:54:17\",\n" +
                "                \"modifiedTime\": \"2025-07-06 16:49:44\",\n" +
                "                \"createTime\": \"2025-06-19 02:53:25\"\n" +
                "            },\n" +
                "            {\n" +
                "                \"uniqueProductId\": \"31cdb2cf16b94cd19c17c18aa3b86cd6\",\n" +
                "                \"uniqueContentId\": \"c5918b6823034d54b35f0e19dc8cc670\",\n" +
                "                \"uniqueImageId\": \"532f92a53fa441e0ad34d2d0a73cefd7.jjewelry-aigc-api.KziDSiprWR_2\",\n" +
                "                \"uniqueWantItId\": \"1b83a9f4d42146bf98f4194b92d58f01\",\n" +
                "                \"detail\": \"{\\\"exposeFields\\\":{\\\"productStage\\\":1,\\\"productImages\\\":[\\\"https://static.zaohaowu.com/jjewelry/web/resources/product/20250616/fa109a26af424772a4f0b06a33ea5519/resource/1c125c5164b14b4cbfe217b720c61a55/9f4ecb1e9b9ace7fcabb94c33d3df254/webp/9f4ecb1e9b9ace7fcabb94c33d3df254.webp\\\",\\\"https://static.zaohaowu.com/jjewelry/web/resources/product/20250616/8fb5c1b365664d298e87ed160777503c/resource/333f49dd6d124bafa55357708e2de409/6626e8b815faf5bbdec44857377d8661/webp/6626e8b815faf5bbdec44857377d8661.webp\\\"],\\\"price\\\":29.9,\\\"title\\\":\\\"贪吃鼠鼠戒指\\\"}}\",\n" +
                "                \"status\": 1,\n" +
                "                \"productionStatus\": 2,\n" +
                "                \"uniqueUser\": {\n" +
                "                    \"id\": \"b2f91a99f1654c948cd6bde0c8f9b01c\",\n" +
                "                    \"nickname\": \"叉叉啼\",\n" +
                "                    \"avatar\": \"https://static.zaohaowu.com/jjewelry/web/images/avatar/89c58c05897e40c6bb7b548fc2f7bd75/f6132e6e38918d69f58301d70b6394e3.webp\",\n" +
                "                    \"accountLevel\": \"officialQualityCreator\"\n" +
                "                },\n" +
                "                \"ticketCountUpdateTime\": \"2025-06-16 07:32:58\",\n" +
                "                \"modifiedTime\": \"2025-07-06 16:47:56\",\n" +
                "                \"createTime\": \"2025-06-16 07:25:14\"\n" +
                "            },\n" +
                "            {\n" +
                "                \"uniqueProductId\": \"566be50b62f44ca397e543fa4b34b815\",\n" +
                "                \"uniqueContentId\": \"72fd755ae3e34a7a96ba1e0ed92c5d54\",\n" +
                "                \"uniqueImageId\": \"d634be634c6a40b3ac798ba3a9f14b25.jjewelry-aigc-api.Y1tzuojhuE_0\",\n" +
                "                \"uniqueWantItId\": \"9670a34d0a5b429eb7e89294f9beab6f\",\n" +
                "                \"detail\": \"{\\\"exposeFields\\\":{\\\"productStage\\\":1,\\\"productImages\\\":[\\\"https://static.zaohaowu.com/jjewelry/web/resources/product/20250616/41ce5ea85a6543f6bcb1c9d5eec32f7f/resource/386e650d10074e57bcfde0ced51fb824/5b2ec9c09c8bdbe660168fa5672a49ab/webp/5b2ec9c09c8bdbe660168fa5672a49ab.webp\\\",\\\"https://static.zaohaowu.com/jjewelry/web/resources/product/20250616/94f42694dff54527ad6598a0a7d14c33/resource/78d3e2564ea843cda44093d51b8f742c/093cce496d46814888b000e9b2e93839/webp/093cce496d46814888b000e9b2e93839.webp\\\"],\\\"price\\\":29.9,\\\"title\\\":\\\"雪山小企鹅戒指\\\"}}\",\n" +
                "                \"status\": 1,\n" +
                "                \"productionStatus\": 2,\n" +
                "                \"uniqueUser\": {\n" +
                "                    \"id\": \"1f0304b08f664343a7baca99206b0fcd\",\n" +
                "                    \"nickname\": \"Fishball RQ\",\n" +
                "                    \"avatar\": \"https://static.zaohaowu.com/jjewelry/web/images/avatar/5e769bdbb9224f859c54a43fca2eaf46/2a36c55236c4f6846295b4df2597c5ab.webp\",\n" +
                "                    \"accountLevel\": \"officialQualityCreator\"\n" +
                "                },\n" +
                "                \"ticketCountUpdateTime\": \"2025-06-16 07:37:45\",\n" +
                "                \"modifiedTime\": \"2025-07-06 16:47:18\",\n" +
                "                \"createTime\": \"2025-06-16 07:28:36\"\n" +
                "            },\n" +
                "            {\n" +
                "                \"uniqueProductId\": \"b1c7526fc1994c3ab605843fef8ea797\",\n" +
                "                \"uniqueContentId\": \"ad39d89da6fe444d8cc17798bbfc128c\",\n" +
                "                \"uniqueImageId\": \"7a39ea3217af4baaa32e2979e655c859.jjewelry-aigc-api.Cuy56HzjCX_1\",\n" +
                "                \"uniqueWantItId\": \"a479fa6a31b04437b546f17c285934bc\",\n" +
                "                \"detail\": \"{\\\"exposeFields\\\":{\\\"productStage\\\":1,\\\"productImages\\\":[\\\"https://static.zaohaowu.com/jjewelry/web/resources/product/20250616/b19383b1c1d3441786124cb4b746810b/resource/ff7e38313d36401c9f4eb92bac63be07/756bd1d8fb6e698a81895569c720f4f7/webp/756bd1d8fb6e698a81895569c720f4f7.webp\\\",\\\"https://static.zaohaowu.com/jjewelry/web/resources/product/20250616/0891694c97ca401e929806e23cd554e2/resource/5cc445d054094d8280db0cff52a93d9c/46b62b0262ce69dd57b9cbabb0a3ffa4/webp/46b62b0262ce69dd57b9cbabb0a3ffa4.webp\\\"],\\\"price\\\":29.9,\\\"title\\\":\\\"小黄鸭戒指\\\"}}\",\n" +
                "                \"status\": 1,\n" +
                "                \"productionStatus\": 2,\n" +
                "                \"uniqueUser\": {\n" +
                "                    \"id\": \"f601e7ba8b824edea6a8fdf7167f5b33\",\n" +
                "                    \"nickname\": \"啾啾舅舅救救\",\n" +
                "                    \"avatar\": \"https://static.zaohaowu.com/jjewelry/web/images/avatar/e2647c8e216f484ebd9ae5c787aba603/54d68243da7d0fe375d4e062730cc053.webp\",\n" +
                "                    \"accountLevel\": \"\"\n" +
                "                },\n" +
                "                \"ticketCountUpdateTime\": \"2025-06-16 07:14:02\",\n" +
                "                \"modifiedTime\": \"2025-07-06 16:47:21\",\n" +
                "                \"createTime\": \"2025-06-16 06:14:07\"\n" +
                "            },\n" +
                "            {\n" +
                "                \"uniqueProductId\": \"5ba3b40bd1d14f009bfac2e4301da519\",\n" +
                "                \"uniqueContentId\": \"de64917d9db642679f2db4409bb3fd90\",\n" +
                "                \"uniqueImageId\": \"1733a443c5fa4290940f8472cd3408c4.jjewelry-aigc-api.4wNIM3fHat_0\",\n" +
                "                \"uniqueWantItId\": \"00d543a886c34df8812c3a874261bb42\",\n" +
                "                \"detail\": \"{\\\"exposeFields\\\":{\\\"productStage\\\":1,\\\"productImages\\\":[\\\"https://static.zaohaowu.com/jjewelry/web/resources/product/20250616/f2969bb1c068434fa3cf88c24576d8f0/resource/8a3f0faea58d4af4a21dee90f20619f4/dbeb3541925dd11e3628dfde38aea431/webp/dbeb3541925dd11e3628dfde38aea431.webp\\\",\\\"https://static.zaohaowu.com/jjewelry/web/resources/product/20250616/a89282b07f7742a28f7838545fcd4533/resource/36e49636bf5b4867baf72fa1f0d4c609/ddeacfd25809aa7e916ecbd471e7b718/webp/ddeacfd25809aa7e916ecbd471e7b718.webp\\\"],\\\"price\\\":29.9,\\\"title\\\":\\\"子宫戒指\\\"}}\",\n" +
                "                \"status\": 1,\n" +
                "                \"productionStatus\": 2,\n" +
                "                \"uniqueUser\": {\n" +
                "                    \"id\": \"51061baaa2084547ada33b1cfb637e57\",\n" +
                "                    \"nickname\": \"豹豹宝宝饱饱\",\n" +
                "                    \"avatar\": \"https://static.zaohaowu.com/jjewelry/web/images/avatar/e449004602374f2f83dc8329882aca6e/95cd4c4dfde3f1191f7082d4c591c7db.webp\",\n" +
                "                    \"accountLevel\": \"\"\n" +
                "                },\n" +
                "                \"ticketCountUpdateTime\": \"2025-06-30 08:14:19\",\n" +
                "                \"modifiedTime\": \"2025-07-06 16:47:23\",\n" +
                "                \"createTime\": \"2025-06-16 06:00:37\"\n" +
                "            },\n" +
                "            {\n" +
                "                \"uniqueProductId\": \"04859417057d493ab75a6f72c1028320\",\n" +
                "                \"uniqueContentId\": \"0e8d20bc5269443aa9902d2dbde6d072\",\n" +
                "                \"uniqueImageId\": \"677ed33dfbde469badb87cbe5d8b8397.jjewelry-aigc-api.Wo1b5Ce2pH_3\",\n" +
                "                \"uniqueWantItId\": \"3638e9bd60e0486ea655bc0e75ebcf06\",\n" +
                "                \"detail\": \"{\\\"exposeFields\\\":{\\\"productStage\\\":1,\\\"productImages\\\":[\\\"https://static.zaohaowu.com/jjewelry/web/resources/product/20250616/689a9f8c90aa4a5d956e46e5295604ea/resource/a229c14576464c9880bbc8df2b8bddcd/8126c2cb719b512c74bb51c6c0d979c5/webp/8126c2cb719b512c74bb51c6c0d979c5.webp\\\",\\\"https://static.zaohaowu.com/jjewelry/web/resources/product/20250616/45c5f20681194115bcb52dee955491e5/resource/4a5f1a8cc6f54cf1afbde02f2fedc2dc/afaeb420b81ce04c45b4cc68b872bf90/webp/afaeb420b81ce04c45b4cc68b872bf90.webp\\\"],\\\"price\\\":29.9,\\\"title\\\":\\\"森林小鹿戒指\\\"}}\",\n" +
                "                \"status\": 1,\n" +
                "                \"productionStatus\": 2,\n" +
                "                \"uniqueUser\": {\n" +
                "                    \"id\": \"1f0304b08f664343a7baca99206b0fcd\",\n" +
                "                    \"nickname\": \"Fishball RQ\",\n" +
                "                    \"avatar\": \"https://static.zaohaowu.com/jjewelry/web/images/avatar/5e769bdbb9224f859c54a43fca2eaf46/2a36c55236c4f6846295b4df2597c5ab.webp\",\n" +
                "                    \"accountLevel\": \"officialQualityCreator\"\n" +
                "                },\n" +
                "                \"ticketCountUpdateTime\": \"2025-06-16 07:38:14\",\n" +
                "                \"modifiedTime\": \"2025-07-06 16:47:24\",\n" +
                "                \"createTime\": \"2025-06-16 06:16:27\"\n" +
                "            },\n" +
                "            {\n" +
                "                \"uniqueProductId\": \"3d63f83f1fcd4f3a976d4cdd2d551003\",\n" +
                "                \"uniqueContentId\": \"b7d3093160c74d488bfc18e59b05cc48\",\n" +
                "                \"uniqueImageId\": \"2163f32be96c4038a40da937e793a519.jjewelry-aigc-api.mfXghywZjs_3\",\n" +
                "                \"uniqueWantItId\": \"fb693d2f2d294306be656ac32246006f\",\n" +
                "                \"detail\": \"{\\\"exposeFields\\\":{\\\"productStage\\\":1,\\\"productImages\\\":[\\\"https://static.zaohaowu.com/jjewelry/web/resources/product/20250619/c06450be8e0f4eb5ae9dad111490406c/resource/98e1545924b14e1f9343b4e94fcdd28a/d5e3a4af4bd1811654cbe16c2d2ddf0f/webp/d5e3a4af4bd1811654cbe16c2d2ddf0f.webp\\\",\\\"https://static.zaohaowu.com/jjewelry/web/resources/product/20250619/913fa28e66d14789b6195788f43414b3/resource/ded3686ecd804653ab062185adce4906/eb73160613f98981f2b33f0e76f33db2/webp/eb73160613f98981f2b33f0e76f33db2.webp\\\"],\\\"price\\\":29.9,\\\"title\\\":\\\"波点翅膀戒指\\\"}}\",\n" +
                "                \"status\": 1,\n" +
                "                \"productionStatus\": 2,\n" +
                "                \"uniqueUser\": {\n" +
                "                    \"id\": \"c7b1a4b9f0bb4a8f9d1db70469d8f468\",\n" +
                "                    \"nickname\": \"苹果糖\",\n" +
                "                    \"avatar\": \"https://static.zaohaowu.com/jjewelry/web/images/avatar/92fea6edb5b94e37aa888d59046c711c/571eee083b3599ec914ce37f846da3e5.webp\",\n" +
                "                    \"accountLevel\": \"\"\n" +
                "                },\n" +
                "                \"ticketCountUpdateTime\": \"2025-06-20 15:42:25\",\n" +
                "                \"modifiedTime\": \"2025-07-06 16:47:26\",\n" +
                "                \"createTime\": \"2025-06-19 02:47:13\"\n" +
                "            },\n" +
                "            {\n" +
                "                \"uniqueProductId\": \"fba8e21a8d5c4791a1b7ef4ca3e8fce1\",\n" +
                "                \"uniqueContentId\": \"f0431049258648f99105adb21183b329\",\n" +
                "                \"uniqueImageId\": \"d96c5fc389dd4ed8b083bf5afa26d4ef.jjewelry-aigc-api.TbB3KzkWN9_2\",\n" +
                "                \"uniqueWantItId\": \"8b0803031d5d4396b9b27382d5a0c822\",\n" +
                "                \"detail\": \"{\\\"exposeFields\\\":{\\\"productStage\\\":1,\\\"productImages\\\":[\\\"https://static.zaohaowu.com/jjewelry/web/resources/product/20250619/50ae54bc86a246489b4b90683df019c9/resource/cb950333b9ab464a81041d5613bd12c1/e98643739eb07fe2b247ab1d3b343530/webp/e98643739eb07fe2b247ab1d3b343530.webp\\\",\\\"https://static.zaohaowu.com/jjewelry/web/resources/product/20250619/9b09016a503845ea865a7cde9d472617/resource/ec4d1f2436dd49349f751b45d9a9711e/1f2af63fbc003cb3c174e0f30c300028/webp/1f2af63fbc003cb3c174e0f30c300028.webp\\\"],\\\"price\\\":29.9,\\\"title\\\":\\\"树藤缠绕眼球戒指\\\"}}\",\n" +
                "                \"status\": 1,\n" +
                "                \"productionStatus\": 2,\n" +
                "                \"uniqueUser\": {\n" +
                "                    \"id\": \"c619c025f44f4d2a81b118d28e20fc45\",\n" +
                "                    \"nickname\": \"杨宇\",\n" +
                "                    \"avatar\": \"https://static.zaohaowu.com/jjewelry/web/images/avatar/8c46032190e848e8a01cf9795e1f5fcf/33a29a99d50b5d3abc1b62cdea93cfea.webp\",\n" +
                "                    \"accountLevel\": \"officialQualityCreator\"\n" +
                "                },\n" +
                "                \"ticketCountUpdateTime\": \"2025-06-19 03:06:21\",\n" +
                "                \"modifiedTime\": \"2025-07-06 16:47:28\",\n" +
                "                \"createTime\": \"2025-06-19 02:50:49\"\n" +
                "            },\n" +
                "            {\n" +
                "                \"uniqueProductId\": \"83425cc2d8924a3ca31f4a152a0ac06e\",\n" +
                "                \"uniqueContentId\": \"01048d1879114eb6a8afb5f843df95fb\",\n" +
                "                \"uniqueImageId\": \"7001ff43a7014bbe863826cb723b0aee.jjewelry-aigc-api.2bWa7JAXSF_2\",\n" +
                "                \"uniqueWantItId\": \"32f1a4102ceb462dbc5d0b3f30161292\",\n" +
                "                \"detail\": \"{\\\"exposeFields\\\":{\\\"productStage\\\":1,\\\"productImages\\\":[\\\"https://static.zaohaowu.com/jjewelry/web/resources/product/20250613/ddd3785f29284c92845465bb346e0399/resource/685aaa8301934f3a8be2e5b7541509a0/a9462e86cf2aec985b1515314fe9bf92/webp/a9462e86cf2aec985b1515314fe9bf92.webp\\\",\\\"https://static.zaohaowu.com/jjewelry/web/resources/product/20250613/df30aeb5d91f49acaeaefbee03b21279/resource/1e806f58bf94423fb3d9470addf95ec7/5314a0362a31a3d4f9452e5c6a20ae86/webp/5314a0362a31a3d4f9452e5c6a20ae86.webp\\\"],\\\"price\\\":29.9,\\\"title\\\":\\\"嘻嘻我是小猫咪戒指\\\"}}\",\n" +
                "                \"status\": 1,\n" +
                "                \"productionStatus\": 2,\n" +
                "                \"uniqueUser\": {\n" +
                "                    \"id\": \"24fd7028f56c4495af883353623ab565\",\n" +
                "                    \"nickname\": \"造好物er-0b2d8885\",\n" +
                "                    \"avatar\": \"https://static.zaohaowu.com/jjewelry/web/images/avatar/user-HMQoCE-2.png\",\n" +
                "                    \"accountLevel\": \"\"\n" +
                "                },\n" +
                "                \"ticketCountUpdateTime\": \"2025-06-13 11:19:55\",\n" +
                "                \"modifiedTime\": \"2025-07-06 16:47:29\",\n" +
                "                \"createTime\": \"2025-06-13 08:25:15\"\n" +
                "            },\n" +
                "            {\n" +
                "                \"uniqueProductId\": \"077eb37a2dca4192840fcbb106881821\",\n" +
                "                \"uniqueContentId\": \"36f2a2cee8474e3ab232a4bd3c260d56\",\n" +
                "                \"uniqueImageId\": \"39e78f6a6a164084a6aeb6c7eb9230e5.jjewelry-aigc-api.IBb2vk7fpG_0\",\n" +
                "                \"uniqueWantItId\": \"b5d78f0b8b644c309fc629f51bcb3677\",\n" +
                "                \"detail\": \"{\\\"exposeFields\\\":{\\\"productStage\\\":1,\\\"productImages\\\":[\\\"https://static.zaohaowu.com/jjewelry/web/resources/product/20250616/18ea4beaddca41d0b02328f8f7810437/resource/53fed98e1e7e468094e1fbaccea63db7/5d39ce2d5ce6b15c4a74d462dcf5c20f/webp/5d39ce2d5ce6b15c4a74d462dcf5c20f.webp\\\",\\\"https://static.zaohaowu.com/jjewelry/web/resources/product/20250616/23d0364ce5bd4072bfec9d003c056eae/resource/1c791ccfbe834cf0b8f52d731ea59ceb/5e924b91f351ac67c70f40087888f23b/webp/5e924b91f351ac67c70f40087888f23b.webp\\\"],\\\"price\\\":29.9,\\\"title\\\":\\\"双手篮球戒指\\\"}}\",\n" +
                "                \"status\": 1,\n" +
                "                \"productionStatus\": 2,\n" +
                "                \"uniqueUser\": {\n" +
                "                    \"id\": \"bab9c97fd1af42989464986b07a7900e\",\n" +
                "                    \"nickname\": \"name\",\n" +
                "                    \"avatar\": \"https://static.zaohaowu.com/jjewelry/web/images/avatar/70bcf3b6a7d941ec908d19b40b00b4d1/0935f65f5ab542aa3886a4112720a365.webp\",\n" +
                "                    \"accountLevel\": \"officialQualityCreator\"\n" +
                "                },\n" +
                "                \"ticketCountUpdateTime\": \"2025-06-16 06:30:21\",\n" +
                "                \"modifiedTime\": \"2025-07-06 16:47:32\",\n" +
                "                \"createTime\": \"2025-06-16 06:18:56\"\n" +
                "            },\n" +
                "            {\n" +
                "                \"uniqueProductId\": \"982c197dcaf04c38a13d94eb70cdbde0\",\n" +
                "                \"uniqueContentId\": \"b557a2eb263f40bea6b2a3951ffcf7a5\",\n" +
                "                \"uniqueImageId\": \"0c8f4547df75435c8aec7e9a9f7560d2.jjewelry-aigc-api.Sv6yHbjVWO_0\",\n" +
                "                \"uniqueWantItId\": \"047af5625b564511b7724a395e7bb21c\",\n" +
                "                \"detail\": \"{\\\"exposeFields\\\":{\\\"productStage\\\":0,\\\"productImages\\\":[\\\"https://static.zaohaowu.com/jjewelry/web/resources/product/20250613/237ce03efc1f4bfdb2a71b2a8c510a8d/resource/217fbe8954b44eb8a37b3905fc3bbd25/0ed4d0ec1f8b6be6f8b175042f4e9fbb/webp/0ed4d0ec1f8b6be6f8b175042f4e9fbb.webp\\\",\\\"https://static.zaohaowu.com/jjewelry/web/resources/product/20250613/1f18ccd358bb443eb5ca09c7ca0e2eb6/resource/284dea346688412fbc6968b19d26961d/488abdd4880e2cbd7e835be85e1c7032/webp/488abdd4880e2cbd7e835be85e1c7032.webp\\\"],\\\"price\\\":29.9,\\\"title\\\":\\\"黄金蛋炒饭戒指\\\"}}\",\n" +
                "                \"status\": 1,\n" +
                "                \"productionStatus\": 2,\n" +
                "                \"uniqueUser\": {\n" +
                "                    \"id\": \"bab9c97fd1af42989464986b07a7900e\",\n" +
                "                    \"nickname\": \"name\",\n" +
                "                    \"avatar\": \"https://static.zaohaowu.com/jjewelry/web/images/avatar/70bcf3b6a7d941ec908d19b40b00b4d1/0935f65f5ab542aa3886a4112720a365.webp\",\n" +
                "                    \"accountLevel\": \"officialQualityCreator\"\n" +
                "                },\n" +
                "                \"ticketCountUpdateTime\": \"2025-06-13 11:06:49\",\n" +
                "                \"modifiedTime\": \"2025-07-06 16:45:04\",\n" +
                "                \"createTime\": \"2025-06-13 11:06:28\"\n" +
                "            },\n" +
                "            {\n" +
                "                \"uniqueProductId\": \"2f8f25ef5aa64adea16c5832b57d4ef2\",\n" +
                "                \"uniqueContentId\": \"95cc94db866448e3a24c058cd778c6dd\",\n" +
                "                \"uniqueImageId\": \"a555db4797c841aabb55974e218ed306.jjewelry-aigc-api.KSWSzV9PqT_1\",\n" +
                "                \"uniqueWantItId\": \"f15d02f54ce04829a2bf235eb0da5a62\",\n" +
                "                \"detail\": \"{\\\"exposeFields\\\":{\\\"productStage\\\":0,\\\"productImages\\\":[\\\"https://static.zaohaowu.com/jjewelry/web/resources/product/20250613/cc7f92fdbc844c27bef370436837c3a6/resource/1866c701a669460d8d0597feb7597336/3ba350bd6defbbcec6a4e5637f467df2/webp/3ba350bd6defbbcec6a4e5637f467df2.webp\\\",\\\"https://static.zaohaowu.com/jjewelry/web/resources/product/20250616/9311bed7bbdb4d8891d92399bda6a81f/resource/cb201532c5074059b5ed74f3b6532d80/e4515b03d833207fd05e5dac8489514d/webp/e4515b03d833207fd05e5dac8489514d.webp\\\"],\\\"price\\\":29.9,\\\"title\\\":\\\"做人最重要是开心戒指\\\"}}\",\n" +
                "                \"status\": 1,\n" +
                "                \"productionStatus\": 2,\n" +
                "                \"uniqueUser\": {\n" +
                "                    \"id\": \"bab9c97fd1af42989464986b07a7900e\",\n" +
                "                    \"nickname\": \"name\",\n" +
                "                    \"avatar\": \"https://static.zaohaowu.com/jjewelry/web/images/avatar/70bcf3b6a7d941ec908d19b40b00b4d1/0935f65f5ab542aa3886a4112720a365.webp\",\n" +
                "                    \"accountLevel\": \"officialQualityCreator\"\n" +
                "                },\n" +
                "                \"ticketCountUpdateTime\": \"2025-06-13 11:04:41\",\n" +
                "                \"modifiedTime\": \"2025-07-06 16:44:40\",\n" +
                "                \"createTime\": \"2025-06-13 08:32:13\"\n" +
                "            },\n" +
                "            {\n" +
                "                \"uniqueProductId\": \"9087a7fbbb30449dba1c62a8a685bdf9\",\n" +
                "                \"uniqueContentId\": \"bd4d25acf27447d896d9ba927435ad3b\",\n" +
                "                \"uniqueImageId\": \"632c4df074ab4481bf4e8bfe3c25ec00.jjewelry-aigc-api.iJqXs5o6lT_3\",\n" +
                "                \"uniqueWantItId\": \"20c444307ffa49fe9750642e3a09273b\",\n" +
                "                \"detail\": \"{\\\"exposeFields\\\":{\\\"productStage\\\":0,\\\"productImages\\\":[\\\"https://static.zaohaowu.com/jjewelry/web/resources/product/20250613/6c022150848a4b12b5d7eeb6fb13f6fc/resource/11df135ae2d54da19be9217fca52aeb9/35f695cead29a0c2a5d10d3082686773/webp/35f695cead29a0c2a5d10d3082686773.webp\\\",\\\"https://static.zaohaowu.com/jjewelry/web/resources/product/20250613/39f83a279d8d46d0991eef91a5d6af5e/resource/696778563935436bb8889ddde23c1d0d/9c505776e2407bfb8d5cba85e6507e78/webp/9c505776e2407bfb8d5cba85e6507e78.webp\\\"],\\\"price\\\":29.9,\\\"title\\\":\\\"阳光彩虹小白马戒指\\\"}}\",\n" +
                "                \"status\": 1,\n" +
                "                \"productionStatus\": 2,\n" +
                "                \"uniqueUser\": {\n" +
                "                    \"id\": \"56f8e06c1c4843f2ae87b2d815b7e2c1\",\n" +
                "                    \"nickname\": \"Milk\",\n" +
                "                    \"avatar\": \"https://static.zaohaowu.com/jjewelry/web/images/avatar/f5368ca77f8e4c7b9e262b02452380f1/ff9abd18cd95847a0e260b1cd8902fc5.webp\",\n" +
                "                    \"accountLevel\": \"\"\n" +
                "                },\n" +
                "                \"ticketCountUpdateTime\": \"2025-06-15 06:07:57\",\n" +
                "                \"modifiedTime\": \"2025-07-06 16:44:26\",\n" +
                "                \"createTime\": \"2025-06-13 08:46:20\"\n" +
                "            },\n" +
                "            {\n" +
                "                \"uniqueProductId\": \"0e00598c1e3747b99d4ce8eeb6fef860\",\n" +
                "                \"uniqueContentId\": \"9973396ec83743d5a7864d224a3ce55c\",\n" +
                "                \"uniqueImageId\": \"9ccd887b7eac439884033af83fa718f2.jjewelry-aigc-api.amPrdVBrMx_1\",\n" +
                "                \"uniqueWantItId\": \"983e9fa1a28b4218a868609510be43aa\",\n" +
                "                \"detail\": \"{\\\"exposeFields\\\":{\\\"productStage\\\":1,\\\"productImages\\\":[\\\"https://static.zaohaowu.com/jjewelry/web/resources/product/20250613/bdf74d4e01d748f4aa41f39628b6635a/resource/0bfa8485620b48bebb59c83ebd93149b/23972f67fdb162a673dc5cf46904d263/webp/23972f67fdb162a673dc5cf46904d263.webp\\\",\\\"https://static.zaohaowu.com/jjewelry/web/resources/product/20250613/2180c026a66f4f9daf6365b46ef11474/resource/cf2dd4a21d174ec4b0659e23439e5bfe/0f3ac569498e7f8b659ae0137b8d3b71/webp/0f3ac569498e7f8b659ae0137b8d3b71.webp\\\"],\\\"price\\\":29.9,\\\"title\\\":\\\"铁锅炖大鹅戒指\\\\n\\\"}}\",\n" +
                "                \"status\": 1,\n" +
                "                \"productionStatus\": 2,\n" +
                "                \"uniqueUser\": {\n" +
                "                    \"id\": \"bab9c97fd1af42989464986b07a7900e\",\n" +
                "                    \"nickname\": \"name\",\n" +
                "                    \"avatar\": \"https://static.zaohaowu.com/jjewelry/web/images/avatar/70bcf3b6a7d941ec908d19b40b00b4d1/0935f65f5ab542aa3886a4112720a365.webp\",\n" +
                "                    \"accountLevel\": \"officialQualityCreator\"\n" +
                "                },\n" +
                "                \"ticketCountUpdateTime\": \"2025-06-13 11:05:35\",\n" +
                "                \"modifiedTime\": \"2025-07-06 16:44:18\",\n" +
                "                \"createTime\": \"2025-06-13 06:31:27\"\n" +
                "            },\n" +
                "            {\n" +
                "                \"uniqueProductId\": \"0fe35936ad3f43f9854c0763258d0afb\",\n" +
                "                \"uniqueContentId\": \"3cca0e01b3134ebfbc2db7adddce3e1e\",\n" +
                "                \"uniqueImageId\": \"22906d4fa2fd4cb3b3cf111996260835.jjewelry-aigc-api.7KJ2ZVJ7a6_1\",\n" +
                "                \"uniqueWantItId\": \"f029a4b793c5482b80ee0cd8fe39bf0b\",\n" +
                "                \"detail\": \"{\\\"exposeFields\\\":{\\\"productStage\\\":2,\\\"productImages\\\":[\\\"https://static.zaohaowu.com/jjewelry/web/resources/product/20250613/09188a8989724cf4aae82401c702566b/resource/974bc6dc84b145089ab23a4a155c9820/900445f1770b76178457561c56fd44b6/webp/900445f1770b76178457561c56fd44b6.webp\\\",\\\"https://static.zaohaowu.com/jjewelry/web/resources/product/20250613/05f476c3ba78436a9363292687409b28/resource/0218a6281e2541f7b794b4363dd2c617/88ea9566eb9fb774dc4bd08760679e1e/webp/88ea9566eb9fb774dc4bd08760679e1e.webp\\\"],\\\"price\\\":29.9,\\\"title\\\":\\\"带来好运的锦鲤戒指\\\"}}\",\n" +
                "                \"status\": 1,\n" +
                "                \"productionStatus\": 2,\n" +
                "                \"uniqueUser\": {\n" +
                "                    \"id\": \"788955e31acb4ade97072e243f49b180\",\n" +
                "                    \"nickname\": \"闭环式躺平\",\n" +
                "                    \"avatar\": \"https://static.zaohaowu.com/jjewelry/web/images/avatar/8255ecb186e54be89cf8f4c4a0de7626/feebd79ec235d4c3729b40442d61aaf7.webp\",\n" +
                "                    \"accountLevel\": \"\"\n" +
                "                },\n" +
                "                \"ticketCountUpdateTime\": \"2025-06-13 06:19:51\",\n" +
                "                \"modifiedTime\": \"2025-07-06 16:44:08\",\n" +
                "                \"createTime\": \"2025-06-13 06:17:33\"\n" +
                "            },\n" +
                "            {\n" +
                "                \"uniqueProductId\": \"4ca4e051ba574d578761412decd0f628\",\n" +
                "                \"uniqueContentId\": \"d63bdd9feb1545d0a72def06c53515e6\",\n" +
                "                \"uniqueImageId\": \"d4ae3195c9a44987a6af1dbf7ab53b96.jjewelry-aigc-api.3p6OHVOVS4_3\",\n" +
                "                \"uniqueWantItId\": \"a629be5ed73f4d149db06a17cbafdbb9\",\n" +
                "                \"detail\": \"{\\\"exposeFields\\\":{\\\"productStage\\\":1,\\\"productImages\\\":[\\\"https://static.zaohaowu.com/jjewelry/web/resources/product/20250613/ade35332b11e49a98656a6d5ce530900/resource/24cdb524407b429985af7d6a060647c5/a1789654c0f6549ae3fcd0c94294bfd4/webp/a1789654c0f6549ae3fcd0c94294bfd4.webp\\\",\\\"https://static.zaohaowu.com/jjewelry/web/resources/product/20250613/3cdc3d6ba35d46209540d99dbf83af3b/resource/f4723d8262e54d6183accd0ba5457bfb/bd3e9bbffa16e09d3e50024d17b8e992/webp/bd3e9bbffa16e09d3e50024d17b8e992.webp\\\"],\\\"price\\\":29.9,\\\"title\\\":\\\"双重点赞戒指\\\"}}\",\n" +
                "                \"status\": 1,\n" +
                "                \"productionStatus\": 2,\n" +
                "                \"uniqueUser\": {\n" +
                "                    \"id\": \"b8c7f60162114780a3e51cc1ae6d7954\",\n" +
                "                    \"nickname\": \"AI摸鱼姐\",\n" +
                "                    \"avatar\": \"https://static.zaohaowu.com/jjewelry/web/images/avatar/6ad6169be9cb498f87cd5ad235be2b1d/4c63073849c3aefc2521c6765e84536f.webp\",\n" +
                "                    \"accountLevel\": \"officialQualityCreator\"\n" +
                "                },\n" +
                "                \"ticketCountUpdateTime\": \"2025-06-13 12:38:34\",\n" +
                "                \"modifiedTime\": \"2025-07-06 16:43:32\",\n" +
                "                \"createTime\": \"2025-06-13 06:19:34\"\n" +
                "            },\n" +
                "            {\n" +
                "                \"uniqueProductId\": \"79c16f5171cd46618e942a1d53c9176c\",\n" +
                "                \"uniqueContentId\": \"9bca1ebb4a8f437e8fffa863ba508ff8\",\n" +
                "                \"uniqueImageId\": \"355aa14579a4419088204687532dbec4.jjewelry-aigc-api.QDSKs4LMr3_2\",\n" +
                "                \"uniqueWantItId\": \"1490920f69514ea885c65403662bf81f\",\n" +
                "                \"detail\": \"{\\\"exposeFields\\\":{\\\"productStage\\\":1,\\\"productImages\\\":[\\\"https://static.zaohaowu.com/jjewelry/web/resources/product/20250613/4f87244952794509ab15a20592975894/resource/e2f43808804b44abbc3a59188ac4c02c/bd55e8ebe3d11821cf08d6793ffd0e92/webp/bd55e8ebe3d11821cf08d6793ffd0e92.webp\\\",\\\"https://static.zaohaowu.com/jjewelry/web/resources/product/20250613/5b3959fcf0f84f7d8c85681e523ebcb2/resource/cbc728ee0b7548e49755830787e85b53/f183bca9ca55873ecd680f0e64c8b356/webp/f183bca9ca55873ecd680f0e64c8b356.webp\\\"],\\\"price\\\":29.9,\\\"title\\\":\\\"丑萌憨憨云朵戒指\\\"}}\",\n" +
                "                \"status\": 1,\n" +
                "                \"productionStatus\": 2,\n" +
                "                \"uniqueUser\": {\n" +
                "                    \"id\": \"ee8f83c674d24451b37ebc708afe64ad\",\n" +
                "                    \"nickname\": \"Caroline\",\n" +
                "                    \"avatar\": \"https://static.zaohaowu.com/jjewelry/web/images/avatar/cedb12849c08436da3f79619cbd7e133/9f75931b25e14c0a7fcac2883accaefe.webp\",\n" +
                "                    \"accountLevel\": \"officialQualityCreator\"\n" +
                "                },\n" +
                "                \"ticketCountUpdateTime\": \"2025-06-13 06:48:13\",\n" +
                "                \"modifiedTime\": \"2025-07-06 16:43:34\",\n" +
                "                \"createTime\": \"2025-06-13 06:24:01\"\n" +
                "            },\n" +
                "            {\n" +
                "                \"uniqueProductId\": \"9fd4ae92cb084b75ad2f02a5c51a2634\",\n" +
                "                \"uniqueContentId\": \"c83c979dc73f48139a280f2b0066d74c\",\n" +
                "                \"uniqueImageId\": \"1567c41a38b04b909ebfb3950b03811b.jjewelry-aigc-api.NQGiI72QYQ_0\",\n" +
                "                \"uniqueWantItId\": \"a9dc70c99c1548108b5aa42cb28fc2c3\",\n" +
                "                \"detail\": \"{\\\"exposeFields\\\":{\\\"productStage\\\":1,\\\"productImages\\\":[\\\"https://static.zaohaowu.com/jjewelry/web/resources/product/20250616/968e2c45d10a4c3fb4bacdd9a379940a/resource/00d99c8e104a4f7f81d81bb0b66e5b2f/00c5745ce25a7c828a44a1d85cfbdd66/webp/00c5745ce25a7c828a44a1d85cfbdd66.webp\\\",\\\"https://static.zaohaowu.com/jjewelry/web/resources/product/20250616/05f933b0808446fda37522df065927ee/resource/706aceaaa0ac4d2e95c61964b20aea0c/31d80747ea29abad4f0b3a6a4fc3f962/webp/31d80747ea29abad4f0b3a6a4fc3f962.webp\\\"],\\\"price\\\":29.9,\\\"title\\\":\\\"滑板蝴蝶戒指\\\\n\\\"}}\",\n" +
                "                \"status\": 1,\n" +
                "                \"productionStatus\": 2,\n" +
                "                \"uniqueUser\": {\n" +
                "                    \"id\": \"f601e7ba8b824edea6a8fdf7167f5b33\",\n" +
                "                    \"nickname\": \"啾啾舅舅救救\",\n" +
                "                    \"avatar\": \"https://static.zaohaowu.com/jjewelry/web/images/avatar/e2647c8e216f484ebd9ae5c787aba603/54d68243da7d0fe375d4e062730cc053.webp\",\n" +
                "                    \"accountLevel\": \"\"\n" +
                "                },\n" +
                "                \"ticketCountUpdateTime\": \"2025-06-14 03:41:53\",\n" +
                "                \"modifiedTime\": \"2025-07-06 16:43:36\",\n" +
                "                \"createTime\": \"2025-06-13 04:07:48\"\n" +
                "            },\n" +
                "            {\n" +
                "                \"uniqueProductId\": \"807b74cfb4474223a645e6cb73bacd16\",\n" +
                "                \"uniqueContentId\": \"07e0a1da171b44269898ee2f9bfa998f\",\n" +
                "                \"uniqueImageId\": \"e10d7c0685c941d2a29b160fc2879ef6.jjewelry-aigc-api.lqPYO3lTpj_2\",\n" +
                "                \"uniqueWantItId\": \"a4191360ae9248c98f2141992b6bf45c\",\n" +
                "                \"detail\": \"{\\\"exposeFields\\\":{\\\"productStage\\\":2,\\\"productImages\\\":[\\\"https://static.zaohaowu.com/jjewelry/web/resources/product/20250613/345260881a744ca1bd3e6b939c8bd036/resource/aaceea0c34f34ea9b0fff91a7147c913/00574ae44208458157449b34e446bd9b/webp/00574ae44208458157449b34e446bd9b.webp\\\",\\\"https://static.zaohaowu.com/jjewelry/web/resources/product/20250613/0957bcf8acdd4347a523d174807b7ea6/resource/9b6bb72aaf8e4216a238aa0da4ed6bf9/b655cef898bea0dcecc689b22272326c/webp/b655cef898bea0dcecc689b22272326c.webp\\\"],\\\"price\\\":29.9,\\\"title\\\":\\\"浪花海龟戒指\\\\n\\\"}}\",\n" +
                "                \"status\": 1,\n" +
                "                \"productionStatus\": 2,\n" +
                "                \"uniqueUser\": {\n" +
                "                    \"id\": \"f601e7ba8b824edea6a8fdf7167f5b33\",\n" +
                "                    \"nickname\": \"啾啾舅舅救救\",\n" +
                "                    \"avatar\": \"https://static.zaohaowu.com/jjewelry/web/images/avatar/e2647c8e216f484ebd9ae5c787aba603/54d68243da7d0fe375d4e062730cc053.webp\",\n" +
                "                    \"accountLevel\": \"\"\n" +
                "                },\n" +
                "                \"ticketCountUpdateTime\": \"2025-06-14 03:42:13\",\n" +
                "                \"modifiedTime\": \"2025-07-06 16:43:38\",\n" +
                "                \"createTime\": \"2025-06-13 04:08:52\"\n" +
                "            },\n" +
                "            {\n" +
                "                \"uniqueProductId\": \"0895539784624f86b007b7fc4c67c7b1\",\n" +
                "                \"uniqueContentId\": \"abea35d555ba4bfb8a0e2084ff515ff6\",\n" +
                "                \"uniqueImageId\": \"0e6de2f76e8d4b20b5bfbed0bd7f7d4b.jjewelry-aigc-api.voqgzY62Rx_0\",\n" +
                "                \"uniqueWantItId\": \"47ba862029f44186a435d8c4500d3a9b\",\n" +
                "                \"detail\": \"{\\\"exposeFields\\\":{\\\"productStage\\\":1,\\\"productImages\\\":[\\\"https://static.zaohaowu.com/jjewelry/web/resources/product/20250613/c45d63bf66854e4091aa651ee552a4f7/resource/ed5c693e5aa24eb588c10bec910074bb/a58f9b0fed033be15d752b38a18aeb9e/webp/a58f9b0fed033be15d752b38a18aeb9e.webp\\\",\\\"https://static.zaohaowu.com/jjewelry/web/resources/product/20250613/eb1a07e78019492d8d0584f57dd99c2d/resource/99dcd64de5ec405c87b2c462ed42344c/a576b8c17a77c06461fedbf961c60401/webp/a576b8c17a77c06461fedbf961c60401.webp\\\"],\\\"price\\\":29.9,\\\"title\\\":\\\"克苏鲁触手红色眼球戒指\\\\n\\\"}}\",\n" +
                "                \"status\": 1,\n" +
                "                \"productionStatus\": 2,\n" +
                "                \"uniqueUser\": {\n" +
                "                    \"id\": \"c619c025f44f4d2a81b118d28e20fc45\",\n" +
                "                    \"nickname\": \"杨宇\",\n" +
                "                    \"avatar\": \"https://static.zaohaowu.com/jjewelry/web/images/avatar/8c46032190e848e8a01cf9795e1f5fcf/33a29a99d50b5d3abc1b62cdea93cfea.webp\",\n" +
                "                    \"accountLevel\": \"officialQualityCreator\"\n" +
                "                },\n" +
                "                \"ticketCountUpdateTime\": \"2025-06-13 09:58:29\",\n" +
                "                \"modifiedTime\": \"2025-07-06 16:43:39\",\n" +
                "                \"createTime\": \"2025-06-13 03:39:05\"\n" +
                "            },\n" +
                "            {\n" +
                "                \"uniqueProductId\": \"25f07c71eddd47efb1be9c2c6b697736\",\n" +
                "                \"uniqueContentId\": \"57ec817badef4a43a862d92802bc972d\",\n" +
                "                \"uniqueImageId\": \"bc56c0dfcdfc4c6aa1aa2711461de5db.jjewelry-aigc-api.WaL3hKgOJL_2\",\n" +
                "                \"uniqueWantItId\": \"8287ab33c9884a1e84ecb2305cab7f72\",\n" +
                "                \"detail\": \"{\\\"exposeFields\\\":{\\\"productStage\\\":1,\\\"productImages\\\":[\\\"https://static.zaohaowu.com/jjewelry/web/resources/product/20250616/622cb16011e84a15880924dd6341d315/resource/f8d8ab981b2141c7a31c234d0b6fd523/646f3c5c82cfb2bac05789f2d01b7722/webp/646f3c5c82cfb2bac05789f2d01b7722.webp\\\",\\\"https://static.zaohaowu.com/jjewelry/web/resources/product/20250616/1745dbfac3ba4418925f7574fbcbbf1b/resource/ee2d1c0b0b9e472899bfb5223c841bfc/6fcf0b9c9f3b635a18118ba17f92a250/webp/6fcf0b9c9f3b635a18118ba17f92a250.webp\\\"],\\\"price\\\":29.9,\\\"title\\\":\\\"克苏鲁系情侣戒の女款\\\\n\\\"}}\",\n" +
                "                \"status\": 1,\n" +
                "                \"productionStatus\": 2,\n" +
                "                \"uniqueUser\": {\n" +
                "                    \"id\": \"b8c7f60162114780a3e51cc1ae6d7954\",\n" +
                "                    \"nickname\": \"AI摸鱼姐\",\n" +
                "                    \"avatar\": \"https://static.zaohaowu.com/jjewelry/web/images/avatar/6ad6169be9cb498f87cd5ad235be2b1d/4c63073849c3aefc2521c6765e84536f.webp\",\n" +
                "                    \"accountLevel\": \"officialQualityCreator\"\n" +
                "                },\n" +
                "                \"ticketCountUpdateTime\": \"2025-06-13 05:37:58\",\n" +
                "                \"modifiedTime\": \"2025-07-06 16:43:41\",\n" +
                "                \"createTime\": \"2025-06-13 04:00:46\"\n" +
                "            },\n" +
                "            {\n" +
                "                \"uniqueProductId\": \"9260b0e74ff04ec58ec25ef1807df262\",\n" +
                "                \"uniqueContentId\": \"bcc57bc07bed449f8c2bf4abd9012f06\",\n" +
                "                \"uniqueImageId\": \"0e6de2f76e8d4b20b5bfbed0bd7f7d4b.jjewelry-aigc-api.voqgzY62Rx_3\",\n" +
                "                \"uniqueWantItId\": \"153cde9ad24748ed94c61d8573798a12\",\n" +
                "                \"detail\": \"{\\\"exposeFields\\\":{\\\"productStage\\\":1,\\\"productImages\\\":[\\\"https://static.zaohaowu.com/jjewelry/web/resources/product/20250616/44167c067c374371a128de8ba94fcc28/resource/8e3d36ce319349d093b0695de86f7fb3/8afbc5563562c65043e3c2b4b74c1d87/webp/8afbc5563562c65043e3c2b4b74c1d87.webp\\\",\\\"https://static.zaohaowu.com/jjewelry/web/resources/product/20250616/0dbbfd5265e64911b208ec2084b17382/resource/1a986e2331ea47849749afd21141aac0/8ddad8abcfaf3d355a266489732a6434/webp/8ddad8abcfaf3d355a266489732a6434.webp\\\"],\\\"price\\\":29.9,\\\"title\\\":\\\"克苏鲁触手绿色眼球戒指\\\\n\\\"}}\",\n" +
                "                \"status\": 1,\n" +
                "                \"productionStatus\": 2,\n" +
                "                \"uniqueUser\": {\n" +
                "                    \"id\": \"c619c025f44f4d2a81b118d28e20fc45\",\n" +
                "                    \"nickname\": \"杨宇\",\n" +
                "                    \"avatar\": \"https://static.zaohaowu.com/jjewelry/web/images/avatar/8c46032190e848e8a01cf9795e1f5fcf/33a29a99d50b5d3abc1b62cdea93cfea.webp\",\n" +
                "                    \"accountLevel\": \"officialQualityCreator\"\n" +
                "                },\n" +
                "                \"ticketCountUpdateTime\": \"2025-06-13 09:57:57\",\n" +
                "                \"modifiedTime\": \"2025-07-06 16:43:42\",\n" +
                "                \"createTime\": \"2025-06-13 03:37:27\"\n" +
                "            },\n" +
                "            {\n" +
                "                \"uniqueProductId\": \"4bec2205dd374a5f9d20bfdab2879413\",\n" +
                "                \"uniqueContentId\": \"1e71a2dd786546eab9fffaf871a22ba9\",\n" +
                "                \"uniqueImageId\": \"9f7304435c95427690a8515349d8b784.jjewelry-aigc-api.DLVu8DPX8W_0\",\n" +
                "                \"uniqueWantItId\": \"51cb4d0c95364fbd9f460a733d4f8eeb\",\n" +
                "                \"detail\": \"{\\\"exposeFields\\\":{\\\"productStage\\\":1,\\\"productImages\\\":[\\\"https://static.zaohaowu.com/jjewelry/web/resources/product/20250613/d54f4efe01cf4288a881b3d584296548/resource/d1b7d19e757f4059a219e9fb54f526ee/ae662755bc329993cb149d5e53896ba2/webp/ae662755bc329993cb149d5e53896ba2.webp\\\",\\\"https://static.zaohaowu.com/jjewelry/web/resources/product/20250613/cfa8699282fe4986b3878511f0dfa0fd/resource/949c48c6fa834e0c80c948164f50c302/3967aa60d06ee5426573eeddf8ec12db/webp/3967aa60d06ee5426573eeddf8ec12db.webp\\\"],\\\"price\\\":29.9,\\\"title\\\":\\\"异议之戒 \\\\\\\"I dissent\\\\\\\"\\\\n\\\"}}\",\n" +
                "                \"status\": 1,\n" +
                "                \"productionStatus\": 2,\n" +
                "                \"uniqueUser\": {\n" +
                "                    \"id\": \"51061baaa2084547ada33b1cfb637e57\",\n" +
                "                    \"nickname\": \"豹豹宝宝饱饱\",\n" +
                "                    \"avatar\": \"https://static.zaohaowu.com/jjewelry/web/images/avatar/e449004602374f2f83dc8329882aca6e/95cd4c4dfde3f1191f7082d4c591c7db.webp\",\n" +
                "                    \"accountLevel\": \"\"\n" +
                "                },\n" +
                "                \"ticketCountUpdateTime\": \"2025-06-13 11:06:12\",\n" +
                "                \"modifiedTime\": \"2025-07-06 16:43:44\",\n" +
                "                \"createTime\": \"2025-06-13 04:03:19\"\n" +
                "            },\n" +
                "            {\n" +
                "                \"uniqueProductId\": \"1c9c4acc5eb34a0cb6ef6875d689b179\",\n" +
                "                \"uniqueContentId\": \"0039611fba994d17873e2ff5028484fb\",\n" +
                "                \"uniqueImageId\": \"649a8c677fcd4d98b18389b3c9d41dd2.jjewelry-aigc-api.rml03vLgb0_3\",\n" +
                "                \"uniqueWantItId\": \"d4bee356efb4427496a96d33853bad1d\",\n" +
                "                \"detail\": \"{\\\"exposeFields\\\":{\\\"productStage\\\":1,\\\"productImages\\\":[\\\"https://static.zaohaowu.com/jjewelry/web/resources/product/20250616/22392a05bf834db2b721ad516cb03c4f/resource/8eb1a26f7ecb4b63b616592ba092a8f8/d8ffd81bc1e8128ea16cdc956f7c7834/webp/d8ffd81bc1e8128ea16cdc956f7c7834.webp\\\",\\\"https://static.zaohaowu.com/jjewelry/web/resources/product/20250616/32ff03371338452e889fd36f421409c9/resource/94af38202cc84092b2e6e2f818953507/50f792e49b0f3bd36739543391e0c338/webp/50f792e49b0f3bd36739543391e0c338.webp\\\"],\\\"price\\\":29.9,\\\"title\\\":\\\"家有恶猫英短戒指\\\\n\\\"}}\",\n" +
                "                \"status\": 1,\n" +
                "                \"productionStatus\": 2,\n" +
                "                \"uniqueUser\": {\n" +
                "                    \"id\": \"51061baaa2084547ada33b1cfb637e57\",\n" +
                "                    \"nickname\": \"豹豹宝宝饱饱\",\n" +
                "                    \"avatar\": \"https://static.zaohaowu.com/jjewelry/web/images/avatar/e449004602374f2f83dc8329882aca6e/95cd4c4dfde3f1191f7082d4c591c7db.webp\",\n" +
                "                    \"accountLevel\": \"\"\n" +
                "                },\n" +
                "                \"ticketCountUpdateTime\": \"2025-06-30 08:13:45\",\n" +
                "                \"modifiedTime\": \"2025-07-06 16:43:45\",\n" +
                "                \"createTime\": \"2025-06-13 04:04:54\"\n" +
                "            },\n" +
                "            {\n" +
                "                \"uniqueProductId\": \"2ef17d5cd98c4dbc9206b630021b0397\",\n" +
                "                \"uniqueContentId\": \"19fe691fbd5f4aa99c44a82fed3215f6\",\n" +
                "                \"uniqueImageId\": \"aa62e2a9de684a3498e74c462d102b87.jjewelry-aigc-api.bZKw6NcMhd_2\",\n" +
                "                \"uniqueWantItId\": \"f8e02272a4094188a81d3ca77b85758d\",\n" +
                "                \"detail\": \"{\\\"exposeFields\\\":{\\\"productStage\\\":0,\\\"productImages\\\":[\\\"https://static.zaohaowu.com/jjewelry/web/resources/product/20250507/99a0f42583604796964eb60c7ded8ff0/resource/599251e5bf134d92a989996201309057/27aeba4789d0dc7ec77c2e08b6cdd26d/webp/27aeba4789d0dc7ec77c2e08b6cdd26d.webp\\\",\\\"https://static.zaohaowu.com/jjewelry/web/resources/product/20250630/593163c43e7145a4bab07cc23a17b5e8/resource/ebfd38b03b1446e7901e07b29eb33756/3be3c5350eea0f47a29e07456ab8ae0b/webp/3be3c5350eea0f47a29e07456ab8ae0b.webp\\\"],\\\"price\\\":39.9,\\\"title\\\":\\\"小熊先生键帽\\\"}}\",\n" +
                "                \"status\": 1,\n" +
                "                \"productionStatus\": 2,\n" +
                "                \"uniqueUser\": {\n" +
                "                    \"id\": \"26fd0984e67347cfa541c7e60225c6c3\",\n" +
                "                    \"nickname\": \"兔子兔砸\",\n" +
                "                    \"avatar\": \"https://static.zaohaowu.com/jjewelry/web/images/avatar/926adfba0fe04ccb8a3d8e223ed4828d/076d3fc4345e206a176ecfebf2a2119d.webp\",\n" +
                "                    \"accountLevel\": \"\"\n" +
                "                },\n" +
                "                \"ticketCountUpdateTime\": \"2025-05-09 13:36:38\",\n" +
                "                \"modifiedTime\": \"2025-06-30 05:28:46\",\n" +
                "                \"createTime\": \"2025-05-07 05:58:42\"\n" +
                "            },\n" +
                "            {\n" +
                "                \"uniqueProductId\": \"d50b79ae9884400fbab38947bd680e27\",\n" +
                "                \"uniqueContentId\": \"8ca252bf07c54cd4b5790296a29614b2\",\n" +
                "                \"uniqueImageId\": \"b2b00a6a67d24906a8ef869805b0a1a4.jjewelry-aigc-api.BPaBYdUyN8_0\",\n" +
                "                \"uniqueWantItId\": \"44ba585ceb4843bfae7831051aba24b8\",\n" +
                "                \"detail\": \"{\\\"exposeFields\\\":{\\\"productStage\\\":1,\\\"productImages\\\":[\\\"https://static.zaohaowu.com/jjewelry/web/resources/product/20250507/553a752358254338a0a085633eeda2e5/resource/571722318e8240df9bfd2c3b3f23f6c2/88be7ee4534a5ae4a58c77cc2b141561/webp/88be7ee4534a5ae4a58c77cc2b141561.webp\\\",\\\"https://static.zaohaowu.com/jjewelry/web/resources/product/20250630/3508949e5bdf40de9cba93b826af7da5/resource/c14a9dd6c33a4b94bac9c0ed0c33593d/8b604f317fd30327fc7c1a9207a2491a/webp/8b604f317fd30327fc7c1a9207a2491a.webp\\\"],\\\"price\\\":39.9,\\\"title\\\":\\\"猫猫信号灯键帽\\\"}}\",\n" +
                "                \"status\": 1,\n" +
                "                \"productionStatus\": 2,\n" +
                "                \"uniqueUser\": {\n" +
                "                    \"id\": \"ee8f83c674d24451b37ebc708afe64ad\",\n" +
                "                    \"nickname\": \"Caroline\",\n" +
                "                    \"avatar\": \"https://static.zaohaowu.com/jjewelry/web/images/avatar/cedb12849c08436da3f79619cbd7e133/9f75931b25e14c0a7fcac2883accaefe.webp\",\n" +
                "                    \"accountLevel\": \"officialQualityCreator\"\n" +
                "                },\n" +
                "                \"ticketCountUpdateTime\": \"2025-05-20 10:09:16\",\n" +
                "                \"modifiedTime\": \"2025-06-30 05:24:18\",\n" +
                "                \"createTime\": \"2025-05-07 05:56:50\"\n" +
                "            },\n" +
                "            {\n" +
                "                \"uniqueProductId\": \"b1f3f271c5d04881af752d59192a19ba\",\n" +
                "                \"uniqueContentId\": \"1f287eb9e4524887844e797555caa5e7\",\n" +
                "                \"uniqueImageId\": \"f4e6b6c365d54394940095029f0b06eb.jjewelry-aigc-api.kWZExyxaCW_2\",\n" +
                "                \"uniqueWantItId\": \"81604d88b07d4364a1b05ed0c01629c3\",\n" +
                "                \"detail\": \"{\\\"exposeFields\\\":{\\\"productStage\\\":1,\\\"productImages\\\":[\\\"https://static.zaohaowu.com/jjewelry/web/resources/product/20250411/a6fe9cfce70746bfacdead4e145b76f6/resource/002d096eabf84511a59a4438770ed3eb/e689d3660512c12d0ddce506dfa4deae/webp/e689d3660512c12d0ddce506dfa4deae.webp\\\",\\\"https://static.zaohaowu.com/jjewelry/web/resources/product/20250411/538ef45fe5fd4a75bb049126e38049fe/resource/e633c7f672054fa5afb9a9d0265241e3/d07503fe7e9b6f44744bfd568349bb65/webp/d07503fe7e9b6f44744bfd568349bb65.webp\\\"],\\\"price\\\":39.9,\\\"title\\\":\\\"请你吃小笼包键帽\\\"}}\",\n" +
                "                \"status\": 1,\n" +
                "                \"productionStatus\": 2,\n" +
                "                \"uniqueUser\": {\n" +
                "                    \"id\": \"116ae7f2d7344f0494b62eae3273e717\",\n" +
                "                    \"nickname\": \"三水设计-可爱风版\",\n" +
                "                    \"avatar\": \"https://static.zaohaowu.com/jjewelry/web/images/avatar/0d13660f419048f5a146ceeb81620060/c94cc41495c4f376e3e5dce8696e8181.webp\",\n" +
                "                    \"accountLevel\": \"\"\n" +
                "                },\n" +
                "                \"ticketCountUpdateTime\": \"2025-04-11 02:11:57\",\n" +
                "                \"modifiedTime\": \"2025-06-30 04:56:03\",\n" +
                "                \"createTime\": \"2025-04-11 01:47:22\"\n" +
                "            },\n" +
                "            {\n" +
                "                \"uniqueProductId\": \"8db7f4ea8ac743c49366530535bb9eb7\",\n" +
                "                \"uniqueContentId\": \"42702a0e78e946628655e3ce6da32c3b\",\n" +
                "                \"uniqueImageId\": \"86c48c2035d541ec837274c13387b0b2.jjewelry-aigc-api.ZoStCKTi28_3\",\n" +
                "                \"uniqueWantItId\": \"f3c1ec76fac2481fadf14f8f88fbc52f\",\n" +
                "                \"detail\": \"{\\\"exposeFields\\\":{\\\"productStage\\\":1,\\\"productImages\\\":[\\\"https://static.zaohaowu.com/jjewelry/web/resources/product/20250610/9cf25f9b54b344d281f68d06e10e91c7/resource/d35f23ca355a4c80b8ac958855948727/aebbcb7661c987baf604ce7475b1a57b/webp/aebbcb7661c987baf604ce7475b1a57b.webp\\\",\\\"https://static.zaohaowu.com/jjewelry/web/resources/product/20250610/2eb70add557e4ea28fde323be32b0765/resource/949682a4ff154a65abb29a96b7c50924/e599347ba96ecaa17f932745c0bc8570/webp/e599347ba96ecaa17f932745c0bc8570.webp\\\"],\\\"price\\\":39.9,\\\"title\\\":\\\"小狗旺旺键帽\\\"}}\",\n" +
                "                \"status\": 1,\n" +
                "                \"productionStatus\": 2,\n" +
                "                \"uniqueUser\": {\n" +
                "                    \"id\": \"d23adea0bbca4d0c9ab03eadf4f09b74\",\n" +
                "                    \"nickname\": \"好运气\",\n" +
                "                    \"avatar\": \"https://static.zaohaowu.com/jjewelry/web/images/avatar/user-HMQoCE-2.png\",\n" +
                "                    \"accountLevel\": \"\"\n" +
                "                },\n" +
                "                \"ticketCountUpdateTime\": \"2025-06-12 15:13:47\",\n" +
                "                \"modifiedTime\": \"2025-06-30 04:52:12\",\n" +
                "                \"createTime\": \"2025-06-10 11:00:44\"\n" +
                "            },\n" +
                "            {\n" +
                "                \"uniqueProductId\": \"eb1043a7825b4589b9e36cee0b3cbc8e\",\n" +
                "                \"uniqueContentId\": \"0f5cf8b6533e41ecaf0b4839b737d0d2\",\n" +
                "                \"uniqueImageId\": \"9d777610eee84f89920a1129072bba39.jjewelry-aigc-api.w8eaq2ekCh_3\",\n" +
                "                \"detail\": \"{\\\"exposeFields\\\":{\\\"productImages\\\":[\\\"https://static.zaohaowu.com/jjewelry/web/resources/product/20250609/4a8b65d2d41140cc838aed6eb0005803/resource/427b81d6db7a4239aa1a5771687c0b49/a5d4f89952f302d05081f7d0bb958ef2/webp/a5d4f89952f302d05081f7d0bb958ef2.webp\\\",\\\"https://static.zaohaowu.com/jjewelry/web/resources/product/20250609/6d526ecb762945faa712c8647579098c/resource/56008f7decc648c1b3f1985636e0caa5/33eee63ee2f7458fc52664af013a1e99/webp/33eee63ee2f7458fc52664af013a1e99.webp\\\"],\\\"price\\\":39.9,\\\"title\\\":\\\"治疗药水键帽 \\\"}}\",\n" +
                "                \"status\": 1,\n" +
                "                \"productionStatus\": 2,\n" +
                "                \"uniqueUser\": {\n" +
                "                    \"id\": \"290ff560011f48458b29737a2b1ad36a\",\n" +
                "                    \"nickname\": \"CapGenius键设局\",\n" +
                "                    \"avatar\": \"https://static.zaohaowu.com/jjewelry/web/images/avatar/b83b0a4c7ee745c68d1313969d466527/188901c30a072a14ca4f8b573a78de3d.webp\",\n" +
                "                    \"accountLevel\": \"officialQualityCreator\"\n" +
                "                },\n" +
                "                \"modifiedTime\": \"2025-06-26 08:05:14\",\n" +
                "                \"createTime\": \"2025-06-09 10:37:37\"\n" +
                "            },\n" +
                "            {\n" +
                "                \"uniqueProductId\": \"f96d71a288ea46fb8ea1c6cb3bd01092\",\n" +
                "                \"uniqueContentId\": \"12c4127876924ef2975b6bdddf0e6676\",\n" +
                "                \"uniqueImageId\": \"fb33776b67b54d2eafd15b651eab0045.jjewelry-aigc-api.gMuHZzF41y_2\",\n" +
                "                \"uniqueWantItId\": \"e4a35e92cb2f413db3d7ac756d213ce9\",\n" +
                "                \"detail\": \"{\\\"exposeFields\\\":{\\\"productStage\\\":1,\\\"productImages\\\":[\\\"https://static.zaohaowu.com/jjewelry/web/resources/product/20250506/b15e5ee048d74c73ab5a6859eb8d6652/resource/191ac0c0c3d543e8a27143b75193a8b5/cf50242c5892ff50c6276877244c6d48/webp/cf50242c5892ff50c6276877244c6d48.webp\\\",\\\"https://static.zaohaowu.com/jjewelry/web/resources/product/20250506/1fa43372f020483cbac448be3cf4d1c2/resource/67ee1d3b71394ba88814b2546fc2b532/d8c5399873800c026af03be15462708b/webp/d8c5399873800c026af03be15462708b.webp\\\"],\\\"price\\\":39.9,\\\"title\\\":\\\"饼干键帽\\\"}}\",\n" +
                "                \"status\": 1,\n" +
                "                \"productionStatus\": 2,\n" +
                "                \"uniqueUser\": {\n" +
                "                    \"id\": \"551187fdcae643eea99ebda6fa906f4f\",\n" +
                "                    \"nickname\": \"Lu\",\n" +
                "                    \"avatar\": \"https://static.zaohaowu.com/jjewelry/web/images/avatar/6a33fc40f98c4deea9bf603a4939d6de/5965f83bc49805968a7813fc65bada0f.webp\",\n" +
                "                    \"accountLevel\": \"\"\n" +
                "                },\n" +
                "                \"ticketCountUpdateTime\": \"2025-06-09 10:30:58\",\n" +
                "                \"modifiedTime\": \"2025-06-10 12:31:33\",\n" +
                "                \"createTime\": \"2025-04-23 02:32:32\"\n" +
                "            },\n" +
                "            {\n" +
                "                \"uniqueProductId\": \"9ddf0257ab074edf9d875d9cf1079d0b\",\n" +
                "                \"uniqueContentId\": \"777bd81f0e8644f7a1e2ec17495fc8ce\",\n" +
                "                \"uniqueImageId\": \"a2c2c267bf9f44f0befabab4c29abe52.jjewelry-aigc-api.m5kj5QyPq1_3\",\n" +
                "                \"uniqueWantItId\": \"0a871d596ed94026bde3c0d6f3eb29d3\",\n" +
                "                \"detail\": \"{\\\"exposeFields\\\":{\\\"productStage\\\":1,\\\"productImages\\\":[\\\"https://static.zaohaowu.com/jjewelry/web/resources/product/20250514/ce5a2d416a814d5cbc1b61d6bfb26f78/resource/7df6a46f20c14ef8824aed71230a8965/46477fcf6e579f5e77d380d9d5c9c887/webp/46477fcf6e579f5e77d380d9d5c9c887.webp\\\",\\\"https://static.zaohaowu.com/jjewelry/web/resources/product/20250514/7a121c6d974e4a66955d5e68bc8965e3/resource/1c84a4a485d14092ad3dfecf8952e50f/e18eadf13c7c14cf30824614ce284c16/webp/e18eadf13c7c14cf30824614ce284c16.webp\\\"],\\\"price\\\":39.9,\\\"title\\\":\\\"解压猫爪键帽\\\"}}\",\n" +
                "                \"status\": 1,\n" +
                "                \"productionStatus\": 2,\n" +
                "                \"uniqueUser\": {\n" +
                "                    \"id\": \"ac9e41138ff748f0af7a85e8fbc1908d\",\n" +
                "                    \"nickname\": \"陈一诺言\",\n" +
                "                    \"avatar\": \"https://static.zaohaowu.com/jjewelry/web/images/avatar/3a1c644ddd944201a4a6e1f23170bd4d/9da16fb36e5599c3e5790dafa42c2a4d.webp\",\n" +
                "                    \"accountLevel\": \"\"\n" +
                "                },\n" +
                "                \"ticketCountUpdateTime\": \"2025-04-03 02:38:02\",\n" +
                "                \"modifiedTime\": \"2025-06-10 12:30:55\",\n" +
                "                \"createTime\": \"2025-04-01 17:05:50\"\n" +
                "            },\n" +
                "            {\n" +
                "                \"uniqueProductId\": \"ebabfbe2acf0406b8cec8e8bd3454634\",\n" +
                "                \"uniqueContentId\": \"cac85e7f949f4ed89f4190dfe2ffcfb1\",\n" +
                "                \"uniqueImageId\": \"240e6473079e460c9caebfd8f6e76a15.jjewelry-aigc-api.7wFDFkPSG0_2\",\n" +
                "                \"uniqueWantItId\": \"edac1a7c59cd4f79995a70cb8cb05417\",\n" +
                "                \"detail\": \"{\\\"exposeFields\\\":{\\\"productStage\\\":1,\\\"productImages\\\":[\\\"https://static.zaohaowu.com/jjewelry/web/resources/product/20250514/5137f10898ae478e8a7a37d832bf39f8/resource/7660a12cb7c24c788c26f58105575712/2434f18f32e1c3359c0b88d09188edbd/webp/2434f18f32e1c3359c0b88d09188edbd.webp\\\",\\\"https://static.zaohaowu.com/jjewelry/web/resources/product/20250514/a36a080518c64d908d8f37e0f347ebae/resource/b458fdd02a5647ddba2c23d7edb0cbe8/e18eadf13c7c14cf30824614ce284c16/webp/e18eadf13c7c14cf30824614ce284c16.webp\\\"],\\\"price\\\":39.9,\\\"title\\\":\\\"呆萌焦糖布丁兔兔键帽\\\"}}\",\n" +
                "                \"status\": 1,\n" +
                "                \"productionStatus\": 2,\n" +
                "                \"uniqueUser\": {\n" +
                "                    \"id\": \"1ded52d3635c4528b16f2608bfddf4b5\",\n" +
                "                    \"nickname\": \"YS\",\n" +
                "                    \"avatar\": \"https://static.zaohaowu.com/jjewelry/web/images/avatar/56d24a1522e1471389a115325cd0429f/eb8b5bdb462a95fd49ce0d60484b0f9a.webp\",\n" +
                "                    \"accountLevel\": \"\"\n" +
                "                },\n" +
                "                \"ticketCountUpdateTime\": \"2025-03-31 20:19:28\",\n" +
                "                \"modifiedTime\": \"2025-05-26 05:28:35\",\n" +
                "                \"createTime\": \"2025-03-31 20:09:56\"\n" +
                "            },\n" +
                "            {\n" +
                "                \"uniqueProductId\": \"211ea4d7eaff4d209e01fb95236979df\",\n" +
                "                \"uniqueContentId\": \"1d1043748f3b477abf057b7f4e384bef\",\n" +
                "                \"uniqueImageId\": \"1e8b970f1ce64eaf9279faba55c41404.jjewelry-aigc-api.idMT0HR2Zf_1\",\n" +
                "                \"uniqueWantItId\": \"4ca6d054d38441f58f782c0d93f8d91d\",\n" +
                "                \"detail\": \"{\\\"exposeFields\\\":{\\\"productStage\\\":1,\\\"productImages\\\":[\\\"https://static.zaohaowu.com/jjewelry/web/resources/product/20250514/cdc0bb0fc2424586bd8260479716bd60/resource/8d5e336ab1734424b31b93a8e727f5c6/3d6db4e2569404f3bd37d0ae885cd796/webp/3d6db4e2569404f3bd37d0ae885cd796.webp\\\",\\\"https://static.zaohaowu.com/jjewelry/web/resources/product/20250514/485925d7255244d59eea94f428ffd655/resource/82a9c46a65364065851b132547eedf76/e18eadf13c7c14cf30824614ce284c16/webp/e18eadf13c7c14cf30824614ce284c16.webp\\\"],\\\"price\\\":39.9,\\\"title\\\":\\\"可爱柴犬宝宝键帽\\\"}}\",\n" +
                "                \"status\": 1,\n" +
                "                \"productionStatus\": 2,\n" +
                "                \"uniqueUser\": {\n" +
                "                    \"id\": \"116ae7f2d7344f0494b62eae3273e717\",\n" +
                "                    \"nickname\": \"三水设计-可爱风版\",\n" +
                "                    \"avatar\": \"https://static.zaohaowu.com/jjewelry/web/images/avatar/0d13660f419048f5a146ceeb81620060/c94cc41495c4f376e3e5dce8696e8181.webp\",\n" +
                "                    \"accountLevel\": \"\"\n" +
                "                },\n" +
                "                \"ticketCountUpdateTime\": \"2025-04-10 04:23:29\",\n" +
                "                \"modifiedTime\": \"2025-06-10 12:30:24\",\n" +
                "                \"createTime\": \"2025-04-08 20:39:38\"\n" +
                "            },\n" +
                "            {\n" +
                "                \"uniqueProductId\": \"5c26646d4c7a4c34832aee8b399d1c25\",\n" +
                "                \"uniqueContentId\": \"46dd73d0aa434c2f9d909735bc76339c\",\n" +
                "                \"uniqueImageId\": \"cdc702b51e644c209396c3c80c13a6a3.jjewelry-aigc-api.XIe4cBfDHF_3\",\n" +
                "                \"uniqueWantItId\": \"6e1a4dc4d15e4f2fa6a22c6d1b438133\",\n" +
                "                \"detail\": \"{\\\"exposeFields\\\":{\\\"productStage\\\":1,\\\"productImages\\\":[\\\"https://static.zaohaowu.com/jjewelry/web/resources/product/20250506/f2f8e0b688c14c23966fbdfff64fdce2/resource/8450697bb8174121b4db7abd0fd1275c/7c4a5254d08887a247a80c4f3ec89ea8/webp/7c4a5254d08887a247a80c4f3ec89ea8.webp\\\",\\\"https://static.zaohaowu.com/jjewelry/web/resources/product/20250506/135256e4e24a4f198fa3d22c231da073/resource/d584f3ca013b4c7aa38b40763bc2a35f/738d8220c3f92f3d6679d35e0c5e98f1/webp/738d8220c3f92f3d6679d35e0c5e98f1.webp\\\"],\\\"price\\\":39.9,\\\"title\\\":\\\"蓝莓小兔子键帽\\\"}}\",\n" +
                "                \"status\": 1,\n" +
                "                \"productionStatus\": 2,\n" +
                "                \"uniqueUser\": {\n" +
                "                    \"id\": \"a821c902743f4d42bf94b72f206735fb\",\n" +
                "                    \"nickname\": \"冰镇石榴\",\n" +
                "                    \"avatar\": \"https://static.zaohaowu.com/jjewelry/web/images/avatar/8b170073805847b7987ad88cf0b935d8/74cd682b40cca42f3ac0b97f4d1ec5ce.webp\",\n" +
                "                    \"accountLevel\": \"\"\n" +
                "                },\n" +
                "                \"ticketCountUpdateTime\": \"2025-03-31 03:57:45\",\n" +
                "                \"modifiedTime\": \"2025-06-10 12:30:04\",\n" +
                "                \"createTime\": \"2025-03-31 03:55:43\"\n" +
                "            },\n" +
                "            {\n" +
                "                \"uniqueProductId\": \"e7c72e69e70247c5aaa8f5f3224e38d2\",\n" +
                "                \"uniqueContentId\": \"674f26cc9329429fb0eb89fc8dbc7423\",\n" +
                "                \"uniqueImageId\": \"934d540d578d4a10afe2266f06267605.jjewelry-aigc-api.P0YzRdpGwA_2\",\n" +
                "                \"uniqueWantItId\": \"ee4379f6e4fe4ce7b469880fa9209ed0\",\n" +
                "                \"detail\": \"{\\\"exposeFields\\\":{\\\"productStage\\\":1,\\\"productImages\\\":[\\\"https://static.zaohaowu.com/jjewelry/web/resources/product/20250506/37a893f5cdaf498b87a83e1c7d9c6bff/resource/3adf21128b844175ae5507c9aa200c07/270195c1c00055c3637ebaf6fc3e70bc/webp/270195c1c00055c3637ebaf6fc3e70bc.webp\\\",\\\"https://static.zaohaowu.com/jjewelry/web/resources/product/20250506/372efa8489f3468e8910bd12922fa150/resource/246a685a45e745db8a002bd5bba257bc/db278d9ca9097654c2a78f46b4acea77/webp/db278d9ca9097654c2a78f46b4acea77.webp\\\"],\\\"price\\\":39.9,\\\"title\\\":\\\"萌趣卡皮巴拉键帽\\\"}}\",\n" +
                "                \"status\": 1,\n" +
                "                \"productionStatus\": 2,\n" +
                "                \"uniqueUser\": {\n" +
                "                    \"id\": \"ee8f83c674d24451b37ebc708afe64ad\",\n" +
                "                    \"nickname\": \"Caroline\",\n" +
                "                    \"avatar\": \"https://static.zaohaowu.com/jjewelry/web/images/avatar/cedb12849c08436da3f79619cbd7e133/9f75931b25e14c0a7fcac2883accaefe.webp\",\n" +
                "                    \"accountLevel\": \"officialQualityCreator\"\n" +
                "                },\n" +
                "                \"ticketCountUpdateTime\": \"2025-04-08 03:09:37\",\n" +
                "                \"modifiedTime\": \"2025-06-10 12:29:49\",\n" +
                "                \"createTime\": \"2025-03-26 12:05:25\"\n" +
                "            },\n" +
                "            {\n" +
                "                \"uniqueProductId\": \"b7cf87235e0d4525afd090cf2c3b7185\",\n" +
                "                \"uniqueContentId\": \"fb36bb69e9d74c3da54b14c3b1bd404f\",\n" +
                "                \"uniqueImageId\": \"709af22a71f044e8977637efec38f518.jjewelry-aigc-api.UNSTQ2iHQD_3\",\n" +
                "                \"uniqueWantItId\": \"5fcfc2de877640eb993a1b58f36a9098\",\n" +
                "                \"detail\": \"{\\\"exposeFields\\\":{\\\"productStage\\\":1,\\\"productImages\\\":[\\\"https://static.zaohaowu.com/jjewelry/web/resources/product/20250506/65f4464a92ab43c4bda88d5a80034374/resource/80e8c5e20417461394fcb85cfe85d163/0a606a871d3f4a29045566c17d79d98e/webp/0a606a871d3f4a29045566c17d79d98e.webp\\\",\\\"https://static.zaohaowu.com/jjewelry/web/resources/product/20250506/662afa30906e4bfb9b868163128edc55/resource/862097a9eb3f4511bf6b4d3916739613/952ca80664cb5a5e11217b48f3d48e89/webp/952ca80664cb5a5e11217b48f3d48e89.webp\\\"],\\\"price\\\":39.9,\\\"title\\\":\\\"巧克力树莓蛋糕键帽\\\"}}\",\n" +
                "                \"status\": 1,\n" +
                "                \"productionStatus\": 2,\n" +
                "                \"uniqueUser\": {\n" +
                "                    \"id\": \"e87a9f8ed7084eafae2d3b485e9b620a\",\n" +
                "                    \"nickname\": \"Andy\",\n" +
                "                    \"avatar\": \"https://static.zaohaowu.com/jjewelry/web/images/avatar/cd72d8cc3d8647bcbfd3e6f3020e8cca/a9e84520dc12636069eada356c350434.webp\",\n" +
                "                    \"accountLevel\": \"\"\n" +
                "                },\n" +
                "                \"ticketCountUpdateTime\": \"2025-03-31 03:57:52\",\n" +
                "                \"modifiedTime\": \"2025-06-10 12:29:24\",\n" +
                "                \"createTime\": \"2025-03-31 03:54:31\"\n" +
                "            },\n" +
                "            {\n" +
                "                \"uniqueProductId\": \"bda76be0821d4d8c97f74e7dc4dbcc50\",\n" +
                "                \"uniqueContentId\": \"f7ba1c5ae0f64582b35e0ecc15223393\",\n" +
                "                \"uniqueImageId\": \"68df342dddba40428eb0062dfcb38b32.jjewelry-aigc-api.VF4L8FW5oL_3\",\n" +
                "                \"uniqueWantItId\": \"bb2b1f1f88f348f08d5dfe83061b5c10\",\n" +
                "                \"detail\": \"{\\\"exposeFields\\\":{\\\"productStage\\\":1,\\\"productImages\\\":[\\\"https://static.zaohaowu.com/jjewelry/web/resources/product/20250509/80c7c1fffb6143148788c12451ad7c94/resource/91e5d30294fd457f87d87608ed67e5da/750b61e63f4152c09499883bd0cfb9b3/webp/750b61e63f4152c09499883bd0cfb9b3.webp\\\",\\\"https://static.zaohaowu.com/jjewelry/web/resources/product/20250509/7d206484d36244e9a9e559e0bcc3f9a5/resource/f4f0682b0e204a69ac95d14704e8a06d/db278d9ca9097654c2a78f46b4acea77/webp/db278d9ca9097654c2a78f46b4acea77.webp\\\"],\\\"price\\\":39.9,\\\"title\\\":\\\"微型景观键帽\\\"}}\",\n" +
                "                \"status\": 1,\n" +
                "                \"productionStatus\": 2,\n" +
                "                \"uniqueUser\": {\n" +
                "                    \"id\": \"a7928149bafc42cc94decc0f0e7d4afb\",\n" +
                "                    \"nickname\": \"羊捏捏\",\n" +
                "                    \"avatar\": \"https://static.zaohaowu.com/jjewelry/web/images/avatar/41eceb8fd3c94e869c2ba320c101ddfc/bc4f352f10922ee630f266d63c2858c3.webp\",\n" +
                "                    \"accountLevel\": \"\"\n" +
                "                },\n" +
                "                \"ticketCountUpdateTime\": \"2025-04-16 04:45:35\",\n" +
                "                \"modifiedTime\": \"2025-06-10 12:28:38\",\n" +
                "                \"createTime\": \"2025-04-14 02:39:00\"\n" +
                "            },\n" +
                "            {\n" +
                "                \"uniqueProductId\": \"b76b01c037f449ed9971ecba41f5fa1d\",\n" +
                "                \"uniqueContentId\": \"1fdda575668642949033747202af2c08\",\n" +
                "                \"uniqueImageId\": \"1da006aafcbb458887437403b1b2168a.jjewelry-aigc-api.aLDBjzfjmP_0\",\n" +
                "                \"uniqueWantItId\": \"f7f5a27a17414d24a0331f14ffb12344\",\n" +
                "                \"detail\": \"{\\\"exposeFields\\\":{\\\"productStage\\\":1,\\\"productImages\\\":[\\\"https://static.zaohaowu.com/jjewelry/web/resources/product/20250506/22bbdb9b79ab4a0cb681d39ecf903192/resource/f62aec89a8e840c297b426194ea020fa/ef3a09c9c804e66688dcfcba14cf9e31/webp/ef3a09c9c804e66688dcfcba14cf9e31.webp\\\",\\\"https://static.zaohaowu.com/jjewelry/web/resources/product/20250506/29dd534ac3b44b0e9944e6f8e51ca93f/resource/08cbd027f254487fbdf9cfbc8fe0c1a2/b3d1aff44516a726cf610815bc46cf66/webp/b3d1aff44516a726cf610815bc46cf66.webp\\\"],\\\"price\\\":39.9,\\\"title\\\":\\\"雪山键帽\\\"}}\",\n" +
                "                \"status\": 1,\n" +
                "                \"productionStatus\": 2,\n" +
                "                \"uniqueUser\": {\n" +
                "                    \"id\": \"f7c1d601279b4dc68209c7a7dd2ba666\",\n" +
                "                    \"nickname\": \"问月\",\n" +
                "                    \"avatar\": \"https://static.zaohaowu.com/jjewelry/web/images/avatar/user-HMQoCE-2.png\",\n" +
                "                    \"accountLevel\": \"\"\n" +
                "                },\n" +
                "                \"ticketCountUpdateTime\": \"2025-04-09 02:37:38\",\n" +
                "                \"modifiedTime\": \"2025-06-10 12:28:09\",\n" +
                "                \"createTime\": \"2025-04-08 20:35:29\"\n" +
                "            },\n" +
                "            {\n" +
                "                \"uniqueProductId\": \"5a7cabbc8f2046e4b12dff400d65ea80\",\n" +
                "                \"uniqueContentId\": \"1b907a83a53742009083a1169ca45d82\",\n" +
                "                \"uniqueImageId\": \"dd9477e2c8c74106acda8c509c32b2bb.jjewelry-aigc-api.e4hAsMwAIs_3\",\n" +
                "                \"uniqueWantItId\": \"dc126a9e86b74094be42e0369b551afd\",\n" +
                "                \"detail\": \"{\\\"exposeFields\\\":{\\\"productStage\\\":1,\\\"productImages\\\":[\\\"https://static.zaohaowu.com/jjewelry/web/resources/product/20250506/a029f13edd104207ae197303f76f2e0e/resource/2845164f3d384c2d902c3bdcd5a88f27/95dfb7fe88772f27a16bd97b2ea92567/webp/95dfb7fe88772f27a16bd97b2ea92567.webp\\\",\\\"https://static.zaohaowu.com/jjewelry/web/resources/product/20250506/66101c02a9134d78bf53e99395af31aa/resource/9f5e07bd7a984863baa864a2ac65b9ca/db278d9ca9097654c2a78f46b4acea77/webp/db278d9ca9097654c2a78f46b4acea77.webp\\\"],\\\"price\\\":39.9,\\\"title\\\":\\\"勇敢牛牛键帽\\\"}}\",\n" +
                "                \"status\": 1,\n" +
                "                \"productionStatus\": 2,\n" +
                "                \"uniqueUser\": {\n" +
                "                    \"id\": \"ee8f83c674d24451b37ebc708afe64ad\",\n" +
                "                    \"nickname\": \"Caroline\",\n" +
                "                    \"avatar\": \"https://static.zaohaowu.com/jjewelry/web/images/avatar/cedb12849c08436da3f79619cbd7e133/9f75931b25e14c0a7fcac2883accaefe.webp\",\n" +
                "                    \"accountLevel\": \"officialQualityCreator\"\n" +
                "                },\n" +
                "                \"ticketCountUpdateTime\": \"2025-04-08 03:28:33\",\n" +
                "                \"modifiedTime\": \"2025-06-10 12:27:43\",\n" +
                "                \"createTime\": \"2025-04-02 17:45:42\"\n" +
                "            },\n" +
                "            {\n" +
                "                \"uniqueProductId\": \"0fc1aef51654449780900e1cf76e3c7b\",\n" +
                "                \"uniqueContentId\": \"7805e6243ca3481cb6ae2fb4f4150787\",\n" +
                "                \"uniqueImageId\": \"28e2ec9c699b4e03b435f11e5a6b0fe7.jjewelry-aigc-api.OF25Rbzv31_2\",\n" +
                "                \"uniqueWantItId\": \"3225cfa4dd5040ec8ee701a26a414f1f\",\n" +
                "                \"detail\": \"{\\\"exposeFields\\\":{\\\"productStage\\\":1,\\\"productImages\\\":[\\\"https://static.zaohaowu.com/jjewelry/web/resources/product/20250506/f69ebe20425843a6a7109f016b3085ca/resource/8ce2c7754faf4762a544ef9fde6f0b20/79a69b15c3c8a0ab904fae65b1f5b226/webp/79a69b15c3c8a0ab904fae65b1f5b226.webp\\\",\\\"https://static.zaohaowu.com/jjewelry/web/resources/product/20250506/3648569db58c4de6b0266b89005209ac/resource/11b885b16a864acabecf8495199c46b4/0ef59db91f66d6bc4646841fc57386f5/webp/0ef59db91f66d6bc4646841fc57386f5.webp\\\"],\\\"price\\\":39.9,\\\"title\\\":\\\"萌趣猫屁股键帽\\\"}}\",\n" +
                "                \"status\": 1,\n" +
                "                \"productionStatus\": 2,\n" +
                "                \"uniqueUser\": {\n" +
                "                    \"id\": \"ac9e41138ff748f0af7a85e8fbc1908d\",\n" +
                "                    \"nickname\": \"陈一诺言\",\n" +
                "                    \"avatar\": \"https://static.zaohaowu.com/jjewelry/web/images/avatar/3a1c644ddd944201a4a6e1f23170bd4d/9da16fb36e5599c3e5790dafa42c2a4d.webp\",\n" +
                "                    \"accountLevel\": \"\"\n" +
                "                },\n" +
                "                \"ticketCountUpdateTime\": \"2025-04-01 17:08:17\",\n" +
                "                \"modifiedTime\": \"2025-06-10 12:27:19\",\n" +
                "                \"createTime\": \"2025-04-01 17:08:00\"\n" +
                "            },\n" +
                "            {\n" +
                "                \"uniqueProductId\": \"342c3e02ef574189983559c317bda64b\",\n" +
                "                \"uniqueContentId\": \"c9cc61edda5d45ba94312410fe484cba\",\n" +
                "                \"uniqueImageId\": \"9ac94bab8ec749cebb6226bfd4136ec8.jjewelry-aigc-api.4jO8kBCtwx_1\",\n" +
                "                \"uniqueWantItId\": \"b91e81995d394fe19cb981b28e64b4c9\",\n" +
                "                \"detail\": \"{\\\"exposeFields\\\":{\\\"productStage\\\":1,\\\"productImages\\\":[\\\"https://static.zaohaowu.com/jjewelry/web/resources/product/20250506/4a761666515247a3849c1ae5aaecf107/resource/1c1aba457311456caaacedc2ece9541e/f0ccdb5332960607be82317395710c05/webp/f0ccdb5332960607be82317395710c05.webp\\\",\\\"https://static.zaohaowu.com/jjewelry/web/resources/product/20250506/67b36ab0eb4e48f880ccc832f13ecc96/resource/974bcec2c68743f8a18b0dfc6b93f232/ebbdee7386552137d9383bf88cddedb4/webp/ebbdee7386552137d9383bf88cddedb4.webp\\\"],\\\"price\\\":39.9,\\\"title\\\":\\\"天使猫猫键帽\\\"}}\",\n" +
                "                \"status\": 1,\n" +
                "                \"productionStatus\": 2,\n" +
                "                \"uniqueUser\": {\n" +
                "                    \"id\": \"1ded52d3635c4528b16f2608bfddf4b5\",\n" +
                "                    \"nickname\": \"YS\",\n" +
                "                    \"avatar\": \"https://static.zaohaowu.com/jjewelry/web/images/avatar/56d24a1522e1471389a115325cd0429f/eb8b5bdb462a95fd49ce0d60484b0f9a.webp\",\n" +
                "                    \"accountLevel\": \"\"\n" +
                "                },\n" +
                "                \"ticketCountUpdateTime\": \"2025-04-12 04:27:58\",\n" +
                "                \"modifiedTime\": \"2025-06-10 12:26:59\",\n" +
                "                \"createTime\": \"2025-04-11 01:43:05\"\n" +
                "            },\n" +
                "            {\n" +
                "                \"uniqueProductId\": \"bcc816956c81491dbf9be4d6fdbf6b56\",\n" +
                "                \"uniqueContentId\": \"24ae04a9c1894c6c94c624417c565cf4\",\n" +
                "                \"uniqueImageId\": \"11502c75984546f6be8d1908c6e9942f.jjewelry-aigc-api.TbrWdEKDH1_1\",\n" +
                "                \"uniqueWantItId\": \"cc6dfc2d5fe34c14b033324537a27e11\",\n" +
                "                \"detail\": \"{\\\"exposeFields\\\":{\\\"productStage\\\":1,\\\"productImages\\\":[\\\"https://static.zaohaowu.com/jjewelry/web/resources/product/20250506/6f2553c6336e432caa2d663be29188e1/resource/bd2d5d0c67184a10bbbe69fc4b7d4b67/0a2324382417efd0b162fa59134e73a9/webp/0a2324382417efd0b162fa59134e73a9.webp\\\",\\\"https://static.zaohaowu.com/jjewelry/web/resources/product/20250506/9ee9ff36d4b24a54a3b82938583cba26/resource/45f5dce6366d43e8883e27413e7c0a40/913fcd573a387a983f2411fcc3a9a0f3/webp/913fcd573a387a983f2411fcc3a9a0f3.webp\\\"],\\\"price\\\":39.9,\\\"title\\\":\\\"3D地球键帽\\\"}}\",\n" +
                "                \"status\": 1,\n" +
                "                \"productionStatus\": 2,\n" +
                "                \"uniqueUser\": {\n" +
                "                    \"id\": \"a5197859ea2c43e88468cd488a65b3b4\",\n" +
                "                    \"nickname\": \"Aq\",\n" +
                "                    \"avatar\": \"https://static.zaohaowu.com/jjewelry/web/images/avatar/user-HMQoCE-2.png\",\n" +
                "                    \"accountLevel\": \"\"\n" +
                "                },\n" +
                "                \"ticketCountUpdateTime\": \"2025-04-04 07:24:49\",\n" +
                "                \"modifiedTime\": \"2025-06-10 12:26:29\",\n" +
                "                \"createTime\": \"2025-04-03 19:13:05\"\n" +
                "            },\n" +
                "            {\n" +
                "                \"uniqueProductId\": \"540deae9842c45569a416da6906d258e\",\n" +
                "                \"uniqueContentId\": \"440fbe5174794b42af1e799d0e6c6bb1\",\n" +
                "                \"uniqueImageId\": \"b37494c20c03472594e6fe0cd7a805ca.jjewelry-aigc-api.OHaOqlt77l_1\",\n" +
                "                \"detail\": \"{\\\"exposeFields\\\":{\\\"productStage\\\":1,\\\"productImages\\\":[\\\"https://static.zaohaowu.com/jjewelry/web/resources/product/20250506/42c08e3b344245d79892b3d08eb9ef4b/resource/e65086fa0ad04ea0bfe47f59479a3953/fcc314d8bd899d7a2913bb21796caeb0/webp/fcc314d8bd899d7a2913bb21796caeb0.webp\\\",\\\"https://static.zaohaowu.com/jjewelry/web/resources/product/20250506/595c9ac7e5194217901b13f286f7cbdd/resource/cc2f8179acd341fb9f538c468de07f23/aef652ca9e0f7f75171deedcf06c1d2b/webp/aef652ca9e0f7f75171deedcf06c1d2b.webp\\\"],\\\"price\\\":39.9,\\\"title\\\":\\\"科幻时空键帽\\\"}}\",\n" +
                "                \"status\": 1,\n" +
                "                \"productionStatus\": 2,\n" +
                "                \"uniqueUser\": {\n" +
                "                    \"id\": \"8ec4107088e74cd9802853bd435d7046\",\n" +
                "                    \"nickname\": \"云想\",\n" +
                "                    \"avatar\": \"https://static.zaohaowu.com/jjewelry/web/images/avatar/c6f98fe97ba04aef924956799c40d197/596e2cae7e6f32ec0b12b9ea46e5a089.webp\",\n" +
                "                    \"accountLevel\": \"\"\n" +
                "                },\n" +
                "                \"modifiedTime\": \"2025-06-10 12:26:10\",\n" +
                "                \"createTime\": \"2025-04-03 08:04:47\"\n" +
                "            },\n" +
                "            {\n" +
                "                \"uniqueProductId\": \"c5b10a59666640e286227b2d4eb57589\",\n" +
                "                \"uniqueContentId\": \"9bbd85a67b15445e8f7403efc4905b88\",\n" +
                "                \"uniqueImageId\": \"750c46745f9f4df6b4db87a34749c4f6.jjewelry-aigc-api.nZNyNm9jDx_0\",\n" +
                "                \"uniqueWantItId\": \"d765cd86c32243b7b21de4f672db75dc\",\n" +
                "                \"detail\": \"{\\\"exposeFields\\\":{\\\"productStage\\\":1,\\\"productImages\\\":[\\\"https://static.zaohaowu.com/jjewelry/web/resources/product/20250411/fd6a4c4b3d504144935b804445c75efc/resource/2d92df358e8c4d2ea8810149768ae1f7/5a290361843b99cfa83542c12647f9d2/webp/5a290361843b99cfa83542c12647f9d2.webp\\\",\\\"https://static.zaohaowu.com/jjewelry/web/resources/product/20250411/c2f52af6f4544b46804d0c273beac369/resource/521bf10e02674fb7b589dd1b5495b734/8a01c149442bc471c393abf9feca8ecc/webp/8a01c149442bc471c393abf9feca8ecc.webp\\\"],\\\"price\\\":59,\\\"title\\\":\\\"熊猫拉花咖啡键帽\\\"}}\",\n" +
                "                \"status\": 1,\n" +
                "                \"productionStatus\": 2,\n" +
                "                \"uniqueUser\": {\n" +
                "                    \"id\": \"ee8f83c674d24451b37ebc708afe64ad\",\n" +
                "                    \"nickname\": \"Caroline\",\n" +
                "                    \"avatar\": \"https://static.zaohaowu.com/jjewelry/web/images/avatar/cedb12849c08436da3f79619cbd7e133/9f75931b25e14c0a7fcac2883accaefe.webp\",\n" +
                "                    \"accountLevel\": \"officialQualityCreator\"\n" +
                "                },\n" +
                "                \"ticketCountUpdateTime\": \"2025-03-05 03:20:21\",\n" +
                "                \"modifiedTime\": \"2025-06-10 12:24:55\",\n" +
                "                \"createTime\": \"2025-03-04 02:56:58\"\n" +
                "            },\n" +
                "            {\n" +
                "                \"uniqueProductId\": \"f79d7173e3ba4950a0bc713907e390bb\",\n" +
                "                \"uniqueContentId\": \"29af94fd326f4c3894497e428eaf743c\",\n" +
                "                \"uniqueImageId\": \"69e642f0b4b44cd5a4c6435f496bf261.jjewelry-aigc-api.paEHtJPxEL_3\",\n" +
                "                \"uniqueWantItId\": \"298595555bb5457aa86bb234cac3efdc\",\n" +
                "                \"detail\": \"{\\\"exposeFields\\\":{\\\"productStage\\\":1,\\\"productImages\\\":[\\\"https://static.zaohaowu.com/jjewelry/web/resources/product/20250411/a4554f04cdc649849844338a16f02b73/resource/b23707c2797e47b5b3581efc7915878f/1d55793438eb5398cf690253d1c9d3c9/webp/1d55793438eb5398cf690253d1c9d3c9.webp\\\",\\\"https://static.zaohaowu.com/jjewelry/web/resources/product/20250411/58aa3ac186a0401a8133b4bdfdc0f24f/resource/6298de45716244a389fbd9e323d0e65a/5bc072c260c032d9bbf8ea05c518505b/webp/5bc072c260c032d9bbf8ea05c518505b.webp\\\"],\\\"price\\\":39.9,\\\"title\\\":\\\"萌趣汉堡键帽\\\"}}\",\n" +
                "                \"status\": 1,\n" +
                "                \"productionStatus\": 2,\n" +
                "                \"uniqueUser\": {\n" +
                "                    \"id\": \"1c3247388896489988d629c23ed52099\",\n" +
                "                    \"nickname\": \"Eric\",\n" +
                "                    \"avatar\": \"https://static.zaohaowu.com/jjewelry/web/images/avatar/200d02bc4f3b466cb7e24fc7636c0383/90d845d2e99c7666a53f5634812554da.webp\",\n" +
                "                    \"accountLevel\": \"officialQualityCreator\"\n" +
                "                },\n" +
                "                \"ticketCountUpdateTime\": \"2025-04-01 17:10:08\",\n" +
                "                \"modifiedTime\": \"2025-05-29 03:10:34\",\n" +
                "                \"createTime\": \"2025-04-01 17:09:58\"\n" +
                "            },\n" +
                "            {\n" +
                "                \"uniqueProductId\": \"fa118baa058441e1ac4f2323b5772876\",\n" +
                "                \"uniqueContentId\": \"b7a54cf49ff64112b310777b1d5660d9\",\n" +
                "                \"uniqueImageId\": \"26bf791677c645b08a4d94c77f2f14fd.jjewelry-aigc-api.dmu6OszzIn_1\",\n" +
                "                \"uniqueWantItId\": \"b10d27e1a1a24909bbbcfe291b6b490e\",\n" +
                "                \"detail\": \"{\\\"exposeFields\\\":{\\\"productStage\\\":1,\\\"productImages\\\":[\\\"https://static.zaohaowu.com/jjewelry/web/resources/product/20250411/8642a1af92484142bde7d205536881ec/resource/4b84e2dcaeba49ff990289baf1dbbd60/11409f5b99759b7bb2f50dded64bdeb6/webp/11409f5b99759b7bb2f50dded64bdeb6.webp\\\",\\\"https://static.zaohaowu.com/jjewelry/web/resources/product/20250411/98473bc9751c4dcf8608043c2abaedd0/resource/41fd10e0e4cf4674acf61bb33a1fa610/c373c9ea30ea03be53c7ded8e8623635/webp/c373c9ea30ea03be53c7ded8e8623635.webp\\\"],\\\"price\\\":59,\\\"title\\\":\\\"沙滩键帽\\\"}}\",\n" +
                "                \"status\": 1,\n" +
                "                \"productionStatus\": 2,\n" +
                "                \"uniqueUser\": {\n" +
                "                    \"id\": \"14fed238cb784ca08213a8c6afe92876\",\n" +
                "                    \"nickname\": \"looooo\",\n" +
                "                    \"avatar\": \"https://static.zaohaowu.com/jjewelry/web/images/avatar/5d536bbcf17a4f519c6d85cc87f09bdc/ed02bfdd871339662179d660bf1deaa2.webp\",\n" +
                "                    \"accountLevel\": \"\"\n" +
                "                },\n" +
                "                \"ticketCountUpdateTime\": \"2025-02-28 10:04:52\",\n" +
                "                \"modifiedTime\": \"2025-05-29 03:10:39\",\n" +
                "                \"createTime\": \"2025-02-28 07:27:40\"\n" +
                "            },\n" +
                "            {\n" +
                "                \"uniqueProductId\": \"ada2fcf2cdf44a29b6d16c72e9eacdda\",\n" +
                "                \"uniqueContentId\": \"9ccf86a032564a0aac3c9b6ee9b554ee\",\n" +
                "                \"uniqueImageId\": \"52929222f40848c99ece6065396ef487.jjewelry-aigc-api.gLgvWEVkR3_0\",\n" +
                "                \"detail\": \"{\\\"exposeFields\\\":{\\\"productStage\\\":1,\\\"productImages\\\":[\\\"https://static.zaohaowu.com/jjewelry/web/resources/product/20250514/71f8074f21c74dfd96a049b83cc294b3/resource/64e7d122bf354ce79191844866458a50/1afa3dae7d4f3be59baf426144c22526/webp/1afa3dae7d4f3be59baf426144c22526.webp\\\",\\\"https://static.zaohaowu.com/jjewelry/web/resources/product/20250514/30b760f9be7543dab188cee3b6ba1c13/resource/ebbcd15966604ca095ed2339a8c57056/913fcd573a387a983f2411fcc3a9a0f3/webp/913fcd573a387a983f2411fcc3a9a0f3.webp\\\"],\\\"price\\\":59,\\\"title\\\":\\\"清凉药丸键帽\\\"}}\",\n" +
                "                \"status\": 1,\n" +
                "                \"productionStatus\": 2,\n" +
                "                \"uniqueUser\": {\n" +
                "                    \"id\": \"b3ae1eb2d67d468790aa1ffe322fa543\",\n" +
                "                    \"nickname\": \"Raw\",\n" +
                "                    \"avatar\": \"https://static.zaohaowu.com/jjewelry/web/images/avatar/3428593da6a4413caf158bd0e45abec1/96a2eb5bfa95504e486144c038e3c8d2.webp\",\n" +
                "                    \"accountLevel\": \"\"\n" +
                "                },\n" +
                "                \"modifiedTime\": \"2025-05-29 03:10:41\",\n" +
                "                \"createTime\": \"2025-02-24 07:51:27\"\n" +
                "            },\n" +
                "            {\n" +
                "                \"uniqueProductId\": \"0a59599c7f3b4eccb554b34f5f947e84\",\n" +
                "                \"uniqueContentId\": \"a8a08ff273b648f5b5fb0d9309a0720f\",\n" +
                "                \"uniqueImageId\": \"2dd24bd0aa864b21967c6f40567ba317.jjewelry-aigc-api.R25FpJetk3_3\",\n" +
                "                \"uniqueWantItId\": \"94e55f34490d4243893dbaa9ff386460\",\n" +
                "                \"detail\": \"{\\\"exposeFields\\\":{\\\"productStage\\\":1,\\\"productImages\\\":[\\\"https://static.zaohaowu.com/jjewelry/web/resources/product/20250411/491bbb0445714229aa579e449c23e530/resource/2d5f9d9be8ad40629c6f83cd005abe04/5864fb9821396ccbffb9adc421a20139/webp/5864fb9821396ccbffb9adc421a20139.webp\\\",\\\"https://static.zaohaowu.com/jjewelry/web/resources/product/20250411/c57778baeba643f9b4559f522b5a67da/resource/89bd1e684a924a09ad6866af2794853b/ffb83e8b8f64d844e0f684dcb9f97c51/webp/ffb83e8b8f64d844e0f684dcb9f97c51.webp\\\"],\\\"price\\\":39.9,\\\"title\\\":\\\"尖叫鸡键帽\\\"}}\",\n" +
                "                \"status\": 1,\n" +
                "                \"productionStatus\": 2,\n" +
                "                \"uniqueUser\": {\n" +
                "                    \"id\": \"ee8f83c674d24451b37ebc708afe64ad\",\n" +
                "                    \"nickname\": \"Caroline\",\n" +
                "                    \"avatar\": \"https://static.zaohaowu.com/jjewelry/web/images/avatar/cedb12849c08436da3f79619cbd7e133/9f75931b25e14c0a7fcac2883accaefe.webp\",\n" +
                "                    \"accountLevel\": \"officialQualityCreator\"\n" +
                "                },\n" +
                "                \"ticketCountUpdateTime\": \"2025-03-24 09:27:48\",\n" +
                "                \"modifiedTime\": \"2025-05-29 03:10:42\",\n" +
                "                \"createTime\": \"2025-03-24 09:27:36\"\n" +
                "            },\n" +
                "            {\n" +
                "                \"uniqueProductId\": \"1f5425e81f6f4424841abf097f243947\",\n" +
                "                \"uniqueContentId\": \"3ea4ef7646254dacbdacde74f54719d2\",\n" +
                "                \"uniqueImageId\": \"5455cd2609b749f883b01d876652f4c3.jjewelry-aigc-api.kvq6tSVGSj_0\",\n" +
                "                \"uniqueWantItId\": \"ff040be8e00c4ce894d7ea3fbb44cdda\",\n" +
                "                \"detail\": \"{\\\"exposeFields\\\":{\\\"productStage\\\":1,\\\"productImages\\\":[\\\"https://static.zaohaowu.com/jjewelry/web/resources/product/20250411/c52110e365644ffaa320aab6a93ee673/resource/b2bbb7dd725840fe9a628b24fd9e5c05/2df63748e52ce5787843d3170e602b27/webp/2df63748e52ce5787843d3170e602b27.webp\\\",\\\"https://static.zaohaowu.com/jjewelry/web/resources/product/20250411/86a61b9ac8a04b3089532aeec45e990d/resource/8a377ed3cd974c0c8ba21bc79fd9530e/8c3cd08c7533f4796ec68e5db74a04de/webp/8c3cd08c7533f4796ec68e5db74a04de.webp\\\"],\\\"price\\\":39.9,\\\"title\\\":\\\"萌趣汉堡卡皮巴拉键帽\\\"}}\",\n" +
                "                \"status\": 1,\n" +
                "                \"productionStatus\": 2,\n" +
                "                \"uniqueUser\": {\n" +
                "                    \"id\": \"bc80aa2ea3064f8988e9f5ddec32c1c4\",\n" +
                "                    \"nickname\": \"黄油猫\\uD83D\\uDC3E\",\n" +
                "                    \"avatar\": \"https://static.zaohaowu.com/jjewelry/web/images/avatar/c57f0d383d4145ebb32e2574b4149de4/5839c9b192fb819a1f459f3b1b2bb6a9.webp\",\n" +
                "                    \"accountLevel\": \"\"\n" +
                "                },\n" +
                "                \"ticketCountUpdateTime\": \"2025-03-28 13:45:32\",\n" +
                "                \"modifiedTime\": \"2025-05-29 02:57:05\",\n" +
                "                \"createTime\": \"2025-03-28 13:45:25\"\n" +
                "            },\n" +
                "            {\n" +
                "                \"uniqueProductId\": \"b32659f1dbe24fa9a73e14c03c8ae47e\",\n" +
                "                \"uniqueContentId\": \"122841d0c50a41ad9a840b3a52cfa785\",\n" +
                "                \"uniqueImageId\": \"d4e684ad81a74829a7151db88805bfe4.jjewelry-aigc-api.94swYwKlYD_2\",\n" +
                "                \"uniqueWantItId\": \"e46c1a61e41c40a89ce69ce695f24577\",\n" +
                "                \"detail\": \"{\\\"exposeFields\\\":{\\\"productStage\\\":1,\\\"productImages\\\":[\\\"https://static.zaohaowu.com/jjewelry/web/resources/product/20250411/a5270370804d47538cf36fd94dc3d5f0/resource/55e9c7ac382940d993409942df236ef3/788d95d8afdfd21a91636b139b18fc0f/webp/788d95d8afdfd21a91636b139b18fc0f.webp\\\",\\\"https://static.zaohaowu.com/jjewelry/web/resources/product/20250411/ff75c6c7a05a42a6aabcd7d8eb0dd423/resource/da251d07772544519427423aa669250c/eae055b68533c82ef03c2132c08cebeb/webp/eae055b68533c82ef03c2132c08cebeb.webp\\\"],\\\"price\\\":39.9,\\\"title\\\":\\\"卡布奇诺拉花键帽\\\"}}\",\n" +
                "                \"status\": 1,\n" +
                "                \"productionStatus\": 2,\n" +
                "                \"uniqueUser\": {\n" +
                "                    \"id\": \"07dd996c8ad04813b4b81cb2ae57d192\",\n" +
                "                    \"nickname\": \"Mars造物\",\n" +
                "                    \"avatar\": \"https://static.zaohaowu.com/jjewelry/web/images/avatar/f5cef63899f44617aea6bde988f66eed/683558052218c4bf9f43c8ce0421eaae.webp\",\n" +
                "                    \"accountLevel\": \"\"\n" +
                "                },\n" +
                "                \"ticketCountUpdateTime\": \"2025-03-24 12:04:52\",\n" +
                "                \"modifiedTime\": \"2025-05-29 03:11:37\",\n" +
                "                \"createTime\": \"2025-03-24 12:04:44\"\n" +
                "            },\n" +
                "            {\n" +
                "                \"uniqueProductId\": \"c8f66f9ab10e484bb7baebf7fd348034\",\n" +
                "                \"uniqueContentId\": \"a396581243c34a888d3dfad480e8f6ae\",\n" +
                "                \"uniqueImageId\": \"55096e8dd6f14f7ba74db9439ab6bf2a.jjewelry-aigc-api.2iHqbz4GwF_1\",\n" +
                "                \"uniqueWantItId\": \"156e59cf87114c4fa0ad1b42dd58f9ed\",\n" +
                "                \"detail\": \"{\\\"exposeFields\\\":{\\\"productStage\\\":1,\\\"productImages\\\":[\\\"https://static.zaohaowu.com/jjewelry/web/resources/product/20250411/ee93f88c85ba42509e9de44ddecc82c7/resource/0f0bbc2f7aca499f89e23444ffdd09a8/ef2ad9abf06f62edc6490742ba680d46/webp/ef2ad9abf06f62edc6490742ba680d46.webp\\\",\\\"https://static.zaohaowu.com/jjewelry/web/resources/product/20250411/760f02e57145421b8647cc8245bb82d5/resource/6892f07dd16148c6b14c9a7a57309e93/e111a01bcbe47a4c6ad5130a8fa1905a/webp/e111a01bcbe47a4c6ad5130a8fa1905a.webp\\\"],\\\"price\\\":39.9,\\\"title\\\":\\\"可可爱爱小苹果键帽\\\"}}\",\n" +
                "                \"status\": 1,\n" +
                "                \"productionStatus\": 2,\n" +
                "                \"uniqueUser\": {\n" +
                "                    \"id\": \"1c3247388896489988d629c23ed52099\",\n" +
                "                    \"nickname\": \"Eric\",\n" +
                "                    \"avatar\": \"https://static.zaohaowu.com/jjewelry/web/images/avatar/200d02bc4f3b466cb7e24fc7636c0383/90d845d2e99c7666a53f5634812554da.webp\",\n" +
                "                    \"accountLevel\": \"officialQualityCreator\"\n" +
                "                },\n" +
                "                \"ticketCountUpdateTime\": \"2025-03-31 20:19:32\",\n" +
                "                \"modifiedTime\": \"2025-05-29 02:57:07\",\n" +
                "                \"createTime\": \"2025-03-31 20:06:54\"\n" +
                "            },\n" +
                "            {\n" +
                "                \"uniqueProductId\": \"4d23f3b0083d4248881df18285e1a809\",\n" +
                "                \"uniqueContentId\": \"be9575ab133c401c842992bb3a596d5e\",\n" +
                "                \"uniqueImageId\": \"645733595e8f4f189f1e4d4cd39589e0.jjewelry-aigc-api.6czyhAMpBp_2\",\n" +
                "                \"uniqueWantItId\": \"df4ba675039745af991bda259678095d\",\n" +
                "                \"detail\": \"{\\\"exposeFields\\\":{\\\"productStage\\\":1,\\\"productImages\\\":[\\\"https://static.zaohaowu.com/jjewelry/web/resources/product/20250411/f876045c7df548aaa73ba02937bfe22e/resource/4da38a111c9b405094a7a1cc6ab7b466/e5cc3659d9caaf522b599155c9928af1/webp/e5cc3659d9caaf522b599155c9928af1.webp\\\",\\\"https://static.zaohaowu.com/jjewelry/web/resources/product/20250411/f6b2614190a146a1a118df59b4d8a1db/resource/29752a8fd37f4d39ae3e3f7d739f9dde/9840f79d423d4c7938881498d3192ca9/webp/9840f79d423d4c7938881498d3192ca9.webp\\\"],\\\"price\\\":39.9,\\\"title\\\":\\\"布莱猫键帽\\\"}}\",\n" +
                "                \"status\": 1,\n" +
                "                \"productionStatus\": 2,\n" +
                "                \"uniqueUser\": {\n" +
                "                    \"id\": \"890fe3455eb44e0a9d9f9408523f38e9\",\n" +
                "                    \"nickname\": \"BlackMeow\",\n" +
                "                    \"avatar\": \"https://static.zaohaowu.com/jjewelry/web/images/avatar/dd01edc98ccd4e2a92b564503fb41431/686d851c3a9d05571277101f575f8a2c.webp\",\n" +
                "                    \"accountLevel\": \"\"\n" +
                "                },\n" +
                "                \"ticketCountUpdateTime\": \"2025-03-26 06:51:03\",\n" +
                "                \"modifiedTime\": \"2025-05-29 02:57:09\",\n" +
                "                \"createTime\": \"2025-03-26 06:50:54\"\n" +
                "            },\n" +
                "            {\n" +
                "                \"uniqueProductId\": \"d6972a7832a4492e97b7691671ca2ea4\",\n" +
                "                \"uniqueContentId\": \"3733caaade8f472789f8a1bfcfce5881\",\n" +
                "                \"uniqueImageId\": \"64b98a41faa649a0a62584bea732f21d.jjewelry-aigc-api.wTFDHYzYWd_2\",\n" +
                "                \"uniqueWantItId\": \"01b61b44453946bab42e3c1d0bb1540b\",\n" +
                "                \"detail\": \"{\\\"exposeFields\\\":{\\\"productStage\\\":1,\\\"productImages\\\":[\\\"https://static.zaohaowu.com/jjewelry/web/resources/product/20250411/65cc9a4c351044f08329f326ca604e67/resource/a251242812c340ec98a89e26427cce5b/5ab089c3fa6f31cd5036a8088e4d7351/webp/5ab089c3fa6f31cd5036a8088e4d7351.webp\\\",\\\"https://static.zaohaowu.com/jjewelry/web/resources/product/20250411/a0a9fe3c82e04075a4acd340e2f92051/resource/a4ba78e4cedc47a297845905e9ea3fae/2d7d0229f480d4b76a89b3c49348095e/webp/2d7d0229f480d4b76a89b3c49348095e.webp\\\"],\\\"price\\\":39.9,\\\"title\\\":\\\"梦幻童话城堡键帽\\\"}}\",\n" +
                "                \"status\": 1,\n" +
                "                \"productionStatus\": 2,\n" +
                "                \"uniqueUser\": {\n" +
                "                    \"id\": \"0ca1b2a369d04fd1a23ea95cd5a28dc2\",\n" +
                "                    \"nickname\": \"爱玩的怂怂\",\n" +
                "                    \"avatar\": \"https://static.zaohaowu.com/jjewelry/web/images/avatar/a4fe795495474cfba8a56c9fa2016df3/78e6e711e31de514f1df68a161c713d9.webp\",\n" +
                "                    \"accountLevel\": \"\"\n" +
                "                },\n" +
                "                \"ticketCountUpdateTime\": \"2025-03-27 12:35:41\",\n" +
                "                \"modifiedTime\": \"2025-05-29 02:57:11\",\n" +
                "                \"createTime\": \"2025-03-27 12:35:34\"\n" +
                "            },\n" +
                "            {\n" +
                "                \"uniqueProductId\": \"22f2314bbd66483a8d9bc11e57654ea2\",\n" +
                "                \"uniqueContentId\": \"f503b7001ef941809ca2264f65a96758\",\n" +
                "                \"uniqueImageId\": \"c2af2929b86f41ec8d71189df8298885.jjewelry-aigc-api.rzuHw3BdJy_0\",\n" +
                "                \"uniqueWantItId\": \"93ef33554899418186091ecc67c541e7\",\n" +
                "                \"detail\": \"{\\\"exposeFields\\\":{\\\"productImages\\\":[\\\"https://static.zaohaowu.com/jjewelry/web/resources/product/20250411/579eab01d7ce4602bbd8f1abec414104/resource/92891152cd61430ab200d9d9ecfdfadd/1b5b4b221a5672f332bdf8db6e42de0d/webp/1b5b4b221a5672f332bdf8db6e42de0d.webp\\\",\\\"https://static.zaohaowu.com/jjewelry/web/resources/product/20250411/807c513ee050481a86150f76a66c74fd/resource/84edd0371aa94dc89c82f2dc3162d7f7/99e74b248cb6419064dbf72955d9527c/webp/99e74b248cb6419064dbf72955d9527c.webp\\\"],\\\"price\\\":39.9,\\\"title\\\":\\\"经典可乐键帽\\\"}}\",\n" +
                "                \"status\": 1,\n" +
                "                \"productionStatus\": 2,\n" +
                "                \"uniqueUser\": {\n" +
                "                    \"id\": \"1c3247388896489988d629c23ed52099\",\n" +
                "                    \"nickname\": \"Eric\",\n" +
                "                    \"avatar\": \"https://static.zaohaowu.com/jjewelry/web/images/avatar/200d02bc4f3b466cb7e24fc7636c0383/90d845d2e99c7666a53f5634812554da.webp\",\n" +
                "                    \"accountLevel\": \"officialQualityCreator\"\n" +
                "                },\n" +
                "                \"ticketCountUpdateTime\": \"2025-04-22 12:41:19\",\n" +
                "                \"modifiedTime\": \"2025-05-29 02:57:13\",\n" +
                "                \"createTime\": \"2025-04-11 19:01:40\"\n" +
                "            },\n" +
                "            {\n" +
                "                \"uniqueProductId\": \"1c49afbf936245bfa42c22c23128d9bf\",\n" +
                "                \"uniqueContentId\": \"0f30df19fdd14c9a97038c862e1ea453\",\n" +
                "                \"uniqueImageId\": \"e23caf66ff5442e8a1c74931e25eb093.jjewelry-aigc-api.wDVVXdOTbv_0\",\n" +
                "                \"uniqueWantItId\": \"f689f6bf2d4749cf8fe7bad7b227bc55\",\n" +
                "                \"detail\": \"{\\\"exposeFields\\\":{\\\"productStage\\\":1,\\\"productImages\\\":[\\\"https://static.zaohaowu.com/jjewelry/web/resources/product/20250411/cb04b101b58d4c0b8b22cdfdf4360c10/resource/439ad8e2f3b14f588ea4022ad2e8bb27/a32a0ad253ad8b6c1a0f0fd465b59f4f/webp/a32a0ad253ad8b6c1a0f0fd465b59f4f.webp\\\",\\\"https://static.zaohaowu.com/jjewelry/web/resources/product/20250411/e2135aea8cbd4fed9815969a3cfe920d/resource/b1bd2e99068f4f1689b3c0ddf0a6917f/968659428bae9d038f7ed118b7ccad44/webp/968659428bae9d038f7ed118b7ccad44.webp\\\"],\\\"price\\\":59,\\\"title\\\":\\\"治愈桃子猫咪键帽\\\"}}\",\n" +
                "                \"status\": 1,\n" +
                "                \"productionStatus\": 2,\n" +
                "                \"uniqueUser\": {\n" +
                "                    \"id\": \"ee8f83c674d24451b37ebc708afe64ad\",\n" +
                "                    \"nickname\": \"Caroline\",\n" +
                "                    \"avatar\": \"https://static.zaohaowu.com/jjewelry/web/images/avatar/cedb12849c08436da3f79619cbd7e133/9f75931b25e14c0a7fcac2883accaefe.webp\",\n" +
                "                    \"accountLevel\": \"officialQualityCreator\"\n" +
                "                },\n" +
                "                \"ticketCountUpdateTime\": \"2025-02-27 07:02:00\",\n" +
                "                \"modifiedTime\": \"2025-05-29 03:10:28\",\n" +
                "                \"createTime\": \"2025-02-27 05:45:52\"\n" +
                "            },\n" +
                "            {\n" +
                "                \"uniqueProductId\": \"65c265dd67bb49d294d2ff2a379d3693\",\n" +
                "                \"uniqueContentId\": \"c93af0f7b1f4421fb4cbcdf3ad3eeed4\",\n" +
                "                \"uniqueImageId\": \"d3c3cf9622954bb5a27b88b26660b452.jjewelry-aigc-api.ZnW6aX2kcT_3\",\n" +
                "                \"detail\": \"{\\\"exposeFields\\\":{\\\"productStage\\\":1,\\\"productImages\\\":[\\\"https://static.zaohaowu.com/jjewelry/web/resources/product/20250630/ccdc71e77117479d9b05bc5a60b8c9e1/resource/e61a6d766880456989f00188cef66faf/9a4dc92f4bea0b376f58a79262ae8035/webp/9a4dc92f4bea0b376f58a79262ae8035.webp\\\",\\\"https://static.zaohaowu.com/jjewelry/web/resources/product/20250630/ebd6a53e0de243319ed4bb08fb7b5739/resource/76ff4f4025cf4d92a12425be499ccca9/39c5b9741157e0f8d130541fa59a8c7c/webp/39c5b9741157e0f8d130541fa59a8c7c.webp\\\"],\\\"price\\\":59,\\\"title\\\":\\\"魔法猫头鹰键帽\\\"}}\",\n" +
                "                \"status\": 1,\n" +
                "                \"productionStatus\": 2,\n" +
                "                \"uniqueUser\": {\n" +
                "                    \"id\": \"c63e18b8239c4e7fa67e1a0a68fe8c72\",\n" +
                "                    \"nickname\": \"ppc\",\n" +
                "                    \"avatar\": \"https://static.zaohaowu.com/jjewelry/web/images/avatar/c11269f911a14fcdb24236545127a09d/21370b5f6aa9433447028a9a2cbb4387.webp\",\n" +
                "                    \"accountLevel\": \"\"\n" +
                "                },\n" +
                "                \"modifiedTime\": \"2025-06-30 05:17:58\",\n" +
                "                \"createTime\": \"2025-03-21 08:58:10\"\n" +
                "            },\n" +
                "            {\n" +
                "                \"uniqueProductId\": \"a81a97ee8ade40abbbe927c6dbf84b93\",\n" +
                "                \"uniqueContentId\": \"a25d2fcb444247e68738c5c5aae56244\",\n" +
                "                \"uniqueImageId\": \"8a54c5eae7624c0fa29541864fa538e0.jjewelry-aigc-api.K56E6smUFB_2\",\n" +
                "                \"uniqueWantItId\": \"4869ff4997a847abb43df2e419d44965\",\n" +
                "                \"detail\": \"{\\\"exposeFields\\\":{\\\"productStage\\\":1,\\\"productImages\\\":[\\\"https://static.zaohaowu.com/jjewelry/web/resources/product/20250609/9e017ed7a4bc4275aead47537d6f5000/resource/350625a9a397408f9dea3d161fe4f562/31c78a419dff4bee88e3dd1327c2ff30/webp/31c78a419dff4bee88e3dd1327c2ff30.webp\\\",\\\"https://static.zaohaowu.com/jjewelry/web/resources/product/20250609/7c25460991514f8cbb1c205b8879c7cc/resource/0075dd1f744e4e3fb7748268aa20f93d/10c47dec6e24d13994f339546ad9ea3f/webp/10c47dec6e24d13994f339546ad9ea3f.webp\\\"],\\\"price\\\":39.9,\\\"title\\\":\\\"像素高草键帽\\\"}}\",\n" +
                "                \"status\": 1,\n" +
                "                \"productionStatus\": 2,\n" +
                "                \"uniqueUser\": {\n" +
                "                    \"id\": \"290ff560011f48458b29737a2b1ad36a\",\n" +
                "                    \"nickname\": \"CapGenius键设局\",\n" +
                "                    \"avatar\": \"https://static.zaohaowu.com/jjewelry/web/images/avatar/b83b0a4c7ee745c68d1313969d466527/188901c30a072a14ca4f8b573a78de3d.webp\",\n" +
                "                    \"accountLevel\": \"officialQualityCreator\"\n" +
                "                },\n" +
                "                \"ticketCountUpdateTime\": \"2025-06-09 10:28:14\",\n" +
                "                \"modifiedTime\": \"2025-06-26 08:07:17\",\n" +
                "                \"createTime\": \"2025-06-09 10:28:07\"\n" +
                "            },\n" +
                "            {\n" +
                "                \"uniqueProductId\": \"82a7765772684b0fb04b4f40f4bae43d\",\n" +
                "                \"uniqueContentId\": \"1ad8fc679807496f8ab1840cbe990d6b\",\n" +
                "                \"uniqueImageId\": \"ac32519bbbb24d7384109c61c67eb9ef.jjewelry-aigc-api.HxJ79rx2Nm_0\",\n" +
                "                \"detail\": \"{\\\"exposeFields\\\":{\\\"productImages\\\":[\\\"https://static.zaohaowu.com/jjewelry/web/resources/product/20250609/381e5e25369b4b51b37418832ceabc51/resource/594f8aac6d1142f3b0367ca33c00f35a/508f7a5d66a2809a6e64c587624992ea/webp/508f7a5d66a2809a6e64c587624992ea.webp\\\",\\\"https://static.zaohaowu.com/jjewelry/web/resources/product/20250609/1cc34b4214bc4f6596551d3c542dcdb2/resource/e3643a49edbb4a26a0b9c7853a96c47a/f3be875cbda1e69f44f70c2502dcb69b/webp/f3be875cbda1e69f44f70c2502dcb69b.webp\\\"],\\\"price\\\":39.9,\\\"title\\\":\\\"海豚跃动键帽\\\"}}\",\n" +
                "                \"status\": 1,\n" +
                "                \"productionStatus\": 2,\n" +
                "                \"uniqueUser\": {\n" +
                "                    \"id\": \"290ff560011f48458b29737a2b1ad36a\",\n" +
                "                    \"nickname\": \"CapGenius键设局\",\n" +
                "                    \"avatar\": \"https://static.zaohaowu.com/jjewelry/web/images/avatar/b83b0a4c7ee745c68d1313969d466527/188901c30a072a14ca4f8b573a78de3d.webp\",\n" +
                "                    \"accountLevel\": \"officialQualityCreator\"\n" +
                "                },\n" +
                "                \"modifiedTime\": \"2025-06-26 08:06:45\",\n" +
                "                \"createTime\": \"2025-06-09 10:30:13\"\n" +
                "            },\n" +
                "            {\n" +
                "                \"uniqueProductId\": \"69a0178f0907470e95500bfc25ac7f26\",\n" +
                "                \"uniqueContentId\": \"72a79822a6194b378c4d26615c0e0374\",\n" +
                "                \"uniqueImageId\": \"2c9d6d9f4e734bcba388ee42a3ed071e.jjewelry-aigc-api.FblcCoiAwL_0\",\n" +
                "                \"detail\": \"{\\\"exposeFields\\\":{\\\"productImages\\\":[\\\"https://static.zaohaowu.com/jjewelry/web/resources/product/20250609/d5e47e2f3e9d4c449a7d7923b4b24eff/resource/6afaec91504148e5ae42f5ab21b6fe94/27f777e10cf960ecaa4065d1095e6446/webp/27f777e10cf960ecaa4065d1095e6446.webp\\\",\\\"https://static.zaohaowu.com/jjewelry/web/resources/product/20250609/a7106076a4e244c7aa56e6b3e30e5240/resource/b1e2c1f5c2f445dfa8527d94ef14e541/e1a4cead42b1b19122c6c1af8a771624/webp/e1a4cead42b1b19122c6c1af8a771624.webp\\\"],\\\"price\\\":39.9,\\\"title\\\":\\\"农场小鸡键帽\\\"}}\",\n" +
                "                \"status\": 1,\n" +
                "                \"productionStatus\": 2,\n" +
                "                \"uniqueUser\": {\n" +
                "                    \"id\": \"290ff560011f48458b29737a2b1ad36a\",\n" +
                "                    \"nickname\": \"CapGenius键设局\",\n" +
                "                    \"avatar\": \"https://static.zaohaowu.com/jjewelry/web/images/avatar/b83b0a4c7ee745c68d1313969d466527/188901c30a072a14ca4f8b573a78de3d.webp\",\n" +
                "                    \"accountLevel\": \"officialQualityCreator\"\n" +
                "                },\n" +
                "                \"modifiedTime\": \"2025-06-26 08:09:06\",\n" +
                "                \"createTime\": \"2025-06-09 10:32:46\"\n" +
                "            },\n" +
                "            {\n" +
                "                \"uniqueProductId\": \"34a0cbd533ab47ec80f3820fd09171c6\",\n" +
                "                \"uniqueContentId\": \"511330e4688e4c479648fb76facda644\",\n" +
                "                \"uniqueImageId\": \"a6c0b0181638487095257dff786a88d3.jjewelry-aigc-api.dStg9dSQoh_1\",\n" +
                "                \"detail\": \"{\\\"exposeFields\\\":{\\\"productStage\\\":1,\\\"productImages\\\":[\\\"https://static.zaohaowu.com/jjewelry/web/resources/product/20250529/fd733f647e154e92afca0d7402c82ad2/resource/71c0343de0884812a7511f490c9dfaa1/6a2cc24fd24dc9fe5ade518dbc0aac72/webp/6a2cc24fd24dc9fe5ade518dbc0aac72.webp\\\",\\\"https://static.zaohaowu.com/jjewelry/web/resources/product/20250529/8405a32119404b80859af2f705fdf754/resource/a0e177828105451e936b57257cc07f0c/55330e8ec3f93734229e81d6de361d88/webp/55330e8ec3f93734229e81d6de361d88.webp\\\"],\\\"price\\\":285.7,\\\"title\\\":\\\"死亡缠绕戒指\\\"}}\",\n" +
                "                \"status\": 1,\n" +
                "                \"productionStatus\": 2,\n" +
                "                \"uniqueUser\": {\n" +
                "                    \"id\": \"1e88f1db40de4ed4a0189745e099fd53\",\n" +
                "                    \"nickname\": \"momo1231\",\n" +
                "                    \"avatar\": \"https://static.zaohaowu.com/jjewelry/web/images/avatar/ac95bc0e864d4e3faa602552dbf7ad28/11f8d869850d5e9e5e2cbab299a074ea.webp\",\n" +
                "                    \"accountLevel\": \"officialQualityCreator\"\n" +
                "                },\n" +
                "                \"modifiedTime\": \"2025-06-10 12:16:52\",\n" +
                "                \"createTime\": \"2025-02-25 07:36:46\"\n" +
                "            },\n" +
                "            {\n" +
                "                \"uniqueProductId\": \"d5f7e4b7e2644588923fb1f3cf1be310\",\n" +
                "                \"uniqueContentId\": \"e3ca06a2eae94e6ab8ed3a37549e050b\",\n" +
                "                \"uniqueImageId\": \"413a47e3af724b16afab14a7b6d7e946.jjewelry-aigc-api.FnMgbfcJxq_3\",\n" +
                "                \"detail\": \"{\\\"exposeFields\\\":{\\\"productStage\\\":1,\\\"productImages\\\":[\\\"https://static.zaohaowu.com/jjewelry/web/resources/product/20250529/e962563856444d7a9d4d2fce6acae434/resource/d477c5cfcabc416ab53720b599951984/474862b43457e47d4504453fe551d73c/webp/474862b43457e47d4504453fe551d73c.webp\\\",\\\"https://static.zaohaowu.com/jjewelry/web/resources/product/20250610/ba3ff94719bb45cbad14e7c6379a2c0f/resource/e13daf6640e247a985a0cef222ba7e71/349d66e8689301a7972dd689f702bdc9/webp/349d66e8689301a7972dd689f702bdc9.webp\\\"],\\\"price\\\":114.3,\\\"title\\\":\\\"流体水银戒指\\\"}}\",\n" +
                "                \"status\": 1,\n" +
                "                \"productionStatus\": 2,\n" +
                "                \"uniqueUser\": {\n" +
                "                    \"id\": \"3dcbb201d4cc4298899e1ae953967822\",\n" +
                "                    \"nickname\": \"PUPU\",\n" +
                "                    \"avatar\": \"https://static.zaohaowu.com/jjewelry/web/images/avatar/79c14a42ca3f415bb958416f1a5a8643/4c86435745733441b0e4e10f2a2b174b.webp\",\n" +
                "                    \"accountLevel\": \"officialQualityCreator\"\n" +
                "                },\n" +
                "                \"modifiedTime\": \"2025-06-10 12:18:01\",\n" +
                "                \"createTime\": \"2025-02-24 12:10:44\"\n" +
                "            },\n" +
                "            {\n" +
                "                \"uniqueProductId\": \"63dacff47a9b453dbea2e40bc11adcf5\",\n" +
                "                \"uniqueContentId\": \"e5805fb2218d4bf487f8ae23f3f09ac7\",\n" +
                "                \"uniqueImageId\": \"98dd2d6abf714f6e99ce73e823c3cc1b.jjewelry-aigc-api.ZfhG9jRr9u_2\",\n" +
                "                \"detail\": \"{\\\"exposeFields\\\":{\\\"productStage\\\":1,\\\"productImages\\\":[\\\"https://static.zaohaowu.com/jjewelry/web/resources/product/20250509/94af0a5c05bb4ecb8062b625c561c8ff/resource/493e0b9c2b9b45ef935a5e2b85076adb/66eb829f7e6e02da958e5cc211794a7f/webp/66eb829f7e6e02da958e5cc211794a7f.webp\\\",\\\"https://static.zaohaowu.com/jjewelry/web/resources/product/20250509/0c65cf08a6b24f28b0d773e16b282073/resource/6cafb85baba74f7e9ac3e9f5c2b44ca4/6e2c4348bf79e09324d8895ce0d40f3c/webp/6e2c4348bf79e09324d8895ce0d40f3c.webp\\\"],\\\"price\\\":157.1,\\\"title\\\":\\\"极光之眼耳钉\\\"}}\",\n" +
                "                \"status\": 1,\n" +
                "                \"productionStatus\": 2,\n" +
                "                \"uniqueUser\": {\n" +
                "                    \"id\": \"3dcbb201d4cc4298899e1ae953967822\",\n" +
                "                    \"nickname\": \"PUPU\",\n" +
                "                    \"avatar\": \"https://static.zaohaowu.com/jjewelry/web/images/avatar/79c14a42ca3f415bb958416f1a5a8643/4c86435745733441b0e4e10f2a2b174b.webp\",\n" +
                "                    \"accountLevel\": \"officialQualityCreator\"\n" +
                "                },\n" +
                "                \"modifiedTime\": \"2025-06-10 12:14:13\",\n" +
                "                \"createTime\": \"2025-02-24 08:44:02\"\n" +
                "            },\n" +
                "            {\n" +
                "                \"uniqueProductId\": \"aabbbbd3b9594bc292db6b9b90511ced\",\n" +
                "                \"uniqueContentId\": \"f9bbb013ce0d4a06972273044a6509d7\",\n" +
                "                \"uniqueImageId\": \"13391e1ebd604d80b04e7c0e9b31aebd.jjewelry-aigc-api.2GknF8Cm12_0\",\n" +
                "                \"detail\": \"{\\\"exposeFields\\\":{\\\"productStage\\\":0,\\\"productImages\\\":[\\\"https://static.zaohaowu.com/jjewelry/web/resources/product/20250529/479b047e902c4e99a24582a419b8de90/resource/31bcbd137063464390e0d7a1bd18d974/eaf9a6632a7530f2964d486d6fdd8611/webp/eaf9a6632a7530f2964d486d6fdd8611.webp\\\",\\\"https://static.zaohaowu.com/jjewelry/web/resources/product/20250529/cd9587ae7bcf480ea6159a9cd8d8c5a7/resource/5a5705b3f78441f89215a322bd38b1ce/59837d6f2a18f6804e540c16e56ac3a6/webp/59837d6f2a18f6804e540c16e56ac3a6.webp\\\"],\\\"price\\\":1000,\\\"title\\\":\\\"生锈的锡罐机器人摆件\\\"}}\",\n" +
                "                \"status\": 1,\n" +
                "                \"productionStatus\": 2,\n" +
                "                \"uniqueUser\": {\n" +
                "                    \"id\": \"5fb445111f1a46cbaac515dca76c6015\",\n" +
                "                    \"nickname\": \"PastoralX\",\n" +
                "                    \"avatar\": \"https://static.zaohaowu.com/jjewelry/web/images/avatar/bd80f0eac0784b2ca9ee875c3ae54ca6/d3a4dc6985472206ef026b01a4daa82c.webp\",\n" +
                "                    \"accountLevel\": \"officialQualityCreator\"\n" +
                "                },\n" +
                "                \"modifiedTime\": \"2025-06-10 12:13:48\",\n" +
                "                \"createTime\": \"2025-02-25 02:25:38\"\n" +
                "            },\n" +
                "            {\n" +
                "                \"uniqueProductId\": \"8c27a0d71fbe48c09ba810e06a1151fc\",\n" +
                "                \"uniqueContentId\": \"1df7b929b531431599fd1311c9cbe1f9\",\n" +
                "                \"uniqueImageId\": \"c7d56a1c643345c5884285db76f930e3.jjewelry-aigc-api.4wHGG1AHa1_3\",\n" +
                "                \"detail\": \"{\\\"exposeFields\\\":{\\\"productStage\\\":0,\\\"productImages\\\":[\\\"https://static.zaohaowu.com/jjewelry/web/resources/product/20250228/24098618379b44e5be90c1f750b57bdd/resource/104a69c9891d42cea4e8f8f839557b6b/39bcdb24fd6dc54e17acc60b4c5d7329/webp/39bcdb24fd6dc54e17acc60b4c5d7329.webp\\\",\\\"https://static.zaohaowu.com/jjewelry/web/resources/product/20250228/4ae01256f87b4e178c8a43391363fb5c/resource/609f2ce0605b44368da0c6b2247b0268/0afecae5710aed6953392abb5e5b704b/webp/0afecae5710aed6953392abb5e5b704b.webp\\\"],\\\"price\\\":200,\\\"title\\\":\\\"赛博朋克机械蝴蝶戒指\\\"}}\",\n" +
                "                \"status\": 1,\n" +
                "                \"productionStatus\": 2,\n" +
                "                \"uniqueUser\": {\n" +
                "                    \"id\": \"75b8d8c341f449cd9e268ef52a1e72d6\",\n" +
                "                    \"nickname\": \"jessie\",\n" +
                "                    \"avatar\": \"https://static.zaohaowu.com/jjewelry/web/images/avatar/16cefe0f47b64e6b9f0e33b3d9576640/c2443b6ad37ef27129c2a595abd162d5.webp\",\n" +
                "                    \"accountLevel\": \"\"\n" +
                "                },\n" +
                "                \"modifiedTime\": \"2025-06-10 12:13:21\",\n" +
                "                \"createTime\": \"2025-02-28 09:17:29\"\n" +
                "            },\n" +
                "            {\n" +
                "                \"uniqueProductId\": \"60835c4ef1de42c28091eb212e7f823b\",\n" +
                "                \"uniqueContentId\": \"79f79c7099ba4c7fba07c8d6dec3a07b\",\n" +
                "                \"uniqueImageId\": \"97373178e5264c16a2bb370bcd1b03b6.jjewelry-aigc-api.NRKcgLlJS3_3\",\n" +
                "                \"detail\": \"{\\\"exposeFields\\\":{\\\"productStage\\\":0,\\\"productImages\\\":[\\\"https://static.zaohaowu.com/jjewelry/web/resources/product/20250421/8cd99472906d462e898cc932accf77f1/resource/dd02950f6b7440c98533637c79f661ee/b6e80ceb5a414af5fccc97e78c993d52/webp/b6e80ceb5a414af5fccc97e78c993d52.webp\\\",\\\"https://static.zaohaowu.com/jjewelry/web/resources/product/20250421/74969568870d423b922d212a9fc5616b/resource/e788bd37124b46d1a9b08f79e6d88554/64f95a69b57ebbdcbae68596144d9031/webp/64f95a69b57ebbdcbae68596144d9031.webp\\\"],\\\"price\\\":171.4,\\\"title\\\":\\\"泪眼神谕耳环\\\"}}\",\n" +
                "                \"status\": 1,\n" +
                "                \"productionStatus\": 2,\n" +
                "                \"uniqueUser\": {\n" +
                "                    \"id\": \"3dcbb201d4cc4298899e1ae953967822\",\n" +
                "                    \"nickname\": \"PUPU\",\n" +
                "                    \"avatar\": \"https://static.zaohaowu.com/jjewelry/web/images/avatar/79c14a42ca3f415bb958416f1a5a8643/4c86435745733441b0e4e10f2a2b174b.webp\",\n" +
                "                    \"accountLevel\": \"officialQualityCreator\"\n" +
                "                },\n" +
                "                \"modifiedTime\": \"2025-06-10 12:12:53\",\n" +
                "                \"createTime\": \"2025-03-03 03:23:44\"\n" +
                "            },\n" +
                "            {\n" +
                "                \"uniqueProductId\": \"b475649444db4c9f904c54a72a63ca30\",\n" +
                "                \"uniqueContentId\": \"e6c8beb9986e4c2aac52339f1d5d4cfe\",\n" +
                "                \"uniqueImageId\": \"9b24592d95bc497eaa20ea1caf31ffd1.jjewelry-aigc-api.KloS5KkvPH_0\",\n" +
                "                \"detail\": \"{\\\"exposeFields\\\":{\\\"productStage\\\":0,\\\"productImages\\\":[\\\"https://static.zaohaowu.com/jjewelry/web/resources/product/20250303/dcc3815f026c4c8ba523ad9ec913a1a8/resource/683fe40efb7b4abe86e6a1bdc153053a/8e13729fab92fc3cc89d701b9051ead7/webp/8e13729fab92fc3cc89d701b9051ead7.webp\\\",\\\"https://static.zaohaowu.com/jjewelry/web/resources/product/20250303/a8cc7c40eb76433ea023526cb65e35db/resource/794ced41368448debc98eee9722edb6b/463b6f20198fa2bee84bcad6dc4a4860/webp/463b6f20198fa2bee84bcad6dc4a4860.webp\\\"],\\\"price\\\":71.4,\\\"title\\\":\\\"夜光十字架项链\\\"}}\",\n" +
                "                \"status\": 1,\n" +
                "                \"productionStatus\": 2,\n" +
                "                \"uniqueUser\": {\n" +
                "                    \"id\": \"2162149aa8654c20b5be8627a2bc9ccd\",\n" +
                "                    \"nickname\": \"光子イチロー\",\n" +
                "                    \"avatar\": \"https://static.zaohaowu.com/jjewelry/web/images/avatar/e9a019e7e3114c7e8fa30b99da0f1fba/697b01a968b4087cfa1c749b36fa6d86.webp\",\n" +
                "                    \"accountLevel\": \"\"\n" +
                "                },\n" +
                "                \"modifiedTime\": \"2025-06-10 12:12:35\",\n" +
                "                \"createTime\": \"2025-03-03 03:26:36\"\n" +
                "            },\n" +
                "            {\n" +
                "                \"uniqueProductId\": \"c220eb395c05473993cef817c7262947\",\n" +
                "                \"uniqueContentId\": \"6f1793748ec1417c8d6470ff5719e02d\",\n" +
                "                \"uniqueImageId\": \"64be3ca989e64a5cb87160ad2e8fce61.jjewelry-aigc-api.k1njrhRbsO_1\",\n" +
                "                \"detail\": \"{\\\"exposeFields\\\":{\\\"productStage\\\":0,\\\"productImages\\\":[\\\"https://static.zaohaowu.com/jjewelry/web/resources/product/20250303/ca1aa2dee14f4a32bb7a746ded4d5e30/resource/5d88738466f244ec8bbf1a17a3465d66/d5e3bc42647782e124f49f9d289e0051/webp/d5e3bc42647782e124f49f9d289e0051.webp\\\",\\\"https://static.zaohaowu.com/jjewelry/web/resources/product/20250303/108a8c8b6e0b4786ae4a6c7d174d724a/resource/79bc0b69078848a1a4edc78e5cc0d1ca/5c2b7a7ccd7033183fc1475ddd2fa5f6/webp/5c2b7a7ccd7033183fc1475ddd2fa5f6.webp\\\"],\\\"price\\\":251.4,\\\"title\\\":\\\"铆钉戒指\\\"}}\",\n" +
                "                \"status\": 1,\n" +
                "                \"productionStatus\": 2,\n" +
                "                \"uniqueUser\": {\n" +
                "                    \"id\": \"2162149aa8654c20b5be8627a2bc9ccd\",\n" +
                "                    \"nickname\": \"光子イチロー\",\n" +
                "                    \"avatar\": \"https://static.zaohaowu.com/jjewelry/web/images/avatar/e9a019e7e3114c7e8fa30b99da0f1fba/697b01a968b4087cfa1c749b36fa6d86.webp\",\n" +
                "                    \"accountLevel\": \"\"\n" +
                "                },\n" +
                "                \"modifiedTime\": \"2025-05-23 10:32:23\",\n" +
                "                \"createTime\": \"2025-03-03 03:47:17\"\n" +
                "            },\n" +
                "            {\n" +
                "                \"uniqueProductId\": \"6a82333cf6cf47018140bf92e6c59b2b\",\n" +
                "                \"uniqueContentId\": \"0f6c01b6ce0544fa8031d569a74135d3\",\n" +
                "                \"uniqueImageId\": \"53c357c8692f42238ab856d30b03e8af.jjewelry-aigc-api.b2UTTUc9XH_2\",\n" +
                "                \"uniqueWantItId\": \"92499685e3fe432a8e05375d01ca449e\",\n" +
                "                \"detail\": \"{\\\"exposeFields\\\":{\\\"productStage\\\":1,\\\"productImages\\\":[\\\"https://static.zaohaowu.com/jjewelry/web/resources/product/20250328/020f8b15cc954571b3c981141ce73e95/resource/39b4a6d32e6646ca9b2c9ad582da232d/04c0a3e9936d1fc19a2644ad0eccc8f7/webp/04c0a3e9936d1fc19a2644ad0eccc8f7.webp\\\",\\\"https://static.zaohaowu.com/jjewelry/web/resources/product/20250328/48930b565fb44ae2889b25a8bab22176/resource/5a4eddf016a543df97d7d3342885697e/bcc2a3a66abffae53f2e396878ab397a/webp/bcc2a3a66abffae53f2e396878ab397a.webp\\\"],\\\"price\\\":39.9,\\\"title\\\":\\\"重生之我在故宫办公键帽\\\"}}\",\n" +
                "                \"status\": 1,\n" +
                "                \"productionStatus\": 2,\n" +
                "                \"uniqueUser\": {\n" +
                "                    \"id\": \"ee8f83c674d24451b37ebc708afe64ad\",\n" +
                "                    \"nickname\": \"Caroline\",\n" +
                "                    \"avatar\": \"https://static.zaohaowu.com/jjewelry/web/images/avatar/cedb12849c08436da3f79619cbd7e133/9f75931b25e14c0a7fcac2883accaefe.webp\",\n" +
                "                    \"accountLevel\": \"officialQualityCreator\"\n" +
                "                },\n" +
                "                \"ticketCountUpdateTime\": \"2025-04-03 12:15:43\",\n" +
                "                \"modifiedTime\": \"2025-05-23 10:30:13\",\n" +
                "                \"createTime\": \"2025-03-28 13:40:22\"\n" +
                "            },\n" +
                "            {\n" +
                "                \"uniqueProductId\": \"fb7e6bbb3fb24104bc2f7af795733194\",\n" +
                "                \"uniqueContentId\": \"f396b87d723941abb09dc7cd357bdb67\",\n" +
                "                \"uniqueImageId\": \"f34fe44ebd9b45f3853de482ef6dda99.jjewelry-aigc-api.acTjZtVjE3_3\",\n" +
                "                \"uniqueWantItId\": \"503ea7faf9e4472b87a01f09fb76e9db\",\n" +
                "                \"detail\": \"{\\\"exposeFields\\\":{\\\"productStage\\\":1,\\\"productImages\\\":[\\\"https://static.zaohaowu.com/jjewelry/web/resources/product/20250331/238e097a5ae7452eaddeaa9cc2cac99f/resource/7b581b82ef134852abbf8b0ebfb1da7a/38172192ae6bf56cc965cd28eadfd818/webp/38172192ae6bf56cc965cd28eadfd818.webp\\\",\\\"https://static.zaohaowu.com/jjewelry/web/resources/product/20250331/f51bed074bef4675be6aab22e9c770e4/resource/eab90ba8431549b399b88c9174a623de/e0e43a753cec25af950aa26df7393231/webp/e0e43a753cec25af950aa26df7393231.webp\\\"],\\\"price\\\":39.9,\\\"title\\\":\\\"绿色树叶键帽\\\"}}\",\n" +
                "                \"status\": 1,\n" +
                "                \"productionStatus\": 2,\n" +
                "                \"uniqueUser\": {\n" +
                "                    \"id\": \"1d118bf9d7084f6cb38c81949e145320\",\n" +
                "                    \"nickname\": \"花开一场梦\",\n" +
                "                    \"avatar\": \"https://static.zaohaowu.com/jjewelry/web/images/avatar/user-HMQoCE-2.png\",\n" +
                "                    \"accountLevel\": \"\"\n" +
                "                },\n" +
                "                \"ticketCountUpdateTime\": \"2025-03-31 20:19:00\",\n" +
                "                \"modifiedTime\": \"2025-05-23 10:28:40\",\n" +
                "                \"createTime\": \"2025-03-31 20:14:26\"\n" +
                "            },\n" +
                "            {\n" +
                "                \"uniqueProductId\": \"e4ac8ebbcc8840f09b3241d9e5251aff\",\n" +
                "                \"uniqueContentId\": \"8541e7debeed417dabba97a344c3ac43\",\n" +
                "                \"uniqueImageId\": \"4d9d374389984f2f829234960935c311.jjewelry-aigc-api.Zsittm1c18_0\",\n" +
                "                \"uniqueWantItId\": \"f2fe449c655a422ea33fee826c45a628\",\n" +
                "                \"detail\": \"{\\\"exposeFields\\\":{\\\"productStage\\\":1,\\\"productImages\\\":[\\\"https://static.zaohaowu.com/jjewelry/web/resources/product/20250331/0a21f322c71f4fc1aa61d83ac8454c4d/resource/29f7a16fd7c94c30a225da4abc111b8a/5a95419d92a4affe4636c3212d2abc85/webp/5a95419d92a4affe4636c3212d2abc85.webp\\\",\\\"https://static.zaohaowu.com/jjewelry/web/resources/product/20250331/f7037fa31c064bc49f2e473d48009aa0/resource/318234828ae94db98425f5582d356f50/c2e9731b3581f3713cf0179e041adb63/webp/c2e9731b3581f3713cf0179e041adb63.webp\\\"],\\\"price\\\":39.9,\\\"title\\\":\\\"萌趣暖暖猪键帽\\\"}}\",\n" +
                "                \"status\": 1,\n" +
                "                \"productionStatus\": 2,\n" +
                "                \"uniqueUser\": {\n" +
                "                    \"id\": \"fe62eb268d2c4590944ad44dff3f8829\",\n" +
                "                    \"nickname\": \"xiaoxiong\",\n" +
                "                    \"avatar\": \"https://static.zaohaowu.com/jjewelry/web/images/avatar/0ed5b14473ef46c088baf494b99130d3/3b8b9073d84d0db708b919bfb67c7b61.webp\",\n" +
                "                    \"accountLevel\": \"\"\n" +
                "                },\n" +
                "                \"ticketCountUpdateTime\": \"2025-03-31 20:18:55\",\n" +
                "                \"modifiedTime\": \"2025-06-10 12:03:29\",\n" +
                "                \"createTime\": \"2025-03-31 20:16:37\"\n" +
                "            },\n" +
                "            {\n" +
                "                \"uniqueProductId\": \"62320c22b09348a98504a27b03a977df\",\n" +
                "                \"uniqueContentId\": \"9fe3de07bb9e4b30bc30b4e6f2600add\",\n" +
                "                \"uniqueImageId\": \"9f9e0d12852b43aba948a1fa14ba1650.jjewelry-aigc-api.A1XgOyik5E_3\",\n" +
                "                \"uniqueWantItId\": \"824fe812586f48b4a02e76cebd0bc280\",\n" +
                "                \"detail\": \"{\\\"exposeFields\\\":{\\\"productStage\\\":1,\\\"productImages\\\":[\\\"https://static.zaohaowu.com/jjewelry/web/resources/product/20250402/000e3891c4e24f3495c39cbaa4206acb/resource/e14cdd87ab594f90bf8727c0c042949e/e9177949d8388218390ad3c89ee5378d/webp/e9177949d8388218390ad3c89ee5378d.webp\\\",\\\"https://static.zaohaowu.com/jjewelry/web/resources/product/20250402/20283c1da2b14b29ba53b6463a58020d/resource/eb85ced510bf4db4a838cf73ea5ba443/578715b37c19d4dca19f0acec15ca193/webp/578715b37c19d4dca19f0acec15ca193.webp\\\"],\\\"price\\\":39.9,\\\"title\\\":\\\"可爱小老虎键帽\\\"}}\",\n" +
                "                \"status\": 1,\n" +
                "                \"productionStatus\": 2,\n" +
                "                \"uniqueUser\": {\n" +
                "                    \"id\": \"07dd996c8ad04813b4b81cb2ae57d192\",\n" +
                "                    \"nickname\": \"Mars造物\",\n" +
                "                    \"avatar\": \"https://static.zaohaowu.com/jjewelry/web/images/avatar/f5cef63899f44617aea6bde988f66eed/683558052218c4bf9f43c8ce0421eaae.webp\",\n" +
                "                    \"accountLevel\": \"\"\n" +
                "                },\n" +
                "                \"ticketCountUpdateTime\": \"2025-04-02 17:41:03\",\n" +
                "                \"modifiedTime\": \"2025-06-10 12:03:01\",\n" +
                "                \"createTime\": \"2025-04-02 17:40:53\"\n" +
                "            },\n" +
                "            {\n" +
                "                \"uniqueProductId\": \"8f40ca7f1cb84408a63e2feb5da90b51\",\n" +
                "                \"uniqueContentId\": \"d2a7770dac6b42ac851d43d679ec4ce3\",\n" +
                "                \"uniqueImageId\": \"3d3f0baf6bba4cf9bf21369d09de8f06.jjewelry-aigc-api.WMWOq0UUEU_3\",\n" +
                "                \"uniqueWantItId\": \"e1cafedd22a942159a65d1498a81d971\",\n" +
                "                \"detail\": \"{\\\"exposeFields\\\":{\\\"productStage\\\":1,\\\"productImages\\\":[\\\"https://static.zaohaowu.com/jjewelry/web/resources/product/20250402/4387bf71803141b3b23d191d763c2813/resource/ee7e85155d0043b88ad7ce7798d2c5c5/1396920608ce7fb0339f3eed3eb8dfa4/webp/1396920608ce7fb0339f3eed3eb8dfa4.webp\\\",\\\"https://static.zaohaowu.com/jjewelry/web/resources/product/20250402/0004f93ad2e04a7f829a5d9d50a9040f/resource/8ca5ae5e99c04ac8aae33752e5f5670f/531da48bff6d7088a9445baf60e878fb/webp/531da48bff6d7088a9445baf60e878fb.webp\\\"],\\\"price\\\":39.9,\\\"title\\\":\\\"可爱兔兔键帽\\\"}}\",\n" +
                "                \"status\": 1,\n" +
                "                \"productionStatus\": 2,\n" +
                "                \"uniqueUser\": {\n" +
                "                    \"id\": \"07dd996c8ad04813b4b81cb2ae57d192\",\n" +
                "                    \"nickname\": \"Mars造物\",\n" +
                "                    \"avatar\": \"https://static.zaohaowu.com/jjewelry/web/images/avatar/f5cef63899f44617aea6bde988f66eed/683558052218c4bf9f43c8ce0421eaae.webp\",\n" +
                "                    \"accountLevel\": \"\"\n" +
                "                },\n" +
                "                \"ticketCountUpdateTime\": \"2025-04-02 17:42:25\",\n" +
                "                \"modifiedTime\": \"2025-06-10 12:02:36\",\n" +
                "                \"createTime\": \"2025-04-02 17:42:17\"\n" +
                "            },\n" +
                "            {\n" +
                "                \"uniqueProductId\": \"33b9502826a24c5d824de0894742ca4a\",\n" +
                "                \"uniqueContentId\": \"605f62cdc006441e81e4ed3ed9d8ad13\",\n" +
                "                \"uniqueImageId\": \"d9d058b3f3224a6cbe2509ac3a91341f.jjewelry-aigc-api.ssl5NVJ4LO_3\",\n" +
                "                \"uniqueWantItId\": \"93658bf5ce70431db9e0dc4dd25d5754\",\n" +
                "                \"detail\": \"{\\\"exposeFields\\\":{\\\"productStage\\\":1,\\\"productImages\\\":[\\\"https://static.zaohaowu.com/jjewelry/web/resources/product/20250402/f70eee46f39b41dda7fe40c20df9d1ec/resource/b7a1f2e84dd64499976bab4965237881/990d9042f40e14cc06c9d7943c282670/webp/990d9042f40e14cc06c9d7943c282670.webp\\\",\\\"https://static.zaohaowu.com/jjewelry/web/resources/product/20250402/6134b914cd2c4cfc87deaff26d1d31b5/resource/1344468047a64247af4bdce2fd115724/3429bc9e4d9e2c2a06fe28481abc470c/webp/3429bc9e4d9e2c2a06fe28481abc470c.webp\\\"],\\\"price\\\":39.9,\\\"title\\\":\\\"十二生肖之羊\\\"}}\",\n" +
                "                \"status\": 1,\n" +
                "                \"productionStatus\": 2,\n" +
                "                \"uniqueUser\": {\n" +
                "                    \"id\": \"ac9e41138ff748f0af7a85e8fbc1908d\",\n" +
                "                    \"nickname\": \"陈一诺言\",\n" +
                "                    \"avatar\": \"https://static.zaohaowu.com/jjewelry/web/images/avatar/3a1c644ddd944201a4a6e1f23170bd4d/9da16fb36e5599c3e5790dafa42c2a4d.webp\",\n" +
                "                    \"accountLevel\": \"\"\n" +
                "                },\n" +
                "                \"ticketCountUpdateTime\": \"2025-04-02 17:44:31\",\n" +
                "                \"modifiedTime\": \"2025-06-10 12:02:10\",\n" +
                "                \"createTime\": \"2025-04-02 17:44:25\"\n" +
                "            },\n" +
                "            {\n" +
                "                \"uniqueProductId\": \"1915691885b041169632bbe17c6289cd\",\n" +
                "                \"uniqueContentId\": \"c0753c0cdadd42ddb47e0723565acb3a\",\n" +
                "                \"uniqueImageId\": \"8fb5131f9d6640219dbd50157e8504ad.jjewelry-aigc-api.Pf1v3udqya_1\",\n" +
                "                \"detail\": \"{\\\"exposeFields\\\":{\\\"productStage\\\":1,\\\"productImages\\\":[\\\"https://static.zaohaowu.com/jjewelry/web/resources/product/20250403/ae83e5223d654d3da89015b7933d4e9d/resource/0285bcb617ae4e9e878b4ae90e882fb8/db421c80901f39c0fcb4a21056799794/webp/db421c80901f39c0fcb4a21056799794.webp\\\",\\\"https://static.zaohaowu.com/jjewelry/web/resources/product/20250403/197259e149c04964b6901a94eff38b35/resource/03135515e7f4409e8c0d1c00d745d51c/4a3ed957a401b7fee6d567aa00089ddf/webp/4a3ed957a401b7fee6d567aa00089ddf.webp\\\"],\\\"price\\\":39.9,\\\"title\\\":\\\"高精度微缩跑车键帽\\\"}}\",\n" +
                "                \"status\": 1,\n" +
                "                \"productionStatus\": 2,\n" +
                "                \"uniqueUser\": {\n" +
                "                    \"id\": \"6e56dd3c068a459098bb9921acb51fad\",\n" +
                "                    \"nickname\": \"亨利Henry微缩场景\",\n" +
                "                    \"avatar\": \"https://static.zaohaowu.com/jjewelry/web/images/avatar/3b72e7d9d6c9414ab4dca57dd49a6aab/3f6e40aceb9fc9cd019c108c103f43fb.webp\",\n" +
                "                    \"accountLevel\": \"\"\n" +
                "                },\n" +
                "                \"modifiedTime\": \"2025-06-10 12:00:28\",\n" +
                "                \"createTime\": \"2025-04-03 05:34:43\"\n" +
                "            },\n" +
                "            {\n" +
                "                \"uniqueProductId\": \"8282fd4095f048b7bd89380065827c2d\",\n" +
                "                \"uniqueContentId\": \"4b5f5bed915148be8b61e997029024d1\",\n" +
                "                \"uniqueImageId\": \"cf391fb4ba044aacbea68f5347945c01.jjewelry-aigc-api.rh9lrEBBGQ_0\",\n" +
                "                \"uniqueWantItId\": \"0f0bc56a63194ea0b3408f24d9ff2e05\",\n" +
                "                \"detail\": \"{\\\"exposeFields\\\":{\\\"productStage\\\":1,\\\"productImages\\\":[\\\"https://static.zaohaowu.com/jjewelry/web/resources/product/20250328/1273dde1a8e94e7a9f62a3d5d6769538/resource/386dae97840f42f98969d3a309ff0c7c/ecd816f85a9ab5c3bbfa1adb3013c091/webp/ecd816f85a9ab5c3bbfa1adb3013c091.webp\\\",\\\"https://static.zaohaowu.com/jjewelry/web/resources/product/20250328/0f513a8b23174a05b6755e53bd79f420/resource/c12bf920176341f5bfef30194deb487a/3666845fafdcd78b5c5c708f5b8f0762/webp/3666845fafdcd78b5c5c708f5b8f0762.webp\\\"],\\\"price\\\":39.9,\\\"title\\\":\\\"故宫玉兰花键帽\\\"}}\",\n" +
                "                \"status\": 1,\n" +
                "                \"productionStatus\": 2,\n" +
                "                \"uniqueUser\": {\n" +
                "                    \"id\": \"52463fbbf6d64cc29237983ce26f4d61\",\n" +
                "                    \"nickname\": \"宫禧福\",\n" +
                "                    \"avatar\": \"https://static.zaohaowu.com/jjewelry/web/images/avatar/user-HMQoCE-2.png\",\n" +
                "                    \"accountLevel\": \"\"\n" +
                "                },\n" +
                "                \"ticketCountUpdateTime\": \"2025-03-28 13:44:21\",\n" +
                "                \"modifiedTime\": \"2025-06-10 11:59:55\",\n" +
                "                \"createTime\": \"2025-03-28 13:44:07\"\n" +
                "            },\n" +
                "            {\n" +
                "                \"uniqueProductId\": \"df7d4c7885e849f289895d773e09b675\",\n" +
                "                \"uniqueContentId\": \"3862a5aa253243da90d8c9fd96318c52\",\n" +
                "                \"uniqueImageId\": \"3e3dfd83d055449e86bc46d6e1c5b5ec.jjewelry-aigc-api.KTTgmZigGd_0\",\n" +
                "                \"uniqueWantItId\": \"87055dfbc5034adf8cdecffcc966716e\",\n" +
                "                \"detail\": \"{\\\"exposeFields\\\":{\\\"productStage\\\":1,\\\"productImages\\\":[\\\"https://static.zaohaowu.com/jjewelry/web/resources/product/20250331/68b75976327f4e1b83f6795e7342fc91/resource/d1b1b377d5ad455298152e83ef5fb1a9/5b3613951bc4cbd9ea18b4d243cfa8f7/webp/5b3613951bc4cbd9ea18b4d243cfa8f7.webp\\\",\\\"https://static.zaohaowu.com/jjewelry/web/resources/product/20250331/2cbda89a3d5947f8be66ebc94540b830/resource/56d24db5ceba4f92ab32a189e128a275/cc3a09e3c2bbc034f21ad1c3f8650c4b/webp/cc3a09e3c2bbc034f21ad1c3f8650c4b.webp\\\"],\\\"price\\\":39.9,\\\"title\\\":\\\"沉默海岸键帽\\\"}}\",\n" +
                "                \"status\": 1,\n" +
                "                \"productionStatus\": 2,\n" +
                "                \"uniqueUser\": {\n" +
                "                    \"id\": \"fb6e640709ff4d1ea8f29cbaf4343d62\",\n" +
                "                    \"nickname\": \"星谕天工\",\n" +
                "                    \"avatar\": \"https://static.zaohaowu.com/jjewelry/web/images/avatar/3128f6c2021c44e594149970da141dbf/f02a78bf70fc01390f11717d3bb76e22.webp\",\n" +
                "                    \"accountLevel\": \"\"\n" +
                "                },\n" +
                "                \"ticketCountUpdateTime\": \"2025-03-31 20:19:03\",\n" +
                "                \"modifiedTime\": \"2025-06-10 11:59:20\",\n" +
                "                \"createTime\": \"2025-03-31 20:12:24\"\n" +
                "            },\n" +
                "            {\n" +
                "                \"uniqueProductId\": \"e06c37dfb03a401c92c571d1dc7eade3\",\n" +
                "                \"uniqueContentId\": \"273d1ea404034b34a7ae23062dd1aa3b\",\n" +
                "                \"uniqueImageId\": \"0d5637ca1753403984b745e2c9effa3a.jjewelry-aigc-api.0Kfwe0kx2o_2\",\n" +
                "                \"uniqueWantItId\": \"9c4cbd0de7e548cc95db2ffe65af04d0\",\n" +
                "                \"detail\": \"{\\\"exposeFields\\\":{\\\"productStage\\\":1,\\\"productImages\\\":[\\\"https://static.zaohaowu.com/jjewelry/web/resources/product/20250331/d614de853efb459b95c905aed8247568/resource/da565e218cdd4dbdad86d80d8119469b/338c3fa623d2a32fbfacc03701b8ec2c/webp/338c3fa623d2a32fbfacc03701b8ec2c.webp\\\",\\\"https://static.zaohaowu.com/jjewelry/web/resources/product/20250331/04bf5206064e4814b7679397e8e097a7/resource/f1befd453bc14d6ead8033d6280361e0/f274ef23ede5b9635d356ef6c2f372d8/webp/f274ef23ede5b9635d356ef6c2f372d8.webp\\\"],\\\"price\\\":39.9,\\\"title\\\":\\\"美杜莎键帽\\\"}}\",\n" +
                "                \"status\": 1,\n" +
                "                \"productionStatus\": 2,\n" +
                "                \"uniqueUser\": {\n" +
                "                    \"id\": \"07dd996c8ad04813b4b81cb2ae57d192\",\n" +
                "                    \"nickname\": \"Mars造物\",\n" +
                "                    \"avatar\": \"https://static.zaohaowu.com/jjewelry/web/images/avatar/f5cef63899f44617aea6bde988f66eed/683558052218c4bf9f43c8ce0421eaae.webp\",\n" +
                "                    \"accountLevel\": \"\"\n" +
                "                },\n" +
                "                \"ticketCountUpdateTime\": \"2025-03-31 20:19:31\",\n" +
                "                \"modifiedTime\": \"2025-06-10 11:57:37\",\n" +
                "                \"createTime\": \"2025-03-31 20:11:02\"\n" +
                "            },\n" +
                "            {\n" +
                "                \"uniqueProductId\": \"314280e68f5d44bab6f2ebc63a4bbfd7\",\n" +
                "                \"uniqueContentId\": \"ed8289bc6a9b44a8b09f4de43b4434c5\",\n" +
                "                \"uniqueImageId\": \"5643ec96d1834b39909bf9958840bcec.jjewelry-aigc-api.HU5eqBvRGS_3\",\n" +
                "                \"uniqueWantItId\": \"ebd7d57ec38b4657b4309b122dff925b\",\n" +
                "                \"detail\": \"{\\\"exposeFields\\\":{\\\"productStage\\\":1,\\\"productImages\\\":[\\\"https://static.zaohaowu.com/jjewelry/web/resources/product/20250403/0b2cdebbdecc46d3b5d14d368c50de5b/resource/f51cd9f99fad4b7daa2e88479c144732/a47d8ff5ef05e3ab1897a3a414a0cfcf/webp/a47d8ff5ef05e3ab1897a3a414a0cfcf.webp\\\",\\\"https://static.zaohaowu.com/jjewelry/web/resources/product/20250403/af7b1eb0f65a40a3a3b4ba24c3dcf85c/resource/6d39680c77074b31b94ad286b2036079/d84b65824bb642c7d0c6c3dbd8dfc54a/webp/d84b65824bb642c7d0c6c3dbd8dfc54a.webp\\\"],\\\"price\\\":39.9,\\\"title\\\":\\\"复古摇杆键帽\\\"}}\",\n" +
                "                \"status\": 1,\n" +
                "                \"productionStatus\": 2,\n" +
                "                \"uniqueUser\": {\n" +
                "                    \"id\": \"a5197859ea2c43e88468cd488a65b3b4\",\n" +
                "                    \"nickname\": \"Aq\",\n" +
                "                    \"avatar\": \"https://static.zaohaowu.com/jjewelry/web/images/avatar/user-HMQoCE-2.png\",\n" +
                "                    \"accountLevel\": \"\"\n" +
                "                },\n" +
                "                \"ticketCountUpdateTime\": \"2025-04-04 07:24:27\",\n" +
                "                \"modifiedTime\": \"2025-06-10 11:56:18\",\n" +
                "                \"createTime\": \"2025-04-03 19:06:26\"\n" +
                "            },\n" +
                "            {\n" +
                "                \"uniqueProductId\": \"9c404124b269477c807286fb8b4dd9a9\",\n" +
                "                \"uniqueContentId\": \"5d94758d5a7d4aeaa283e4ce7dc433de\",\n" +
                "                \"uniqueImageId\": \"c50324cd20ec4607b8dcdf0a74b5da88.jjewelry-aigc-api.TYk9bzOV26_2\",\n" +
                "                \"uniqueWantItId\": \"338e643fc2084d0da07084f84a804d6b\",\n" +
                "                \"detail\": \"{\\\"exposeFields\\\":{\\\"productStage\\\":1,\\\"productImages\\\":[\\\"https://static.zaohaowu.com/jjewelry/web/resources/product/20250403/45fbad0bcd5c4900aab39b45084e98b6/resource/e9ddc6cc77464299a17cedc087170f54/1ddde991da5918bcb0e4a78d3c8b8f3f/webp/1ddde991da5918bcb0e4a78d3c8b8f3f.webp\\\",\\\"https://static.zaohaowu.com/jjewelry/web/resources/product/20250403/1c94cf951e1b493cb1d8992c18714ac2/resource/c2977eeee7574b2eaba95ebf16cd7c58/1d06b8013df5ebf2f9be82f3d748492b/webp/1d06b8013df5ebf2f9be82f3d748492b.webp\\\"],\\\"price\\\":39.9,\\\"title\\\":\\\"博物馆珍藏键帽\\\"}}\",\n" +
                "                \"status\": 1,\n" +
                "                \"productionStatus\": 2,\n" +
                "                \"uniqueUser\": {\n" +
                "                    \"id\": \"3af6010149b448bc80b8158f43f67734\",\n" +
                "                    \"nickname\": \"造物梦工厂\",\n" +
                "                    \"avatar\": \"https://static.zaohaowu.com/jjewelry/web/images/avatar/82a29dc156d04092bc1349077bb42c21/5f2cf656f962cb3a15fba3ee72b59a33.webp\",\n" +
                "                    \"accountLevel\": \"\"\n" +
                "                },\n" +
                "                \"ticketCountUpdateTime\": \"2025-04-04 09:31:59\",\n" +
                "                \"modifiedTime\": \"2025-06-18 08:28:29\",\n" +
                "                \"createTime\": \"2025-04-03 19:14:42\"\n" +
                "            },\n" +
                "            {\n" +
                "                \"uniqueProductId\": \"c1f9621af7a8445ab507b9eba9aa8147\",\n" +
                "                \"uniqueContentId\": \"264d63139477481981f4aa4d90c6573b\",\n" +
                "                \"uniqueImageId\": \"e6e96a2f41584d988629738587392cd0.jjewelry-aigc-api.dV9CcrSlcE_2\",\n" +
                "                \"uniqueWantItId\": \"7ca916f155f9465898c940f75a89b49e\",\n" +
                "                \"detail\": \"{\\\"exposeFields\\\":{\\\"productStage\\\":1,\\\"productImages\\\":[\\\"https://static.zaohaowu.com/jjewelry/web/resources/product/20250408/b8ddbfc98e6d4d81b12bfe008e8a4219/resource/a8c613a3609c4d9099fffd418f830c8f/828e220775d4ed878530ba39814eead5/webp/828e220775d4ed878530ba39814eead5.webp\\\",\\\"https://static.zaohaowu.com/jjewelry/web/resources/product/20250408/774dbe5888af4adb923c9611befb1436/resource/ff665bb699e74a39bd66b9de9b5a86d8/d2a9dd78a776bf6f00a31479f309b8eb/webp/d2a9dd78a776bf6f00a31479f309b8eb.webp\\\"],\\\"price\\\":39.9,\\\"title\\\":\\\"朋克老鼠键帽\\\"}}\",\n" +
                "                \"status\": 1,\n" +
                "                \"productionStatus\": 2,\n" +
                "                \"uniqueUser\": {\n" +
                "                    \"id\": \"f336da278fbd43afaa744b06bdd3be1d\",\n" +
                "                    \"nickname\": \"COOKER NEVER DIE\",\n" +
                "                    \"avatar\": \"https://static.zaohaowu.com/jjewelry/web/images/avatar/8a35842b74774cf8a98f852802b7cc6e/b551b1af279acbf65a796081e69feaa1.webp\",\n" +
                "                    \"accountLevel\": \"officialQualityCreator\"\n" +
                "                },\n" +
                "                \"ticketCountUpdateTime\": \"2025-04-09 15:28:05\",\n" +
                "                \"modifiedTime\": \"2025-06-10 11:54:46\",\n" +
                "                \"createTime\": \"2025-04-08 20:31:34\"\n" +
                "            },\n" +
                "            {\n" +
                "                \"uniqueProductId\": \"773de8feb99442cdac9ffc354012b4ef\",\n" +
                "                \"uniqueContentId\": \"4ca38d6362674d06b34f7a3078fd83b3\",\n" +
                "                \"uniqueImageId\": \"d9af9d0bf3be40b7951394c9c225e263.jjewelry-aigc-api.ufBKC7RrDv_3\",\n" +
                "                \"uniqueWantItId\": \"80f10363cb9c428ba5ed1c04f7d5c5e8\",\n" +
                "                \"detail\": \"{\\\"exposeFields\\\":{\\\"productStage\\\":1,\\\"productImages\\\":[\\\"https://static.zaohaowu.com/jjewelry/web/resources/product/20250408/c05fe2b69d3c438c9338f620ceae07e0/resource/82e032daa4b04103968c5d77d06c9ae0/a01a6f0426474abc9e5bd86a3d7bd915/webp/a01a6f0426474abc9e5bd86a3d7bd915.webp\\\",\\\"https://static.zaohaowu.com/jjewelry/web/resources/product/20250408/eb9594990cb84bcf9b5200716b06c76f/resource/0d3dedee03284314a60eee8a63e97229/7d656d7408c3d0e94df97ab5e35efd38/webp/7d656d7408c3d0e94df97ab5e35efd38.webp\\\"],\\\"price\\\":39.9,\\\"title\\\":\\\"德文猫哈哈键帽\\\"}}\",\n" +
                "                \"status\": 1,\n" +
                "                \"productionStatus\": 2,\n" +
                "                \"uniqueUser\": {\n" +
                "                    \"id\": \"2bd87c634b6e41169db69e69b3a1ef19\",\n" +
                "                    \"nickname\": \"ENTP的大脑黑洞\",\n" +
                "                    \"avatar\": \"https://static.zaohaowu.com/jjewelry/web/images/avatar/c3f37763f0634780807f9e33a30ad4a2/cbd53edc9fb5559ffbec33663fa4af07.webp\",\n" +
                "                    \"accountLevel\": \"\"\n" +
                "                },\n" +
                "                \"ticketCountUpdateTime\": \"2025-04-14 02:40:15\",\n" +
                "                \"modifiedTime\": \"2025-06-10 11:54:17\",\n" +
                "                \"createTime\": \"2025-04-08 20:33:03\"\n" +
                "            },\n" +
                "            {\n" +
                "                \"uniqueProductId\": \"3706487bc76b43f18063a9c81dd3e255\",\n" +
                "                \"uniqueContentId\": \"cefe5e3f79cf403d897214ac3a98385a\",\n" +
                "                \"uniqueImageId\": \"be08f130ded64cee92bd5346e88faf2e.jjewelry-aigc-api.QVZ3DrSTra_3\",\n" +
                "                \"uniqueWantItId\": \"ef71fe8ea975485cae8254ab9434b09f\",\n" +
                "                \"detail\": \"{\\\"exposeFields\\\":{\\\"productStage\\\":1,\\\"productImages\\\":[\\\"https://static.zaohaowu.com/jjewelry/web/resources/product/20250408/daf6e074940c46919a72d9e8fed45a4f/resource/03ea4a488ce244b4b937d5dc030dbdb9/65fd3ed86aa87acda310c6733f998ae8/webp/65fd3ed86aa87acda310c6733f998ae8.webp\\\",\\\"https://static.zaohaowu.com/jjewelry/web/resources/product/20250408/85dcaf8d90be4d60adff3fab6d3de107/resource/bc440051aef740b69d0c8584471a0f29/ad8c1082b7d3e24494da90878ce96a5f/webp/ad8c1082b7d3e24494da90878ce96a5f.webp\\\"],\\\"price\\\":39.9,\\\"title\\\":\\\"绿草皮键帽\\\"}}\",\n" +
                "                \"status\": 1,\n" +
                "                \"productionStatus\": 2,\n" +
                "                \"uniqueUser\": {\n" +
                "                    \"id\": \"05148c79b8e34821bb7c1ac0d2aa3551\",\n" +
                "                    \"nickname\": \"Linlin\",\n" +
                "                    \"avatar\": \"https://static.zaohaowu.com/jjewelry/web/images/avatar/5cea1cf122a54e448a0640d6dbc6a393/7a430ccf1a82512cb3d1fa82bc8e409b.webp\",\n" +
                "                    \"accountLevel\": \"\"\n" +
                "                },\n" +
                "                \"ticketCountUpdateTime\": \"2025-04-08 20:35:38\",\n" +
                "                \"modifiedTime\": \"2025-06-10 11:53:38\",\n" +
                "                \"createTime\": \"2025-04-08 20:34:19\"\n" +
                "            },\n" +
                "            {\n" +
                "                \"uniqueProductId\": \"150be0cf1a07441484a5c5b239a1d050\",\n" +
                "                \"uniqueContentId\": \"83563e4ac8b6417c84231439a3e7fd08\",\n" +
                "                \"uniqueImageId\": \"9e7bd9d39e7146638238a317cd025425.jjewelry-aigc-api.cDVuEK08dp_3\",\n" +
                "                \"uniqueWantItId\": \"e318439d26d947babd4031d79ae23b10\",\n" +
                "                \"detail\": \"{\\\"exposeFields\\\":{\\\"productStage\\\":1,\\\"productImages\\\":[\\\"https://static.zaohaowu.com/jjewelry/web/resources/product/20250408/4858ed9a0b3448a5a786864a3d939b73/resource/3a5567aaac3647c093055a760b3dff84/0dec332a17b99472b744b850f4349638/webp/0dec332a17b99472b744b850f4349638.webp\\\",\\\"https://static.zaohaowu.com/jjewelry/web/resources/product/20250408/614dbf2e45af4a3f94d96b32ebdac8f1/resource/3bd796227dda431c880cb62105ee04e6/f86f72890991d200558e1fd21f1f799e/webp/f86f72890991d200558e1fd21f1f799e.webp\\\"],\\\"price\\\":39.9,\\\"title\\\":\\\"消防栓键帽\\\"}}\",\n" +
                "                \"status\": 1,\n" +
                "                \"productionStatus\": 2,\n" +
                "                \"uniqueUser\": {\n" +
                "                    \"id\": \"f7c1d601279b4dc68209c7a7dd2ba666\",\n" +
                "                    \"nickname\": \"问月\",\n" +
                "                    \"avatar\": \"https://static.zaohaowu.com/jjewelry/web/images/avatar/user-HMQoCE-2.png\",\n" +
                "                    \"accountLevel\": \"\"\n" +
                "                },\n" +
                "                \"ticketCountUpdateTime\": \"2025-04-09 02:38:05\",\n" +
                "                \"modifiedTime\": \"2025-06-10 11:53:04\",\n" +
                "                \"createTime\": \"2025-04-08 20:36:58\"\n" +
                "            },\n" +
                "            {\n" +
                "                \"uniqueProductId\": \"a4d61f95a83e4c6fbc0213409b578f65\",\n" +
                "                \"uniqueContentId\": \"c58ffb0bd7384be89899b527fa52678f\",\n" +
                "                \"uniqueImageId\": \"31a63a5bf1164353a86796b86fee84eb.jjewelry-aigc-api.yRmsObmLiK_1\",\n" +
                "                \"uniqueWantItId\": \"5b56c432d1d14dc39fb595cf2eeae7c6\",\n" +
                "                \"detail\": \"{\\\"exposeFields\\\":{\\\"productStage\\\":1,\\\"productImages\\\":[\\\"https://static.zaohaowu.com/jjewelry/web/resources/product/20250514/f401296372084345a7cb895d59fdb2aa/resource/ce2980bd94ad4863a413eb1701103da6/43b108b9dc3ac19223f56eae072f2372/webp/43b108b9dc3ac19223f56eae072f2372.webp\\\",\\\"https://static.zaohaowu.com/jjewelry/web/resources/product/20250514/9c2cb235b7214a26a0fe441c560c68b8/resource/ff1e374e6c9a4b8dab0a90e19656d656/39cbfd4b4506aeffb8effe8b9db2c844/webp/39cbfd4b4506aeffb8effe8b9db2c844.webp\\\"],\\\"price\\\":39.9,\\\"title\\\":\\\"趣味青蛙键帽\\\"}}\",\n" +
                "                \"status\": 1,\n" +
                "                \"productionStatus\": 2,\n" +
                "                \"uniqueUser\": {\n" +
                "                    \"id\": \"f7b6dd7518754ab6b027f42425b183d9\",\n" +
                "                    \"nickname\": \"buptidsuper\",\n" +
                "                    \"avatar\": \"https://static.zaohaowu.com/jjewelry/web/images/avatar/050c488f537f42ebb8ecd8f33cfe8aae/e7ead7dec3ef127f3094304ca6ff9b94.webp\",\n" +
                "                    \"accountLevel\": \"\"\n" +
                "                },\n" +
                "                \"ticketCountUpdateTime\": \"2025-04-09 02:19:48\",\n" +
                "                \"modifiedTime\": \"2025-06-10 11:52:31\",\n" +
                "                \"createTime\": \"2025-04-08 20:38:18\"\n" +
                "            },\n" +
                "            {\n" +
                "                \"uniqueProductId\": \"1b540fc56bfa46fea6f461e07cdd5ab4\",\n" +
                "                \"uniqueContentId\": \"482e6d6a879b4f0fb4c3f585485d68f0\",\n" +
                "                \"uniqueImageId\": \"7753f8c9f8fc444a9cab5c50b5ea4190.jjewelry-aigc-api.MtOZifzrpp_2\",\n" +
                "                \"uniqueWantItId\": \"4a59df063a604266bd704ad206d1e41c\",\n" +
                "                \"detail\": \"{\\\"exposeFields\\\":{\\\"productStage\\\":1,\\\"productImages\\\":[\\\"https://static.zaohaowu.com/jjewelry/web/resources/product/20250411/4a0bdd054ea54fddba4d1e5fbe4a285c/resource/81983cb2530f491f9afd28e76318230c/25d886059f398b77852d94a9d8a1fb9e/webp/25d886059f398b77852d94a9d8a1fb9e.webp\\\",\\\"https://static.zaohaowu.com/jjewelry/web/resources/product/20250411/0f75f86048564e5b86b40c2fae116ff5/resource/efdec3583ce645aebb5fe3128d4f2a1e/b9d99e2ead1027e700bc4ec9d8016a32/webp/b9d99e2ead1027e700bc4ec9d8016a32.webp\\\"],\\\"price\\\":39.9,\\\"title\\\":\\\"长白山键帽\\\"}}\",\n" +
                "                \"status\": 1,\n" +
                "                \"productionStatus\": 2,\n" +
                "                \"uniqueUser\": {\n" +
                "                    \"id\": \"b8c7f60162114780a3e51cc1ae6d7954\",\n" +
                "                    \"nickname\": \"AI摸鱼姐\",\n" +
                "                    \"avatar\": \"https://static.zaohaowu.com/jjewelry/web/images/avatar/6ad6169be9cb498f87cd5ad235be2b1d/4c63073849c3aefc2521c6765e84536f.webp\",\n" +
                "                    \"accountLevel\": \"officialQualityCreator\"\n" +
                "                },\n" +
                "                \"ticketCountUpdateTime\": \"2025-04-14 08:34:25\",\n" +
                "                \"modifiedTime\": \"2025-06-10 11:51:14\",\n" +
                "                \"createTime\": \"2025-04-11 00:36:44\"\n" +
                "            },\n" +
                "            {\n" +
                "                \"uniqueProductId\": \"68196c050e67489495b2e23339093a05\",\n" +
                "                \"uniqueContentId\": \"74c9893ab8c8410d81e8c30a59b69f82\",\n" +
                "                \"uniqueImageId\": \"c009d2414adf41ffa63935320586d781.jjewelry-aigc-api.7g6CQh8GrL_2\",\n" +
                "                \"uniqueWantItId\": \"5dab837ca59a4035b373690046087811\",\n" +
                "                \"detail\": \"{\\\"exposeFields\\\":{\\\"productStage\\\":1,\\\"productImages\\\":[\\\"https://static.zaohaowu.com/jjewelry/web/resources/product/20250411/ded0c0122ba745a891c655eb7766a14a/resource/4edf3bb26fcc4028a13a1b455633c656/e4b539dfb1865915d5b49bc9a75814d5/webp/e4b539dfb1865915d5b49bc9a75814d5.webp\\\",\\\"https://static.zaohaowu.com/jjewelry/web/resources/product/20250411/f0909686bb124752a193cd28c0a9e8a7/resource/005cb169377d44b19f69b9ee3c22b28c/f60c715bd4d5e9ccc8d62f86596d0580/webp/f60c715bd4d5e9ccc8d62f86596d0580.webp\\\"],\\\"price\\\":39.9,\\\"title\\\":\\\"日式漫画小猫键帽\\\"}}\",\n" +
                "                \"status\": 1,\n" +
                "                \"productionStatus\": 2,\n" +
                "                \"uniqueUser\": {\n" +
                "                    \"id\": \"ee8f83c674d24451b37ebc708afe64ad\",\n" +
                "                    \"nickname\": \"Caroline\",\n" +
                "                    \"avatar\": \"https://static.zaohaowu.com/jjewelry/web/images/avatar/cedb12849c08436da3f79619cbd7e133/9f75931b25e14c0a7fcac2883accaefe.webp\",\n" +
                "                    \"accountLevel\": \"officialQualityCreator\"\n" +
                "                },\n" +
                "                \"ticketCountUpdateTime\": \"2025-04-18 03:32:29\",\n" +
                "                \"modifiedTime\": \"2025-06-18 08:25:33\",\n" +
                "                \"createTime\": \"2025-04-11 01:45:39\"\n" +
                "            },\n" +
                "            {\n" +
                "                \"uniqueProductId\": \"e90df608a5154e47a1f421bad2c563b2\",\n" +
                "                \"uniqueContentId\": \"aeb4bccf9a5946c095bc2b172e7349ec\",\n" +
                "                \"uniqueImageId\": \"3478993754e54c35958375f9b8afa91c.jjewelry-aigc-api.7hdcWixTTP_2\",\n" +
                "                \"uniqueWantItId\": \"959010ee46cc47599dde960bef555527\",\n" +
                "                \"detail\": \"{\\\"exposeFields\\\":{\\\"productStage\\\":1,\\\"productImages\\\":[\\\"https://static.zaohaowu.com/jjewelry/web/resources/product/20250411/193dec15c47d401786fd55ba358441cb/resource/f43925a31c51495eb4463ac4f56cb30d/8d0c208a0ca17826b001b9467506c45f/webp/8d0c208a0ca17826b001b9467506c45f.webp\\\",\\\"https://static.zaohaowu.com/jjewelry/web/resources/product/20250411/e191e09e7e2d4fcf8bea04c553316baa/resource/bbf6989f562342118740f783fa5db3a2/2843b88fbd3ae2ae0ceb1732cc9e2f4c/webp/2843b88fbd3ae2ae0ceb1732cc9e2f4c.webp\\\"],\\\"price\\\":39.9,\\\"title\\\":\\\"粉蓝姐妹键帽\\\"}}\",\n" +
                "                \"status\": 1,\n" +
                "                \"productionStatus\": 2,\n" +
                "                \"uniqueUser\": {\n" +
                "                    \"id\": \"1cdc84f30e734fe2853b6679754c8b02\",\n" +
                "                    \"nickname\": \"凯凯文\",\n" +
                "                    \"avatar\": \"https://static.zaohaowu.com/jjewelry/web/images/avatar/f86b4c704dc74d88a0ad9c5976509966/7743745e7762916850550fec23531489.webp\",\n" +
                "                    \"accountLevel\": \"\"\n" +
                "                },\n" +
                "                \"ticketCountUpdateTime\": \"2025-06-17 14:29:27\",\n" +
                "                \"modifiedTime\": \"2025-06-18 08:25:01\",\n" +
                "                \"createTime\": \"2025-04-11 01:50:58\"\n" +
                "            },\n" +
                "            {\n" +
                "                \"uniqueProductId\": \"72e4f1dfa4d94cb8a398592111946c42\",\n" +
                "                \"uniqueContentId\": \"323100dafa3a402eb1ba0af0d4f3aed9\",\n" +
                "                \"uniqueImageId\": \"89dfad21bdb44f0b8da31ee308294a4a.jjewelry-aigc-api.w32z0Jnwyj_2\",\n" +
                "                \"uniqueWantItId\": \"03e239677b80443da9906956b1883b77\",\n" +
                "                \"detail\": \"{\\\"exposeFields\\\":{\\\"productStage\\\":1,\\\"productImages\\\":[\\\"https://static.zaohaowu.com/jjewelry/web/resources/product/20250411/103b4bd3d9a94e3bb87641e08453ed68/resource/8f981e06716242749acaea4461c07409/3bca00c07ad71ad023cb62bc3a916567/webp/3bca00c07ad71ad023cb62bc3a916567.webp\\\",\\\"https://static.zaohaowu.com/jjewelry/web/resources/product/20250411/1c6d0e497f5a43e7aafcd461fb2ae866/resource/370370ca308e470d8902fcd374bf9471/2320be738be8876c47870f080649228b/webp/2320be738be8876c47870f080649228b.webp\\\"],\\\"price\\\":39.9,\\\"title\\\":\\\"像素小鸡键帽\\\"}}\",\n" +
                "                \"status\": 1,\n" +
                "                \"productionStatus\": 2,\n" +
                "                \"uniqueUser\": {\n" +
                "                    \"id\": \"116ae7f2d7344f0494b62eae3273e717\",\n" +
                "                    \"nickname\": \"三水设计-可爱风版\",\n" +
                "                    \"avatar\": \"https://static.zaohaowu.com/jjewelry/web/images/avatar/0d13660f419048f5a146ceeb81620060/c94cc41495c4f376e3e5dce8696e8181.webp\",\n" +
                "                    \"accountLevel\": \"\"\n" +
                "                },\n" +
                "                \"ticketCountUpdateTime\": \"2025-04-11 02:12:22\",\n" +
                "                \"modifiedTime\": \"2025-06-10 11:46:24\",\n" +
                "                \"createTime\": \"2025-04-11 01:52:20\"\n" +
                "            },\n" +
                "            {\n" +
                "                \"uniqueProductId\": \"d2cf8177dd92441ca240740bcfdfc5ba\",\n" +
                "                \"uniqueContentId\": \"5ee7f88ed5974962b0ee2b975c6ddecc\",\n" +
                "                \"uniqueImageId\": \"7dcfebe526dc42a4a6fb22d3142b2cb2.jjewelry-aigc-api.NOd2bOmtbF_2\",\n" +
                "                \"uniqueWantItId\": \"37629090a59d4d1793057b4759541632\",\n" +
                "                \"detail\": \"{\\\"exposeFields\\\":{\\\"productStage\\\":1,\\\"productImages\\\":[\\\"https://static.zaohaowu.com/jjewelry/web/resources/product/20250415/2a783bc1009d475a938147132520bb1f/resource/22c6c34aad9f4ab49794650b97437502/7a94a9d1a34c00ea4ca180f75e6aaa01/webp/7a94a9d1a34c00ea4ca180f75e6aaa01.webp\\\",\\\"https://static.zaohaowu.com/jjewelry/web/resources/product/20250415/32a010fcae0b4feb81b18c05c797e1cb/resource/5a3332c6b2164beb9b1cf68ab0ceb153/3f85c81a524dc49abecd9dfbe879efcf/webp/3f85c81a524dc49abecd9dfbe879efcf.webp\\\"],\\\"price\\\":39.9,\\\"title\\\":\\\"比特币主题键帽\\\"}}\",\n" +
                "                \"status\": 1,\n" +
                "                \"productionStatus\": 2,\n" +
                "                \"uniqueUser\": {\n" +
                "                    \"id\": \"ac9e41138ff748f0af7a85e8fbc1908d\",\n" +
                "                    \"nickname\": \"陈一诺言\",\n" +
                "                    \"avatar\": \"https://static.zaohaowu.com/jjewelry/web/images/avatar/3a1c644ddd944201a4a6e1f23170bd4d/9da16fb36e5599c3e5790dafa42c2a4d.webp\",\n" +
                "                    \"accountLevel\": \"\"\n" +
                "                },\n" +
                "                \"ticketCountUpdateTime\": \"2025-04-15 03:14:52\",\n" +
                "                \"modifiedTime\": \"2025-06-10 11:45:43\",\n" +
                "                \"createTime\": \"2025-04-15 03:14:41\"\n" +
                "            },\n" +
                "            {\n" +
                "                \"uniqueProductId\": \"2865c35f8a3846dc8979f348cc738944\",\n" +
                "                \"uniqueContentId\": \"54075c551f2f4edb91f5670817cbb1c1\",\n" +
                "                \"uniqueImageId\": \"60ad304887314c26ba211dd8570da5b0.jjewelry-aigc-api.t2fr7BUmGs_3\",\n" +
                "                \"uniqueWantItId\": \"36707da46e1a4dc59290dde956683b98\",\n" +
                "                \"detail\": \"{\\\"exposeFields\\\":{\\\"productStage\\\":1,\\\"productImages\\\":[\\\"https://static.zaohaowu.com/jjewelry/web/resources/product/20250408/097df20b50d64c6b958026add8189cff/resource/28b46258c43645f686706ced7c1de229/2968531f85762156c19b55e9ead87980/webp/2968531f85762156c19b55e9ead87980.webp\\\",\\\"https://static.zaohaowu.com/jjewelry/web/resources/product/20250408/665a280674ac4adabd4b8ad2ca2c0888/resource/137a1e3c9dfc4474b3fe435d5b5c8504/a0e5d3c29c837af0df25958704f819d4/webp/a0e5d3c29c837af0df25958704f819d4.webp\\\"],\\\"price\\\":39.9,\\\"title\\\":\\\"金鱼键帽\\\"}}\",\n" +
                "                \"status\": 1,\n" +
                "                \"productionStatus\": 2,\n" +
                "                \"uniqueUser\": {\n" +
                "                    \"id\": \"450f3e16470e498692fffa6bbea112a9\",\n" +
                "                    \"nickname\": \"VictorYoung\",\n" +
                "                    \"avatar\": \"https://static.zaohaowu.com/jjewelry/web/images/avatar/4127a44fba6e4e48b2f714a6b3021184/7dd8355a951840103310afeb640a2427.webp\",\n" +
                "                    \"accountLevel\": \"\"\n" +
                "                },\n" +
                "                \"ticketCountUpdateTime\": \"2025-04-21 09:44:13\",\n" +
                "                \"modifiedTime\": \"2025-06-10 11:45:06\",\n" +
                "                \"createTime\": \"2025-04-08 20:30:17\"\n" +
                "            },\n" +
                "            {\n" +
                "                \"uniqueProductId\": \"c81b88b230454733a11a5b124af32d0a\",\n" +
                "                \"uniqueContentId\": \"d6ef150895564ab98f0c72bf59f58c2a\",\n" +
                "                \"uniqueImageId\": \"355fedd98e954cf2ac535d9ca4122bab.jjewelry-aigc-api.ojwRYSSSfC_2\",\n" +
                "                \"uniqueWantItId\": \"f521160491034f808b2f8c61aa4e85bf\",\n" +
                "                \"detail\": \"{\\\"exposeFields\\\":{\\\"productStage\\\":0,\\\"productImages\\\":[\\\"https://static.zaohaowu.com/jjewelry/web/resources/product/20250411/c1adf1941b1043c7818401238f023800/resource/145f315370fd4d54a71c44efe220983c/65132fd5f409813a788acbb3d0333348/webp/65132fd5f409813a788acbb3d0333348.webp\\\",\\\"https://static.zaohaowu.com/jjewelry/web/resources/product/20250411/4d2b0dd7af504d4ca74a1c24e15651af/resource/067607aac76642ef9cf282c7b98c5883/591be79d3fd47dbd4daa724300853594/webp/591be79d3fd47dbd4daa724300853594.webp\\\"],\\\"price\\\":39.9,\\\"title\\\":\\\"多汁桃桃键帽\\\"}}\",\n" +
                "                \"status\": 1,\n" +
                "                \"productionStatus\": 2,\n" +
                "                \"uniqueUser\": {\n" +
                "                    \"id\": \"ee8f83c674d24451b37ebc708afe64ad\",\n" +
                "                    \"nickname\": \"Caroline\",\n" +
                "                    \"avatar\": \"https://static.zaohaowu.com/jjewelry/web/images/avatar/cedb12849c08436da3f79619cbd7e133/9f75931b25e14c0a7fcac2883accaefe.webp\",\n" +
                "                    \"accountLevel\": \"officialQualityCreator\"\n" +
                "                },\n" +
                "                \"ticketCountUpdateTime\": \"2025-05-09 08:00:26\",\n" +
                "                \"modifiedTime\": \"2025-06-10 11:36:43\",\n" +
                "                \"createTime\": \"2025-04-11 01:48:53\"\n" +
                "            },\n" +
                "            {\n" +
                "                \"uniqueProductId\": \"350f94f9602546f79a6c425b91aca67d\",\n" +
                "                \"uniqueContentId\": \"2679a695313e43d3a2ac28d7ebbf4622\",\n" +
                "                \"uniqueImageId\": \"cf0f7cdfd22a40e98eba1f15239568a8.jjewelry-aigc-api.2njlCNsrJz_1\",\n" +
                "                \"uniqueWantItId\": \"2216315102564578ab978480cdbbc01e\",\n" +
                "                \"detail\": \"{\\\"exposeFields\\\":{\\\"productStage\\\":1,\\\"productImages\\\":[\\\"https://static.zaohaowu.com/jjewelry/web/resources/product/20250514/6d6bf0ac79544b9a8f692c0eacab4189/resource/dca36f40528f43d3ba6e7a6a5e2a3389/30edde4f1590cfdfb789a166deabe8e5/webp/30edde4f1590cfdfb789a166deabe8e5.webp\\\",\\\"https://static.zaohaowu.com/jjewelry/web/resources/product/20250514/3acd7b48e1454d8f8dc826957206864e/resource/593e6754086e4a7a950e29adecd33d51/4013c86af605e3e88f9f85218b9c690a/webp/4013c86af605e3e88f9f85218b9c690a.webp\\\"],\\\"price\\\":39.9,\\\"title\\\":\\\"梦幻猫咪键帽\\\"}}\",\n" +
                "                \"status\": 1,\n" +
                "                \"productionStatus\": 2,\n" +
                "                \"uniqueUser\": {\n" +
                "                    \"id\": \"fe62eb268d2c4590944ad44dff3f8829\",\n" +
                "                    \"nickname\": \"xiaoxiong\",\n" +
                "                    \"avatar\": \"https://static.zaohaowu.com/jjewelry/web/images/avatar/0ed5b14473ef46c088baf494b99130d3/3b8b9073d84d0db708b919bfb67c7b61.webp\",\n" +
                "                    \"accountLevel\": \"\"\n" +
                "                },\n" +
                "                \"ticketCountUpdateTime\": \"2025-03-31 20:18:58\",\n" +
                "                \"modifiedTime\": \"2025-06-10 11:29:59\",\n" +
                "                \"createTime\": \"2025-03-31 20:15:35\"\n" +
                "            },\n" +
                "            {\n" +
                "                \"uniqueProductId\": \"0e5f01bcb43647b0903aa1cdccd5f26c\",\n" +
                "                \"uniqueContentId\": \"21568ad0ac5b4ac6ac7a74db816d65d1\",\n" +
                "                \"uniqueImageId\": \"47807e58671a49d0a1d5d3380f3e22eb.jjewelry-aigc-api.4Ab5g8WcC5_3\",\n" +
                "                \"uniqueWantItId\": \"25a3f19101a34cf38fca732b10178650\",\n" +
                "                \"detail\": \"{\\\"exposeFields\\\":{\\\"productStage\\\":1,\\\"productImages\\\":[\\\"https://static.zaohaowu.com/jjewelry/web/resources/product/20250514/83821e5844c74f989bd588270f30bf84/resource/4f1ed45f5acb4abb9c574caf17ae1cc8/690d0f5f7f6e5894d3560321ab308ab2/webp/690d0f5f7f6e5894d3560321ab308ab2.webp\\\",\\\"https://static.zaohaowu.com/jjewelry/web/resources/product/20250514/bb21b09a60294f4bad31707e078ea6a5/resource/6571e6a63aa14eda96100a898f6f058a/cd36ed94b4f20e11d16848855415607e/webp/cd36ed94b4f20e11d16848855415607e.webp\\\"],\\\"price\\\":39.9,\\\"title\\\":\\\"十二生肖之狗键帽\\\"}}\",\n" +
                "                \"status\": 1,\n" +
                "                \"productionStatus\": 2,\n" +
                "                \"uniqueUser\": {\n" +
                "                    \"id\": \"ac9e41138ff748f0af7a85e8fbc1908d\",\n" +
                "                    \"nickname\": \"陈一诺言\",\n" +
                "                    \"avatar\": \"https://static.zaohaowu.com/jjewelry/web/images/avatar/3a1c644ddd944201a4a6e1f23170bd4d/9da16fb36e5599c3e5790dafa42c2a4d.webp\",\n" +
                "                    \"accountLevel\": \"\"\n" +
                "                },\n" +
                "                \"ticketCountUpdateTime\": \"2025-04-03 19:15:25\",\n" +
                "                \"modifiedTime\": \"2025-06-10 11:22:10\",\n" +
                "                \"createTime\": \"2025-04-03 19:04:57\"\n" +
                "            },\n" +
                "            {\n" +
                "                \"uniqueProductId\": \"3a9b2263e6014ad9bb19a91fa2f3c2d9\",\n" +
                "                \"uniqueContentId\": \"25513c4691594fc483e4fa039a7624b1\",\n" +
                "                \"uniqueImageId\": \"8fae3c4578a04864874de42919395d77.jjewelry-aigc-api.rgA6iU94yW_2\",\n" +
                "                \"uniqueWantItId\": \"1165acc6629f4f74be8a0519be25063c\",\n" +
                "                \"detail\": \"{\\\"exposeFields\\\":{\\\"productStage\\\":1,\\\"productImages\\\":[\\\"https://static.zaohaowu.com/jjewelry/web/resources/product/20250513/5305be203960413eb2c876335b7d00b1/resource/cf0cde721fd24954863326527ec4508d/c40d145ae5281787fcb45d20891ef1da/webp/c40d145ae5281787fcb45d20891ef1da.webp\\\",\\\"https://static.zaohaowu.com/jjewelry/web/resources/product/20250513/a104c0e7c6e649ca8c97e6cf7435f0f1/resource/035d9f4d38f64c9a91af3e4bf6045b68/e24025ee3360bbd25e5f1455a4822e27/webp/e24025ee3360bbd25e5f1455a4822e27.webp\\\"],\\\"price\\\":39.9,\\\"title\\\":\\\"充满质感的蛋挞键帽\\\"}}\",\n" +
                "                \"status\": 1,\n" +
                "                \"productionStatus\": 2,\n" +
                "                \"uniqueUser\": {\n" +
                "                    \"id\": \"e87a9f8ed7084eafae2d3b485e9b620a\",\n" +
                "                    \"nickname\": \"Andy\",\n" +
                "                    \"avatar\": \"https://static.zaohaowu.com/jjewelry/web/images/avatar/cd72d8cc3d8647bcbfd3e6f3020e8cca/a9e84520dc12636069eada356c350434.webp\",\n" +
                "                    \"accountLevel\": \"\"\n" +
                "                },\n" +
                "                \"ticketCountUpdateTime\": \"2025-04-11 07:36:56\",\n" +
                "                \"modifiedTime\": \"2025-06-10 11:21:25\",\n" +
                "                \"createTime\": \"2025-04-11 01:49:49\"\n" +
                "            },\n" +
                "            {\n" +
                "                \"uniqueProductId\": \"3c69288daaed431bae1aecd0b66ddfe4\",\n" +
                "                \"uniqueContentId\": \"5784d9feea52488ab44acfa2d41230d4\",\n" +
                "                \"uniqueImageId\": \"f853550c5b704cf2b5214899b34208b3.jjewelry-aigc-api.KPxuX2ZLvO_0\",\n" +
                "                \"uniqueWantItId\": \"a2beb9b92faa4f79a8a2b90700a9682d\",\n" +
                "                \"detail\": \"{\\\"exposeFields\\\":{\\\"productStage\\\":1,\\\"productImages\\\":[\\\"https://static.zaohaowu.com/jjewelry/web/resources/product/20250513/0a9cfb90b81a4180a2ded9ad212f297d/resource/461309bdb0d540dc85a255f339c3aaf2/0168f394fa192541099df8419edf1fa1/webp/0168f394fa192541099df8419edf1fa1.webp\\\",\\\"https://static.zaohaowu.com/jjewelry/web/resources/product/20250513/61570e0fb88a4d6998df84ce18b75c97/resource/390224f5a0af425f9b237bfcb3908f17/a15562d3ece016de56f4d7eff513d52b/webp/a15562d3ece016de56f4d7eff513d52b.webp\\\"],\\\"price\\\":39.9,\\\"title\\\":\\\"萌趣煎蛋键帽\\\"}}\",\n" +
                "                \"status\": 1,\n" +
                "                \"productionStatus\": 2,\n" +
                "                \"uniqueUser\": {\n" +
                "                    \"id\": \"a5197859ea2c43e88468cd488a65b3b4\",\n" +
                "                    \"nickname\": \"Aq\",\n" +
                "                    \"avatar\": \"https://static.zaohaowu.com/jjewelry/web/images/avatar/user-HMQoCE-2.png\",\n" +
                "                    \"accountLevel\": \"\"\n" +
                "                },\n" +
                "                \"ticketCountUpdateTime\": \"2025-04-04 07:23:57\",\n" +
                "                \"modifiedTime\": \"2025-05-23 09:49:36\",\n" +
                "                \"createTime\": \"2025-04-03 19:11:18\"\n" +
                "            },\n" +
                "            {\n" +
                "                \"uniqueProductId\": \"2890b8a74476480690601bdc31783ae1\",\n" +
                "                \"uniqueContentId\": \"659aa84100e24253a72a0226da78eba7\",\n" +
                "                \"uniqueImageId\": \"3fd90cb5f24f4814a145a1451f6e3ab1.jjewelry-aigc-api.kWhAUA4G96_2\",\n" +
                "                \"uniqueWantItId\": \"8f964e6c0bdc42748c38bb3d97c5bdba\",\n" +
                "                \"detail\": \"{\\\"exposeFields\\\":{\\\"productStage\\\":1,\\\"productImages\\\":[\\\"https://static.zaohaowu.com/jjewelry/web/resources/product/20250513/3434508e8a6548aeab4a1caf3dc19582/resource/23f926879ac9489882ed92134eb6a98d/b93f7ad30b63e5b7a45db0cecb3504a6/webp/b93f7ad30b63e5b7a45db0cecb3504a6.webp\\\",\\\"https://static.zaohaowu.com/jjewelry/web/resources/product/20250513/fb2352711f1e4a0482c9addc01d96604/resource/8e4619cb11294eeab86849a2539ab92b/9acd1d217575717ed643599f26e53281/webp/9acd1d217575717ed643599f26e53281.webp\\\"],\\\"price\\\":39.9,\\\"title\\\":\\\"完美咖啡键帽\\\"}}\",\n" +
                "                \"status\": 1,\n" +
                "                \"productionStatus\": 2,\n" +
                "                \"uniqueUser\": {\n" +
                "                    \"id\": \"fb6e640709ff4d1ea8f29cbaf4343d62\",\n" +
                "                    \"nickname\": \"星谕天工\",\n" +
                "                    \"avatar\": \"https://static.zaohaowu.com/jjewelry/web/images/avatar/3128f6c2021c44e594149970da141dbf/f02a78bf70fc01390f11717d3bb76e22.webp\",\n" +
                "                    \"accountLevel\": \"\"\n" +
                "                },\n" +
                "                \"ticketCountUpdateTime\": \"2025-03-31 20:18:52\",\n" +
                "                \"modifiedTime\": \"2025-06-10 11:20:40\",\n" +
                "                \"createTime\": \"2025-03-31 20:17:44\"\n" +
                "            },\n" +
                "            {\n" +
                "                \"uniqueProductId\": \"1ff61ccb8533411383cb5be81de70d46\",\n" +
                "                \"uniqueContentId\": \"94de8d2d970348b8a1f28f6bee4f4da2\",\n" +
                "                \"uniqueImageId\": \"00b7c92888584cb3b6fc57ddbb54a3e5.jjewelry-aigc-api.HljkF5vpcs_1\",\n" +
                "                \"uniqueWantItId\": \"1d196c5c976d4ad7b6ee28aaec2c96f9\",\n" +
                "                \"detail\": \"{\\\"exposeFields\\\":{\\\"productStage\\\":1,\\\"productImages\\\":[\\\"https://static.zaohaowu.com/jjewelry/web/resources/product/20250513/8644da05909e4a89bf544c927f606956/resource/7fba91afafb14476bbfcd849d355785a/2d8abefff06dc23d7a341fde85ba8ab6/webp/2d8abefff06dc23d7a341fde85ba8ab6.webp\\\",\\\"https://static.zaohaowu.com/jjewelry/web/resources/product/20250513/f1346dcdc97e44f4a77e15e713a6415b/resource/b7d9a422102748de86dec9b0039f2dcc/49de45ffc2913c816fbd747099322a56/webp/49de45ffc2913c816fbd747099322a56.webp\\\"],\\\"price\\\":39.9,\\\"title\\\":\\\"麻婆豆腐键帽\\\"}}\",\n" +
                "                \"status\": 1,\n" +
                "                \"productionStatus\": 2,\n" +
                "                \"uniqueUser\": {\n" +
                "                    \"id\": \"bbb1ae4389634dd89e515c0f0b058d79\",\n" +
                "                    \"nickname\": \"别点进来\",\n" +
                "                    \"avatar\": \"https://static.zaohaowu.com/jjewelry/web/images/avatar/a97c32fa39514af99c5b1fd85ec08dba/a775f130f415155ffa871df4097f263f.webp\",\n" +
                "                    \"accountLevel\": \"officialQualityCreator\"\n" +
                "                },\n" +
                "                \"ticketCountUpdateTime\": \"2025-04-11 07:54:45\",\n" +
                "                \"modifiedTime\": \"2025-06-10 11:19:47\",\n" +
                "                \"createTime\": \"2025-04-11 01:55:05\"\n" +
                "            },\n" +
                "            {\n" +
                "                \"uniqueProductId\": \"62c917aa083d4bd384c6f4f4f7461a80\",\n" +
                "                \"uniqueContentId\": \"6c447114e0ff411b8049b4ac49c1a4d7\",\n" +
                "                \"uniqueImageId\": \"ce4401e8bb2a42e796b002c0b9398484.jjewelry-aigc-api.7KjnQWN0PH_1\",\n" +
                "                \"uniqueWantItId\": \"b9c3dd2ce024445fba09ef8f7cd88444\",\n" +
                "                \"detail\": \"{\\\"exposeFields\\\":{\\\"productStage\\\":1,\\\"productImages\\\":[\\\"https://static.zaohaowu.com/jjewelry/web/resources/product/20250515/38197bac06ac4e2f8704c7bbb2f528ac/resource/5dd14ba519b34125bdd9dc40d000caf6/e0d233c410f1554469b7b1d9d62d8191/webp/e0d233c410f1554469b7b1d9d62d8191.webp\\\",\\\"https://static.zaohaowu.com/jjewelry/web/resources/product/20250515/34d467bee3154a5091d10ee759c1a9e5/resource/df92cf26595b455eaa7291c1c9a242c7/1bd22c4d3741eca9307022880ad8b94a/webp/1bd22c4d3741eca9307022880ad8b94a.webp\\\"],\\\"price\\\":39.9,\\\"title\\\":\\\"梦幻鱼缸键帽\\\"}}\",\n" +
                "                \"status\": 1,\n" +
                "                \"productionStatus\": 2,\n" +
                "                \"uniqueUser\": {\n" +
                "                    \"id\": \"14fed238cb784ca08213a8c6afe92876\",\n" +
                "                    \"nickname\": \"looooo\",\n" +
                "                    \"avatar\": \"https://static.zaohaowu.com/jjewelry/web/images/avatar/5d536bbcf17a4f519c6d85cc87f09bdc/ed02bfdd871339662179d660bf1deaa2.webp\",\n" +
                "                    \"accountLevel\": \"\"\n" +
                "                },\n" +
                "                \"ticketCountUpdateTime\": \"2025-03-27 07:35:21\",\n" +
                "                \"modifiedTime\": \"2025-06-10 11:18:43\",\n" +
                "                \"createTime\": \"2025-03-27 07:30:24\"\n" +
                "            },\n" +
                "            {\n" +
                "                \"uniqueProductId\": \"a56397ce5346420e8bc9c7e57d04baa0\",\n" +
                "                \"uniqueContentId\": \"ae691c8c1221431dbeff14e751a215c8\",\n" +
                "                \"uniqueImageId\": \"a910fc5274fe486e96f129628b7e8d49.jjewelry-aigc-api.YdbmWvN3wT_2\",\n" +
                "                \"uniqueWantItId\": \"f2cd3098dce64aeaae8709bbfbad1e6e\",\n" +
                "                \"detail\": \"{\\\"exposeFields\\\":{\\\"productStage\\\":1,\\\"productImages\\\":[\\\"https://static.zaohaowu.com/jjewelry/web/resources/product/20250509/d295a59722f64da09608aec02dd2278a/resource/bc1e169471254b94b45c03d203baee4b/cc0f7d3af6dcf27e0ea4928b0d946fe7/webp/cc0f7d3af6dcf27e0ea4928b0d946fe7.webp\\\",\\\"https://static.zaohaowu.com/jjewelry/web/resources/product/20250509/89b0ccff575d4be7af0c5ffbcb3deacf/resource/b3af21396f574985819ff9e122ba379d/d109bf5bd14b82cb1eb0914236a53bb3/webp/d109bf5bd14b82cb1eb0914236a53bb3.webp\\\"],\\\"price\\\":39.9,\\\"title\\\":\\\"绒毛狐狸键帽\\\"}}\",\n" +
                "                \"status\": 1,\n" +
                "                \"productionStatus\": 2,\n" +
                "                \"uniqueUser\": {\n" +
                "                    \"id\": \"3af6010149b448bc80b8158f43f67734\",\n" +
                "                    \"nickname\": \"造物梦工厂\",\n" +
                "                    \"avatar\": \"https://static.zaohaowu.com/jjewelry/web/images/avatar/82a29dc156d04092bc1349077bb42c21/5f2cf656f962cb3a15fba3ee72b59a33.webp\",\n" +
                "                    \"accountLevel\": \"\"\n" +
                "                },\n" +
                "                \"ticketCountUpdateTime\": \"2025-04-04 09:30:44\",\n" +
                "                \"modifiedTime\": \"2025-06-10 11:18:09\",\n" +
                "                \"createTime\": \"2025-04-03 19:07:27\"\n" +
                "            },\n" +
                "            {\n" +
                "                \"uniqueProductId\": \"811ff1b805b7489185adfc0c2ab6bd32\",\n" +
                "                \"uniqueContentId\": \"42a9c777c2e6450a9b3609495c6a236a\",\n" +
                "                \"uniqueImageId\": \"c1b0af9eea914c109b23c72eba15f191.jjewelry-aigc-api.MmXDYg6nn9_3\",\n" +
                "                \"uniqueWantItId\": \"98aff60d96e247e6a3bd0c97664bf4a5\",\n" +
                "                \"detail\": \"{\\\"exposeFields\\\":{\\\"productStage\\\":1,\\\"productImages\\\":[\\\"https://static.zaohaowu.com/jjewelry/web/resources/product/20250515/a8e4b338b4544c71a2d8370e1b32e0ac/resource/2316f0bb9fa54106a4cf5dd1d02c7abc/5a2fe0acdd4172c5a9938bfa8b7e7eaf/webp/5a2fe0acdd4172c5a9938bfa8b7e7eaf.webp\\\",\\\"https://static.zaohaowu.com/jjewelry/web/resources/product/20250515/449ba9f80ad745a29d625dfaac9e0795/resource/f4d973b2d6aa491c992f1fca6641b7f1/df461a8d8b46f3ebf6e29e1e524150c8/webp/df461a8d8b46f3ebf6e29e1e524150c8.webp\\\"],\\\"price\\\":39.9,\\\"title\\\":\\\"安睡大橘键帽\\\"}}\",\n" +
                "                \"status\": 1,\n" +
                "                \"productionStatus\": 2,\n" +
                "                \"uniqueUser\": {\n" +
                "                    \"id\": \"1ded52d3635c4528b16f2608bfddf4b5\",\n" +
                "                    \"nickname\": \"YS\",\n" +
                "                    \"avatar\": \"https://static.zaohaowu.com/jjewelry/web/images/avatar/56d24a1522e1471389a115325cd0429f/eb8b5bdb462a95fd49ce0d60484b0f9a.webp\",\n" +
                "                    \"accountLevel\": \"\"\n" +
                "                },\n" +
                "                \"ticketCountUpdateTime\": \"2025-03-31 20:19:01\",\n" +
                "                \"modifiedTime\": \"2025-06-10 11:17:16\",\n" +
                "                \"createTime\": \"2025-03-31 20:13:16\"\n" +
                "            },\n" +
                "            {\n" +
                "                \"uniqueProductId\": \"d0b8101af4d44ed9b35f5641cda52d97\",\n" +
                "                \"uniqueContentId\": \"88e081c3dc1e423c91d33edb8ebf6f7b\",\n" +
                "                \"uniqueImageId\": \"9aab47cbe7574919a7ce7989901c43d7.jjewelry-aigc-api.chA5vs0xfc_2\",\n" +
                "                \"uniqueWantItId\": \"dd7e7144ac064a87a518b0186f9764f2\",\n" +
                "                \"detail\": \"{\\\"exposeFields\\\":{\\\"productStage\\\":1,\\\"productImages\\\":[\\\"https://static.zaohaowu.com/jjewelry/web/resources/product/20250515/d52d631351044184b13b9b1a8a42ff0d/resource/7bc1fad1e5b64d2cbbaf837a5023e6a9/1c0ad444afa3662f83ae05ad0b9ab144/webp/1c0ad444afa3662f83ae05ad0b9ab144.webp\\\",\\\"https://static.zaohaowu.com/jjewelry/web/resources/product/20250515/6bcc7f444128400c93e991f680b020cf/resource/6596dac1cbf34231a0a30d9a4209a41d/39b69b57f85d72c6fb76cccc599ff2b1/webp/39b69b57f85d72c6fb76cccc599ff2b1.webp\\\"],\\\"price\\\":39.9,\\\"title\\\":\\\"梦幻紫蝶键帽\\\"}}\",\n" +
                "                \"status\": 1,\n" +
                "                \"productionStatus\": 2,\n" +
                "                \"uniqueUser\": {\n" +
                "                    \"id\": \"0ca1b2a369d04fd1a23ea95cd5a28dc2\",\n" +
                "                    \"nickname\": \"爱玩的怂怂\",\n" +
                "                    \"avatar\": \"https://static.zaohaowu.com/jjewelry/web/images/avatar/a4fe795495474cfba8a56c9fa2016df3/78e6e711e31de514f1df68a161c713d9.webp\",\n" +
                "                    \"accountLevel\": \"\"\n" +
                "                },\n" +
                "                \"ticketCountUpdateTime\": \"2025-03-27 12:36:09\",\n" +
                "                \"modifiedTime\": \"2025-06-10 11:16:45\",\n" +
                "                \"createTime\": \"2025-03-27 12:36:02\"\n" +
                "            },\n" +
                "            {\n" +
                "                \"uniqueProductId\": \"2917ba6bc97945e6ade9c5d674b58b77\",\n" +
                "                \"uniqueContentId\": \"a6190ddeb5784544936169bbb760c35f\",\n" +
                "                \"uniqueImageId\": \"f54a212fadd54f6bbdfd18b9632419bc.jjewelry-aigc-api.echFFgVs4w_1\",\n" +
                "                \"detail\": \"{\\\"exposeFields\\\":{\\\"productStage\\\":1,\\\"productImages\\\":[\\\"https://static.zaohaowu.com/jjewelry/web/resources/product/20250515/68b9ba3f6d924762a7a78869405d1cff/resource/c70defb52a6946ddabe46df3f28efba4/de4d30ff427ac140388c6c8062829ba9/webp/de4d30ff427ac140388c6c8062829ba9.webp\\\",\\\"https://static.zaohaowu.com/jjewelry/web/resources/product/20250515/ba1fd949327046bda5ca11da70ca547a/resource/3bf4f2ece3894cc19068466363128521/7100d07a8726a1525d2c85f826056462/webp/7100d07a8726a1525d2c85f826056462.webp\\\"],\\\"price\\\":69,\\\"title\\\":\\\"春日草地毛毡键帽\\\"}}\",\n" +
                "                \"status\": 1,\n" +
                "                \"productionStatus\": 2,\n" +
                "                \"uniqueUser\": {\n" +
                "                    \"id\": \"2efb12c7e74c47ebaa96fc1ee12d403c\",\n" +
                "                    \"nickname\": \"MORIYO\",\n" +
                "                    \"avatar\": \"https://static.zaohaowu.com/jjewelry/web/images/avatar/a0e0a4431d3b4a8fb306cbeac5171f83/052d7658e306c68ef4c84541c235883d.webp\",\n" +
                "                    \"accountLevel\": \"\"\n" +
                "                },\n" +
                "                \"modifiedTime\": \"2025-06-10 11:16:07\",\n" +
                "                \"createTime\": \"2025-02-25 07:04:37\"\n" +
                "            },\n" +
                "            {\n" +
                "                \"uniqueProductId\": \"7b06bf1476de4583a473dd8be283003c\",\n" +
                "                \"uniqueContentId\": \"bb8d147c8dc34d4e8529d593df64280c\",\n" +
                "                \"uniqueImageId\": \"fb262469f8d74dc4852e009aaa99f296.jjewelry-aigc-api.fTmAjzaRD0_2\",\n" +
                "                \"detail\": \"{\\\"exposeFields\\\":{\\\"productStage\\\":2,\\\"productImages\\\":[\\\"https://static.zaohaowu.com/jjewelry/web/resources/product/20250515/f87888c06dea4b78a561634cd7a2e999/resource/b21c7275ef6e4a2082a2fa00f31ddd58/bc12931f42cf5e9b47503d5e50e1240a/webp/bc12931f42cf5e9b47503d5e50e1240a.webp\\\",\\\"https://static.zaohaowu.com/jjewelry/web/resources/product/20250515/8846dc2f71f54d5e9ea7578472f766b3/resource/9539a76aae9940fbacb653405b5ca5f3/8fdba18bff9852dbe2460cb3addfb39f/webp/8fdba18bff9852dbe2460cb3addfb39f.webp\\\"],\\\"price\\\":59,\\\"title\\\":\\\"Naori键帽\\\"}}\",\n" +
                "                \"status\": 1,\n" +
                "                \"productionStatus\": 2,\n" +
                "                \"uniqueUser\": {\n" +
                "                    \"id\": \"14fed238cb784ca08213a8c6afe92876\",\n" +
                "                    \"nickname\": \"looooo\",\n" +
                "                    \"avatar\": \"https://static.zaohaowu.com/jjewelry/web/images/avatar/5d536bbcf17a4f519c6d85cc87f09bdc/ed02bfdd871339662179d660bf1deaa2.webp\",\n" +
                "                    \"accountLevel\": \"\"\n" +
                "                },\n" +
                "                \"modifiedTime\": \"2025-05-23 08:58:11\",\n" +
                "                \"createTime\": \"2025-03-25 02:41:30\"\n" +
                "            },\n" +
                "            {\n" +
                "                \"uniqueProductId\": \"b60215f7f1294a4e9cf3b59e3dccedde\",\n" +
                "                \"uniqueContentId\": \"d30ce6f7d67442089b77ceb7092e1060\",\n" +
                "                \"uniqueImageId\": \"1cf89a3904ce407888e3ce36851a768e.jjewelry-aigc-api.0nKiHXxLiO_1\",\n" +
                "                \"uniqueWantItId\": \"feb52daaf70f4bdeabbb99ce7c6cf855\",\n" +
                "                \"detail\": \"{\\\"exposeFields\\\":{\\\"productStage\\\":1,\\\"productImages\\\":[\\\"https://static.zaohaowu.com/jjewelry/web/resources/product/20250515/e4a888e4cb444e88bd13336bd793bd36/resource/05ab2d8266f64808b7aee5fc78dca600/0a0e6e770e26bcffdbb1a6ca465cc87c/webp/0a0e6e770e26bcffdbb1a6ca465cc87c.webp\\\",\\\"https://static.zaohaowu.com/jjewelry/web/resources/product/20250515/cb506bc3eba946e38b888b85bf85d711/resource/e0f3e1769e90440f81ad225a2e57c1a6/b0e745c7bc71484dc74fa1aeb6e43ec9/webp/b0e745c7bc71484dc74fa1aeb6e43ec9.webp\\\"],\\\"price\\\":39.9,\\\"title\\\":\\\"哥特白虎键帽\\\"}}\",\n" +
                "                \"status\": 1,\n" +
                "                \"productionStatus\": 2,\n" +
                "                \"uniqueUser\": {\n" +
                "                    \"id\": \"07dd996c8ad04813b4b81cb2ae57d192\",\n" +
                "                    \"nickname\": \"Mars造物\",\n" +
                "                    \"avatar\": \"https://static.zaohaowu.com/jjewelry/web/images/avatar/f5cef63899f44617aea6bde988f66eed/683558052218c4bf9f43c8ce0421eaae.webp\",\n" +
                "                    \"accountLevel\": \"\"\n" +
                "                },\n" +
                "                \"ticketCountUpdateTime\": \"2025-04-01 17:08:21\",\n" +
                "                \"modifiedTime\": \"2025-05-23 08:39:14\",\n" +
                "                \"createTime\": \"2025-04-01 17:07:03\"\n" +
                "            }\n" +
                "        ]\n" +
                "    }\n" +
                "}";
        JSONObject jsonObject = JSONObject.parseObject(json, JSONObject.class);
        JSONArray dataList = (JSONArray) ((JSONObject)jsonObject.get("data")).get("dataList");

        List<String> productIds = new ArrayList<>();
        for (int i = 0; i < dataList.size(); i++) {
            productIds.add(dataList.getJSONObject(i).getString("uniqueProductId"));
        }

        productIds.stream()
                .sorted()
                .forEach(p -> {
                    System.out.println("'" + p + "',");
                });
    }
    public static void main(String[] args) {
        String str = "{\"coupon\":\"{\\\"endTime\\\":\\\"2018-04-24 23:59:59\\\",\\\"quota\\\":100,\\\"discount\\\":99,\\\"startTime\\\":\\\"2018-04-21 00:00:00\\\"}\",\"points\":12,\"sendType\":2}";
        Ext ext = JSON.parseObject(str, Ext.class);
        System.out.println(new JSONTest().getClass().getSimpleName());
    }

    @Test
    public void testNumberSerializeDefaultValue() throws JsonProcessingException {
        Map<String, Object> dataMap = new HashMap<>(2);
        dataMap.put("aInteger", 1);
        dataMap.put("aLong", 2L);
        String jsonStr = JSON.toJSONString(dataMap);
        System.out.println(jsonStr);


        // fastjson
        System.out.println("--- fastjson -----");
        Map<String, Object> fastMap = JSON.parseObject(jsonStr, new com.alibaba.fastjson.TypeReference<Map<String, Object>>() {
        });
        printMap(fastMap);

        System.out.println("--- gson -----");
        Map<String, Object> gsonMap = new GsonBuilder().create()
                .fromJson(jsonStr, (new TypeReference<Map<String, Object>>(){}).getType() );
        printMap(gsonMap);

        System.out.println("--- jackson -----");
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, Object> jacksonMap = objectMapper.readValue(jsonStr, new TypeReference<Map<String, Object>>() {
        });
        printMap((jacksonMap));
    }

    private static void printMap(Map<String, Object> map) {
        map.forEach((key, value) -> {
            System.out.println("key:" + key + ",value=" + value + ",valueClass=" + value.getClass());
        });
    }

    @Test
    public void testParse() {
        String s = "{\"supplierId\":\"ves0002730\",\"pin\":\"prom_shop\",\"rebateInfo\":\"{ \"supplierId \": \"ves0002730 \", \"supportPercent \": \"100 \", \"fileInfoList \":[{ \"fileFolderName \": \"promo-rebate \", \"fileName \": \"1540799137951_d15d6e0c0ed9469d99963d3baea9d748.xlsx \"}]}\",\"ip\":\"127.0.0.1\"}\n";
        JSON.parseObject(s);
    }

    @Test
    public void testJson3() {
        String str = "{\"happened_time\":\"2018-06-09\", \"method\":\"title213\", \"time\":\"12312\", \"time_unit\":\"milliseconds\"}";
        JSONObject object = JSON.parseObject(str);

        System.out.println(object.get("happened_time"));
        System.out.println(object.get("method"));
        System.out.println(object.get("time"));
        System.out.println(object.get("time_unit"));
    }

    @Test
    public void testJson2() {
        JSONObject jsonObject = JSON.parseObject("");
        Map result = new HashMap();

        for (Map.Entry<String, Object> entry : jsonObject.entrySet()) {
            result.put(entry.getKey(), entry.getValue());
        }
    }

    @Test
    public void testJson() {
        JSONObject obj = new JSONObject();
        obj.put("code", "200");
        Person p = new Person();
        AAA a = new AAA();
        a.setAge(12);
        a.setName("hanyu");
        obj.put("aaa", a);
        System.out.println(obj.toString());
    }

    @Test
    public void testJSONRegx() {
        String regx = "(^[1-9]\\d{5}(18|19|([23]\\d))\\d{2}((0[1-9])|(10|11|12))(([0-2][1-9])|10|20|30|31)\\d{3}[0-9Xx]$)|(^[1-9]\\d{5}\\d{2}((0[1-9])|(10|11|12))(([0-2][1-9])|10|20|30|31)\\d{2}$)";
        Pattern p = Pattern.compile(regx);
        System.out.println(p.matcher("360424199506232155").matches());
    }
}

enum BusinessTypeEnum {
    AUCTION,//拍卖
    SELFSHOP,//自营店铺
    RED,//闪购
}

class AAA {
    private Integer age;

    private String name;

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}