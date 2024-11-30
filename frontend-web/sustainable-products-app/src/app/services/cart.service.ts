import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { catchError, of } from 'rxjs';
import { Cart } from '../models/cart';

@Injectable({
  providedIn: 'root'
})
export class CartService {
  private url = "http://localhost:8087/cart/api/cart";

  constructor(private http: HttpClient) {}

  getCart(id: number) {
    return this.http
      .get(
        `${this.url}/${id}`
      )
      .pipe(
        catchError(() => {
          return of(null);
        })
      );
  }

  createCart() {
    return this.http
      .post(
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
      .post(
        `${this.url}/product/add/${productId}`,
        {cart}
      )
      .pipe(
        catchError(() => {
          return of(null);
        })
      );      
  }

  removeItem(productId: number, cart: Cart) {
    return this.http
      .post(
        `${this.url}/product/remove/${productId}`,
        {cart}
      )
      .pipe(
        catchError(() => {
          return of(null);
        })
      );      
  }


  orderCart(id: number, total: number, ordered: boolean) {
    return this.http
      .patch(
        `${this.url}/${id}/order`,
        {total, ordered}
      )
      .pipe(
        catchError(() => {
          return of(null);
        })
      );    
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
