import { Component, inject } from '@angular/core';
import { LoginService } from 'src/app/services/login.service';
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

  constructor(private loginService: LoginService, private http: HttpClient, private router: Router) {}

  login() {
    // this.loginService.authenticate(this.credentials, () => {
    //   this.router.navigateByUrl('/');
    // });
    // return false;

    this.loginService.authenticate(this.credentials).subscribe(response => {
      console.log("got token " + response);
      localStorage.setItem('token', response);
      this.router.navigateByUrl('/');
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
