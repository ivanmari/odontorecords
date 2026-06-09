import { Component,EventEmitter, Input, SimpleChanges } from '@angular/core';
import { OnInit } from '@angular/core';

import { PatientService } from './patient.service'

@Component({
  selector: 'tooth',
  
    template: `
	<g  id={{tooth}} [attr.transform]={{translation}} (click)="onToothClick()">
		<polygon points="5,5 	15,5 	15,15 	5,15" [attr.fill]="getFaceColor(tooth, 'Oclusal')" stroke="navy" stroke-width="0.5" id="C" opacity="1" (click)="onFaceClick('Oclusal')"></polygon>
		<polygon points="0,0 	20,0 	15,5 	5,5" [attr.fill]="getFaceColor(tooth, 'Vestibular')" stroke="navy" stroke-width="0.5" id="T" opacity="1"></polygon>
		<polygon points="5,15 	15,15 	20,20 	0,20" [attr.fill]="getFaceColor(tooth, 'Lingual')" stroke="navy" stroke-width="0.5" id="B" opacity="1"></polygon>
		<polygon points="15,5 	20,0 	20,20 	15,15" [attr.fill]="getFaceColor(tooth, 'Mesial')" stroke="navy" stroke-width="0.5" id="R" opacity="1"></polygon>
		<polygon points="0,0 	5,5 	5,15 	0,20" [attr.fill]="getFaceColor(tooth, 'Distal')" stroke="navy" stroke-width="0.5" id="L" opacity="1"></polygon>
		<text x="6" y="30" stroke="navy" fill="navy" stroke-width="0.1" style="font-size: 6pt;font-weight:normal">{{tooth}}</text>
	</g>

  `,
    providers: [PatientService]
})

export class ToothView
{
	@Input()
	tooth: number;
	
	@Input()
	translation: number;
	
}