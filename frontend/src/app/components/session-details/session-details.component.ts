import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Params } from '@angular/router';
import { HttpClient } from '@angular/common/http';
import { SessionsService } from 'src/app/services/sessions.service';
import { SessionBody } from 'src/app/interfaces/ResponseDetails';

@Component({
  selector: 'app-session-details',
  templateUrl: './session-details.component.html',
  styleUrls: ['./session-details.component.css']
})
export class SessionDetailsComponent implements OnInit {

  session! : SessionBody;

  constructor(private route: ActivatedRoute, private http: HttpClient,
     private sessionService: SessionsService) {}

  ngOnInit(): void {
    this.route.queryParams.subscribe((param:Params) => {
      //console.log(param['id']);
      const sessionId = param['sessionid'];
      this.sessionService.getSession(sessionId as number).subscribe((result) => this.session = result);
    });
  }

}
