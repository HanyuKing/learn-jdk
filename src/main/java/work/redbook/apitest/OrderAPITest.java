package work.redbook.apitest;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.xiaohongshu.fls.opensdk.client.DataClient;
import com.xiaohongshu.fls.opensdk.client.MaterialClient;
import com.xiaohongshu.fls.opensdk.client.OauthClient;
import com.xiaohongshu.fls.opensdk.client.OrderClient;
import com.xiaohongshu.fls.opensdk.entity.BaseResponse;
import com.xiaohongshu.fls.opensdk.entity.data.request.BatchDecryptRequest;
import com.xiaohongshu.fls.opensdk.entity.data.response.BatchDecryptResponse;
import com.xiaohongshu.fls.opensdk.entity.material.MaterialType;
import com.xiaohongshu.fls.opensdk.entity.material.request.UploadMaterialInfoRequest;
import com.xiaohongshu.fls.opensdk.entity.material.response.MaterialDetail;
import com.xiaohongshu.fls.opensdk.entity.oauth.request.GetAccessTokenRequest;
import com.xiaohongshu.fls.opensdk.entity.oauth.request.RefreshTokenRequest;
import com.xiaohongshu.fls.opensdk.entity.oauth.response.GetAccessTokenResponse;
import com.xiaohongshu.fls.opensdk.entity.oauth.response.RefreshTokenResponse;
import com.xiaohongshu.fls.opensdk.entity.order.Requset.GetOrderDetailRequest;
import com.xiaohongshu.fls.opensdk.entity.order.Requset.GetOrderListRequest;
import com.xiaohongshu.fls.opensdk.entity.order.Requset.GetOrderReceiverInfoRequest;
import com.xiaohongshu.fls.opensdk.entity.order.Response.GetOrderDetailResponse;
import com.xiaohongshu.fls.opensdk.entity.order.Response.GetOrderListResponse;
import com.xiaohongshu.fls.opensdk.entity.order.Response.GetOrderReceiverInfoResponse;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * @author hanyu.wang
 * @version 1.0
 * @date 2025/12/25
 */
public class OrderAPITest {
    private String appId = "ae807376fea64bbe9335";
    private String version = "2.0";
    private String appSecre = "7f86dcecb3237a5502ae51eff5a232bb";
    String code = "code-d92e34bac40f46818092a0d13e28be72-0dc69dfa92844c2897f08f95d9e00d06";
    private String accessToken = "token-68645d3e35a045bbaaf0cdf4d8a2b1cf-5cf61e19e2ce46a7a2023c77a1938529";

    private MaterialClient materialClient = new MaterialClient("https://ark.xiaohongshu.com/ark/open_api/v3/common_controller", appId, version, appSecre);

    @Test
    public void testUploadImage() throws Exception {
        String imageUrl = "https://prestatic.zaohaowu.com/jjewelry/web/resources/trade/product/20250910/f595db4dacaa44fb81b9df18b4be21bb/resource/f5e492f55adf42d49231d7aef553efd8/bb1ddaae6fde658a689e2767bbcb930e/original/bb1ddaae6fde658a689e2767bbcb930e.jpg";
        
        // 下载图片并转换为字节数组
        byte[] imageBytes = downloadImageFromUrl(imageUrl);
        
        UploadMaterialInfoRequest request = new UploadMaterialInfoRequest();
        request.setName("测试1");
        request.setType(MaterialType.IMAGE);
        request.setMaterialContent(imageBytes);
        
        BaseResponse<MaterialDetail> response = materialClient.execute(request, this.accessToken);
        System.out.println(JSON.toJSONString(response));
    }

    /**
     * 从 URL 下载图片并转换为字节数组
     */
    private byte[] downloadImageFromUrl(String imageUrl) throws IOException {
        URL url = new URL(imageUrl);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setConnectTimeout(10000);
        conn.setReadTimeout(10000);
        
        int responseCode = conn.getResponseCode();
        if (responseCode != HttpURLConnection.HTTP_OK) {
            throw new IOException("下载图片失败，HTTP状态码: " + responseCode);
        }
        
        try (InputStream inputStream = conn.getInputStream();
             ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            
            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
            
            return outputStream.toByteArray();
        } finally {
            conn.disconnect();
        }
    }

