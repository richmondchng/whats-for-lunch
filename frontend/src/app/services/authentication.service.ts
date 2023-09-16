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

  // authenticate2(credentials: Credentials) : Observable<string> {
  //   const headers = new HttpHeaders({
  //       'Authorization' : 'Basic ' + btoa(credentials.username + ':' + credentials.password),
  //       'Content-Type' : 'application/x-www-form-urlencoded'
  //     });

  //   return this.http.post<ResponseToken>('http://localhost:8080/api/v1/tokens', null, {headers : headers})
  //     .pipe(map((response) => response.data[0].accessToken));

    // .pipe(map((response) => response.data[0].accessToken))
  
      // .subscribe(response => {
      //     console.log("login success");
      //     localStorage.setItem('token', response.data[0].accessToken);
      //     return callback && callback();
      //   });


      // return this.http.post<ResponseToken>('http://localhost:8080/api/v1/tokens', null, {headers : headers}).pipe(
      //   concatMap((result1: ResponseToken) => result1.data[0].accessToken),
      //   concatMap((result2:string) => {
      //     const headers = new HttpHeaders({
      //       'Authorization' : 'Bearer ' + result2,
      //       'Content-Type' : 'application/json'
      //     });
      //     // result2 is the response from the second request
      //     return this.http.get<ResponseUsers>('http://localhost:8080/api/v1/users/' + credentials.username, {headers : headers});
      //   }),
      //   concatMap((result3:ResponseUsers) => {
      //     const data:ResponseUser = result3.data[0];
      //     const myObject: Me = {
      //       token: result2,
      //       userName: data.userName,
      //       displayName: data.firstName,
      //       userId: data.id,
      //     };
      //     return myObject;
      //   })
      // );

  // }

  // loadLoggedInUser(token:string) : Observable<Me> {
  //   const headers = new HttpHeaders({
  //     'Authorization' : 'Bearer ' + token,
  //     'Content-Type' : 'application/json'
  //   });
  //   return this.http.get<ResponseUsers>('http://localhost:8080/api/v1/currentuser', {headers : headers})
  //   .pipe(map((response:ResponseUsers) => {
  //     const data:ResponseUser = response.data[0];
  //     const myObject: Me = {
  //       userName: data.userName,
  //       displayName: data.firstName,
  //       userId: data.id,
  //     };
  //     return myObject;
  //     }));
  // }


  // // getTasks(): Observable<Task[]> {
  // //   return this.http.get<Task[]>(this.apiUrl);
  // // }


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

    // .pipe(map((response) => response.data[0].accessToken))
  
      // .subscribe(response => {
      //     console.log("login success");
      //     localStorage.setItem('token', response.data[0].accessToken);
      //     return callback && callback();
      //   });


      // return this.http.post<ResponseToken>('http://localhost:8080/api/v1/tokens', null, {headers : headers}).pipe(
      //   concatMap((result1: ResponseToken) => result1.data[0].accessToken),
      //   concatMap((result2:string) => {
      //     const headers = new HttpHeaders({
      //       'Authorization' : 'Bearer ' + result2,
      //       'Content-Type' : 'application/json'
      //     });
      //     // result2 is the response from the second request
      //     return this.http.get<ResponseUsers>('http://localhost:8080/api/v1/users/' + credentials.username, {headers : headers});
      //   }),
      //   concatMap((result3:ResponseUsers) => {
      //     const data:ResponseUser = result3.data[0];
      //     const myObject: Me = {
      //       token: result2,
      //       userName: data.userName,
      //       displayName: data.firstName,
      //       userId: data.id,
      //     };
      //     return myObject;
      //   })
      // );

  }
}
