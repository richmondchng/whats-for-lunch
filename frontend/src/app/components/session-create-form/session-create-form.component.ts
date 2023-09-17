import { Component, Output, EventEmitter, OnInit } from '@angular/core';
import { SessionsService } from 'src/app/services/sessions.service';
import { FormControl } from '@angular/forms';
import { ResponseSessions, UserBody } from 'src/app/interfaces/ResponseDetails';
import { MessageService } from 'src/app/services/message.service';
import { DisplayMessage } from 'src/app/interfaces/DisplayMessage';
import { Session } from 'src/app/interfaces/Session';
import { UserService } from 'src/app/services/user.service';

@Component({
  selector: 'app-session-create-form',
  templateUrl: './session-create-form.component.html',
  styleUrls: ['./session-create-form.component.css'],
})
export class SessionCreateFormComponent implements OnInit {
  startDate = new Date();
  showForm: boolean = true;
  sessionDate = new FormControl<Date>(new Date());
  sessionParticipants = new FormControl<number[]>([]);
  @Output() onCreateSession: EventEmitter<Session> = new EventEmitter();
  participantList: UserBody[] = []

  constructor(private sessionService: SessionsService, private userService: UserService, private messageService: MessageService) {}

  ngOnInit(): void {
    this.participantList.push(
      { id: 1, userName: 'jon', firstName: 'John', lastName: 'Wick' },
      { id: 2, userName: 'indy', firstName: 'Indiana', lastName: 'Jones' },
    )
    
    this.userService.getUsers().subscribe({
      next: (data) => this.participantList = data,
      error: (err) => this.publishError(err)
    });

  }

  toggleForm() {
    this.showForm = !this.showForm;
  }

  createSession() {
    const session: Session = {
      date: this.sessionDate.value as Date,
      participants: this.sessionParticipants.value as number[]
    };
    this.onCreateSession.emit(session);
    this.sessionDate = new FormControl<Date>(new Date());
    this.toggleForm();
  }

  publishError(err: Error) {
    const message : DisplayMessage = {
      message: err.message,
      category: "ERROR"
    }
    this.messageService.addMessage(message);
  }
}
