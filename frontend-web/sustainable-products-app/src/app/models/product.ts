import { Category } from "./category.enum";
import { Score } from "./score.enum";

export class Product {
    id: number | null = null; 
    name: string = ''; 
    description: string = ''; 
    price: number = 0; 
    category: Category | null = null;
    score: Score | null = null; 
    labels: string = ''; 
    quantity: number = 1; 
  }