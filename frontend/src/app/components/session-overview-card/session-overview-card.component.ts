import { Component, Input, Output, EventEmitter } from '@angular/core';
import { faTimes } from '@fortawesome/free-solid-svg-icons';
import { SessionDetails } from 'src/app/interfaces/SessionDetails';


@Component({
  selector: 'app-session-overview-card',
  templateUrl: './session-overview-card.component.html',
  styleUrls: ['./session-overview-card.component.css']
})
export class SessionOverviewCardComponent {
  @Input() session!: SessionDetails;
  @Output() onDeleteSession: EventEmitter<SessionDetails> = new EventEmitter();
  faTimes = faTimes;

  onDelete(session:SessionDetails) {
    if(confirm("Are you sure you want to delete this session?")) {
      this.onDeleteSession.emit(session);
    }
  }
}
