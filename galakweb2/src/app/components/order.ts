import {Component} from 'angular2/core';
import {Http, RequestOptions, Headers, HTTP_PROVIDERS} from 'angular2/http';


@Component({
  selector: 'order-ctrl',
  viewProviders: [HTTP_PROVIDERS],
  templateUrl: 'templates/order.html'
})
export class OrderCtrl {

 
  planet : string;
  size : number =  0;
  variant: string = "HAWAII";
  constructor(private http: Http) {

  }

  selectVariant(variant:string) {
    this.variant = variant;
  }

  placeOrder() {
    var headers = new Headers();
    headers.append('Content-Type', 'application/json');
    var options = new RequestOptions({
         headers: headers
       });
    console.info("ordering:" + this.size+" @  " + this.variant+ "  on" +  this.planet);
    this.http.post("/services/order", JSON.stringify({ "planet" : this.planet, "variant" : this.variant, "size": "XL"} ), options)
      .subscribe(res => console.log("got:" + res));
  }

}
