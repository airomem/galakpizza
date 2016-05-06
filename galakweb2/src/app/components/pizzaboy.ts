import {Component} from 'angular2/core';
import {Http, RequestOptions, Headers, HTTP_PROVIDERS} from 'angular2/http';

class Order {
  public planet : string;
}

class DeliveryInfo {
   constructor ( public planet :String, public cnt:  Number) {

   }
}

@Component({
  selector: 'pizzaboy-ctrl',
  viewProviders: [HTTP_PROVIDERS],
  templateUrl: 'templates/pizzaboy.html'
})
export class PizzaboyCtrl {
   deliveredOrders : Array<Order>;

   delivery: DeliveryInfo;

  constructor(private http: Http) {
      this.delivery = new DeliveryInfo("", 0);
  }

  takeOrders() {
    var headers = new Headers();
    headers.append('Content-Type', 'application/json');
    var options = new RequestOptions({
      headers: headers
    });
    this.http.post("/services/takeOrders", JSON.stringify({} ), options)
      .subscribe(res => {
        let result = res.json();
        console.log("got:" + result);
        this.storeDelivery(result);
      });
  }

  private storeDelivery( orders : Array<Order>) {
    this.deliveredOrders = orders;
    if ( orders.length > 0 ) {
      this.delivery = new DeliveryInfo(orders[0].planet ,orders.length);
    } else {
      this.delivery = new DeliveryInfo("",0);
    }
  }

}

