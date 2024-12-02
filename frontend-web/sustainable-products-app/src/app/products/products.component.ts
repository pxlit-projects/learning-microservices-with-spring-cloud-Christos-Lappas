import { Component, signal } from '@angular/core';
import { ProductService } from '../services/product.service';
import { Product } from '../models/product';
import { CartService } from '../services/cart.service';
import { Cart } from '../models/cart';
import { FormsModule } from '@angular/forms';
import { Category } from '../models/category.enum';
import { Score } from '../models/score.enum';

@Component({
  selector: 'app-products',
  standalone: true,
  imports: [FormsModule],
  templateUrl: './products.component.html',
  styleUrl: './products.component.css'
})
export class ProductsComponent {
  products = signal<Product[]>([]);
  searchQuery: string | undefined = undefined;

  categories = Object.values(Category);
  scores = Object.values(Score);

  selectedCategory: Category | undefined = undefined;
  selectedScore: Score | undefined = undefined;

  constructor(private productService: ProductService, private cartService: CartService) {}

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

  addToCart(productId: number) {
    if (this.cartService.cartIsNull()) {
      this.cartService.createCart()
        .subscribe((cart) => {
          if(cart) {
            this.cartService.addItem(productId, cart);
          }
        })
    } else {
      console.log(this.cartService.cart);
      this.cartService.addItem(productId, this.cartService.cart!);      
    }    
  }

  filterProducts() {
    this.productService.searchProducts(this.selectedCategory, this.selectedScore, this.searchQuery, this.searchQuery)
    .subscribe({
      next: (response) => {
        this.products.set(response ?? [])      
      },
      error: (err) => {
        console.error('Failed to fetch products', err)
      }
    });
  }

  resetSearch() {
    this.searchQuery = undefined;
    this.selectedCategory = undefined;
    this.selectedScore = undefined;
    this.fetchProducts();
  }


}
