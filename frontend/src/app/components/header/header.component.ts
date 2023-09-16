import { Component, Input } from '@angular/core';
import { faBurger } from '@fortawesome/free-solid-svg-icons';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.css']
})
export class HeaderComponent {

  @Input() title: string = "";
  faBurger = faBurger;
}
