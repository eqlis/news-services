package com.news.producer.scraper;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.Playwright;
import lombok.Getter;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnProperty(name = "scraper.type", havingValue = "playwright")
public class PlaywrightBrowserProvider implements DisposableBean {
  private final Playwright playwright;

  @Getter
  private final Browser browser;

  public PlaywrightBrowserProvider() {
    this.playwright = Playwright.create();
    this.browser = playwright.chromium().launch(
      new BrowserType.LaunchOptions().setHeadless(true)
    );
  }

  @Override
  public void destroy() {
    browser.close();
    playwright.close();
  }
}
