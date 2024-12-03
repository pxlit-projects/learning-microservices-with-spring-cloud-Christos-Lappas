import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { catchError, of } from 'rxjs';
import { Product } from '../models/product';
import { Category } from '../models/category.enum';
import { Score } from '../models/score.enum';

@Injectable({
  providedIn: 'root'
})
export class ProductService {
  private url = "http://localhost:8087/product/api/product";

  constructor(private http: HttpClient) {}

  getProducts() {
    return this.http
      .get<Product[]>(
        `${this.url}`
      )
      .pipe(
        catchError(() => {
          return of(null);
        })
      );
  }

  getProduct(id: number) {
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

  addProduct(product: Product) {
    return this.http
      .post(
        `${this.url}`,
        product
      )
      .pipe(
        catchError(() => {
          return of(null);
        })
      );      
  }

  deleteProduct(id: number) {
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

  updateProduct(id: number, product: Product) {
    return this.http
      .put(
        `${this.url}/${id}`,
        product
      )
      .pipe(
        catchError(() => {
          return of(null);
        })
      );      
  }
  
  searchProducts(category?: Category, score?: Score, name?: string, label?: string, maxPrice?: number) {
    let params = new HttpParams();

    if (category) params = params.set('category', category);
    if (score) params = params.set('score', score);
    if (name) params = params.set('name', name);
    if (label) params = params.set('label', label);
    if (maxPrice) params = params.set('maxPrice', maxPrice);

    console.log(params);

    return this.http
      .get<Product[]>(
        `${this.url}/filter`,
        { params }
      )
      .pipe(
        catchError(() => {
          return of(null);
        })
      ); 
  }

  addLabel(id: number, label: string) {
    let params = new HttpParams();
    params = params.set('label', label);

    return this.http
      .post<Product>(
        `${this.url}/${id}/labels`,
        params
      )
      .pipe(
        catchError(() => {
          return of(null);
        })
      );      
  }

  deleteLabel(id: number, label: string) {
    let params = new HttpParams();
    params = params.set('label', label);

    return this.http
      .delete<Product>(
        `${this.url}/${id}/labels`,
        {params}
      )
      .pipe(
        catchError(() => {
          return of(null);
        })
      );      
  }


}
