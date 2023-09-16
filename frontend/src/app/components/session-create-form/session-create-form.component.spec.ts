import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SessionCreateFormComponent } from './session-create-form.component';

describe('SessionCreateFormComponent', () => {
  let component: SessionCreateFormComponent;
  let fixture: ComponentFixture<SessionCreateFormComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [SessionCreateFormComponent]
    });
    fixture = TestBed.createComponent(SessionCreateFormComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
