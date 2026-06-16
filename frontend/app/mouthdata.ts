export class ToothFace
{
	name: string;
	color: string;
	filled: boolean;
	planned: boolean;
}

export class Tooth
{
	toothNumber: number;
	faces: ToothFace[];
	status: string;
	planned: boolean;
}

export class MouthData
{
	temporaryTeeth: Map<number, Tooth>;
	permanentTeeth: Map<number, Tooth>;
}

