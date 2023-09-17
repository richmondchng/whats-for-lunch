import { Component, Input } from '@angular/core';
import { faBurger } from '@fortawesome/free-solid-svg-icons';
import { Subscription } from 'rxjs';
import { MessageService } from 'src/app/services/message.service';
import { AuthenticationService } from 'src/app/services/authentication.service';
import { DisplayMessage } from 'src/app/interfaces/DisplayMessage';
import { faTimes } from '@fortawesome/free-solid-svg-icons';
import { Me } from 'src/app/interfaces/Me';
import { Router } from '@angular/router';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.css']
})
export class HeaderComponent {

  @Input() title: string = "";
  faBurger = faBurger;
  showMessage: boolean = false;
  message: DisplayMessage = { message:'You got mail', category:'INFO'}
  subscription: Subscription;
  loginSubscription: Subscription;
  faTimes = faTimes;
  me?: Me;
  
  constructor(private messageService: MessageService, private authenticationService: AuthenticationService, private router: Router) {
    this.subscription = this.messageService.onMessage()
      .subscribe((value: DisplayMessage) => {
        this.showMessage = true;
        this.message = value;
        setTimeout(() => {
          this.showMessage = false;
        }, 5000);
      });
    this.loginSubscription = this.authenticationService.onLogin().subscribe((response:Me) => {
      this.me = response;
    });
    this.me = this.authenticationService.amILoggedIn();
  }

  ngOnDestroy() {
    // Unsubscribe to ensure no memory leaks
    this.subscription.unsubscribe();
    this.loginSubscription.unsubscribe();
  }

  onCloseMessage() {
    this.showMessage = false;
    this.message.category = 'INFO';
    this.message.message = '';
  }

  onLogout() {
    this.authenticationService.logout();
    this.router.navigateByUrl('/');
    this.me = undefined;
  }


}
