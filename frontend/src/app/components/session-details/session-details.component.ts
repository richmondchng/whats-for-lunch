import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Params } from '@angular/router';
import { HttpClient } from '@angular/common/http';
import { SessionsService } from 'src/app/services/sessions.service';
import { SessionBody } from 'src/app/interfaces/ResponseDetails';
import { Restaruant } from 'src/app/interfaces/Restaurant';

@Component({
  selector: 'app-session-details',
  templateUrl: './session-details.component.html',
  styleUrls: ['./session-details.component.css']
})
export class SessionDetailsComponent implements OnInit {

  session! : SessionBody;
  restaurant: Restaruant = {name: "", description: ""};

  constructor(private route: ActivatedRoute, private http: HttpClient,
     private sessionService: SessionsService) {}

  ngOnInit(): void {
    this.route.queryParams.subscribe((param:Params) => {
      //console.log(param['id']);
      const sessionId = param['sessionid'];
      this.loadSessionDetails(sessionId);
    });
  }

  loadSessionDetails(sessionId:number) {
    this.sessionService.getSession(sessionId as number).subscribe((result) => this.session = result);
  }

  addRestaurant() {
    console.log("click!");
    this.sessionService.addRestaurantToSession(this.session.id, this.restaurant)
    .subscribe(() => (this.loadSessionDetails(this.session.id)));
    
    
    // .authenticate(this.credentials).subscribe((response:boolean) => {
    //   console.log("got token " + response);
    //   this.router.navigateByUrl('/home');
    // });
  }

}
