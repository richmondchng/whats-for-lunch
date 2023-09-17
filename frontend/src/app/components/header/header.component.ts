import { Component, Input } from '@angular/core';
import { faBurger } from '@fortawesome/free-solid-svg-icons';
import { Subscription } from 'rxjs';
import { MessageService } from 'src/app/services/message.service';
import { DisplayMessage } from 'src/app/interfaces/DisplayMessage';
import { faTimes } from '@fortawesome/free-solid-svg-icons';

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
  faTimes = faTimes;
  
  constructor(private messageService: MessageService) {
    this.subscription = this.messageService.onMessage()
      .subscribe((value: DisplayMessage) => {
        this.showMessage = true;
        this.message = value;
      });
  }

  ngOnDestroy() {
    // Unsubscribe to ensure no memory leaks
    this.subscription.unsubscribe();
  }

  onCloseMessage() {
    this.showMessage = false;
    this.message.category = 'INFO';
    this.message.message = '';
  }
}
