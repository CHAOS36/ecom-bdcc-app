import {Component, OnInit} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {ActivatedRoute} from '@angular/router';

@Component({
  selector: 'app-order-details',
  standalone: false,

  templateUrl: './order-details.component.html',
  styleUrl: './order-details.component.css'
})
export class OrderDetailsComponent implements OnInit{
  orderId : string;
  orderDetails : any;
  //j'ai besoin de recuperer id a l'aide de activated route
  constructor(private route : ActivatedRoute, private http : HttpClient) {
    this.orderId = this.route.snapshot.params['id'];
  }
  ngOnInit() {
    this.http.get("http://localhost:8082/api/orders/"+this.orderId).subscribe({
      next : order => {
        this.orderDetails = order
      },
      error : err => {
        console.log(err);
      }
    })
  }

  getTotal(orderDetails: any) {
    let total : number = 0;
    for (let pi of orderDetails.productItems){
      total = total + (pi.price * pi.quantity);
    }
    return total;
  }
}
