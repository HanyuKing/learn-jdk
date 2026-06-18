package work.redbook.apitest;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.xiaohongshu.fls.opensdk.client.ExpressClient;
import com.xiaohongshu.fls.opensdk.client.OrderClient;
import com.xiaohongshu.fls.opensdk.entity.BaseResponse;
import com.xiaohongshu.fls.opensdk.entity.express.ElectronicBillAddress;
import com.xiaohongshu.fls.opensdk.entity.express.ElectronicBillItem;
import com.xiaohongshu.fls.opensdk.entity.express.ElectronicBillUserInfo;
import com.xiaohongshu.fls.opensdk.entity.express.request.ElectronicBillOrdersCreateRequest;
import com.xiaohongshu.fls.opensdk.entity.express.request.ElectronicBillSubscribesQueryRequest;
import com.xiaohongshu.fls.opensdk.entity.express.request.ElectronicBillTemplatesQueryRequest;
import com.xiaohongshu.fls.opensdk.entity.express.response.ElectronicBillOrdersCreateResponse;
import com.xiaohongshu.fls.opensdk.entity.express.response.ElectronicBillSubscribesQueryResponse;
import com.xiaohongshu.fls.opensdk.entity.express.response.ElectronicBillTemplatesQueryResponse;
import com.xiaohongshu.fls.opensdk.entity.order.Requset.GetOrderDetailRequest;
import com.xiaohongshu.fls.opensdk.entity.order.Requset.OrderDeliverRequest;
import com.xiaohongshu.fls.opensdk.entity.order.Response.GetOrderDetailResponse;
import org.junit.Test;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

/**
 * 电子面单发货 SDK 接口测试
 * 流程: 查询订购关系 -> 查询模板 -> 批量取号 -> 订单发货
 * 参考文档: https://xiaohongshu.apifox.cn/doc-2811019.md
 */
public class ElectronicBillDeliverAPITest {
    private static final String API_URL = "https://ark.xiaohongshu.com/ark/open_api/v3/common_controller";
    private static final int BILL_VERSION = 2;
    private static final String ORDER_CHANNEL = "XIAO_HONG_SHU";

    private String appId = "ae807376fea64bbe9335";
    private String version = "2.0";
    private String appSecre = "7f86dcecb3237a5502ae51eff5a232bb";
    private String accessToken = "token-f0011ace562b49409adb093c4a219c87-7720a1088a1c4676a753d85126be5770";

    /** 待发货订单号，需替换为 status=待发货 的订单 */
    private String orderId = "P790600755242471711";
    /** 快递公司编码，如 zto、yuantong、shunfeng */
    private String cpCode = "zto";

    private final ExpressClient expressClient = new ExpressClient(API_URL, appId, version, appSecre);
    private final OrderClient orderClient = new OrderClient(API_URL, appId, version, appSecre);

    /**
     * 查询电子面单订购关系
     * method: express.queryEbillSubscribes
     */
    @Test
    public void testQueryEbillSubscribes() throws IOException {
        ElectronicBillSubscribesQueryRequest request = new ElectronicBillSubscribesQueryRequest();
        request.setBillVersion(BILL_VERSION);
        request.setCpCode(cpCode);
        request.setNeedUsage(true);

        BaseResponse<ElectronicBillSubscribesQueryResponse> response = expressClient.execute(request, accessToken);
        System.out.println(JSON.toJSONString(response));
    }

    /**
     * 查询电子面单模板
     * method: express.queryEbillTemplates
     */
    @Test
    public void testQueryEbillTemplates() throws IOException {
        ElectronicBillTemplatesQueryRequest request = new ElectronicBillTemplatesQueryRequest();
        request.setBillVersion(BILL_VERSION);
        request.setCpCode(cpCode);

        BaseResponse<ElectronicBillTemplatesQueryResponse> response = expressClient.execute(request, accessToken);
        System.out.println(JSON.toJSONString(response));
    }

