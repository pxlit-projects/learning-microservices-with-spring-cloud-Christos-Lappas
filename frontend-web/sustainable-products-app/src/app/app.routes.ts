import { Routes } from '@angular/router';
import { ProductsComponent } from './products/products.component';
import { CartComponent } from './cart/cart.component';
import { AdminProductsComponent } from './admin-products/admin-products.component';
import { LogsComponent } from './logs/logs.component';

export const routes: Routes = [
    {
        path: 'customer/products',
        component: ProductsComponent
    },
    {
        path: 'customer/cart',
        component: CartComponent
    },
    {
        path: 'admin/products',
        component: AdminProductsComponent
    },
    {
        path: 'admin/logs',
        component: LogsComponent
    },
];
