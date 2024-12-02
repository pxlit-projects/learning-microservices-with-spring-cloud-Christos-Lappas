import { HttpClient } from '@angular/common/http';
import { Injectable, signal } from '@angular/core';
import { catchError, of } from 'rxjs';
import { Cart } from '../models/cart';

@Injectable({
  providedIn: 'root'
})
export class CartService {
  private url = "http://localhost:8087/cart/api/cart";
  cartSignal = signal<Cart | null>(this.getCartFromLocalStorage());

  get cart() {
    return this.cartSignal();
  }

  constructor(private http: HttpClient) {}

  cartIsNull(): boolean {
    return this.cart == null ? true : false;
  }

  setCartNull() {
    this.cartSignal.set(null);
  }

  private saveCartToLocalStorage(cart: Cart | null): void {
    if (cart) {
      localStorage.setItem('cart', JSON.stringify(cart));
    } else {
      localStorage.removeItem('cart');
    }
  }

  private getCartFromLocalStorage(): Cart | null {
    const cartString = localStorage.getItem('cart');
    return cartString ? JSON.parse(cartString) : null;
  }

  getCart(id: number) {
    return this.http
      .get<Cart>(
        `${this.url}/${id}`
      )
      .pipe(
        catchError(() => {
          return of(null);
        })
      )
      .subscribe((response) => {
        console.log(response);
        if (response) {
          this.cartSignal.set(response);
          this.saveCartToLocalStorage(this.cart);
          return response;
        } else {
          return null;
        }
      }
    );
  }

  createCart() {
    return this.http
      .post<Cart>(
        `${this.url}`,
        {}
      )
      .pipe(
        catchError(() => {
          return of(null);
        })
      );      
  }

  addItem(productId: number, cart: Cart) {
    return this.http
      .post<Cart>(
        `${this.url}/product/add/${productId}`,
        cart
      )
      .pipe(
        catchError((err) => {
          console.log(err);
          return of(null);
        })
      )
      .subscribe((response) => {
        if (response) {
          this.cartSignal.set(response);
          this.saveCartToLocalStorage(this.cart);
          return response;
        } else {
          return null;
        }
      }); 
            
  }

  removeItem(productId: number, cart: Cart) {
    return this.http
      .post<Cart>(
        `${this.url}/product/remove/${productId}`,
        cart
      )
      .pipe(
        catchError(() => {
          return of(null);
        })
      )
      .subscribe((response) => {
        if (response) {
          this.cartSignal.set(response);
          this.saveCartToLocalStorage(this.cart);
          return response;
        } else {
          return null;
        }
      });      
  }


  orderCart(id: number, total: number, ordered: boolean) {
    return this.http
      .patch<Cart>(
        `${this.url}/${id}/order`,
        {total, ordered}
      )
      .pipe(
        catchError(() => {
          return of(null);
        })
      )
      .subscribe((response) => {
        if (response) {
          this.setCartNull();
          this.saveCartToLocalStorage(null);
          return response;
        } else {
          return null;
        }
      });    
  }

  deleteCart(id: number) {
    return this.http
      .delete(
        `${this.url}/${id}`
      )
      .pipe(
        catchError(() => {
          return of(null);
        })
      );
  }
}
