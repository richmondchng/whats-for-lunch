import { Injectable } from '@angular/core';
import { HttpClient, HttpErrorResponse, HttpHeaders } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { map, catchError } from 'rxjs/operators'
import { ResponseUsers, UserBody } from '../interfaces/ResponseDetails';
import { environment } from 'src/environments/environment';

@Injectable({
  providedIn: 'root'
})
export class UserService {

  constructor(private http: HttpClient) { }

  getUsers(): Observable<UserBody[]> {

    const url = `${environment.apiUrl}/users`;
    const headers = new HttpHeaders({
      'Authorization' : 'Bearer ' + localStorage.getItem("token"),
      'Content-Type' : 'application/json'
    });
    return this.http.get<ResponseUsers>(url, {headers: headers})
    .pipe(
      map((response:ResponseUsers) => {
        return response.data;
      }),
      catchError(this.handleError)
    );
  }

  private handleError(error: HttpErrorResponse) {
    let msg = 'Something bad happened; please try again later.';
    if (error.status === 0) {
      // A client-side or network error occurred. Handle it accordingly.
      console.error('An error occurred:', error.error);
    } else {
      // The backend returned an unsuccessful response code.
      // The response body may contain clues as to what went wrong.
      console.error(`Backend returned code ${error.status}, body was: `, error.error);
      if(error.status === 401) {
        msg = 'Token has expired, please log in again';
      }
    }
    // Return an observable with a user-facing error message.
    return throwError(() => new Error(msg));
  }
}
