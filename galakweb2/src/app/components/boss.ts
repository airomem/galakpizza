import {Component} from 'angular2/core';
import {Http, RequestOptions, Headers, HTTP_PROVIDERS} from 'angular2/http';



@Component({
  selector: 'boss-ctrl',
  viewProviders: [HTTP_PROVIDERS],
  templateUrl: 'templates/boss.html'
})
export class BossCtrl {

   orderCnt: Number;

  constructor(private http: Http) {
      this.orderCnt = 0;
      setInterval( ()=>this.countOrders(), 1000);
  }

  countOrders() {
    var headers = new Headers();
    headers.append('Content-Type', 'application/json');
    var options = new RequestOptions({
      headers: headers
    });
    this.http.get("/services/countOrders", options)
      .subscribe(res => {
        this.orderCnt = Number(res.text());
      });
  }


}

