import { Component, Input, Output, EventEmitter, OnInit } from '@angular/core';
import { faTimes } from '@fortawesome/free-solid-svg-icons';
import { SessionBody } from 'src/app/interfaces/ResponseDetails';
import { Me } from 'src/app/interfaces/Me';
import { Router } from '@angular/router';

@Component({
  selector: 'app-session-overview-card',
  templateUrl: './session-overview-card.component.html',
  styleUrls: ['./session-overview-card.component.css']
})
export class SessionOverviewCardComponent implements OnInit {

  @Input() session!: SessionBody;
  @Output() onDeleteSession: EventEmitter<SessionBody> = new EventEmitter();
  faTimes = faTimes;
  sessionOwner: boolean = false;

  constructor(private router:Router) {}

  ngOnInit(): void {
    const obj = localStorage.getItem("me")
    if(obj) {
      const me : Me = JSON.parse(obj);
      this.sessionOwner = (this.session.owner.id ===  me.userId);
    }
  }

  onDelete(session:SessionBody) {
    if(confirm("Are you sure you want to delete this session?")) {
      this.onDeleteSession.emit(session);
    }
  }

  onClick(session:SessionBody) {
    this.router.navigate(["/details"], { queryParams: {sessionid : session.id}});
  }
}
