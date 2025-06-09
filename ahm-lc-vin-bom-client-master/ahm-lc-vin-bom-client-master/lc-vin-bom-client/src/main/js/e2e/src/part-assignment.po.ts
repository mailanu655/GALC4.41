import { browser, element, by, protractor, promise, ElementFinder } from 'protractor'
import { BasePageObject } from './base.po';

export class PartAssignmentPage extends BasePageObject {
  

    getPartAssignmentPage() {
        return element(by.className('part-assignment'));
    }
    
}
