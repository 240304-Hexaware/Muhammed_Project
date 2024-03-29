import { ComponentFixture, TestBed } from '@angular/core/testing';

import { FileParserComponent } from './file-parser.component';

describe('FileParserComponent', () => {
  let component: FileParserComponent;
  let fixture: ComponentFixture<FileParserComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [FileParserComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(FileParserComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
