import { Component, Inject, OnInit } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { Practice } from './practice';

@Component({
  selector: 'practice-dialog',
  template: `
    <h2 mat-dialog-title>Practice Details</h2>
    <mat-dialog-content>
      <div layout="column" layout-gap="15px" style="padding-top: 10px;">
        <mat-form-field appearance="fill">
          <mat-label>Practice Code</mat-label>
          <mat-select [(ngModel)]="data.practice.code">
            <mat-option value="FillingFront">Filling (Front)</mat-option>
            <mat-option value="FillingBack">Filling (Back)</mat-option>
            <mat-option value="RootCanal">Root Canal</mat-option>
            <mat-option value="Extraction">Extraction</mat-option>
            <mat-option value="Implant">Implant</mat-option>
            <mat-option value="Crown">Crown</mat-option>
            <mat-option value="Bridge">Bridge</mat-option>
            <mat-option value="Cleaning">Cleaning</mat-option>
          </mat-select>
        </mat-form-field>

        <mat-form-field appearance="fill">
          <mat-label>Delivery Date</mat-label>
          <input matInput [matDatepicker]="picker" [(ngModel)]="deliveryDate">
          <mat-datepicker-toggle matSuffix [for]="picker"></mat-datepicker-toggle>
          <mat-datepicker #picker></mat-datepicker>
        </mat-form-field>

        <mat-form-field appearance="fill">
          <mat-label>Price</mat-label>
          <input matInput type="number" [(ngModel)]="data.practice.price">
          <span matPrefix>$&nbsp;</span>
        </mat-form-field>

        <mat-form-field appearance="fill">
          <mat-label>Comments</mat-label>
          <textarea matInput [(ngModel)]="data.practice.comments" rows="3"></textarea>
        </mat-form-field>

        <mat-checkbox [(ngModel)]="data.practice.done">
          Mark as Done (Completed)
        </mat-checkbox>
      </div>
    </mat-dialog-content>
    <mat-dialog-actions align="end">
      <button mat-button (click)="onCancel()">Cancel</button>
      <button mat-raised-button color="primary" (click)="onSave()">Confirm Practice</button>
    </mat-dialog-actions>
  `
})
export class PracticeDialog implements OnInit {
  deliveryDate: Date;

  constructor(
    public dialogRef: MatDialogRef<PracticeDialog>,
    @Inject(MAT_DIALOG_DATA) public data: { practice: Practice }
  ) {}

  ngOnInit() {
    if (this.data.practice.deliveryDate) {
      this.deliveryDate = new Date(this.data.practice.deliveryDate);
    } else {
      this.deliveryDate = new Date();
    }
  }

  onCancel() {
    this.dialogRef.close();
  }

  onSave() {
    if (this.deliveryDate) {
      this.data.practice.deliveryDate = this.deliveryDate.toISOString();
    }
    this.dialogRef.close(this.data.practice);
  }
}
