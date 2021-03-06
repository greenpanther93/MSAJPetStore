package org.mybatis.jpetstore.mapper;

import java.lang.Object;
import java.lang.String;
import java.util.List;
import java.util.Map;
import org.mybatis.jpetstore.domain.Item;

public interface ItemMapper {


void updateInventoryQuantity(Map<String,Object> param);

int getInventoryQuantity(String itemId);

List<Item> getItemListByProduct(String productId);

Item getItem(String itemId);

}