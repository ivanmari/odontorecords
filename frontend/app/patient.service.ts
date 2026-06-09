import { Injectable }     from '@angular/core';
import { Response } from '@angular/http';
import { Http, Headers, RequestOptions } from '@angular/http';
import { Observable }     from 'rxjs/Observable';

// Statics
import 'rxjs/add/observable/throw';

// Operators
import 'rxjs/add/operator/catch';
import 'rxjs/add/operator/debounceTime';
import 'rxjs/add/operator/distinctUntilChanged';
import 'rxjs/add/operator/map';
import 'rxjs/add/operator/switchMap';
import 'rxjs/add/operator/toPromise';

import { Patient } from './patient'
import { PatientBasicInfo } from './patientbasic'
import { MouthData } from './mouthdata'
import { Practice } from './practice'
import { Charge } from './charge'
import { Installment } from './installment'

@Injectable()
export class PatientService{
	//private baseUrl = 'http://192.168.43.84:8080';
	private baseUrl = 'http://127.0.0.1:8080';
	private patientsUrl = '/patients';
	private patientUrl = '/patient';
	private headers: Headers;
	private options: RequestOptions;
	
	
	constructor (private http: Http) {
		this.headers = new Headers();
		this.createAuthorizationHeader(this.headers);
		this.options = new RequestOptions({ headers: this.headers });
	}

	//TODO Sacar esta pass hardcoded
	createAuthorizationHeader(headers: Headers) {
		headers.append('Authorization', 'Basic ' +
		btoa('greg:turnquist')); 
	}
	
	private extractData(res: Response) {
		let body = res.json();
		console.log("Service: Received data from server");
		console.log(body)
		return body;
	}
	
	private extractHttpResponseCode(res: Response): boolean {
		console.log("Service: Extracting Http Code");
		console.log(res.ok);
		return res.ok;
	}
	
	public getAllPatients (): Observable<PatientBasicInfo[]> {
		console.log("Getting all patients");

		return this.http.get(this.baseUrl + this.patientsUrl, { headers: this.headers })
					.map(res => this.extractData(res));
                    
	}
  
  	public getPatients (lastName : String): Observable<PatientBasicInfo[]> {
		console.log("Getting patients with surname " + lastName);

		return this.http.get(this.baseUrl + this.patientUrl + "?name=" + lastName, { headers: this.headers })
                   .map(res => this.extractData(res));
                    
	}
	
	public getPatient(id : string): Observable<Patient> {
		console.log("Getting patient with id: " + id);
					
		return this.http.get(this.baseUrl + this.patientUrl + "/" + id, { headers: this.headers })
						.map(res => this.extractData(res))
						.catch(this.handleError);
						
	}

	public getMouth(id : string): Observable<MouthData> {
		console.log("Getting patient mouth with id: " + id);
					
		return this.http.get(this.baseUrl + this.patientUrl + "/" + id + "/graphmouth", { headers: this.headers })
						.map(res => this.extractData(res))
						.catch(this.handleError);
						
	}
	
	public addFacePractice(practice: Practice, patientId: string, tooth:number, face: string){
		console.log("Adding face practice to tooth:" + tooth + " face: " + face + "that belongs to patient with id: " + patientId);
		let headers = new Headers({ 'Content-Type': 'application/json' });
		this.createAuthorizationHeader(headers);
		headers.append('Accept', 'application/json');
		let options = new RequestOptions({ headers: headers });
		
		return this.http.post(this.baseUrl + this.patientUrl + "/" + patientId + "/" + tooth + "/" + face + "/practice", practice, options)
						.map(res => this.extractHttpResponseCode(res))
						.catch(this.handleError);
	}
	
	public deleteFacePractice(practiceId: string, patientId: string, tooth:number, face: string) {
		console.log("Deleting face practice from tooth:" + tooth + " face: " + face + "that belongs to patient with id: " + patientId);
		
		return this.http.delete(this.baseUrl + this.patientUrl + "/" + patientId + "/" + tooth + "/" + face + "/practice" + practiceId, this.options)
						.map(res => this.extractHttpResponseCode(res))
						.catch(this.handleError);		
	}
	
