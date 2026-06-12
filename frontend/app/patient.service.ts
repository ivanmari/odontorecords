import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError, map } from 'rxjs/operators';

import { Patient } from './patient';
import { PatientBasicInfo } from './patientbasic';
import { MouthData } from './mouthdata';
import { Practice } from './practice';
import { Charge } from './charge';
import { Installment } from './installment';

@Injectable({
  providedIn: 'root'
})
export class PatientService {
  private baseUrl = 'http://127.0.0.1:8080';
  private patientsUrl = '/patients';
  private patientUrl = '/patient';

  constructor(private http: HttpClient) {}

  private getHeaders(): HttpHeaders {
    return new HttpHeaders({
      'Authorization': 'Basic ' + btoa('greg:turnquist'),
      'Content-Type': 'application/json',
      'Accept': 'application/json'
    });
  }

  public getAllPatients(): Observable<PatientBasicInfo[]> {
    console.log("Getting all patients");
    return this.http.get<any>(this.baseUrl + this.patientsUrl, { headers: this.getHeaders() })
      .pipe(map(res => res.content));
  }

  public getPatients(lastName: string): Observable<PatientBasicInfo[]> {
    console.log("Getting patients with surname " + lastName);
    const params = new HttpParams().set('name', lastName);
    return this.http.get<any>(this.baseUrl + this.patientUrl, { headers: this.getHeaders(), params })
      .pipe(map(res => res.content));
  }

  public getPatient(id: string): Observable<Patient> {
    console.log("Getting patient with id: " + id);
    return this.http.get<Patient>(this.baseUrl + this.patientUrl + "/" + id, { headers: this.getHeaders() })
      .pipe(catchError(this.handleError));
  }

  public getMouth(id: string): Observable<MouthData> {
    console.log("Getting patient mouth with id: " + id);
    return this.http.get<MouthData>(this.baseUrl + this.patientUrl + "/" + id + "/graphmouth", { headers: this.getHeaders() })
      .pipe(catchError(this.handleError));
  }

  public addFacePractice(practice: Practice, patientId: string, tooth: number, face: string): Observable<any> {
    console.log(`Adding face practice to tooth: ${tooth} face: ${face} for patient: ${patientId}`);
    return this.http.post(this.baseUrl + this.patientUrl + "/" + patientId + "/" + tooth + "/" + face + "/practice", practice, { headers: this.getHeaders(), observe: 'response' })
      .pipe(
        map(res => res.ok),
        catchError(this.handleError)
      );
  }

  public deleteFacePractice(practiceId: string, patientId: string, tooth: number, face: string): Observable<boolean> {
    console.log(`Deleting face practice from tooth: ${tooth} face: ${face} for patient: ${patientId}`);
    return this.http.delete(this.baseUrl + this.patientUrl + "/" + patientId + "/" + tooth + "/" + face + "/practice" + practiceId, { headers: this.getHeaders(), observe: 'response' })
      .pipe(
        map(res => res.ok),
        catchError(this.handleError)
      );
  }

  public updateFacePractice(practice: Practice, patientId: string, tooth: number, face: string): Observable<boolean> {
    console.log(`Updating face practice for tooth: ${tooth} face: ${face} for patient: ${patientId}`);
    return this.http.put(this.baseUrl + this.patientUrl + "/" + patientId + "/" + tooth + "/" + face + "/practice", practice, { headers: this.getHeaders(), observe: 'response' })
      .pipe(
        map(res => res.ok),
        catchError(this.handleError)
      );
  }

  public getFacePractices(patientId: string, tooth: number, face: string): Observable<Practice> {
    return this.http.get<Practice>(this.baseUrl + this.patientUrl + "/" + patientId + "/" + tooth + "/" + face + "/practices", { headers: this.getHeaders() })
      .pipe(catchError(this.handleError));
  }

  public getToothPractices(patientId: string, tooth: number): Observable<{[face: string]: Practice[]}> {
    return this.http.get<{[face: string]: Practice[]}>(this.baseUrl + this.patientUrl + "/" + patientId + "/" + tooth + "/practices", { headers: this.getHeaders() })
      .pipe(catchError(this.handleError));
  }

  public updatePatient(patient: Patient): Observable<boolean> {
    console.log("Updating patient:", patient);
    return this.http.put(this.baseUrl + this.patientUrl, patient, { headers: this.getHeaders(), observe: 'response' })
      .pipe(
        map(res => res.ok),
        catchError(this.handleError)
      );
  }

  public addPatient(patient: Patient): Observable<boolean> {
    console.log("Adding patient:", patient);
    return this.http.post(this.baseUrl + this.patientUrl, patient, { headers: this.getHeaders(), observe: 'response' })
      .pipe(
        map(res => res.ok),
        catchError(this.handleError)
      );
  }

  public delPatient(patientId: string): Observable<boolean> {
    console.log("Deleting patient:", patientId);
    return this.http.delete(this.baseUrl + this.patientUrl + "/" + patientId, { headers: this.getHeaders(), observe: 'response' })
      .pipe(
        map(res => res.ok),
        catchError(this.handleError)
      );
  }

  private handleError(error: any) {
    console.error("An error occurred", error);
    let errMsg = (error.message) ? error.message :
      error.status ? `${error.status} - ${error.statusText}` : 'Server error';
    return throwError(() => new Error(errMsg));
  }
}
