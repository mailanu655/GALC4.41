import { browser, logging } from 'protractor';
import { by, element } from 'protractor';
import { HomePage } from './homepage.po';

describe('SUMS - Dashboard', () => {

  let homePage: HomePage = new HomePage();

  beforeEach(() => {
    homePage.navigateToHome();
  });

  it('should display Dashboard', async () => {
    await homePage.navigateToHome();
    expect(await homePage.getWelcomeMsgText()).toEqual('Welcome');
  });

  afterEach(async () => {
    // Assert that there are no errors emitted from the browser
    const logs = await browser.manage().logs().get(logging.Type.BROWSER);
    expect(logs).not.toContain(jasmine.objectContaining({
      level: logging.Level.SEVERE,
    } as logging.Entry));
  });
});
