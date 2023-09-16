import { Component, Input, Output, EventEmitter, OnInit } from '@angular/core';
import { faTimes } from '@fortawesome/free-solid-svg-icons';
import { SessionDetails } from 'src/app/interfaces/SessionDetails';
import { Me } from 'src/app/interfaces/Me';

@Component({
  selector: 'app-session-overview-card',
  templateUrl: './session-overview-card.component.html',
  styleUrls: ['./session-overview-card.component.css']
})
export class SessionOverviewCardComponent implements OnInit {

  @Input() session!: SessionDetails;
  @Output() onDeleteSession: EventEmitter<SessionDetails> = new EventEmitter();
  faTimes = faTimes;
  sessionOwner: boolean = false;

  ngOnInit(): void {
    const obj = localStorage.getItem("me")
    if(obj) {
      const me : Me = JSON.parse(obj);
      this.sessionOwner = (this.session.ownerId ===  me.userId);
    }
  }

  onDelete(session:SessionDetails) {
    if(confirm("Are you sure you want to delete this session?")) {
      this.onDeleteSession.emit(session);
    }
  }
}
