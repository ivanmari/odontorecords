
//Pasar al backend el closing date en el getMouth y que devuelva un model view de la mouth a mostrar, sin historia previa. DONE
//Se muestra mas de una practica en una pieza? 						
//Puede haber dos codigos que pinten una misma cara, uno antiguo y otro nuevo?
//En este caso como se diferencian ambas practicas?

import { Component, EventEmitter, Input, SimpleChanges } from '@angular/core';
import { OnInit } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { ToothEditDialog } from './tooth-edit-dialog.component';

import { PatientService } from './patient.service'
import { Patient } from './patient'
import { MouthData } from './mouthdata'
import { Tooth } from './mouthdata'
import { ToothFace } from './mouthdata' 
import { ToothDetails } from './tooth-details.component'

@Component({
  selector: 'mouth',
  
  template: `
  <svg viewBox="20 -60 500 300"   preserveAspectRatio="xMidYMid meet">
  	<g transform="scale(1)" id="gmain" >
	<g  *ngFor="let tooth of teeth" id={{tooth}} [attr.transform]="getTranslation(tooth)" (click)="onToothClick(tooth)">
		<polygon points="5,5 	15,5 	15,15 	5,15" [attr.fill]="getFaceColor(tooth, 'Oclusal')" stroke="navy" stroke-width="0.5" id="C" opacity="1"></polygon>
		<polygon points="0,0 	20,0 	15,5 	5,5" [attr.fill]="getFaceColor(tooth, 'Vestibular')" stroke="navy" stroke-width="0.5" id="T" opacity="1"></polygon>
		<polygon points="5,15 	15,15 	20,20 	0,20" [attr.fill]="getFaceColor(tooth, 'Lingual')" stroke="navy" stroke-width="0.5" id="B" opacity="1"></polygon>
		<polygon points="15,5 	20,0 	20,20 	15,15" [attr.fill]="getFaceColor(tooth, 'Mesial')" stroke="navy" stroke-width="0.5" id="R" opacity="1"></polygon>
		<polygon points="0,0 	5,5 	5,15 	0,20" [attr.fill]="getFaceColor(tooth, 'Distal')" stroke="navy" stroke-width="0.5" id="L" opacity="1"></polygon>
		<text x="6" y="30" stroke="navy" fill="navy" stroke-width="0.1" style="font-size: 6pt;font-weight:normal">{{tooth}}</text>

		<g *ngIf="getStatus(tooth) === 'Removed'">
			<line x1="0" y1="0" x2="20" y2="20" [attr.stroke]="getToothColor(tooth)" stroke-width="1"></line>
			<line x1="20" y1="0" x2="0" y2="20" [attr.stroke]="getToothColor(tooth)" stroke-width="1"></line>
		</g>

		<circle *ngIf="getStatus(tooth) === 'Crown'" cx="10" cy="10" r="12" fill="none" [attr.stroke]="getToothColor(tooth)" stroke-width="1"></circle>

		<text *ngIf="getStatus(tooth) === 'Implant'" x="2" y="12" [attr.fill]="getToothColor(tooth)" style="font-size: 6px; font-weight: bold;">IMP</text>

		<line *ngIf="getStatus(tooth) === 'BridgeStart'" x1="10" y1="-5" x2="25" y2="-5" [attr.stroke]="getToothColor(tooth)" stroke-width="2"></line>
		<line *ngIf="getStatus(tooth) === 'BridgeIntermediate'" x1="-5" y1="-5" x2="25" y2="-5" [attr.stroke]="getToothColor(tooth)" stroke-width="2"></line>
		<line *ngIf="getStatus(tooth) === 'BridgeEnd'" x1="-5" y1="-5" x2="10" y2="-5" [attr.stroke]="getToothColor(tooth)" stroke-width="2"></line>
	</g>
	</g> 			
  </svg>

  `,
    providers: [PatientService]
})
export class Mouth
{
	@Input()
	patientId: string;

	@Input()
	editMode: boolean = false;
	
	mouthData: MouthData = null;
	
