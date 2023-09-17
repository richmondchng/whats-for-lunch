import { HttpClient, HttpErrorResponse, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Credentials } from '../interfaces/Credentials';
import { Me } from '../interfaces/Me';
import { Observable, of, throwError, Subject } from 'rxjs';
import { catchError, concatMap} from 'rxjs/operators'
import { ResponseTokens, ResponseUsers, UserBody } from '../interfaces/ResponseDetails';
import { environment } from 'src/environments/environment';

@Injectable({
  providedIn: 'root'
})
export class AuthenticationService {

  private subject = new Subject<Me>();

  constructor(private http: HttpClient) { }

  onLogin(): Observable<Me> {
    return this.subject.asObservable();
  }

  authenticate(credentials: Credentials) : Observable<boolean> {
    const headers = new HttpHeaders({
        'Authorization' : 'Basic ' + btoa(credentials.username + ':' + credentials.password),
        'Content-Type' : 'application/x-www-form-urlencoded'
      });
    const url = `${environment.apiUrl}/tokens`;
    return this.http.post<ResponseTokens>(url, null, {headers : headers})
      .pipe(
        concatMap((response:ResponseTokens) => {
          let token = response.data[0].accessToken;
          localStorage.setItem("token", token);
          const headers = new HttpHeaders({
            'Authorization' : 'Bearer ' + token,
            'Content-Type' : 'application/x-www-form-urlencoded'
          });
          const url = `${environment.apiUrl}/currentuser`;
          return this.http.get<ResponseUsers>(url, {headers : headers});
        }),
        concatMap((response:ResponseUsers) => {
          const data:UserBody = response.data[0];
          const me: Me = {
            userName: data.userName,
            displayName: data.firstName,
            userId: data.id,
          };
          localStorage.setItem("me", JSON.stringify(me));
          this.subject.next(me);
          return of(true);
        }),
        catchError(this.handleError)
      );
  }

  logout() {
    localStorage.removeItem("me");
    localStorage.removeItem("token");
  }

  amILoggedIn() : Me | undefined {
    const obj = localStorage.getItem("me");
    if(obj) {
      return JSON.parse(obj) as Me;
    } else {
      return undefined;
    }
  }

  private handleError(error: HttpErrorResponse) {
    if (error.status === 0) {
      // A client-side or network error occurred. Handle it accordingly.
      console.error('An error occurred:', error.error);
    } else {
      // The backend returned an unsuccessful response code.
      // The response body may contain clues as to what went wrong.
      console.error(`Backend returned code ${error.status}, body was: `, error.error);
    }
    // Return an observable with a user-facing error message.
    return throwError(() => new Error('Something bad happened; please try again later.'));
  }
}
