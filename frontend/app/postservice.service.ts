import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError } from 'rxjs/operators';

import { Post } from './post'

@Injectable()
export class PostService{
	private postUrl = 'https://jsonplaceholder.typicode.com/posts';

	constructor (private http: HttpClient) {}

	public getPosts(): Observable<Post[]> {
		return this.http.get<Post[]>(this.postUrl).pipe(
			catchError(err => {
				console.error('PostService error', err);
				return throwError(() => err);
			})
		);
	}
}
