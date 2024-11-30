import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { RouterOutlet } from '@angular/router';
import { CustomerComponent } from './customer/customer.component';
import { AdminComponent } from './admin/admin.component';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterOutlet, FormsModule, CustomerComponent, AdminComponent],
  templateUrl: './app.component.html',
  styleUrl: './app.component.css'
})
export class AppComponent {
  private readonly storageKey = 'userRole';
  username: string = '';
  password: string = '';

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


  isAdmin(): boolean {
    return this.getUserRole() === 'admin';
  }

  isCustomer(): boolean {
    return this.getUserRole() === 'customer';
  }
  
}
