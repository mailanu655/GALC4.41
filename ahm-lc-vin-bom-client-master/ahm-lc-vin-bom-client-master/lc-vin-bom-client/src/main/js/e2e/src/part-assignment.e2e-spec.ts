import { browser, logging } from 'protractor';
import { by, element } from 'protractor';
import { HomePage } from './homepage.po';
import { PartAssignmentPage } from './part-assignment.po';

describe('SUMS - Part Assignment', () => {

  let homePage: HomePage = new HomePage();
  let partAssignmentPage: PartAssignmentPage = new PartAssignmentPage();

  beforeEach(() => {
    homePage.navigateToHome();
  });


  it('should display Part Assignment screen on click of part-assignment link from Menu', async () => {
    await homePage.navigateToHome();
    await homePage.getPartAssignmentLink().click();
    expect(await partAssignmentPage.getPartAssignmentPage()).toBeTruthy;
  });

  afterEach(async () => {
    // Assert that there are no errors emitted from the browser
    const logs = await browser.manage().logs().get(logging.Type.BROWSER);
    expect(logs).not.toContain(jasmine.objectContaining({
      level: logging.Level.SEVERE,
    } as logging.Entry));
  });
});
