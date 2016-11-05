import {Component} from 'angular2/core';
import {bootstrap} from 'angular2/platform/browser';
import {Http, RequestOptions, Headers, HTTP_PROVIDERS} from 'angular2/http';

@Component({
  selector: 'debug',
  viewProviders: [HTTP_PROVIDERS],
  directives: [],
  templateUrl: 'templates/debug.html'
})

class Debug {
  orderCnt: Number;
  orders:any

  constructor(private http: Http) {
    this.orderCnt = 0;
    setInterval( ()=>this.countOrders(), 1000);
    setInterval( ()=>this.getOrders(), 2000);
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
  getOrders() {
    var headers = new Headers();
    headers.append('Content-Type', 'application/json');
    var options = new RequestOptions({
      headers: headers
    });
    this.http.get("/services/debugOrders", options)
      .subscribe(res => {
        this.orders = res.json();
      });
  }
}

bootstrap(Debug);
