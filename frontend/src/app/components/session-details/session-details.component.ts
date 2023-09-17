import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Params, Router } from '@angular/router';
import { SessionsService } from 'src/app/services/sessions.service';
import { SessionBody } from 'src/app/interfaces/ResponseDetails';
import { Restaruant } from 'src/app/interfaces/Restaurant';
import { concatMap} from 'rxjs/operators';
import { Me } from 'src/app/interfaces/Me';
import { ResponseSelectRestaurant } from 'src/app/interfaces/ResponseDetails';
import { faCalendarDay, faUtensils, faUserGroup, faCheck } from '@fortawesome/free-solid-svg-icons';
import { DisplayMessage } from 'src/app/interfaces/DisplayMessage';
import { MessageService } from 'src/app/services/message.service';
import { AuthenticationService } from 'src/app/services/authentication.service';

@Component({
  selector: 'app-session-details',
  templateUrl: './session-details.component.html',
  styleUrls: ['./session-details.component.css']
})
export class SessionDetailsComponent implements OnInit {

  session : SessionBody = {
    id: 0,
    date: new Date(),
    owner: {id: 0, userName: "", displayName: ""},
    participants: [],
    restaurants: [],
    selectedRestaurant: 0,
    status: ""
  };
  restaurant: Restaruant = {name: "", description: ""};
  showSelectRestaurantButton: boolean = false;
  showAddRestaurantForm: boolean = false;
  faCalendarDay = faCalendarDay;
  faCheck = faCheck;
  faUtensils = faUtensils;
  faUserGroup = faUserGroup;

  constructor(private route: ActivatedRoute, private router: Router, private sessionService: SessionsService, private messageService: MessageService, private authenticationService: AuthenticationService) {}

  ngOnInit(): void {
    this.route.queryParams.subscribe({
      next: (param:Params) => {
        const sessionId = param['sessionid'];
        this.sessionService.getSession(sessionId as number).subscribe({
          next: (result) => {
            this.session = result;
            if(this.session.status === "ACTIVE") {
              const obj = localStorage.getItem("me");
              if(obj) {
                const me: Me = JSON.parse(obj);
                this.showSelectRestaurantButton = (this.session.owner.userName === me.userName);
              }
              this.showAddRestaurantForm = true;
            }
          },
          error: (err) => {
            this.publishError(err);
          }
        });
      },
      error: (err) => this.publishError(err)
    });
    if(!this.authenticationService.amILoggedIn()) {
      this.router.navigateByUrl('/');
    }
  }

  loadSessionDetails(sessionId:number) {
    return this.sessionService.getSession(sessionId as number).subscribe({
      next: (result) => {
        this.session = result;
      },
      error: (err) => this.publishError(err)
    });
  }

  addRestaurant() {
    if (!this.restaurant.name) {
      alert('Please add a restaurant');
      return;
    }
    this.sessionService.addRestaurantToSession(this.session.id, this.restaurant)
    .subscribe({
      next: () => {
        this.loadSessionDetails(this.session.id);
        this.restaurant.name = "";
        this.restaurant.description = "";
      },
      error: (err) => this.publishError(err)
    });
  }

  selectRestaurant() {
    this.sessionService.selectRestaurantForSession(this.session.id).subscribe({
      next: (response: ResponseSelectRestaurant)=>{
        this.showSelectRestaurantButton = false;
        this.showAddRestaurantForm = false;
        // update UI
        this.session.selectedRestaurant = response.data[0].restaurantId;
        // post message
        const message : DisplayMessage = {
          message: `Decided! We are going to  ${response.data[0].restaurantName}`,
          category: "INFO"
        }
        this.messageService.addMessage(message);
      },
      error: (err) => this.publishError(err)
    });
  }

  publishError(err: Error) {
    console.debug("Caught error " + err);
    const message : DisplayMessage = {
      message: err.message,
      category: "ERROR"
    }
    this.messageService.addMessage(message);
  }

}
