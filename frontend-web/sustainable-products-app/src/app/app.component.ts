import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Router, RouterModule, RouterOutlet } from '@angular/router';
import { CartService } from './services/cart.service';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterOutlet, RouterModule, FormsModule],
  templateUrl: './app.component.html',
  styleUrl: './app.component.css'
})
export class AppComponent {
  private readonly storageKey = 'userRole';
  username: string = '';
  password: string = '';

  constructor(private router: Router, private cartService: CartService) {}

  setUserRole(): void {
    if (this.username === 'admin' || this.username === 'customer') {
      localStorage.setItem(this.storageKey, this.username);
    } else {
      throw new Error('Invalid role. Role must be either "admin" or "customer".');
    }
  }

  getUserRole(): 'admin' | 'customer' | null {
    const role = localStorage.getItem(this.storageKey);
    return role === 'admin' || role === 'customer' ? role : null;
  }

  clearUserRole(): void {
    localStorage.removeItem('userRole');
  }


  isAdmin(): boolean {
    return this.getUserRole() === 'admin';
  }

  isCustomer(): boolean {
    return this.getUserRole() === 'customer';
  }

  isNothing(): boolean {
    return this.getUserRole() === null;
  }

  login() {
    if(this.username === 'admin') {
      this.setUserRole();
      this.router.navigate(['/admin/products'])
    } else if (this.username === 'customer') {
      this.setUserRole();
      this.router.navigate(['/customer/products'])
    }

  }  

  logout() {
    this.clearUserRole();
    this.router.navigate(['/']);
  }
  
}
