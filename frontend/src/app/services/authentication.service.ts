import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Credentials } from '../interfaces/Credentials';
import { Me } from '../interfaces/Me';
import { Observable, of } from 'rxjs';
import { concatMap} from 'rxjs/operators'
import { ResponseTokens, ResponseUsers, UserBody } from '../interfaces/ResponseDetails';

@Injectable({
  providedIn: 'root'
})
export class AuthenticationService {

  constructor(private http: HttpClient) { }

  authenticate(credentials: Credentials) : Observable<boolean> {
    const headers = new HttpHeaders({
        'Authorization' : 'Basic ' + btoa(credentials.username + ':' + credentials.password),
        'Content-Type' : 'application/x-www-form-urlencoded'
      });

    return this.http.post<ResponseTokens>('http://localhost:8080/api/v1/tokens', null, {headers : headers})
      .pipe(
        concatMap((response:ResponseTokens) => {
          let token = response.data[0].accessToken;
          localStorage.setItem("token", token);
          const headers = new HttpHeaders({
            'Authorization' : 'Bearer ' + token,
            'Content-Type' : 'application/x-www-form-urlencoded'
          });
          return this.http.get<ResponseUsers>('http://localhost:8080/api/v1/currentuser', {headers : headers});
        }),
        concatMap((response:ResponseUsers) => {
          const data:UserBody = response.data[0];
          const me: Me = {
            userName: data.userName,
            displayName: data.firstName,
            userId: data.id,
          };
          localStorage.setItem("me", JSON.stringify(me));
          return of(true);
        })
      );
  }
}
