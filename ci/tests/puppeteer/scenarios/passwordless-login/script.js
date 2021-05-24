const puppeteer = require('puppeteer');
const assert = require('assert');
const url = require('url');
const cas = require('../../cas.js');

(async () => {
    const browser = await puppeteer.launch(cas.browserOptions());
    const page = await browser.newPage();
    
    await page.goto("https://localhost:8443/cas/login");

    let pswd = await page.$('#password');
    assert(pswd == null);

    let uid = await page.$('#username');
    assert("none" === await uid.evaluate(el => el.getAttribute("autocapitalize")))
    assert("false" === await uid.evaluate(el => el.getAttribute("spellcheck")))
    assert("username" === await uid.evaluate(el => el.getAttribute("autocomplete")))
    
    await page.type('#username', "casuser");
    await page.keyboard.press('Enter');
    await page.waitForNavigation();

    // await page.waitForTimeout(10000)

    const header = await page.$eval('#login h3', el => el.innerText)
    console.log(header)
    assert(header === "Provide Token")

    const sub = await page.$eval('#login p', el => el.innerText)
    console.log(sub)
    assert(sub.startsWith("Please provide the security token sent to you"));

    let span = await page.$('#token');
    assert(await span.boundingBox() != null);
    
    await browser.close();
})();