	/*
	draft
	Mapa de practica a svg picture. Para una pieza hay una lista de practicas que se han ido realizando en el tiempo.
	Identificar prioridades y como mostrar la historia.
	Ej. 
	1) Pieza con relleno que posteriormente se remueve. Tiene prioridad la remocion. Pasa de fill -> X
	2) Pieza con trat. conducto que se remueve y luego se realiza implante o perno y corona. Pasa de TC -> X -> I
	3) Pieza removida, posteriormente se realiza implante. Pasa de X -> I
	*/
	//practicePicture: Map<string, string>;
	
	removedTooth: boolean = false;
	selectedTooth:number = 0;
	tooth = 1;
	
	
	teeth = [18,17,16,15,14,13,12,11,21,22,23,24,25,26,27,28,
			   48,47,46,45,44,43,42,41,31,32,33,34,35,36,37,38];
	
	//Calculates distance between teeth
	getTranslation(d:number):string
	{
		let separation:number = 25;
		let row:number = 0;
		let index:number = 0;
		
		
		if(d < 31)
		{
			if (d < 21)
			{
				index = 19 - d;
			}
			else
			{
				index = d - 10;
			}
		}
		else
		{
			row = 50;
			if(d > 38)
			{
				index = 49 - d;
			}
			else
			{
				index = d - 20;
			}
		}
		//console.log (`translate ( ${index*separation} ,${row}))`);
		return `translate ( ${index*separation} ,${row})`;
	}
	
	onToothClick(toothNum: number) : void
	{
		this.selectedTooth = toothNum;
		if (this.editMode) {
			const tooth = this.getTooth(toothNum);
			if (tooth) {
				const dialogRef = this.dialog.open(ToothEditDialog, {
					width: '500px',
					data: { tooth: JSON.parse(JSON.stringify(tooth)), patientId: this.patientId }
				});

				dialogRef.afterClosed().subscribe(result => {
					if (result) {
						this.getMouth(this.patientId);
					}
				});
			}
		}
	}

	getMouth(patientId : string){
			this.patientService.getMouth(patientId)
							.subscribe(
							m => this.mouthData = m);
	}
	
	ngOnChanges(changes : SimpleChanges) {
		for(let propName in changes)
		{
			if(propName === "patientId" && changes[propName]) {
				let patientId = changes[propName].currentValue;
				
				if(patientId)
				{
					this.getMouth(this.patientId);
					this.selectedTooth = 11;
				} else {
					this.mouthData = null;
				}
			}
		}
	}
	
	getTooth(toothId: number): Tooth
	{
		if(this.mouthData)
		{
			// Check permanent teeth
			if (this.mouthData.permanentTeeth && this.mouthData.permanentTeeth[toothId.toString()]) {
				return this.mouthData.permanentTeeth[toothId.toString()];
			}
			// Check temporary teeth
			if (this.mouthData.temporaryTeeth && this.mouthData.temporaryTeeth[toothId.toString()]) {
				return this.mouthData.temporaryTeeth[toothId.toString()];
			}
		}
		return null;
	}
	
	getStatus(toothId: number): string
	{
		let tooth: Tooth = this.getTooth(toothId);
		return (tooth && tooth.status) ? tooth.status : 'Healthy';
	}

	getToothColor(toothId: number): string {
		let tooth: Tooth = this.getTooth(toothId);
		if (tooth && tooth.planned) {
			return 'blue';
		}
		return 'red';
	}
		
	getFaceColor(toothNum: number, faceToPaint: string): string
	{
		let color: string = "white";
		if(this.mouthData){	
			let tooth = this.getTooth(toothNum);
			if (!tooth || !tooth.faces) return color;

			let faces: ToothFace[] = tooth.faces;
			
			for( let face of faces)
			{
				if(faceToPaint === face.faceName)
				{
					if (face.filled) {
						return face.planned ? 'blue' : 'red';
					}
					color = face.color || "white";
				}
			}
		}
		
		return color;
	}
	
	
	constructor(private patientService : PatientService, private dialog: MatDialog)
	{
	}
}
