package org.mybatis.jpetstore.mapper;

import java.lang.String;
import java.util.List;
import org.mybatis.jpetstore.domain.Product;

public interface ProductMapper {


List<Product> getProductListByCategory(String categoryId);

Product getProduct(String productId);

List<Product> searchProductList(String keywords);

}