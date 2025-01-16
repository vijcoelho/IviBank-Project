import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private apiUrl = 'http://localhost:8080/auth/signup';

  constructor(private http: HttpClient) { }

  signup(data: { [key: string]: string }): Observable<any> {
    return this.http.post<any>(this.apiUrl, data);
  }
}
