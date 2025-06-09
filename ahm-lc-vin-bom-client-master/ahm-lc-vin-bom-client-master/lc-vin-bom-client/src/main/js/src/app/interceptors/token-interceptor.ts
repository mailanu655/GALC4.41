import { HttpEvent, HttpHandler, HttpInterceptor, HttpRequest, HttpXsrfTokenExtractor } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { Observable } from "rxjs";
import { SecurityService } from '../services/security.service';

const URL_WILDCARD: string = "/**";

@Injectable()
export class TokenInterceptor implements HttpInterceptor {

    constructor(private securityService: SecurityService, private tokenExtractor: HttpXsrfTokenExtractor) {
    }

    intercept(request: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {

        if (this.securityService.isUseSecurity() && this.securityService.isRestSecurityEnabled()) {
            if (this.isSecure(request.url, request.method)) {
                const authToken = this.securityService.getToken() || "";
                //const token = this.tokenExtractor.getToken() as string;
                request = request.clone({
                    setHeaders: {
                        "Authorization": "Bearer " + authToken
                    }
                });
                //console.log(request.method + " " + request.url)
                //console.log("authToken: " + JSON.stringify(authToken));
            }
        }
        return next.handle(request);
    }

    isSecure(url: string, method: string): boolean {

        let resources = this.securityService.secureRestResources;
        let secure: boolean = false;
        if (resources) {
            for (let resource of resources) {
                let urlPattern = resource.url;
                if (this.isUrlMatched(url, urlPattern) && this.isMethodMatched(method, resource.methods)) {
                    secure = true;
                    break;
                }
            }
        }
        return secure;
    }

    isUrlMatched(url: string, urlPattern: string): boolean {
        if (urlPattern.endsWith(URL_WILDCARD)) {
            urlPattern = urlPattern.substr(0, urlPattern.length - URL_WILDCARD.length);
            if (url.startsWith(urlPattern)) {
                return true;
            }
        } else if (url == urlPattern) {
            return true;
        }
        return false;
    }

    isMethodMatched(method: string, methods: string[]): boolean {
        if (!methods || methods.length == 0) {
            return true;
        } else if (methods.indexOf(method) > -1) {
            return true;
        }
        return false;
    }
}