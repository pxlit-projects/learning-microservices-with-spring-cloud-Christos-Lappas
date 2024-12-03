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

  showToast = false;
  toastMessage = '';

  selectedPriceRange: [number, number] = [0, 200];

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
            setTimeout(() => {
              this.toastMessage = 'Product successfully added to cart!';
              this.showToast = true;
        
              setTimeout(() => {
                this.showToast = false;
              }, 3000);
            }, 500);
          }
        })
    } else { 
      this.cartService.addItem(productId, this.cartService.cart!);   
      setTimeout(() => {
        this.toastMessage = 'Product successfully added to cart!';
        this.showToast = true;
  
        setTimeout(() => {
          this.showToast = false;
        }, 3000);
      }, 500);  
    }    
  }

  filterProducts() {
    this.productService.searchProducts(this.selectedCategory, this.selectedScore, this.searchQuery, this.searchQuery, this.selectedPriceRange[1])
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
    this.selectedPriceRange = [0, 200];
    this.fetchProducts();
  }

  hideToast() {
    this.showToast = false;
  }

}
