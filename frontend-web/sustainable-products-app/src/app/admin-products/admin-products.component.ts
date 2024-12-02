import { Component, signal } from '@angular/core';
import { Product } from '../models/product';
import { ProductService } from '../services/product.service';

@Component({
  selector: 'app-admin-products',
  standalone: true,
  imports: [],
  templateUrl: './admin-products.component.html',
  styleUrl: './admin-products.component.css'
})
export class AdminProductsComponent {
  products = signal<Product[]>([]);

  constructor(private productService: ProductService) {}

  ngOnInit(): void {
    this.fetchProducts();
  }

  fetchProducts(): void {
    this.productService.getProducts()
    .subscribe({
      next: (response) => {
        this.products.set(response ?? [])      
      },
      error: (err) => {
        console.error('Failed to fetch products', err)
      }
    });
  }

  editProduct(productId: number) {
    
  }
}
