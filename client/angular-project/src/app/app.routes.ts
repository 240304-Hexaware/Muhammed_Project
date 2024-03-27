import { RouterModule, Routes } from '@angular/router';
import { FileParserComponent } from './file-parser/file-parser.component';
import { RecordsComponent } from './records/records.component';
import { NgModule } from '@angular/core';
import { UploadComponent } from './upload/upload.component';

export const routes: Routes = [
    {path: 'parseFile', component: FileParserComponent},
    {path: 'records', component: RecordsComponent},
    {path: 'upload', component: UploadComponent},
];

@NgModule({
    imports: [RouterModule.forRoot(routes)],
    exports: [RouterModule]
})

export class AppRouterModule {}
