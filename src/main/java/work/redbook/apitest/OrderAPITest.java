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
import com.xiaohongshu.fls.opensdk.entity.order.Response.OrderSimpleDetail;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * @author hanyu.wang
 * @version 1.0
 * @date 2025/12/25
 */
public class OrderAPITest {
    private static final DateTimeFormatter TIME_FORMATTER =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private static final ZoneId TIME_ZONE = ZoneId.of("Asia/Shanghai");

    private String appId = "ae807376fea64bbe9335";
    private String version = "2.0";
    private String appSecre = "7f86dcecb3237a5502ae51eff5a232bb";
    String code = "code-d92e34bac40f46818092a0d13e28be72-0dc69dfa92844c2897f08f95d9e00d06";
    private String accessToken = "token-1c2f43be2be14bffb5ad8f87577f6b7c-f947dd354fcd41c0abf7b46230936d2b";

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
        request.setOrderId("P798063388702233473");

        BaseResponse<GetOrderDetailResponse> baseResponse = orderClient.execute(request, this.accessToken);

        System.out.println(JSON.toJSONString(baseResponse));
    }

    @Test
    public void testGetOrderList() throws IOException {
        String startTime = "2026-07-16 19:00:00";
        String endTime = "2026-07-21 11:45:00";
        Path outputFile = Paths.get(
                System.getProperty("user.dir"),
                "src/main/java/work/redbook/apitest/order_status.csv"
        );

        exportOrderStatusCsv(startTime, endTime, outputFile);
    }

    @Test
    public void testGetOrderListBySkuId() throws IOException {
        String startTime = "2026-07-16 19:00:00";
        String endTime = "2026-07-21 12:06:00";
        String skuId = "6a5465cba553c30015337bad"; // 填写需要筛选的规格 ID
        Path outputFile = Paths.get(
                System.getProperty("user.dir"),
                "src/main/java/work/redbook/apitest/order_status_by_sku.csv"
        );

        exportOrderStatusBySkuIdCsv(startTime, endTime, skuId, outputFile);
    }

    /**
     * 按创建时间查询订单，并将订单号、订单状态导出为 CSV。
     * 接口限制单次查询跨度不超过 24 小时，因此较大的时间范围会自动拆分。
     */
    private void exportOrderStatusCsv(String startTimeText,
                                      String endTimeText,
                                      Path outputFile) throws IOException {
        OrderClient orderClient = new OrderClient("https://ark.xiaohongshu.com/ark/open_api/v3/common_controller", appId, version, appSecre);
        Map<String, OrderSimpleDetail> orderMap =
                getOrdersByCreatedTime(orderClient, startTimeText, endTimeText);

        List<String> csvLines = new ArrayList<>(orderMap.size() + 1);
        csvLines.add("\uFEFF" + getOrderCsvHeader());
        for (OrderSimpleDetail order : orderMap.values()) {
            csvLines.add(getOrderCsvRow(order));
        }
        writeCsv(outputFile, csvLines);
        System.out.println(orderMap.size() + " 条订单记录已写入: " + outputFile.toAbsolutePath());
    }

    /**
     * 按创建时间查询订单详情，仅导出包含指定规格 ID 的订单。
     */
    private void exportOrderStatusBySkuIdCsv(String startTimeText,
                                             String endTimeText,
                                             String skuId,
                                             Path outputFile) throws IOException {
        if (skuId == null || skuId.trim().isEmpty()) {
            throw new IllegalArgumentException("规格 ID 不能为空");
        }

        OrderClient orderClient = new OrderClient("https://ark.xiaohongshu.com/ark/open_api/v3/common_controller", appId, version, appSecre);
        Map<String, OrderSimpleDetail> orderMap =
                getOrdersByCreatedTime(orderClient, startTimeText, endTimeText);
        List<String> csvLines = new ArrayList<>();
        csvLines.add("\uFEFF" + getOrderCsvHeader() + ",规格ID,规格数量");

        List<OrderSimpleDetail> orders = new ArrayList<>(orderMap.values());
        final int detailBatchSize = 20;
        int totalBatchCount = (orders.size() + detailBatchSize - 1) / detailBatchSize;
        int matchedCount = 0;
        ExecutorService detailExecutor = Executors.newFixedThreadPool(detailBatchSize);
        try {
            for (int batchStart = 0; batchStart < orders.size(); batchStart += detailBatchSize) {
                int batchEnd = Math.min(batchStart + detailBatchSize, orders.size());
                List<OrderSimpleDetail> batchOrders = orders.subList(batchStart, batchEnd);
                List<Future<GetOrderDetailResponse>> detailFutures =
                        new ArrayList<>(batchOrders.size());

                // 详情接口只接受单个订单号，每批并发查询最多 20 个订单。
                for (OrderSimpleDetail order : batchOrders) {
                    detailFutures.add(detailExecutor.submit(
                            () -> getOrderDetail(orderClient, order.getOrderId())));
                }

                for (int i = 0; i < batchOrders.size(); i++) {
                    OrderSimpleDetail order = batchOrders.get(i);
                    GetOrderDetailResponse orderDetail =
                            getOrderDetailResult(detailFutures.get(i), order.getOrderId());
                    Long skuQuantity = getSkuQuantity(orderDetail, skuId);
                    if (skuQuantity != null) {
                        csvLines.add(getOrderCsvRow(order) + "," + skuId + "," + skuQuantity);
                        matchedCount++;
                    }
                }

                int currentBatchNo = batchStart / detailBatchSize + 1;
                System.out.println("已查询订单详情第 " + currentBatchNo + "/"
                        + totalBatchCount + " 批，本批 " + batchOrders.size()
                        + " 个，累计匹配订单数: " + matchedCount);
            }
        } finally {
            detailExecutor.shutdownNow();
        }

        writeCsv(outputFile, csvLines);
        System.out.println(matchedCount + " 条匹配订单已写入: " + outputFile.toAbsolutePath());
    }

    private Map<String, OrderSimpleDetail> getOrdersByCreatedTime(OrderClient orderClient,
                                                                  String startTimeText,
                                                                  String endTimeText) throws IOException {
        long startTime = parseTime(startTimeText);
        long endTime = parseTime(endTimeText);
        if (startTime < 0 || endTime < startTime) {
            throw new IllegalArgumentException("时间范围不合法: " + startTimeText + " - " + endTimeText);
        }

        Map<String, OrderSimpleDetail> orderMap = new LinkedHashMap<>();
        long queryStartTime = startTime;
        final long maxTimeRangeSeconds = 24 * 60 * 60L;

        while (queryStartTime <= endTime) {
            long queryEndTime = Math.min(queryStartTime + maxTimeRangeSeconds, endTime);

            GetOrderListRequest sdkRequest = new GetOrderListRequest();
            sdkRequest.setTimeType(1);
            sdkRequest.setStartTime(queryStartTime);
            sdkRequest.setEndTime(queryEndTime);
            sdkRequest.setPageSize(100);

            GetOrderListResponse firstPage = getOrderListPage(orderClient, sdkRequest, 1);
            int maxPageNo = Math.max(firstPage.getMaxPageNo(), 1);
            if (maxPageNo > 100) {
                throw new IOException("时间段 " + queryStartTime + " - " + queryEndTime
                        + " 共 " + maxPageNo + " 页，超过接口 100 页限制，请缩小查询时间范围");
            }

            for (int pageNo = maxPageNo; pageNo >= 2; pageNo--) {
                GetOrderListResponse response = getOrderListPage(orderClient, sdkRequest, pageNo);
                collectOrders(response.getOrderList(), orderMap);
                System.out.println("已拉取第 " + pageNo + "/" + maxPageNo
                        + " 页，累计订单数: " + orderMap.size());
            }
            collectOrders(firstPage.getOrderList(), orderMap);
            System.out.println("已拉取第 1/" + maxPageNo
                    + " 页，累计订单数: " + orderMap.size());

            if (queryEndTime == endTime) {
                break;
            }
            queryStartTime = queryEndTime + 1;
        }

        return orderMap;
    }

    private String getOrderCsvHeader() {
        return "订单号,订单状态,创建时间,支付时间,更新时间,发货时间,取消时间,完成时间,最晚承诺发货时间";
    }

    private String getOrderCsvRow(OrderSimpleDetail order) {
        return order.getOrderId()
                + "," + getOrderStatusName(order.getOrderStatus())
                + "," + formatTime(order.getCreatedTime())
                + "," + formatTime(order.getPaidTime())
                + "," + formatTime(order.getUpdateTime())
                + "," + formatTime(order.getDeliveryTime())
                + "," + formatTime(order.getCancelTime())
                + "," + formatTime(order.getFinishTime())
                + "," + formatTime(order.getPromiseLastDeliveryTime());
    }

    private void writeCsv(Path outputFile, List<String> csvLines) throws IOException {
        Path parent = outputFile.getParent();
        if (parent != null) {
            Files.createDirectories(parent);
        }
        Files.write(outputFile, csvLines, StandardCharsets.UTF_8);
    }

    private long parseTime(String timeText) {
        try {
            return LocalDateTime.parse(timeText, TIME_FORMATTER)
                    .atZone(TIME_ZONE)
                    .toEpochSecond();
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException(
                    "时间格式不正确，请使用 yyyy-MM-dd HH:mm:ss: " + timeText, e);
        }
    }

    private GetOrderListResponse getOrderListPage(OrderClient orderClient,
                                                   GetOrderListRequest request,
                                                   int pageNo) throws IOException {
        request.setPageNo(pageNo);
        BaseResponse<GetOrderListResponse> baseResponse = orderClient.execute(request, this.accessToken);
        if (baseResponse == null || !baseResponse.isSuccess() || baseResponse.getData() == null) {
            throw new IOException("拉取第 " + pageNo + " 页订单失败: " + JSON.toJSONString(baseResponse));
        }
        if (baseResponse.getData().getOrderList() == null) {
            throw new IOException("第 " + pageNo + " 页订单列表为空: " + JSON.toJSONString(baseResponse));
        }
        return baseResponse.getData();
    }

    private GetOrderDetailResponse getOrderDetail(OrderClient orderClient,
                                                   String orderId) throws IOException {
        GetOrderDetailRequest request = new GetOrderDetailRequest();
        request.setOrderId(orderId);
        BaseResponse<GetOrderDetailResponse> baseResponse =
                orderClient.execute(request, this.accessToken);
        if (baseResponse == null || !baseResponse.isSuccess() || baseResponse.getData() == null) {
            throw new IOException("拉取订单 " + orderId + " 详情失败: "
                    + JSON.toJSONString(baseResponse));
        }
        return baseResponse.getData();
    }

    private GetOrderDetailResponse getOrderDetailResult(
            Future<GetOrderDetailResponse> detailFuture,
            String orderId) throws IOException {
        try {
            return detailFuture.get();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new IOException("查询订单 " + orderId + " 详情时线程被中断", e);
        } catch (ExecutionException e) {
            Throwable cause = e.getCause();
            if (cause instanceof IOException) {
                throw (IOException) cause;
            }
            throw new IOException("查询订单 " + orderId + " 详情失败", cause);
        }
    }

    private Long getSkuQuantity(GetOrderDetailResponse orderDetail, String skuId) {
        List<GetOrderDetailResponse.OrderSkuDTOV3> skuList = orderDetail.getSkuList();
        if (skuList == null) {
            return null;
        }

        long totalQuantity = 0;
        boolean matched = false;
        for (GetOrderDetailResponse.OrderSkuDTOV3 sku : skuList) {
            if (sku != null && skuId.equals(sku.getSkuId())) {
                totalQuantity += sku.getSkuQuantity();
                matched = true;
            }
        }
        return matched ? totalQuantity : null;
    }

    private void collectOrders(List<OrderSimpleDetail> orderList,
                               Map<String, OrderSimpleDetail> orderMap) {
        for (OrderSimpleDetail order : orderList) {
            if (order != null && order.getOrderId() != null) {
                orderMap.put(order.getOrderId(), order);
            }
        }
    }

    private String formatTime(long timestamp) {
        if (timestamp <= 0) {
            return "";
        }
        return Instant.ofEpochMilli(timestamp)
                .atZone(TIME_ZONE)
                .format(TIME_FORMATTER);
    }

    private String getOrderStatusName(int orderStatus) {
        switch (orderStatus) {
            case 1:
                return "已下单待付款";
            case 2:
                return "已支付处理中";
            case 3:
                return "清关中";
            case 4:
                return "待发货";
            case 5:
                return "部分发货";
            case 6:
                return "待收货";
            case 7:
                return "已完成";
            case 8:
                return "已关闭";
            case 9:
                return "已取消";
            case 10:
                return "换货申请中";
            default:
                return "未知状态(" + orderStatus + ")";
        }
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
