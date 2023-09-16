import { Component, OnInit } from '@angular/core';
import { SessionsService } from 'src/app/services/sessions.service';
import { Router } from '@angular/router';
import { Me } from 'src/app/interfaces/Me';
import { SessionDetails } from 'src/app/interfaces/SessionDetails';
import { faTimes } from '@fortawesome/free-solid-svg-icons';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})
export class HomeComponent implements OnInit {

  faTimes = faTimes;
  sessions: SessionDetails[] = [];

  constructor(private sessionsService: SessionsService, private router: Router) {}

  ngOnInit(): void {
    this.sessionsService.getSessionForUser().subscribe(
      // sort descending
      (sessions) => (this.sessions = sessions.sort((a, b) => a.sessionDate >= b.sessionDate ? -1 : 1))
    );
  }

  findActiveSessions(data: SessionDetails[]) : SessionDetails[] {
    return data.filter(s => s.status !== 'CLOSED' && s.status !== 'DELETED');
  }

  findClosedSessions(data: SessionDetails[]) : SessionDetails[] {
    return data.filter(s => s.status === 'CLOSED');
  }

  deleteSession(session: SessionDetails) {
    this.sessionsService.deleteSession(session)
      // on success, refresh records
      .subscribe(() => (this.sessions = this.sessions.filter((s) => s.id !== session.id)));
  }
}
