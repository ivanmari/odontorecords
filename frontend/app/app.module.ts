import { NgModule, Directive, HostBinding, Input } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { HttpClientModule } from '@angular/common/http';
import { FormsModule } from '@angular/forms';
import { AppComponent } from './app.component';
import { PatientDetails } from './patient-details.component';
import { ToothDetails } from './tooth-details.component';
import { PatientSelect } from './patient-select.component';
import { PracticeEdit } from './practice-edit.component';
import { Mouth } from './mouth.component';
import { MatButtonModule } from '@angular/material/button';
import { MatCardModule } from '@angular/material/card';
import { MatInputModule } from '@angular/material/input';
import { MatListModule } from '@angular/material/list';
import { MatToolbarModule } from '@angular/material/toolbar';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatDatepickerModule } from '@angular/material/datepicker';
import { MatNativeDateModule } from '@angular/material/core';
import { MatSnackBarModule } from '@angular/material/snack-bar';
import { MatSidenavModule } from '@angular/material/sidenav';
import { MatIconModule } from '@angular/material/icon';
import { MatTabsModule } from '@angular/material/tabs';
import { MatGridListModule } from '@angular/material/grid-list';

// Layout Directives
@Directive({
  selector: '[layout]'
})
export class LayoutDirective {
  @Input() layout: string;
  @HostBinding('style.display') display = 'flex';

  @HostBinding('style.flex-direction')
  get direction() {
    return (this.layout === 'column') ? 'column' : 'row';
  }
}

@Directive({
  selector: '[flex]'
})
export class FlexDirective {
  @Input() shrink: number = 1;
  @Input() grow: number = 1;
  @Input() flex: string;

  @HostBinding('style.flex')
  get style() {
    return `${this.grow} ${this.shrink} ${this.flex === '' ? '0' : this.flex}%`;
  }
}

@NgModule({
  imports: [
    BrowserModule,
    BrowserAnimationsModule,
    HttpClientModule,
    FormsModule,
    MatButtonModule,
    MatCardModule,
    MatInputModule,
    MatListModule,
    MatToolbarModule,
    MatFormFieldModule,
    MatDatepickerModule,
    MatNativeDateModule,
    MatSnackBarModule,
    MatSidenavModule,
    MatIconModule,
    MatTabsModule,
    MatGridListModule
  ],
  declarations: [
    AppComponent,
    PatientSelect,
    PatientDetails,
    ToothDetails,
    PracticeEdit,
    Mouth,
    FlexDirective,
    LayoutDirective
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
