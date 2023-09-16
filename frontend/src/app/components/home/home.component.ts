import { Component, OnInit } from '@angular/core';
import { SessionsService } from 'src/app/services/sessions.service';
import { Router } from '@angular/router';
import { Me } from 'src/app/interfaces/Me';
import { SessionBody } from 'src/app/interfaces/ResponseDetails';
import { faTimes } from '@fortawesome/free-solid-svg-icons';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})
export class HomeComponent implements OnInit {

  faTimes = faTimes;
  sessions: SessionBody[] = [];

  constructor(private sessionsService: SessionsService, private router: Router) {}

  ngOnInit(): void {
    this.sessionsService.getSessionForUser().subscribe(
      // sort descending
      (sessions) => (this.sessions = sessions.sort((a, b) => a.date >= b.date ? -1 : 1))
    );
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
      .subscribe(() => (this.sessions = this.sessions.filter((s) => s.id !== session.id)));
  }
}
