import { RouterModule, Routes } from '@angular/router';
import { FileParserComponent } from './file-parser/file-parser.component';
import { RecordsComponent } from './records/records.component';
import { NgModule } from '@angular/core';
import { UploadComponent } from './upload/upload.component';
import { LoginComponent } from './login/login.component';
import { NavComponent } from './nav/nav.component';
import { RegisterComponent } from './register/register.component';

export const routes: Routes = [
    {path: 'login', component: LoginComponent},
    {path: 'register', component: RegisterComponent},
    {
        path: '', 
        component: NavComponent,
        children: [
            {path: 'parseFile', component: FileParserComponent},
            {path: 'records', component: RecordsComponent},
            {path: 'upload', component: UploadComponent}
        ]
    },
    
    {path: '', redirectTo: 'login', pathMatch: 'full' }, 
    { path: '**', redirectTo: 'login' } // Redirect to login for unknown routes
];

@NgModule({
    imports: [RouterModule.forRoot(routes)],
    exports: [RouterModule]
})

export class AppRouterModule {}
