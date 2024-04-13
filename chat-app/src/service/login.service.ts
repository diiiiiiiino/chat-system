// login.service.ts

import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class LoginService {

  private apiUrl = 'http://localhost:8080/login';

  constructor(private http: HttpClient) { }

  login(loginId: string, password: string, userAgent: string): Observable<any> {
    const body = { loginId, password, userAgent };
    return this.http.post<any>(this.apiUrl, body);
  }
}