    /**
     * 电子面单发货完整流程: 取号 + 发货
     */
    @Test
    public void testElectronicBillDeliver() throws IOException {
        GetOrderDetailResponse orderDetail = getOrderDetail(orderId);
        ElectronicBillSubscribesQueryResponse.Subscribe subscribe = querySubscribe(cpCode);
        Long templateId = queryTemplateId(cpCode, subscribe.getBrandCode());

        String waybillCode = createEbillOrder(orderDetail, subscribe, templateId);
        deliverOrder(orderId, waybillCode, cpCode, subscribe.getCpName());
    }

    private GetOrderDetailResponse getOrderDetail(String orderId) throws IOException {
        GetOrderDetailRequest request = new GetOrderDetailRequest();
        request.setOrderId(orderId);

        BaseResponse<GetOrderDetailResponse> response = orderClient.execute(request, accessToken);
        System.out.println("order detail: " + JSON.toJSONString(response));

        if (response == null || !response.isSuccess() || response.getData() == null) {
            throw new IllegalStateException("查询订单详情失败");
        }
        return response.getData();
    }

    private ElectronicBillSubscribesQueryResponse.Subscribe querySubscribe(String cpCode) throws IOException {
        ElectronicBillSubscribesQueryRequest request = new ElectronicBillSubscribesQueryRequest();
        request.setBillVersion(BILL_VERSION);
        request.setCpCode(cpCode);
        request.setNeedUsage(true);

        BaseResponse<ElectronicBillSubscribesQueryResponse> response = expressClient.execute(request, accessToken);
        System.out.println("ebill subscribes: " + JSON.toJSONString(response));

        if (response == null || !response.isSuccess() || response.getData() == null
                || isEmpty(response.getData().getSubscribeList())) {
            throw new IllegalStateException("未查询到电子面单订购关系，请先在千帆后台开通电子面单");
        }
        return response.getData().getSubscribeList().get(0);
    }

    private Long queryTemplateId(String cpCode, String brandCode) throws IOException {
        ElectronicBillTemplatesQueryRequest request = new ElectronicBillTemplatesQueryRequest();
        request.setBillVersion(BILL_VERSION);
        request.setCpCode(cpCode);
        request.setBrandCode(brandCode);

        BaseResponse<ElectronicBillTemplatesQueryResponse> response = expressClient.execute(request, accessToken);
        System.out.println("ebill templates: " + JSON.toJSONString(response));

        if (response == null || !response.isSuccess() || response.getData() == null
                || isEmpty(response.getData().getTemplateList())) {
            throw new IllegalStateException("未查询到电子面单模板");
        }
        return response.getData().getTemplateList().get(0).getId();
    }

    private String createEbillOrder(GetOrderDetailResponse orderDetail,
                                    ElectronicBillSubscribesQueryResponse.Subscribe subscribe,
                                    Long templateId) throws IOException {
        ElectronicBillOrdersCreateRequest request = new ElectronicBillOrdersCreateRequest();
        request.setBillVersion(BILL_VERSION);
        request.setCpCode(subscribe.getCpCode());
        request.setCustomerCode(subscribe.getCustomerCode());
        request.setBranchCode(subscribe.getBranchCode());
        request.setBrandCode(subscribe.getBrandCode());
        request.setSender(buildSender(subscribe));
        request.setTradeOrderInfoList(Lists.newArrayList(buildTradeOrderInfo(orderDetail, templateId)));

        BaseResponse<ElectronicBillOrdersCreateResponse> response = expressClient.execute(request, accessToken);
        System.out.println("create ebill: " + JSON.toJSONString(response));

        if (response == null || !response.isSuccess() || response.getData() == null
                || isEmpty(response.getData().getWayBillList())) {
            throw new IllegalStateException("电子面单取号失败");
        }

        ElectronicBillOrdersCreateResponse.ElectronicBillPrintData printData = response.getData().getWayBillList().get(0);
        if (!printData.isSuccess()) {
            throw new IllegalStateException("电子面单取号失败: " + printData.getErrorMsg());
        }
        return printData.getWaybillCode();
    }

