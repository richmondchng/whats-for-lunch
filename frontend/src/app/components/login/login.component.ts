import { Component, Output, EventEmitter } from '@angular/core';
import { AuthenticationService } from 'src/app/services/authentication.service';
import { Credentials } from 'src/app/interfaces/Credentials';
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';
import { DisplayMessage } from 'src/app/interfaces/DisplayMessage';
import { MessageService } from 'src/app/services/message.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent {
  // @Output() onLoginError: EventEmitter<DisplayMessage> = new EventEmitter();
  credentials: Credentials = {username: "", password: ""};
  title = "Login";

  constructor(private authenticationService: AuthenticationService, private messageService: MessageService, private http: HttpClient, private router: Router) {}

  login() {
    this.authenticationService.authenticate(this.credentials).subscribe((response:boolean) => {
      console.log("got token " + response);
      this.router.navigateByUrl('/home');
    }, ((err) => {
      console.log("Caught error " + err);
      const message : DisplayMessage = {
        message: err,
        category: "ERROR"
      }
      this.messageService.addMessage(message);
    }));
  }
}