	public updateFacePractice(practice: Practice, patientId: string, tooth:number, face: string) {
		console.log("Updating face practice for tooth: " + tooth + " face: " + face + ", that belongs to patient with id: " + patientId);
		console.log(practice);

		let headers = new Headers({ 'Content-Type': 'application/json' });
		this.createAuthorizationHeader(headers);
		headers.append('Accept', 'application/json');
		let options = new RequestOptions({ headers: headers });
		
		return this.http.put(this.baseUrl + this.patientUrl + "/" + patientId + "/" + tooth + "/" + face + "/practice", practice, options)
						.map(res => this.extractHttpResponseCode(res))
						.catch(this.handleError);
	}
	
	public getFacePractices(patientId: string, tooth:number, face: string): Observable<Practice> {
		console.log("Getting face practices for patient with id: " + patientId);
		
		return this.http.get(this.baseUrl + this.patientUrl + "/" + patientId + "/" + tooth + "/" + face + "/practices", { headers: this.headers })
						.map(res => this.extractData(res))
						.catch(this.handleError);						
	}

	public getFacePractice(patientId: string, tooth:number, face: string, practiceId: number): Observable<Practice> {
		console.log("Getting face practice: " + practiceId + " for patient with id: " + patientId);
	
		return this.http.get(this.baseUrl + this.patientUrl + "/" + patientId + "/" + tooth + "/" + face + "/practice" + practiceId, { headers: this.headers })
						.map(res => this.extractData(res))
						.catch(this.handleError);						
	}
	
	public getToothPractices(patientId: string, tooth:number): Observable<Map<string, Practice[]>> {
		console.log("Getting tooth practices for patient with id: " + patientId);

		return this.http.get(this.baseUrl + this.patientUrl + "/" + patientId + "/" + tooth + "/practices", { headers: this.headers })
						.map(res => this.extractData(res))
						.catch(this.handleError);		
	}
	
	public updatePatient(patient : Patient): Observable<boolean> {
		 
		let headers = new Headers({ 'Content-Type': 'application/json' });
		this.createAuthorizationHeader(headers);
		headers.append('Accept', 'application/json');
		let options = new RequestOptions({ headers: headers });
		
		console.log("Service: Updating patient : ");
		console.log(patient);
		
		return this.http.put(this.baseUrl + this.patientUrl ,  patient, options )
					.map(res => this.extractHttpResponseCode(res))
					.catch(this.handleError);
	}
	
	public addPatient(patient : Patient): Observable<boolean> {
		
		let headers = new Headers({ 'Content-Type': 'application/json' });
		this.createAuthorizationHeader(headers);
		headers.append('Accept', 'application/json');
		let options = new RequestOptions({ headers: headers });
		
		console.log("Service: Adding patient: ");
		console.log(patient);
		
		return this.http.post(this.baseUrl + this.patientUrl ,  patient, options )
					.map(res => this.extractHttpResponseCode(res))
					.catch(this.handleError);
	}
	
	public delPatient(patientId : string): Observable<boolean> {
		
		let headers = new Headers({ 'Content-Type': 'application/json' });
		this.createAuthorizationHeader(headers);
		headers.append('Accept', 'application/json');
		let options = new RequestOptions({ headers: headers });
		
		console.log("Service: Deleting patient: " + patientId);
		
		return this.http.delete(this.baseUrl + this.patientUrl + "/" + patientId , options )
					.map(res => this.extractHttpResponseCode(res))
					.catch(this.handleError);
	}
	
	public getCharges(patientId: string){
	}
	
	public addCharge(patientId: string, charge: Charge) {
	}
	
	public updateCharge(patientId: string, charge: Charge) {
	}
	
	public deleteCharge(patientId: string, chargeId: string) {
	}
	
	public getInstallments(patientID: string){
	}
	
	public addInstallment(patientId: string, installment: Installment) {
	}
	
	public updateInstallment(patientId: string, installment: Installment) {
	}
	
	public deleteInstallment(patientId: string, installmentId: string) {
	}
	
	private handleError (error: any) { 
		console.log("Handle ERROR called");
		let errMsg = (error.message) ? error.message :
		error.status ? `${error.status} - ${error.statusText}` : 'Server error';
		console.log(errMsg); // log to console instead
		return Observable.throw(errMsg);
  }
  
}