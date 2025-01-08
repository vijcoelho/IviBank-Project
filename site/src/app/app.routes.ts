import { RouterModule, Routes } from '@angular/router';
import { MainComponent } from './component/main/main.component';
import { NgModule } from '@angular/core';
import { AboutComponent } from './component/about/about.component';

export const routes: Routes = [
  { path: '', component: MainComponent, pathMatch: 'full'},
  { path: 'main', component: MainComponent },
  { path: 'about', component: AboutComponent },
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
