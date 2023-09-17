import { Injectable } from '@angular/core';
import { Observable, Subject } from 'rxjs';
import { DisplayMessage } from '../interfaces/DisplayMessage';

@Injectable({
  providedIn: 'root'
})
export class MessageService {

  private subject = new Subject<DisplayMessage>();

  constructor() { }

  addMessage(message: DisplayMessage): void {
    this.subject.next(message);
  }

  onMessage(): Observable<DisplayMessage> {
    return this.subject.asObservable();
  }
}
