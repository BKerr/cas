const puppeteer = require('puppeteer');
const assert = require('assert');
const cas = require('../../cas.js');

(async () => {
    const browser = await puppeteer.launch(cas.browserOptions());
    const page = await browser.newPage();
    await page.goto("https://localhost:8443/cas/login");

    await page.waitForTimeout(2000)

    let element = await page.$('#forgotPasswordLink');
    const link = await page.evaluate(element => element.textContent, element);
    console.log(link)
    assert(link === "Reset your password")

    await cas.click(page, "#forgotPasswordLink")
    await page.waitForTimeout(1000)

    element = await page.$('#reset #fm1 h3');
    let header = await page.evaluate(element => element.textContent, element);
    console.log(header)
    assert(header === "Reset your password")
    
    let uid = await page.$('#username');
    assert(await uid.boundingBox() != null);

    await page.type('#username', "casuser");
    await page.keyboard.press('Enter');
    await page.waitForNavigation();

    await page.waitForTimeout(1000)

    element = await page.$('div .banner-danger p');
    header = await page.evaluate(element => element.textContent, element);
    console.log(header)
    assert(header === "reCAPTCHA validation failed.")

    await browser.close();
})();

