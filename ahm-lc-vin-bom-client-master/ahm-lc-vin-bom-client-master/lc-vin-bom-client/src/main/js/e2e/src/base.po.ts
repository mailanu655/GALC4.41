import { browser, by, element, ElementFinder, protractor } from 'protractor';

let EC = protractor.ExpectedConditions;
const BROWSER_TIMEOUT = 25000;

export class BasePageObject {
  waitForText(element: ElementFinder, text: string) {
      return browser.wait(
        EC.textToBePresentInElement(element, text),
        BROWSER_TIMEOUT
      );
  }

  waitForElement(el: ElementFinder) {
      browser.wait(EC.presenceOf(el), BROWSER_TIMEOUT);
  }

  navigateToHome() {
    return browser.get('./#/');
  }

  getTitleText() {
    return element(by.className('title'));
  }

  

}
