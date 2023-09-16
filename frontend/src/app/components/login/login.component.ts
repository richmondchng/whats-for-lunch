import { Component, inject } from '@angular/core';
import { AuthenticationService } from 'src/app/services/authentication.service';
import { Credentials } from 'src/app/interfaces/Credentials';
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent {
  credentials: Credentials = {username: "", password: ""};
  title = "Login";

  constructor(private authenticationService: AuthenticationService, private http: HttpClient, private router: Router) {}

  login() {
    this.authenticationService.authenticate(this.credentials).subscribe((response:boolean) => {
      console.log("got token " + response);
      this.router.navigateByUrl('/home');
    });
  }
}
