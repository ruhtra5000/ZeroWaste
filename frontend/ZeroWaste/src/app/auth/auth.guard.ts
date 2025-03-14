import { CanActivateFn, Router, ActivatedRouteSnapshot } from '@angular/router';
import { inject } from '@angular/core';
import { AuthService } from './auth.service';

export const authGuard: CanActivateFn = (route: ActivatedRouteSnapshot) => {
  const authService = inject(AuthService);
  const router = inject(Router);
  const requiredRole: string | undefined = route.data?.['role'];

  if(!authService.isAuthenticated()) {
    return router.createUrlTree(['/login']);
  }

  if(requiredRole && !authService.hasRole(requiredRole)) {
    return router.createUrlTree(['/home'])
  }

  return true;
};
