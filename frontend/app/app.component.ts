import { Component,EventEmitter	 } from '@angular/core';
import { OnInit } from '@angular/core';

import { PatientSelect } from './patient-select.component'


@Component({
  selector: 'my-app',
  
  template: `
  <h3> App comp list</h3>
  
	<div>	<patient-select>Loading... </patient-select></div>

  `
})

export class AppComponent implements OnInit {

	ngOnInit(): void {				
	}
}
