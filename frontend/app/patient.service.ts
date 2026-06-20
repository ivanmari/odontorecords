import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError, map } from 'rxjs/operators';

import { Patient } from './patient';
import { PatientBasicInfo } from './patientbasic';
import { MouthData, ToothFace } from './mouthdata';
import { Practice } from './practice';
import { Charge } from './charge';
import { Installment } from './installment';

@Injectable({
  providedIn: 'root'
})
export class PatientService {
  private baseUrl = '';
  private patientsUrl = '/patients';
  private patientUrl = '/patient';

  constructor(private http: HttpClient) {}

  public getAllPatients(): Observable<PatientBasicInfo[]> {
    console.log("Getting all patients");
    return this.http.get<any>(this.baseUrl + this.patientsUrl)
      .pipe(map(response => response.content));
  }

  public getPatients(lastName: string): Observable<PatientBasicInfo[]> {
    console.log("Getting patients with surname " + lastName);
    const params = new HttpParams().set('name', lastName);
    return this.http.get<any>(this.baseUrl + this.patientUrl, { params })
      .pipe(map(response => response.content));
  }

  public getPatient(id: string): Observable<Patient> {
    console.log("Getting patient with id: " + id);
    return this.http.get<Patient>(this.baseUrl + this.patientUrl + "/" + id)
      .pipe(catchError(this.handleError));
  }

  public getMouth(id: string): Observable<MouthData> {
    console.log("Getting patient mouth with id: " + id);
    return this.http.get<MouthData>(this.baseUrl + this.patientUrl + "/" + id + "/graphmouth")
      .pipe(catchError(this.handleError));
  }

  public addFacePractice(practice: Practice, patientId: string, tooth: number, face: string): Observable<any> {
    console.log(`Adding face practice to tooth: ${tooth} face: ${face} for patient: ${patientId}`);
    return this.http.post(this.baseUrl + this.patientUrl + "/" + patientId + "/" + tooth + "/" + face + "/practice", practice, { observe: 'response' })
      .pipe(
        map(res => res.ok),
        catchError(this.handleError)
      );
  }

  public updateToothFacesStatus(patientId: string, toothId: number, faces: ToothFace[], date?: string, planned?: boolean): Observable<boolean> {
    console.log(`Updating multiple faces for tooth ${toothId} for patient ${patientId}`);
    const events = faces.map(f => ({
      toothId: toothId,
      face: f.faceName,
      filled: f.filled,
      planned: planned !== undefined ? planned : f.planned,
      date: date || new Date().toISOString()
    }));
    return this.http.put(this.baseUrl + this.patientUrl + "/" + patientId + "/tooth/" + toothId + "/faces", events, { observe: 'response' })
      .pipe(
        map(res => res.ok),
        catchError(this.handleError)
      );
  }

  public deleteFacePractice(practiceId: string, patientId: string, tooth: number, face: string): Observable<boolean> {
    console.log(`Deleting face practice from tooth: ${tooth} face: ${face} for patient: ${patientId}`);
    return this.http.delete(this.baseUrl + this.patientUrl + "/" + patientId + "/" + tooth + "/" + face + "/practice" + practiceId, { observe: 'response' })
      .pipe(
        map(res => res.ok),
        catchError(this.handleError)
      );
  }

  public updateFacePractice(practice: Practice, patientId: string, tooth: number, face: string): Observable<boolean> {
    console.log(`Updating face practice for tooth: ${tooth} face: ${face} for patient: ${patientId}`);
    return this.http.put(this.baseUrl + this.patientUrl + "/" + patientId + "/" + tooth + "/" + face + "/practice", practice, { observe: 'response' })
      .pipe(
        map(res => res.ok),
        catchError(this.handleError)
      );
  }

  public getFacePractices(patientId: string, tooth: number, face: string): Observable<Practice> {
    return this.http.get<Practice>(this.baseUrl + this.patientUrl + "/" + patientId + "/" + tooth + "/" + face + "/practices")
      .pipe(catchError(this.handleError));
  }

  public getToothPractices(patientId: string, tooth: number): Observable<{[face: string]: Practice[]}> {
    return this.http.get<{[face: string]: Practice[]}>(this.baseUrl + this.patientUrl + "/" + patientId + "/" + tooth + "/practices")
      .pipe(catchError(this.handleError));
  }

