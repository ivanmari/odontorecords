export enum DentalSupplyCategory {
    Resin = 'Resin',
    Anesthesia = 'Anesthesia',
    DrillBit = 'DrillBit',
    Acrylic = 'Acrylic'
}

export interface DentalSupply {
    id?: string;
    name: string;
    category: DentalSupplyCategory;
    purchaseCost: number;
    quantity: number;
}
