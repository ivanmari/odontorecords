import { Component, Inject, OnInit } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef, MatDialog } from '@angular/material/dialog';
import { PatientService } from './patient.service';
import { Tooth, ToothFace } from './mouthdata';
import { Practice } from './practice';
import { PracticeDialog } from './practice-dialog.component';
import { forkJoin, of, Observable } from 'rxjs';
import { switchMap } from 'rxjs/operators';

@Component({
  selector: 'tooth-edit-dialog',
  template: `
    <h2 mat-dialog-title>Edit Tooth {{data.tooth.toothNumber}}</h2>
    <mat-dialog-content>
      <div style="display: flex; flex-direction: row; gap: 20px;">
        <div class="svg-container">
          <svg viewBox="-5 -5 30 40" width="200" height="250">
            <g>
              <polygon points="5,5 15,5 15,15 5,15" [attr.fill]="getFaceColor('Oclusal')" stroke="navy" stroke-width="0.5" (click)="toggleFace('Oclusal')"></polygon>
              <polygon points="0,0 20,0 15,5 5,5" [attr.fill]="getFaceColor('Vestibular')" stroke="navy" stroke-width="0.5" (click)="toggleFace('Vestibular')"></polygon>
              <polygon points="5,15 15,15 20,20 0,20" [attr.fill]="getFaceColor('Lingual')" stroke="navy" stroke-width="0.5" (click)="toggleFace('Lingual')"></polygon>
              <polygon points="15,5 20,0 20,20 15,15" [attr.fill]="getFaceColor('Mesial')" stroke="navy" stroke-width="0.5" (click)="toggleFace('Mesial')"></polygon>
              <polygon points="0,0 5,5 5,15 0,20" [attr.fill]="getFaceColor('Distal')" stroke="navy" stroke-width="0.5" (click)="toggleFace('Distal')"></polygon>

              <g *ngIf="data.tooth.status === 'Removed'">
                <line x1="0" y1="0" x2="20" y2="20" [attr.stroke]="data.tooth.planned ? 'red' : 'blue'" stroke-width="1"></line>
                <line x1="20" y1="0" x2="0" y2="20" [attr.stroke]="data.tooth.planned ? 'red' : 'blue'" stroke-width="1"></line>
              </g>
              <circle *ngIf="data.tooth.status === 'Crown'" cx="10" cy="10" r="12" fill="none" [attr.stroke]="data.tooth.planned ? 'red' : 'blue'" stroke-width="1"></circle>
              <text *ngIf="data.tooth.status === 'Implant'" x="2" y="12" [attr.fill]="data.tooth.planned ? 'red' : 'blue'" style="font-size: 6px; font-weight: bold;">IMP</text>

              <line *ngIf="data.tooth.status === 'BridgeStart'" x1="10" y1="-5" x2="25" y2="-5" [attr.stroke]="data.tooth.planned ? 'red' : 'blue'" stroke-width="2"></line>
              <line *ngIf="data.tooth.status === 'BridgeIntermediate'" x1="-5" y1="-5" x2="25" y2="-5" [attr.stroke]="data.tooth.planned ? 'red' : 'blue'" stroke-width="2"></line>
              <line *ngIf="data.tooth.status === 'BridgeEnd'" x1="-5" y1="-5" x2="10" y2="-5" [attr.stroke]="data.tooth.planned ? 'red' : 'blue'" stroke-width="2"></line>
            </g>
          </svg>
        </div>

        <div style="flex: 1; display: flex; flex-direction: column; gap: 15px;">
          <mat-form-field appearance="fill" style="width: 100%;">
            <mat-label>Status</mat-label>
            <mat-select [(ngModel)]="data.tooth.status" (selectionChange)="onStatusChange()">
              <mat-option value="Healthy">Healthy</mat-option>
              <mat-option value="Filling">Filling</mat-option>
              <mat-option value="Removed">Removed</mat-option>
              <mat-option value="Implant">Implant</mat-option>
              <mat-option value="Crown">Crown</mat-option>
              <mat-option value="BridgeStart">Bridge Start</mat-option>
              <mat-option value="BridgeIntermediate">Bridge Intermediate</mat-option>
              <mat-option value="BridgeEnd">Bridge End</mat-option>
            </mat-select>
          </mat-form-field>

          <div *ngIf="data.tooth.status === 'Filling'">
            <h4 style="margin: 0 0 10px 0;">Affected Faces</h4>
            <div style="display: grid; grid-template-columns: 1fr 1fr; gap: 5px;">
              <mat-checkbox *ngFor="let face of data.tooth.faces" [(ngModel)]="face.filled">
                {{face.faceName}}
              </mat-checkbox>
            </div>
          </div>

          <div>
            <mat-radio-group [(ngModel)]="data.tooth.planned" style="display: flex; flex-direction: column; gap: 8px;">
              <mat-radio-button [value]="false">Existing (Blue)</mat-radio-button>
              <mat-radio-button [value]="true">Planned (Red)</mat-radio-button>
            </mat-radio-group>
          </div>
        </div>
      </div>
    </mat-dialog-content>
    <mat-dialog-actions align="end">
      <button mat-button (click)="onCancel()">Cancel</button>
      <button mat-raised-button color="primary" (click)="saveStatus()">Save Status</button>
      <button mat-raised-button color="accent" (click)="saveAsPractice()">Save as Practice</button>
    </mat-dialog-actions>
  `,
  styles: [`
    .svg-container {
      background: #f8f9fa;
      padding: 15px;
      border-radius: 8px;
      border: 1px solid #dee2e6;
    }
    polygon:hover {
      opacity: 0.7;
      cursor: pointer;
    }
  `]
})
export class ToothEditDialog implements OnInit {
  constructor(
    public dialogRef: MatDialogRef<ToothEditDialog>,
    @Inject(MAT_DIALOG_DATA) public data: { tooth: Tooth, patientId: string },
    private patientService: PatientService,
    private dialog: MatDialog
  ) {
    console.log("Opening ToothEditDialog for tooth:", data.tooth.toothNumber);
  }

