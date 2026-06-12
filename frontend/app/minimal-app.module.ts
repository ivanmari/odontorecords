import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { MinimalAppComponent } from './minimal-app.component';

@NgModule({
  imports: [BrowserModule],
  declarations: [MinimalAppComponent],
  bootstrap: [MinimalAppComponent]
})
export class MinimalAppModule {}
