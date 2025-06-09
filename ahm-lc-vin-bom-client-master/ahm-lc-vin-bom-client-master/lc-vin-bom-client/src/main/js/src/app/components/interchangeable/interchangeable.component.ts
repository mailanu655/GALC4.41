import { Component, OnInit, ViewChild } from '@angular/core';
import { MatSort } from '@angular/material/sort';
import { MatTableDataSource } from '@angular/material/table';
import { Title } from '@angular/platform-browser';
import { InterchangeableService } from 'src/app/services/interchangeable.service';
import { AlertService } from 'src/app/alert/alert.service';
import { ModelPart } from 'src/app/models/model-part';
import { Message } from 'src/app/models/message';
import { FormControl } from '@angular/forms';

@Component({
  selector: 'app-interchangeable',
  templateUrl: './interchangeable.component.html',
  styleUrls: ['./interchangeable.component.css']
})
export class InterchangeableComponent implements OnInit {

  public loading = false;
  letSystemName: string;
  letSystemNameList: string[];
  productSpecWildcardList: string[];
  list: ModelPart[];
  newList: ModelPart[] = [];
  displayedColumns = ['productSpecWildcard', 'letSystemName', 'dcPartNumber', 'active'];
  @ViewChild(MatSort) sort: MatSort;
  dataSource: MatTableDataSource<ModelPart>;
  selectedRow: number;
  selectRowData: ModelPart = new ModelPart();
  highlightedRow: Number;
  selectRow: Function;
  highlightBomPart: Function;

  productSpecWildcardFilter = new FormControl('');
  letSystemNameFilter = new FormControl('');
  dcPartNumberFilter = new FormControl('');
  activeFilter = new FormControl('');

  filterValues = {
    productSpecWildcard: '',
    letSystemName: '',
    dcPartNumber: '',
    active: '',
  };

  public message: Message = { type: null, message: null };
  messageOptionsAutoClose = {
    autoClose: false,
    keepAfterRouteChange: false
  };

  messageOptions = {
    autoClose: false,
    keepAfterRouteChange: false
  };

  constructor(
    private titleService: Title,
    private interchangeableService: InterchangeableService,
    protected alertService: AlertService,
  ) {
    
    this.titleService.setTitle("SUMS-VIN BOM Interchangeable");

    this.selectRow = function(index, row){
      this.alertService.clear();
      this.selectedRow = index;
      this.selectRowData = row;
    }

    this.highlightBomPart = function(index){
      this.highlightedRow = index;
    }
  }

  ngOnInit(): void {
    this.letSystemName = '';

    //this.populateLetSystemName();
    this.searchAll();

    this.productSpecWildcardFilter.valueChanges
    .subscribe(
      productSpecWildcard => {
        this.filterValues.productSpecWildcard = productSpecWildcard;
        this.dataSource.filter = JSON.stringify(this.filterValues);
      }
    )
    this.letSystemNameFilter.valueChanges
    .subscribe(
      letSystemName => {
        this.filterValues.letSystemName = letSystemName;
        this.dataSource.filter = JSON.stringify(this.filterValues);
      }
    )
    this.dcPartNumberFilter.valueChanges
    .subscribe(
      dcPartNumber => {
        this.filterValues.dcPartNumber = dcPartNumber;
        this.dataSource.filter = JSON.stringify(this.filterValues);
      }
    )
    this.activeFilter.valueChanges
    .subscribe(
      active => {
        this.filterValues.active = active;
        this.dataSource.filter = JSON.stringify(this.filterValues);
      }
    )
  }

  populateLetSystemName(): void {
    this.interchangeableService.findDistinctLetSystemName()
      .subscribe((letSystemNameList: string[]) => {

        this.letSystemNameList = letSystemNameList;
      },(error: any) => {
        
      });
  }

  search(letSystemName: string): void {
        if(letSystemName == null || letSystemName == '' || letSystemName == '-1') {
          this.newList = this.list;
        } else {
          this.newList.length = 0;
          this.list.forEach((p: ModelPart) => {
            if(p.letSystemName == letSystemName) {
              this.newList.push(p);
            }
          })
        }
        console.log(this.newList);
        this.dataSource = new MatTableDataSource(this.newList);
        this.dataSource.sort = this.sort;

  }

  searchAll(): void {
    this.loading = true;
    this.interchangeableService.search()
      .subscribe((list: ModelPart[]) => {
        this.list = list;
        var replaedNull = JSON.stringify(list).replace(/null/g, '""');
        this.list = JSON.parse(replaedNull);
        this.productSpecWildcardList = Array.from(new Set(this.list.map(ele=>ele.productSpecWildcard)));
        this.letSystemNameList = Array.from(new Set(this.list.map(ele=>ele.letSystemName)));

        this.dataSource = new MatTableDataSource(list);
        this.dataSource.sort = this.sort;
        this.dataSource.filterPredicate = this.createFilter();
        this.loading = false;
      },(error: any) => {
        this.loading = false;
      });
  }

  inactive(): void {
    console.log(this.selectRowData);
    this.alertService.clear();
    this.interchangeableService.inactive(this.selectRowData).subscribe(data => {
      console.log(data);
      this.searchAll(); 
      this.alertService.success('Changed status to Inactive', this.messageOptionsAutoClose);
    },(error: any) => {
      alert("Error changing status");
    });
  }

  active(): void {

  }

  createFilter(): (data: any, filter: string) => boolean {
    let filterFunction = function(data, filter): boolean {
      let searchTerms = JSON.parse(filter);
      console.log(searchTerms.active);
      console.log(data.active.toLowerCase());
      return data.productSpecWildcard.toLowerCase().indexOf(searchTerms.productSpecWildcard) !== -1
        && data.letSystemName.toLowerCase().indexOf(searchTerms.letSystemName) !== -1
        && data.dcPartNumber.toLowerCase().indexOf(searchTerms.dcPartNumber) !== -1
        && String(data.active).toLowerCase().indexOf(searchTerms.active) !== -1;
    }
    return filterFunction;
  }
}
