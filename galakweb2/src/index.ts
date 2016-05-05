import {Component} from 'angular2/core';
import {bootstrap} from 'angular2/platform/browser';
import {Galakweb2} from 'galakweb-2';
import {OrderCtrl} from 'app/components/order';

@Component({
  selector: 'main',
  directives: [Galakweb2, OrderCtrl],
  template: `
    <div>{{title}}<galakweb-2></galakweb-2><order-ctrl></order-ctrl></div>
  `
})



class Main {
  title = 'Tour of Heroes';
  hero = 'Windstorm';
}

bootstrap(Main);
