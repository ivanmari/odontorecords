
//Pasar al backend el closing date en el getMouth y que devuelva un model view de la mouth a mostrar, sin historia previa. DONE
//Se muestra mas de una practica en una pieza? 						
//Puede haber dos codigos que pinten una misma cara, uno antiguo y otro nuevo?
//En este caso como se diferencian ambas practicas?

import { Component,EventEmitter, Input, SimpleChanges } from '@angular/core';
import { OnInit } from '@angular/core';

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
		<svg  *ngIf="isRemoved(tooth)" id="tooth-ausente-red"><line x1="0" y1="0" x2="20" y2="20" stroke = "red" stroke-width="1"></line><line x1="20" y1="0" x2="0" y2="20" stroke="red" stroke-width="1"></line></svg>
	</g>
	</g> 			
  </svg>

    <tooth-details  [toothId]="selectedTooth"  [patientId]="patientId"> </tooth-details>
  
  `,
    providers: [PatientService]
})
export class Mouth
{
	@Input()
	patientId: string;
	
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
	
	onToothClick(tooth: number) : void
	{
		this.selectedTooth = tooth;
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
				}
			}
		}
	}
	
	getTooth(toothId: number): Tooth
	{
		let tooth: Tooth = null;

		if(this.mouthData)
		{
			let index: number;
			
			if(toothId < 21)
			{
				index = toothId - 10;
			}
			else if(toothId < 31)
			{
				index = toothId - 20 + 8;
			}
			else if(toothId < 41)
			{
				index = toothId - 30 + 16;
			}
			else
			{
				index = toothId - 40 + 24;
			}
			
			index = index - 1;
			
			//console.log("toothId = " + toothId + " index = " + index);
			let pTeethMap: Map<number, Tooth> = this.mouthData.permanentTeeth;	
			tooth = pTeethMap[index.toString()];
			
			return tooth;
		}
/*	
TODO scope problem here for tooth var

		console.log("getTooth returning: ");
		console.log(tooth);
*/	
		return tooth;
	}
	
	isRemoved(toothId: number): boolean
	{
		let removed: boolean = false;
		let tooth: Tooth = this.getTooth(toothId);
		if(tooth)
		{
			if("Removed" === tooth["status"])
			{
				removed = true;
			}
		}
		
		return removed;
	}
		
	getFaceColor(toothNum: number, faceToPaint: string): string
	{
		let color: string = "white";
		//console.log("GetFaceColor for tooth: " + toothNum);
		if(this.mouthData){	
			let faces: ToothFace[] = this.getTooth(toothNum)["faces"];
			
			//console.log("Looking for face: " + faceToPaint);
			for( let face of faces)
			{
				if(faceToPaint === face["name"])
				{
					color = face["color"];
					//console.log("Found: " + face["name"] + " with color: " + color + " toothNum= " toothNum);
				}
			}
		}
		
		return color;
	}
	
	
	constructor(private patientService : PatientService)
	{
	}
}
