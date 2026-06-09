import { NgModule,Directive,HostBinding,Input }      from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { HttpModule, JsonpModule } from '@angular/http';
import { FormsModule }   from '@angular/forms';
import { AppComponent } from './app.component'
import { PatientDetails } from './patient-details.component'
import { ToothDetails } from './tooth-details.component'
import { PatientSelect} from './patient-select.component'
import { PracticeEdit } from './practice-edit.component'
import { Mouth } from './mouth.component'
import { MaterialModule } from '@angular/material';

//Added for table configuration
@Directive({
  selector:'[layout]'
})
export class LayoutDirective{
  @Input() layout:string;
  @HostBinding('style.display') display = 'flex';

  @HostBinding('style.flex-direction')
  get direction(){
       return (this.layout === 'column') ? 'column':'row';
  }
}
@Directive({
  selector:'[flex]'
})
export class FlexDirective{
    @Input() shrink:number = 1;
    @Input() grow:number = 1;
    @Input() flex:string;

    @HostBinding('style.flex')
    get style(){
        return `${this.grow} ${this.shrink} ${this.flex === '' ? '0':this.flex}%`;
    }
}

@NgModule({
  imports:      [ BrowserModule, HttpModule, JsonpModule, FormsModule, MaterialModule],
  declarations: [ AppComponent, PatientSelect, PatientDetails, ToothDetails, PracticeEdit, Mouth, FlexDirective, LayoutDirective ],
  bootstrap:    [ AppComponent ]
})
export class AppModule { }
