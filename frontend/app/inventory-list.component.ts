import { Component, OnInit } from '@angular/core';
import { InventoryService } from './inventory.service';
import { DentalSupply, DentalSupplyCategory } from './inventory';
import { MatSnackBar } from '@angular/material/snack-bar';

@Component({
  selector: 'inventory-list',
  template: `
    <div class="inventory-container">
      <mat-card>
        <mat-card-header>
          <mat-card-title>Inventory Management</mat-card-title>
        </mat-card-header>
        <mat-card-content>
          <div class="actions">
            <button mat-raised-button color="primary" (click)="showAddForm = !showAddForm">
              {{ showAddForm ? 'Cancel' : 'Add New Supply' }}
            </button>
          </div>

          <div *ngIf="showAddForm" class="add-form">
            <h3>Add New Supply</h3>
            <mat-form-field appearance="outline">
              <mat-label>Name</mat-label>
              <input matInput [(ngModel)]="newSupply.name">
            </mat-form-field>

            <mat-form-field appearance="outline">
              <mat-label>Category</mat-label>
              <mat-select [(ngModel)]="newSupply.category">
                <mat-option *ngFor="let cat of categories" [value]="cat">{{cat}}</mat-option>
              </mat-select>
            </mat-form-field>

            <mat-form-field appearance="outline">
              <mat-label>Quantity</mat-label>
              <input matInput type="number" [(ngModel)]="newSupply.quantity">
            </mat-form-field>

            <mat-form-field appearance="outline">
              <mat-label>Purchase Cost</mat-label>
              <input matInput type="number" [(ngModel)]="newSupply.purchaseCost">
            </mat-form-field>

            <mat-form-field appearance="outline">
              <mat-label>Uses per Unit</mat-label>
              <input matInput type="number" [(ngModel)]="newSupply.usesPerUnit">
            </mat-form-field>

            <button mat-raised-button color="accent" (click)="saveNewSupply()">Save</button>
          </div>

          <table mat-table [dataSource]="supplies" class="mat-elevation-z8 inventory-table">
            <ng-container matColumnDef="name">
              <th mat-header-cell *matHeaderCellDef> Name </th>
              <td mat-cell *matCellDef="let supply"> {{supply.name}} </td>
            </ng-container>

            <ng-container matColumnDef="category">
              <th mat-header-cell *matHeaderCellDef> Category </th>
              <td mat-cell *matCellDef="let supply"> {{supply.category}} </td>
            </ng-container>

            <ng-container matColumnDef="quantity">
              <th mat-header-cell *matHeaderCellDef> Quantity </th>
              <td mat-cell *matCellDef="let supply">
                <div *ngIf="editingSupplyId !== supply.id">{{supply.quantity}}</div>
                <mat-form-field *ngIf="editingSupplyId === supply.id" class="small-input">
                  <input matInput type="number" [(ngModel)]="supply.quantity">
                </mat-form-field>
              </td>
            </ng-container>

            <ng-container matColumnDef="purchaseCost">
              <th mat-header-cell *matHeaderCellDef> Cost </th>
              <td mat-cell *matCellDef="let supply">
                <div *ngIf="editingSupplyId !== supply.id">{{supply.purchaseCost | currency}}</div>
                <mat-form-field *ngIf="editingSupplyId === supply.id" class="small-input">
                  <input matInput type="number" [(ngModel)]="supply.purchaseCost">
                </mat-form-field>
              </td>
            </ng-container>

            <ng-container matColumnDef="usesPerUnit">
              <th mat-header-cell *matHeaderCellDef> Uses/Unit </th>
              <td mat-cell *matCellDef="let supply">
                <div *ngIf="editingSupplyId !== supply.id">{{supply.usesPerUnit}}</div>
                <mat-form-field *ngIf="editingSupplyId === supply.id" class="small-input">
                  <input matInput type="number" [(ngModel)]="supply.usesPerUnit">
                </mat-form-field>
              </td>
            </ng-container>

            <ng-container matColumnDef="currentUses">
              <th mat-header-cell *matHeaderCellDef> Remaining Uses </th>
              <td mat-cell *matCellDef="let supply">
                {{supply.currentUses}}
              </td>
            </ng-container>

            <ng-container matColumnDef="actions">
              <th mat-header-cell *matHeaderCellDef> Actions </th>
              <td mat-cell *matCellDef="let supply">
                <button mat-icon-button *ngIf="editingSupplyId !== supply.id" (click)="startEdit(supply)">
                  <mat-icon>edit</mat-icon>
                </button>
                <button mat-icon-button *ngIf="editingSupplyId === supply.id" (click)="saveEdit(supply)">
                  <mat-icon>save</mat-icon>
                </button>
                <button mat-icon-button *ngIf="editingSupplyId === supply.id" (click)="cancelEdit()">
                  <mat-icon>cancel</mat-icon>
                </button>
              </td>
            </ng-container>

            <tr mat-header-row *matHeaderRowDef="displayedColumns"></tr>
            <tr mat-row *matRowDef="let row; columns: displayedColumns;"></tr>
          </table>
        </mat-card-content>
      </mat-card>
    </div>
  `,
  styles: [`
    .inventory-container {
      padding: 20px;
    }
    .inventory-table {
      width: 100%;
      margin-top: 20px;
    }
    .actions {
      margin-bottom: 20px;
    }
    .add-form {
      display: flex;
      flex-direction: column;
      gap: 10px;
      margin-bottom: 30px;
      padding: 20px;
      background-color: #f5f5f5;
      border-radius: 4px;
    }
    .small-input {
      width: 80px;
    }
  `]
})
export class InventoryListComponent implements OnInit {
  supplies: DentalSupply[] = [];
  displayedColumns: string[] = ['name', 'category', 'quantity', 'purchaseCost', 'usesPerUnit', 'currentUses', 'actions'];
  categories = Object.values(DentalSupplyCategory);

  showAddForm = false;
  newSupply: DentalSupply = {
    name: '',
    category: DentalSupplyCategory.Resin,
    quantity: 0,
    purchaseCost: 0,
    usesPerUnit: 1
  };

  editingSupplyId: string | null = null;

  constructor(private inventoryService: InventoryService, private snackBar: MatSnackBar) {}

  ngOnInit() {
    this.loadSupplies();
  }

  loadSupplies() {
    this.inventoryService.getAllSupplies().subscribe(data => {
      this.supplies = data;
    });
  }

  saveNewSupply() {
    this.inventoryService.addOrUpdateSupply(this.newSupply).subscribe(() => {
      this.snackBar.open('Supply added successfully', 'Close', { duration: 3000 });
      this.showAddForm = false;
      this.newSupply = {
        name: '',
        category: DentalSupplyCategory.Resin,
        quantity: 0,
        purchaseCost: 0,
        usesPerUnit: 1
      };
      this.loadSupplies();
    });
  }

  startEdit(supply: DentalSupply) {
    this.editingSupplyId = supply.id || null;
  }

  saveEdit(supply: DentalSupply) {
    this.inventoryService.addOrUpdateSupply(supply).subscribe(() => {
      this.snackBar.open('Supply updated successfully', 'Close', { duration: 3000 });
      this.editingSupplyId = null;
      this.loadSupplies();
    });
  }

  cancelEdit() {
    this.editingSupplyId = null;
    this.loadSupplies();
  }
}
