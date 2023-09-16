import { Component, inject } from '@angular/core';
import { AuthenticationService } from 'src/app/services/authentication.service';
import { Credentials } from 'src/app/interfaces/Credentials';
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';
import {concatMap} from 'rxjs/operators'
import { Me } from 'src/app/interfaces/Me';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent {
  // loginService: LoginService = inject(LoginService);
  credentials: Credentials = {username: "", password: ""};
  title = "Login";

  constructor(private authenticationService: AuthenticationService, private http: HttpClient, private router: Router) {}

  login() {
    // this.loginService.authenticate(this.credentials, () => {
    //   this.router.navigateByUrl('/');
    // });
    // return false;

    this.authenticationService.authenticate(this.credentials).subscribe((response:boolean) => {
      console.log("got token " + response);
      this.router.navigateByUrl('/home');
    });

    // this.loginService.authenticate(this.credentials).pipe(
    //   concatMap((token:string) => {
    //     localStorage.setItem('token', token);
    //     return this.loginService.loadLoggedInUser(this.credentials.username, token);
    //   }),
    //   concatMap((me:Me) => {
    //     localStorage.setItem('me', JSON.stringify(me));
    //     return;
    //   })
    // ).subscribe((response) => {this.router.navigateByUrl('/');});
  }
}