    /**
     * {
     *   "data": {
     *     "receiverInfos": [
     *       {
     *         "matched": true,
     *         "orderId": "P782011893460274131",
     *         "receiverAddress": "#Du12G8dm0Clc7JiDumrEzBB2HEi6dh0CWxG+5e+KyCzwaZNtR/VQ3HqB7/0JvhhgvRV73NBr5EilJSCEstn0duOlV6OhBiLkYb992xEfMG0bhI80ngnNSSDp44J3/PS+#etvbna6OmfzhTR2Xt5KCRqBqrLOi3jJHXkRO/xTg0gmRG3NY7DDT7REay+alfnq+1jEGeJ5ZwoC2jXn1WM95KfXaCTdQAzvxwAZvoCFg8bZeQSKl5CbJ3QlwnA7bAD+syxo6mp6MT1Ru+SimeLeCvYyyokHvqGWALZ2qjzrgvthMeRjOrgj7Uo75Dp/007npwuLMl71+J25qEwo7VCsX/goQYdTP3EUUighWs+VKtY00emIjHbkSipOaVKBPH4QA#1##",
     *         "receiverCityName": "长沙市",
     *         "receiverDistrictName": "岳麓区",
     *         "receiverName": "#Du12G8dm0Clc7JiDumrEzO9F1I7DUVOvJ8tmQWofREs=#etvbna6OmfzhTR2Xt5KCRqBqrLOi3jJHXkRO/xTg0gm8ECllfj53Mq0YaP4kQVFiy0HvHtjbmISM/3CsrRv54A3cP94DNhsZODKRVLApGCOHCM5E9NKd0naaEWOd3PVGwNGtrAg2wBv13K/0+Wo4ArOU03CaKVTGC0JSCckiBcs=#2##",
     *         "receiverPhone": "#Du12G8dm0Clc7JiDumrEzJE6Ci8st9wngFQXeOMV94o=#etvbna6OmfzhTR2Xt5KCRqBqrLOi3jJHXkRO/xTg0gk0XqXnb6JKg+PchmeZHd2f//Eq7CHZo2vVVBxujKf1QcHv7UBlQhXTtiwntBEYp3NcONJYK6YJ5k4p4SMNytk5Z8VWaPH63UQbpY8RIPKo7cWKLhjnA0SzcPe17gBEJy0=#3##",
     *         "receiverProvinceName": "湖南省",
     *         "receiverTownName": "望月湖街道"
     *       }
     *     ]
     *   },
     *   "success": true
     * }
     * @throws IOException
     */
    @Test
    public void testBatchDecrypt() throws IOException {
        DataClient dataClient = new DataClient("http://ark.xiaohongshu.com/ark/open_api/v3/common_controller", appId, version, appSecre);

        BatchDecryptRequest request = new BatchDecryptRequest();
        request.setActionType("1");
        request.setAppUserId("1");

        BatchDecryptRequest.baseInfo receiverAddress = new BatchDecryptRequest.baseInfo();
        receiverAddress.setDataTag("P782011893460274131");
        receiverAddress.setEncryptedData("#Du12G8dm0Clc7JiDumrEzBB2HEi6dh0CWxG+5e+KyCzwaZNtR/VQ3HqB7/0JvhhgvRV73NBr5EilJSCEstn0duOlV6OhBiLkYb992xEfMG0bhI80ngnNSSDp44J3/PS+#etvbna6OmfzhTR2Xt5KCRqBqrLOi3jJHXkRO/xTg0gmRG3NY7DDT7REay+alfnq+1jEGeJ5ZwoC2jXn1WM95KfXaCTdQAzvxwAZvoCFg8bZeQSKl5CbJ3QlwnA7bAD+syxo6mp6MT1Ru+SimeLeCvYyyokHvqGWALZ2qjzrgvthMeRjOrgj7Uo75Dp/007npwuLMl71+J25qEwo7VCsX/goQYdTP3EUUighWs+VKtY00emIjHbkSipOaVKBPH4QA#1##");

        BatchDecryptRequest.baseInfo receiverName = new BatchDecryptRequest.baseInfo();
        receiverName.setDataTag("P782011893460274131");
        receiverName.setEncryptedData("#Du12G8dm0Clc7JiDumrEzO9F1I7DUVOvJ8tmQWofREs=#etvbna6OmfzhTR2Xt5KCRqBqrLOi3jJHXkRO/xTg0gm8ECllfj53Mq0YaP4kQVFiy0HvHtjbmISM/3CsrRv54A3cP94DNhsZODKRVLApGCOHCM5E9NKd0naaEWOd3PVGwNGtrAg2wBv13K/0+Wo4ArOU03CaKVTGC0JSCckiBcs=#2##");

        BatchDecryptRequest.baseInfo receiverPhone = new BatchDecryptRequest.baseInfo();
        receiverPhone.setDataTag("P782011893460274131");
        receiverPhone.setEncryptedData("#Du12G8dm0Clc7JiDumrEzJE6Ci8st9wngFQXeOMV94o=#etvbna6OmfzhTR2Xt5KCRqBqrLOi3jJHXkRO/xTg0gk0XqXnb6JKg+PchmeZHd2f//Eq7CHZo2vVVBxujKf1QcHv7UBlQhXTtiwntBEYp3NcONJYK6YJ5k4p4SMNytk5Z8VWaPH63UQbpY8RIPKo7cWKLhjnA0SzcPe17gBEJy0=#3##");

        request.setBaseInfos(Lists.newArrayList(receiverAddress, receiverName, receiverPhone));


        BaseResponse<BatchDecryptResponse> baseResponse = dataClient.execute(request, this.accessToken);

        System.out.println(JSON.toJSONString(baseResponse));
    }


