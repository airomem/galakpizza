import {Component} from 'angular2/core';
import {Http, RequestOptions, Headers, HTTP_PROVIDERS} from 'angular2/http';
import {BossCtrl} from "./boss";
import 'rxjs/add/operator/map';
import 'rxjs/add/operator/catch';


@Component({
  selector: 'order-ctrl',
  viewProviders: [HTTP_PROVIDERS, BossCtrl],
  templateUrl: 'templates/order.html'
})
export class OrderCtrl {

  error: string = "";
  planet : string;
  size : number =  0;
  variant: string = "HAWAII";
  constructor(private http: Http, private boss: BossCtrl) {

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
    this.error = "";

    this.http.post("/services/order", JSON.stringify({ "planet" : this.planet, "variant" : this.variant, "size": "XL"} ), options)
      .subscribe(res => console.log("got:" + res), error=>{
        console.log("ERROR:"+error)
        this.error = JSON.stringify(error);
      });


    this.boss.inc();

  }

  private getSize(sz: Number) {
    console.log("size is"+ sz);
    switch (Number(sz)) {
      case 0 : return "MEDIUM";
      case 1 : return "LARGE";
      case 2 : return "XL";
    }
    return "ook!";
  }

}