    private void deliverOrder(String orderId, String expressNo, String expressCompanyCode, String expressCompanyName)
            throws IOException {
        OrderDeliverRequest request = new OrderDeliverRequest();
        request.setOrderId(orderId);
        request.setExpressNo(expressNo);
        request.setExpressCompanyCode(expressCompanyCode);
        request.setExpressCompanyName(expressCompanyName);
        request.setDeliveringTime(System.currentTimeMillis());
        request.setUnpack(false);

        BaseResponse<String> response = orderClient.execute(request, accessToken);
        System.out.println("order deliver: " + JSON.toJSONString(response));
    }

    private ElectronicBillUserInfo buildSender(ElectronicBillSubscribesQueryResponse.Subscribe subscribe) {
        if (isEmpty(subscribe.getSenderAddressList())) {
            throw new IllegalStateException("订购关系中未配置寄件地址");
        }

        ElectronicBillSubscribesQueryResponse.SenderAddress senderAddress = subscribe.getSenderAddressList().get(0);
        ElectronicBillSubscribesQueryResponse.Address sourceAddress = senderAddress.getAddress();

        ElectronicBillAddress address = new ElectronicBillAddress();
        address.setProvince(sourceAddress.getProvince());
        address.setCity(sourceAddress.getCity());
        address.setDistrict(sourceAddress.getDistrict());
        address.setTown(sourceAddress.getTown());
        address.setDetail(sourceAddress.getDetail());

        ElectronicBillUserInfo sender = new ElectronicBillUserInfo();
        sender.setName(senderAddress.getName());
        sender.setMobile(senderAddress.getMobile());
        sender.setPhone(senderAddress.getPhone());
        sender.setAddress(address);
        return sender;
    }

    private ElectronicBillOrdersCreateRequest.ElectronicBillTradeOrderInfo buildTradeOrderInfo(
            GetOrderDetailResponse orderDetail, Long templateId) {
        String objectId = UUID.randomUUID().toString().replace("-", "");

        ElectronicBillOrdersCreateRequest.ElectronicBillOrderInfo orderInfo =
                new ElectronicBillOrdersCreateRequest.ElectronicBillOrderInfo();
        orderInfo.setOrderChannelsType(ORDER_CHANNEL);
        orderInfo.setXhsOrderId(orderDetail.getOrderId());
        orderInfo.setXhsOrderList(Lists.newArrayList(orderDetail.getOrderId()));

        ElectronicBillOrdersCreateRequest.ElectronicBillPackageInfo packageInfo =
                new ElectronicBillOrdersCreateRequest.ElectronicBillPackageInfo();
        packageInfo.setId("1");
        packageInfo.setWeight(100L);
        packageInfo.setItems(buildPackageItems(orderDetail.getSkuList()));

        ElectronicBillUserInfo recipient = new ElectronicBillUserInfo();
        recipient.setOpenAddressId(orderDetail.getOpenAddressId());

        ElectronicBillOrdersCreateRequest.ElectronicBillTradeOrderInfo tradeOrderInfo =
                new ElectronicBillOrdersCreateRequest.ElectronicBillTradeOrderInfo();
        tradeOrderInfo.setObjectId(objectId);
        tradeOrderInfo.setTemplateId(templateId);
        tradeOrderInfo.setOrderInfo(orderInfo);
        tradeOrderInfo.setPackageInfo(packageInfo);
        tradeOrderInfo.setRecipient(recipient);
        return tradeOrderInfo;
    }

    private List<ElectronicBillItem> buildPackageItems(
            List<GetOrderDetailResponse.OrderSkuDTOV3> skuList) {
        if (isEmpty(skuList)) {
            ElectronicBillItem item = new ElectronicBillItem();
            item.setName("商品");
            item.setCount(1);
            return Lists.newArrayList(item);
        }

        GetOrderDetailResponse.OrderSkuDTOV3 sku = skuList.get(0);
        ElectronicBillItem item = new ElectronicBillItem();
        item.setName(sku.getSkuName());
        item.setSpecification(sku.getSkuSpec());
        item.setCount(sku.getSkuQuantity());
        return Lists.newArrayList(item);
    }

    private static boolean isEmpty(List<?> list) {
        return list == null || list.isEmpty();
    }
}
