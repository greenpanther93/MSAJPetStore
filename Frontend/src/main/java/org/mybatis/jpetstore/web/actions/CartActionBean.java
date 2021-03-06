/**
 *    Copyright ${license.git.copyrightYears} the original author or authors.
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package org.mybatis.jpetstore.web.actions;

import java.lang.Exception;
import java.lang.Integer;
import java.lang.String;
import java.util.Iterator;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import net.sourceforge.stripes.action.ActionBeanContext;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.Resolution;
import org.mybatis.jpetstore.domain.Cart;
import org.mybatis.jpetstore.domain.CartItem;
import org.mybatis.jpetstore.domain.Item;
import org.mybatis.jpetstore.web.actions.AbstractActionBean;

import interfaces.ProxyCatalogService;
import net.sourceforge.stripes.action.SessionScope;
import net.sourceforge.stripes.integration.spring.SpringBean;

@SessionScope
public class CartActionBean extends AbstractActionBean {
  private static final long serialVersionUID = -4038684592582714235L;
  private static final String VIEW_CART = "/WEB-INF/jsp/cart/Cart.jsp";
  private static final String CHECK_OUT = "/WEB-INF/jsp/cart/Checkout.jsp";
  @SpringBean
  private transient ProxyCatalogService catalogService;
  private Cart cart = new Cart();
  private String workingItemId;

  static {

  }

  public Cart getCart() {

    return cart;

  }

  public void setCart(Cart cart) {

    this.cart = cart;

  }

  public void setWorkingItemId(String workingItemId) {

    this.workingItemId = workingItemId;

  }

  public Resolution addItemToCart() {

    if (cart.containsItemId(workingItemId)) {
      cart.incrementQuantityByItemId(workingItemId);
    } else {
      // isInStock is a "real-time" property that must be updated
      // every time an item is added to the cart, even if other
      // item details are cached.
      boolean isInStock = catalogService.isItemInStock(workingItemId);
      Item item = catalogService.getItem(workingItemId);
      cart.addItem(item, isInStock);
    }

    return new ForwardResolution(VIEW_CART);

  }

  public Resolution removeItemFromCart() {

    Item item = cart.removeItemById(workingItemId);

    if (item == null) {
      setMessage("Attempted to remove null CartItem from Cart.");
      return new ForwardResolution(ERROR);
    } else {
      return new ForwardResolution(VIEW_CART);
    }

  }

  public Resolution updateCartQuantities() {

    HttpServletRequest request = context.getRequest();

    Iterator<CartItem> cartItems = getCart().getAllCartItems();
    while (cartItems.hasNext()) {
      CartItem cartItem = cartItems.next();
      String itemId = cartItem.getItem().getItemId();
      try {
        int quantity = Integer.parseInt(request.getParameter(itemId));
        getCart().setQuantityByItemId(itemId, quantity);
        if (quantity < 1) {
          cartItems.remove();
        }
      } catch (Exception e) {
        // ignore parse exceptions on purpose
      }
    }

    return new ForwardResolution(VIEW_CART);

  }

  public ForwardResolution viewCart() {

    return new ForwardResolution(VIEW_CART);

  }

  public ForwardResolution checkOut() {

    return new ForwardResolution(CHECK_OUT);

  }

  public void clear() {

    cart = new Cart();
    workingItemId = null;

  }

}