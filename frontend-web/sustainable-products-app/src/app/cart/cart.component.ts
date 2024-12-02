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
  }

  order() {
    this.cartService.orderCart(this.cartService.cart?.id!, this.cartService.cart?.total!, true);
    this.cartService.setCartNull();
  }

}
