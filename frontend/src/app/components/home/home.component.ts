import { Component, OnInit } from '@angular/core';
import { SessionsService } from 'src/app/services/sessions.service';
import { Router } from '@angular/router';
import { Me } from 'src/app/interfaces/Me';
import { SessionBody } from 'src/app/interfaces/ResponseDetails';
import { faTimes } from '@fortawesome/free-solid-svg-icons';
import { DisplayMessage } from 'src/app/interfaces/DisplayMessage';
import { MessageService } from 'src/app/services/message.service';
import { AuthenticationService } from 'src/app/services/authentication.service';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})
export class HomeComponent implements OnInit {

  faTimes = faTimes;
  sessions: SessionBody[] = [];

  constructor(private sessionsService: SessionsService, private messageService: MessageService, private authenticationService: AuthenticationService, private router: Router) {}

  ngOnInit(): void {
    this.sessionsService.getSessionForUser().subscribe({
      // sort descending
      next: (sessions) => (this.sessions = sessions.sort((a, b) => a.date >= b.date ? -1 : 1)),
      error: (err) => this.publishError(err)
    });
    if(!this.authenticationService.amILoggedIn()) {
      this.router.navigateByUrl('/');
    }
  }

  findActiveSessions(data: SessionBody[]) : SessionBody[] {
    return data.filter(s => s.status !== 'CLOSED' && s.status !== 'DELETED');
  }

  findClosedSessions(data: SessionBody[]) : SessionBody[] {
    return data.filter(s => s.status === 'CLOSED');
  }

  deleteSession(session: SessionBody) {
    this.sessionsService.deleteSession(session)
      // on success, refresh records
      .subscribe({
        next: () => (this.sessions = this.sessions.filter((s) => s.id !== session.id)),
        error: (err) => this.publishError(err)
      });
  }

  publishError(err: Error) {
    const message : DisplayMessage = {
      message: err.message,
      category: "ERROR"
    }
    this.messageService.addMessage(message);
  }
}
