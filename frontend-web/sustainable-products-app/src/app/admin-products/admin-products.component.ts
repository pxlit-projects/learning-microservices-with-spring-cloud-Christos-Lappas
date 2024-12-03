import { Component, signal } from '@angular/core';
import { Product } from '../models/product';
import { ProductService } from '../services/product.service';
import { Category } from '../models/category.enum';
import { Score } from '../models/score.enum';
import { FormsModule } from '@angular/forms';
import * as bootstrap from 'bootstrap';

@Component({
  selector: 'app-admin-products',
  standalone: true,
  imports: [FormsModule],
  templateUrl: './admin-products.component.html',
  styleUrl: './admin-products.component.css'
})
export class AdminProductsComponent {
  products = signal<Product[]>([]);
  categories = Object.values(Category);
  scores = Object.values(Score);
  newProduct: Product = new Product();
  currentProduct: Product = new Product(); 
  newLabel: string = ''; 
  showToast = false;
  toastMessage = '';

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

  addProduct() {
    if(this.newProduct) {
      this.productService.addProduct(this.newProduct)
        .subscribe({
          next: (response) => {
              this.newProduct = new Product();
              const modal = document.getElementById('createProductModal');
              if (modal) {
                const bootstrapModal = bootstrap.Modal.getInstance(modal);
                bootstrapModal?.hide();
              }
              setTimeout(() => {
                this.toastMessage = 'Product successfully created!';
                this.showToast = true;
          
                setTimeout(() => {
                  this.showToast = false;
                }, 3000);
              }, 500);  
              this.fetchProducts();
          },
          error: (err) => {
            console.error('Failed to add product', err)
          }
        });
    }
  }  

  openCreateProductModal(): void {
    const modal = document.getElementById('createProductModal');
    if (modal) {
      const bootstrapModal = new bootstrap.Modal(modal);
      bootstrapModal.show();
    }
  }

  openEditProductModal(product: Product): void {
    this.currentProduct = { ...product };
    const modal = document.getElementById('editProductModal');
    if (modal) {
      const bootstrapModal = new bootstrap.Modal(modal);
      bootstrapModal.show();
    }
  }

  updateProduct() {
    console.log(this.currentProduct);
    this.productService.updateProduct(this.currentProduct.id!, this.currentProduct)
      .subscribe({
        next: (response) => {
          if (response) {
            console.log(response);
            const modal = document.getElementById('editProductModal');
            if (modal) {
              const bootstrapModal = bootstrap.Modal.getInstance(modal);
              bootstrapModal?.hide();
            }
            setTimeout(() => {
              this.toastMessage = 'Product successfully updated!';
              this.showToast = true;
        
              setTimeout(() => {
                this.showToast = false;
              }, 3000);
            }, 500);  
            this.fetchProducts();
          }          
        },
        error: (err) => {
          console.error('Failed to add product', err)
        }
      });
  }

  get labelsList(): string[] {
    return this.currentProduct.labels ? this.currentProduct.labels.split(',') : [];
  }

  addLabel(): void {
    if (this.newLabel && !this.labelsList.includes(this.newLabel)) {      
      this.labelsList.push(this.newLabel);
      this.currentProduct.labels = this.labelsList.join(',');
      this.productService.addLabel(this.currentProduct.id!, this.newLabel)
        .subscribe({
          next: (response) => {
            if(response) {
              console.log(response);
              this.currentProduct = response; 
            }    
          },
          error: (err) => {
            console.error('Failed to fetch products', err)
          }
        });

      this.newLabel = '';
    }
  }

  removeLabel(label: string): void {    
    this.productService.deleteLabel(this.currentProduct.id!, label)
        .subscribe({
          next: (response) => {
            console.log(response);
            if(response) {
              console.log(response);
              this.currentProduct = response; 
            }    
          },
          error: (err) => {
            console.error('Failed to fetch products', err)
          }
        });

    this.labelsList.filter(
      (item) => item !== label
    );
    this.currentProduct.labels = this.labelsList.join(',');
  }

  hideToast() {
    this.showToast = false;
  }

}
