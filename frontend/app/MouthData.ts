
export class ToothFace
{
	name: string;
    color: string;
}

export class Tooth
{
	toothNumber: number;
    faces: ToothFace[];
}

export class MouthData
{
	temporaryTeeth: Map<number, Tooth>;
	permanentTeeth: Map<number, Tooth>;
}