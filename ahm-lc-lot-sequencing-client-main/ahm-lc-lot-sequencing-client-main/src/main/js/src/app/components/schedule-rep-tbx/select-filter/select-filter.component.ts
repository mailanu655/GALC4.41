import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { FormGroup } from '@angular/forms';

@Component({
  selector: 'app-select-filter',
  templateUrl: './select-filter.component.html',
  styleUrls: ['./select-filter.component.css']
})
export class SelectFilterComponent implements OnInit {

  @Input() selectFilterForm: FormGroup;
  @Output() handleFilter = new EventEmitter<any>();
  @Input() filterOptions: any;

  
  ngOnInit(): void {}

}
