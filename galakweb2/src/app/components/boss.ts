import {Component} from 'angular2/core';
import {Http, RequestOptions, Headers, HTTP_PROVIDERS} from 'angular2/http';


@Component({
  selector: 'boss-ctrl',
  viewProviders: [HTTP_PROVIDERS],
  templateUrl: 'templates/boss.html'
})
export class BossCtrl {
   error : string ="";


   bossData =  {
      orderCnt: 0
   };

  constructor(private http: Http) {
      this.bossData.orderCnt = 0;
      setInterval( ()=>this.countOrders(), 5000);
  }

  countOrders() {
    var headers = new Headers();
    headers.append('Content-Type', 'application/json');
    var options = new RequestOptions({
      headers: headers
    });
    this.http.get("/services/countOrders", options)
      .subscribe(res => {
        this.error = "";
        this.bossData.orderCnt = Number(res.text());
      }, error => this.error = JSON.stringify(error));
  }

  inc() {
    this.bossData.orderCnt  =  this.bossData.orderCnt +1;
  }

}

