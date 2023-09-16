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
  data: SessionDetails[] = [];

  constructor(private sessionsService: SessionsService, private router: Router) {
    console.log("hello");
  }

  ngOnInit(): void {
    console.log("init");
    // let token = localStorage.getItem("token");
    // if(!token) {
    //    this.router.navigateByUrl('/login');
    // } else {
    this.sessionsService.getSessionForUser().subscribe((sessions) => (this.data = sessions));

    // this.authenticationService.loadLoggedInUser(token as string).subscribe((response:Me) => {
    //     console.log(JSON.stringify(response));
    //     // localStorage.setItem("user", JSON.stringify(response));
    //   })
    //}
  }
}
