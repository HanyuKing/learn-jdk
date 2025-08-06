package easyexcel;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.metadata.Cell;
import com.alibaba.excel.read.listener.ReadListener;
import com.alibaba.excel.read.metadata.holder.ReadRowHolder;
import com.alibaba.excel.util.ListUtils;
import com.alibaba.excel.metadata.data.CellData;
import com.alibaba.excel.metadata.data.ReadCellData;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.metadata.data.CellData;
import com.alibaba.excel.read.listener.ReadListener;
import com.alibaba.excel.util.ListUtils;
import com.alibaba.fastjson.JSON;

import java.util.*;

public class ProductImportListener implements ReadListener<Map<Integer, String>> {
    private List<ProductImportDTO> dataList;
    public ProductImportListener(List<ProductImportDTO> dataList) {
        this.dataList = dataList;
    }

    private Map<Integer, String> headMap;

    @Override
    public void invokeHead(Map<Integer, ReadCellData<?>> headerMap, AnalysisContext context) {
        this.headMap = new HashMap<>();
        headerMap.forEach((index, cell) -> {
            headMap.put(index, cell.getStringValue());
        });
    }

    @Override
    public void invoke(Map<Integer, String> integerStringMap, AnalysisContext analysisContext) {
//        if (analysisContext.readRowHolder().getRowIndex() <= 1) {
//            return;
//        }
        ProductImportDTO productImportDTO = new ProductImportDTO();
        productImportDTO.setProductId(integerStringMap.get(0));

        Map<String, List<String>> skuPropMap = new HashMap<>();
        productImportDTO.setSkuPropMap(skuPropMap);

        Map<String, List<String>> categoryKeyPropMap = new HashMap<>();
        productImportDTO.setCategoryKeyPropMap(categoryKeyPropMap);

        Map<String, List<String>> categoryOtherPropMap = new HashMap<>();
        productImportDTO.setCategoryOtherPropMap(categoryOtherPropMap);

        integerStringMap.forEach((index, val) -> {
            String header = headMap.get(index);
            if (header != null && header.startsWith("商品规格-")) {
                skuPropMap.put(header.replace("商品规格-", ""), Arrays.asList(val.split(",")));
            }
            if (header != null && header.startsWith("关键属性-")) {
                categoryKeyPropMap.put(header.replace("关键属性-", ""), Arrays.asList(val.split(",")));
            }
            if (header != null && header.startsWith("其它属性-")) {
                categoryOtherPropMap.put(header.replace("其它属性-", ""), Arrays.asList(val.split(",")));
            }
        });

        dataList.add(productImportDTO);
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {

    }
}