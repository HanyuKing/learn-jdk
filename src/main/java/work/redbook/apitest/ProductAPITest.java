package work.redbook.apitest;

import com.alibaba.fastjson.JSON;
import com.xiaohongshu.fls.opensdk.client.ProductClient;
import com.xiaohongshu.fls.opensdk.entity.BaseResponse;
import com.xiaohongshu.fls.opensdk.entity.product.request.v3.GetItemInfoRequest;
import com.xiaohongshu.fls.opensdk.entity.product.request.v3.SearchItemListRequest;
import com.xiaohongshu.fls.opensdk.entity.product.request.v3.UpdateSkuAvailableRequest;
import com.xiaohongshu.fls.opensdk.entity.product.response.v3.GetItemInfoResponse;
import com.xiaohongshu.fls.opensdk.entity.product.response.v3.SearchItemListResponse;
import org.junit.Test;

import java.io.IOException;

/**
 * 小红书商品 API 接口测试
 * 参考文档: https://xiaohongshu.apifox.cn/api-24868781.md (获取ITEM详情)
 *          https://xiaohongshu.apifox.cn/api-24865354.md (查询Item列表)
 *          https://xiaohongshu.apifox.cn/api-24838962.md (商品上下架)
 */
public class ProductAPITest {
    private static final String API_URL = "https://ark.xiaohongshu.com/ark/open_api/v3/common_controller";

    private String appId = "ae807376fea64bbe9335";
    private String version = "2.0";
    private String appSecre = "7f86dcecb3237a5502ae51eff5a232bb";
    private String accessToken = "token-e9a0c574875d4dee9946931523273a6f-59c74938500b482bba2ccf0259130ccb";

    private final ProductClient productClient = new ProductClient(API_URL, appId, version, appSecre);

    /**
     * 获取 ITEM 详情（含 SKU 信息）
     * method: product.getItemInfo
     */
    @Test
    public void testGetItemInfo() throws IOException {
        GetItemInfoRequest request = new GetItemInfoRequest();
        request.setItemId("69c5ebe1f1fe0f00153c6e64");
        request.setPageNo(1);
        request.setPageSize(10);

        BaseResponse<GetItemInfoResponse> response = productClient.execute(request, accessToken);
        System.out.println(JSON.toJSONString(response));
    }

    /**
     * 查询 Item 列表，可用于获取 itemId
     * method: product.searchItemList
     */
    @Test
    public void testSearchItemList() throws IOException {
        SearchItemListRequest request = new SearchItemListRequest();
        request.setPageNo(1);
        request.setPageSize(10);

        SearchItemListRequest.ItemSearchParam searchParam = new SearchItemListRequest.ItemSearchParam();
        request.setSearchParam(searchParam);

        BaseResponse<SearchItemListResponse> response = productClient.execute(request, accessToken);
        System.out.println(JSON.toJSONString(response));
    }

    /**
     * 商品下架
     * method: product.updateSkuAvailable
     * 文档: https://xiaohongshu.apifox.cn/api-24838962.md
     * available 为 true 表示上架，false 表示下架
     * skuId 可从 testGetItemInfo 返回的 skuInfos.id 获取
     */
    @Test
    public void testOffShelfSku() throws IOException {
        UpdateSkuAvailableRequest request = new UpdateSkuAvailableRequest();
        request.setSkuId("6a1fea40b8334e0001a3189e");
        request.setAvailable(false);

        BaseResponse<String> response = productClient.execute(request, accessToken);
        System.out.println(JSON.toJSONString(response));
    }
}
