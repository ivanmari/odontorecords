export class Patient
{
	id         : string;
	dni        : number;
	firstName  : string;
	lastName   : string;
	city       : string;
	street     : string;
	streetNum  : string;
	apartment  : string;
	socialSecOrg: string;
	socialId   : string;
	birthday   : string;
	gender     : string;
	phone      : string;
	comments   : string;
	firstVisit : string;
	balance    : number;
    
	fromJson() : void {
	}
}

