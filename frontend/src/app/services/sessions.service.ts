import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable, pipe, of } from 'rxjs';
import { concatMap, map } from 'rxjs/operators';
import { SessionDetails } from '../interfaces/SessionDetails';
import { ResponseSessions, SessionBody, ResponseDeleteSession, DeleteSessionBody } from '../interfaces/ResponseDetails';


@Injectable({
  providedIn: 'root'
})
export class SessionsService {

  constructor(private http: HttpClient) { }

  getSessionForUser() : Observable<SessionDetails[]> {

      const headers = new HttpHeaders({
        'Authorization' : 'Bearer ' + localStorage.getItem("token"),
        'Content-Type' : 'application/x-www-form-urlencoded'
      });
      return this.http.get<ResponseSessions>('http://localhost:8080/api/v1/sessions', {headers: headers})
      .pipe(
        map((response:ResponseSessions) => {
          const results: SessionDetails[] = response.data.map((item) => {
            const obj: SessionDetails = {
              id: item.id,
              sessionDate: item.date,
              ownerName: item.owner.displayName,
              ownerId: item.owner.id,
              status: item.status,
              selectedRestaurant: item.selectedRestaurant,
            }
            return obj;
          });
          return results;
        })
      );
  }

  deleteSession(session: SessionDetails): Observable<ResponseDeleteSession> {
    const headers = new HttpHeaders({
      'Authorization' : 'Bearer ' + localStorage.getItem("token"),
      'Content-Type' : 'application/x-www-form-urlencoded'
    });
    const url = "http://localhost:8080/api/v1/sessions/" + session.id; //`${this.apiUrl}/${task.id}`;
    return this.http.delete<ResponseDeleteSession>(url, {headers: headers});
  }
}
