import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Params } from '@angular/router';
import { SessionsService } from 'src/app/services/sessions.service';
import { SessionBody } from 'src/app/interfaces/ResponseDetails';
import { Restaruant } from 'src/app/interfaces/Restaurant';
import { concatMap} from 'rxjs/operators';
import { Me } from 'src/app/interfaces/Me';
import { ResponseSelectRestaurant } from 'src/app/interfaces/ResponseDetails';
import { faCalendarDay, faUtensils, faUserGroup, faCheck } from '@fortawesome/free-solid-svg-icons';

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

  constructor(private route: ActivatedRoute, private sessionService: SessionsService) {}

  ngOnInit(): void {
    this.route.queryParams.subscribe((param:Params) => {
      const sessionId = param['sessionid'];
      this.sessionService.getSession(sessionId as number).subscribe((result) => {
        this.session = result;
        if(this.session.status === "ACTIVE") {
          const obj = localStorage.getItem("me");
          if(obj) {
            const me: Me = JSON.parse(obj);
            this.showSelectRestaurantButton = (this.session.owner.userName === me.userName);
          }
          this.showAddRestaurantForm = true;
        }
      })
    });
  }

  loadSessionDetails(sessionId:number) {
    return this.sessionService.getSession(sessionId as number).subscribe((result) => {
      this.session = result;
    });
  }

  addRestaurant() {
    if (!this.restaurant.name) {
      alert('Please add a restaurant');
      return;
    }
    this.sessionService.addRestaurantToSession(this.session.id, this.restaurant)
    .subscribe(() => {
      this.loadSessionDetails(this.session.id);
      this.restaurant.name = "";
      this.restaurant.description = "";
    });
  }

  selectRestaurant() {
    this.sessionService.selectRestaurantForSession(this.session.id).subscribe(
      (response: ResponseSelectRestaurant)=>{
        this.showSelectRestaurantButton = false;
        this.showAddRestaurantForm = false;
        console.log("Selected " + response.data[0].restaurantName);
    });
  }
}
