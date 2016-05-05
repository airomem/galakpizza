import {Component} from 'angular2/core';


@Component({
  selector: 'order-ctrl',
  templateUrl: 'templates/order.html'
})


export class OrderCtrl {
  planet : string;
  size : number =  0;
  variant: string = "hawaii";
  constructor() {

  }

  selectVariant(variant:string) {
    this.variant = variant;
  }

  placeOrder() {
    console.info("ordeering:" + this.size+" @  " + this.variant+ "  on" +  this.planet);
  }

}
