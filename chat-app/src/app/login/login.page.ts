import { Component } from '@angular/core';
import { Router } from '@angular/router';
import {LoginService} from "../../service/login.service";

@Component({
  selector: 'app-login',
  templateUrl: './login.page.html',
  styleUrls: ['./login.page.scss'],
})
export class LoginPage {
  loginId: string;
  password: string;

  constructor(private router: Router,
              private loginService: LoginService) {
    this.loginId = "";
    this.password = "";
  }

  login() {
    this.loginService.login(this.loginId, this.password, navigator.userAgent).subscribe(
      response => {
        // 서버로부터 받은 응답 처리
        const jwtToken = response.headers.get('jwtToken');
        // JWT 토큰을 저장하거나 다른 작업을 수행
      },
      error => {
        // 오류 처리
        console.error('로그인 오류:', error);
      }
    );
  }
}
