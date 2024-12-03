import { Component } from '@angular/core';
import { CartService } from '../services/cart.service';

@Component({
  selector: 'app-cart',
  standalone: true,
  imports: [],
  templateUrl: './cart.component.html',
  styleUrl: './cart.component.css'
})
export class CartComponent {
  constructor(public cartService: CartService) {}

  showToast = false;
  toastMessage = '';

  calculateTotal(): number {
    const total = this.cartService.cart!.items.reduce(
      (sum, product) => sum + product.price * product.quantity,
      0
    );
    this.cartService.cart!.total = total;
    return total;    
  }

  removeProduct(productId: number) {
    this.cartService.removeItem(productId, this.cartService.cart!);
    setTimeout(() => {
      this.toastMessage = 'Product successfully deleted from cart!';
      this.showToast = true;

      setTimeout(() => {
        this.showToast = false;
      }, 3000);
    }, 500);  
  }

  order() {
    this.cartService.orderCart(this.cartService.cart?.id!, this.cartService.cart?.total!, true);
    setTimeout(() => {
      this.toastMessage = 'Order completed, we will send your order as soon as possible';
      this.showToast = true;

      setTimeout(() => {
        this.showToast = false;
      }, 5000);
    }, 500);  
    this.cartService.setCartNull();
  }

  hideToast() {
    this.showToast = false;
  }

}