    @Test
    public void testGetReceiveAddressDetail() throws IOException {
        OrderClient orderClient = new OrderClient("https://ark.xiaohongshu.com/ark/open_api/v3/common_controller", appId, version, appSecre);

        GetOrderReceiverInfoRequest request = new GetOrderReceiverInfoRequest();

        GetOrderReceiverInfoRequest.OrderReceiverQuery query = new GetOrderReceiverInfoRequest.OrderReceiverQuery();
        query.setOrderId("P782011893460274131");
        query.setOpenAddressId("4903b7bf6e37ad0efc1ce429dfccd670");

        request.setReceiverQueries(Lists.newArrayList(query));
        request.setIsReturn(Boolean.FALSE);

        BaseResponse<GetOrderReceiverInfoResponse> baseResponse = orderClient.execute(request, this.accessToken);

        System.out.println(JSON.toJSONString(baseResponse));
    }

    @Test
    public void testGetOrderDetail() throws IOException {
        OrderClient orderClient = new OrderClient("https://ark.xiaohongshu.com/ark/open_api/v3/common_controller", appId, version, appSecre);

        GetOrderDetailRequest request = new GetOrderDetailRequest();
        request.setOrderId("P790600755242471711");

        BaseResponse<GetOrderDetailResponse> baseResponse = orderClient.execute(request, this.accessToken);

        System.out.println(JSON.toJSONString(baseResponse));
    }

    @Test
    public void testGetOrderList() throws IOException {
        OrderClient orderClient = new OrderClient("https://ark.xiaohongshu.com/ark/open_api/v3/common_controller", appId, version, appSecre);

        GetOrderListRequest sdkRequest = new GetOrderListRequest();
        sdkRequest.setTimeType(1);
        sdkRequest.setStartTime(1775048400L);
        sdkRequest.setEndTime(1775052000L);
        sdkRequest.setPageNo(1);
        sdkRequest.setPageSize(10);
        sdkRequest.setOrderStatus(4);

        BaseResponse<GetOrderListResponse> baseResponse = orderClient.execute(sdkRequest, this.accessToken);

        System.out.println(JSON.toJSONString(baseResponse));
    }


    @Before
    public void init() throws IOException {
        // https://open.xiaohongshu.com/document/developer/file/38
        // 获取 accessToken
//        accessToken = getAccessToken();
//        System.out.println("获取到的 accessToken: " + accessToken);
    }

    /**
     * 使用 code 换取 accessToken
     * 参考文档: https://open.xiaohongshu.com/document/developer/file/38
     */
    private String getAccessToken() throws IOException {
        OauthClient oauthClient = new OauthClient("https://ark.xiaohongshu.com/ark/open_api/v3/common_controller", appId, version, appSecre);
        BaseResponse<GetAccessTokenResponse> baseResponse = oauthClient.execute(new GetAccessTokenRequest(code));

        System.out.println(JSON.toJSONString(baseResponse));

        if (baseResponse != null && baseResponse.isSuccess()) {
            return baseResponse.getData().getAccessToken();
        }
        return null;
    }

    private String refreshAccessToken() throws IOException {
        OauthClient oauthClient = new OauthClient("https://ark.xiaohongshu.com/ark/open_api/v3/common_controller", appId, version, appSecre);
        BaseResponse<RefreshTokenResponse> baseResponse = oauthClient.execute(new RefreshTokenRequest("refresh-5c0a882d94a9411c89b1cac8bdb8b476-25a6c939aa8544768528bc7f8fa6e037"));

        System.out.println(JSON.toJSONString(baseResponse));

        if (baseResponse != null && baseResponse.isSuccess()) {
            return baseResponse.getData().getAccessToken();
        }
        return null;
    }

    @Test
    public void testGetRefreshToken() throws Exception {
        System.out.println(refreshAccessToken());
    }

}
