import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SessionOverviewCardComponent } from './session-overview-card.component';

describe('SessionOverviewCardComponent', () => {
  let component: SessionOverviewCardComponent;
  let fixture: ComponentFixture<SessionOverviewCardComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [SessionOverviewCardComponent]
    });
    fixture = TestBed.createComponent(SessionOverviewCardComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
