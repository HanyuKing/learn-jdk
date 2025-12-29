package work.redbook.apitest;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.xiaohongshu.fls.opensdk.client.DataClient;
import com.xiaohongshu.fls.opensdk.client.OauthClient;
import com.xiaohongshu.fls.opensdk.client.OrderClient;
import com.xiaohongshu.fls.opensdk.entity.BaseResponse;
import com.xiaohongshu.fls.opensdk.entity.afterSale.request.ReceiveAndShipRequest;
import com.xiaohongshu.fls.opensdk.entity.data.request.BatchDecryptRequest;
import com.xiaohongshu.fls.opensdk.entity.data.response.BatchDecryptResponse;
import com.xiaohongshu.fls.opensdk.entity.oauth.request.GetAccessTokenRequest;
import com.xiaohongshu.fls.opensdk.entity.oauth.response.GetAccessTokenResponse;
import com.xiaohongshu.fls.opensdk.entity.order.Requset.GetOrderDetailRequest;
import com.xiaohongshu.fls.opensdk.entity.order.Requset.GetOrderListRequest;
import com.xiaohongshu.fls.opensdk.entity.order.Requset.GetOrderReceiverInfoRequest;
import com.xiaohongshu.fls.opensdk.entity.order.Response.GetOrderDetailResponse;
import com.xiaohongshu.fls.opensdk.entity.order.Response.GetOrderListResponse;
import com.xiaohongshu.fls.opensdk.entity.order.Response.GetOrderReceiverInfoResponse;
import org.junit.Before;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

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
    private String accessToken = "token-2859f39a464a4df2b75b38f7bfa6ab34-82dd48c83a254964b7e7b87de10052fa";

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
        OrderClient orderClient = new OrderClient("http://ark.xiaohongshu.com/ark/open_api/v3/common_controller", appId, version, appSecre);

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
        OrderClient orderClient = new OrderClient("http://ark.xiaohongshu.com/ark/open_api/v3/common_controller", appId, version, appSecre);

        GetOrderDetailRequest request = new GetOrderDetailRequest();
        request.setOrderId("P782011893460274131");

        BaseResponse<GetOrderDetailResponse> baseResponse = orderClient.execute(request, this.accessToken);

        System.out.println(JSON.toJSONString(baseResponse));
    }

    @Test
    public void testGetOrderList() throws IOException {
        OrderClient orderClient = new OrderClient("http://ark.xiaohongshu.com/ark/open_api/v3/common_controller", appId, version, appSecre);

        GetOrderListRequest request = new GetOrderListRequest();
        request.setTimeType(1);
        request.setStartTime(1766459000L);
        request.setEndTime(1766542693L);

        BaseResponse<GetOrderListResponse> baseResponse = orderClient.execute(request, this.accessToken);

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

}
