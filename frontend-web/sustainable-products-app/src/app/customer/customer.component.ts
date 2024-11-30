import { Component } from '@angular/core';
import { Router, RouterModule } from '@angular/router';

@Component({
  selector: 'app-customer',
  standalone: true,
  imports: [RouterModule],
  templateUrl: './customer.component.html',
  styleUrl: './customer.component.css'
})
export class CustomerComponent {

  constructor(private router: Router) {}

  

}
