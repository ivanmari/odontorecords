import { Injectable }     from '@angular/core';
import { Http, Response } from '@angular/http';
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

import { Post } from './post'

@Injectable()
export class PostService{
	private postUrl = 'https://jsonplaceholder.typicode.com/posts';
	
	constructor (private http: Http) {}

	public getPosts (): Observable<Post[]> {
	return this.http.get(this.postUrl)
                    .map(res => this.extractData(res));
                    
	}
  
	private extractData(res: Response) {
    let body = res.json();
	console.log("Service: Received data from serve");
    return body.data;
	}
  
  private handleError (error: any) {    
		let errMsg = (error.message) ? error.message :
		error.status ? `${error.status} - ${error.statusText}` : 'Server error';
		console.error(errMsg); // log to console instead
		return Observable.throw(errMsg);
  }
  
}
