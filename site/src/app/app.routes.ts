import { RouterModule, Routes } from '@angular/router';
import { MainComponent } from './component/main/main.component';
import { NgModule } from '@angular/core';
import { AboutComponent } from './component/about/about.component';
import { SignupComponent } from './component/signup/signup.component';

export const routes: Routes = [
  { path: '', component: MainComponent, pathMatch: 'full'},
  { path: 'main', component: MainComponent },
  { path: 'about', component: AboutComponent },
  { path: 'signup', component: SignupComponent}
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
