import { browser, element, by, protractor, promise, ElementFinder } from 'protractor'
import { BasePageObject } from './base.po';

export class HomePage extends BasePageObject {
  
    navigateToHome() {
        return browser.get('./#/');
    }

    getWelcomeMsgText() {
        return element(by.className('welcome-msg')).getText();
    }

    getPartAssignmentLink() {
        return element(by.className('part-assignment__link'));
    }
    
}