  public getPatientPractices(patientId: string): Observable<Practice[]> {
    return this.http.get<any>(this.baseUrl + this.patientUrl + "/" + patientId + "/practices")
      .pipe(
        map(response => response.content),
        catchError(this.handleError)
      );
  }

  public updatePatient(patient: Patient): Observable<boolean> {
    console.log("Updating patient:", patient);
    return this.http.put(this.baseUrl + this.patientUrl, patient, { observe: 'response' })
      .pipe(
        map(res => res.ok),
        catchError(this.handleError)
      );
  }

  public addPatient(patient: Patient): Observable<boolean> {
    console.log("Adding patient:", patient);
    return this.http.post(this.baseUrl + this.patientUrl, patient, { observe: 'response' })
      .pipe(
        map(res => res.ok),
        catchError(this.handleError)
      );
  }

  public addGeneralPractice(patientId: string, practice: Practice, affectedPieces: string): Observable<boolean> {
    console.log(`Adding general practice for patient: ${patientId}`);
    const body = {
        patientId: patientId,
        code: practice.code,
        deliveryDate: practice.deliveryDate || new Date().toISOString(),
        price: practice.price || 0,
        comments: practice.comments || '',
        preexisting: false,
        done: practice.done,
        affectedPieces: affectedPieces
    };
    return this.http.post(this.baseUrl + this.patientUrl + "/" + patientId + "/practice", body, { observe: 'response' })
      .pipe(
        map(res => res.ok),
        catchError(this.handleError)
      );
  }

  public delPatient(patientId: string): Observable<boolean> {
    console.log("Deleting patient:", patientId);
    return this.http.delete(this.baseUrl + this.patientUrl + "/" + patientId, { observe: 'response' })
      .pipe(
        map(res => res.ok),
        catchError(this.handleError)
      );
  }

  public updateToothStatus(patientId: string, toothId: number, status: string, planned: boolean, date?: string): Observable<boolean> {
    console.log(`Updating tooth ${toothId} status to ${status} for patient ${patientId}`);
    let params = new HttpParams().set('status', status).set('planned', planned.toString());
    if (date) params = params.set('date', date);
    return this.http.put(this.baseUrl + this.patientUrl + "/" + patientId + "/tooth/" + toothId + "/status", null, { params, observe: 'response' })
      .pipe(
        map(res => res.ok),
        catchError(this.handleError)
      );
  }

  public updateToothFaceStatus(patientId: string, toothId: number, faceName: string, filled: boolean, planned: boolean, date?: string): Observable<boolean> {
    console.log(`Updating tooth ${toothId} face ${faceName} status for patient ${patientId}`);
    let params = new HttpParams().set('filled', filled.toString()).set('planned', planned.toString());
    if (date) params = params.set('date', date);
    return this.http.put(this.baseUrl + this.patientUrl + "/" + patientId + "/tooth/" + toothId + "/face/" + faceName, null, { params, observe: 'response' })
      .pipe(
        map(res => res.ok),
        catchError(this.handleError)
      );
  }

  public getCharges(patientId: string): Observable<Charge[]> {
    return this.http.get<any>(this.baseUrl + this.patientUrl + "/" + patientId + "/account/charges")
      .pipe(
        map(response => response.content || response),
        catchError(this.handleError)
      );
  }

  public getInstallments(patientId: string): Observable<Installment[]> {
    return this.http.get<any>(this.baseUrl + this.patientUrl + "/" + patientId + "/account/installments")
      .pipe(
        map(response => response.content || response),
        catchError(this.handleError)
      );
  }

  public addCharge(patientId: string, charge: Partial<Charge>): Observable<boolean> {
    return this.http.post(this.baseUrl + this.patientUrl + "/" + patientId + "/account/charges", charge, { observe: 'response' })
      .pipe(
        map(res => res.ok),
        catchError(this.handleError)
      );
  }

  public addInstallment(patientId: string, installment: Partial<Installment>): Observable<boolean> {
    return this.http.post(this.baseUrl + this.patientUrl + "/" + patientId + "/account/installments", installment, { observe: 'response' })
      .pipe(
        map(res => res.ok),
        catchError(this.handleError)
      );
  }

  public getBalance(patientId: string): Observable<number> {
    return this.http.get<any>(this.baseUrl + this.patientUrl + "/" + patientId + "/balance")
      .pipe(
        map(res => res.balance),
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
