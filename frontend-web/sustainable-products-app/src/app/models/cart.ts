import { Product } from "./product";

export class Cart {
    id: number | null = null;
    customer: string = ''; 
    items: Product[] = []; 
    total: number | null = null;
    ordered: boolean = false; 
  }