  ngOnInit() {
    if (!this.data.tooth.status) this.data.tooth.status = 'Healthy';
  }

  getFaceColor(faceName: string): string {
    if (!this.data.tooth.faces) return 'white';
    const face = this.data.tooth.faces.find(f => f.faceName === faceName);
    if (face && face.filled) {
      return this.data.tooth.planned || face.planned ? 'red' : 'blue';
    }
    return 'white';
  }

  toggleFace(faceName: string) {
    if (this.data.tooth.status !== 'Filling') {
       this.data.tooth.status = 'Filling';
    }
    if (!this.data.tooth.faces) return;
    const face = this.data.tooth.faces.find(f => f.faceName === faceName);
    if (face) {
      face.filled = !face.filled;
    }
  }

  onStatusChange() {
    if (this.data.tooth.status !== 'Filling') {
      this.data.tooth.faces.forEach(f => f.filled = false);
    }
  }

  onCancel() {
    this.dialogRef.close();
  }

  private applyOdontogramChanges(): Observable<any> {
    return this.patientService.updateToothStatus(
      this.data.patientId,
      this.data.tooth.toothNumber,
      this.data.tooth.status,
      this.data.tooth.planned
    ).pipe(
      switchMap(() => {
        if (this.data.tooth.status === 'Filling' && this.data.tooth.faces && this.data.tooth.faces.length > 0) {
          const faceUpdates = this.data.tooth.faces.map(face =>
            this.patientService.updateToothFaceStatus(
              this.data.patientId,
              this.data.tooth.toothNumber,
              face.faceName,
              face.filled,
              this.data.tooth.planned
            )
          );
          return forkJoin(faceUpdates);
        }
        return of(true);
      })
    );
  }

  saveStatus() {
    this.applyOdontogramChanges().subscribe({
      next: () => this.dialogRef.close(true),
      error: () => this.dialogRef.close(true)
    });
  }

  saveAsPractice() {
    const practice = new Practice();
    // Map tooth status to practice code
    practice.code = this.mapStatusToCode(this.data.tooth.status);
    practice.done = !this.data.tooth.planned;
    practice.deliveryDate = practice.done ? new Date().toISOString() : null;
    practice.price = 0;
    practice.comments = '';

    let affected = `${this.data.tooth.toothNumber}`;
    if (this.data.tooth.status === 'BridgeStart') {
      affected += 'BS';
    } else if (this.data.tooth.status === 'BridgeIntermediate') {
      affected += 'BI';
    } else if (this.data.tooth.status === 'BridgeEnd') {
      affected += 'BE';
    }

    if (this.data.tooth.status === 'Filling' && this.data.tooth.faces) {
      const faces = this.data.tooth.faces.filter(f => f.filled).map(f => f.faceName[0].toLowerCase()).join(':');
      if (faces) {
        affected += ` ${faces}`;
      }
    }

    const dialogRef = this.dialog.open(PracticeDialog, {
      width: '400px',
      data: { practice: JSON.parse(JSON.stringify(practice)) }
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        // Use the edited practice details
        const finalPractice = result as Practice;
        // Also update planned status based on 'done' flag
        this.data.tooth.planned = !finalPractice.done;

        this.patientService.addGeneralPractice(this.data.patientId, finalPractice, affected).pipe(
          switchMap(() => this.applyOdontogramChanges())
        ).subscribe({
          next: () => this.dialogRef.close(true),
          error: () => this.dialogRef.close(true)
        });
      }
    });
  }

  private mapStatusToCode(status: string): string {
    switch(status) {
        case 'Filling': return 'FillingFront'; // Simplified mapping
        case 'Removed': return 'Extraction';
        case 'Implant': return 'Implant';
        case 'Crown': return 'Crown';
        case 'BridgeStart':
        case 'BridgeIntermediate':
        case 'BridgeEnd': return 'Bridge';
        default: return 'Cleaning';
    }
  }
}
