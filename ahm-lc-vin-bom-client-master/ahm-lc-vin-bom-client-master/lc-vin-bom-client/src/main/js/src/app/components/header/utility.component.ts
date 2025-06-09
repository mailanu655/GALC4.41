import { Component, OnInit } from '@angular/core';

import { Resource } from 'src/app/services/constants';

@Component({
  template: '<div>Test</div>'
})
export class UtilityComponent implements OnInit {

  options = {
      autoClose: false,
      keepAfterRouteChange: false
  };

  constructor() { }

  ngOnInit(): void {
  }


}
